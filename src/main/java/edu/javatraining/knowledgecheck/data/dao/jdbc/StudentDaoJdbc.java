package edu.javatraining.knowledgecheck.data.dao.jdbc;

import com.google.inject.Singleton;
import edu.javatraining.knowledgecheck.data.dao.StudentDao;
import edu.javatraining.knowledgecheck.data.dao.jdbc.tools.PrimitiveEnvelope;
import edu.javatraining.knowledgecheck.domain.Student;
import edu.javatraining.knowledgecheck.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class StudentDaoJdbc extends UserDaoJdbc implements StudentDao {

    private final static String BASIC_SELECT = "SELECT users.`id` as `id`, " +
            " `firstname`, " +
            " `lastname`, " +
            " `email`, " +
            " `role`, " +
            " `username`, " +
            " `password`, " +
            " `verified`, " +
            " `specialty`, " +
            " `group`, " +
            " `year` " +
            " from users" +
            " left join student_profiles on users.id = student_profiles.id";
    
    public StudentDaoJdbc() {
        super();
    }

    @Override
    public Long save(Student entity) {

        Long id = entity.getId();
        if(!update(entity)) {
            id = insert(entity);
        }

        return id;
    }

    @Override
    public Student findOne(Student entity) {
        Student student = null;

        if(entity.getId() != null) {
            student = findOneById(entity.getId());
        }

        if(student == null && entity.getUsername() != null) {
            student = findOneByUsername(entity.getUsername());
        }

        return student;
    }

    @Override
    public Student findOneById(Long id) {
        String sql = BASIC_SELECT +
                " where users.id = ? and users.role = ?";

        PrimitiveEnvelope<Student> student = new PrimitiveEnvelope<>();
        select(sql,
                (statement -> {
                    statement.setLong(1, id);
                    statement.setInt(2, User.Role.STUDENT.ordinal());
                }),
                (resultSet -> {
                    if(resultSet.next()) {
                        student.value = new Student();
                        extractUserFromResultSet(student.value, resultSet);
                        extractProfileFromResultSet(student.value, resultSet);
                    }
                }));

        return student.value;
    }

    @Override
    public Student findOneByUsername(String username) {
        String sql = BASIC_SELECT +
                " where users.username = ? and users.role = ?";

        PrimitiveEnvelope<Student> student = new PrimitiveEnvelope<>();
        select(sql,
                (statement -> {
                    statement.setString(1, username);
                    statement.setInt(2, User.Role.STUDENT.ordinal());
                }),
                (resultSet -> {
                    if(resultSet.next()) {
                        student.value = new Student();
                        extractUserFromResultSet(student.value, resultSet);
                        extractProfileFromResultSet(student.value, resultSet);
                    }
                }));

        return student.value;
    }

    @Override
    public Student attachProfile(User user) {
        if(user == null || user.getRole() != User.Role.STUDENT) {
            return null;
        }

        String sql = "SELECT " +
                " `specialty`, " +
                " `group`, " +
                " `year` " +
                " from users" +
                " left join student_profiles on users.id = student_profiles.id" +
                " where users.id = ? and users.role = ?";

        PrimitiveEnvelope<Student> student = new PrimitiveEnvelope<>();
        student.value = new Student(user);

        select(sql,
                (statement -> {
                    statement.setLong(1, student.value.getId());
                    statement.setInt(2, User.Role.STUDENT.ordinal());
                }),
                (resultSet -> {
                    if(resultSet.next()) {
                        extractProfileFromResultSet(student.value, resultSet);
                    }
                }));

        return student.value;
    }

    @Override
    public Long insert(Student student) {
        Long resultId = super.insert(student);
        insertProfile(student);

        return resultId;
    }

    @Override
    public boolean delete(Student student) {
        // DELETE CASCADE
        return super.delete(student);
    }

    @Override
    public boolean deleteById(Long id) {
        // DELETE CASCADE
        return super.deleteById(id);
    }

    @Override
    public boolean update(Student student) {
        if(!super.update(student)) {
            return false;
        }

        if(!updateProfile(student)) {
            insertProfile(student);
        }

        return true;
    }

    @Override
    public Student[] findAll() {
        return findAll(null, null);
    }

    @Override
    public Student[] findAll(Long offset, Long count) {

        List<Student> students = new ArrayList<>();
        String sql = BASIC_SELECT + " where role = ?";
        if(offset != null) {
            sql += " LIMIT ?, ?";
        }

        select(sql,

                (statement -> {
                    statement.setInt(1, User.Role.STUDENT.ordinal());
                    if(offset != null) {
                        statement.setLong(2, offset);
                        statement.setLong(3, count);
                    }
                }),

                (resultSet -> {
                    while (resultSet.next()) {
                        Student student = new Student();
                        super.extractUserFromResultSet(student, resultSet);
                        extractProfileFromResultSet(student, resultSet);
                        students.add(student);
                    }
                }));

        return  students.toArray(new Student[students.size()]);
    }

    private boolean updateProfile(Student student) {
        String sql = "UPDATE student_profiles SET "
                +"`specialty` = ?, `group` = ?, `year` = ? "
                + "WHERE id = ?";

        return update(sql,
                (statement -> {
                    statement.setString(1, student.getSpecialty());
                    statement.setString(2, student.getGroup());
                    statement.setInt(3, student.getYear());
                    statement.setLong(4, student.getId());
                }));
    }

    private void insertProfile(Student student) {
        String sql = "INSERT INTO student_profiles (`id`, `specialty`, `group`, `year`) " +
                "VALUES(?, ?, ?, ?)";

        insert(sql,
                (statement -> {
                    statement.setLong(1, student.getId());
                    statement.setString(2, student.getSpecialty());
                    statement.setString(3, student.getGroup());
                    statement.setInt(4, student.getYear());
                }));
    }

    public List<Student> getStudentsTookTest(long testId)  {

        final int role = User.Role.STUDENT.ordinal();
        List<Student> students = new ArrayList<>();

        String sql = BASIC_SELECT +
                "INNER JOIN testing_results ON users.id = testing_results.student_id " +
                "INNER JOIN questions ON questions.id = testing_results.question_id " +
                "WHERE questions.test_id = ? AND users.role = ? " +
                "ORDER BY firstname, lastname ";

        select(sql,
                (statement -> {
                    statement.setLong(1, testId);
                    statement.setInt(2, role);
                }),
                (resultSet -> {
                    while(resultSet.next()) {
                        Student student = new Student();
                        extractUserFromResultSet(student, resultSet);
                        extractProfileFromResultSet(student, resultSet);
                        students.add(student);
                    }
                }));

        return students;
    }

    private void extractProfileFromResultSet(Student student, ResultSet resultSet) throws SQLException {
        // extract profile
        String specialty = resultSet.getString("specialty");
        String group = resultSet.getString("group");
        int year = resultSet.getInt("year");

        student.setSpecialty(specialty);
        student.setGroup(group);
        student.setYear(year);
    }
}
