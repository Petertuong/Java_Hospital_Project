package ui;

import model.Patients.Patient;
import model.Treatment.Status;
import service.PersonService.PatientService;
import service.MultiService.AdmissionService;

import ui.util.TableUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class PatientController extends BaseController {

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

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAdmit;
    @FXML
    private Button btnDischarge;

    @FXML
    private Label lblMessage;

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

    private final PatientService patientService = new PatientService();
    private final AdmissionService admissionService = new AdmissionService();        // Data cho TableView
        private final ObservableList<Patient> data =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // ComboBox gender & status
        cbGender.setItems(FXCollections.observableArrayList("Male", "Female"));
        cbStatus.setItems(FXCollections.observableArrayList(
                "Waiting", "Admitted", "Discharged", "Under Observation"
        ));
        
        // Disable status ComboBox - status is managed by Admit/Discharge buttons only
        cbStatus.setDisable(true);
        cbStatus.setValue("Waiting"); // Always default to Waiting
        
        // Configure DatePicker for better user experience
        dpDOB.setEditable(true);
        dpDOB.setPromptText("dd/MM/yyyy");
        
        // Set DatePicker format to dd/MM/yyyy
        dpDOB.setConverter(new javafx.util.StringConverter<java.time.LocalDate>() {
            private final java.time.format.DateTimeFormatter dateFormatter = 
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(java.time.LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public java.time.LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return java.time.LocalDate.parse(string, dateFormatter);
                    } catch (java.time.format.DateTimeParseException e) {
                        // If parsing fails, try other common formats
                        try {
                            return java.time.LocalDate.parse(string, 
                                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        } catch (java.time.format.DateTimeParseException e2) {
                            System.err.println("Invalid date format: " + string + ". Please use dd/MM/yyyy format.");
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            }
        });

        // Mapping column -> Patient property using TableUtil
        TableUtil.setStringColumn(colName, Patient::getFullname);
        TableUtil.setStringColumn(colPhone, Patient::getPhoneNo);
        // Add StatusMapping import
        TableUtil.setStringColumn(colGender, p -> mapGenderCharToText(p.getGender()));
        TableUtil.setStringColumn(colSSN, Patient::getSSN);
        TableUtil.setNullableStringColumn(colDOB, p -> p.getDOB() == null ? "" : p.getDOB().toString());
        TableUtil.setStringColumn(colAddress, Patient::getAddress);
        TableUtil.setStringColumn(colEmergency, Patient::getEmergencyContact);
        TableUtil.setStringColumn(colStatus, p -> {
            String statusStr = p.getStatus();
            // Display exact status without any conversion
            return statusStr;
        });

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

    // Load from backend

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

    // Show details in form

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
        cbStatus.setDisable(true); // Always disabled - status managed by Admit/Discharge buttons

        updateButtonsState();
        lblMessage.setText("");
    }

    // Clear form

    private void clearForm() {
        txtName.clear();
        txtPhone.clear();
        cbGender.setValue(null);
        txtSSN.clear();
        dpDOB.setValue(null);
        txtAddress.clear();
        txtEmergency.clear();
        cbStatus.setValue("Waiting"); // Always default to Waiting
        cbStatus.setDisable(true); // Always disabled

        patientTable.getSelectionModel().clearSelection();
        updateButtonsState();
        lblMessage.setText("");
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    // Buttons enable/disable

    private void updateButtonsState() {
        boolean hasSelection = patientTable.getSelectionModel().getSelectedItem() != null;
        btnUpdate.setDisable(!hasSelection);
        btnDelete.setDisable(!hasSelection);
        
        // Enable admit/discharge based on patient status
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String status = selected.getStatus();
            // Only enable Admit button for Waiting or Discharged patients
            boolean canAdmit = "Waiting".equals(status) || "Discharged".equals(status) || "Under Observation".equals(status);
            // Only enable Discharge button for Admitted patients
            boolean canDischarge = "Admitted".equals(status);
            
            btnAdmit.setDisable(!canAdmit);
            btnDischarge.setDisable(!canDischarge);
        } else {
            btnAdmit.setDisable(true);
            btnDischarge.setDisable(true);
        }
    }

    // Validation

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

        // Status validation removed - always defaults to Waiting

        return null;
    }

    // UC03.1 – Add Patient (POST)

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
        // Status is always Waiting for new patients - managed by Admit/Discharge buttons
        p.setStatus(Status.Null); // Always Waiting

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

    // UC03.3 – Update Patient (PUT)

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
        // Status is NOT updated here - only via Admit/Discharge buttons

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

    // UC03.4 – Delete Patient (DELETE)

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

    // UC03.5 – Admit Patient 

    @FXML
    private void handleAdmitPatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No patient selected.");
            return;
        }

        String status = selected.getStatus();
        if ("Admitted".equals(status)) {
            lblMessage.setText("Patient is already admitted.");
            return;
        }

        // Only allow admission for Waiting, Discharged, or Under Observation patients
        if (!"Waiting".equals(status) && !"Discharged".equals(status) && !"Under Observation".equals(status)) {
            lblMessage.setText("Patient must be in Waiting, Discharged, or Under Observation status to be admitted.");
            return;
        }

        try {
            String result = admissionService.admitPatient(selected.getSSN());
            if ("admitted".equals(result)) {
                lblMessage.setText("Patient admitted successfully!");
                lblMessage.setTextFill(javafx.scene.paint.Color.GREEN);
                loadPatientsFromServer(); // Reload to show updated status
            } else if ("already admitted".equals(result)) {
                lblMessage.setText("Patient is already admitted.");
                lblMessage.setTextFill(javafx.scene.paint.Color.ORANGE);
            } else {
                lblMessage.setText("Failed to admit patient: " + result);
                lblMessage.setTextFill(javafx.scene.paint.Color.RED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Error during admission: " + e.getMessage());
            lblMessage.setTextFill(javafx.scene.paint.Color.RED);
            showErrorAlert("Admit Patient Error", 
                    "Cannot admit patient.\n\n" + e.getMessage());
        }
    }

    // UC03.6 – Discharge Patient

    @FXML
    private void handleDischargePatient() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No patient selected.");
            return;
        }

        String status = selected.getStatus();
        if (!"Admitted".equals(status)) {
            lblMessage.setText("Patient must be admitted to be discharged. Current status: " + status);
            return;
        }

        this.showDischargeOptions("Discharge Patient", 
            "Choose discharge option for " + selected.getFullname() + ":")
            .ifPresent(selectedStatus -> {
                try {
                    String dischargeResult = admissionService.dischargePatient(selected.getSSN(), selectedStatus);
                    if (dischargeResult.startsWith("discharged")) {
                        lblMessage.setText("Patient discharged successfully! Status: " + selectedStatus);
                        lblMessage.setTextFill(javafx.scene.paint.Color.GREEN);
                        loadPatientsFromServer(); // Reload to show updated status
                    } else {
                        lblMessage.setText("Failed to discharge patient: " + dischargeResult);
                        lblMessage.setTextFill(javafx.scene.paint.Color.RED);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    lblMessage.setText("Error during discharge: " + e.getMessage());
                    lblMessage.setTextFill(javafx.scene.paint.Color.RED);
                    showErrorAlert("Discharge Patient Error", 
                            "Cannot discharge patient.\n\n" + e.getMessage());
                }
            });
    }
    
    
    private Optional<Status> showDischargeOptions(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        
        // Create custom buttons
        ButtonType dischargeButton = new ButtonType("Discharge (Final)");
        ButtonType waitingButton = new ButtonType("Set to Waiting (Re-admission possible)");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(dischargeButton, waitingButton, cancelButton);
        
        alert.setContentText("Choose how to handle this patient discharge:");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent()) {
            if (result.get() == dischargeButton) {
                return Optional.of(Status.Discharge);
            } else if (result.get() == waitingButton) {
                return Optional.of(Status.Null); // Use Null which represents "Waiting"
            }
        }
        
        return Optional.empty(); // User cancelled
    }
}

