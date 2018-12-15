package edu.javatraining.knowledgecheck;

import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;

public class UserFactory {

    public static User getAdmin() {
        User u = new User();
        u.setId(1L);
        u.setFirstName("John");
        u.setLastName("Smith");
        u.setUsername("admin");
        u.setVerified(true);
        u.setEmail("admin@mail.edu");
        u.setRole(User.Role.ADMINISTRATOR);
        u.setPassword("1000:ed1f2367300c535ccd03e1798b7997cf:5fc67e01468149d9795cd05048bd28752373577f4b4d0054a7fb1afc027b657547bc93ddb8bf966c52de0328c5a1e61380a8e8821e42e7f8647c6bd11b901d9c");

        return u;
    }

    public static Tutor getTutor() {
        Tutor u = new Tutor();
        u.setId(2L);
        u.setFirstName("Mary");
        u.setLastName("Smith");
        u.setUsername("tutor");
        u.setVerified(true);
        u.setEmail("tutor@mail.edu");
        u.setRole(User.Role.TUTOR);
        u.setPassword("1000:ed1f2367300c535ccd03e1798b7997cf:5fc67e01468149d9795cd05048bd28752373577f4b4d0054a7fb1afc027b657547bc93ddb8bf966c52de0328c5a1e61380a8e8821e42e7f8647c6bd11b901d9c");
        u.setScientificDegree("Doctor of Mathematics");
        u.setPosition("Chief of department");
        u.setAcademicTitle("Professor of Mathematics");
        return u;
    }

    public static Student getStudent() {
        Student u = new Student();
        u.setId(3L);
        u.setFirstName("Bob");
        u.setLastName("Smith");
        u.setUsername("student");
        u.setVerified(true);
        u.setEmail("student@mail.edu");
        u.setRole(User.Role.STUDENT);
        u.setPassword("1000:ed1f2367300c535ccd03e1798b7997cf:5fc67e01468149d9795cd05048bd28752373577f4b4d0054a7fb1afc027b657547bc93ddb8bf966c52de0328c5a1e61380a8e8821e42e7f8647c6bd11b901d9c");
        u.setYear(2014);
        u.setSpecialty("Mathematics");
        u.setGroup("M-2014");
        return u;
    }

    public static User getById(int id) {

        switch(id) {
            case 1:
                return getAdmin();
            case 2:
                return getTutor();
            case 3:
                return getStudent();
            default:
                return null;
        }
    }
}
