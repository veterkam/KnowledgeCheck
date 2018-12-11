package edu.javatraining.knowledgecheck.controller.dto;

import edu.javatraining.knowledgecheck.domain.Answer;
import edu.javatraining.knowledgecheck.domain.Question;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public class AnswerDto implements DtoWithErrors {

    @Pattern(regexp = "\\d*", message = "app.testing.validation.answer.wrong")
    private String answerId;

    @NotBlank(message="app.testing.validation.test.description.not_empty")
    @Size(min=3, max=500, message="app.testing.validation.description.test.size")
    private String description;

    @Pattern(regexp = "0|1", message = "app.testing.validation.answer.wrong")
    private String isRemoved = "0";

    private boolean isCorrect = false;

    private Map<String, List<String>> errors;

    public AnswerDto() {

    }

    public AnswerDto(Answer a) {
        fromAnswer(a);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isRemoved() {
        return "1".equals(isRemoved);
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed ? "1" : "0";
    }

    public void setRemoved(String removed) {
        isRemoved = removed;
    }

    public String getRemoved() {
        return isRemoved;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Answer toAnswer() {

        Answer a = new Answer();
        toAnswer(a);

        return a;
    }

    protected void toAnswer(Answer out) {

        out.setDescription(description);
        out.setCorrect(isCorrect);

        try {
            out.setId(Long.parseLong(answerId));
        }catch(NumberFormatException e){
            // do nothing
        }
    }

    public void fromAnswer(Answer a) {

        description = a.getDescription();
        isCorrect = a.isCorrect();

        if(a.getId() != null) {
            answerId = "" + a.getId();
        }
    }
}
