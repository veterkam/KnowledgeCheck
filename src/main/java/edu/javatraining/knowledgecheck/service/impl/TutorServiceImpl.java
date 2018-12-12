package edu.javatraining.knowledgecheck.service.impl;

import edu.javatraining.knowledgecheck.data.dao.TutorDao;
import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;
import edu.javatraining.knowledgecheck.service.TutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TutorServiceImpl implements TutorService {

    private static Logger logger = LogManager.getLogger("Service");

    private TutorDao tutorDao;

    public TutorServiceImpl(TutorDao tutorDao) {
        this.tutorDao = tutorDao;
    }

    @Override
    public Long insert(Tutor tutor) {
        return tutorDao.insert(tutor);
    }

    @Override
    public boolean update(Tutor tutor) {
        return tutorDao.update(tutor);
    }

    @Override
    public Long save(Tutor tutor) {
        return tutorDao.save(tutor);
    }

    @Override
    public boolean delete(Tutor tutor) {
        return tutorDao.delete(tutor);
    }

    @Override
    public boolean deleteById(Long id) {
        return tutorDao.deleteById(id);
    }

    @Override
    public Tutor findOne(Tutor tutor) {
        return tutorDao.findOne(tutor);
    }

    @Override
    public Tutor findOneById(Long id) {
        return tutorDao.findOneById(id);
    }

    @Override
    public List<Tutor> findAll() {
        return tutorDao.findAllTutors();
    }

    @Override
    public List<Tutor> findAll(Long offset, Long count) {
        return tutorDao.findAllTutors(offset, count);
    }

    @Override
    public Long count() {
        return tutorDao.count();
    }

    @Override
    public Tutor findOneByUsername(String username) {
        return tutorDao.findOneByUsername(username);
    }

    @Override
    public Tutor attachProfile(User user) {
        return tutorDao.attachProfile(user);
    }
}
