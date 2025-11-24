package ui;

import model.Staff.Doctor;
import service.PersonService.DoctorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DoctorController {

    // ====== Form fields ======
    @FXML
    private TextField txtId;               // read-only
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhone;
    @FXML
    private ComboBox<String> cbGender;
    @FXML
    private ComboBox<String> cbSpecialization;
    @FXML
    private TextField txtQualification;

    // ====== Buttons ======
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;

    // ====== Message ======
    @FXML
    private Label lblMessage;

    // ====== Table ======
    @FXML
    private TableView<Doctor> doctorTable;
    @FXML
    private TableColumn<Doctor, String> colId;
    @FXML
    private TableColumn<Doctor, String> colName;
    @FXML
    private TableColumn<Doctor, String> colPhone;
    @FXML
    private TableColumn<Doctor, String> colGender;
    @FXML
    private TableColumn<Doctor, String> colSpecialization;
    @FXML
    private TableColumn<Doctor, String> colQualification;

    // ====== Service & Data ======
    private final DoctorService doctorService = new DoctorService();
    private final ObservableList<Doctor> data = FXCollections.observableArrayList();

    // ---------------------------------------------------------
    // init
    // ---------------------------------------------------------
    @FXML
    public void initialize() {
        // ComboBox: gender
        cbGender.setItems(FXCollections.observableArrayList("Male", "Female"));

        // ComboBox: specialization – em chỉnh lại list này cho phù hợp
        cbSpecialization.setItems(FXCollections.observableArrayList(
                "Cardiology",
                "Neurology",
                "Oncology",
                "Pediatrics",
                "General Medicine"
        ));
        cbSpecialization.setEditable(true);
        // Mapping cột
        colId.setCellValueFactory(cd ->
            new SimpleStringProperty(String.valueOf(cd.getValue().getSID())));

        colName.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getFullname()));

        colPhone.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getPhoneNo()));

        colGender.setCellValueFactory(cd ->
                new SimpleStringProperty(mapGenderCharToText(cd.getValue().getGender())));

        colSpecialization.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getSpecialization()));

        colQualification.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().getQualification()));

        doctorTable.setItems(data);

        // Chọn 1 dòng -> show lên form
        doctorTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showDoctorDetails(newSel)
        );

        lblMessage.setText("");
        txtId.setEditable(false); // id chỉ hiển thị, không sửa tay
        updateButtonsState();

        // load dữ liệu ban đầu
        loadDoctorsFromServer();
    }

    // ---------------------------------------------------------
    // Map gender
    // ---------------------------------------------------------
    private char mapGenderTextToChar(String text) {
    if ("Female".equals(text)) {
        return 'F';
    }
    // Mặc định coi tất cả cái khác là Male
    return 'M';
}

private String mapGenderCharToText(char c) {
    if (c == 'F') return "Female";
    return "Male";   // mặc định
}

    // ---------------------------------------------------------
    // Load from backend
    // ---------------------------------------------------------
    private void loadDoctorsFromServer() {
        try {
            java.util.ArrayList<Doctor> serverData = doctorService.listDoctor();
            data.setAll(FXCollections.observableArrayList(serverData));
            lblMessage.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Cannot load doctors: " + e.getMessage());
            showErrorAlert("Load Doctors Error",
                    "Cannot load doctors from database.\n\n" + e.getMessage());
        }
        updateButtonsState();
    }

    // ---------------------------------------------------------
    // Show in form
    // ---------------------------------------------------------
    private void showDoctorDetails(Doctor d) {
        if (d == null) {
            clearForm();
            return;
        }

        txtId.setText(String.valueOf(d.getSID()));
        txtName.setText(d.getFullname());
        txtPhone.setText(d.getPhoneNo());
        cbGender.setValue(mapGenderCharToText(d.getGender()));
        cbSpecialization.setValue(d.getSpecialization());
        txtQualification.setText(d.getQualification());

        lblMessage.setText("");
        updateButtonsState();
    }

    // ---------------------------------------------------------
    // Clear form
    // ---------------------------------------------------------
    private void clearForm() {
        txtId.clear();
        txtName.clear();
        txtPhone.clear();
        cbGender.setValue(null);
        cbSpecialization.setValue(null);
        txtQualification.clear();

        doctorTable.getSelectionModel().clearSelection();
        lblMessage.setText("");
        updateButtonsState();
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    // ---------------------------------------------------------
    // Buttons enable/disable
    // ---------------------------------------------------------
    private void updateButtonsState() {
        boolean selected = doctorTable.getSelectionModel().getSelectedItem() != null;
        btnUpdate.setDisable(!selected);
        btnDelete.setDisable(!selected);
    }

    // ---------------------------------------------------------
    // Validation
    // ---------------------------------------------------------
    private String validateForm() {
        if (txtName.getText() == null || txtName.getText().trim().isEmpty()) {
            return "Name is required.";
        }

        String phone = txtPhone.getText();
        if (phone == null || phone.trim().isEmpty()) {
            return "Phone is required.";
        }
        if (!phone.trim().matches("\\d+")) {
            return "Phone must contain digits only.";
        }

        if (cbGender.getValue() == null || cbGender.getValue().trim().isEmpty()) {
            return "Gender is required.";
        }

        if (cbSpecialization.getValue() == null || cbSpecialization.getValue().trim().isEmpty()) {
            return "Specialization is required.";
        }

        if (txtQualification.getText() == null || txtQualification.getText().trim().isEmpty()) {
            return "Qualification is required.";
        }

        return null;
    }

    // ---------------------------------------------------------
    // Add Doctor
    // ---------------------------------------------------------
    @FXML
    private void handleAddDoctor() {
        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }
        Doctor d = new Doctor(txtName.getText().trim(), txtPhone.getText().trim(), mapGenderTextToChar(cbGender.getValue()), cbSpecialization.getValue(), txtQualification.getText().trim());
        try {
            doctorService.insertDoctor(d);
            lblMessage.setText("Doctor added successfully.");
            loadDoctorsFromServer(); // reload để lấy doctor_id mới từ DB
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Failed to add doctor: " + e.getMessage());
            showErrorAlert("Add Doctor Error",
                    "Cannot add doctor.\n\n" + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // Update Doctor
    // ---------------------------------------------------------
    @FXML
    private void handleUpdateDoctor() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No doctor selected.");
            return;
        }

        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        try {
            Doctor d = new Doctor(txtName.getText().trim(), txtPhone.getText().trim(), mapGenderTextToChar(cbGender.getValue()), selected.getSID(), cbSpecialization.getValue(), txtQualification.getText().trim());

            doctorService.updateDoctor(d);
            lblMessage.setText("Doctor updated successfully.");
            loadDoctorsFromServer();

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Failed to update doctor: " + e.getMessage());
            showErrorAlert("Update Doctor Error",
                    "Cannot update doctor.\n\n" + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // Delete Doctor
    // ---------------------------------------------------------
    @FXML
    private void handleDeleteDoctor() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No doctor selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Doctor");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this doctor?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    doctorService.deleteDoctor(selected.getSID());
                    lblMessage.setText("Doctor deleted successfully.");
                    loadDoctorsFromServer();
                    clearForm();
                } catch (Exception e) {
                    e.printStackTrace();
                    lblMessage.setText("Failed to delete doctor: " + e.getMessage());
                    showErrorAlert("Delete Doctor Error",
                            "Cannot delete doctor.\n\n" + e.getMessage());
                }
            }
        });
    }

    // ---------------------------------------------------------
    // Alert helper
    // ---------------------------------------------------------
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
