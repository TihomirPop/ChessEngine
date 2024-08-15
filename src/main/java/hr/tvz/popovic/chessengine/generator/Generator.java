package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;

import java.util.Set;

public abstract class Generator {

    public abstract Set<Move> from(Board board, int from);

    static boolean isIndexInBounds(int index) {
        return index >= 0 && index < 64;
    }

    static boolean isPieceOnIndexEmpty(Board board, int index) {
        return board.getPiece(index) == Piece.EMPTY;
    }

    static boolean isPieceOnIndexNotFriendly(Board board, int index) {
        var piece = board.getPiece(index);

        if (piece == Piece.EMPTY) {
            return true;
        }

        return board.isWhiteTurn() ?
                Piece.BLACK_PIECES.contains(piece) :
                Piece.WHITE_PIECES.contains(piece);
    }

    static boolean isPieceOnIndexOpponent(Board board, int index) {
        var piece = board.getPiece(index);

        return board.isWhiteTurn() ?
                Piece.BLACK_PIECES.contains(piece) :
                Piece.WHITE_PIECES.contains(piece);
    }

    static boolean isPieceOnIndexFriendly(Board board, int index) {
        var piece = board.getPiece(index);

        return board.isWhiteTurn() ?
                Piece.WHITE_PIECES.contains(piece) :
                Piece.BLACK_PIECES.contains(piece);
    }


}
