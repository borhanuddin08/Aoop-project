module com.example.mediscribe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires pdfjet;
    requires mysql.connector.j;
    requires com.jfoenix;
    requires jBCrypt;


    opens com.example.mediscribe to javafx.fxml;
    exports com.example.mediscribe;
}