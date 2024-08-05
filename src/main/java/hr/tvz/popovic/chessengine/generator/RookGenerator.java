package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
class RookGenerator extends SlidingGenerator {

    @Override
    public List<Move> from(Board board, int from) {
        return Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
                .map(direction -> generateSlidingMoves(board, from, direction))
                .flatMap(List::stream)
                .toList();
    }

}
