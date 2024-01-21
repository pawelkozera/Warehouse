package org.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainStage extends Stage {
    public MainStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainStage.class.getResource("MainScreen.fxml"));
        super.setScene(new Scene(fxmlLoader.load()));
        super.setTitle("Menu główne");
        super.setResizable(false);
    }
}
