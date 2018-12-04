package com.epam.javatraining.knowledgecheck.data.dao;

import com.epam.javatraining.knowledgecheck.domain.Tutor;
import com.epam.javatraining.knowledgecheck.domain.User;

public interface TutorDao extends BasicDao<Tutor, Long> {
    Tutor attachProfile(User user);
    Tutor findOneByUsername(String username);
}
