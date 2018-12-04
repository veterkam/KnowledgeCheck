package com.epam.javatraining.knowledgecheck.data.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetReader {
    void read(ResultSet resultSet) throws SQLException;
}
