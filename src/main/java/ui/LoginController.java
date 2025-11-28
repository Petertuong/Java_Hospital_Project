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

public class LoginController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtPasswordVisible;
    @FXML
    private CheckBox chkShowPassword;
    @FXML
    private Label lblError;

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
    }

    @FXML
    private void handleTogglePasswordVisibility() {
        boolean showPassword = chkShowPassword.isSelected();
        
        if (showPassword) {
            // Sync values and show text field
            txtPasswordVisible.setText(txtPassword.getText());
            txtPassword.setVisible(false);
            txtPasswordVisible.setVisible(true);
        } else {
            // Sync values and show password field
            txtPassword.setText(txtPasswordVisible.getText());
            txtPasswordVisible.setVisible(false);
            txtPassword.setVisible(true);
        }
    }

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        // Get password from visible field if showing, otherwise from password field
        String password = chkShowPassword.isSelected() ? 
                          txtPasswordVisible.getText().trim() : txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Username and password are required.");
            return;
        }

        AuthManager.User user = AuthManager.authenticate(username, password);
        if (user == null) {
            lblError.setText("Invalid username or password.");
            return;
        }

        // Save session
        SessionManager.login(user.getUsername(), user.getRole());

        // Switch to main_view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setTitle("Hospital Management System - " + user.getRole().name());

            // Bigger scene for main UI
            Scene scene = new Scene(root, 1100, 700);
            stage.setScene(scene);

            // Allow resizing for main window
            stage.setResizable(true);
            stage.setMinWidth(900);
            stage.setMinHeight(600);

            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            lblError.setText("Failed to load main view.");
        }
    }

    @FXML
    private void handleGoToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/register_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setTitle("Hospital Management System - Register");

            Scene scene = new Scene(root, 450, 320);
            stage.setScene(scene);

            // Optional: allow resizing for register view as well
            stage.setResizable(true);
            stage.setMinWidth(450);
            stage.setMinHeight(320);

            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            lblError.setText("Failed to load register view.");
        }
    }
}
