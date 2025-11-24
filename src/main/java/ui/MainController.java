package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.SessionManager.Role;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Label lblCurrentUser;
    @FXML
    private Label lblRoleTag;
    @FXML
    private HBox headerBar;
    @FXML private Button btnBeds;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnPatients;
    @FXML
    private Button btnPrescriptions;

    @FXML
    private Button btnDoctors;
    @FXML
    private Button btnNurses;
    @FXML
    private Button btnRooms;
    @FXML
    private Button btnMedicines;
    @FXML
    private Button btnLogout;

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            goToLogin();
            return;
        }

        String username = SessionManager.getCurrentUsername();
        Role role = SessionManager.getCurrentRole();

        lblCurrentUser.setText(username);
        applyRoleUI(role);
        applyRolePermissions(role);

        showDashboard();
    }

    private void applyRoleUI(Role role) {
        if (role == Role.ADMIN) {
            headerBar.setStyle("-fx-padding: 10; -fx-background-color: #c0392b;");
            lblRoleTag.setText("ADMIN VIEW");
            lblRoleTag.setStyle(
                    "-fx-text-fill: white; -fx-font-weight: bold;" +
                    "-fx-padding: 3 8 3 8;" +
                    "-fx-background-radius: 10;" +
                    "-fx-background-color: #e74c3c;"
            );
        } else {
            headerBar.setStyle("-fx-padding: 10; -fx-background-color: #2980b9;");
            lblRoleTag.setText("STAFF VIEW");
            lblRoleTag.setStyle(
                    "-fx-text-fill: white; -fx-font-weight: bold;" +
                    "-fx-padding: 3 8 3 8;" +
                    "-fx-background-radius: 10;" +
                    "-fx-background-color: #3498db;"
            );
        }
    }

    private void applyRolePermissions(Role role) {
        if (role == Role.STAFF) {
            btnDoctors.setDisable(true);
            btnNurses.setDisable(true);
            btnRooms.setDisable(true);
            btnMedicines.setDisable(true);
        }
    }

    private void setContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/" + fxmlFile));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDashboard() {
        setContent("dashboard_view.fxml");
    }

    @FXML
    private void showPatients() {
        setContent("patient_view.fxml");
    }

    @FXML
    private void showPrescriptions() {
        setContent("prescription_view.fxml");
    }
    
    @FXML
    private void showDoctors() {
        setContent("doctor_view.fxml");
    }
    @FXML
    private void showBeds() {
    setContent("bed_view.fxml"); // đúng path FXML mà em đã tạo
}

    @FXML
    private void showNurses() {
        setContent("nurse_view.fxml");
    }

    @FXML
    private void showRooms() {
        setContent("room_view.fxml");
    }

    @FXML
    private void showMedicines() {
        setContent("medicine_view.fxml");
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        goToLogin();
    }

    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setTitle("Hospital Management System - Login");

            Scene scene = new Scene(root, 450, 300);
            stage.setScene(scene);

            // Allow resizing for login window as well (optional)
            stage.setResizable(true);
            stage.setMinWidth(450);
            stage.setMinHeight(300);

            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
