package hr.tvz.popovic.chessengine.model;

import lombok.*;

import java.util.Arrays;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Board {

    private final Piece[] board = new Piece[64];
    private boolean isWhiteTurn = true;
    private boolean isWhiteKingSideCastle = true;
    private boolean isWhiteQueenSideCastle = true;
    private boolean isBlackKingSideCastle = true;
    private boolean isBlackQueenSideCastle = true;
    private int enPassantSquare = -1;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;
    private int whiteKingIndex;
    private int blackKingIndex;

    public static Board createInitialBoard() {
        Board board = new Board();
        board.initializeBoard();
        return board;
    }

    public Board createCopy() {
        Board copy = new Board();
        System.arraycopy(board, 0, copy.board, 0, board.length);
        copy.isWhiteTurn = isWhiteTurn;
        copy.isWhiteKingSideCastle = isWhiteKingSideCastle;
        copy.isWhiteQueenSideCastle = isWhiteQueenSideCastle;
        copy.isBlackKingSideCastle = isBlackKingSideCastle;
        copy.isBlackQueenSideCastle = isBlackQueenSideCastle;
        copy.enPassantSquare = enPassantSquare;
        copy.halfMoveClock = halfMoveClock;
        copy.fullMoveNumber = fullMoveNumber;
        copy.whiteKingIndex = whiteKingIndex;
        copy.blackKingIndex = blackKingIndex;
        return copy;
    }

    public static Board createEmptyBoard() {
        Board emptyBoard = new Board();
        Arrays.fill(emptyBoard.board, Piece.EMPTY);
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
                Piece.WHITE_PIECES.contains(board[index]) :
                Piece.BLACK_PIECES.contains(board[index]);
    }

    public void makeMove(Move move) {
        if(enPassantSquare != -1) {
            enPassantSquare = -1;
        }

        switch (move.type()) {
            case CASTLING -> makeCastlingMove(move);
            case DOUBLE_PAWN_PUSH -> makeDoublePawnPush(move);
            case EN_PASSANT -> makeEnPassantMove(move);
            case QUEEN_PROMOTION -> makePromotionMove(move, isWhiteTurn ? Piece.WHITE_QUEEN : Piece.BLACK_QUEEN);
            case ROOK_PROMOTION -> makePromotionMove(move, isWhiteTurn ? Piece.WHITE_ROOK : Piece.BLACK_ROOK);
            case BISHOP_PROMOTION -> makePromotionMove(move, isWhiteTurn ? Piece.WHITE_BISHOP : Piece.BLACK_BISHOP);
            case KNIGHT_PROMOTION -> makePromotionMove(move, isWhiteTurn ? Piece.WHITE_KNIGHT : Piece.BLACK_KNIGHT);
            case FIRST_MOVE -> makeFirstMove(move);
            default -> makeRegularMove(move);
        }
        var to = move.to();
        board[move.from()] = Piece.EMPTY;
        if (board[to] == Piece.WHITE_KING) {
            whiteKingIndex = to;
        } else if (board[to] == Piece.BLACK_KING) {
            blackKingIndex = to;
        }
        setWhiteTurn(!isWhiteTurn);
    }

    public Piece getPiece(int index) {
        return board[index];
    }

    public void setPiece(int index, Piece piece) {
        board[index] = piece;
    }

    public int size() {
        return board.length;
    }

    public int getKingIndex() {
        return isWhiteTurn ? whiteKingIndex : blackKingIndex;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < 64; i++) {
            sb.append(board[i].toFen()).append(" ");
            if (i % 8 == 7) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private void initializeBoard() {
        addPieces(
                0,
                Piece.BLACK_ROOK,
                Piece.BLACK_KNIGHT,
                Piece.BLACK_BISHOP,
                Piece.BLACK_QUEEN,
                Piece.BLACK_KING,
                Piece.BLACK_BISHOP,
                Piece.BLACK_KNIGHT,
                Piece.BLACK_ROOK
        );
        addInitialPawns(8, Piece.BLACK_PAWN);
        addInitialEmptySpaces(16);
        addInitialPawns(48, Piece.WHITE_PAWN);
        addPieces(
                56,
                Piece.WHITE_ROOK,
                Piece.WHITE_KNIGHT,
                Piece.WHITE_BISHOP,
                Piece.WHITE_QUEEN,
                Piece.WHITE_KING,
                Piece.WHITE_BISHOP,
                Piece.WHITE_KNIGHT,
                Piece.WHITE_ROOK
        );
        whiteKingIndex = 60;
        blackKingIndex = 4;
    }

    private void addPieces(int startingIndex, Piece... pieces) {
        System.arraycopy(pieces, 0, board, startingIndex, pieces.length);
    }

    private void addInitialPawns(int startingIndex, Piece pawn) {
        for (int i = 0; i < 8; i++) {
            board[startingIndex + i] = pawn;
        }
    }

    private void addInitialEmptySpaces(int startingIndex) {
        for (int i = 0; i < 32; i++) {
            board[startingIndex + i] = Piece.EMPTY;
        }
    }

    private void makeRegularMove(Move move) {
        board[move.to()] = board[move.from()];
    }

    private void makePromotionMove(Move move, Piece piece) {
        board[move.to()] = piece;
    }

    private void makeEnPassantMove(Move move) {
        var captureIndex = isWhiteTurn ?
                move.to() + Direction.DOWN.getOffset() :
                move.to() + Direction.UP.getOffset();
        makeRegularMove(move);
        board[captureIndex] = Piece.EMPTY;
    }

    private void makeDoublePawnPush(Move move) {
        makeRegularMove(move);
        enPassantSquare = isWhiteTurn ?
                move.from() + Direction.UP.getOffset() :
                move.from() + Direction.DOWN.getOffset();
    }

    private void makeCastlingMove(Move move) {
        makeRegularMove(move);
        switch (move.to()) {
            case 2 -> {
                board[3] = Piece.BLACK_ROOK;
                board[0] = Piece.EMPTY;
                isBlackKingSideCastle = false;
                isBlackQueenSideCastle = false;
            }
            case 6 -> {
                board[5] = Piece.BLACK_ROOK;
                board[7] = Piece.EMPTY;
                isBlackKingSideCastle = false;
                isBlackQueenSideCastle = false;
            }
            case 58 -> {
                board[59] = Piece.WHITE_ROOK;
                board[56] =Piece.EMPTY;
                isWhiteKingSideCastle = false;
                isWhiteQueenSideCastle = false;
            }
            case 62 -> {
                board[61] = Piece.WHITE_ROOK;
                board[63] = Piece.EMPTY;
                isWhiteKingSideCastle = false;
                isWhiteQueenSideCastle = false;
            }
        }
    }

    private void makeFirstMove(Move move) {
        makeRegularMove(move);
        switch (board[move.from()]) {
            case WHITE_KING -> {
                isWhiteKingSideCastle = false;
                isWhiteQueenSideCastle = false;
            }
            case BLACK_KING -> {
                isBlackKingSideCastle = false;
                isBlackQueenSideCastle = false;
            }
            case WHITE_ROOK -> {
                if (move.from() == 56) {
                    isWhiteQueenSideCastle = false;
                } else if (move.from() == 63) {
                    isWhiteKingSideCastle = false;
                }
            }
            case BLACK_ROOK -> {
                if (move.from() == 0) {
                    isBlackQueenSideCastle = false;
                } else if (move.from() == 7) {
                    isBlackKingSideCastle = false;
                }
            }
        }
    }

}
