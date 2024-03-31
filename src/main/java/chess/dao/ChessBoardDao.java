package chess.dao;

import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.ChessGameComponentDto;
import chess.exception.DBConnectionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ChessBoardDao {
    public List<ChessGameComponentDto> findAll(Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM chess_boards");
            final ResultSet resultSet = statement.executeQuery();

            return getChessGameComponentDtos(resultSet);
        } catch (SQLException e) {
            throw new DBConnectionException("진행중인 게임 데이터를 가져올 수 없습니다.");
        }
    }

    public List<ChessGameComponentDto> findById(int gameId, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM chess_boards WHERE game_id = ?");
            statement.setInt(1, gameId);
            ResultSet resultSet = statement.executeQuery();

            return getChessGameComponentDtos(resultSet);
        } catch (SQLException e) {
            throw new DBConnectionException("id에 해당되는 게임 내역을 가져올 수 없습니다.");
        }
    }

    public Piece findPieceByPosition(Position position, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM chess_boards WHERE `file` = ? AND `rank` = ?");
            statement.setString(1, position.getFileSymbol());
            statement.setInt(2, position.getRankValue());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Type type = Type.convertToType(resultSet.getString("type"));
                Color color = Color.convertToColor(resultSet.getString("color"));
                return type.generatePiece(color);
            }
            throw new NoSuchElementException("해당 위치에 존재하는 말을 찾을 수 없습니다.");
        } catch (SQLException e) {
            throw new DBConnectionException("해당 위치에 존재하는 말을 찾을 수 없습니다.");
        }
    }

    public void saveChessBoard(ChessBoard createdChessBoard, Connection connection) {
        Map<Position, Piece> chessBoard = createdChessBoard.getChessBoard();
        int gameId = createdChessBoard.getGameId();
        List<ChessGameComponentDto> dtos =
                chessBoard.entrySet().stream()
                        .map(entry -> new ChessGameComponentDto(entry.getKey(), entry.getValue(), gameId))
                        .toList();
        dtos.forEach(dto -> save(dto, connection));
    }

    public void save(ChessGameComponentDto chessGameComponentDto, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO chess_boards (`file`,`rank`,`type`,`color`,`game_id`)VALUES (?,?,?,?,?)");
            statement.setString(1, chessGameComponentDto.position().getFileSymbol());
            statement.setInt(2, chessGameComponentDto.position().getRankValue());
            statement.setString(3, chessGameComponentDto.piece().identifyType());
            statement.setString(4, chessGameComponentDto.piece().getColor().name());
            statement.setInt(5, chessGameComponentDto.gameId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionException("데이터를 저장할 수 없습니다.");
        }
    }

    public void update(Position source, Position target, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "UPDATE chess_boards SET file = ?, `rank` = ? WHERE file = ? AND `rank` = ?");
            statement.setString(1, target.getFileSymbol());
            statement.setInt(2, target.getRankValue());
            statement.setString(3, source.getFileSymbol());
            statement.setInt(4, source.getRankValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionException("해당 위치의 게임 정보를 업데이트 할 수 없습니다.");
        }
    }

    public void remove(Position target, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM chess_boards WHERE file = ? AND `rank` = ?");
            statement.setString(1, target.getFileSymbol());
            statement.setInt(2, target.getRankValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionException("해당 위치의 말을 제거할 수 없습니다.");
        }
    }

    private List<ChessGameComponentDto> getChessGameComponentDtos(ResultSet resultSet) throws SQLException {
        final List<ChessGameComponentDto> chessBoardComponents = new ArrayList<>();
        while (resultSet.next()) {
            File file = File.convertToFile(resultSet.getString("file"));
            Rank rank = Rank.convertToRank(resultSet.getInt("rank"));
            Type type = Type.convertToType(resultSet.getString("type"));
            Color color = Color.convertToColor(resultSet.getString("color"));
            int gameId = resultSet.getInt("game_id");
            ChessGameComponentDto chessGameComponentDto
                    = new ChessGameComponentDto(Position.of(file, rank), type.generatePiece(color), gameId);
            chessBoardComponents.add(chessGameComponentDto);
        }
        return chessBoardComponents;
    }
}