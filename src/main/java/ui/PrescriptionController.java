package ui;

import model.Treatment.Prescription;
import model.Facility.Medicine;
import model.Patients.Patient;
import model.Staff.Doctor;
import service.TreatmentService.PrescriptionService;
import service.FacilityService.MedicineService;
import service.PersonService.PatientService;
import service.PersonService.DoctorService;
import ui.util.TableUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PrescriptionController extends BaseController {

    @FXML
    private TextField txtTreatmentId;
    @FXML
    private TextField txtDrugId;
    @FXML
    private TextField txtDoctorId;
    @FXML
    private TextField txtSSN;
    @FXML
    private TextField txtDosagePerDay;
    @FXML
    private TextField txtNumberOfDay;
    @FXML
    private TextArea txtDescription;

    @FXML
    private Button btnAdd, btnUpdate, btnDelete, btnClear;

    @FXML
    private TableView<Prescription> tblPrescriptions;
    @FXML
    private TableColumn<Prescription, String> colTreatmentId;
    @FXML
    private TableColumn<Prescription, String> colDrugId;
    @FXML
    private TableColumn<Prescription, String> colDoctorId;
    @FXML
    private TableColumn<Prescription, String> colSSN;
    @FXML
    private TableColumn<Prescription, String> colDosagePerDay;
    @FXML
    private TableColumn<Prescription, String> colNumberOfDay;

    @FXML
    private Label lblMessage;

    private final ObservableList<Prescription> data = FXCollections.observableArrayList();
    private final PrescriptionService prescriptionService = new PrescriptionService();
    private final MedicineService medicineService = new MedicineService();
    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();

    @FXML
    public void initialize() {
        // ID không cho sửa
        if (txtTreatmentId != null) {
            txtTreatmentId.setEditable(false);
        }

        TableUtil.setStringColumn(colTreatmentId, p -> String.valueOf(p.getTreatmentID()));
        TableUtil.setStringColumn(colDrugId, p -> p.getMedicine() == null ? "" : String.valueOf(p.getMedicine().getDrugID()));
        TableUtil.setStringColumn(colDoctorId, p -> p.getDoctor() == null ? "" : String.valueOf(p.getDoctor().getSID()));
        TableUtil.setStringColumn(colSSN, p -> p.getPatient() == null ? "" : p.getPatient().getSSN());
        TableUtil.setStringColumn(colDosagePerDay, p -> String.valueOf(p.getDosagePerDay()));
        TableUtil.setStringColumn(colNumberOfDay, p -> String.valueOf(p.getNumberOfDay()));

        tblPrescriptions.setItems(data);

        loadPrescriptionsFromServer();

        tblPrescriptions.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showDetails(newSel)
        );

        updateButtons();
        if (lblMessage != null) lblMessage.setText("");
    }

    private void loadPrescriptionsFromServer() {
        try {
            java.util.ArrayList<Prescription> list = prescriptionService.listPrescription();
            data.setAll(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Load Error", "Cannot load prescriptions.\n\n" + e.getMessage());
        }
    }
    private void showDetails(Prescription p) {
        if (p == null) {
            clearForm();
        } else {
            if (txtTreatmentId != null) {
                txtTreatmentId.setText(String.valueOf(p.getTreatmentID()));
            }
            txtDrugId.setText(p.getMedicine() == null ? "" : String.valueOf(p.getMedicine().getDrugID()));
            txtDoctorId.setText(p.getDoctor() == null ? "" : String.valueOf(p.getDoctor().getSID()));
            txtSSN.setText(p.getPatient() == null ? "" : p.getPatient().getSSN());
            txtDosagePerDay.setText(String.valueOf(p.getDosagePerDay()));
            txtNumberOfDay.setText(String.valueOf(p.getNumberOfDay()));
            txtDescription.setText(p.getDescription() == null ? "" : p.getDescription());
        }

        updateButtons();
        if (lblMessage != null) lblMessage.setText("");
    }

    private void updateButtons() {
        boolean selected = tblPrescriptions.getSelectionModel().getSelectedItem() != null;
        btnUpdate.setDisable(!selected);
        btnDelete.setDisable(!selected);
    }

    private void clearForm() {
        if (txtTreatmentId != null) txtTreatmentId.clear();
        txtDrugId.clear();
        txtDoctorId.clear();
        txtSSN.clear();
        txtDosagePerDay.clear();
        txtNumberOfDay.clear();
        txtDescription.clear();
        tblPrescriptions.getSelectionModel().clearSelection();
        updateButtons();
        if (lblMessage != null) lblMessage.setText("");
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    private String validateForm() {
        String ssn = txtSSN.getText();
        if (ssn == null || ssn.trim().isEmpty()) {
            return "SSN is required.";
        }

        String dosageText = txtDosagePerDay.getText();
        if (dosageText == null || dosageText.trim().isEmpty() || !dosageText.trim().matches("\\d+")) {
            return "Dosage per day must be a non-negative integer.";
        }

        String daysText = txtNumberOfDay.getText();
        if (daysText == null || daysText.trim().isEmpty() || !daysText.trim().matches("\\d+")) {
            return "Number of days must be a non-negative integer.";
        }

        // drugId & doctorId có thể để trống (NULL)
        String drugText = txtDrugId.getText();
        if (drugText != null && !drugText.trim().isEmpty() && !drugText.trim().matches("\\d+")) {
            return "Drug ID must be a number if provided.";
        }

        String doctorText = txtDoctorId.getText();
        if (doctorText != null && !doctorText.trim().isEmpty() && !doctorText.trim().matches("\\d+")) {
            return "Doctor ID must be a number if provided.";
        }

        return null;
    }

    @FXML
    private void handleAddPrescription() {
        String error = validateForm();
        if (error != null) {
            if (lblMessage != null) lblMessage.setText(error);
            return;
        }

        Integer drugId = null;
        if (!txtDrugId.getText().trim().isEmpty()) {
            drugId = Integer.valueOf(txtDrugId.getText().trim());
        }

        Integer doctorId = null;
        if (!txtDoctorId.getText().trim().isEmpty()) {
            doctorId = Integer.valueOf(txtDoctorId.getText().trim());
        }

        String ssn = txtSSN.getText().trim();
        int dosage = Integer.parseInt(txtDosagePerDay.getText().trim());
        int days = Integer.parseInt(txtNumberOfDay.getText().trim());
        String desc = txtDescription.getText() == null ? "" : txtDescription.getText().trim();
        try {
            Medicine med = drugId == null ? null : medicineService.findMedicineByNo(drugId);
            Patient pat = patientService.findPatientById(ssn);
            if (pat == null) {
                showErrorAlert("Add Error", "Patient with SSN not found: " + ssn);
                return;
            }
            Doctor doc = doctorId == null ? null : doctorService.findDoctorByID(doctorId);

            Prescription pres = new Prescription(med, dosage, days, pat, doc, desc);
            Prescription created = prescriptionService.addPrescription(pres);
            if (created == null) {
                showErrorAlert("Add Error", "Cannot add prescription.");
                return;
            }
            data.add(created);
            tblPrescriptions.getSelectionModel().select(created);
            if (lblMessage != null) lblMessage.setText("Prescription added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Add Error", "Cannot add prescription.\n\n" + e.getMessage());
        }
    }

    @FXML
    private void handleUpdatePrescription() {
        Prescription selected = tblPrescriptions.getSelectionModel().getSelectedItem();
        if (selected == null) {
            if (lblMessage != null) lblMessage.setText("No prescription selected.");
            return;
        }

        String error = validateForm();
        if (error != null) {
            if (lblMessage != null) lblMessage.setText(error);
            return;
        }

        Integer drugId = null;
        if (!txtDrugId.getText().trim().isEmpty()) {
            drugId = Integer.valueOf(txtDrugId.getText().trim());
        }

        Integer doctorId = null;
        if (!txtDoctorId.getText().trim().isEmpty()) {
            doctorId = Integer.valueOf(txtDoctorId.getText().trim());
        }

        try {
            Medicine med = drugId == null ? null : medicineService.findMedicineByNo(drugId);
            Patient pat = patientService.findPatientById(txtSSN.getText().trim());
            if (pat == null) {
                showErrorAlert("Update Error", "Patient with SSN not found: " + txtSSN.getText().trim());
                return;
            }
            Doctor doc = doctorId == null ? null : doctorService.findDoctorByID(doctorId);

            selected.setMedicine(med);
            selected.setDPD(Integer.parseInt(txtDosagePerDay.getText().trim()));
            selected.setNOD(Integer.parseInt(txtNumberOfDay.getText().trim()));
            selected.setPatient(pat);
            selected.setDoctor(doc);
            selected.setDescription(txtDescription.getText() == null ? "" : txtDescription.getText().trim());

            Integer r = prescriptionService.changeDrug(selected);
            if (r == null || r < 0) {
                showErrorAlert("Update Error", "Cannot update prescription.");
                return;
            }
            loadPrescriptionsFromServer();
            if (lblMessage != null) lblMessage.setText("Prescription updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Update Error", "Cannot update prescription.\n\n" + e.getMessage());
        }
    }

    @FXML
    private void handleDeletePrescription() {
        Prescription selected = tblPrescriptions.getSelectionModel().getSelectedItem();
        if (selected == null) {
            if (lblMessage != null) lblMessage.setText("No prescription selected.");
            return;
        }

        this.showConfirm("Delete Prescription", "Delete this prescription?").ifPresent(res -> {
            if (res == ButtonType.OK) {
                try {
                    Integer r = prescriptionService.deletePrescription(selected.getTreatmentID());
                    if (r == null || r < 0) {
                        showErrorAlert("Delete Error", "Cannot delete prescription.");
                        return;
                    }
                    data.remove(selected);
                    clearForm();
                    if (lblMessage != null) lblMessage.setText("Prescription deleted successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorAlert("Delete Error", "Cannot delete prescription.\n\n" + e.getMessage());
                }
            }
        });
    }
    
}
