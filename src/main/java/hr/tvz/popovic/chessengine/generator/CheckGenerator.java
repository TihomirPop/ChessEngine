package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CheckGenerator extends SlidingGenerator {

    @Override
    public List<Move> from(Board board, int from) {
        return List.of();
    }

    private static List<Move> generateSlidingAttacks(Board board, int from, Direction direction) {
        var offset = direction.getOffset();
        var directionType = direction.getType();
        var to = from;
        List<Move> moves = new ArrayList<>();

        while (true) {
            to += offset;

            if (!Generator.isIndexInBounds(to) ||
                    (directionType == Direction.Type.HORIZONTAL && wentToNewRow(from, to)) ||
                    (directionType == Direction.Type.DIAGONAL && didNotGoDiagonally(from, to)) ||
                    Generator.isPieceOnIndexFriendly(board, to)) {
                break;
            }

            var piece = board.getPiece(to);
            var isRookOrQueen = piece == Piece.WHITE_ROOK || piece == Piece.WHITE_QUEEN || piece == Piece.BLACK_ROOK || piece == Piece.BLACK_QUEEN;
            var isBishopOrQueen = piece == Piece.WHITE_BISHOP || piece == Piece.WHITE_QUEEN || piece == Piece.BLACK_BISHOP || piece == Piece.BLACK_QUEEN;

            if ((directionType != Direction.Type.DIAGONAL && isRookOrQueen) ||
                    (directionType == Direction.Type.DIAGONAL && isBishopOrQueen)) {
                moves.add(new Move(from, to));
                break;
            }
        }

        return moves;
    }

    private static List<Move> generatePawnAttacks(Board board, int from) {
        var direction = board.isWhiteTurn() ? Direction.UP.getOffset() : Direction.DOWN.getOffset();
        var opponent = board.isWhiteTurn() ? Piece.BLACK_PAWN : Piece.WHITE_PAWN;
        var leftAttack = from + direction + Direction.LEFT.getOffset();
        var rightAttack = from + direction + Direction.RIGHT.getOffset();
        List<Move> moves = new ArrayList<>();

        if (Generator.isIndexInBounds(leftAttack) && Board.getColumn(from) != 1 && board.getPiece(leftAttack) == opponent) {
            moves.add(new Move(from, leftAttack));
        }
        if (Generator.isIndexInBounds(rightAttack) &&  Board.getColumn(from) != 8 && board.getPiece(rightAttack) == opponent) {
            moves.add(new Move(from, rightAttack));
        }

        return moves;
    }

}
