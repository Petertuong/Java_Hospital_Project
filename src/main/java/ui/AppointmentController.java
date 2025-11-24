package ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class AppointmentController {

    @FXML
    private ComboBox<String> cbPatient;
    @FXML
    private ComboBox<String> cbDoctor;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TextField txtTime;
    @FXML
    private ComboBox<String> cbStatus;
    @FXML
    private TextField txtNotes;

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> colPatient;
    @FXML
    private TableColumn<Appointment, String> colDoctor;
    @FXML
    private TableColumn<Appointment, String> colDate;
    @FXML
    private TableColumn<Appointment, String> colTime;
    @FXML
    private TableColumn<Appointment, String> colStatus;
    @FXML
    private TableColumn<Appointment, String> colNotes;

    @FXML
    private Label lblMessage;

    @FXML
    private Button btnAdd, btnUpdate, btnDelete, btnClear;

    private final ObservableList<Appointment> data =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Dummy patients & doctors (sau này nối DB sẽ lấy từ Patient/Doctor table)
        cbPatient.setItems(FXCollections.observableArrayList(
                "John Doe", "Alice Smith", "Michael Brown", "Custom Patient"
        ));
        cbDoctor.setItems(FXCollections.observableArrayList(
                "Dr. Gregory House", "Dr. Meredith Grey", "Dr. John Watson"
        ));

        cbStatus.setItems(FXCollections.observableArrayList(
                "Scheduled", "Completed", "Cancelled", "No-show", "Rescheduled"
        ));

        colPatient.setCellValueFactory(new PropertyValueFactory<>("patient"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));

        // Dummy data
        data.add(new Appointment("John Doe", "Dr. Gregory House",
                "2025-01-10", "09:00", "Scheduled", "Routine checkup"));
        data.add(new Appointment("Alice Smith", "Dr. Meredith Grey",
                "2025-01-11", "14:30", "Completed", "Post-surgery follow-up"));

        appointmentTable.setItems(data);

        appointmentTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> showDetails(newSel)
        );

        updateButtons();
        lblMessage.setText("");
    }

    private void showDetails(Appointment a) {
        if (a == null) {
            clearForm();
            return;
        }
        cbPatient.setValue(a.getPatient());
        cbDoctor.setValue(a.getDoctor());

        String dateStr = a.getDate();
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            try {
                dpDate.setValue(LocalDate.parse(dateStr));
            } catch (Exception e) {
                dpDate.setValue(null);
            }
        } else {
            dpDate.setValue(null);
        }

        txtTime.setText(a.getTime());
        cbStatus.setValue(a.getStatus());
        txtNotes.setText(a.getNotes());

        updateButtons();
        lblMessage.setText("");
    }

    private void updateButtons() {
        boolean selected = appointmentTable.getSelectionModel().getSelectedItem() != null;
        btnUpdate.setDisable(!selected);
        btnDelete.setDisable(!selected);
    }

    private void clearForm() {
        cbPatient.setValue(null);
        cbDoctor.setValue(null);
        dpDate.setValue(null);
        txtTime.clear();
        cbStatus.setValue(null);
        txtNotes.clear();
        appointmentTable.getSelectionModel().clearSelection();
        updateButtons();
        lblMessage.setText("");
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    private String validateForm() {
        String patient = cbPatient.getValue();
        if (patient == null || patient.trim().isEmpty()) {
            return "Patient is required.";
        }

        String doctor = cbDoctor.getValue();
        if (doctor == null || doctor.trim().isEmpty()) {
            return "Doctor is required.";
        }

        if (dpDate.getValue() == null) {
            return "Date is required.";
        }

        String time = txtTime.getText();
        if (time == null || time.trim().isEmpty()) {
            return "Time is required.";
        }
        // Time format simple check HH:MM (24h)
        if (!time.trim().matches("^[0-2]\\d:[0-5]\\d$")) {
            return "Time must be in HH:MM format (00:00 - 23:59).";
        }

        String status = cbStatus.getValue();
        if (status == null || status.trim().isEmpty()) {
            return "Status is required.";
        }

        return null;
    }

    @FXML
    private void handleAddAppointment() {
        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        String dateStr = dpDate.getValue() != null
                ? dpDate.getValue().toString()
                : null;

        Appointment a = new Appointment(
                cbPatient.getValue(),
                cbDoctor.getValue(),
                dateStr,
                txtTime.getText().trim(),
                cbStatus.getValue(),
                txtNotes.getText().trim()
        );

        data.add(a);
        appointmentTable.getSelectionModel().select(a);
        lblMessage.setText("Appointment added successfully.");
    }

    @FXML
    private void handleUpdateAppointment() {
        Appointment a = appointmentTable.getSelectionModel().getSelectedItem();
        if (a == null) {
            lblMessage.setText("No appointment selected.");
            return;
        }

        String error = validateForm();
        if (error != null) {
            lblMessage.setText(error);
            return;
        }

        String dateStr = dpDate.getValue() != null
                ? dpDate.getValue().toString()
                : null;

        a.setPatient(cbPatient.getValue());
        a.setDoctor(cbDoctor.getValue());
        a.setDate(dateStr);
        a.setTime(txtTime.getText().trim());
        a.setStatus(cbStatus.getValue());
        a.setNotes(txtNotes.getText().trim());

        appointmentTable.refresh();
        lblMessage.setText("Appointment updated successfully.");
    }

    @FXML
    private void handleDeleteAppointment() {
        Appointment a = appointmentTable.getSelectionModel().getSelectedItem();
        if (a == null) {
            lblMessage.setText("No appointment selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this appointment?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                data.remove(a);
                clearForm();
                lblMessage.setText("Appointment deleted successfully.");
            }
        });
    }

    // Model class
    public static class Appointment {
        private final SimpleStringProperty patient;
        private final SimpleStringProperty doctor;
        private final SimpleStringProperty date;
        private final SimpleStringProperty time;
        private final SimpleStringProperty status;
        private final SimpleStringProperty notes;

        public Appointment(String patient, String doctor,
                           String date, String time,
                           String status, String notes) {
            this.patient = new SimpleStringProperty(patient);
            this.doctor = new SimpleStringProperty(doctor);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.status = new SimpleStringProperty(status);
            this.notes = new SimpleStringProperty(notes);
        }

        public String getPatient() { return patient.get(); }
        public void setPatient(String v) { patient.set(v); }

        public String getDoctor() { return doctor.get(); }
        public void setDoctor(String v) { doctor.set(v); }

        public String getDate() { return date.get(); }
        public void setDate(String v) { date.set(v); }

        public String getTime() { return time.get(); }
        public void setTime(String v) { time.set(v); }

        public String getStatus() { return status.get(); }
        public void setStatus(String v) { status.set(v); }

        public String getNotes() { return notes.get(); }
        public void setNotes(String v) { notes.set(v); }
    }
}
