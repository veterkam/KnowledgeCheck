package edu.javatraining.knowledgecheck.domain;

import java.util.Objects;

public class Answer {
    private Long id;
    private Long questionId;
    private String description;
    private boolean correct;

    public Answer(Long id) {
        this.id = id;
    }

    public Answer() {

    }

    public Answer(Long id, Long questionId, String description, boolean correct) {
        this.id = id;
        this.questionId = questionId;
        this.description = description;
        this.correct = correct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }


    @Override
    public boolean equals(Object other) {

        return (other != null)
                && (other instanceof Answer)
                && Objects.equals(id, ((Answer) other).getId());
    }

    public boolean fullEquals(Answer other) {

        return (other != null)
                && Objects.equals(id, other.getId() )
                && Objects.equals(questionId, other.getQuestionId())
                && Objects.equals(description, other.getDescription() );
    }
}
