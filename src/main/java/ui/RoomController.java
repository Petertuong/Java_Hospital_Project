package ui;

import model.Facility.Room;
import service.FacilityService.RoomService;
import service.MultiService.BedRoomService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RoomController implements ReadOnlyController {

    private boolean readOnlyMode = false;

    @FXML
    private TextField txtRoomNo;
    @FXML
    private TextField txtBedsAvailable;

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
    private TableView<Room> roomTable;
    @FXML
    private TableColumn<Room, String> colRoomNo;
    @FXML
    private TableColumn<Room, String> colBedsAvailable;

    private final RoomService roomService = new RoomService();
    private final BedRoomService syncManager = new BedRoomService();
    private final ObservableList<Room> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Disable beds available field - this is auto-managed by the system
        txtBedsAvailable.setDisable(true);
        txtBedsAvailable.setPromptText("Auto-calculated based on bed assignments");
        
        colRoomNo.setCellValueFactory(cd ->
            new SimpleStringProperty(String.valueOf(cd.getValue().getRoomNo())));
        colBedsAvailable.setCellValueFactory(cd ->
            new SimpleStringProperty(String.valueOf(cd.getValue().getBedsAvailable())));

        roomTable.setItems(data);

        roomTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showRoomDetails(newSel)
        );

        lblMessage.setText("");
        updateButtonsState();

        loadRoomsFromServer();
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
            btnClear.setDisable(true);
            
            txtRoomNo.setEditable(false);
            txtBedsAvailable.setEditable(false);
            
            lblMessage.setText("READ-ONLY MODE: Staff can view but not modify room data");
            lblMessage.setStyle("-fx-text-fill: #2c3e50; -fx-font-style: italic;");
        } else {
            // 👑 ADMIN MODE: FULL access
            lblMessage.setText("");
        }
    }

    private void loadRoomsFromServer() {
        try {
            java.util.ArrayList<Room> list = roomService.listRoom();
            data.setAll(FXCollections.observableArrayList(list));
            
            // Updated: by Tuong
            BedRoomService brS = new BedRoomService();
            for (Room room : list) {
                brS.updateBedsAvailable(room.getRoomNo());
            }
            
            // Reload after sync to show updated data
            list = roomService.listRoom();
            data.setAll(FXCollections.observableArrayList(list));
            
            lblMessage.setText("Rooms loaded and bed availability synchronized.");
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Cannot load rooms: " + e.getMessage());
            showError("Load Rooms Error", "Cannot load rooms.\n\n" + e.getMessage());
        }
        updateButtonsState();
    }

    private void showRoomDetails(Room r) {
        if (r == null) {
            clearForm();
            return;
        }
        txtRoomNo.setText(String.valueOf(r.getRoomNo()));
        txtBedsAvailable.setText(String.valueOf(r.getBedsAvailable()));
        
        // Show real-time bed stats for selected room
        RoomService roomService = new RoomService();
        Room room = roomService.findRoomByNo(r.getRoomNo());

        lblMessage.setText(String.format("Room %d: Beds Available = %d", r.getRoomNo(), room.getBedsAvailable()));

        updateButtonsState();
    }

    private void clearForm() {
        txtRoomNo.clear();
        txtBedsAvailable.clear();
        roomTable.getSelectionModel().clearSelection();
        updateButtonsState();
        lblMessage.setText("");
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }
    
    
    @FXML
    private void handleSyncRoomBeds() {
        try {
            lblMessage.setText("Synchronizing room bed availability...");
            
            java.util.ArrayList<Room> list = roomService.listRoom();
            data.setAll(FXCollections.observableArrayList(list));
            
            // Updated: by Tuong
            BedRoomService brS = new BedRoomService();
            for (Room room : list) {
                brS.updateBedsAvailable(room.getRoomNo());
            }
            
            // Reload rooms to show updated data
            loadRoomsFromServer();
            
            lblMessage.setText("Room bed availability synchronized successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Failed to sync room bed availability: " + e.getMessage());
            showError("Sync Error", "Cannot sync room bed availability.\n\n" + e.getMessage());
        }
    }
    
    
    @FXML 
    private void handleSyncSelectedRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("Please select a room to sync.");
            return;
        }
        
        try {
            lblMessage.setText(String.format("Synchronizing Room %d...", selected.getRoomNo()));
            
            // Updated: by Tuong
            BedRoomService brS = new BedRoomService();
            // Update specific room's bed availability
            brS.updateBedsAvailable(selected.getRoomNo());
            
            // Reload rooms to show updated data
            loadRoomsFromServer();
            
            // Refresh selection to show updated stats
            showRoomDetails(roomTable.getSelectionModel().getSelectedItem());
            
            lblMessage.setText(String.format("✅ Room %d synchronized successfully!", selected.getRoomNo()));
            
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("❌ Failed to sync room: " + e.getMessage());
            showError("Sync Error", "Cannot sync room.\n\n" + e.getMessage());
        }
    }

    private void updateButtonsState() {
        boolean hasSelection = roomTable.getSelectionModel().getSelectedItem() != null;
        btnUpdate.setDisable(!hasSelection);
        btnDelete.setDisable(!hasSelection);
    }

    private String validateForm() {
        String roomNoStr = txtRoomNo.getText();
        if (roomNoStr == null || roomNoStr.trim().isEmpty()) {
            return "Room No is required.";
        }
        try {
            Integer.parseInt(roomNoStr.trim());
        } catch (NumberFormatException e) {
            return "Room No must be an integer.";
        }

        // Beds Available is auto-managed, no validation needed
        return null;
    }

    @FXML
    private void handleAddRoom() {
        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        // Create room with default beds available (will be auto-calculated)
        Room r = new Room(Integer.parseInt(txtRoomNo.getText().trim()), 0);
        try {
            roomService.addRoom(r);
            lblMessage.setText("Room added successfully. Beds Available will be calculated automatically.");
            loadRoomsFromServer();
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Failed to add room: " + e.getMessage());
            showError("Add Room Error", "Cannot add room.\n\n" + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No room selected.");
            return;
        }

        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        // Only update room number, keep existing beds available (auto-managed)
        selected.setRoomNo(Integer.parseInt(txtRoomNo.getText().trim()));
        // Don't modify beds available - it's auto-calculated by the system
        try {
            roomService.updateRoom(selected);
            lblMessage.setText("Room updated successfully. Beds Available is auto-managed.");
            loadRoomsFromServer();
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Failed to update room: " + e.getMessage());
            showError("Update Room Error", "Cannot update room.\n\n" + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("No room selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Room");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this room?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    roomService.deleteRoom(selected.getRoomNo());
                    lblMessage.setText("Room deleted successfully.");
                    loadRoomsFromServer();
                    clearForm();
                } catch (Exception e) {
                    e.printStackTrace();
                    lblMessage.setText("Failed to delete room: " + e.getMessage());
                    showError("Delete Room Error", "Cannot delete room.\n\n" + e.getMessage());
                }
            }
        });
    }

    private void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
