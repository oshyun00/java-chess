package chess.dao;

import chess.domain.board.ChessBoard;
import chess.domain.piece.Piece;
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
    private final PieceDao pieceDao;

    private ChessBoardDao(PieceDao pieceDao) {
        this.pieceDao = pieceDao;
    }

    public ChessBoardDao() {
        this(new PieceDao());
    }

    public List<ChessGameComponentDto> findAll(Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM chess_boards");
            final ResultSet resultSet = statement.executeQuery();

            return getChessGameComponentDtos(resultSet, connection);
        } catch (SQLException e) {
            throw new DBConnectionException("진행중인 게임 데이터를 가져올 수 없습니다.");
        }
    }

    public List<ChessGameComponentDto> findByGameName(String gameName, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM chess_boards WHERE game_name = ?");
            statement.setString(1, gameName);
            final ResultSet resultSet = statement.executeQuery();

            return getChessGameComponentDtos(resultSet, connection);
        } catch (SQLException e) {
            throw new DBConnectionException("게임 이름에 해당되는 게임 내역을 가져올 수 없습니다.");
        }
    }

    public Piece findPieceByPosition(Position position, String gameName, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM chess_boards WHERE `file` = ? AND `rank` = ? AND game_name = ?");
            statement.setString(1, position.getFileSymbol());
            statement.setInt(2, position.getRankValue());
            statement.setString(3, gameName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return pieceDao.findPieceById(resultSet.getInt("id"), connection);
            }
            throw new NoSuchElementException("해당 위치에 존재하는 말을 찾을 수 없습니다.");
        } catch (SQLException e) {
            throw new DBConnectionException("해당 위치에 존재하는 말을 찾을 수 없습니다.");
        }
    }

    public ChessBoard create(ChessBoard createdChessBoard, Connection connection) {
        Map<Position, Piece> chessBoard = createdChessBoard.getChessBoard();
        String gameName = createdChessBoard.getGameName();
        List<ChessGameComponentDto> dtos =
                chessBoard.entrySet().stream()
                        .map(entry -> new ChessGameComponentDto(entry.getKey(), entry.getValue(), gameName))
                        .toList();
        dtos.forEach(dto -> save(dto, connection));
        return createdChessBoard;
    }

    public void update(Position source, Position target, String gameName, Connection connection) {
        try {
            remove(target, gameName, connection);
            final PreparedStatement statement = connection.prepareStatement(
                    "UPDATE chess_boards SET file = ?, `rank` = ? WHERE file = ? AND `rank` = ? AND game_name = ?");
            statement.setString(1, target.getFileSymbol());
            statement.setInt(2, target.getRankValue());
            statement.setString(3, source.getFileSymbol());
            statement.setInt(4, source.getRankValue());
            statement.setString(5, gameName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionException("해당 위치의 게임 정보를 업데이트 할 수 없습니다.");
        }
    }

    private void save(ChessGameComponentDto chessGameComponentDto, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO chess_boards (`file`,`rank`,`piece_id`,`game_name`) VALUES (?,?,?,?)");
            statement.setString(1, chessGameComponentDto.position().getFileSymbol());
            statement.setInt(2, chessGameComponentDto.position().getRankValue());
            statement.setInt(3, pieceDao.findIdByPiece(chessGameComponentDto.piece(), connection));
            statement.setString(4, chessGameComponentDto.gameName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionException("데이터를 저장할 수 없습니다.");
        }
    }

    private void remove(Position target, String gameName, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM chess_boards WHERE file = ? AND `rank` = ? AND game_name = ?");
            statement.setString(1, target.getFileSymbol());
            statement.setInt(2, target.getRankValue());
            statement.setString(3, gameName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionException("해당 위치의 말을 제거할 수 없습니다.");
        }
    }

    private List<ChessGameComponentDto> getChessGameComponentDtos(ResultSet resultSet, Connection connection)
            throws SQLException {
        final List<ChessGameComponentDto> chessBoardComponents = new ArrayList<>();
        while (resultSet.next()) {
            File file = File.convertToFile(resultSet.getString("file"));
            Rank rank = Rank.convertToRank(resultSet.getInt("rank"));
            int pieceId = resultSet.getInt("piece_id");
            Piece piece = pieceDao.findPieceById(pieceId, connection);
            String gameName = resultSet.getString("game_name");
            ChessGameComponentDto chessGameComponentDto
                    = new ChessGameComponentDto(Position.of(file, rank), piece, gameName);
            chessBoardComponents.add(chessGameComponentDto);
        }
        return chessBoardComponents;
    }
}
