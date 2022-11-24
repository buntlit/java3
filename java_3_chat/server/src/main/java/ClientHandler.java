import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

public class ClientHandler {
    private static final String KEY = "/";
    private static final String KEY_END = "/end";
    private static final String KEY_AUTH_OK = "/auth_ok ";
    private static final String KEY_AUTH = "/auth";
    private static final String KEY_WHISPER = "/w ";
    private static final String KEY_CLIENTS = "/clients ";
    private static final String KEY_REG = "/reg";
    private static final String KEY_CHANGE_NICK = "/change";
    private static final String KEY_REGISTRATION_RESULT_OK = "/registration result ok";
    private static final String KEY_REGISTRATION_RESULT_FAILED = "/registration result failed";
    private static final String KEY_CHANGE_NICK_RESULT_OK = "/change nick result ok";
    private static final String KEY_CHANGE_NICK_RESULT_FAILED = "/change nick result failed";

    Server server;
    Socket socket;
    DataInputStream inSocket;
    DataOutputStream outSocket;
    private String nick;
    private String history;

    private String login;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            inSocket = new DataInputStream(socket.getInputStream());
            outSocket = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {

                    while (true) {
                        socket.setSoTimeout(5000);
                        String strInSocket = inSocket.readUTF();
                        if (strInSocket.startsWith(KEY_AUTH)) {
                            String[] token = strInSocket.split("\\s");
                            if (token.length > 2) {
                                String newNick = null;
                                try {
                                    String[] subStrings = server.getAuthenticationService().
                                            getNickAndHistoryByLoginAndPassword(token[1], token[2]).split("\\s", 2);
                                    newNick = subStrings[0];
                                    history = subStrings[1];
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if (newNick != null) {
                                    nick = newNick;
                                    login = token[1];
                                    if (!server.isAuthorized(login)) {
                                        sendMessage(String.format("%s%s", KEY_AUTH_OK, nick));
                                        server.subscribe(this, KEY_CLIENTS);
                                        System.out.printf("Client %s connected\n", nick);
                                        break;
                                    } else {
                                        sendMessage("User already connected");
                                    }
                                } else {
                                    sendMessage("Incorrect login/password");
                                }
                            }
                        } else if (strInSocket.startsWith(KEY_REG)) {
                            String[] token = strInSocket.split("\\s");
                            String regLogin = token[1];
                            String regPassword = token[2];
                            String regNick = token[3];
                            try {
                                if (server.getAuthenticationService().registration(regLogin, regPassword, regNick)) {
                                    sendMessage(KEY_REGISTRATION_RESULT_OK);
                                } else {
                                    sendMessage(KEY_REGISTRATION_RESULT_FAILED);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                    socket.setSoTimeout(0);
                    if (!history.equals("")) {
                        outSocket.writeUTF(history);
                    }
                    while (true) {
                        String strInSocket = inSocket.readUTF();
                        if (strInSocket.equals(KEY_END)) {
                            sendMessage(KEY_END);
                            break;
                        } else if (strInSocket.startsWith(KEY_WHISPER)) {
                            String[] token = strInSocket.split("\\s");
                            String nickTo = token[1];
                            int lastIndexNick = strInSocket.indexOf(nickTo) + nickTo.length();
                            String subString1 = strInSocket.substring(0, lastIndexNick);
                            String subString2 = strInSocket.substring(lastIndexNick).trim();
                            String message = String.format("%s from %s: %s", subString1, nick, subString2);
                            server.whisperMessage(message, nickTo, nick);
                        } else if (strInSocket.startsWith(KEY_CHANGE_NICK)) {
                            System.out.println(strInSocket);
                            String[] token = strInSocket.split("\\s", 2);
                            String newNick = token[1];
                            try {
                                if (server.getAuthenticationService().changeNick(nick, newNick)) {
                                    sendMessage(KEY_CHANGE_NICK_RESULT_OK);
                                    server.broadcastMessage(String.format("Person %s changed nickname to %s", nick, newNick));
                                    nick = newNick;
                                    server.update(KEY_CLIENTS);
                                } else {
                                    sendMessage(KEY_CHANGE_NICK_RESULT_FAILED);
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            server.broadcastMessage(String.format("%s: %s", nick, strInSocket));
                        }
                    }
                } catch (SocketTimeoutException e) {
                    sendMessage(KEY_END);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.printf("Client %s disconnected\n", nick);
                    try {
                        if (history.endsWith("\n")) {
                            history = history.substring(0, history.length() - 1);
                        }
                        server.getAuthenticationService().historizeMessageList(nick, history);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    server.unsubscribe(this, KEY_CLIENTS);
                    try {
                        inSocket.close();
                        outSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getNick() {
        return nick;
    }

    public String getLogin() {
        return login;
    }


    public void sendMessage(String message) {
        try {
            if (message.startsWith(KEY_WHISPER) || !message.startsWith(KEY)) {
                history += message + "\n";
            }
            outSocket.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
