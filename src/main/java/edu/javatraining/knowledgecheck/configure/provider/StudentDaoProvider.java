package edu.javatraining.knowledgecheck.configure.provider;

import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import edu.javatraining.knowledgecheck.data.dao.StudentDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.StudentDaoJdbc;

public class StudentDaoProvider implements Provider<StudentDao> {

    @Override
    public StudentDao get() {
        return new StudentDaoJdbc();
    }
}
