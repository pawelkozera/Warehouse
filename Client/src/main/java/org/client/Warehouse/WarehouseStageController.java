package org.client.Warehouse;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.CommunicationModule.*;
import org.CommunicationModule.Commodity.Commodity;
import org.CommunicationModule.Commodity.CommodityReception;
import org.CommunicationModule.Commodity.CommodityRelease;
import org.CommunicationModule.Invoice.Invoice;
import org.CommunicationModule.Reception.Reception;
import org.CommunicationModule.Release.Release;
import org.client.Authorization.UserSession;
import org.client.CommunicationModule.CommunicationUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.client.CommunicationModule.ValidationTextFields.onlyNumbers;

public class WarehouseStageController {

    private final ObservableList<String> commoditiesList = FXCollections.observableArrayList();
    private final ObservableList<String> invoiceList = FXCollections.observableArrayList();
    private final ObservableList<Commodity> commodities = FXCollections.observableArrayList();
    //Elementy ekranu wydawania towaru
    @FXML
    public TableView <Commodity> releaseTabView;
    @FXML
    private TableColumn<Commodity, String> idRlColumn;
    @FXML
    private TableColumn<Commodity, String> nameRlColumn;
    @FXML
    private TableColumn<Commodity, String> producerRlColumn;
    @FXML
    private TableColumn<Commodity, String> numberRlColumn;
    @FXML
    private TableColumn<Commodity, Button> deleteRlColumn;
    @FXML
    private TextField idRLPersonTextField;
    @FXML
    private TextField dateReleaseTextField;
    @FXML
    private TextField idRlTextField;
    @FXML
    private TextField nameRlTextField;
    @FXML
    private TextField categoryRlTextField;
    @FXML
    private TextField producerRlTextField;
    @FXML
    private TextField numberRlTextField;
    @FXML
    private ChoiceBox<String> commodityRlChoice;
    @FXML
    private ChoiceBox<String> invoiceRlChoice;
    @FXML
    private Tab releaseTab;

    //Elementy ekranu wyswietlania towarów
    @FXML
    private Tab warehouseStatusTab;
    @FXML
    public TableView <Commodity> allTabView;
    @FXML
    private TableColumn<Commodity, String> idAllColumn;
    @FXML
    private TableColumn<Commodity, String> nameAllColumn;
    @FXML
    private TableColumn<Commodity, String> categoryAllColumn;
    @FXML
    private TableColumn<Commodity, String> producerAllColumn;
    @FXML
    private TableColumn<Commodity, String> quantityAllColumn;
    @FXML
    private TableColumn<Commodity, String> priceAllColumn;

    //Elementy ekranu przyjęć towarów
    @FXML
    public TableView <Commodity> receptionTabView;
    @FXML
    private TableColumn<Commodity, String> idReColumn;
    @FXML
    private TableColumn<Commodity, String> nameReColumn;
    @FXML
    private TableColumn<Commodity, String> producerReColumn;
    @FXML
    private TableColumn<Commodity, String> numberReColumn;
    @FXML
    private TableColumn<Commodity, Button> deleteReColumn;
    @FXML
    private TextField idRePersonTextField;
    @FXML
    private TextField dateReceptionTextField;
    @FXML
    private TextField idReTextField;
    @FXML
    private TextField nameReTextField;
    @FXML
    private TextField categoryReTextField;
    @FXML
    private TextField producerReTextField;
    @FXML
    private TextField numberReTextField;
    @FXML
    private ChoiceBox<String> commodityReChoice;
    @FXML
    private Tab receptionTab;
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }

    private void setMouseClickHandler(Control control) {
        control.setOnMouseClicked(mouseEvent -> {
            control.setStyle("");
        });
    }
    protected void columnMapping()
    {
        idRlColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().id()));
        numberRlColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().quantityInStock()));
        nameRlColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().productName()));
        producerRlColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().producer()));

        idReColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().id()));
        numberReColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().quantityInStock()));
        nameReColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().productName()));
        producerReColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().producer()));

        idAllColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().id()));
        nameAllColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().productName()));
        categoryAllColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().category()));
        producerAllColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().producer()));
        quantityAllColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().quantityInStock()));
        priceAllColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().price()));
        deleteRlColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Commodity, Button> call(TableColumn<Commodity, Button> param) {
                return new TableCell<>() {
                    final Button deleteButton = new Button("Usuń");
                    {
                        deleteButton.setOnAction(event -> {
                            Commodity commodity = getTableView().getItems().get(getIndex());
                            commodities.remove(commodity);
                        });
                    }
                    @Override
                    protected void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });
        deleteReColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Commodity, Button> call(TableColumn<Commodity, Button> param) {
                return new TableCell<>() {
                    final Button deleteButton = new Button("Usuń");
                    {
                        deleteButton.setOnAction(event -> {
                            Commodity commodity = getTableView().getItems().get(getIndex());
                            commodities.remove(commodity);
                        });
                    }
                    @Override
                    protected void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });
    }
    boolean checkData()
    {
        boolean check=true;
        TextField[] textAreasRl={nameRlTextField,categoryRlTextField,producerRlTextField,numberRlTextField,idRlTextField};
        for (TextField textArea : textAreasRl) {
            if (Objects.equals(textArea.getText(), "")) {
                textArea.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
                check = false;
            }
        }
        if (commodityRlChoice.getValue()==null) {
            commodityRlChoice.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check = false;
        }

        return check;
    }

    private void releaseTabClear()
    {
        TextField[] textAreas = {nameRlTextField,categoryRlTextField,producerRlTextField,numberRlTextField,idRlTextField};

        for (TextField textArea : textAreas) {
            textArea.setText("");
            textArea.setStyle("");
        }
        invoiceRlChoice.setStyle("");
        invoiceRlChoice.setValue(null);
        commodityRlChoice.setStyle("");
        commodityRlChoice.setValue(null);
        releaseTabView.getItems().clear();
    }
    private void receptionTabClear()
    {
        TextField[] textAreas = {nameReTextField,categoryReTextField,producerReTextField,numberReTextField,idReTextField};

        for (TextField textArea : textAreas) {
            textArea.setText("");
            textArea.setStyle("");
        }
        commodityReChoice.setStyle("");
        commodityReChoice.setValue(null);
        receptionTabView.getItems().clear();
    }
    boolean checkDataRe()
    {
        boolean check=true;
        TextField[] textAreasRl={nameReTextField,categoryReTextField,producerReTextField,numberReTextField,idReTextField};
        for (TextField textArea : textAreasRl) {
            if (Objects.equals(textArea.getText(), "")) {
                textArea.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
                check = false;
            }
        }
        if (commodityReChoice.getValue()==null) {
            commodityReChoice.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check = false;
        }

        return check;
    }

    private void initializeInvoiceList() {
        invoiceList.clear();
        OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(new OperationMessageDTO<>("LIST_UNRELEASED_INVOICES", null));

        if (response.responseType().equals("LIST_UNRELEASED_INVOICES_SUCCESS")) {
            List<Invoice> invoicesData = (List<Invoice>) response.data();

            for (Invoice invoice : invoicesData) {
                invoiceList.add(invoice.id());
            }
        } else {
            System.out.println("Lista faktur jest pusta lub dane są nieprawidłowe.");
        }

        invoiceRlChoice.setItems(invoiceList);
    }

    private void initializeCommodityList(ChoiceBox<String> commodityChoice) {
        commoditiesList.clear();
        OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(new OperationMessageDTO<>("LIST_COMMODITIES", null));

        if (response.responseType().equals("LIST_COMMODITIES_SUCCESS")) {
            List<Commodity> commoditiesData = (List<Commodity>) response.data();

            for (Commodity commodity : commoditiesData) {
                commoditiesList.add(commodity.id() + ", " + commodity.productName() + ", " + commodity.productName());
            }
        } else {
            System.out.println("Lista towarów jest pusta lub dane są nieprawidłowe.");
        }

        commodityChoice.setItems(commoditiesList);
    }
    protected void toSelectCommodity(ActionEvent event,ChoiceBox<String> commodityChoice, TextField ID, TextField name, TextField category, TextField producer )
    {
        String data="";
        if (commodityChoice != null && commodityChoice.getValue() != null) {
            data = commodityChoice.getValue();
        }
        if(!data.isEmpty()) {
            int indexComma = data.indexOf(',');
            String id = data.substring(0, indexComma);

            Commodity commodity = new Commodity(id);
            OperationMessageDTO<Commodity> request = new OperationMessageDTO<>("GET_COMMODITY_BY_ID", commodity);
            OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(request);

            if (response.responseType().equals("GET_COMMODITY_BY_ID_SUCCESS")) {
                Commodity commodityData = (Commodity) response.data();
                ID.setText(commodityData.id());
                name.setText(commodityData.productName());
                category.setText(commodityData.category());
                producer.setText(commodityData.producer());

            } else {
                System.out.println("Błąd podczas pobierania danych towaru.");
            }
        }
    }
    protected int generateReceptionID()
    {
        Random random = new Random();
        int uniqueID=random.nextInt(90000);
        OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(new OperationMessageDTO<>("GET_ALL_RECEPTION_ID", null));

        if (response.responseType().equals("GET_ALL_RECEPTION_ID_SUCCESS")) {

            List<Reception> receptionIDs = (List<Reception>) response.data();
            while (containsID(receptionIDs, uniqueID)) {
                uniqueID = random.nextInt(90000);
            }
            return uniqueID;
        }
        else
            return 111;
    }
    private boolean containsID(List<Reception> receptionIDs, int uniqueID) {
        for (Reception reception : receptionIDs) {
            if (reception.id() == uniqueID) {
                return true;
            }
        }
        return false;
    }

    protected boolean checkQuantity (int quantity, String commodityID)
    {
        Commodity commodity = new Commodity(commodityID);
        OperationMessageDTO<Commodity> request = new OperationMessageDTO<>("GET_COMMODITY_BY_ID", commodity);
        OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(request);

        if (response.responseType().equals("GET_COMMODITY_BY_ID_SUCCESS")) {
            Commodity commodityData = (Commodity) response.data();

            if(Integer.parseInt(commodityData.quantityInStock())<quantity)
                return false;


        } else {
            System.out.println("Błąd podczas pobierania danych towaru.");
            return false;
        }
        return true;
    }
    protected boolean checkIDInTable(String commodityID)
    {
        ObservableList<Commodity> allRows = releaseTabView.getItems();
        for(Commodity commodity: allRows)
        {
            if(Objects.equals(commodity.id(), commodityID))
                return false;
        }
        return true;
    }

    protected void initializeCommodityTable() {
        ObservableList<Commodity> commodityTable;
        OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(new OperationMessageDTO<>("LIST_COMMODITIES", null));

        if (response.responseType().equals("LIST_COMMODITIES_SUCCESS")) {
            List<Commodity> commodityData = (List<Commodity>) response.data();
            commodityTable = FXCollections.observableArrayList(commodityData);
            allTabView.setItems(commodityTable);
        } else {
            System.out.println("Lista towarów jest pusta lub dane są nieprawidłowe.");
        }
    }
    @FXML
    public void initialize() {
        Control [] controls={commodityRlChoice,invoiceRlChoice,numberRlTextField,commodityReChoice,numberReTextField};
        LocalDate currentDate = LocalDate.now();
        String dateString = currentDate.toString();
        dateReleaseTextField.setText(dateString);
        idRLPersonTextField.setText(UserSession.getInstance().getUsername());
        dateReceptionTextField.setText(dateString);
        idRePersonTextField.setText(UserSession.getInstance().getUsername());
        commodityRlChoice.setOnAction(event -> toSelectCommodity(event, commodityRlChoice,idRlTextField,nameRlTextField,categoryRlTextField,producerRlTextField));
        commodityReChoice.setOnAction(event -> toSelectCommodity(event, commodityReChoice,idReTextField,nameReTextField,categoryReTextField,producerReTextField));
        numberRlTextField.setOnKeyTyped(event -> onlyNumbers(numberRlTextField,4));
        numberReTextField.setOnKeyTyped(event -> onlyNumbers(numberReTextField,4));
        for(Control c: controls)
            setMouseClickHandler(c);

        columnMapping();
    }

    //Metody ekranu wydawania towaru
    @FXML
    public void onClickAcceptRlButton()
    {
        if (!releaseTabView.getItems().isEmpty()) {

            if(!(invoiceRlChoice.getValue() ==null))
            {
                String releaseID=invoiceRlChoice.getValue();
                Release release=new Release(releaseID,dateReleaseTextField.getText(),UserSession.getInstance().getId(),
                        invoiceRlChoice.getValue());

                OperationMessageDTO<Release> request = new OperationMessageDTO<>("ADD_RELEASE", release);
                OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(request);

                if(response.responseType().equals("ADD_RELEASE_SUCCESS")){

                    Invoice invoice=new Invoice(invoiceRlChoice.getValue(),"true");
                    OperationMessageDTO<Invoice> requestI = new OperationMessageDTO<>("UPDATE_INVOICE_STATUS", invoice);
                    OperationMessageDTO<?> responseI = CommunicationUtils.sendMessageToServer(requestI);

                    ObservableList<Commodity> allRows = releaseTabView.getItems();
                    for(Commodity commodity: allRows)
                    {
                        CommodityRelease position = new CommodityRelease(Integer.parseInt(commodity.id()),releaseID,Integer.parseInt(commodity.quantityInStock()));
                        OperationMessageDTO<CommodityRelease> requestR = new OperationMessageDTO<>("ADD_COMMODITY_RELEASE", position);
                        OperationMessageDTO<?> responseR = CommunicationUtils.sendMessageToServer(requestR);
                        OperationMessageDTO<CommodityRelease> requestS = new OperationMessageDTO<>("SUBSTRACT_COMMODITY_STOCK", position);
                        OperationMessageDTO<?> responseS = CommunicationUtils.sendMessageToServer(requestS);

                    }
                    showAlert(Alert.AlertType.CONFIRMATION, "Potwierdzenie", "Wydanie towarów zostało zarejestrowane w systemie.");
                }
                else
                {
                    showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Wystąpił błąd podczas aktualizowania danych. Skontaktuj się z administratorem.");
                }
                releaseTabClear();
                initializeInvoiceList();
            }
            else
            {
                showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Nie wybrano faktury, z którą powiązane jest wydanie.");
            }
        }
        else
        {
            showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Brak danych o wydawanych towarach, tabela  jest pusta.");
        }
    }
    @FXML
    public void onClickAddPositionRlButton()
    {
        if(checkData())
        {
            String name = nameRlTextField.getText();
            String producer = producerRlTextField.getText();
            String number = numberRlTextField.getText();
            String id = idRlTextField.getText();
            if(checkIDInTable(id)) {
                if (checkQuantity(Integer.parseInt(number), id)) {
                    Commodity c = new Commodity(id, name, null, producer, number, null, null);
                    commodities.add(c);
                    releaseTabView.setItems(commodities);

                    TextField[] textAreas = {nameRlTextField, categoryRlTextField, producerRlTextField, numberRlTextField, idRlTextField};

                    for (TextField textArea : textAreas) {
                        textArea.setText("");
                        textArea.setStyle("");
                    }
                    invoiceRlChoice.setStyle("");
                    invoiceRlChoice.setValue(null);
                    commodityRlChoice.setStyle("");
                    commodityRlChoice.setValue(null);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Ilość wybranego towaru jest większa niż stan magazynu");
                }
            }
            else
            {
                showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Wybrany towar znajduje się już w tabeli.");
            }
        }
        else
        {
            showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Prosimy o poprawne wypełnienie wszystkich pól.");
        }

    }
    @FXML
    public void onClickClearTableRlButton()
    {
        releaseTabView.getItems().clear();
    }
    @FXML
    public void onReleaseTabClick()
    {
        if(!releaseTab.isSelected())
            releaseTabClear();
        else {
            initializeCommodityList(commodityRlChoice);
            initializeInvoiceList();
        }

    }
    //Metody ekranu wyswietlania listy towarow
    @FXML
    public void onWarehouseStatusTabClick()
    {
        if(warehouseStatusTab.isSelected())
            initializeCommodityTable();
    }
    //Metody ekranu przyjec towarow
    @FXML
    public void onReceptionTabClick()
    {
        if(!receptionTab.isSelected())
            receptionTabClear();
        else {
            initializeCommodityList(commodityReChoice);
        }
    }
    @FXML
    public void onClickAddPositionReButton()
    {
        if(checkDataRe())
        {
            String name = nameReTextField.getText();
            String producer = producerReTextField.getText();
            String number = numberReTextField.getText();
            String id = idReTextField.getText();
            if(checkIDInTable(id)) {
                Commodity c = new Commodity(id, name, null, producer, number, null, null);
                commodities.add(c);
                receptionTabView.setItems(commodities);

                TextField[] textAreas = {nameReTextField, categoryReTextField, producerReTextField, numberReTextField, idReTextField};

                for (TextField textArea : textAreas) {
                    textArea.setText("");
                    textArea.setStyle("");
                }
                commodityReChoice.setStyle("");
                commodityReChoice.setValue(null);
            }
            else
            {
                showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Wybrany towar znajduje się już w tabeli.");
            }
        }
        else
        {
            showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Prosimy o poprawne wypełnienie wszystkich pól.");
        }
    }
    @FXML
    public void onClickClearTableReButton()
    {
        receptionTabView.getItems().clear();
        int i =generateReceptionID();
        System.out.println(i);
    }
    @FXML
    public void onClickAcceptReButton()
    {
        if (!receptionTabView.getItems().isEmpty()) {

            int receptionID=generateReceptionID();
            Reception reception=new Reception(receptionID,dateReceptionTextField.getText(),UserSession.getInstance().getId());

            OperationMessageDTO<Reception> request = new OperationMessageDTO<>("ADD_RECEPTION", reception);
            OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(request);

            if(response.responseType().equals("ADD_RECEPTION_SUCCESS")){

                ObservableList<Commodity> allRows = receptionTabView.getItems();
                for(Commodity commodity: allRows)
                {
                    CommodityReception position = new CommodityReception(0,Integer.parseInt(commodity.id()),receptionID,Integer.parseInt(commodity.quantityInStock()));
                    OperationMessageDTO<CommodityReception> requestR = new OperationMessageDTO<>("ADD_COMMODITY_RECEPTION", position);
                    OperationMessageDTO<?> responseR = CommunicationUtils.sendMessageToServer(requestR);
                    OperationMessageDTO<CommodityReception> requestS = new OperationMessageDTO<>("ADD_COMMODITY_STOCK", position);
                    OperationMessageDTO<?> responseS = CommunicationUtils.sendMessageToServer(requestS);
                    OperationMessageDTO<CommodityReception> requestSL = new OperationMessageDTO<>("ADD_COMMODITY_TO_SELL", position);
                    OperationMessageDTO<?> responseSL = CommunicationUtils.sendMessageToServer(requestSL);
                }
                showAlert(Alert.AlertType.CONFIRMATION, "Potwierdzenie", "Przyjęcie towarów zostało zarejestrowane w systemie.");
            }
            else
            {
                showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Wystąpił błąd podczas aktualizowania danych. Skontaktuj się z administratorem.");
            }
            receptionTabClear();
        }
        else
        {
            showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Brak danych o przyjmowanych towarach, tabela  jest pusta.");
        }
    }
}