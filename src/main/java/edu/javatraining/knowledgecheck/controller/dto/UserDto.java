package edu.javatraining.knowledgecheck.controller.dto;

//import org.hibernate.validator.constraints.ScriptAssert;

import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ScriptAssert(
        lang = "javascript",
        script="_this.password.equals(_this.confirmPassword)",
        message="{app.account.password.passwords_do_not_match}",
        reportOn="confirmPassword")
public class UserDto {
    @NotNull(message="{app.account.firstName.not_empty}")
	@NotEmpty(message="{app.account.firstName.not_empty}")
    @Size(min=3, max=50, message="{app.account.firstName.size}")
    private String firstName;

    @NotNull(message="{app.account.lastName.not_empty}")
	@NotEmpty(message="{app.account.lastName.not_empty}")
    @Size(min=3, max=50, message="{app.account.lastName.size}")
    private String lastName;

    @NotNull(message="{app.account.username.not_empty}")
	@NotEmpty(message="{app.account.username.not_empty}")
    @Size(min=3, max=50, message="{app.account.username.size}")
    private String username;

    @NotNull(message="{app.account.password.not_empty}")
	@NotEmpty(message="{app.account.password.not_empty}")
    @Size(min=3, max=100, message="{app.account.password.size}")
    private String password;

    @NotNull(message="{app.account.confirm_password.not_empty}")
	@NotEmpty(message="{app.account.confirm_password.not_empty}")
    private String confirmPassword;

    @NotNull(message="{app.account.email.not_empty}")
	@NotEmpty(message="{app.account.email.not_empty}")
    @Email(message="{app.account.email.valid}")
    private String email;

    @NotNull(message="{app.account.role.valid}")
	@NotEmpty(message="{app.account.role.valid}")
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
