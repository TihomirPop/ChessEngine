package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;

import java.util.HashSet;
import java.util.Set;

abstract class SlidingGenerator extends Generator {

    protected static Set<Move> generateSlidingMoves(Board board, int from, Direction direction) {
        var offset = direction.getOffset();
        var directionType = direction.getType();
        var to = from;
        Set<Move> moves = new HashSet<>();

        while (true) {
            to += offset;

            if (!Generator.isIndexInBounds(to) ||
                    (directionType == Direction.Type.HORIZONTAL && wentToNewRow(from, to)) ||
                    (directionType == Direction.Type.DIAGONAL && didNotGoDiagonally(from, to)) ||
                    Generator.isPieceOnIndexFriendly(board, to)) {
                break;
            }

            if (Generator.isPieceOnIndexOpponent(board, to)) {
                moves.add(new Move(from, to));
                break;
            }

            moves.add(new Move(from, to));
        }

        return moves;
    }

    protected static Set<Move> generateSlidingMoves(Board board, int from, Direction direction, Move.Type moveType) {
        var offset = direction.getOffset();
        var directionType = direction.getType();
        var to = from;
        Set<Move> moves = new HashSet<>();

        while (true) {
            to += offset;

            if (!Generator.isIndexInBounds(to) ||
                    (directionType == Direction.Type.HORIZONTAL && wentToNewRow(from, to)) ||
                    (directionType == Direction.Type.DIAGONAL && didNotGoDiagonally(from, to)) ||
                    Generator.isPieceOnIndexFriendly(board, to)) {
                break;
            }

            if (Generator.isPieceOnIndexOpponent(board, to)) {
                moves.add(new Move(from, to, moveType));
                break;
            }

            moves.add(new Move(from, to, moveType));
        }

        return moves;
    }

    protected static boolean wentToNewRow(int from, int to) {
        return Board.getRow(from) != Board.getRow(to);
    }

    protected static boolean didNotGoDiagonally(int from, int to) {
        return Math.abs(Board.getRow(from) - Board.getRow(to)) != Math.abs(Board.getColumn(to) - Board.getColumn(from));
    }

}
