package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BishopGenerator extends SlidingGenerator {

    public static List<Move> from(Board board, int from) {
        List<Move> list = new ArrayList<>();

        list.addAll(generateSlidingMoves(board, from, Direction.UP_LEFT));
        list.addAll(generateSlidingMoves(board, from, Direction.UP_RIGHT));
        list.addAll(generateSlidingMoves(board, from, Direction.DOWN_LEFT));
        list.addAll(generateSlidingMoves(board, from, Direction.DOWN_RIGHT));

        return list;
    }

}
