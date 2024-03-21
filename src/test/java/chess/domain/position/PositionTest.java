package chess.domain.position;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PositionTest {

    @DisplayName("주어진 File, Rank와 일치하는 포지션인지 확인한다.")
    @Test
    void findPosition() {
        // given
        Rank rank = Rank.ONE;
        File file = File.A;
        Position position = Positions.of(File.A, Rank.ONE);

        // when
        boolean isRightPosition = position.findPosition(file, rank);

        // then
        assertThat(isRightPosition).isTrue();
    }
}
