package chess.view;

import static chess.utils.Constant.END_COMMAND;
import static chess.utils.Constant.MOVE_COMMAND;
import static chess.utils.Constant.START_COMMAND;
import static chess.utils.Constant.STATUS_COMMAND;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputView {
    public static final int MAX_GAME_NAME_LENGTH = 10;
    private static final String MOVE_COMMAND_FORMAT = "^" + MOVE_COMMAND + " [a-h][1-8] [a-h][1-8]$";
    private static final String DELIMITER = " ";
    private final Scanner scanner = new Scanner(System.in);

    public List<String> readCommand() {
        String input = scanner.nextLine();
        if (input.equals(START_COMMAND) || input.equals(END_COMMAND) || input.equals(STATUS_COMMAND)) {
            return List.of(input);
        }
        if (isMoveCommand(input)) {
            return convertMoveCommand(input);
        }
        throw new IllegalArgumentException("잘못된 입력입니다.");
    }

    public String readGameName() {
        String input = scanner.nextLine();
        if (input.isEmpty() || input.isBlank() || input.length() > MAX_GAME_NAME_LENGTH) {
            throw new IllegalArgumentException("게임 이름은 공백일 수 없으며 " + MAX_GAME_NAME_LENGTH + "자 이내이어야합니다.");
        }
        return input;
    }

    private boolean isMoveCommand(String input) {
        return input.matches(MOVE_COMMAND_FORMAT);
    }

    private List<String> convertMoveCommand(String input) {
        return Arrays.asList(input.split(DELIMITER));
    }
}
