package chess;

import chess.controller.ChessGameController;
import chess.dao.ConnectionGenerator;
import chess.dao.GameInformationDao;
import chess.view.InputView;
import chess.view.OutputView;

public class Application {
    public static void main(String[] args) {
        ChessGameController chessGameController = new ChessGameController(
                new InputView(),
                new OutputView(),
                new GameInformationDao(
                        ConnectionGenerator.from("src/main/java/chess/resource/application.yml"))
        );
        chessGameController.run();
    }
}
