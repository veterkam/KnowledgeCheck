package edu.javatraining.knowledgecheck.service.impl;

import edu.javatraining.knowledgecheck.data.dao.SubjectDao;
import edu.javatraining.knowledgecheck.domain.Subject;
import edu.javatraining.knowledgecheck.service.SubjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SubjectServiceTestImpl implements SubjectService {

    private static Logger logger = LogManager.getLogger("Service");

    private SubjectDao dao;

    public SubjectServiceTestImpl(SubjectDao dao) {
        this.dao = dao;
    }

    @Override
    public Long insert(Subject subject) {
        return dao.insert(subject);
    }

    @Override
    public boolean update(Subject subject) {
        return dao.update(subject);
    }

    @Override
    public Long save(Subject subject) {
        return dao.save(subject);
    }

    @Override
    public boolean delete(Subject subject) {
        return dao.delete(subject);
    }

    @Override
    public boolean deleteById(Long id) {
        return dao.deleteById(id);
    }

    @Override
    public Subject findOne(Subject subject) {
        return dao.findOne(subject);
    }

    @Override
    public Subject findOneById(Long id) {
        return dao.findOneById(id);
    }

    @Override
    public List<Subject> findAll() {
        return dao.findAll();
    }

    @Override
    public List<Subject> findAll(Long offset, Long count) {
        return dao.findAll(offset, count);
    }

    @Override
    public Long count() {
        return dao.count();
    }
}
