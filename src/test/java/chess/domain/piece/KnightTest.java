package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Positions;
import chess.domain.position.Rank;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KnightTest {
    Knight knight;
    Position source;

    @BeforeEach
    void setUp() {
        knight = new Knight(Color.WHITE);
        source = Positions.of(File.B, Rank.ONE);
    }

    @DisplayName("나이트는 상, 하 1칸 + 좌, 우 2칸 움직일 수 있다.")
    @Test
    void canMove_1() {
        // given
        Position target = Positions.of(File.D, Rank.TWO);
        Color color = Color.NONE;

        // when
        boolean canMove = knight.canMove(source, target, color);

        //then
        assertThat(canMove).isTrue();
    }

    @DisplayName("나이트는 상, 하 2칸 + 좌, 우 1칸 움직일 수 있다.")
    @Test
    void canMove_2() {
        // given
        Position target = Positions.of(File.A, Rank.THREE);
        Color color = Color.NONE;

        // when
        boolean canMove = knight.canMove(source, target, color);

        //then
        assertThat(canMove).isTrue();
    }

    @DisplayName("target 위치에 같은 색의 말이 있다면 움직일 수 없다.")
    @Test
    void canNotMoveWithSameColor() {
        // given
        Position target = Positions.of(File.A, Rank.THREE);
        Color color = Color.WHITE;

        // when
        boolean canMove = knight.canMove(source, target, color);

        //then
        assertThat(canMove).isFalse();
    }

    @DisplayName("나이트는 나이트 이동 규칙에 해당하지 않는 위치로 움직일 수 없다.")
    @Test
    void canNotMoveInvalidPath() {
        // given
        Position target = Positions.of(File.D, Rank.THREE);
        Color color = Color.NONE;

        // when
        boolean canMove = knight.canMove(source, target, color);

        // then
        assertThat(canMove).isFalse();
    }

    @DisplayName("나이트의 이동 경로는 계산하지 않는다.")
    @Test
    void makePath() {
        // given
        Position target = Positions.of(File.C, Rank.THREE);

        // when
        List<Position> movingPath = knight.searchPath(source, target);

        // then
        assertThat(movingPath).isEmpty();
    }

}
