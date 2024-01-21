package org.client.Authorization;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.CommunicationModule.Employee.Employee;
import org.client.CommunicationModule.CommunicationUtils;
import org.client.MainStage;

import org.CommunicationModule.*;
import java.io.IOException;

public class LoginScreenController {
    @FXML
    private Button loginButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text errorMessage;
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }
    @FXML
    protected void login() throws IOException {

        String login;
        String password;

        try {
            login=loginField.getText();
            password=passwordField.getText();
            if(!login.isEmpty()&&!password.isEmpty()) {

                Employee employee = new Employee(login, password);
                OperationMessageDTO<Employee> loginMessageDTO = new OperationMessageDTO<>("LOGIN", employee);
                OperationMessageDTO<?> response = CommunicationUtils.sendMessageToServer(loginMessageDTO);

                if (response.responseType().equals("LOGIN_SUCCESS")) {
                    Employee user=(Employee) response.data();
                    UserSession.getInstance().setUsername(login);
                    UserSession.getInstance().setPassword(password);
                    UserSession.getInstance().setId(user.id());
                    UserSession.getInstance().setPosition(user.position());
                    UserSession.getInstance().setName(user.name());
                    UserSession.getInstance().setSurname(user.surname());
                    ((Stage) loginField.getScene().getWindow()).close();
                    Stage stage = new MainStage();
                    stage.show();
                }
                else
                {
                    showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Dane do logowania niepoprawne");
                }
            }
            else
            {
                showAlert(Alert.AlertType.ERROR, "Dane niepoprawne", "Uzupełnij poprawnie wszystkie pola.");
            }
        }
         catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    protected void clear()
    {
        passwordField.clear();
        loginField.clear();
        errorMessage.setText("");
    }


}
