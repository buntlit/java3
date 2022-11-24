import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleAuthenticationService implements AuthenticationService {

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;


    public SimpleAuthenticationService() {
        try {
            connectToData();
            System.out.println("Connect to db");
//            for (int i = 1; i <= 10; i++) {
//                statement.executeUpdate("INSERT INTO users (login, password, nickname, history) VALUES ('l" + i + "', 'p" + i + "', 'nick" + i + "', '');");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectToData() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:users_info.db");
        statement = connection.createStatement();
    }

    @Override
    public String getNickAndHistoryByLoginAndPassword(String login, String password) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT nickname, history FROM users WHERE login == ? AND password == ?;");
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        return String.format("%s %s",resultSet.getString(1), resultSet.getString(2));
    }

    @Override
    public boolean registration(String login, String password, String nick) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE login == ? OR nickname == ?");
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, nick);
        ResultSet resultSet = preparedStatement.executeQuery();
        String idFromDB = resultSet.getString(1);

        if (idFromDB != null) {
            return false;
        } else {
            preparedStatement = connection.prepareStatement("INSERT INTO users (login, password, nickname, history) VALUES (?, ?, ?, '');");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, nick);
            preparedStatement.executeUpdate();
            return true;
        }
    }

    @Override
    public boolean changeNick(String oldNick, String newNick) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE nickname == ?");
        preparedStatement.setString(1, newNick);
        ResultSet resultSet = preparedStatement.executeQuery();
        String idFromDB = resultSet.getString(1);

        if (idFromDB != null) {
            return false;
        } else {
            preparedStatement = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname == ?;");
            preparedStatement.setString(1, newNick);
            preparedStatement.setString(2, oldNick);
            preparedStatement.executeUpdate();
            return true;
        }
    }

    @Override
    public void disconnectData() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Disconnect from db");
    }

    @Override
    public void historizeMessageList(String nick, String message) throws SQLException {
        preparedStatement = connection.prepareStatement("UPDATE users SET history = ? WHERE nickname == ?;");
        preparedStatement.setString(1, message);
        preparedStatement.setString(2, nick);
        preparedStatement.executeUpdate();
    }
}
