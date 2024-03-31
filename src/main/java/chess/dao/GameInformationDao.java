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
    private final ConnectionGenerator connectionGenerator;

    public GameInformationDao(ConnectionGenerator connectionGenerator) {
        this.connectionGenerator = connectionGenerator;
    }

    public List<GameInformation> findAll() {
        try (final Connection connection = connectionGenerator.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM game_information");
            final ResultSet resultSet = statement.executeQuery();

            return convertToGameInformation(resultSet);
        } catch (SQLException e) {
            connectionGenerator.handleSQLException(e);
            throw new DBConnectionException("진행중인 게임을 찾을 수 없습니다.");
        }
    }

    public GameInformation findByGameId(int gameId) {
        try (final Connection connection = connectionGenerator.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM game_information WHERE `id` = ?");
            statement.setInt(1, gameId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Color color = Color.convertToColor(resultSet.getString("current_turn_color"));
                return new GameInformation(gameId, color);
            }
            throw new NoSuchElementException("id에 해당되는 게임 정보를 찾을 수 없습니다.");
        } catch (SQLException e) {
            connectionGenerator.handleSQLException(e);
            throw new DBConnectionException("id에 해당되는 게임 정보를 찾을 수 없습니다.");
        }
    }

    public GameInformation findLatestGame() {
        try (final Connection connection = connectionGenerator.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM game_information ORDER BY id DESC LIMIT 1");
            final ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int gameId = resultSet.getInt("id");
                Color color = Color.convertToColor(resultSet.getString("current_turn_color"));
                return new GameInformation(gameId, color);
            }
            throw new NoSuchElementException("마지막 게임을 찾을 수 없습니다.");
        } catch (SQLException e) {
            connectionGenerator.handleSQLException(e);
            throw new DBConnectionException("마지막 게임을 찾을 수 없습니다.");
        }
    }

    public void remove(int gameId) {
        try (final Connection connection = connectionGenerator.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM game_information WHERE id = ?");
            statement.setInt(1, gameId);
            statement.executeUpdate();
        } catch (SQLException e) {
            connectionGenerator.handleSQLException(e);
            throw new DBConnectionException("id에 해당되는 게임 정보를 삭제할 수 없습니다.");
        }
    }

    public void create() {
        try (final Connection connection = connectionGenerator.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO game_information (`current_turn_color`)VALUES (?)");
            statement.setString(1, Color.WHITE.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            connectionGenerator.handleSQLException(e);
            throw new DBConnectionException("새로운 게임을 생성할 수 없습니다.");
        }
    }

    public void updateTurn(GameInformation gameInformation) {
        try (final Connection connection = connectionGenerator.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement(
                    "UPDATE game_information SET current_turn_color = ? WHERE id = ?");
            statement.setString(1, gameInformation.getCurentTurnColor().name());
            statement.setInt(2, gameInformation.getGameId());
            statement.executeUpdate();
        } catch (SQLException e) {
            connectionGenerator.handleSQLException(e);
            throw new DBConnectionException("게임 정보를 업데이트 할 수 없습니다.");
        }
    }

    private List<GameInformation> convertToGameInformation(ResultSet resultSet) throws SQLException {
        final List<GameInformation> gameInfos = new ArrayList<>();
        while (resultSet.next()) {
            int gameId = resultSet.getInt("id");
            Color color = Color.convertToColor(resultSet.getString("current_turn_color"));
            GameInformation gameInformation = new GameInformation(gameId, color);

            gameInfos.add(gameInformation);
        }
        return gameInfos;
    }
}
