package edu.javatraining.knowledgecheck.configure.provider.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.javatraining.knowledgecheck.data.dao.StudentDao;
import edu.javatraining.knowledgecheck.service.StudentService;
import edu.javatraining.knowledgecheck.service.impl.StudentServiceImpl;

public class StudentServiceProvider implements Provider<StudentService> {

    @Inject
    private StudentDao dao;

    @Override
    public StudentService get() {
        return new StudentServiceImpl(dao);
    }
}
