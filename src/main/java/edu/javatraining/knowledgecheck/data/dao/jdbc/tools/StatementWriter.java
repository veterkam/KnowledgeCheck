package edu.javatraining.knowledgecheck.data.dao.jdbc.tools;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementWriter {
    void write(PreparedStatement statement) throws SQLException;
}
