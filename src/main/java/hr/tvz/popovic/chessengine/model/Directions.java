package hr.tvz.popovic.chessengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Directions {
    UP(-8),
    DOWN(8),
    LEFT(-1),
    RIGHT(1),
    UP_LEFT(-9),
    UP_RIGHT(-7),
    DOWN_LEFT(7),
    DOWN_RIGHT(9);

    private final int offset;
}
