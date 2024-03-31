package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.piece.Color;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.service.ConnectionGenerator;
import java.sql.Connection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PieceDaoTest implements DaoTest {
    private PieceDao pieceDao;
    private Connection connection;

    @BeforeEach
    void initializePieceDao() {
        pieceDao = new PieceDao();
        ConnectionGenerator testConnectionGenerator = ConnectionGenerator.from(TEST_CONFIGURATION_FILE_NAME);
        connection = testConnectionGenerator.getConnection();
    }

    @DisplayName("pieceId로 해당되는 Piece를 찾는다.")
    @Test
    void findPieceById() {
        // when
        int pieceId = 2;
        Piece result = pieceDao.findPieceById(pieceId, connection);

        // then
        assertAll(() -> assertThat(result).isInstanceOf(Knight.class),
                () -> assertThat(result.getColor()).isEqualTo(Color.WHITE));
    }

    @DisplayName("Piece로 해당되는 pieceId를 찾는다.")
    @Test
    void findIdByPiece() {
        // when
        Piece piece = new Pawn(Color.BLACK);
        int pieceId = pieceDao.findIdByPiece(piece, connection);

        // then
        assertThat(pieceId).isEqualTo(12);
    }
}
