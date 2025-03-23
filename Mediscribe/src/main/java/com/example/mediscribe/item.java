package com.example.mediscribe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class item {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    @FXML
    private Label messageLabel;

    // This method is invoked when the "Yes" button is clicked
    @FXML
    private void onNoButtonClicked(ActionEvent event) {
        System.out.println("User confirmed sign-out.");
        try {
            // Load the login screen or previous screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Scene primaryStage = new Scene(root);
            Main.mainStage.setScene(primaryStage); // Ensure this is the correct reference to your main stage
            System.out.println("Login screen loaded.");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the login screen");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // This method is invoked when the "No" button is clicked
    @FXML
    private void onYesButtonClicked(ActionEvent event) {
        System.out.println("User canceled sign-out.");
        // Close the confirmation window (dialog)
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
