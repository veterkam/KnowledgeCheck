package quiz.model.bean;

public class User {
    public enum Role{
        ADMINISTRATOR,
        TUTOR,
        STUDENT;

        public static Role fromOrdinal(int n) {return values()[n];}
    }

    private int id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private Role role;

    public User() {

    }

    public User(int id) {
        this.id = id;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String firstname, String lastname, String email, Role role, String username, String password) {
        this(username, password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
    }

    public User(int id, String firstname, String lastname, String email, Role role, String username, String password) {
        this(firstname, lastname, email, role, username, password);
        this.id = id;
    }

    public User(User user) {
        this(user.id, user.firstname, user.lastname, user.email, user.role, user.username, user.password);
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}