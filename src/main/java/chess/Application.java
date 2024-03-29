package chess;

import chess.controller.ChessGameController;
import chess.dao.ChessDBService;
import chess.dao.ChessGameDao;
import chess.dao.ConnectionGenerator;
import chess.dao.GameInformationDao;
import chess.view.InputView;
import chess.view.OutputView;

public class Application {
    private static final String fileName = "src/main/java/chess/resource/application.yml";

    public static void main(String[] args) {
        ChessGameController chessGameController = new ChessGameController(new InputView(), new OutputView(),
                new ChessDBService(
                        new GameInformationDao(ConnectionGenerator.from(fileName)),
                        new ChessGameDao(ConnectionGenerator.from(fileName))
                ));
        chessGameController.run();
    }
}
