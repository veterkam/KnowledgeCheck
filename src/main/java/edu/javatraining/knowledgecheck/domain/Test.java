package edu.javatraining.knowledgecheck.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Test {

    private Long id;
    private Subject subject;
    private Tutor tutor;
    private String title;
    private String description;
    private Timestamp updateTime;
    private int timeLimitation = 0;
    private List<Question> questions = new ArrayList<>();

    public Test() {
    }

    public Test(Long id) {
        this.id = id;
    }

    public Test(Long id, Subject subject, Tutor tutor, String title, String description, Timestamp updateTime) {
        this.id = id;
        this.subject = subject;
        this.tutor = tutor;
        this.description = description;
        this.title = title;
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions.clear();
        this.questions = questions;
    }

    public Long getSubjectId() {
        return (subject == null) ? 0 : subject.getId();
    }

    public Long getTutorId() {
        return (tutor == null) ? 0 : tutor.getId();
    }

    public int getTimeLimitation() {
        return timeLimitation;
    }

    public String getTimeLimitationAsTimePeriod() {

        int seconds = timeLimitation;
        int hh = Math.floorDiv(seconds, 3600 );
        seconds -= hh * 3600;
        int mm = Math.floorDiv(seconds, 60);
        seconds -= mm * 60;
        int ss = seconds;

        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

    public void setTimeLimitation(int timeLimitation) {
        this.timeLimitation = timeLimitation;
    }

    @Override
    public boolean equals(Object other) {

        if(other == null) {
            return false;
        }

        return (other == null) ? false
                :(! (other instanceof Test)) ? false
                    : (id != null) ? id == ((Test) other).getId()
                        : false;
    }
}
