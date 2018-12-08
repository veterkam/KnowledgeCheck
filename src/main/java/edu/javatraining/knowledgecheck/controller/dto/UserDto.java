package edu.javatraining.knowledgecheck.controller.dto;

//import org.hibernate.validator.constraints.ScriptAssert;

import edu.javatraining.knowledgecheck.domain.User;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.*;
import java.util.*;

@ScriptAssert(
        lang = "javascript",
        script="_this.role != null && (_this.role.equals('ADMINISTRATOR') || _this.role.equals('TUTOR') || _this.role.equals('STUDENT'))",
        message="app.account.validation.role.valid",
        reportOn="role")
public class UserDto extends UserRecoveryDto{
	@NotBlank(message="app.account.validation.firstName.not_empty")
    @Size(min=3, max=50, message="app.account.validation.firstName.size")
    private String firstName;

	@NotBlank(message="app.account.validation.lastName.not_empty")
    @Size(min=3, max=50, message="app.account.validation.lastName.size")
    private String lastName;

	@NotBlank(message="app.account.validation.role.not_empty")
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public User toUser() {

        User u = new User();
        toUser(u);

        return u;
    }

    @Override
    protected void toUser(User out) {

        super.toUser(out);
        out.setFirstName(firstName);
        out.setLastName(lastName);
        out.setRole(User.Role.valueOf(role));
    }

    @Override
    public void fromUser(User u) {

        super.fromUser(u);
        firstName = u.getFirstName();
        lastName = u.getLastName();
        role = u.getRole().toString();
    }
}
