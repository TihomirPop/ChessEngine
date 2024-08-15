package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CheckGenerator extends SlidingGenerator {

    @Override
    public Set<Move> from(Board board, int from) {
        var moves = generatePawnAttacks(board, from);
        moves.addAll(generateKnightAttacks(board, from));
        moves.addAll(generateKingAttacks(board, from));
        moves.addAll(generateAllSlidingAttacks(board, from));

        return moves;
    }

    private static Set<Move> generateAllSlidingAttacks(Board board, int from) {
        Set<Move> list = new HashSet<>();

        list.addAll(generateSlidingAttacks(board, from, Direction.UP));
        list.addAll(generateSlidingAttacks(board, from, Direction.DOWN));
        list.addAll(generateSlidingAttacks(board, from, Direction.LEFT));
        list.addAll(generateSlidingAttacks(board, from, Direction.RIGHT));
        list.addAll(generateSlidingAttacks(board, from, Direction.UP_LEFT));
        list.addAll(generateSlidingAttacks(board, from, Direction.UP_RIGHT));
        list.addAll(generateSlidingAttacks(board, from, Direction.DOWN_LEFT));
        list.addAll(generateSlidingAttacks(board, from, Direction.DOWN_RIGHT));

        return list;
    }

    private static Set<Move> generateSlidingAttacks(Board board, int from, Direction direction) {
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

            var piece = board.getPiece(to);
            var isRookOrQueen = piece == Piece.WHITE_ROOK || piece == Piece.WHITE_QUEEN || piece == Piece.BLACK_ROOK || piece == Piece.BLACK_QUEEN;
            var isBishopOrQueen = piece == Piece.WHITE_BISHOP || piece == Piece.WHITE_QUEEN || piece == Piece.BLACK_BISHOP || piece == Piece.BLACK_QUEEN;

            if ((directionType != Direction.Type.DIAGONAL && isRookOrQueen) ||
                    (directionType == Direction.Type.DIAGONAL && isBishopOrQueen)) {
                moves.add(new Move(from, to));
                break;
            } else if(!Generator.isPieceOnIndexEmpty(board, to)) {
                break;
            }
        }

        return moves;
    }

    private static Set<Move> generatePawnAttacks(Board board, int from) {
        var direction = board.isWhiteTurn() ? Direction.UP.getOffset() : Direction.DOWN.getOffset();
        var opponent = board.isWhiteTurn() ? Piece.BLACK_PAWN : Piece.WHITE_PAWN;
        var leftAttack = from + direction + Direction.LEFT.getOffset();
        var rightAttack = from + direction + Direction.RIGHT.getOffset();
        Set<Move> moves = new HashSet<>();

        if (Generator.isIndexInBounds(leftAttack) && Board.getColumn(from) != 1 && board.getPiece(leftAttack) == opponent) {
            moves.add(new Move(from, leftAttack));
        }
        if (Generator.isIndexInBounds(rightAttack) && Board.getColumn(from) != 8 && board.getPiece(rightAttack) == opponent) {
            moves.add(new Move(from, rightAttack));
        }

        return moves;
    }

    private static Set<Move> generateKnightAttacks(Board board, int from) {
        Set<Move> list = new HashSet<>();
        for (int to : new int[]{from - 17, from - 15, from - 10, from - 6, from + 6, from + 10, from + 15, from + 17}) {
            if (Generator.isIndexInBounds(to) && Generator.isPieceOnIndexNotFriendly(board, to) && KnightGenerator.isValidKnightMove(from, to) && (board.getPiece(to) == (board.isWhiteTurn() ? Piece.BLACK_KNIGHT : Piece.WHITE_KNIGHT))) {
                list.add(new Move(from, to));
            }
        }
        return list;
    }

    private static Set<Move> generateKingAttacks(Board board, int from) {
        Set<Move> list = new HashSet<>();
        for (Direction direction : Direction.values()) {
            int to = from + direction.getOffset();
            if (Generator.isIndexInBounds(to) && (Math.abs(Board.getColumn(from) - Board.getColumn(to)) <= 1) && (Math.abs(Board.getRow(from) - Board.getRow(to)) <= 1) && (board.getPiece(to) == (board.isWhiteTurn() ? Piece.BLACK_KING : Piece.WHITE_KING))) {
                list.add(new Move(from, to));
            }
        }
        return list;
    }

}
