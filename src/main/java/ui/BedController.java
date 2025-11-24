package ui;

import model.Facility.Bed;
import model.Facility.Room;
import model.Patients.Patient;
import model.Staff.Nurse;
import service.FacilityService.BedService;
import service.PersonService.PatientService;
import service.PersonService.NurseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BedController {

    @FXML
    private TextField txtBedNo;
    @FXML
    private TextField txtRoomNo;
    @FXML
    private CheckBox chkOccupied;
    @FXML
    private TextField txtPatientSsn;
    @FXML
    private TextField txtNurseId;

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
    private TableView<Bed> bedTable;
    @FXML
    private TableColumn<Bed, String> colBedNo;
    @FXML
    private TableColumn<Bed, String> colRoomNo;
    @FXML
    private TableColumn<Bed, String> colOccupied;
    @FXML
    private TableColumn<Bed, String> colPatientSsn;
    @FXML
    private TableColumn<Bed, String> colNurseId;

    private final BedService bedService = new BedService();
    private final PatientService patientService = new PatientService();
    private final NurseService nurseService = new NurseService();
    private final ObservableList<Bed> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colBedNo.setCellValueFactory(cd ->
            new SimpleStringProperty(
                String.valueOf(cd.getValue().getBedNo())
            ));
        colRoomNo.setCellValueFactory(cd ->
            new SimpleStringProperty(
                String.valueOf(cd.getValue().getRoom() != null ? cd.getValue().getRoom().getRoomNo() : "")
            ));
        colOccupied.setCellValueFactory(cd ->
            new SimpleStringProperty(cd.getValue().isOccupied() ? "Yes" : "No"));
        colPatientSsn.setCellValueFactory(cd ->
            new SimpleStringProperty(
                cd.getValue().getPatient() != null ? cd.getValue().getPatient().getSSN() : ""
            ));
        colNurseId.setCellValueFactory(cd ->
            new SimpleStringProperty(
                        cd.getValue().getNurse() != null ? String.valueOf(cd.getValue().getNurse().getSID()) : ""
            ));

        bedTable.setItems(data);

        bedTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showBedDetails(newSel)
        );

        lblMessage.setText("");
        loadBedsFromServer();
        updateButtonsState();
    }

    private void loadBedsFromServer() {
        try {
            java.util.ArrayList<Bed> serverData = bedService.listBed();
            data.setAll(FXCollections.observableArrayList(serverData));
            lblMessage.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Cannot load beds: " + e.getMessage());
            showErrorAlert("Load Beds Error", "Cannot load beds.\n\n" + e.getMessage());
        }
        updateButtonsState();
    }

    private void showBedDetails(Bed dto) {
        if (dto == null) {
            clearForm();
            return;
        }

        txtBedNo.setText(String.valueOf(dto.getBedNo()));
        txtRoomNo.setText(String.valueOf(dto.getRoom() != null ? dto.getRoom().getRoomNo() : ""));
        chkOccupied.setSelected(dto.isOccupied());
        txtPatientSsn.setText(dto.getPatient() != null ? dto.getPatient().getSSN() : "");
        txtNurseId.setText(dto.getNurse() != null ? String.valueOf(dto.getNurse().getSID()) : "");

        updateButtonsState();
        lblMessage.setText("");
    }

    private void clearForm() {
        txtBedNo.clear();
        txtRoomNo.clear();
        chkOccupied.setSelected(false);
        txtPatientSsn.clear();
        txtNurseId.clear();
        bedTable.getSelectionModel().clearSelection();
        updateButtonsState();
        lblMessage.setText("");
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    private void updateButtonsState() {
        boolean hasSelection = bedTable.getSelectionModel().getSelectedItem() != null;
        btnUpdate.setDisable(!hasSelection);
        btnDelete.setDisable(!hasSelection);
    }

    private String validateForm() {
        String bedNoStr = txtBedNo.getText();
        if (bedNoStr == null || bedNoStr.trim().isEmpty()) {
            return "Bed No is required.";
        }
        if (!bedNoStr.trim().matches("\\d+")) {
            return "Bed No must be digits.";
        }

        String roomNoStr = txtRoomNo.getText();
        if (roomNoStr == null || roomNoStr.trim().isEmpty()) {
            return "Room No is required.";
        }

        // patient SSN, nurse ID có thể để trống => optional
        return null;
    }

    @FXML
    private void handleAddBed() {
        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        int bedNo = Integer.parseInt(txtBedNo.getText().trim());
        int roomNo = Integer.parseInt(txtRoomNo.getText().trim());
        boolean occupied = chkOccupied.isSelected();
        String ssn = txtPatientSsn.getText();
        String nurseIdStr = txtNurseId.getText();
        try {
            Room room = new Room();
            room.setRoomNo(roomNo);
            Patient p = (ssn != null && !ssn.trim().isEmpty()) ? patientService.findPatientById(ssn.trim()) : null;
            Nurse n = (nurseIdStr != null && !nurseIdStr.trim().isEmpty()) ? nurseService.findNurseByID(Integer.parseInt(nurseIdStr.trim())) : null;
            Bed dto = new Bed(room, bedNo);
            dto.setOccupied(occupied);
            dto.setPatient(p);
            dto.setNurse(n);
            bedService.addBed(dto);
            lblMessage.setText("Bed added successfully.");
            loadBedsFromServer();
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Failed to add bed: " + e.getMessage());
            showErrorAlert("Add Bed Error", "Cannot add bed.\n\n" + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateBed() {
        Bed selected = bedTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No bed selected.");
            return;
        }

        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        int bedNo = Integer.parseInt(txtBedNo.getText().trim());
        int roomNo = Integer.parseInt(txtRoomNo.getText().trim());
        boolean occupied = chkOccupied.isSelected();
        String ssn = txtPatientSsn.getText();
        String nurseIdStr = txtNurseId.getText();
        try {
            Room room = new Room();
            room.setRoomNo(roomNo);
            Patient p = (ssn != null && !ssn.trim().isEmpty()) ? patientService.findPatientById(ssn.trim()) : null;
            Nurse n = (nurseIdStr != null && !nurseIdStr.trim().isEmpty()) ? nurseService.findNurseByID(Integer.parseInt(nurseIdStr.trim())) : null;
            Bed dto = new Bed(room, bedNo);
            dto.setOccupied(occupied);
            dto.setPatient(p);
            dto.setNurse(n);
            bedService.updateBed(dto);
            lblMessage.setText("Bed updated successfully.");
            loadBedsFromServer();
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Failed to update bed: " + e.getMessage());
            showErrorAlert("Update Bed Error", "Cannot update bed.\n\n" + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteBed() {
        Bed selected = bedTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No bed selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Bed");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this bed?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    bedService.delete(selected.getBedNo());
                    lblMessage.setText("Bed deleted successfully.");
                    loadBedsFromServer();
                    clearForm();
                } catch (Exception e) {
                    e.printStackTrace();
                    lblMessage.setText("Failed to delete bed: " + e.getMessage());
                    showErrorAlert("Delete Bed Error", "Cannot delete bed.\n\n" + e.getMessage());
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
