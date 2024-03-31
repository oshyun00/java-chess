package chess.domain.board;

import chess.domain.piece.Color;

public class GameInformation {
    private final String gameName;
    private Color curentTurnColor;

    public GameInformation(String gameName, Color curentTurnColor) {
        this.gameName = gameName;
        this.curentTurnColor = curentTurnColor;
    }

    public GameInformation(String gameName) {
        this(gameName, Color.WHITE);
    }

    public String getGameName() {
        return gameName;
    }

    public Color getCurentTurnColor() {
        return curentTurnColor;
    }

    public void convertTurn() {
        curentTurnColor = curentTurnColor.convertTurn();
    }
}
