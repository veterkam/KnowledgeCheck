package edu.javatraining.knowledgecheck.domain;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Question {
    private Long id;
    private Long testId;
    private String description;
    private List<Answer> answers = new ArrayList<>();

    public Question(Long id, Long testId, String description) {
        this.id = id;
        this.testId = testId;
        this.description = description;
    }

    public Question(Long id) {
        this.id = id;
    }

    public Question() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers.clear();
        this.answers = answers;
    }

    public void setAnswers(Answer[] answers) {
        this.answers.clear();
        this.answers = Arrays.asList(answers);
    }

    @Override
    public boolean equals(Object other) {

        return (other != null)
                && (other instanceof Question)
                && Objects.equals(id, ((Question) other).getId());
    }

    public boolean fullEquals(Question other) {

        return (other != null)
                && Objects.equals(id, other.getId() )
                && Objects.equals(testId, other.getTestId())
                && Objects.equals(description, other.getDescription() );
    }
}
