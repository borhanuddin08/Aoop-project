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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class patientChat extends Application {

    private Socket socket;
    private DataInputStream inputFromServer;
    private DataOutputStream outputToServer;

    private Label statusLabel;
    private TextArea chatArea;
    private TextField inputField;


    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        // Status Label
        statusLabel = new Label("Doctor Status");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");
        root.setTop(statusLabel);

        // Chat Area
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setStyle("-fx-font-size: 14px; -fx-background-color: #f5f5f5;");
        chatArea.setMaxHeight(Double.MAX_VALUE);
        root.setCenter(chatArea);

        // Input Field and Send Button
        inputField = new TextField();
        inputField.setStyle("-fx-font-size: 14px;");
        inputField.setPromptText("Type your message here...");
        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: #fff;");
        sendButton.setOnAction(event -> sendMessage());

        HBox inputBox = new HBox();
        inputBox.getChildren().addAll(inputField, sendButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));
        root.setBottom(inputBox);

        // Create Scene and Set Stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Patient Chat");
        primaryStage.show();

        // Connect to Server
        connectToServer("localhost", 8000);
    }




    private void connectToServer(String host, int port) {
        try {
            socket = new Socket(host, port);
            inputFromServer = new DataInputStream(socket.getInputStream());
            outputToServer = new DataOutputStream(socket.getOutputStream());
            statusLabel.setText("Doctor is online");

            // Start a new thread to handle incoming messages
            Thread messageHandler = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String message = inputFromServer.readUTF();
                            Platform.runLater(() -> {
                                chatArea.appendText("Doctor: " + message + "\n");
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            messageHandler.start();

        } catch (IOException e) {
            statusLabel.setText("Doctor is not online");
        }
    }

    private void sendMessage() {
        try {
            String message = inputField.getText();
            outputToServer.writeUTF(message);
            chatArea.appendText(message + "\n");
            inputField.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }



}

