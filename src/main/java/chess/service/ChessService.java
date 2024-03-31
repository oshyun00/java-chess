package chess.service;

import chess.dao.ChessBoardDao;
import chess.dao.GameInformationDao;
import chess.domain.board.ChessBoard;
import chess.domain.board.GameInformation;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.ChessGameComponentDto;
import chess.exception.DBConnectionException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChessService {
    private static final int SOURCE_INDEX = 0;
    private static final int TARGET_INDEX = 1;

    private final GameInformationDao gameInformationDao;
    private final ChessBoardDao chessBoardDao;
    private final ConnectionGenerator connectionGenerator;

    public ChessService(GameInformationDao gameInformationDao, ChessBoardDao chessBoardDao,
                        ConnectionGenerator connectionGenerator) {
        this.gameInformationDao = gameInformationDao;
        this.chessBoardDao = chessBoardDao;
        this.connectionGenerator = connectionGenerator;
    }

    public List<GameInformation> getAllGameInformation() {
        Connection connection = connectionGenerator.getConnection();
        try {
            connection.setAutoCommit(false);
            List<GameInformation> gameInfos = gameInformationDao.findAll(connection);
            connection.commit();
            return gameInfos;
        } catch (RuntimeException | SQLException e) {
            rollback(connection);
            throw new DBConnectionException(e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }

    public void removeFinishedGame(ChessBoard chessBoard) {
        Connection connection = connectionGenerator.getConnection();
        try {
            connection.setAutoCommit(false);
            gameInformationDao.remove(chessBoard.getGameName(), connection);
            connection.commit();
        } catch (RuntimeException | SQLException e) {
            rollback(connection);
            throw new DBConnectionException(e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }

    public void updateChessBoard(List<Position> movedPath, GameInformation gameInformation) {
        Connection connection = connectionGenerator.getConnection();
        try {
            connection.setAutoCommit(false);
            chessBoardDao.update(movedPath.get(SOURCE_INDEX), movedPath.get(TARGET_INDEX),
                    gameInformation.getGameName(), connection);
            gameInformationDao.updateTurn(gameInformation, connection);
            connection.commit();
        } catch (RuntimeException | SQLException e) {
            rollback(connection);
            throw new DBConnectionException(e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }

    public ChessBoard loadChessBoard(List<GameInformation> gameInfos, String gameName) {
        Connection connection = connectionGenerator.getConnection();
        try {
            connection.setAutoCommit(false);
            if (isExistingGameName(gameInfos, gameName)) {
                ChessBoard searchedChessBoard = getSavedChessBoard(gameName, connection);
                connection.commit();
                return searchedChessBoard;
            }
            GameInformation gameInformation = new GameInformation(gameName);
            ChessBoard createdChessBoard = createNewChessBoard(gameInformation, connection);
            connection.commit();
            return createdChessBoard;
        } catch (RuntimeException | SQLException e) {
            rollback(connection);
            throw new DBConnectionException(e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }

    private boolean isExistingGameName(List<GameInformation> gameInfos, String gameName) {
        return gameInfos.stream()
                .map(GameInformation::getGameName)
                .toList()
                .contains(gameName);
    }

    private ChessBoard createNewChessBoard(GameInformation gameInformation, Connection connection) {
        GameInformation createdGameInformation = gameInformationDao.create(gameInformation, connection);
        ChessBoard createdChessBoard = new ChessBoard(createdGameInformation);
        return chessBoardDao.create(createdChessBoard, connection);
    }

    private ChessBoard getSavedChessBoard(String gameName, Connection connection) {
        List<ChessGameComponentDto> dto = chessBoardDao.findByGameName(gameName, connection);
        Map<Position, Piece> chessBoard = new LinkedHashMap<>();
        dto.forEach(e -> chessBoard.put(e.position(), e.piece()));
        GameInformation gameInformation = gameInformationDao.findByGameName(gameName, connection);
        return new ChessBoard(chessBoard, gameInformation);
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DBConnectionException(e.getMessage());
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DBConnectionException(e.getMessage());
        }
    }
}
