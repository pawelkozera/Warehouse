module klient.klient {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.CommunicationModule;
    requires itextpdf;

    opens org.client to javafx.fxml;
    exports org.client;
    exports org.client.Employees;
    opens org.client.Employees to javafx.fxml;
    exports org.client.Authorization;
    opens org.client.Authorization to javafx.fxml;
    exports org.client.Warehouse;
    opens org.client.Warehouse to javafx.fxml;
}