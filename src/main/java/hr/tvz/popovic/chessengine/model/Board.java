package hr.tvz.popovic.chessengine.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Board {

    private final List<Piece> board = new ArrayList<>(64);
    private boolean isWhiteTurn = true;
    private boolean isWhiteKingSideCastle = true;
    private boolean isWhiteQueenSideCastle = true;
    private boolean isBlackKingSideCastle = true;
    private boolean isBlackQueenSideCastle = true;
    private int enPassantSquare = -1;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;

    public static Board createInitialBoard() {
        Board board = new Board();
        board.initializeBoard();
        return board;
    }

    public Board createCopy() {
        Board copy = new Board();
        copy.board.addAll(board);
        copy.isWhiteTurn = isWhiteTurn;
        copy.isWhiteKingSideCastle = isWhiteKingSideCastle;
        copy.isWhiteQueenSideCastle = isWhiteQueenSideCastle;
        copy.isBlackKingSideCastle = isBlackKingSideCastle;
        copy.isBlackQueenSideCastle = isBlackQueenSideCastle;
        copy.enPassantSquare = enPassantSquare;
        copy.halfMoveClock = halfMoveClock;
        copy.fullMoveNumber = fullMoveNumber;
        return copy;
    }

    public static Board createEmptyBoard() {
        Board emptyBoard = new Board();
        for (int i = 0; i < 64; i++) {
            emptyBoard.board.add(Piece.EMPTY);
        }
        return emptyBoard;
    }

    public static int getRow(int index) {
        return 8 - (index / 8);
    }

    public static int getColumn(int index) {
        return index % 8 + 1;
    }

    public boolean isCurrentPlayerPiece(int index) {
        return isWhiteTurn ?
                Piece.WHITE_PIECES.contains(board.get(index)) :
                Piece.BLACK_PIECES.contains(board.get(index));
    }

    public void makeMove(Move move) {
        board.set(move.to(), board.get(move.from()));
        board.set(move.from(), Piece.EMPTY);
        setWhiteTurn(!isWhiteTurn);
    }

    public Piece getPiece(int index) {
        return board.get(index);
    }

    public void setPiece(int index, Piece piece) {
        board.set(index, piece);
    }

    public int size() {
        return board.size();
    }

    public int getKingIndex() {
        return board.indexOf(isWhiteTurn ? Piece.WHITE_KING : Piece.BLACK_KING);
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

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < 64; i++) {
            sb.append(board.get(i).toFen()).append(" ");
            if (i % 8 == 7) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
