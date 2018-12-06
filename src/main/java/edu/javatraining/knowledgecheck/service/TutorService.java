package edu.javatraining.knowledgecheck.service;

import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;

import java.util.List;

public interface TutorService {

    Long insert(Tutor tutor);
    boolean update(Tutor tutor);
    Long save(Tutor tutor);
    boolean delete(Tutor tutor);
    boolean deleteById(Long id);
    Tutor findOne(Tutor tutor);
    Tutor findOneById(Long id);
    List<Tutor> findAll();
    List<Tutor> findAll(Long offset, Long count);
    Long count();
    Tutor findOneByUsername(String username);
    Tutor attachProfile(User user);
}
