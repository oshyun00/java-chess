package chess.controller;

import static chess.utils.Constant.MOVE_COMMAND;
import static chess.utils.Constant.STATUS_COMMAND;

import chess.dao.ChessDBService;
import chess.domain.board.ChessBoard;
import chess.domain.board.GameInformation;
import chess.domain.piece.Color;
import chess.domain.position.Position;
import chess.domain.state.End;
import chess.domain.state.GameState;
import chess.domain.state.Ready;
import chess.domain.vo.Score;
import chess.dto.ChessBoardDto;
import chess.dto.CurrentResultDto;
import chess.view.InputView;
import chess.view.OutputView;
import java.util.List;
import java.util.function.Supplier;

public class ChessGameController {
    private final InputView inputView;
    private final OutputView outputView;
    private final ChessDBService chessDBService;

    public ChessGameController(InputView inputView, OutputView outputView, ChessDBService chessDBService) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.chessDBService = chessDBService;
    }

    public void run() {
        ChessBoard chessBoard = prepareChessBoard();
        outputView.printStartMessage(chessBoard.getGameId());

        playGame(chessBoard);
    }

    private void playGame(ChessBoard chessBoard) {
        GameState gameState = new Ready(chessBoard);

        while (!gameState.isEnd()) {
            GameState currentGameState = gameState;
            gameState = repeatUntilSuccess(() -> playEachTurn(currentGameState));

            printChessBoardInProgress(gameState, chessBoard);
        }
        handleKingCapture((End) gameState, chessBoard);
    }

    private ChessBoard prepareChessBoard() {
        List<GameInformation> gameInfos = chessDBService.getAllGameInformation();
        outputView.printGameInformation(gameInfos);

        int gameId = repeatUntilSuccess(() -> inputView.readGameId(gameInfos));
        return chessDBService.findChessBoard(gameId);
    }


    private void handleKingCapture(End gameState, ChessBoard chessBoard) {
        if (gameState.isEndByKingCaptured()) {
            printResultByKingCaptured(chessBoard);
            chessDBService.removeFinishedGame(chessBoard);
        }
    }

    private void printResultByKingCaptured(ChessBoard chessBoard) {
        outputView.printChessBoard(new ChessBoardDto(chessBoard));
        outputView.printResultWithKingCaptured(chessBoard.findWinnerColorByKing());
    }

    private GameState playEachTurn(GameState gameState) {
        List<String> command = inputView.readCommand();
        if (command.get(0).equals(STATUS_COMMAND)) {
            printCurrentScore(gameState);
            return gameState;
        }
        if (command.get(0).equals(MOVE_COMMAND)) {
            List<Position> sourceAndTarget = gameState.convertToSourceAndTarget(command);
            GameState updatedGameState = gameState.play(command);
            chessDBService.updateChessBoard(sourceAndTarget);
            return updatedGameState;
        }
        return gameState.play(command);
    }

    private void printCurrentScore(GameState gameState) {
        Score blackScore = gameState.calculateScore(Color.BLACK);
        Score whiteScore = gameState.calculateScore(Color.WHITE);
        Color winnerColor = gameState.getWinnerColor();
        CurrentResultDto currentResultDto = new CurrentResultDto(blackScore, whiteScore, winnerColor);
        outputView.printEachTeamScore(currentResultDto);
    }

    private void printChessBoardInProgress(GameState gameState, ChessBoard chessBoard) {
        if (!gameState.isEnd()) {
            ChessBoardDto chessBoardDto = new ChessBoardDto(chessBoard);
            outputView.printChessBoard(chessBoardDto);
        }
    }

    private <T> T repeatUntilSuccess(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            outputView.printErrorMessage(e.getMessage());
            return repeatUntilSuccess(supplier);
        }
    }
}
