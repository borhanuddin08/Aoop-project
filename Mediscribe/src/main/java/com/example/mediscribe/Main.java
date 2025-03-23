package com.example.mediscribe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    private double x, y;
    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;

        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        if (fxmlLoader.getLocation() == null) {
            System.err.println("Could not find the FXML file!");
            return;
        }
        Parent root = fxmlLoader.load();

        // Enable dragging
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);

            primaryStage.setOpacity(.8);
        });

        root.setOnMouseReleased(event -> {
            primaryStage.setOpacity(1);
        });

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

