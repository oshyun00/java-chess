package chess.domain.state;

import chess.domain.board.ChessBoard;
import java.util.List;

public class Ready implements GameState {
    private static final String START_COMMAND = "start";
    private static final String MOVE_COMMAND = "move";
    private static final String END_COMMAND = "end";

    private final ChessBoard chessBoard;

    public Ready(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    @Override
    public GameState play(List<String> inputCommand) {
        String command = inputCommand.get(0);
        if (START_COMMAND.equals(command)) {
            return new Progress(chessBoard);
        }
        if (MOVE_COMMAND.equals(command)) {
            throw new UnsupportedOperationException("게임이 시작되지 않았습니다.");
        }
        if (END_COMMAND.equals(command)) {
            throw new UnsupportedOperationException("게임이 시작되지 않았습니다.");
        }
        throw new IllegalArgumentException("올바르지 않은 command입니다.");
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}
