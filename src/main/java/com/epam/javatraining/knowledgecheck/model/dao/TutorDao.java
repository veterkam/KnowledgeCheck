package com.epam.javatraining.knowledgecheck.model.dao;

import com.epam.javatraining.knowledgecheck.exception.DAOException;
import com.epam.javatraining.knowledgecheck.model.entity.Tutor;
import com.epam.javatraining.knowledgecheck.model.entity.User;

import java.sql.*;

public class TutorDao extends UserDao {

    public TutorDao() {
        super();
    }

    private void insertProfile(Tutor tutor) throws DAOException {
        String sql = "INSERT INTO tutor_profiles (id, position, scientific_degree, academicTitle) " +
                "VALUES(?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, tutor.getId());
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
    
    public int insert(Tutor tutor) throws DAOException {
        int resultId = super.insert(tutor);
        insertProfile(tutor);

        return resultId;
    }

    public boolean delete(Tutor tutor) throws DAOException {
        // DELETE CASCADE
        return super.delete(tutor);
    }

    private boolean updateProfile(Tutor tutor) throws DAOException {
        String sql = "UPDATE tutor_profiles SET "
                +"position = ?, scientific_degree = ?, academicTitle = ?"
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
            statement.setInt(4, tutor.getId());

            isRowUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Updating tutor data failed.", e);
        } finally {
            closeCommunication(connection, statement);
        }

        return isRowUpdated;
    }

    public boolean update(Tutor tutor) throws DAOException {
        if(!super.update(tutor)) {
            return false;
        }

        if(!updateProfile(tutor)) {
            insertProfile(tutor);
        }

        return true;
    }

    private Tutor extractTutorFromResultSet(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id");
        String firstname = resultSet.getString("firstname");
        String lastname = resultSet.getString("lastname");
        String email = resultSet.getString("email");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        int role = resultSet.getInt("role");
        String position = resultSet.getString("position");
        String scientificDegree = resultSet.getString("scientific_degree");
        String academicTitle = resultSet.getString("academicTitle");

        Tutor tutor = new Tutor();
        tutor.setId(id);
        tutor.setFirstname(firstname);
        tutor.setLastname(lastname);
        tutor.setEmail(email);
        tutor.setRole(User.Role.fromOrdinal(role));
        tutor.setUsername(username);
        tutor.setPassword(password);
        tutor.setPosition(position);
        tutor.setScientificDegree(scientificDegree);
        tutor.setAcademicTitle(academicTitle);
        return tutor;
    }

    public Tutor get(int id) throws DAOException {
        Tutor tutor = null;
        final User.Role role = User.Role.TUTOR;
        String sql = "select `users`.`id`," +
                " `firstname`," +
                " `lastname`," +
                " `email`," +
                " `role`," +
                " `username`," +
                " `password`," +
                " `position`," +
                " `scientific_degree`," +
                " `academicTitle`" +
                " from users" +
                " left join tutor_profiles on users.id = tutor_profiles.id" +
                " where users.id = ? AND role = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setInt(2, role.ordinal());
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                tutor = extractTutorFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DAOException("Reading tutor data failed.", e);
        } finally {
            closeCommunication(connection, statement, resultSet);
        }

        return tutor;
    }
}
