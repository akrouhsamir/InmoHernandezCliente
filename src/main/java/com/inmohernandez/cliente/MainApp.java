package com.inmohernandez.cliente;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-inmuebles-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 800);
        stage.setMinHeight(600);
        stage.setMinWidth(900);
        stage.getIcons().add(new Image(MainApp.class.getResource("imgs/rhinoapp.png").toExternalForm()));
        stage.setTitle("InmoHernandez");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}