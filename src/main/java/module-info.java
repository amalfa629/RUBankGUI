module com.example.rubankgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.project.rubankgui to javafx.fxml;
    exports com.project.rubankgui;
}