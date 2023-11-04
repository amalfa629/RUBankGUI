module com.example.rubankgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.rubankgui to javafx.fxml;
    exports com.rubankgui;
}