package com.example.pixaid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Parent welcomeRoot = FXMLLoader.load(getClass().getResource("welcomescreen.fxml"));
            Scene welcomeScene = new Scene(welcomeRoot);
            Image icon = new Image(getClass().getResourceAsStream("pixAid_icon.png"));
            stage.getIcons().add(icon);
            stage.setTitle("pixAid");
            stage.setScene(welcomeScene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error occurred while loading the application.");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}