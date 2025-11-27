package ui;

import model.Staff.Nurse;
import service.PersonService.NurseService;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class NurseController implements ReadOnlyController {

    private boolean readOnlyMode = false;

    @FXML
    private TextField txtId;                 // nurse_id (read-only)
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhone;
    @FXML
    private ComboBox<String> cbGender;
    @FXML
    private TextField txtSpecialization;
    @FXML
    private TextField txtPatientInCharge;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;

    @FXML
    private Label lblMessage;

        @FXML
        private TableView<Nurse> nurseTable;
        @FXML
        private TableColumn<Nurse, Number> colId;
        @FXML
        private TableColumn<Nurse, String> colName;
        @FXML
        private TableColumn<Nurse, String> colGender;
        @FXML
        private TableColumn<Nurse, String> colPhone;
        @FXML
        private TableColumn<Nurse, String> colSpecialization;
        @FXML
        private TableColumn<Nurse, Number> colPatientInCharge;

        private final NurseService nurseService = new NurseService();

        // Data cho TableView
        private final ObservableList<Nurse> data =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // ComboBox giới tính
        cbGender.setItems(FXCollections.observableArrayList("Male", "Female"));

        // Mapping column -> NurseDto property
        colId.setCellValueFactory(cd ->
            new SimpleIntegerProperty(
                cd.getValue().getSID()
            ));
        colName.setCellValueFactory(cd ->
            new SimpleStringProperty(cd.getValue().getFullname()));
        colGender.setCellValueFactory(cd ->
            new SimpleStringProperty(mapGenderCharToText(cd.getValue().getGender())));
        colPhone.setCellValueFactory(cd ->
            new SimpleStringProperty(cd.getValue().getPhoneNo()));
        colSpecialization.setCellValueFactory(cd ->
            new SimpleStringProperty(cd.getValue().getSpecialization()));
        colPatientInCharge.setCellValueFactory(cd ->
            new SimpleIntegerProperty(
                cd.getValue().getPatient_in_charge()
            ));

        nurseTable.setItems(data);

        // Khi chọn 1 dòng -> show lên form
        nurseTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSel, newSel) -> showNurseDetails(newSel)
        );

        lblMessage.setText("");
        updateButtonsState();

        // Load data từ backend
        loadNursesFromServer();
    }

    // Read-Only Mode Implementation

    @Override
    public void setReadOnlyMode(boolean readOnly) {
        this.readOnlyMode = readOnly;
        
        if (readOnly) {
            // STAFF MODE: READ-ONLY access
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            
            txtName.setEditable(false);
            txtPhone.setEditable(false);
            cbGender.setDisable(true);
            txtSpecialization.setEditable(false);
            
            lblMessage.setText("READ-ONLY MODE: Staff can view but not modify nurse data");
            lblMessage.setStyle("-fx-text-fill: #2c3e50; -fx-font-style: italic;");
        } else {
            // 👑 ADMIN MODE: FULL access
            lblMessage.setText("");
        }
    }

    // Helpers map gender

    private char mapGenderTextToChar(String text) {
        if (text == null) return 'M';
        switch (text) {
            case "Male":
                return 'M';
            case "Female":
                return 'F';
            default:
                return 'M';
        }
    }

    private String mapGenderCharToText(char c) {
        if (c == 'M') return "Male";
        if (c == 'F') return "Female";
        return "Male";
    }

    // Load from backend

    private void loadNursesFromServer() {
        try {
            java.util.ArrayList<Nurse> serverData = nurseService.listNurse();
            data.setAll(FXCollections.observableArrayList(serverData));
            lblMessage.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Cannot load nurses from DB: " + e.getMessage());
            showErrorAlert("Load Nurse Error",
                    "Cannot load nurses from DB.\n\n" + e.getMessage());
        }
        updateButtonsState();
    }

    // Show details in form

    private void showNurseDetails(Nurse n) {
        if (n == null) {
            clearForm();
            return;
        }

        txtId.setText(String.valueOf(n.getSID()));
        txtName.setText(n.getFullname());
        txtPhone.setText(n.getPhoneNo());
        cbGender.setValue(mapGenderCharToText(n.getGender()));
        txtSpecialization.setText(n.getSpecialization());
        txtPatientInCharge.setText(String.valueOf(n.getPatient_in_charge()));

        updateButtonsState();
        lblMessage.setText("");
    }

    // Clear form

    private void clearForm() {
        txtId.clear();
        txtName.clear();
        txtPhone.clear();
        cbGender.setValue(null);
        txtSpecialization.clear();
        txtPatientInCharge.clear();

        nurseTable.getSelectionModel().clearSelection();
        updateButtonsState();
        lblMessage.setText("");
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    // Buttons enable/disable

    private void updateButtonsState() {
        boolean hasSelection = nurseTable.getSelectionModel().getSelectedItem() != null;
        btnUpdate.setDisable(!hasSelection);
        btnDelete.setDisable(!hasSelection);
    }

    // Validation

    private String validateFormForAddOrUpdate() {
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

        return null;
    }

    private Integer parsePatientInCharge() {
        String text = txtPatientInCharge.getText();
        if (text == null || text.trim().isEmpty()) {
            return 0;  // mặc định 0
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Add Nurse

    @FXML
    private void handleAddNurse() {
        String error = validateFormForAddOrUpdate();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        Nurse n = new Nurse(txtName.getText().trim(), txtPhone.getText().trim(), mapGenderTextToChar(cbGender.getValue()), txtSpecialization.getText().trim());
        try {
            nurseService.insertNurse(n);
            lblMessage.setText("Nurse added successfully.");
            loadNursesFromServer();
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Failed to add nurse: " + e.getMessage());
            showErrorAlert("Add Nurse Error",
                    "Cannot add nurse.\n\n" + e.getMessage());
        }
    }

    // Update Nurse

    @FXML
    private void handleUpdateNurse() {
        Nurse selected = nurseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No nurse selected.");
            return;
        }

        String error = validateFormForAddOrUpdate();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        // parse id
        Integer id = null;
        String idText = txtId.getText();
        if (idText != null && !idText.trim().isEmpty()) {
            try {
                id = Integer.parseInt(idText.trim());
            } catch (NumberFormatException ex) {
                lblMessage.setText("Invalid ID.");
                return;
            }
        }

        Nurse n = new Nurse(txtName.getText().trim(), txtPhone.getText().trim(), mapGenderTextToChar(cbGender.getValue()), id, txtSpecialization.getText().trim(), parsePatientInCharge());

        try {
            nurseService.updateNurse(n);
            lblMessage.setText("Nurse updated successfully.");
            loadNursesFromServer();
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Failed to update nurse: " + e.getMessage());
            showErrorAlert("Update Nurse Error",
                    "Cannot update nurse.\n\n" + e.getMessage());
        }
    }

    // Delete Nurse

    @FXML
    private void handleDeleteNurse() {
        Nurse selected = nurseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No nurse selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Nurse");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this nurse?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    nurseService.deleteNurse(selected.getSID());
                    lblMessage.setText("Nurse deleted successfully.");
                    loadNursesFromServer();
                    clearForm();
                } catch (Exception e) {
                    e.printStackTrace();
                    lblMessage.setText("Failed to delete nurse: " + e.getMessage());
                    showErrorAlert("Delete Nurse Error",
                            "Cannot delete nurse.\n\n" + e.getMessage());
                }
            }
        });
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
