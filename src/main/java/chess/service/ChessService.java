package chess.service;

import chess.dao.ChessBoardDao;
import chess.dao.GameInformationDao;
import chess.domain.board.ChessBoard;
import chess.domain.board.GameInformation;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.ChessGameComponentDto;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChessService {
    private static final int NEW_GAME_COMMAND = 0;
    private static final int SOURCE_POSITION_INDEX = 0;
    private static final int TARGET_POSITION_INDEX = 1;

    private final GameInformationDao gameInformationDao;
    private final ChessBoardDao chessBoardDao;

    public ChessService(GameInformationDao gameInformationDao, ChessBoardDao chessBoardDao) {
        this.gameInformationDao = gameInformationDao;
        this.chessBoardDao = chessBoardDao;
    }

    public List<GameInformation> getAllGameInformation() {
        return gameInformationDao.findAll();
    }

    public void removeFinishedGame(ChessBoard chessBoard) {
        gameInformationDao.remove(chessBoard.getGameId());
    }

    public void updateChessBoard(List<Position> movedPath, GameInformation gameInformation) {
        chessBoardDao.update(movedPath.get(SOURCE_POSITION_INDEX), movedPath.get(TARGET_POSITION_INDEX));
        gameInformationDao.updateTurn(gameInformation);
    }

    public ChessBoard findChessBoard(int gameId) {
        if (gameId == NEW_GAME_COMMAND) {
            return createNewChessBoard();
        }
        return getSavedChessBoard(gameId);
    }

    private ChessBoard createNewChessBoard() {
        gameInformationDao.create();
        GameInformation gameInformation = gameInformationDao.findLatestGame();
        ChessBoard createdChessBoard = new ChessBoard(gameInformation);
        chessBoardDao.saveChessBoard(createdChessBoard);
        return createdChessBoard;
    }

    private ChessBoard getSavedChessBoard(int gameId) {
        List<ChessGameComponentDto> dto = chessBoardDao.findById(gameId);
        Map<Position, Piece> chessBoard = new LinkedHashMap<>();
        dto.forEach(e -> chessBoard.put(e.position(), e.piece()));
        GameInformation gameInformation = gameInformationDao.findByGameId(gameId);
        return new ChessBoard(chessBoard, gameInformation);
    }
}
