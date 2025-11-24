package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private Label lblMessage;

    @FXML
    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();
        String confirm = txtConfirmPassword.getText().trim();

        if (username.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            lblMessage.setText("All fields are required.");
            return;
        }
        if (!pass.equals(confirm)) {
            lblMessage.setText("Passwords do not match.");
            return;
        }

        try {
            AuthManager.registerStaff(username, pass);
            lblMessage.setStyle("-fx-text-fill: green;");
            lblMessage.setText("Registered successfully. You can login now.");
        } catch (IllegalArgumentException e) {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText(e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setTitle("Hospital Management System - Login");
            stage.setScene(new Scene(root, 450, 300));
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            lblMessage.setText("Failed to load login view.");
        }
    }
}
