package chess.domain.board;

import chess.domain.piece.Bishop;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChessBoard {
    private final Map<Position, Piece> chessBoard = new LinkedHashMap<>();

    public ChessBoard() {
        initializeBlackPieces();
        initializeWhitePieces();
    }

    public void move(Position source, Position target) {
        if (!canMove(source, target)) {
            throw new IllegalArgumentException("올바르지 않은 이동입니다.");
        }
        Piece sourcePiece = chessBoard.get(source);
        chessBoard.put(target, sourcePiece);
        chessBoard.remove(source);
    }

    public Map<Position, Piece> getChessBoard() {
        return chessBoard;
    }

    private boolean canMove(Position source, Position target) {
        if (!chessBoard.containsKey(source)) {
            throw new IllegalArgumentException("이동할 수 있는 말이 없습니다.");
        }

        Piece sourcePiece = chessBoard.get(source);
        Color targetPieceColor = Color.NONE;

        if (chessBoard.containsKey(target)) {
            Piece targetPiece = chessBoard.get(target);
            targetPieceColor = targetPiece.getColor();
        }

        if (sourcePiece.canMove(source, target, targetPieceColor)) {
            return sourcePiece.searchPath(source, target).stream().noneMatch(chessBoard::containsKey);
        }
        return false;
    }

    private void initializeBlackPieces() {
        initializeEdgeRank(Rank.EIGHT, Color.BLACK);
        initializePawnRank(Rank.SEVEN, Color.BLACK);
    }

    private void initializeWhitePieces() {
        initializePawnRank(Rank.TWO, Color.WHITE);
        initializeEdgeRank(Rank.ONE, Color.WHITE);
    }

    private void initializePawnRank(Rank rank, Color color) {
        for (File file : File.values()) {
            chessBoard.put(Position.of(file, rank), new Pawn(color));
        }
    }

    private void initializeEdgeRank(Rank rank, Color color) {
        chessBoard.put(Position.of(File.A, rank), new Rook(color));
        chessBoard.put(Position.of(File.B, rank), new Knight(color));
        chessBoard.put(Position.of(File.C, rank), new Bishop(color));
        chessBoard.put(Position.of(File.D, rank), new Queen(color));
        chessBoard.put(Position.of(File.E, rank), new King(color));
        chessBoard.put(Position.of(File.F, rank), new Bishop(color));
        chessBoard.put(Position.of(File.G, rank), new Knight(color));
        chessBoard.put(Position.of(File.H, rank), new Rook(color));
    }
}