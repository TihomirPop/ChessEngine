package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Piece;

public class GeneratorUtils {

    public static boolean isIndexInBounds(int index) {
        return index >= 0 && index < 64;
    }

    public static boolean isPieceOnIndexNotFriendly(Board board, int index) {
        var piece = board.getBoard()
                .get(index);

        if (piece == Piece.EMPTY) {
            return true;
        }

        return board.isWhiteTurn() ?
                Piece.BLACK_PIECES.contains(piece) :
                Piece.WHITE_PIECES.contains(piece);
    }

}
