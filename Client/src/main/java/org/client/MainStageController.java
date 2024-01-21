package org.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.client.Employees.EmployeesStage;
import org.client.Sale.SaleStage;
import org.client.Warehouse.WarehouseStage;

import java.io.IOException;

public class MainStageController {
    @FXML
    private Button employeesButton;
    @FXML
    private Button warehouseButton;
    @FXML
    protected void employees() throws IOException {

        Stage stage = new EmployeesStage();
        stage.show();
    }
    @FXML
    protected void warehouse() throws IOException {

        Stage stage = new WarehouseStage();
        stage.show();
    }
    @FXML
    protected void sale() throws IOException {

        Stage stage = new SaleStage();
        stage.show();
    }
}
