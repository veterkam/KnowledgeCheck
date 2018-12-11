package edu.javatraining.knowledgecheck.controller.dto;

import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StudentDto extends UserDto{
    @NotBlank(message="app.account.validation.specialty.not_empty")
    @Size(min=3, max=50, message="app.account.validation.specialty.size")
    private String specialty;
    @NotBlank(message="app.account.validation.year.not_empty")
    @Size(min=1, max=4, message="app.account.validation.year.size")
    @Digits(integer=4, fraction=0,  message="app.account.validation.year.only_digits")
    private String year;
    @NotBlank(message="app.account.validation.group.not_empty")
    @Size(min=3, max=50, message="app.account.validation.group.size")
    private String group;

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Student toStudent() {

        Student s = new Student();
        toUser(s);
        s.setGroup(group);
        s.setSpecialty(specialty);
        s.setYear(Integer.parseInt(year));

        return s;
    }

    @Override
    public User toUser() {

        return toStudent();
    }

    public void fromStudent(Student s) {

        fromUser(s);
        setGroup(s.getGroup());
        setSpecialty(s.getSpecialty());
        setYear("" + s.getYear());
    }
}
