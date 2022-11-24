import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final String KEY_END = "/end";
    private static final String KEY_AUTH_OK = "/auth_ok ";
    private static final String KEY_AUTH = "/auth";
    private static final String KEY_REG = "/reg";
    private static final String WINDOW_TITLE = "Chat";
    private static final String KEY_CLIENTS = "/clients ";
    private static final String KEY_RESULT_OK = "/result ok";
    private static final String KEY_RESULT_FAILED = "/result failed";
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private HBox authPanel;
    @FXML
    private HBox messagePanel;
    @FXML
    private ListView<String> clientList;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textField;
    private Socket socket;
    private DataInputStream inClient;
    private DataOutputStream outClient;
    private final int PORT = 8189;
    private final String SERVER_ADDRESS = "localhost";
    private Stage stage;
    private Stage regStage;
    private RegController regController;

    private boolean isAuthenticated;
    private boolean isTimeout;
    private String nick;

    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
        isTimeout = !isAuthenticated;
        authPanel.setVisible(!isAuthenticated);
        authPanel.setManaged(!isAuthenticated);
        messagePanel.setVisible(isAuthenticated);
        messagePanel.setManaged(isAuthenticated);
        clientList.setVisible(isAuthenticated);
        clientList.setManaged(isAuthenticated);
        if (!isAuthenticated) {
            nick = "";
        }
        setTitle(nick);
        textArea.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(() -> {
            stage = (Stage) (textField.getScene().getWindow());
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    if (socket != null && !socket.isClosed()) {
                        try {
                            outClient.writeUTF(KEY_END);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        });
        setAuthenticated(false);
        regStage = createRegWindow();
    }

    private void connect() {
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);
            inClient = new DataInputStream(socket.getInputStream());
            outClient = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String strInClient = inClient.readUTF();
                        if (strInClient.startsWith(KEY_AUTH_OK)) {
                            nick = strInClient.split("\\s")[1];
                            setAuthenticated(true);
                            break;
                        }
                        if (strInClient.equals(KEY_END)) {
                            setAuthenticated(false);
                            break;
                        }

                        if (strInClient.equals(KEY_RESULT_OK)) {
                            regController.addTextToTextArea("Registration successful");
                        } else if (strInClient.equals(KEY_RESULT_FAILED)) {
                            regController.addTextToTextArea("Registration failed. Possibly nick or login not free");
                        } else {
                            textArea.appendText(strInClient + "\n");
                        }
                    }
                    if (!isTimeout) {
                        while (true) {
                            String strInClient = inClient.readUTF();
                            if (strInClient.equals(KEY_END)) {
                                setAuthenticated(false);
                                break;
                            } else if (strInClient.startsWith(KEY_CLIENTS)) {
                                String[] token = strInClient.split("\\s");
                                Platform.runLater(() -> {
                                    clientList.getItems().clear();
                                    for (int i = 1; i < token.length; i++) {
                                        clientList.getItems().add(token[i]);
                                    }
                                });
                            } else {
                                textArea.appendText(strInClient + "\n");
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inClient.close();
                        outClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onSendButtonClick() {
        getTextFromTextField();
    }

    @FXML
    private void onSendEnter() {
        textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    getTextFromTextField();
                }
            }
        });
    }

    private void getTextFromTextField() {
        try {
            outClient.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void tryToAuth() {

        if (socket == null || socket.isClosed()) {
            connect();
        }

        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        try {
            outClient.writeUTF(String.format("%s %s %s", KEY_AUTH, login, password));
            passwordField.clear();
            passwordField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEnterButtonClick(ActionEvent actionEvent) {
        tryToAuth();
    }

    @FXML
    public void onEnterEnter(ActionEvent actionEvent) {
        passwordField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    tryToAuth();
                }
            }
        });
    }

    private void setTitle(String nick) {
        Platform.runLater(() -> {
            stage.setTitle(WINDOW_TITLE + " : " + nick);
        });
    }

    @FXML
    public void onClickLClientList(MouseEvent mouseEvent) {
        String message = String.format("/w %s ", clientList.getSelectionModel().getSelectedItem().trim());
        textField.setText(message);
    }

    private Stage createRegWindow() {
        final int WIDTH = 230;
        final int HEIGHT = 200;
        final String REG_WINDOW_TITLE = "Register form";
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/reg_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);

            stage.setTitle(REG_WINDOW_TITLE);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            regController = fxmlLoader.getController();
            regController.setController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    public void tryToReg(String login, String password, String nick) {

        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            outClient.writeUTF(String.format("%s %s %s %s", KEY_REG, login, password, nick));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onShowRegWindowButtonClick(ActionEvent actionEvent) {
        regStage.show();
    }
}
