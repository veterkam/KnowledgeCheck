package edu.javatraining.knowledgecheck.configure.provider.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.dao.TutorDao;
import edu.javatraining.knowledgecheck.service.TutorService;
import edu.javatraining.knowledgecheck.service.impl.TutorServiceImpl;

public class TutorServiceProvider implements Provider<TutorService> {

    @Inject
    private TutorDao dao;

    @Override
    public TutorService get() {
        return new TutorServiceImpl(dao);
    }
}
