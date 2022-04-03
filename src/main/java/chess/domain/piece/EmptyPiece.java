package chess.domain.piece;

import chess.domain.board.position.Position;
import chess.domain.piece.attribute.Name;
import chess.domain.piece.attribute.Team;

public final class EmptyPiece extends DefaultPiece {

    private static final String NO_MOVE_MESSAGE = "해당 칸에는 움직일 기물이 없습니다.";
    private static final double SCORE = 0;

    public EmptyPiece() {
        super(Name.NONE, Team.NONE);
    }

    @Override
    public boolean canMove(Piece targetPiece, Position from, Position to) {
        throw new IllegalArgumentException(NO_MOVE_MESSAGE);
    }

    @Override
    public double getScore() {
        return SCORE;
    }
}