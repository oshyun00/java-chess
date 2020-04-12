package chess.dto;

import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.position.Positions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TilesDto {
    private final List<TileDto> tiles;

    public TilesDto(Board board) {
        this.tiles = toDto(board);
    }

    private List<TileDto> toDto(Board board) {
        Map<Position, Piece> fragmentedBoard = board.get();
        List<Position> positions = Positions.getPositions();

        Map<Position, Piece> entireBoard = new LinkedHashMap<>();
        for (Position position : positions) {
            entireBoard.put(position, fragmentedBoard.get(position));
        }

        return entireBoard.entrySet()
                .stream()
                .map(entry -> new TileDto(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public List<TileDto> get() {
        return tiles;
    }
}
