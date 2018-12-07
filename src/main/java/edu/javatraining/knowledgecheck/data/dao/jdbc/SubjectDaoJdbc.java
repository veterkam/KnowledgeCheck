package edu.javatraining.knowledgecheck.data.dao.jdbc;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.SubjectDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.tools.PrimitiveEnvelope;
import edu.javatraining.knowledgecheck.domain.User;
import edu.javatraining.knowledgecheck.exception.DAOException;
import edu.javatraining.knowledgecheck.domain.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDaoJdbc extends BasicDaoJdbc implements SubjectDao {


    public SubjectDaoJdbc(ConnectionPool pool) {
        super(pool);
    }

    @Override
    public Long save(Subject subject) {

        Long id = subject.getId();
        if(!update(subject)) {
            id = insert(subject);
        }

        return id;
    }

    @Override
    public Subject findOne(Subject subject) {
        return findOneById(subject.getId());
    }

    @Override
    public Subject findOneById(Long id) {
        String sql = "SELECT * FROM subjects WHERE id = ?";

        PrimitiveEnvelope<Subject> subject = new PrimitiveEnvelope<>();

        select(sql,
                (statement -> {
                    statement.setLong(1, id);
                }),
                (resultSet -> {
                    if(resultSet.next()) {
                        String name = resultSet.getString("name");
                        subject.value = new Subject(id, name);
                    }
                }));

        return subject.value;
    }

    @Override
    public List<Subject> findAll() {
        return findAll(null, null);
    }

    @Override
    public List<Subject> findAll(Long offset, Long count) {
        List<Subject> subjectList = new ArrayList<>();
        String sql = "SELECT * FROM subjects";
        if(offset != null) {
            sql += " LIMIT ?, ?";
        }

        select(sql,

                (statement -> {
                    if(offset != null) {
                        statement.setLong(1, offset);
                        statement.setLong(2, count);
                    }
                }),

                (resultSet -> {
                    while (resultSet.next()) {
                        Long id = resultSet.getLong("id");
                        String name = resultSet.getString("name");
                        Subject subject = new Subject(id, name);
                        subjectList.add(subject);
                    }
                }));

        return subjectList;//.toArray(new Subject[subjectList.size()]);
    }

    @Override
    public Long count() {
        String sql = "SELECT COUNT(*) FROM subjects";
        return super.count(sql);
    }

    @Override
    public Long insert(Subject subject) {

        String sql = "INSERT INTO subjects (name) VALUES(?)";

        Long resultId = insert(sql,
                (statement -> {
                    statement.setString(1, subject.getName());
                }));

        subject.setId(resultId);
        return resultId;
    }

    @Override
    public boolean update(Subject subject) {

        String sql = "UPDATE subjects SET name = ? WHERE id = ?";

        return update(sql,
                (statement -> {
                    statement.setString(1, subject.getName());
                    statement.setLong(2, subject.getId());
                }));
    }

    @Override
    public boolean delete(Subject subject) {
        return deleteById(subject.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM subjects WHERE id = ?";

        return delete(sql,
                (statement -> {
                    statement.setLong(1, id);
                }));
    }




}
