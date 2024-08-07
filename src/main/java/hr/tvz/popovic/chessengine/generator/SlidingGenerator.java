package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;

import java.util.ArrayList;
import java.util.List;

abstract class SlidingGenerator extends Generator {

    static List<Move> generateSlidingMoves(Board boardState, int from, Direction direction) {
        var offset = direction.getOffset();
        var directionType = direction.getType();
        var to = from;
        List<Move> moves = new ArrayList<>();

        while (true) {
            to += offset;
            if (directionType == Direction.Type.VERTICAL && !Generator.isIndexInBounds(to)) {
                break;
            }

            if (directionType == Direction.Type.HORIZONTAL && wentToNewRow(from, to)) {
                break;
            }

            if (directionType == Direction.Type.DIAGONAL && (didNotGoDiagonally(from, to) || !Generator.isIndexInBounds(to))) {
                break;
            }

            if (Generator.isPieceOnIndexFriendly(boardState, to)) {
                break;
            }

            if (Generator.isPieceOnIndexOpponent(boardState, to)) {
                moves.add(new Move(from, to));
                break;
            }

            moves.add(new Move(from, to));
        }

        return moves;
    }

    private static boolean wentToNewRow(int from, int to) {
        return Board.getRow(from) != Board.getRow(to);
    }

    private static boolean didNotGoDiagonally(int from, int to) {
        return Math.abs(Board.getRow(from) - Board.getRow(to)) != Math.abs(Board.getColumn(to) - Board.getColumn(from));
    }

}
