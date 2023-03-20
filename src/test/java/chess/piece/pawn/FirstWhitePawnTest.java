package chess.piece.pawn;

import static chess.PositionFixtures.A3;
import static chess.PositionFixtures.A4;
import static chess.PositionFixtures.B3;
import static chess.piece.PiecesFixtures.KNIGHT_BLACK_B3;
import static chess.piece.pawn.PawnFixtures.PAWN_WHITE_A2;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;

class FirstWhitePawnTest {

    /*
    ........
    ........
    ........
    ........
    *.......
    *.......
    p.......
    ........
     */
    @Test
    void legalMovePositions_a2() {
        final var movablePositions = PAWN_WHITE_A2.legalMovePositions(Set.of());

        assertThat(movablePositions).containsOnly(A3, A4);
    }

    /*
    ........
    ........
    ........
    ........
    *.......
    *N......
    p.......
    ........
     */
    @Test
    void legalMovePositions_a2_opposite() {
        final var movablePositions = PAWN_WHITE_A2.legalMovePositions(
                Set.of(KNIGHT_BLACK_B3)
        );

        assertThat(movablePositions).containsOnly(A3, A4, B3);
    }
}
