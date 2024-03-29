package chess.dao;

import chess.exception.DBConnectionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnectionGenerator implements ConnectionGenerator {
    private static final String DATABASE = "chess-test";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION, USERNAME, PASSWORD);
        } catch (SQLException e) {
            handleSQLException(e);
            throw new DBConnectionException("데이터베이스 연결에 실패하였습니다.");
        }
    }
}
