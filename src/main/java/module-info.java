module com.ivanov.numeralsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ivanov.numeralsystem to javafx.fxml;
    exports com.ivanov.numeralsystem;
}