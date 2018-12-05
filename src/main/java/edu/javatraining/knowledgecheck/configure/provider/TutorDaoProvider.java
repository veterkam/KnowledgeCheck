package edu.javatraining.knowledgecheck.configure.provider;

import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import edu.javatraining.knowledgecheck.data.dao.TutorDao;
import edu.javatraining.knowledgecheck.data.dao.UserDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.TutorDaoJdbc;
import edu.javatraining.knowledgecheck.data.dao.jdbc.UserDaoJdbc;

public class TutorDaoProvider implements Provider<TutorDao> {

    @Override
    public TutorDao get() {
        return new TutorDaoJdbc();
    }
}
