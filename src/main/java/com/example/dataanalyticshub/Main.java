package com.example.dataanalyticshub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("first-page.fxml"));
        primaryStage.setTitle("Data Analytics Hub");
        primaryStage.setScene(new Scene(root, 630, 379));
        primaryStage.show();

    }

    public static void main (String[] args) {launch(args);}
}
