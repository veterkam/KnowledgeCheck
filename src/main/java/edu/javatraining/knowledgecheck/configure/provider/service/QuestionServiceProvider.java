package edu.javatraining.knowledgecheck.configure.provider.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.dao.QuestionDao;
import edu.javatraining.knowledgecheck.service.QuestionService;
import edu.javatraining.knowledgecheck.service.impl.QuestionServiceImpl;

public class QuestionServiceProvider implements Provider<QuestionService> {

    @Inject
    private QuestionDao dao;

    @Override
    public QuestionService get() {
        return new QuestionServiceImpl(dao);
    }
}
