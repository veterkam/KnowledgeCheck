package edu.javatraining.knowledgecheck.controller.dto;

//import org.hibernate.validator.constraints.ScriptAssert;

import edu.javatraining.knowledgecheck.domain.User;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ScriptAssert(
        lang = "javascript",
        script="_this.password != null && _this.password.equals(_this.confirmPassword)",
        message="app.account.validation.password.passwords_do_not_match",
        reportOn="confirmPassword")
public class UserRecoveryDto {

	@NotBlank(message="app.account.validation.username.not_empty")
    @Size(min=3, max=50, message="app.account.validation.username.size")
    private String username;

	@NotBlank(message="app.account.validation.password.not_empty")
    @Size(min=3, max=50, message="app.account.validation.password.size")
    private String password;

	@NotBlank(message="app.account.validation.confirm_password.not_empty")
    private String confirmPassword;

	@NotBlank(message="app.account.validation.email.not_empty")
    @Email(message="app.account.validation.email.valid")
    private String email;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public User toUser() {

        User u = new User();
        toUser(u);

        return u;
    }

    protected void toUser(User out) {

        out.setEmail(email);
        out.setUsername(username);
        out.setPassword(password);
    }

    public void fromUser(User u) {

        email = u.getEmail();
        username = u.getUsername();
    }
}
