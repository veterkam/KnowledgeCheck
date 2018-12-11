package edu.javatraining.knowledgecheck.controller.dto;

import edu.javatraining.knowledgecheck.domain.Question;
import edu.javatraining.knowledgecheck.domain.Subject;
import edu.javatraining.knowledgecheck.domain.Test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestDto implements DtoWithErrors {

    @Pattern(regexp = "\\d*", message = "app.testing.validation.test.wrong")
    private String testId;

    @NotBlank(message="app.testing.validation.test.title.not_empty")
    @Size(min=3, max=100, message="app.testing.validation.test.title.size")
    private String title;

    @NotBlank(message="app.testing.validation.test.description.not_empty")
    @Size(min=3, max=500, message="app.testing.validation.test.description.size")
    private String description;

    @NotBlank(message= "app.testing.validation.test.subject.not_empty")
    @Pattern(regexp = "\\d+", message = "app.testing.validation.test.subject.failed")
    private String subjectId;

    private List<QuestionDto> questions;

    private Map<String, List<String>> errors;

    public TestDto() {

    }

    public TestDto(Test t) {
        fromTest(t);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Test toTest() {

        Test t = new Test();
        toTest(t);

        return t;
    }

    protected void toTest(Test out) {

        out.setTitle(title);
        out.setDescription(description);

        Subject s = new Subject();
        try {
            s.setId(Long.parseLong(subjectId));
        }catch(NumberFormatException e){
            // do nothing
        }
        out.setSubject(s);

        try {
            out.setId(Long.parseLong(testId));
        }catch(NumberFormatException e){
            // do nothing
        }

        // questions processing
        if(questions != null) {
            out.setQuestions(new ArrayList<>());
            for(QuestionDto q : questions) {
                if(!q.isRemoved()) {
                    Question question = q.toQuestion();
                    question.setTestId(out.getId());
                    out.getQuestions().add(question);
                }
            }
        }
    }

    public void fromTest(Test t) {

        title = t.getTitle();
        description = t.getDescription();

        if(t.getSubject() != null) {
            subjectId = "" + t.getSubject().getId();
        }

        if(t.getId() != null) {
            testId = "" + t.getId();
        }

        if(t.getQuestions() != null) {
            questions = new ArrayList<>();

            for (Question q : t.getQuestions()) {
                QuestionDto dto = new QuestionDto();
                dto.fromQuestion(q);
                questions.add(dto);
            }
        }
    }
}
