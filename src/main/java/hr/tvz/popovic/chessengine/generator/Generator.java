package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Piece;

public abstract class Generator {

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
                Piece.isBlackPiece(piece) :
                Piece.isWhitePiece(piece);
    }

    static boolean isPieceOnIndexOpponent(Board board, int index) {
        var piece = board.getPiece(index);

        return board.isWhiteTurn() ?
                Piece.isBlackPiece(piece) :
                Piece.isWhitePiece(piece);
    }

    static boolean isPieceOnIndexFriendly(Board board, int index) {
        var piece = board.getPiece(index);

        return board.isWhiteTurn() ?
                Piece.isWhitePiece(piece) :
                Piece.isBlackPiece(piece);
    }


}
