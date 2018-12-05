package edu.javatraining.knowledgecheck.data.dao.jdbc.tools;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetReader {
    void read(ResultSet resultSet) throws SQLException;
}
