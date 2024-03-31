package chess.dao;

import chess.exception.DBConnectionException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.BeforeEach;

public interface DaoTest {
    String TEST_CONFIGURATION_FILE_NAME = "src/main/java/chess/resource/test.yml";
    String TEST_INIT_DATA_FILE_NAME = "docker/db/mysql/init/init_for_test.sql";

    /*
     * 초기 체스판 상태
     * RNBQKBNR
     * PPPPPPPP
     * ........
     * ........
     * ........
     * ........
     * pppppppp
     * rnbqkbnr
     */
    @BeforeEach
    default void setUp() {
        try {
            executeInitScript();
        } catch (IOException | SQLException e) {
            System.err.println("DB 연결 오류:" + e.getMessage());
            e.printStackTrace();
            throw new DBConnectionException("테스트 데이터베이스 초기화에 실패하였습니다.");
        }
    }

    private void executeInitScript() throws IOException, SQLException {
        ConnectionGenerator testConnectionGenerator = ConnectionGenerator.from(TEST_CONFIGURATION_FILE_NAME);
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_INIT_DATA_FILE_NAME));
             Connection connection = testConnectionGenerator.getConnection();
             Statement statement = connection.createStatement()) {
            String line;
            StringBuilder scriptContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                handleScriptLine(line, scriptContent, statement);
            }
        }
    }

    private void handleScriptLine(String line, StringBuilder scriptContent, Statement statement) throws SQLException {
        if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
            scriptContent.append(line).append("\n");
        }
        if (line.trim().endsWith(";")) {
            statement.execute(scriptContent.toString());
            scriptContent.setLength(0);
        }
    }
}
