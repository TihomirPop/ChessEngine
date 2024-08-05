package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
class BishopGenerator extends SlidingGenerator {

    @Override
    public List<Move> from(Board board, int from) {
        return Stream.of(Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_LEFT, Direction.DOWN_RIGHT)
                .map(direction -> generateSlidingMoves(board, from, direction))
                .flatMap(List::stream)
                .toList();
    }

}
