package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class QueenGenerator extends SlidingGenerator {

    @Override
    public List<Move> from(Board board, int from) {
        List<Move> list = new ArrayList<>();

        list.addAll(generateSlidingMoves(board, from, Direction.UP));
        list.addAll(generateSlidingMoves(board, from, Direction.DOWN));
        list.addAll(generateSlidingMoves(board, from, Direction.LEFT));
        list.addAll(generateSlidingMoves(board, from, Direction.RIGHT));
        list.addAll(generateSlidingMoves(board, from, Direction.UP_LEFT));
        list.addAll(generateSlidingMoves(board, from, Direction.UP_RIGHT));
        list.addAll(generateSlidingMoves(board, from, Direction.DOWN_LEFT));
        list.addAll(generateSlidingMoves(board, from, Direction.DOWN_RIGHT));

        return list;
    }

}
