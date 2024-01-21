package org.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WarehouseApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Zaloguj się");
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}