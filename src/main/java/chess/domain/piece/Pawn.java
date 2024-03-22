package chess.domain.piece;

import static chess.domain.piece.Type.PAWN;

import chess.domain.position.Position;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private static final int STAY = 0;
    private static final int ONE_SQUARE = 1;
    private static final int TWO_SQUARES = 2;

    public Pawn(Color color) {
        super(color);
    }

    @Override
    public String identifyType() {
        return PAWN.name();
    }

    @Override
    public boolean canMove(Position source, Position target, Color targetPieceColor) {
        if (this.color == targetPieceColor) {
            return false;
        }
        if (this.color == Color.BLACK) {
            return checkBlack(source, target, targetPieceColor);
        }
        return checkWhite(source, target, targetPieceColor);
    }

    @Override
    public List<Position> searchPath(Position source, Position target) {
        int rankDiff = source.calculateRankDifference(target);
        List<Position> path = new ArrayList<>();

        if (Math.abs(rankDiff) == TWO_SQUARES) {
            source = source.move(STAY, rankDiff / TWO_SQUARES);
            path.add(source);
        }
        return path;
    }

    private boolean checkBlack(Position source, Position target, Color color) {
        int rankDiff = source.calculateRankDifference(target);
        int fileDiff = source.calculateFileDifference(target);

        if (rankDiff == -ONE_SQUARE && Math.abs(fileDiff) == ONE_SQUARE) {
            return color == Color.WHITE;
        }
        if (color == Color.WHITE) {
            return false;
        }
        if (source.isPawnFirstTry(this.color)) {
            return (rankDiff == -ONE_SQUARE || rankDiff == -TWO_SQUARES) && (fileDiff == STAY);
        }
        return rankDiff == -ONE_SQUARE && fileDiff == STAY;
    }

    private boolean checkWhite(Position source, Position target, Color color) {
        int rankDiff = source.calculateRankDifference(target);
        int fileDiff = source.calculateFileDifference(target);

        if (rankDiff == ONE_SQUARE && Math.abs(fileDiff) == ONE_SQUARE) {
            return color == Color.BLACK;
        }
        if (color == Color.BLACK) {
            return false;
        }
        if (source.isPawnFirstTry(this.color)) {
            return (rankDiff == ONE_SQUARE || rankDiff == TWO_SQUARES) && (fileDiff == STAY);
        }
        return rankDiff == ONE_SQUARE && fileDiff == STAY;
    }
}
