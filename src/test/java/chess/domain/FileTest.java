package chess.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.model.position.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FileTest {

    @ParameterizedTest
    @DisplayName("세로줄은 a ~ h 값을 벗어날 경우 예외가 발생한다.")
    @ValueSource(ints = {96, 108})
    void validateFileRange(int asciiValue) {
        assertThatThrownBy(() -> File.from((char)asciiValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기물의 세로 위치는 a부터 h까지 놓을 수 있습니다.");
    }

}