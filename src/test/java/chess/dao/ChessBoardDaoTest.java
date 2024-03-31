package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Rook;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.ChessGameComponentDto;
import chess.service.ConnectionGenerator;
import java.sql.Connection;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessBoardDaoTest implements DaoTest {
    private ChessBoardDao chessBoardDao;
    private Connection connection;

    @BeforeEach
    void initializeChessGameDao() {
        chessBoardDao = new ChessBoardDao();
        ConnectionGenerator testConnectionGenerator = ConnectionGenerator.from(TEST_CONFIGURATION_FILE_NAME);
        connection = testConnectionGenerator.getConnection();
    }

    @DisplayName("데이터베이스에서 전체 데이터를 조회한다.")
    @Test
    void findAll() {
        // when
        List<ChessGameComponentDto> dtos = chessBoardDao.findAll(connection);

        // then
        assertThat(dtos.size()).isEqualTo(32);
    }

    @DisplayName("데이터베이스에 데이터를 저장한다.")
    @Test
    void save() {
        // given
        ChessGameComponentDto chessGameComponentDto = new ChessGameComponentDto(
                Position.of(File.A, Rank.ONE), new Rook(Color.WHITE), 1);

        // when
        chessBoardDao.save(chessGameComponentDto, connection);

        // then
        assertThat(chessBoardDao.findAll(connection).size()).isEqualTo(33);
    }

    @DisplayName("데이터베이스에서 position에 해당되는 piece를 찾아온다.")
    @Test
    void findPieceByPosition() {
        // given
        Position position = Position.of(File.A, Rank.ONE);

        // when
        Piece piece = chessBoardDao.findPieceByPosition(position, connection);

        // then
        assertAll(
                () -> assertThat(piece).isInstanceOf(Rook.class),
                () -> assertThat(piece.getColor()).isEqualTo(Color.WHITE)
        );
    }

    @DisplayName("piece가 이동하면 데이터베이스에서 해당 정보를 수정한다.")
    @Test
    void update() {
        // given
        Position source = Position.of(File.A, Rank.ONE);
        Position target = Position.of(File.B, Rank.FIVE);

        // when
        chessBoardDao.update(source, target, connection);
        Piece targetPiece = chessBoardDao.findPieceByPosition(target, connection);

        // then
        assertAll(
                () -> assertThat(targetPiece).isInstanceOf(Rook.class),
                () -> assertThatThrownBy(() -> chessBoardDao.findPieceByPosition(source, connection))
                        .isInstanceOf(NoSuchElementException.class));
    }

    @DisplayName("piece가 제거되면 데이터베이스에서 해당 정보를 삭제한다.")
    @Test
    void remove() {
        // given
        Position target = Position.of(File.A, Rank.ONE);

        // when
        chessBoardDao.remove(target, connection);

        // then
        assertThatThrownBy(() -> chessBoardDao.findPieceByPosition(target, connection))
                .isInstanceOf(NoSuchElementException.class);
    }
}
