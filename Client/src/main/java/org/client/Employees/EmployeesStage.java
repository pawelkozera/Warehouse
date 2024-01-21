package org.client.Employees;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.client.MainStage;

import java.io.IOException;

public class EmployeesStage extends Stage {

    public EmployeesStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainStage.class.getResource("Employees.fxml"));
        super.setScene(new Scene(fxmlLoader.load()));
        super.setTitle("Pracownicy");
        super.setHeight(650.0);
        super.setWidth(806.0);
        super.setResizable(false);
    }

}
