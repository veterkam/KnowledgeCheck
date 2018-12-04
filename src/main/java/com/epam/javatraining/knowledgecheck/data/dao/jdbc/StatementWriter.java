package com.epam.javatraining.knowledgecheck.data.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementWriter {
    void write(PreparedStatement statement) throws SQLException;
}
