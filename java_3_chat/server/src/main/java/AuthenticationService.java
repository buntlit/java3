import java.sql.SQLException;

public interface AuthenticationService {
    String getNickAndHistoryByLoginAndPassword(String login, String password) throws SQLException;
    boolean registration(String login, String password, String nick) throws SQLException;
    boolean changeNick(String oldNick, String newNick) throws SQLException;
    void disconnectData();
    void historizeMessageList(String nick, String message) throws SQLException;
}
