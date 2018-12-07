package edu.javatraining.knowledgecheck.configure.provider.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.dao.AnswerDao;
import edu.javatraining.knowledgecheck.service.AnswerService;
import edu.javatraining.knowledgecheck.service.impl.AnswerServiceImpl;

public class AnswerServiceProvider implements Provider<AnswerService> {

    @Inject
    private AnswerDao dao;

    @Override
    public AnswerService get() {
        return new AnswerServiceImpl(dao);
    }
}