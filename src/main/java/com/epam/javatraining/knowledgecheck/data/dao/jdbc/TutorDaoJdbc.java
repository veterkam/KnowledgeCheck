package com.epam.javatraining.knowledgecheck.data.dao.jdbc;

import com.epam.javatraining.knowledgecheck.data.dao.TutorDao;
import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.domain.Tutor;
import com.epam.javatraining.knowledgecheck.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TutorDaoJdbc extends UserDaoJdbc implements TutorDao{

    private final String BASIC_SELECT = "select `users`.`id`," +
            " `firstname`," +
            " `lastname`," +
            " `email`," +
            " `role`," +
            " `username`," +
            " `password`," +
            " `verified`, " +
            " `position`," +
            " `scientific_degree`," +
            " `academic_title`" +
            " from users" +
            " left join tutor_profiles on users.id = tutor_profiles.id";

    public TutorDaoJdbc() {
        super();
    }

    @Override
    public Long save(Tutor entity) {

        Long id = entity.getId();
        if(!update(entity)) {
            id = insert(entity);
        }

        return id;
    }

    @Override
    public Tutor findOneById(Long id){
        Tutor tutor = null;
        final User.Role role = User.Role.TUTOR;
        String sql = BASIC_SELECT +
                " where users.id = ? AND role = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            statement.setInt(2, role.ordinal());
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                tutor = new Tutor();
                super.extractUserFromResultSet(tutor, resultSet);
                extractProfileFromResultSet(tutor, resultSet);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading tutor data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return tutor;
    }

    @Override
    public Tutor findOneByUsername(String username) {
        Tutor tutor = null;
        final User.Role role = User.Role.TUTOR;
        String sql = BASIC_SELECT +
                " where users.username = ? AND role = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setInt(2, role.ordinal());
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                tutor = new Tutor();
                super.extractUserFromResultSet(tutor, resultSet);
                extractProfileFromResultSet(tutor, resultSet);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading tutor data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return tutor;
    }

    @Override
    public Tutor attachProfile(User user) {

        if(user == null || user.getRole() != User.Role.TUTOR) {
            return null;
        }

        Tutor tutor = new Tutor(user);
        String sql = "select `users`.`id`," +
                " `position`," +
                " `scientific_degree`," +
                " `academic_title`" +
                " from users" +
                " left join tutor_profiles on users.id = tutor_profiles.id" +
                " where users.id = ? AND role = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setLong(1, tutor.getId());
            statement.setInt(2, tutor.getRole().ordinal());
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                extractProfileFromResultSet(tutor, resultSet);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading tutor data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return tutor;
    }

    @Override
    public Tutor findOne(Tutor entity) {
        Tutor tutor = null;

        if(entity.getId() != null) {
            tutor = findOneById(entity.getId());
        }

        if(tutor == null && entity.getUsername() != null) {
            tutor = findOneByUsername(entity.getUsername());
        }

        return tutor;
    }

    @Override
    public Long insert(Tutor tutor) {
        Long resultId = super.insert(tutor);
        insertProfile(tutor);

        return resultId;
    }

    @Override
    public boolean delete(Tutor tutor) {
        // DELETE CASCADE
        return super.delete(tutor);
    }

    @Override
    public boolean update(Tutor tutor) {
        if(!super.update(tutor)) {
            return false;
        }

        if(!updateProfile(tutor)) {
            insertProfile(tutor);
        }

        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        // DELETE CASCADE
        return super.deleteById(id);
    }

    public Tutor[] findAll() {
        return findAll(null, null);
    }

    public Tutor[] findAll(Long offset, Long count) {

        final User.Role role = User.Role.TUTOR;
        List<Tutor> tutorList = new ArrayList<>();
        String sql = BASIC_SELECT + " where role = ?";
        if(offset != null) {
            sql += " LIMIT ?, ?";
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, role.ordinal());

            if(offset != null) {
                statement.setLong(2, offset);
                statement.setLong(3, count);
            }
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Tutor tutor = new Tutor();
                super.extractUserFromResultSet(tutor, resultSet);
                extractProfileFromResultSet(tutor, resultSet);
                tutorList.add(tutor);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading tutor data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return (Tutor[]) tutorList.toArray();
    }

    @Override
    public Long count() {
        final User.Role role = User.Role.TUTOR;
        String sql = "SELECT COUNT(*) FROM users WHERE role = ";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Long result = 0L;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, role.ordinal());
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                result = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading user data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return result;
    }

    private void insertProfile(Tutor tutor) {
        String sql = "INSERT INTO tutor_profiles (id, position, scientific_degree, academic_title) " +
                "VALUES(?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setLong(1, tutor.getId());
            statement.setString(2, tutor.getPosition());
            statement.setString(3, tutor.getScientificDegree());
            statement.setString(4, tutor.getAcademicTitle());

            boolean isRowInserted = statement.executeUpdate() > 0;

            if(!isRowInserted) {
                throw new DAOException("Inserting tutor profile failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Inserting tutor profile data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }
    }

    private boolean updateProfile(Tutor tutor) {
        String sql = "UPDATE tutor_profiles SET "
                +"position = ?, scientific_degree = ?, academic_title = ?"
                + "WHERE id = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        boolean isRowUpdated = false;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setString(1, tutor.getPosition());
            statement.setString(2, tutor.getScientificDegree());
            statement.setString(3, tutor.getAcademicTitle());
            statement.setLong(4, tutor.getId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating tutor data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowUpdated;
    }

    private void extractProfileFromResultSet(Tutor tutor, ResultSet resultSet) throws SQLException{
        String position = resultSet.getString("position");
        String scientificDegree = resultSet.getString("scientific_degree");
        String academicTitle = resultSet.getString("academic_title");
        // extract profile info
        tutor.setPosition(position);
        tutor.setScientificDegree(scientificDegree);
        tutor.setAcademicTitle(academicTitle);
    }
}
