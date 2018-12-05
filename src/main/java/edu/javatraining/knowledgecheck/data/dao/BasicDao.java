package edu.javatraining.knowledgecheck.data.dao;

import java.util.List;

public interface BasicDao<E, K> {

    K insert(E entity);

    boolean update(E entity);

    K save(E entity);

    boolean delete(E entity);

    boolean deleteById(K id);

    E findOne(E entity);

    E findOneById(K id);

    E[] findAll();

    E[] findAll(K offset, K count);

    K count();
}
