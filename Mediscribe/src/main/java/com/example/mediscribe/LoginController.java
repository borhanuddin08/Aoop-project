package com.example.mediscribe;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public static String session;
    public static String pkey;
    @FXML
    private Circle btnclose;

    @FXML
    private Label labeltitle;

    @FXML
    private JFXComboBox<String> designationComboBox;

    @FXML
    private JFXComboBox<String> genderComboBox;

    @FXML
    private DatePicker dobpicker;

    @FXML
    private Label labelslogan;

    @FXML
    private Pane pnsignin2;

    @FXML
    private TextField tfpid;

    @FXML
    private Button Btnsignin2;

    @FXML
    private Label labelforgot1;

    @FXML
    private Label signin1;

    @FXML
    private Button btnptnsigin;

    @FXML
    private Pane pnsignup;

    @FXML
    private Label labelsignup;

    @FXML
    private TextField tfname1;

    @FXML
    private TextField tfpassword2;

    @FXML
    private TextField tfname2;

    @FXML
    private TextField tfemail2;

    @FXML
    private ImageView btnback;

    @FXML
    private Pane pnsignin;

    @FXML
    private PasswordField tfpass;

    @FXML
    private TextField tfemail;

    @FXML
    private Button btnsignin;

    @FXML
    private Button btnsignup;

    @FXML
    private Label labelforgot;

    @FXML
    private Label signin;

    @FXML
    private Button btnsignin2;

    @FXML
    private Button Btnsignin21;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource().equals(btnsignup)) {
            pnsignup.toFront();
        }

        if (event.getSource().equals(Btnsignin2)) {
            createAccount();
        }

        if (event.getSource().equals(Btnsignin21)) {
            pnsignin.toFront();
        }

        if (event.getSource().equals(btnsignin2)) {
            pnsignin2.toFront();
        }

        if (event.getSource().equals(btnsignin)) {
            loginDoctor();
        }

        if (event.getSource().equals(btnptnsigin)) {
            loginPatient();
        }
    }

    private void createAccount() {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/mediscribe", "root", "");
            String sql = "INSERT INTO doctor (Name, Designation, Password, Email, DOB, Gender) VALUES(?,?,?,?,?,?)";
            String fullName = tfname1.getText() + " " + tfname2.getText();
            ps = con.prepareStatement(sql);

            ps.setString(1, fullName);
            ps.setString(2, designationComboBox.getValue());
            ps.setString(3, tfpassword2.getText());
            ps.setString(4, tfemail2.getText());
            ps.setString(5, dobpicker.getValue().toString());
            ps.setString(6, genderComboBox.getValue());

            int i = ps.executeUpdate();
            if (i > 0) {
                pnsignin.toFront();
                showAlert(Alert.AlertType.INFORMATION, "Account Created", "Account Created Successfully", "Your account has been created successfully.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error in Account Creation", "Account creation failed due to: " + e.getMessage());
        }
    }

    private void loginDoctor() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/mediscribe", "root", "");
            String sql = "SELECT * FROM doctor WHERE Email=? AND Password=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, tfemail.getText());
            ps.setString(2, tfpass.getText());
            rs = ps.executeQuery();

            if (rs.next()) {
                session = tfemail.getText();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                Parent root = loader.load();
                Scene dashboard = new Scene(root);
                Main.mainStage.setScene(dashboard);
                showAlert(Alert.AlertType.INFORMATION, "Welcome", "You are Logged In", "You've logged in successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Credentials", "The email or password you entered is incorrect.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Login Failed", "An error occurred while logging in: " + e.getMessage());
        }
    }

    private void loginPatient() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/mediscribe", "root", "");
            String sql = "SELECT * FROM patient WHERE ID=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, tfpid.getText());
            rs = ps.executeQuery();

            if (rs.next()) {
                pkey = tfpid.getText();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("P_Dasboard.fxml"));
                Parent root = loader.load();
                Scene p_dashboard = new Scene(root);
                Main.mainStage.setScene(p_dashboard);
                showAlert(Alert.AlertType.INFORMATION, "Welcome", "You are Logged In", "You've logged in successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Patient ID", "The Patient ID you entered is incorrect.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Login Failed", "An error occurred while logging in: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource() == btnclose) {
            System.exit(0);
        }

        if (event.getSource().equals(btnback)) {
            pnsignin.toFront();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        designationComboBox.setItems(FXCollections.observableArrayList("MBBS", "FCPS", "MD", "BDS", "DVM", "PhD", "MS"));
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female"));
        if (dobpicker == null) {
            System.out.println("dobPicker is null. Check the FXML file for issues.");
        }
    }
}
