package hr.tvz.popovic.chessengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Direction {
    UP(-8, Type.VERTICAL),
    DOWN(8, Type.VERTICAL),
    LEFT(-1, Type.HORIZONTAL),
    RIGHT(1 , Type.HORIZONTAL),
    UP_LEFT(-9, Type.DIAGONAL),
    UP_RIGHT(-7, Type.DIAGONAL),
    DOWN_LEFT(7, Type.DIAGONAL),
    DOWN_RIGHT(9, Type.DIAGONAL);

    private final int offset;
    private final Type type;

    public enum Type {
        VERTICAL,
        HORIZONTAL,
        DIAGONAL
    }
}
