package edu.javatraining.knowledgecheck.data.dao.jdbc;

import edu.javatraining.knowledgecheck.exception.DAOException;
import edu.javatraining.knowledgecheck.domain.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDaoJdbc extends BasicDaoJdbc {

    public SubjectDaoJdbc() {
    }

    public Long insert(Subject subject) throws DAOException {
        Long resultId;
        String sql = "INSERT INTO subjects (name) VALUES(?)";
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, subject.getName());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if (isRowInserted) {
                resultId = getGenKey(statement).longValue();
                subject.setId(resultId);
            } else {
                DAOException e = new DAOException("Inserting subject data failed, no rows affected.");
                logger.error(e.getMessage(), e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting subject data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return resultId;
    }

    private Subject extractSubjectFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        return new Subject(id, name);
    }

    public List<Subject> getList() throws DAOException {
        List<Subject> subjectList = new ArrayList<>();
        String sql = "SELECT * FROM subjects";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Subject subject = extractSubjectFromResultSet(resultSet);
                subjectList.add(subject);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading subject data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return subjectList;
    }

    public boolean delete(Subject subject) throws DAOException {
        String sql = "DELETE FROM subjects WHERE id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowDeleted = false;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, subject.getId());

            isRowDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Deleting subject data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowDeleted;
    }

    public boolean update(Subject subject) throws DAOException {

        String sql = "UPDATE subjects SET name = ? WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;
        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setString(1, subject.getName());
            statement.setLong(2, subject.getId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating subject data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowUpdated;
    }

    public Subject get(Long id) throws DAOException {
        Subject subject = null;
        String sql = "SELECT * FROM subjects WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                subject = extractSubjectFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading subject data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return subject;
    }
}
