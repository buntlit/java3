import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;


public class Server {
    private final int PORT = 8189;
    private static final String KEY_END = "/end";
    private List<ClientHandler> clients;
    private AuthenticationService authenticationService;
    private ExecutorService executorService;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private Handler fileHandler;

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Logger getLogger() {
        return logger;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public Server() {
        clients = new Vector<>();
        executorService = Executors.newCachedThreadPool();
        ServerSocket server = null;
        Socket socket;
        try {
            server = new ServerSocket(PORT);
            logger.setUseParentHandlers(false);
            fileHandler = new FileHandler("logs/log_%g.log", 5*1024, 3, true);
//            fileHandler = new ConsoleHandler();
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord logRecord) {
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a dd.MM.yyyy");
                    return String.format("%s >>>>> %s: %s\n",dateFormat.format(date), logRecord.getLevel(), logRecord.getMessage());
                }
            });
            fileHandler.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
            logger.info("Server started");
            authenticationService = new SimpleAuthenticationService(logger);
            while (true) {
                socket = server.accept();
                logger.info("Remote Socket Address: " + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                broadcastMessage(KEY_END);
                authenticationService.disconnectData();
                executorService.shutdown();
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

    public void update(String key) {
        broadcastClients(key);
    }

}
