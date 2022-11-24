import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler {
    private static final String KEY_END = "/end";
    private static final String KEY_AUTH_OK = "/auth_ok ";
    private static final String KEY_AUTH = "/auth";
    private static final String KEY_WHISPER = "/w ";
    private static final String KEY_CLIENTS = "/clients ";
    private static final String KEY_REG = "/reg";
    private static final String KEY_RESULT_OK = "/result ok";
    private static final String KEY_RESULT_FAILED = "/result failed";
    Server server;
    Socket socket;
    DataInputStream inSocket;
    DataOutputStream outSocket;
    private String nick;

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
                                String newNick = server.getAuthenticationService().getNickByLoginAndPassword(token[1], token[2]);
                                if (newNick != null) {
                                    nick = newNick;
                                    login = token[1];
                                    if (!server.isAuthorized(login)) {
                                        outSocket.writeUTF(String.format("%s %s", KEY_AUTH_OK, nick));
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
                            if (server.getAuthenticationService().registration(regLogin, regPassword, regNick)) {
                                sendMessage(KEY_RESULT_OK);
                            } else {
                                sendMessage(KEY_RESULT_FAILED);
                            }

                        }

                    }
                    socket.setSoTimeout(0);
                    while (true) {
                        String strInSocket = inSocket.readUTF();
                        if (strInSocket.equals(KEY_END)) {
                            outSocket.writeUTF(KEY_END);
                            break;
                        } else if (strInSocket.startsWith(KEY_WHISPER)) {
                            String[] token = strInSocket.split("\\s");
                            String nickTo = token[1];
                            int lastIndexNick = strInSocket.indexOf(nickTo) + nickTo.length();
                            String subString1 = strInSocket.substring(0, lastIndexNick);
                            String subString2 = strInSocket.substring(lastIndexNick).trim();
                            String message = subString1 + " from " + nick + ": " + subString2;
                            server.whisperMessage(message, nickTo, nick);

                        } else {
                            server.broadcastMessage(nick + ": " + strInSocket);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    try {
                        outSocket.writeUTF(KEY_END);
                    } catch (IOException ex) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.printf("Client %s disconnected\n", nick);
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
            outSocket.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
