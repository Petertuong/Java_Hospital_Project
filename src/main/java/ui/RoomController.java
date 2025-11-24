package ui;

import model.Facility.Room;
import service.FacilityService.RoomService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RoomController {

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
    private final ObservableList<Room> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
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

    private void loadRoomsFromServer() {
        try {
            java.util.ArrayList<Room> list = roomService.listRoom();
            data.setAll(FXCollections.observableArrayList(list));
            lblMessage.setText("");
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
        updateButtonsState();
        lblMessage.setText("");
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

        String bedsStr = txtBedsAvailable.getText();
        if (bedsStr == null || bedsStr.trim().isEmpty()) {
            return "Beds Available is required.";
        }
        try {
            Integer.parseInt(bedsStr.trim());
        } catch (NumberFormatException e) {
            return "Beds Available must be an integer.";
        }

        return null;
    }

    @FXML
    private void handleAddRoom() {
        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        Room r = new Room(Integer.parseInt(txtRoomNo.getText().trim()), Integer.parseInt(txtBedsAvailable.getText().trim()));
        try {
            roomService.addRoom(r);
            lblMessage.setText("Room added successfully.");
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

        selected.setRoomNo(Integer.parseInt(txtRoomNo.getText().trim()));
        selected.setBedsAvialabletozero();
        // set exact beds available via setter is not available; use dao update via service
        try {
            selected = new Room(selected.getRoomNo(), Integer.parseInt(txtBedsAvailable.getText().trim()));
            roomService.updateRoom(selected);
            lblMessage.setText("Room updated successfully.");
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
