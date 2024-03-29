package chess.dao;

import chess.domain.board.ChessBoard;
import chess.domain.board.GameInformation;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.ChessGameComponentDto;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChessDBService {
    private static final int NEW_GAME_COMMAND = 0;

    private final GameInformationDao gameInformationDao;
    private final ChessGameDao chessGameDao;

    public ChessDBService(GameInformationDao gameInformationDao, ChessGameDao chessGameDao) {
        this.gameInformationDao = gameInformationDao;
        this.chessGameDao = chessGameDao;
    }

    public List<GameInformation> getAllGameInformation() {
        return gameInformationDao.findAll();
    }

    public void removeFinishedGame(ChessBoard chessBoard) {
        gameInformationDao.remove(chessBoard.getGameId());
    }

    public void updateChessBoard(List<Position> movedPath, GameInformation gameInformation) {
        chessGameDao.update(movedPath.get(0), movedPath.get(1));
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
        chessGameDao.saveChessBoard(createdChessBoard);
        return createdChessBoard;
    }

    private ChessBoard getSavedChessBoard(int gameId) {
        List<ChessGameComponentDto> dto = chessGameDao.findById(gameId);
        Map<Position, Piece> chessBoard = new LinkedHashMap<>();
        dto.forEach(e -> chessBoard.put(e.position(), e.piece()));
        GameInformation gameInformation = gameInformationDao.findByGameId(gameId);
        return new ChessBoard(chessBoard, gameInformation);
    }
}
