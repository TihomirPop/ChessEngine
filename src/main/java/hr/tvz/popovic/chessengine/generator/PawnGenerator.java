package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class PawnGenerator extends Generator {

    @Override
    public List<Move> from(Board board, int from) {
        List<Move> moves = new ArrayList<>();
        var direction = board.isWhiteTurn() ? Direction.UP.getOffset() : Direction.DOWN.getOffset();
        var to = from + direction;

        if (isPieceOnIndexEmpty(board, to) && isIndexInBounds(to)) {
            moves.add(new Move(from, to));
            var doubleMove = to + direction;
            if (isOnStartingRow(board, from) && isPieceOnIndexEmpty(board, doubleMove)) {
                moves.add(new Move(from, doubleMove));
            }
        }

        var leftCapture = to + Direction.LEFT.getOffset();
        var rightCapture = to + Direction.RIGHT.getOffset();

        if (isPieceOnIndexOpponent(board, leftCapture) && Board.getColumn(from) != 1) {
            moves.add(new Move(from, leftCapture));
        }

        if (isPieceOnIndexOpponent(board, rightCapture) && Board.getColumn(from) != 8) {
            moves.add(new Move(from, rightCapture));
        }

        var enPassantSquare = board.getEnPassantSquare();
        if (enPassantSquare != -1 && (enPassantSquare == leftCapture || enPassantSquare == rightCapture)) {
            moves.add(new Move(from, enPassantSquare));
        }

        return moves;
    }

    private boolean isOnStartingRow(Board board, int from) {
        var row = Board.getRow(from);
        return board.isWhiteTurn() ? row == 2 : row == 7;
    }

}
