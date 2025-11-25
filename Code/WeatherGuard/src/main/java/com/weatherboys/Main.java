package com.weatherboys;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the admin FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminView.fxml"));
        Parent root = loader.load();

        // Create the scene (size is set in FXML)
        Scene scene = new Scene(root);

        // Configure the stage (window)
        primaryStage.setTitle("WeatherGuard");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}