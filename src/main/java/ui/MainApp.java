package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login_view.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Hospital Management System");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Kiểm tra runtime trước khi launch UI
        if (!StartupChecker.checkJavaVersion()) {
            // thông báo đã được in trong StartupChecker
            System.exit(1);
        }
        launch(args);
    }
}
