package edu.javatraining.knowledgecheck.data.dao.jdbc;

import edu.javatraining.knowledgecheck.data.connection.ConnectionPool;
import edu.javatraining.knowledgecheck.data.dao.TutorDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.tools.PrimitiveEnvelope;
import edu.javatraining.knowledgecheck.domain.Tutor;
import edu.javatraining.knowledgecheck.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TutorDaoJdbc extends UserDaoJdbc implements TutorDao {

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

    public TutorDaoJdbc(ConnectionPool pool) {
        super(pool);
    }

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
    public Tutor findOneById(Long id){

        final int role = User.Role.TUTOR.ordinal();
        String sql = BASIC_SELECT +
                " where users.id = ? AND role = ?";

        PrimitiveEnvelope<Tutor> tutor = new PrimitiveEnvelope<>();

        select(sql,
                (statement -> {
                    statement.setLong(1, id);
                    statement.setInt(2, role);
                }),
                (resultSet -> {
                    if(resultSet.next()) {
                        tutor.value = new Tutor();
                        super.extractUserFromResultSet(tutor.value, resultSet);
                        extractProfileFromResultSet(tutor.value, resultSet);
                    }
                }));

        return tutor.value;
    }

    @Override
    public Tutor findOneByUsername(String username) {

        final int role = User.Role.TUTOR.ordinal();
        String sql = BASIC_SELECT +
                " where users.username = ? AND role = ?";

        PrimitiveEnvelope<Tutor> tutor = new PrimitiveEnvelope<>();

        select(sql,
                (statement -> {
                    statement.setString(1, username);
                    statement.setInt(2, role);
                }),
                (resultSet -> {
                    if(resultSet.next()) {
                        tutor.value = new Tutor();
                        super.extractUserFromResultSet(tutor.value, resultSet);
                        extractProfileFromResultSet(tutor.value, resultSet);
                    }
                }));

        return tutor.value;
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

        select(sql,
                (statement -> {
                    statement.setLong(1, tutor.getId());
                    statement.setInt(2, tutor.getRole().ordinal());
                }),
                (resultSet -> {
                    if(resultSet.next()) {
                        extractProfileFromResultSet(tutor, resultSet);
                    }
                }));

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
    public boolean deleteById(Long id) {
        // DELETE CASCADE
        return super.deleteById(id);
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
    public List<Tutor> findAllTutors() {
        return findAllTutors(null, null);
    }

    @Override
    public List<Tutor> findAllTutors(Long offset, Long count) {

        List<Tutor> tutorList = new ArrayList<>();
        String sql = BASIC_SELECT + " where role = ?";
        if(offset != null) {
            sql += " LIMIT ?, ?";
        }

        select(sql,
                (statement -> {
                    statement.setInt(1, User.Role.TUTOR.ordinal());
                    if(offset != null) {
                        statement.setLong(2, offset);
                        statement.setLong(3, count);
                    }
                }),
                (resultSet -> {
                    while (resultSet.next()) {
                        Tutor tutor = new Tutor();
                        super.extractUserFromResultSet(tutor, resultSet);
                        extractProfileFromResultSet(tutor, resultSet);
                        tutorList.add(tutor);
                    }
                }));

        return tutorList;//.toArray(new Tutor[tutorList.size()]);
    }

    @Override
    public Long count() {
        final int role = User.Role.TUTOR.ordinal();
        String sql = "SELECT COUNT(*) FROM users WHERE role = ";

        PrimitiveEnvelope<Long>  count = new PrimitiveEnvelope<>();

        select(sql,

                (statement -> {
                    statement.setInt(1, role);
                }),

                (resultSet -> {
                    if(resultSet.next()) {
                        count.value = resultSet.getLong(1);
                    }
                }));

        return count.value;
    }

    private void insertProfile(Tutor tutor) {
        String sql = "INSERT INTO tutor_profiles (id, position, scientific_degree, academic_title) " +
                "VALUES(?, ?, ?, ?)";

        insert(sql,
                (statement -> {
                    statement.setLong(1, tutor.getId());
                    statement.setString(2, tutor.getPosition());
                    statement.setString(3, tutor.getScientificDegree());
                    statement.setString(4, tutor.getAcademicTitle());
                }));
    }

    private boolean updateProfile(Tutor tutor) {
        String sql = "UPDATE tutor_profiles SET "
                +"position = ?, scientific_degree = ?, academic_title = ?"
                + "WHERE id = ?";

        return update(sql,
                (statement -> {
                    statement.setString(1, tutor.getPosition());
                    statement.setString(2, tutor.getScientificDegree());
                    statement.setString(3, tutor.getAcademicTitle());
                    statement.setLong(4, tutor.getId());
                }));
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
