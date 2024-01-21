package org.client.Sale;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.client.MainStage;

import java.io.IOException;

public class SaleStage extends Stage {
    public SaleStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainStage.class.getResource("Sale.fxml"));
        super.setScene(new Scene(fxmlLoader.load()));
        super.setTitle("Sprzedaż");
        super.setHeight(780.0);
        super.setWidth(860.0);
        super.setResizable(false);
    }
}
