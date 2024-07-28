package hr.tvz.popovic.chessengine.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final ArrayList<Piece> board;

    public Board() {
        board = new ArrayList<>(64);
        initializeBoard();
    }

    private void initializeBoard() {
        addPieces(
                Piece.BLACK_ROOK,
                Piece.BLACK_KNIGHT,
                Piece.BLACK_BISHOP,
                Piece.BLACK_QUEEN,
                Piece.BLACK_KING,
                Piece.BLACK_BISHOP,
                Piece.BLACK_KNIGHT,
                Piece.BLACK_ROOK
        );
        addInitialPawns(Piece.BLACK_PAWN);
        addInitialEmptySpaces();
        addInitialPawns(Piece.WHITE_PAWN);
        addPieces(
                Piece.WHITE_ROOK,
                Piece.WHITE_KNIGHT,
                Piece.WHITE_BISHOP,
                Piece.WHITE_QUEEN,
                Piece.WHITE_KING,
                Piece.WHITE_BISHOP,
                Piece.WHITE_KNIGHT,
                Piece.WHITE_ROOK
        );
    }

    private void addPieces(Piece... pieces) {
        board.addAll(Arrays.asList(pieces));
    }

    private void addInitialPawns(Piece pawn) {
        for (int i = 0; i < 8; i++) {
            board.add(pawn);
        }
    }

    private void addInitialEmptySpaces() {
        for (int i = 0; i < 32; i++) {
            board.add(Piece.EMPTY);
        }
    }

}
