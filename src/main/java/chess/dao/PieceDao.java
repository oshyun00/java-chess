package chess.dao;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;
import chess.exception.DBConnectionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PieceDao {
    public Piece findPieceById(int pieceId, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM pieces WHERE id = ?");
            statement.setInt(1, pieceId);
            final ResultSet resultSet = statement.executeQuery();
            return getPiece(resultSet);
        } catch (SQLException e) {
            throw new DBConnectionException("Piece 정보를 찾을 수 없습니다.");
        }
    }

    public int findIdByPiece(Piece piece, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT id FROM pieces WHERE type = ? AND color = ?");
            statement.setString(1, piece.identifyType());
            statement.setString(2, piece.getColor().name());
            final ResultSet resultSet = statement.executeQuery();
            return getPieceId(resultSet);
        } catch (SQLException e) {
            throw new DBConnectionException("Piece 정보를 찾을 수 없습니다.");
        }
    }

    private Piece getPiece(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Type type = Type.convertToType(resultSet.getString("type"));
            Color color = Color.convertToColor(resultSet.getString("color"));
            return type.generatePiece(color);
        }
        throw new DBConnectionException("적절한 Piece를 찾지 못했습니다.");
    }

    private int getPieceId(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            return resultSet.getInt("id");
        }
        throw new DBConnectionException("적절한 PieceId를 찾지 못했습니다.");
    }
}
