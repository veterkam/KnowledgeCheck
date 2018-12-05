package edu.javatraining.knowledgecheck.data.dao;

import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;

public interface TutorDao extends BasicDao<Tutor, Long> {
    Tutor attachProfile(User user);
    Tutor findOneByUsername(String username);
}
