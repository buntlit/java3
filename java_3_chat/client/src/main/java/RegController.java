import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class RegController {
    private MainController controller;

    public void setController(MainController controller) {
        this.controller = controller;
    }

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nickField;
    @FXML
    private TextArea textArea;


    @FXML
    public void onRegistrationEnter(ActionEvent actionEvent) {
        nickField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    tryToReg();
                }
            }
        });
    }

    @FXML
    public void onRegistrationButtonClick(ActionEvent actionEvent) {
        tryToReg();
    }

    public void tryToReg() {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        String nick = nickField.getText().trim();
        if (!login.equals("") || !password.equals("") || !nick.equals("")) {
            controller.tryToReg(login, password, nick);
        }
    }

    @FXML
    public void onCancelButtonClick(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            ((Stage)textArea.getScene().getWindow()).close();
        });
    }

    public void addTextToTextArea(String message){
        textArea.appendText(message);
    }
}
