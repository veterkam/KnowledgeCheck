package edu.javatraining.knowledgecheck.configure.provider.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.StudentDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.StudentDaoJdbc;

public class StudentDaoProvider implements Provider<StudentDao> {

    @Inject
    private ConnectionPool pool;

    @Override
    public StudentDao get() {
        return new StudentDaoJdbc(pool);
    }
}
