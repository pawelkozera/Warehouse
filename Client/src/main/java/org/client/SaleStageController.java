package org.client;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.CommunicationModule.*;
import org.CommunicationModule.Commodity.Commodity;
import org.CommunicationModule.Commodity.CommodityRelease;
import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.Invoice.Invoice;
import org.CommunicationModule.Invoice.InvoiceCommodities;
import org.CommunicationModule.Stat.InvoicesStatistics;
import org.client.Authorization.UserSession;
import org.client.CommunicationModule.CommunicationUtils;
import org.client.Invoice.Buyer;
import org.client.Invoice.GenerateInvoicePDF;
import org.client.Invoice.Product;
import org.client.Invoice.Seller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.client.CommunicationModule.ValidationTextFields.*;

public class SaleStageController {
    private final ObservableList<String> commoditiesList = FXCollections.observableArrayList();
    private final ObservableList<Commodity> commodities = FXCollections.observableArrayList();

    //Pola ekranu faktur
    @FXML
    private TextField employeeNameTextField;
    @FXML
    private TextField employeeSurnameTextField;
    @FXML
    private TextField employeeIDTextField;
    @FXML
    private TextField invoiceIDTextField;
    @FXML
    private TextField dateTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField peselTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField houseNumberTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField nipTextField;
    @FXML
    private TextField sumTextField;
    @FXML
    private TextField commodityIDTextField;
    @FXML
    private TextField commodityNameTextField;
    @FXML
    private TextField commodityCategoryTextField;
    @FXML
    private TextField commodityProducerTextField;
    @FXML
    private TextField numberCommodityTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private ChoiceBox<String> commodityChoiceBox;
    @FXML
    private Tab invoiceTab;
    @FXML
    public TableView<Commodity> invoiceTabView;
    @FXML
    private TableColumn<Commodity, String> idColumn;
    @FXML
    private TableColumn<Commodity, String> nameColumn;
    @FXML
    private TableColumn<Commodity, String> producerColumn;
    @FXML
    private TableColumn<Commodity, String> numberColumn;
    @FXML
    private TableColumn<Commodity, String> priceColumn;
    @FXML
    private TableColumn<Commodity, Button> deleteColumn;
    //Pola ekranu historii
    @FXML
    private ComboBox<Integer> yearPicker;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxisSum;
    @FXML
    private NumberAxis yAxisSum;
    @FXML
    private Tab historyTab;
    @FXML
    private BarChart<String, Number> numberChart;
    @FXML
    private BarChart<String, Number> sumChart;
    //Metody ekranu faktur
    protected  String idInvoiceGenerate()
    {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateString = currentDate.format(formatter);
        return dateString;
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }
    protected void invoiceTabClear()
    {
        TextField[] textAreas = {nameTextField,surnameTextField,peselTextField,cityTextField,streetTextField,houseNumberTextField,phoneNumberTextField,nipTextField,
                sumTextField,commodityIDTextField,commodityNameTextField,commodityCategoryTextField,commodityProducerTextField,numberCommodityTextField};

        for (TextField textArea : textAreas) {
            textArea.setText("");
            textArea.setStyle("");
        }
        commodityChoiceBox.setStyle("");
        commodityChoiceBox.setValue(null);
        invoiceTabView.getItems().clear();
    }
    private void setMouseClickHandler(Control control) {
        control.setOnMouseClicked(mouseEvent -> {
            control.setStyle("");
        });
    }
    protected void columnMapping()
    {
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().id()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().productName()));
        producerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().producer()));
        numberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().quantityToSell()));

        priceColumn.setCellValueFactory(cellData -> {
            Commodity commodity = cellData.getValue();
            String quantityString = commodity.quantityToSell();
            String priceString = commodity.price();

            try {
                double quantity = Double.parseDouble(quantityString);
                double price = Double.parseDouble(priceString);
                double totalPrice = quantity * price;

                return new SimpleStringProperty(String.valueOf(totalPrice));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return new SimpleStringProperty("Błąd konwersji danych");
            }
        });
        deleteColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Commodity, Button> call(TableColumn<Commodity, Button> param) {
                return new TableCell<>() {
                    final Button deleteButton = new Button("Usuń");
                    {
                        deleteButton.setOnAction(event -> {
                            Commodity commodity = getTableView().getItems().get(getIndex());
                            commodities.remove(commodity);
                            sumTextField.setText(String.valueOf(createSum()));
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

    protected void toSelectCommodity(ActionEvent event)
    {
        String data="";
        if (commodityChoiceBox != null && commodityChoiceBox.getValue() != null) {
            data = commodityChoiceBox.getValue();
        }
        if(!data.isEmpty()) {
            int indexComma = data.indexOf(',');
            String id = data.substring(0, indexComma);

            Commodity commodity = new Commodity(id);

            OperationMessageDTO<Commodity> request = new OperationMessageDTO<>("GET_COMMODITY_BY_ID", commodity);
            OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(request);

            if (response.responseType().equals("GET_COMMODITY_BY_ID_SUCCESS")) {
                Commodity commodityData = (Commodity) response.data();
                commodityIDTextField.setText(commodityData.id());
                commodityNameTextField.setText(commodityData.productName());
                commodityCategoryTextField.setText(commodityData.category());
                commodityProducerTextField.setText(commodityData.producer());
                priceTextField.setText(commodityData.price());

            } else {
                System.out.println("Błąd podczas pobierania danych towaru.");
            }
        }
    }
    protected void createNumberChart(List<InvoicesStatistics> data, int year) {
        numberChart.getData().clear();
        xAxis.setLabel("Miesiące");
        yAxis.setLabel("Ilość faktur");
        numberChart.setAnimated(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Integer> months = new ArrayList<>();

        for (InvoicesStatistics invoicesStatistic : data) {
            if (Integer.parseInt(invoicesStatistic.year()) == year) {
                series.getData().add(new XYChart.Data<>(invoicesStatistic.month(), invoicesStatistic.invoiceCount()));
                months.add(Integer.parseInt(invoicesStatistic.month()));
            }
        }

        for (int i = 1; i <= 12; i++) {
            if (!months.contains(i)) {
                String formattedI = String.format("%02d", i);
                series.getData().add(new XYChart.Data<>(formattedI, 0));
            }
        }

        series.getData().sort(Comparator.comparing(dataItem -> Integer.parseInt(dataItem.getXValue())));
        numberChart.getData().add(series);
    }

    protected void toSelectYear(ActionEvent event)
    {
        int year=0;
        if (yearPicker != null && yearPicker.getValue() != null) {
            year = yearPicker.getValue();
        }
        if(year!=0) {

            InvoicesStatistics invoicesStatistics=new InvoicesStatistics(String.valueOf(year),"",0,"");
            OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(new OperationMessageDTO<>("GET_INVOICE_STATS", invoicesStatistics));

            if (response.responseType().equals("LIST_OF_INVOICE_STATS_SUCCESS")) {
                List<InvoicesStatistics> data = (List<InvoicesStatistics>) response.data();
                createSumChart(data,year);
                createNumberChart(data,year);
            }
            else
            {
                showAlert(Alert.AlertType.ERROR,"Błąd przetważania danych","Skontaktuj sie z administratorem");
            }
        }
    }

    protected void createSumChart(List<InvoicesStatistics> data, int year) {
        sumChart.getData().clear();
        xAxisSum.setLabel("Miesiące");
        yAxisSum.setLabel("Wartość sprzedaży (PLN)");
        sumChart.setAnimated(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Integer> months = new ArrayList<>();

        for (InvoicesStatistics invoicesStatistic : data) {
            if (Integer.parseInt(invoicesStatistic.year()) == year) {
                series.getData().add(new XYChart.Data<>(invoicesStatistic.month(), Double.parseDouble(invoicesStatistic.amount())));
                months.add(Integer.parseInt(invoicesStatistic.month()));
            }
        }

        for (int i = 1; i <= 12; i++) {
            if (!months.contains(i)) {
                String formattedI = String.format("%02d", i);
                series.getData().add(new XYChart.Data<>(formattedI, 0.0));
            }
        }

        series.getData().sort(Comparator.comparing(dataItem -> Integer.parseInt(dataItem.getXValue())));
        sumChart.getData().add(series);
    }
    protected boolean checkQuantity (int quantity, String commodityID)
    {
        Commodity commodity = new Commodity(commodityID);
        OperationMessageDTO<Commodity> request = new OperationMessageDTO<>("GET_COMMODITY_BY_ID", commodity);
        OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(request);

        if (response.responseType().equals("GET_COMMODITY_BY_ID_SUCCESS")) {
            Commodity commodityData = (Commodity) response.data();

            if(Integer.parseInt(commodityData.quantityToSell())<quantity)
                return false;


        } else {
            System.out.println("Błąd podczas pobierania danych towaru.");
            return false;
        }
        return true;
    }
    @FXML
    public void initialize() {
        Control [] controls={commodityChoiceBox,numberCommodityTextField,nameTextField,surnameTextField,peselTextField,
                cityTextField,streetTextField,houseNumberTextField,phoneNumberTextField,nipTextField,sumTextField};
        LocalDate currentDate = LocalDate.now();
        String dateString = currentDate.toString();
        dateTextField.setText(dateString);
        employeeIDTextField.setText(UserSession.getInstance().getUsername());
        employeeNameTextField.setText(UserSession.getInstance().getName());
        employeeSurnameTextField.setText(UserSession.getInstance().getSurname());
        invoiceIDTextField.setText(idInvoiceGenerate());

        nameTextField.setOnKeyTyped(event -> onlyLetters(nameTextField));
        surnameTextField.setOnKeyTyped(event -> onlyLetters(surnameTextField));
        peselTextField.setOnKeyTyped(event -> onlyNumbers(peselTextField,11));
        cityTextField.setOnKeyTyped(event -> onlyLetters(cityTextField));
        streetTextField.setOnKeyTyped(event -> withoutSpecialChar(streetTextField));
        houseNumberTextField.setOnKeyTyped(event -> withoutSpecialChar(houseNumberTextField));
        phoneNumberTextField.setOnKeyTyped(event -> onlyNumbers(phoneNumberTextField,9));
        nipTextField.setOnKeyTyped(event -> onlyNumbers(nipTextField,10));
        numberCommodityTextField.setOnKeyTyped(event -> onlyNumbers(numberCommodityTextField,5));
        for(Control c: controls)
            setMouseClickHandler(c);
        commodityChoiceBox.setOnAction(this::toSelectCommodity);
        yearPicker.setOnAction(this::toSelectYear);
        initializeYearPicker();
        columnMapping();
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
    boolean checkCommodityData()
    {
        boolean check=true;
        TextField[] textAreasRl={commodityNameTextField,commodityCategoryTextField,commodityProducerTextField,numberCommodityTextField,commodityIDTextField};
        for (TextField textArea : textAreasRl) {
            if (Objects.equals(textArea.getText(), "")) {
                textArea.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
                check = false;
            }
        }
        if (commodityChoiceBox.getValue()==null) {
            commodityChoiceBox.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check = false;
        }

        return check;
    }
    boolean checkPersonalData()
    {
        boolean check=true;
        TextField[] textAreas = {nameTextField, surnameTextField, cityTextField,
                streetTextField, houseNumberTextField,sumTextField};

        for (TextField textArea : textAreas) {
            if (Objects.equals(textArea.getText(), "")) {
                textArea.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
                check = false;
            }
        }
        if(Objects.equals(phoneNumberTextField.getText(), "")||phoneNumberTextField.getText().length()<9)
        {
            phoneNumberTextField.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }
        if(Objects.equals(peselTextField.getText(), "")||peselTextField.getText().length()<11)
        {
            peselTextField.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }
        if(Objects.equals(nipTextField.getText(), "")||nipTextField.getText().length()<10)
        {
            nipTextField.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }
        return check;
    }
    @FXML
    public void onInvoiceTabClick()
    {
        if(!invoiceTab.isSelected())
            invoiceTabClear();
        else {
            initializeCommodityList(commodityChoiceBox);
        }

    }
    protected boolean checkIDInTable(String commodityID)
    {
        ObservableList<Commodity> allRows = invoiceTabView.getItems();
        for(Commodity commodity: allRows)
        {
            if(Objects.equals(commodity.id(), commodityID))
                return false;
        }
        return true;
    }

    protected double createSum()
    {
        double sum=0.0;
        ObservableList<Commodity> allRows = invoiceTabView.getItems();
        for(Commodity commodity: allRows)
        {
           sum=sum+Double.parseDouble(priceColumn.getCellData(commodity));
        }
      return sum;
    }

    protected void generatePDF() throws DocumentException, IOException {
        try {
            List<Product> products=new ArrayList<>();
            GenerateInvoicePDF generateInvoicePDF = new GenerateInvoicePDF();

            Buyer buyer = new Buyer(nameTextField.getText(), surnameTextField.getText(), peselTextField.getText(), phoneNumberTextField.getText(), cityTextField.getText(), streetTextField.getText(),
                    houseNumberTextField.getText(), nipTextField.getText(), sumTextField.getText());

            Seller seller = new Seller(employeeNameTextField.getText(), employeeSurnameTextField.getText(), employeeIDTextField.getText(), invoiceIDTextField.getText(), dateTextField.getText());

            ObservableList<Commodity> allRows = invoiceTabView.getItems();

            for (Commodity commodity : allRows) {
                Product product = new Product(nameColumn.getCellData(commodity), producerColumn.getCellData(commodity), numberColumn.getCellData(commodity), priceColumn.getCellData(commodity));
                products.add(product);
            }
            generateInvoicePDF.generateInvoice(buyer, seller, products);
        }
        catch (DocumentException|FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected void initializeYearPicker()
    {
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear; year >= currentYear - 5; year--) {
            yearPicker.getItems().add(year);
        }
    }

    @FXML
    public void onClickAddPositionButton()
    {
        if(checkCommodityData())
        {
            String name = commodityNameTextField.getText();
            String producer = commodityProducerTextField.getText();
            String number = numberCommodityTextField.getText();
            String id = commodityIDTextField.getText();
            String price = priceTextField.getText();
            if(checkIDInTable(id)) {
                if (checkQuantity(Integer.parseInt(number), id)) {
                    Commodity c = new Commodity(id, name, null, producer, null, number, price);
                    commodities.add(c);
                    invoiceTabView.setItems(commodities);

                    TextField[] textAreas = {commodityIDTextField, commodityNameTextField, commodityCategoryTextField, commodityProducerTextField, numberCommodityTextField, priceTextField};

                    for (TextField textArea : textAreas) {
                        textArea.setText("");
                        textArea.setStyle("");
                    }
                    commodityChoiceBox.setStyle("");
                    commodityChoiceBox.setValue(null);
                    sumTextField.setText(String.valueOf(createSum()));
                }
                else
                    showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Ilość wybranego towaru jest większa niż stan magazynu");
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
    public void onClickClearTableButton()
    {
        invoiceTabView.getItems().clear();
    }
    @FXML
    public void onCancelClick()
    {
        invoiceTabClear();
    }
    @FXML
    public void onClickAcceptButton() throws DocumentException, IOException {
        if (!invoiceTabView.getItems().isEmpty()) {
            if (checkPersonalData()){

                Invoice invoice=new Invoice(invoiceIDTextField.getText(),"0",nameTextField.getText(),surnameTextField.getText(),
                        peselTextField.getText(),nipTextField.getText(),phoneNumberTextField.getText(),cityTextField.getText(),streetTextField.getText(),
                        houseNumberTextField.getText(),sumTextField.getText(),dateTextField.getText(),String.valueOf(UserSession.getInstance().getId()));
                System.out.println(invoice.employeeId()+"  "+ invoice.purchaseDate()+"  "+invoice.id());
                OperationMessageDTO<Invoice> request = new OperationMessageDTO<>("INSERT_INVOICE", invoice);
                OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(request);

            if (response.responseType().equals("INSERT_INVOICE_SUCCESS")) {

                ObservableList<Commodity> allRows = invoiceTabView.getItems();
                for (Commodity commodity : allRows) {
                    InvoiceCommodities position = new InvoiceCommodities(invoiceIDTextField.getText(),Integer.parseInt(commodity.id()),Integer.parseInt(commodity.quantityToSell()));

                    OperationMessageDTO<InvoiceCommodities> requestIC = new OperationMessageDTO<>("INSERT_INVOICE_COMMODITY", position);
                    OperationMessageDTO<?> responseIC = CommunicationUtils.sendMessageToServer(requestIC);

                    CommodityRelease commodityRelease= new CommodityRelease(Integer.parseInt(commodity.id()),Integer.parseInt(commodity.quantityToSell()));

                    OperationMessageDTO<CommodityRelease> requestSL = new OperationMessageDTO<>("SUBSTRACT_COMMODITY_TO_SELL", commodityRelease);
                    OperationMessageDTO<?> responseSL = CommunicationUtils.sendMessageToServer(requestSL);
                    generatePDF();
                }
                showAlert(Alert.AlertType.CONFIRMATION, "Potwierdzenie", "Faktura została wygenerowana i zapisana w systemie.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Wystąpił błąd podczas aktualizowania danych. Skontaktuj się z administratorem.");
            }
            invoiceTabClear();
        }
            else
                showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Dane osobowe klienta niepoprawne");
        }
        else
        {
            showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Brak danych o kupionych towarach, tabela jest pusta.");
        }
    }
}
