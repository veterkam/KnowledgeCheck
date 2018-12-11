package edu.javatraining.knowledgecheck.controller.dto;

import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TutorDto extends UserDto{
    @NotBlank(message="app.account.validation.position.not_empty")
    @Size(min=3, max=50, message="app.account.validation.position.size")
    private String position;

    @NotBlank(message="app.account.validation.scientificDegree.not_empty")
    @Size(min=3, max=50, message="app.account.validation.scientificDegree.size")
    private String scientificDegree;

    @NotBlank(message="app.account.validation.academicTitle.not_empty")
    @Size(min=3, max=50, message="app.account.validation.academicTitle.size")
    private String academicTitle;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getScientificDegree() {
        return scientificDegree;
    }

    public void setScientificDegree(String scientificDegree) {
        this.scientificDegree = scientificDegree;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public Tutor toTutor() {

        Tutor t = new Tutor();
        toUser(t);
        t.setAcademicTitle(academicTitle);
        t.setPosition(position);
        t.setScientificDegree(scientificDegree);

        return t;
    }

    @Override
    public User toUser() {

        return toTutor();
    }

    public void fromTutor(Tutor t) {

        fromUser(t);
        setAcademicTitle(t.getAcademicTitle());
        setPosition(t.getPosition());
        setScientificDegree(t.getScientificDegree());
    }
}
