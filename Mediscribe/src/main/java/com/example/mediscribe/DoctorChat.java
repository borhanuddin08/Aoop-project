package com.example.mediscribe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DoctorChat extends Application {

    private ServerSocket serverSocket;
    private TextArea messagesArea;
    private Label patientStatusLabel;
    private TextField messageField;

    private Socket clientSocket;
    private DataInputStream fromClient;
    private DataOutputStream toClient;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        messagesArea = new TextArea();
        messagesArea.setEditable(false);
        messagesArea.setWrapText(true);
        messagesArea.setFont(Font.font("Arial", 14));
        messagesArea.setStyle("-fx-font-size: 14px; -fx-background-color: #f5f5f5;");
        messagesArea.setMaxHeight(Double.MAX_VALUE);



        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        sendButton.setOnAction(event -> sendMessage());

        messageField = new TextField();
        messageField.setFont(Font.font("Arial", 14));
        messageField.setPromptText("Type your message here...");

        HBox messageBox = new HBox(10, messageField, sendButton);
        messageBox.setPadding(new Insets(10));
        messageBox.setAlignment(Pos.CENTER);

        Label messagesLabel = new Label("Messages:");
        messagesLabel.setFont(Font.font("Arial", 16));
        messagesLabel.setTextFill(Color.web("#4CAF50"));

        BorderPane root = new BorderPane();
        root.setCenter(messagesArea);
        root.setBottom(messageBox);
        root.setPadding(new Insets(10));
        root.setBackground(new Background(new BackgroundFill(Color.web("#F5F5F5"), CornerRadii.EMPTY, Insets.EMPTY)));

        GridPane statusPane = new GridPane();
        statusPane.setHgap(10);
        statusPane.setVgap(5);
        statusPane.setPadding(new Insets(10));
        statusPane.setAlignment(Pos.CENTER_LEFT);

        patientStatusLabel = new Label("No Patient is Online");
        patientStatusLabel.setFont(Font.font("Arial", 14));
        patientStatusLabel.setTextFill(Color.web("#4CAF50"));
        statusPane.add(new Label("Patient status: "), 0, 0);
        statusPane.add(patientStatusLabel, 1, 0);

        root.setTop(statusPane);

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setTitle("Doctor Chat");
        primaryStage.show();

        // start the server socket and listen for connections
        startServer();
    }



    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> patientStatusLabel.setText("No Patient is Online..."));
                clientSocket = serverSocket.accept();
                Platform.runLater(() -> patientStatusLabel.setText("Patient is online"));

                fromClient = new DataInputStream(clientSocket.getInputStream());
                toClient = new DataOutputStream(clientSocket.getOutputStream());

                while (true) {
                    String message = fromClient.readUTF();
                    Platform.runLater(() -> messagesArea.appendText("Patient: " + message + "\n"));
                }
            } catch (IOException e) {
                Platform.runLater(() -> patientStatusLabel.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    private void sendMessage() {
        try {
            if (clientSocket != null) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    toClient.writeUTF(message);
                    Platform.runLater(() -> messagesArea.appendText(message + "\n"));
                    messageField.clear();
                }
            }
        } catch (IOException e) {
            Platform.runLater(() -> patientStatusLabel.setText("Error: " + e.getMessage()));
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (clientSocket != null) {
            clientSocket.close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
