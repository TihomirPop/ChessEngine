package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class PawnGenerator extends Generator {

    public static List<Move> from(Board board, int from) {
        List<Move> moves = new ArrayList<>();
        var direction = board.isWhiteTurn() ? Direction.UP.getOffset() : Direction.DOWN.getOffset();
        var to = from + direction;

        if (isPieceOnIndexEmpty(board, to) && isIndexInBounds(to)) {
            generateMoveWithPromotionCheck(moves, from, to);
            var doubleMove = to + direction;
            if (isOnStartingRow(board, from) && isPieceOnIndexEmpty(board, doubleMove)) {
                moves.add(new Move(from, doubleMove, Move.Type.DOUBLE_PAWN_PUSH));
            }
        }

        var leftCapture = to + Direction.LEFT.getOffset();
        var rightCapture = to + Direction.RIGHT.getOffset();

        if (Board.getColumn(from) != 1 && isPieceOnIndexOpponent(board, leftCapture)) {
            generateMoveWithPromotionCheck(moves, from, leftCapture);
        }

        if (Board.getColumn(from) != 8 && isPieceOnIndexOpponent(board, rightCapture)) {
            generateMoveWithPromotionCheck(moves, from, rightCapture);
        }

        var enPassantSquare = board.getEnPassantSquare();
        if (enPassantSquare != -1) {
            if ((Board.getColumn(from) != 1 && enPassantSquare == leftCapture) ||
                    (Board.getColumn(from) != 8 && enPassantSquare == rightCapture)) {
                moves.add(new Move(from, enPassantSquare, Move.Type.EN_PASSANT));
            }
        }

        return moves;
    }

    private static void generateMoveWithPromotionCheck(List<Move> moves, int from, int to) {
        if (Board.getRow(to) == 8 || Board.getRow(to) == 1) {
            moves.add(new Move(from, to, Move.Type.QUEEN_PROMOTION));
            moves.add(new Move(from, to, Move.Type.ROOK_PROMOTION));
            moves.add(new Move(from, to, Move.Type.BISHOP_PROMOTION));
            moves.add(new Move(from, to, Move.Type.KNIGHT_PROMOTION));
        } else {
            moves.add(new Move(from, to));
        }
    }

    private static boolean isOnStartingRow(Board board, int from) {
        var row = Board.getRow(from);
        return board.isWhiteTurn() ? row == 2 : row == 7;
    }

}
