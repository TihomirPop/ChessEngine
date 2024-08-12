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
        var moveType = switch (from) {
            case 0 -> board.isBlackQueenSideCastle() ? Move.Type.FIRST_MOVE : Move.Type.NORMAL;
            case 7 -> board.isBlackKingSideCastle() ? Move.Type.FIRST_MOVE : Move.Type.NORMAL;
            case 56 -> board.isWhiteQueenSideCastle() ? Move.Type.FIRST_MOVE : Move.Type.NORMAL;
            case 63 -> board.isWhiteKingSideCastle() ? Move.Type.FIRST_MOVE : Move.Type.NORMAL;
            default -> Move.Type.NORMAL;
        };

        return Stream.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
                .map(direction -> generateSlidingMoves(board, from, direction, moveType))
                .flatMap(List::stream)
                .toList();
    }

}
