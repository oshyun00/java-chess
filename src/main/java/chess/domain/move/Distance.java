package chess.domain.move;

import chess.domain.board.Position;

public final class Distance {

    protected static final int NOT_MOVE = 0;

    private final int horizon;
    private final int vertical;

    private Distance(final int horizon, final int vertical) {
        this.horizon = horizon;
        this.vertical = vertical;
    }

    public static Distance of(final Position source, final Position target) {
        return new Distance(source.subtractColumn(target), source.subtractRow(target));
    }

    public boolean isVerticalMovement() {
        return vertical != NOT_MOVE && horizon == NOT_MOVE;
    }

    public boolean isHorizontalMovement() {
        return horizon != NOT_MOVE && vertical == NOT_MOVE;
    }

    public boolean isPositiveDiagonal() {
        return isSameAbs() && vertical / horizon == 1;
    }

    public boolean isNegativeDiagonal() {
        return isSameAbs() && vertical / horizon == -1;
    }

    private boolean isSameAbs() {
        return Math.abs(vertical) == Math.abs(horizon);
    }

    public int getVertical() {
        return vertical;
    }

    public int getHorizon() {
        return horizon;
    }
}