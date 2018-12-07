package edu.javatraining.knowledgecheck.configure.provider.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.dao.SubjectDao;
import edu.javatraining.knowledgecheck.service.SubjectService;
import edu.javatraining.knowledgecheck.service.impl.SubjectServiceImpl;

public class SubjectServiceProvider implements Provider<SubjectService> {

    @Inject
    private SubjectDao dao;

    @Override
    public SubjectService get() {
        return new SubjectServiceImpl(dao);
    }
}
