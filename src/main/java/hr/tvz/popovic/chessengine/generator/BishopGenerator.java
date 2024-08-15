package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
class BishopGenerator extends SlidingGenerator {

    @Override
    public Set<Move> from(Board board, int from) {
        Set<Move> list = new HashSet<>();

        list.addAll(generateSlidingMoves(board, from, Direction.UP_LEFT));
        list.addAll(generateSlidingMoves(board, from, Direction.UP_RIGHT));
        list.addAll(generateSlidingMoves(board, from, Direction.DOWN_LEFT));
        list.addAll(generateSlidingMoves(board, from, Direction.DOWN_RIGHT));

        return list;
    }

}
