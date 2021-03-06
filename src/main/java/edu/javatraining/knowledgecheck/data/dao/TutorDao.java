package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;

import java.util.List;

public interface TutorDao {
    Tutor attachProfile(User user);
    Tutor findOneByUsername(String username);

    Long insert(Tutor tutor);
    boolean update(Tutor tutor);
    Long save(Tutor tutor);
    boolean delete(Tutor tutor);
    boolean deleteById(Long id);
    Tutor findOne(Tutor tutor);
    Tutor findOneById(Long id);
    List<Tutor> findAllTutors();
    List<Tutor> findAllTutors(Long offset, Long count);
    Long count();
}
