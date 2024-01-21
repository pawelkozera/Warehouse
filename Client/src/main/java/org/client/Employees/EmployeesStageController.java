package org.client.Employees;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.CommunicationModule.Employee.Employee;
import org.CommunicationModule.OperationMessageDTO;
import org.client.CommunicationModule.CommunicationUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.client.CommunicationModule.ValidationTextFields.*;

public class EmployeesStageController {
    ObservableList<String>positions= FXCollections.observableArrayList("Sprzedawca", "Magazynier");
    ObservableList<String>genders= FXCollections.observableArrayList("Kobieta", "Mężczyzna");
    ObservableList<String>status= FXCollections.observableArrayList("AKTYWNY", "NIEAKTYWNY");
    private ObservableList<String> idList=FXCollections.observableArrayList();
    private ObservableList<Employee> employeeList;
    @FXML
    private TextField nameTextArea;
    @FXML
    private TextField surnameTextArea;
    @FXML
    private TextField peselTextArea;
    @FXML
    private TextField cityTextarea;
    @FXML
    private TextField streetTextarea;
    @FXML
    private TextField phoneNumberTextArea;
    @FXML
    private TextField numberHouseTextarea;
    @FXML
    private TextField passwordTextArea;
    @FXML
    private TextField loginTextArea;
    @FXML
    private TextField countryTextArea;
    @FXML
    private ChoiceBox<String> genderChoice;
    @FXML
    private ChoiceBox<String> positionChoice;
    @FXML
    private ChoiceBox<String> idChoice;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Employee> employeesTable;
    @FXML
    private TableColumn<Employee, String> firstNameColumn;
    @FXML
    private TableColumn<Employee, String> idColumn;
    @FXML
    private TableColumn<Employee, String> surnameColumn;
    @FXML
    private TableColumn<Employee, String> genderColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, String> phoneNumberColumn;
    @FXML
    private TableColumn<Employee, String> cityColumn;
    @FXML
    private TableColumn<Employee, String> salaryColumn;
    @FXML
    private TableColumn<Employee, String> statusColumn;
    @FXML
    private  Tab employeesTab;
    @FXML
    private  Tab changeTab;
    @FXML
    private TextField changeNameTextArea;
    @FXML
    private TextField changeSurnameTextArea;
    @FXML
    private TextField changePeselTextArea;
    @FXML
    private TextField changeCityTextarea;
    @FXML
    private TextField changeCountryTextArea;
    @FXML
    private TextField changeStreetTextarea;
    @FXML
    private TextField changePhoneNumberTextArea;
    @FXML
    private TextField changeNumberHouseTextarea;
    @FXML
    private TextField changeSalaryTextarea;
    @FXML
    private ChoiceBox<String> changeGenderChoice;
    @FXML
    private ChoiceBox<String> changePositionChoice;
    @FXML
    private ChoiceBox<String> changeEmploymentStatus;
    @FXML
    private TextArea comments;
    @FXML
    private DatePicker changeDatePicker;
    public EmployeesStageController() {}

    private void setMouseClickHandler(Control control) {
        control.setOnMouseClicked(mouseEvent -> {
            control.setStyle("");
        });
    }

    boolean checkData()
    {
        boolean check=true;
        TextField[] textAreas = {nameTextArea, surnameTextArea, cityTextarea,
                streetTextarea, numberHouseTextarea, passwordTextArea, loginTextArea,
                countryTextArea};

        for (TextField textArea : textAreas) {
            if (Objects.equals(textArea.getText(), "")) {
                textArea.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
                check = false;
            }
        }

        if(Objects.equals(phoneNumberTextArea.getText(), "")||phoneNumberTextArea.getText().length()<9)
        {
            phoneNumberTextArea.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }
        if(Objects.equals(peselTextArea.getText(), "")||peselTextArea.getText().length()<11)
        {
            peselTextArea.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }

        if(genderChoice.getValue()==null){
            genderChoice.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }
        if(positionChoice.getValue()==null){
            positionChoice.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }
        if(datePicker.getValue()==null){
            datePicker.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }

        return check;
    }
    boolean checkEditData()
    {
        boolean check=true;
        TextField[] textAreas = {changeNameTextArea,changeSurnameTextArea,changePeselTextArea,changeCountryTextArea,
                changeCityTextarea,changeStreetTextarea,changeNumberHouseTextarea,changeSalaryTextarea,
                changePhoneNumberTextArea};

        for (TextField textArea : textAreas) {
            if (Objects.equals(textArea.getText(), "")) {
                textArea.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
                check = false;
            }
        }

        if(changeGenderChoice.getValue()==null){
            changeGenderChoice.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }
        if(changePositionChoice.getValue()==null){
            changePositionChoice.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }
        if(changeDatePicker.getValue()==null){
            changeDatePicker.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }
        if(changeEmploymentStatus.getValue()==null){
            changeEmploymentStatus.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }
        if(idChoice.getValue()==null){
            idChoice.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6)");
            check=false;
        }

        return check;
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
    private void initializeChangeIdList() {
        idList.clear();
        changeGenderChoice.setItems(genders);
        changePositionChoice.setItems(positions);
        changeEmploymentStatus.setItems(status);

        OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(new OperationMessageDTO<>("LIST_LOGINS_NAMES", null));

        if (response.responseType().equals("LIST_LOGINS_NAMES_SUCCESS")) {
            List<Employee> employeesData = (List<Employee>) response.data();

            for (Employee employee : employeesData) {
                idList.add(employee.login() + ", " + employee.name() + ", " + employee.surname());
            }
        } else {
            System.out.println("Lista logins jest pusta lub dane są nieprawidłowe.");
        }

        idChoice.setItems(idList);
    }

    private void initializeList() {
        OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(new OperationMessageDTO<>("LIST_EMPLOYEES", null));

        if (response.responseType().equals("LIST_EMPLOYEES_SUCCESS")) {
            List<Employee> employeesData = (List<Employee>) response.data();

            List<Employee> updatedEmployeesData = new ArrayList<>();

            for (Employee employee : employeesData) {
                String updatedStatus = Objects.equals(employee.status(), "1") ? "AKTYWNY" : "NIEAKTYWNY";
                Employee updatedEmployee = employee.withStatus(updatedStatus);
                updatedEmployeesData.add(updatedEmployee);
            }

            employeeList = FXCollections.observableArrayList(updatedEmployeesData);

            firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));
            idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().login()));
            surnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().surname()));
            genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().gender()));
            positionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().position()));
            phoneNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().phoneNumber()));
            cityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().city()));
            salaryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().salary()));
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().status()));

            employeesTable.setItems(employeeList);
        } else {
            System.out.println("Lista pracowników jest pusta lub dane są nieprawidłowe.");
        }
    }

    protected void toSelectAnOption(ActionEvent event)
    {
        String data="";
        if (idChoice != null && idChoice.getValue() != null) {
            data = idChoice.getValue();
        }
        if(!data.isEmpty()) {
            ChangeTabActivate();
            int indexComma = data.indexOf(',');
            String username = data.substring(0, indexComma);

            Employee employee = new Employee(username);

            OperationMessageDTO<Employee> request = new OperationMessageDTO<>("GET_EMPLOYEE_BY_LOGIN", employee);
            OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(request);

            if (response.responseType().equals("GET_EMPLOYEE_BY_LOGIN_SUCCESS")) {
                Employee employeeData = (Employee) response.data();
                changeNameTextArea.setText(employeeData.name());
                changeSurnameTextArea.setText(employeeData.surname());
                changePeselTextArea.setText(employeeData.pesel());
                changeCountryTextArea.setText(employeeData.country());
                changeCityTextarea.setText(employeeData.city());
                changeStreetTextarea.setText(employeeData.street());
                changeNumberHouseTextarea.setText(employeeData.houseNumber());
                changeGenderChoice.setValue(employeeData.gender());
                changeSalaryTextarea.setText(employeeData.salary());
                changePositionChoice.setValue(employeeData.position());
                changePhoneNumberTextArea.setText(employeeData.phoneNumber());
                changeDatePicker.setValue(employeeData.dateOfBrith());
                if(employeeData.status().equals("1"))
                    changeEmploymentStatus.setValue("AKTYWNY");
                else
                    changeEmploymentStatus.setValue("NIEAKTYWNY");
                comments.setText(employeeData.comments());
                ChangeTabClearStyle();
            } else {
                System.out.println("Błąd podczas pobierania danych pracownika.");
            }
        }


    }
    private void ChangeTabClearStyle()
    {
        TextField[] textAreas = {changeNameTextArea,changeSurnameTextArea,changePeselTextArea,changeCountryTextArea,
                changeCityTextarea,changeStreetTextarea,changeNumberHouseTextarea,changeSalaryTextarea,
                changePhoneNumberTextArea};

        for (TextField textArea : textAreas) {
            textArea.setStyle("");
        }
        idChoice.setStyle("");
        changePositionChoice.setStyle("");
        changeGenderChoice.setStyle("");
        changeEmploymentStatus.setStyle("");
        changeDatePicker.setStyle("");
    }
    private void ChangeTabClear()
    {
        TextField[] textAreas = {changeNameTextArea,changeSurnameTextArea,changePeselTextArea,changeCountryTextArea,
                changeCityTextarea,changeStreetTextarea,changeNumberHouseTextarea,changeSalaryTextarea,
                changePhoneNumberTextArea};

        for (TextField textArea : textAreas) {
            textArea.setText("");
            textArea.setDisable(true);
        }
        comments.setText("");
        comments.setDisable(true);
        changePositionChoice.setValue(null);
        changeGenderChoice.setValue(null);
        changeEmploymentStatus.setValue(null);
        changeDatePicker.setValue(null);
        idChoice.setValue(null);
        changePositionChoice.setDisable(true);
        changeGenderChoice.setDisable(true);
        changeEmploymentStatus.setDisable(true);
        changeDatePicker.setDisable(true);
        ChangeTabClearStyle();
    }
    protected void ChangeTabActivate()
    {
        TextField[] textAreas = {changeNameTextArea,changeSurnameTextArea,changePeselTextArea,changeCountryTextArea,
                changeCityTextarea,changeStreetTextarea,changeNumberHouseTextarea,changeSalaryTextarea,
                changePhoneNumberTextArea};

        for (TextField textArea : textAreas) {
            textArea.setDisable(false);
        }
        comments.setDisable(false);
        changePositionChoice.setDisable(false);
        changeGenderChoice.setDisable(false);
        changeEmploymentStatus.setDisable(false);
        changeDatePicker.setDisable(false);
    }
    @FXML
    public void onCancelClick()
    {
        ChangeTabClear();
    }
    @FXML
    public void onEmployeesTabClick()
    {
        if(employeesTab.isSelected())
            initializeList();
    }
    @FXML
    public void onChangeTabClick()
    {
        if(changeTab.isSelected())
        initializeChangeIdList();
        else
            ChangeTabClear();
    }
    @FXML
    public void initialize() {
        genderChoice.setItems(genders);
        positionChoice.setItems(positions);

        Control [] controls={nameTextArea,surnameTextArea,peselTextArea,countryTextArea,cityTextarea,streetTextarea,
                numberHouseTextarea,phoneNumberTextArea,genderChoice,positionChoice,datePicker,changeNameTextArea,
                changeSurnameTextArea,changePeselTextArea,changeCountryTextArea,changeCityTextarea,changeStreetTextarea,
                changeNumberHouseTextarea,changeSalaryTextarea,changePhoneNumberTextArea};

        for(Control c: controls)
        setMouseClickHandler(c);

        nameTextArea.setOnKeyTyped(event -> onlyLetters(nameTextArea));
        changeNameTextArea.setOnKeyTyped(event -> onlyLetters(changeNameTextArea));
        surnameTextArea.setOnKeyTyped(event -> onlyLetters(surnameTextArea));
        changeSurnameTextArea.setOnKeyTyped(event -> onlyLetters(changeSurnameTextArea));
        peselTextArea.setOnKeyTyped(event -> onlyNumbers(peselTextArea,11));
        changePeselTextArea.setOnKeyTyped(event -> onlyNumbers(changePeselTextArea,11));
        countryTextArea.setOnKeyTyped(event -> onlyLetters(countryTextArea));
        changeCountryTextArea.setOnKeyTyped(event -> onlyLetters(changeCountryTextArea));
        cityTextarea.setOnKeyTyped(event -> onlyLetters(cityTextarea));
        changeCityTextarea.setOnKeyTyped(event -> onlyLetters(changeCityTextarea));
        streetTextarea.setOnKeyTyped(event -> withoutSpecialChar(streetTextarea));
        changeStreetTextarea.setOnKeyTyped(event -> withoutSpecialChar(changeStreetTextarea));
        numberHouseTextarea.setOnKeyTyped(event -> withoutSpecialChar(numberHouseTextarea));
        changeNumberHouseTextarea.setOnKeyTyped(event -> withoutSpecialChar(changeNumberHouseTextarea));
        phoneNumberTextArea.setOnKeyTyped(event -> onlyNumbers(phoneNumberTextArea,9));
        changePhoneNumberTextArea.setOnKeyTyped(event -> onlyNumbers(changePhoneNumberTextArea,9));
        changeSalaryTextarea.setOnKeyTyped(event -> onlyNumbers(changeSalaryTextarea,7));
        idChoice.setOnAction(this::toSelectAnOption);

    }

    @FXML
    public void clear()
    {
        TextField[] textAreas = {nameTextArea, surnameTextArea, cityTextarea,
                streetTextarea, numberHouseTextarea, passwordTextArea, loginTextArea,
                countryTextArea};

        for (TextField textArea : textAreas) {
            textArea.setStyle("");
            textArea.setText("");
        }

        phoneNumberTextArea.setStyle("");
        peselTextArea.setStyle("");
        phoneNumberTextArea.setText("");
        peselTextArea.setText("");

        genderChoice.setValue(null);
        positionChoice.setValue(null);
        datePicker.setValue(null);
        genderChoice.setStyle("");
        positionChoice.setStyle("");
        datePicker.setStyle("");
    }

    @FXML
    protected void generateLogin()
    {
        loginTextArea.setStyle("");
        Random random = new Random();
        StringBuilder login = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            int digit = random.nextInt(10);
            login.append(digit);
        }
        loginTextArea.setText(login.toString());
    }

    @FXML
    protected void generatePassword()
    {
        passwordTextArea.setStyle("");
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            password.append(randomChar);
        }
        passwordTextArea.setText(password.toString());
    }


    @FXML
    protected void activateAccount() {
        Employee employee;
        if (checkData()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedBirthDate = datePicker.getValue().format(formatter);

            employee = new Employee(
                    nameTextArea.getText(), surnameTextArea.getText(), positionChoice.getValue(),
                    countryTextArea.getText(), cityTextarea.getText(), streetTextarea.getText(),
                    numberHouseTextarea.getText(), genderChoice.getValue(), peselTextArea.getText(),
                    formattedBirthDate, phoneNumberTextArea.getText(), loginTextArea.getText(),
                    passwordTextArea.getText(), "1"
            );

            OperationMessageDTO<Employee> request = new OperationMessageDTO<>("ADD_EMPLOYEE", employee);
            OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(request);

            if (response.responseType().equals("ADD_EMPLOYEE_SUCCESS")) {
                showAlert(Alert.AlertType.INFORMATION, "Dane poprawne", "Konto zostało pomyślnie aktywowane.");
                clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Wystąpił błąd podczas dodawania danych. Skontaktuj się z administratorem.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Prosimy o poprawne wypełnienie wszystkich pól.");
        }
    }

    @FXML
    protected void editEmployeeData() {
        Employee employee;
        String status;
        String data;
        String username="";
        if(checkEditData())
        {
            if (idChoice != null && idChoice.getValue() != null) {
                data = idChoice.getValue();
                int indexComma = data.indexOf(',');
                username = data.substring(0, indexComma);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedBirthDate = changeDatePicker.getValue().format(formatter);
            if(changeEmploymentStatus.getValue().equals("AKTYWNY"))
                status="1";
            else
                status="0";

            employee=new Employee(changeNameTextArea.getText(),changeSurnameTextArea.getText(),changePositionChoice.getValue(),
                    changeCountryTextArea.getText(),changeCityTextarea.getText(),changeStreetTextarea.getText(),changeNumberHouseTextarea.getText(),
                    changeGenderChoice.getValue(),changePeselTextArea.getText(),formattedBirthDate,changePhoneNumberTextArea.getText(),username,"",status,changeSalaryTextarea.getText(),
                    comments.getText());

            OperationMessageDTO<Employee> request = new OperationMessageDTO<>("EDIT_EMPLOYEE", employee);
            OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(request);

            if(response.responseType().equals("EDIT_EMPLOYEE_SUCCESS")){
                showAlert(Alert.AlertType.INFORMATION, "Dane poprawne", "Dane pracownika zaktualizowane.");
                ChangeTabClear();
                initializeChangeIdList();
            }
         else {
                showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Wystąpił błąd podczas aktualizowania danych. Skontaktuj się z administratorem.");
            }
        }
        else
        {
            showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Prosimy o poprawne wypełnienie wszystkich pól.");
        }
    }
}
