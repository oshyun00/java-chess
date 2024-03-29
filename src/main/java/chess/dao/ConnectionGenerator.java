package chess.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionGenerator {
    String SERVER = "localhost:13306";
    String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    String USERNAME = "root";
    String PASSWORD = "root";

    Connection getConnection();

    default void handleSQLException(SQLException e) {
        System.err.println("DB 연결 오류:" + e.getMessage());
        e.printStackTrace();
    }
}
