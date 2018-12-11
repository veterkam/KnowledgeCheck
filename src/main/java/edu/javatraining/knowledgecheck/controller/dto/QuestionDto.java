package edu.javatraining.knowledgecheck.controller.dto;

import edu.javatraining.knowledgecheck.data.dao.AnswerDao;
import edu.javatraining.knowledgecheck.domain.Answer;
import edu.javatraining.knowledgecheck.domain.Question;
import edu.javatraining.knowledgecheck.domain.Subject;
import edu.javatraining.knowledgecheck.domain.Test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionDto implements DtoWithErrors {

    @Pattern(regexp = "\\d*", message = "app.testing.validation.question.wrong")
    private String questionId;

    @NotBlank(message="app.testing.validation.question.not_empty")
    @Size(min=3, max=500, message="app.testing.validation.question.size")
    private String description;

    private String isRemoved = "0";

    private List<AnswerDto> answers;

    private Map<String, List<String>> errors;

    public QuestionDto() {

    }

    public QuestionDto(Question q) {
        fromQuestion(q);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
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

    public List<AnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDto> answers) {
        this.answers = answers;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Question toQuestion() {

        Question q = new Question();
        toQuestion(q);

        return q;
    }

    protected void toQuestion(Question out) {

        out.setDescription(description);

        try {
            out.setId(Long.parseLong(questionId));
        }catch(NumberFormatException e){
            // do nothing
        }

        // answers processing
        if(answers != null) {
            out.setAnswers(new ArrayList<>());
            for(AnswerDto a : answers) {
                if(!a.isRemoved()) {
                    Answer answer = a.toAnswer();
                    answer.setQuestionId(out.getId());
                    out.getAnswers().add(answer);
                }
            }
        }
    }

    public void fromQuestion(Question q) {

        description = q.getDescription();

        if(q.getId() != null) {
            questionId = "" + q.getId();
        }

        if(q.getAnswers() != null) {
            answers = new ArrayList<>();

            for (Answer a : q.getAnswers()) {
                AnswerDto dto = new AnswerDto();
                dto.fromAnswer(a);
                answers.add(dto);
            }
        }
    }
}
