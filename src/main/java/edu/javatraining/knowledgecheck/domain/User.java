package edu.javatraining.knowledgecheck.domain;

public class User {

    private Long id;
    private String firstname;
    private String lastname;
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
        this.firstname = other.firstname;
        this.lastname = other.lastname;
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
        return firstname + " " + lastname;
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

    public enum Role{
        ADMINISTRATOR("Administrator"),
        TUTOR("Tutor"),
        STUDENT("Student");

        private String caption;

        Role(String caption) {
            this.caption = caption;
        }

        @Override
        public String toString() {
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

    public String toString() {
        String format = "User { id : %d, firstname : %s, lastname : %s, username : %s, password : %s, email : %s, role : %s, verified : %s }";
        return String.format(format, id, firstname, lastname, username, password, email, role.toString(), verified ? "true" : "false");
    }
}
