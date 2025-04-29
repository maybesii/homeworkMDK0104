module com.ivanov.numeralsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ivanov.numeralsystem to javafx.fxml;
    exports com.ivanov.numeralsystem;
    exports com.ivanov.numeralsystem.controller;
    opens com.ivanov.numeralsystem.controller to javafx.fxml;
}