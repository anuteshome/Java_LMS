package com.library;

import com.library.database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/fxml/MainView.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 700);

        // Apply stylesheet
        scene.getStylesheets().add(getClass().getResource("/com/library/fxml/style.css").toExternalForm());

        primaryStage.setTitle("📚 Library Management System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
