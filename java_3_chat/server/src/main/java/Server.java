import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;


public class Server {
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthenticationService authenticationService;

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public Server() {
        clients = new Vector<>();
        authenticationService = new SimpleAuthenticationService();
        ServerSocket server = null;
        Socket socket;
        try {
            server = new ServerSocket(PORT);
            System.out.println("Server started");
            while (true) {
                socket = server.accept();
                System.out.println("Remote Socket Address: " + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void whisperMessage(String message, String nickTo, String nickFrom) {
        for (ClientHandler client : clients) {
            if (client.getNick().equals(nickTo) || client.getNick().equals(nickFrom)) {
                client.sendMessage(message);
            }

        }
    }

    public void broadcastClients(String message) {
        for (ClientHandler client : clients) {
            message = message.concat(client.getNick()) + " ";
           }
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public boolean isAuthorized(String login) {
        for (ClientHandler client : clients) {
            if (client.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public void subscribe(ClientHandler clientHandler, String key) {
        clients.add(clientHandler);
        broadcastClients(key);
    }

    public void unsubscribe(ClientHandler clientHandler, String key) {
        clients.remove(clientHandler);
        broadcastClients(key);
    }


}
