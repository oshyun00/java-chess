package chess.domain.state;

import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.domain.vo.Score;
import java.util.List;

public interface GameState {

    GameState play(List<String> inputCommand);

    boolean isEnd();

    Score calculateScore(Color color);

    Color getWinnerColor();

    List<Position> convertToSourceAndTarget(List<String> command);
}
