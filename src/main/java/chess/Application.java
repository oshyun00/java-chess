package chess;

import chess.controller.ChessGameController;
import chess.dao.ChessBoardDao;
import chess.dao.GameInformationDao;
import chess.service.ChessService;
import chess.service.ConnectionGenerator;
import chess.view.InputView;
import chess.view.OutputView;

public class Application {
    private static final String CONFIGURATION_FILE_NAME = "src/main/java/chess/resource/application.yml";

    public static void main(String[] args) {
        ChessGameController chessGameController = new ChessGameController(new InputView(), new OutputView(),
                new ChessService(
                        new GameInformationDao(),
                        new ChessBoardDao(),
                        ConnectionGenerator.from(CONFIGURATION_FILE_NAME)
                ));
        chessGameController.run();
    }
}
