package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private TextField txtPasswordVisible;
    @FXML
    private TextField txtConfirmPasswordVisible;
    @FXML
    private CheckBox chkShowPassword;
    @FXML
    private Label lblMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Bind password fields for real-time sync
        txtPasswordVisible.textProperty().addListener((obs, oldText, newText) -> {
            if (txtPasswordVisible.isVisible()) {
                txtPassword.setText(newText);
            }
        });
        
        txtPassword.textProperty().addListener((obs, oldText, newText) -> {
            if (txtPassword.isVisible()) {
                txtPasswordVisible.setText(newText);
            }
        });
        
        txtConfirmPasswordVisible.textProperty().addListener((obs, oldText, newText) -> {
            if (txtConfirmPasswordVisible.isVisible()) {
                txtConfirmPassword.setText(newText);
            }
        });
        
        txtConfirmPassword.textProperty().addListener((obs, oldText, newText) -> {
            if (txtConfirmPassword.isVisible()) {
                txtConfirmPasswordVisible.setText(newText);
            }
        });
    }

    @FXML
    private void handleTogglePasswordVisibility() {
        boolean showPassword = chkShowPassword.isSelected();
        
        if (showPassword) {
            // Sync values and show text fields
            txtPasswordVisible.setText(txtPassword.getText());
            txtConfirmPasswordVisible.setText(txtConfirmPassword.getText());
            txtPassword.setVisible(false);
            txtConfirmPassword.setVisible(false);
            txtPasswordVisible.setVisible(true);
            txtConfirmPasswordVisible.setVisible(true);
        } else {
            // Sync values and show password fields
            txtPassword.setText(txtPasswordVisible.getText());
            txtConfirmPassword.setText(txtConfirmPasswordVisible.getText());
            txtPasswordVisible.setVisible(false);
            txtConfirmPasswordVisible.setVisible(false);
            txtPassword.setVisible(true);
            txtConfirmPassword.setVisible(true);
        }
    }

    @FXML
    private void handleRegister() {
        String username = txtUsername.getText().trim();
        // Get password from visible field if showing, otherwise from password field
        String pass = chkShowPassword.isSelected() ? 
                      txtPasswordVisible.getText().trim() : txtPassword.getText().trim();
        String confirm = chkShowPassword.isSelected() ? 
                         txtConfirmPasswordVisible.getText().trim() : txtConfirmPassword.getText().trim();

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
