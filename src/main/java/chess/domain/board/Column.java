package chess.domain.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Column {
    A(0, "a"),
    B(1, "b"),
    C(2, "c"),
    D(3, "d"),
    E(4, "e"),
    F(5, "f"),
    G(6, "g"),
    H(7, "h"),
    ;

    private final int number;
    private final String name;

    Column(final int number, String name) {
        this.number = number;
        this.name = name;
    }

    public static Column from(String rawColumn) {
        return Arrays.stream(values())
            .filter(column -> column.name.equals(rawColumn))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 열입니다."));
    }

    private static Column from(int value) {
        return Arrays.stream(values())
            .filter(column -> column.number == value)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 열입니다."));
    }

    public Column flip() {
        return Arrays.stream(Column.values())
            .filter(it -> it.number == (7 - number))
            .findFirst()
            .orElseThrow();
    }

    public int distance(Column otherColumn) {
        return Math.abs(number - otherColumn.number);
    }

    public List<Column> pathTo(Column otherColumn) {
        if (number < otherColumn.number) {
            return upPathTo(otherColumn);
        }
        return downPathTo(otherColumn);
    }

    private List<Column> upPathTo(Column otherColumn) {
        List<Column> path = new ArrayList<>();
        for (int i = number + 1; i < otherColumn.number; i++) {
            path.add(Column.from(i));
        }
        return path;
    }

    private List<Column> downPathTo(Column otherColumn) {
        List<Column> path = new ArrayList<>();
        for (int i = number - 1; i > otherColumn.number; i--) {
            path.add(Column.from(i));
        }
        return path;
    }
}