package edu.javatraining.knowledgecheck.service.impl;

import edu.javatraining.knowledgecheck.data.dao.StudentDao;
import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.User;
import edu.javatraining.knowledgecheck.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class StudentServiceImpl implements StudentService {

    private static Logger logger = LogManager.getLogger("Service");

    private StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Long insert(Student student) {
        return studentDao.insert(student);
    }

    @Override
    public boolean update(Student student) {
        return studentDao.update(student);
    }

    @Override
    public Long save(Student student) {
        return studentDao.save(student);
    }

    @Override
    public boolean delete(Student student) {
        return studentDao.delete(student);
    }

    @Override
    public boolean deleteById(Long id) {
        return studentDao.deleteById(id);
    }

    @Override
    public Student findOne(Student student) {
        return studentDao.findOne(student);
    }

    @Override
    public Student findOneById(Long id) {
        return studentDao.findOneById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAllStudents();
    }

    @Override
    public List<Student> findAll(Long offset, Long count) {
        return studentDao.findAllStudents(offset, count);
    }

    @Override
    public Long count() {
        return studentDao.count();
    }

    @Override
    public Student findOneByUsername(String username) {
        return studentDao.findOneByUsername(username);
    }

    @Override
    public Student attachProfile(User user) {
        return studentDao.attachProfile(user);
    }
}
