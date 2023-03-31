package chess.domain.state;

import chess.TestPiecesGenerator;
import chess.dao.InMemoryChessGameDao;
import chess.dao.InMemoryPieceDao;
import chess.domain.ChessGame;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.property.Color;
import chess.exception.ChessException;
import chess.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static chess.PositionFixture.A2;
import static chess.PositionFixture.A4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ChessEndTest {

    private static final int TEST_GAME_ROOM_ID = 1;
    private static final Pawn pawn = new Pawn(A2, Color.WHITE);

    @Nested
    @DisplayName("게임 커멘드 실행시 체스가 종료되었다는 예외를 발생시킨다.")
    class command_test {

        @Test
        @DisplayName("게임 시작")
        void start_throws_exception() {
            final ChessEnd chessEnd = new ChessEnd(
                    ChessGame.createWith(new TestPiecesGenerator(List.of(pawn)), ChessGame.INITIAL_STARTING_COLOR, TEST_GAME_ROOM_ID),
                    new InMemoryChessGameDao(),
                    new InMemoryPieceDao());

            assertThatThrownBy(() -> chessEnd.start(1))
                    .isInstanceOf(ChessException.class)
                    .hasMessage(ExceptionCode.GAME_END.name());
        }

        @Test
        @DisplayName("이동")
        void move_throws_exception() {
            final ChessEnd chessEnd = new ChessEnd(
                    ChessGame.createWith(new TestPiecesGenerator(List.of(pawn)), ChessGame.INITIAL_STARTING_COLOR, TEST_GAME_ROOM_ID),
                    new InMemoryChessGameDao(),
                    new InMemoryPieceDao());

            assertThatThrownBy(() -> chessEnd.move(A2, A4))
                    .isInstanceOf(ChessException.class)
                    .hasMessage(ExceptionCode.GAME_END.name());
        }

        @Test
        @DisplayName("종료")
        void end_throws_exception() {
            final ChessEnd chessEnd = new ChessEnd(
                    ChessGame.createWith(new TestPiecesGenerator(List.of(pawn)), ChessGame.INITIAL_STARTING_COLOR, TEST_GAME_ROOM_ID),
                    new InMemoryChessGameDao(),
                    new InMemoryPieceDao());

            assertThatThrownBy(() -> chessEnd.end())
                    .isInstanceOf(ChessException.class)
                    .hasMessage(ExceptionCode.GAME_END.name());
        }

        @Test
        @DisplayName("게임 상태")
        void status_throws_exception() {
            final ChessEnd chessEnd = new ChessEnd(
                    ChessGame.createWith(new TestPiecesGenerator(List.of(pawn)), ChessGame.INITIAL_STARTING_COLOR, TEST_GAME_ROOM_ID),
                    new InMemoryChessGameDao(),
                    new InMemoryPieceDao());

            assertThatThrownBy(() -> chessEnd.status())
                    .isInstanceOf(ChessException.class)
                    .hasMessage(ExceptionCode.GAME_END.name());
        }

    }

    @Test
    @DisplayName("체스말을 가져온다")
    void getting_existing_piece_test() {
        final ChessEnd chessEnd = new ChessEnd(
                ChessGame.createWith(new TestPiecesGenerator(List.of(pawn)), ChessGame.INITIAL_STARTING_COLOR, TEST_GAME_ROOM_ID),
                new InMemoryChessGameDao(),
                new InMemoryPieceDao());

        final Set<Piece> existingPieces = chessEnd.getExistingPieces();

        assertSoftly(softly -> {
            softly.assertThat(existingPieces).hasSize(1);
            softly.assertThat(existingPieces).containsExactly(pawn);
        });

    }

    @Test
    @DisplayName("게임 종료 여부를 확인한다")
    void check_if_is_end_test() {
        final ChessEnd chessEnd = new ChessEnd(
                ChessGame.createWith(new TestPiecesGenerator(List.of(pawn)), ChessGame.INITIAL_STARTING_COLOR, TEST_GAME_ROOM_ID),
                new InMemoryChessGameDao(),
                new InMemoryPieceDao());

        final boolean isEnd = chessEnd.isEnd();

        assertThat(isEnd).isTrue();
    }

}