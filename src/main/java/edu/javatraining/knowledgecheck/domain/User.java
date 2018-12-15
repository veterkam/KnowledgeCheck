package edu.javatraining.knowledgecheck.domain;

public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private Role role;
    private boolean verified;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(User other) {
        this.id = other.id;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.username = other.username;
        this.password = other.password;
        this.email = other.email;
        this.role = other.role;
        this.verified = other.verified;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public enum Role{
        ADMINISTRATOR("app.account.role.administrator"),
        TUTOR("app.account.role.tutor"),
        STUDENT("app.account.role.student");

        private String caption;

        Role(String caption) {
            this.caption = caption;
        }

        public String getCaption() {
            return caption;
        }

        public static Role fromOrdinal(int n) {return values()[n];}
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        String format = "User { id : %d, firstName : %s, lastName : %s, username : %s, password : %s, email : %s, role : %s, verified : %s }";
        return String.format(format, id, firstName, lastName, username, password, email, role.toString(), verified ? "true" : "false");
    }

    @Override
    public boolean equals(Object other) {

        if(other == null) {
            return false;
        }

        return (! (other instanceof User)) ? false
                    : (id != null) ? id == ((User) other).getId()
                        : (username != null) ? username.equals(((User) other).getUsername())
                            : false;
    }
}
