public class User {
    private String name;
    private String login;
    private String password;

    public User() {}

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }
    public boolean enter(String login, String password) {
        if (this.login.equals(login) && this.password.equals(password)) {
            return true;
        } else return false;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }
}
