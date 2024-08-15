package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
class RookGenerator extends SlidingGenerator {

    @Override
    public Set<Move> from(Board board, int from) {
        Set<Move> list = new HashSet<>();

        var moveType = switch (from) {
            case 0 -> board.isBlackQueenSideCastle() ? Move.Type.FIRST_MOVE : Move.Type.NORMAL;
            case 7 -> board.isBlackKingSideCastle() ? Move.Type.FIRST_MOVE : Move.Type.NORMAL;
            case 56 -> board.isWhiteQueenSideCastle() ? Move.Type.FIRST_MOVE : Move.Type.NORMAL;
            case 63 -> board.isWhiteKingSideCastle() ? Move.Type.FIRST_MOVE : Move.Type.NORMAL;
            default -> Move.Type.NORMAL;
        };

        list.addAll(generateSlidingMoves(board, from, Direction.UP, moveType));
        list.addAll(generateSlidingMoves(board, from, Direction.DOWN, moveType));
        list.addAll(generateSlidingMoves(board, from, Direction.LEFT, moveType));
        list.addAll(generateSlidingMoves(board, from, Direction.RIGHT, moveType));

        return list;
    }

}
