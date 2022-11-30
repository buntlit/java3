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

public class ChangeNickController {
    private MainController controller;

    public void setController(MainController controller) {
        this.controller = controller;
    }

    @FXML
    private TextField nickField;
    @FXML
    private TextArea textArea;


    @FXML
    public void onChangeNickEnter(ActionEvent actionEvent) {
        nickField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    tryToChangeNick();
                }
            }
        });
    }

    @FXML
    public void onChangeNickButtonClick(ActionEvent actionEvent) {
        tryToChangeNick();
    }

    public void tryToChangeNick() {
        String nick = nickField.getText().trim();
        if (!nick.equals("")) {
            controller.tryToChangeNick(nick);
        }
        nickField.clear();
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
