module com.example.javafxdeber {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxdeber to javafx.fxml;
    exports com.example.javafxdeber;
}