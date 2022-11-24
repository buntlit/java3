import java.util.ArrayList;
import java.util.List;

public class SimpleAuthenticationService implements AuthenticationService {

    private class UserData {
        String login;
        String password;
        String nickname;
//        boolean isAuth;


        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    private List<UserData> users;

    public SimpleAuthenticationService() {
        users = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            users.add(new UserData("l" + i, "p" + i, "nick" + i));

        }
    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if (user.login.equals(login) && user.password.equals(password)) {
                return user.nickname;
            }
        }
        return null;
    }

    @Override
    public boolean registration(String login, String password, String nick) {
        for (UserData user : users) {
            if (user.login.equals(login)||user.nickname.equals(nick)){
                return false;
            }
        }
        users.add(new UserData(login, password, nick));
        return true;
    }
}
