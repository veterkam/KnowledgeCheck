package edu.javatraining.knowledgecheck.domain;

import org.apache.commons.lang3.StringUtils;

public class Student extends User{
    private String specialty;
    private int year;
    private String group;

    public Student() {

    }

    public Student(User user) {
        super(user);
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean fullEquals(Student other) {

        return super.fullEquals(other)
                && year == other.getYear()
                && StringUtils.equals(specialty, other.getSpecialty())
                && StringUtils.equals(group, other.getGroup());
    }
}
