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

    protected static List<Move> generateSlidingMoves(Board boardState, int from, Direction direction) {
        var offset = direction.getOffset();
        var directionType = direction.getType();
        var to = from;
        List<Move> moves = new ArrayList<>();

        while (true) {
            to += offset;

            if (!Generator.isIndexInBounds(to) ||
                    (directionType == Direction.Type.HORIZONTAL && wentToNewRow(from, to)) ||
                    (directionType == Direction.Type.DIAGONAL && didNotGoDiagonally(from, to)) ||
                    Generator.isPieceOnIndexFriendly(boardState, to)) {
                break;
            }

            var piece = boardState.getPiece(to);
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

}
