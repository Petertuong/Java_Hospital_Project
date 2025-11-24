package ui;

import model.Patients.Patient;
import model.Treatment.Status;
import service.PersonService.PatientService;

import ui.util.TableUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

/**
 * PatientController
 * UC03.x: Manage Patients (CRUD) – connected to backend
 */
public class PatientController extends BaseController {

    // ==== Form fields ====
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhone;
    @FXML
    private ComboBox<String> cbGender;
    @FXML
    private TextField txtSSN;
    @FXML
    private DatePicker dpDOB;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtEmergency;
    @FXML
    private ComboBox<String> cbStatus;

    // ==== Buttons ====
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;

    // ==== Message label ====
    @FXML
    private Label lblMessage;

    // ==== Table & columns ====
    @FXML
        private TableView<Patient> patientTable;
        @FXML
        private TableColumn<Patient, String> colName;
        @FXML
        private TableColumn<Patient, String> colPhone;
        @FXML
        private TableColumn<Patient, String> colGender;
        @FXML
        private TableColumn<Patient, String> colSSN;
        @FXML
        private TableColumn<Patient, String> colDOB;
        @FXML
        private TableColumn<Patient, String> colAddress;
        @FXML
        private TableColumn<Patient, String> colEmergency;
        @FXML
        private TableColumn<Patient, String> colStatus;

        // ==== Service talking to backend ====
        private final PatientService patientService = new PatientService();

        // Data cho TableView
        private final ObservableList<Patient> data =
            FXCollections.observableArrayList();

    // ---------------------------------------------------------
    // init
    // ---------------------------------------------------------
    @FXML
    public void initialize() {

        // ComboBox gender & status
        cbGender.setItems(FXCollections.observableArrayList("Male", "Female"));
        cbStatus.setItems(FXCollections.observableArrayList(
                "Admitted", "Discharged", "Under Observation", "Waiting"
        ));

        // Mapping column -> Patient property using TableUtil
        TableUtil.setStringColumn(colName, Patient::getFullname);
        TableUtil.setStringColumn(colPhone, Patient::getPhoneNo);
        TableUtil.setStringColumn(colGender, p -> mapGenderCharToText(p.getGender()));
        TableUtil.setStringColumn(colSSN, Patient::getSSN);
        TableUtil.setNullableStringColumn(colDOB, p -> p.getDOB() == null ? "" : p.getDOB().toString());
        TableUtil.setStringColumn(colAddress, Patient::getAddress);
        TableUtil.setStringColumn(colEmergency, Patient::getEmergencyContact);
        TableUtil.setStringColumn(colStatus, Patient::getStatus);

        patientTable.setItems(data);

        patientTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showPatientDetails(newSel)
        );

        lblMessage.setText("");
        updateButtonsState();

        // Load data từ backend
        loadPatientsFromServer();
    }

    
    private char mapGenderTextToChar(String text) {
    if ("Male".equals(text))   return 'M';
    if ("Female".equals(text)) return 'F';
    // fallback an toàn, không bao giờ gửi 'U' xuống DB nữa
    return 'M';
}


    private String mapGenderCharToText(char c) {
        if (c == 'M') return "Male";
        if (c == 'F') return "Female";
        return "Male";
    }

    // ---------------------------------------------------------
    // Load from backend
    // ---------------------------------------------------------
    private void loadPatientsFromServer() {
        try {
            java.util.ArrayList<Patient> list = patientService.listPatient();
            data.setAll(FXCollections.observableArrayList(list));
            lblMessage.setText("");
        } catch (Exception e) {
                e.printStackTrace();
                lblMessage.setText("Cannot load patients from server: " + e.getMessage());
                showErrorAlert("Load Patients Error",
                    "Cannot load patients from server.\n\n" + e.getMessage());
        }
        updateButtonsState();
    }

    // ---------------------------------------------------------
    // Show details in form
    // ---------------------------------------------------------
    private void showPatientDetails(Patient p) {
        if (p == null) {
            clearForm();
            return;
        }
        txtName.setText(p.getFullname());
        txtPhone.setText(p.getPhoneNo());
        cbGender.setValue(mapGenderCharToText(p.getGender()));
        txtSSN.setText(p.getSSN());

        java.sql.Date dobDate = p.getDOB();
        if (dobDate != null) {
            try {
                dpDOB.setValue(dobDate.toLocalDate()); // yyyy-MM-dd
            } catch (Exception e) {
                dpDOB.setValue(null);
            }
        } else {
            dpDOB.setValue(null);
        }

        txtAddress.setText(p.getAddress());
        txtEmergency.setText(p.getEmergencyContact());
        cbStatus.setValue(p.getStatus());

        updateButtonsState();
        lblMessage.setText("");
    }

    // ---------------------------------------------------------
    // Clear form
    // ---------------------------------------------------------
    private void clearForm() {
        txtName.clear();
        txtPhone.clear();
        cbGender.setValue(null);
        txtSSN.clear();
        dpDOB.setValue(null);
        txtAddress.clear();
        txtEmergency.clear();
        cbStatus.setValue(null);

        patientTable.getSelectionModel().clearSelection();
        updateButtonsState();
        lblMessage.setText("");
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    // ---------------------------------------------------------
    // Buttons enable/disable
    // ---------------------------------------------------------
    private void updateButtonsState() {
        boolean hasSelection = patientTable.getSelectionModel().getSelectedItem() != null;
        btnUpdate.setDisable(!hasSelection);
        btnDelete.setDisable(!hasSelection);
    }

    // ---------------------------------------------------------
    // Validation
    // ---------------------------------------------------------
    private String validateForm() {
        String name = txtName.getText();
        if (name == null || name.trim().isEmpty()) {
            return "Name is required.";
        }

        String phone = txtPhone.getText();
        if (phone == null || phone.trim().isEmpty()) {
            return "Phone is required.";
        }
        if (!phone.trim().matches("\\d+")) {
            return "Phone must contain digits only.";
        }

        String gender = cbGender.getValue();
        if (gender == null || gender.trim().isEmpty()) {
            return "Gender is required.";
        }

        String ssn = txtSSN.getText();
        if (ssn == null || ssn.trim().isEmpty()) {
            return "SSN is required.";
        }

        String status = cbStatus.getValue();
        if (status == null || status.trim().isEmpty()) {
            return "Status is required.";
        }

        return null;
    }

    // ---------------------------------------------------------
    // UC03.1 – Add Patient (POST)
    // ---------------------------------------------------------
    @FXML
    private void handleAddPatient() {
        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        String dobStr = null;
        if (dpDOB.getValue() != null) {
            // LocalDate.toString() => "yyyy-MM-dd" (chuẩn để backend convert SQL Date)
            dobStr = dpDOB.getValue().toString();
        }

        Patient p = new Patient();
        p.setFullname(txtName.getText().trim());
        p.setPhoneNo(txtPhone.getText().trim());
        p.setGender(mapGenderTextToChar(cbGender.getValue()));
        p.setSSN(txtSSN.getText().trim());
        if (dobStr != null) p.setDOB(java.sql.Date.valueOf(dobStr));
        p.setAddress(txtAddress.getText().trim());
        p.setEmergencyContact(txtEmergency.getText().trim());
        String statusText = cbStatus.getValue();
        if ("Admitted".equals(statusText)) p.setStatus(Status.Admit);
        else if ("Discharged".equals(statusText)) p.setStatus(Status.Discharge);
        else p.setStatus(Status.Null);

        try {
            patientService.addPatient(p);
            lblMessage.setText("Patient added successfully.");
            loadPatientsFromServer();   // reload để Table luôn sync với DB
        } catch (Exception e) {
                e.printStackTrace();
                lblMessage.setText("Failed to add patient: " + e.getMessage());
                showErrorAlert("Add Patient Error",
                    "Cannot add patient.\n\n" + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // UC03.3 – Update Patient (PUT)
    // ---------------------------------------------------------
    @FXML
    private void handleUpdatePatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No patient selected.");
            return;
        }

        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        String dobStr = null;
        if (dpDOB.getValue() != null) {
            dobStr = dpDOB.getValue().toString();
        }

        selected.setFullname(txtName.getText().trim());
        selected.setPhoneNo(txtPhone.getText().trim());
        selected.setGender(mapGenderTextToChar(cbGender.getValue()));
        selected.setSSN(txtSSN.getText().trim());
        if (dobStr != null) selected.setDOB(java.sql.Date.valueOf(dobStr));
        selected.setAddress(txtAddress.getText().trim());
        selected.setEmergencyContact(txtEmergency.getText().trim());
        String statusText2 = cbStatus.getValue();
        if ("Admitted".equals(statusText2)) selected.setStatus(Status.Admit);
        else if ("Discharged".equals(statusText2)) selected.setStatus(Status.Discharge);
        else selected.setStatus(Status.Null);

        try {
            patientService.updatePatientStatus(selected);
            lblMessage.setText("Patient updated successfully.");
            loadPatientsFromServer();
        } catch (Exception e) {
                e.printStackTrace();
                lblMessage.setText("Failed to update patient: " + e.getMessage());
                showErrorAlert("Update Patient Error",
                    "Cannot update patient.\n\n" + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // UC03.4 – Delete Patient (DELETE)
    // ---------------------------------------------------------
    @FXML
    private void handleDeletePatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No patient selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Patient");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this patient?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    patientService.deletePatient(selected.getSSN());
                    lblMessage.setText("Patient deleted successfully.");
                    loadPatientsFromServer();
                    clearForm();
                } catch (Exception e) {
                    e.printStackTrace();
                    lblMessage.setText("Failed to delete patient: " + e.getMessage());
                    showErrorAlert("Delete Patient Error",
                            "Cannot delete patient.\n\n" + e.getMessage());
                }
            }
        });
    }
}

