public interface AuthenticationService {
    String getNickByLoginAndPassword(String login, String password);
    boolean registration(String login, String password, String nick);
}
