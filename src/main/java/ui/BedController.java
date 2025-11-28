package ui;

import model.Facility.Bed;
import model.Facility.Room;
import service.FacilityService.BedService;
import service.FacilityService.RoomService;
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
    private final RoomService roomService = new RoomService();
    private final ObservableList<Bed> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Disable auto-managed fields - these are controlled by admit/discharge workflow
        chkOccupied.setDisable(true);
        txtPatientSsn.setDisable(true);
        txtNurseId.setDisable(true);
        
        chkOccupied.setTooltip(new Tooltip("Auto-managed by patient admit/discharge"));
        txtPatientSsn.setPromptText("Auto-assigned during patient admission");
        txtNurseId.setPromptText("Auto-assigned during patient admission");
        
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

        // Occupied, Patient SSN, and Nurse ID are auto-managed - no validation needed
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
        
        try {
            Room room = new Room();
            room.setRoomNo(roomNo);
            // Create bed with default values (unoccupied, no patient/nurse assigned)
            Bed dto = new Bed(room, bedNo);
            dto.setOccupied(false);  // Default to not occupied
            dto.setPatient(null);    // No patient assigned initially
            dto.setNurse(null);      // No nurse assigned initially
            
            bedService.addBed(dto);
            
            // Auto-sync room bed availability
            roomService.updateRoomBedAvailability(roomNo);
            
            lblMessage.setText("Bed added successfully. Occupancy will be managed by admit/discharge workflow.");
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
        
        try {
            Room room = new Room();
            room.setRoomNo(roomNo);
            
            // Only update bed number and room - keep existing occupancy data
            Bed dto = new Bed(room, bedNo);
            dto.setOccupied(selected.isOccupied());     // Keep current occupancy status
            dto.setPatient(selected.getPatient());      // Keep current patient assignment
            dto.setNurse(selected.getNurse());          // Keep current nurse assignment
            
            int oldRoomNo = selected.getRoom() != null ? selected.getRoom().getRoomNo() : roomNo;
            
            bedService.updateBed(dto);
            
            // Auto-sync room bed availability for both old and new rooms
            roomService.updateRoomBedAvailability(oldRoomNo);
            if (oldRoomNo != roomNo) {
                roomService.updateRoomBedAvailability(roomNo);
            }
            
            lblMessage.setText("Bed updated successfully. Occupancy is managed by admit/discharge workflow.");
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
                    int deletedRoomNo = selected.getRoom() != null ? selected.getRoom().getRoomNo() : 0;
                    
                    bedService.delete(selected.getBedNo());
                    
                    // Auto-sync room bed availability after deletion
                    if (deletedRoomNo > 0) {
                        roomService.updateRoomBedAvailability(deletedRoomNo);
                    }
                    
                    lblMessage.setText("Bed deleted successfully. Room availability updated.");
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
