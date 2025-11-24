package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label lblPatientsCount;
    @FXML
    private Label lblDoctorsCount;
    @FXML
    private Label lblAvailableRooms;
    @FXML
    private Label lblTodayAppointments;

    @FXML
    public void initialize() {
        // Currently not connected to DB yet, using demo values.
        int patientsDemo = 3;          // e.g. 3 patients
        int doctorsDemo = 3;           // e.g. 3 doctors
        int availableRoomsDemo = 5;    // e.g. 5 available rooms
        int todayAppointmentsDemo = 2; // e.g. 2 appointments today

        lblPatientsCount.setText(String.valueOf(patientsDemo));
        lblDoctorsCount.setText(String.valueOf(doctorsDemo));
        lblAvailableRooms.setText(String.valueOf(availableRoomsDemo));
        lblTodayAppointments.setText(String.valueOf(todayAppointmentsDemo));
    }
}
