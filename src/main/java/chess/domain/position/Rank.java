package chess.domain.position;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Rank {
    EIGHT(8),
    SEVEN(7),
    SIX(6),
    FIVE(5),
    FOUR(4),
    THREE(3),
    TWO(2),
    ONE(1);

    private final int rank;

    Rank(int rank) {
        this.rank = rank;
    }

    public int calculateDifference(Rank otherRank) {
        return otherRank.rank - this.rank;
    }

    public Rank move(int moveUnit) {
        int destination = rank + moveUnit;
        return Arrays.stream(Rank.values()).filter(m -> m.rank == destination)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("올바르지 않은 rank 값입니다."));
    }
}
