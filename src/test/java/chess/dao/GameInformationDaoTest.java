package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.GameInformation;
import chess.domain.piece.Color;
import chess.service.ConnectionGenerator;
import java.sql.Connection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GameInformationDaoTest implements DaoTest {
    private GameInformationDao gameInformationDao;
    private Connection connection;

    @BeforeEach
    void initializeChessGameDao() {
        gameInformationDao = new GameInformationDao();
        ConnectionGenerator connectionGenerator = ConnectionGenerator.from(TEST_CONFIGURATION_FILE_NAME);
        connection = connectionGenerator.getConnection();
    }

    @DisplayName("데이터베이스에서 전체 데이터를 조회한다.")
    @Test
    void findAll() {
        // when
        List<GameInformation> dtos = gameInformationDao.findAll(connection);

        // then
        assertThat(dtos).hasSize(2);
    }

    @DisplayName("gameName에 해당되는 게임 정보를 찾아온다.")
    @Test
    void findInformationByGameId() {
        // given
        String gameName = "ella";

        // when
        GameInformation gameInformation = gameInformationDao.findByGameName(gameName, connection);

        // then
        assertThat(gameInformation.getCurentTurnColor()).isEqualTo(Color.WHITE);
    }

    @DisplayName("현재 진행중인 팀의 색상을 전환한다.")
    @Test
    void updateTurn() {
        // given
        String gameName = "ella";
        GameInformation gameInformation = new GameInformation(gameName, Color.BLACK);

        // when
        gameInformationDao.updateTurn(gameInformation, connection);
        GameInformation updatedInformation = gameInformationDao.findByGameName(gameName, connection);

        // then
        assertThat(updatedInformation.getCurentTurnColor()).isEqualTo(Color.BLACK);
    }

    @DisplayName("게임 이름에 해당되는 게임을 삭제한다.")
    @Test
    void remove() {
        // given
        String gameName = "ella";

        // when
        gameInformationDao.remove(gameName, connection);
        List<GameInformation> gameInfos = gameInformationDao.findAll(connection);

        // then
        assertThat(gameInfos).hasSize(1);
    }

    @DisplayName("데이터베이스에 새로운 게임 데이터를 저장한다.")
    @Test
    void save() {
        // given
        GameInformation gameInformation = new GameInformation("lily");

        // when
        gameInformationDao.create(gameInformation, connection);
        List<GameInformation> gameInfos = gameInformationDao.findAll(connection);

        // then
        assertThat(gameInfos).hasSize(3);
    }
}
