package chess.dao;

import chess.domain.board.GameInformation;
import chess.domain.piece.Color;
import chess.exception.DBConnectionException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GameInformationDao {
    public List<GameInformation> findAll(Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM game_information");
            final ResultSet resultSet = statement.executeQuery();

            return convertToGameInformation(resultSet);
        } catch (SQLException e) {
            throw new DBConnectionException("진행중인 게임을 찾을 수 없습니다.");
        }
    }

    public GameInformation findByGameName(String gameName, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM game_information WHERE game_name = ?");
            statement.setString(1, gameName);
            final ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Color color = Color.convertToColor(resultSet.getString("current_turn_color"));
                return new GameInformation(gameName, color);
            }
            throw new NoSuchElementException("게임 이름에 해당되는 게임 정보를 찾을 수 없습니다.");
        } catch (SQLException e) {
            throw new DBConnectionException("게임 이름에 해당되는 게임 정보를 찾을 수 없습니다.");
        }
    }

    public void remove(String gameName, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM game_information WHERE game_name = ?");
            statement.setString(1, gameName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionException("게임 이름에 해당되는 게임 정보를 삭제할 수 없습니다.");
        }
    }

    public void create(GameInformation gameInformation, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO game_information (game_name, current_turn_color) VALUES (?, ?)");
            statement.setString(1, gameInformation.getGameName());
            statement.setString(2, Color.WHITE.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionException("새로운 게임을 생성할 수 없습니다.");
        }
    }

    public void updateTurn(GameInformation gameInformation, Connection connection) {
        try {
            final PreparedStatement statement = connection.prepareStatement(
                    "UPDATE game_information SET current_turn_color = ? WHERE game_name = ?");
            statement.setString(1, gameInformation.getCurentTurnColor().name());
            statement.setString(2, gameInformation.getGameName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionException("게임 정보를 업데이트 할 수 없습니다.");
        }
    }

    private List<GameInformation> convertToGameInformation(ResultSet resultSet) throws SQLException {
        final List<GameInformation> gameInfos = new ArrayList<>();
        while (resultSet.next()) {
            String gameName = resultSet.getString("game_name");
            Color currentTurnColor = Color.convertToColor(resultSet.getString("current_turn_color"));
            GameInformation gameInformation = new GameInformation(gameName, currentTurnColor);

            gameInfos.add(gameInformation);
        }
        return gameInfos;
    }
}
