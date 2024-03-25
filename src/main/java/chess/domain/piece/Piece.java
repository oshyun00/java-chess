package chess.domain.piece;

import chess.domain.position.Position;
import chess.domain.vo.Score;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected final Color color;
    protected final Score score;

    public Piece(Color color, Score score) {
        this.color = color;
        this.score = score;
    }

    public abstract String identifyType();

    public abstract boolean canMove(Position source, Position target, Color color);

    public abstract List<Position> searchPath(Position source, Position target);

    public boolean isBlack() {
        return color == Color.BLACK;
    }

    public boolean isKing() {
        return this instanceof King;
    }

    public boolean isPawn() {
        return this instanceof Pawn;
    }

    public Color getColor() {
        return color;
    }

    public double getScore() {
        return score.getValue();
    }
}
