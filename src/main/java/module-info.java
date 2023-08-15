module com.example.javafxdeber {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.javafxdeber to javafx.fxml;
    exports com.example.javafxdeber;
}