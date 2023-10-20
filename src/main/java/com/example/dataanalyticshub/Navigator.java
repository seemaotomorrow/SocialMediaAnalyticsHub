package com.example.dataanalyticshub;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigator {
    public static void changeScene(ActionEvent event, String fxmlFile, String title){
        Parent root = null;

        try{
            root = FXMLLoader.load(Navigator.class.getResource(fxmlFile));
        } catch (IOException e){
            e.printStackTrace();
        }

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 630, 400));
        stage.show();
    }
}
