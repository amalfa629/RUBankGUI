package com.rubankgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TransactionManagerMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TransactionManagerMain.class.getResource("TransactionManagerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 960);
        stage.setTitle("Welcome to RUBank!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}