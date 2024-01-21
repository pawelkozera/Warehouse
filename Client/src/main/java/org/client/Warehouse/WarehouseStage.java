package org.client.Warehouse;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.client.Employees.EmployeesStage;
import org.client.MainStage;

import java.io.IOException;

public class WarehouseStage extends Stage {
    public WarehouseStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainStage.class.getResource("Warehouse.fxml"));
        super.setScene(new Scene(fxmlLoader.load()));
        super.setTitle("Magazyn");
        super.setHeight(650.0);
        super.setWidth(806.0);
        super.setResizable(false);
    }
}
