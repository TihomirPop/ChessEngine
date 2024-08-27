package hr.tvz.popovic.chessengine.model;

import hr.tvz.popovic.chessengine.helper.PrecomputedConstants;
import lombok.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static hr.tvz.popovic.chessengine.helper.PrecomputedConstants.*;


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
    @EqualsAndHashCode.Exclude
    private int whiteKingIndex;
    @EqualsAndHashCode.Exclude
    private int blackKingIndex;
    @EqualsAndHashCode.Exclude
    private int numberOfBlackPieces;
    @EqualsAndHashCode.Exclude
    private int numberOfWhitePieces;
    @EqualsAndHashCode.Exclude
    private Map<Integer, Byte> repetitionMap = new HashMap<>();
    @EqualsAndHashCode.Exclude
    private boolean isStalemate = false;

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
        copy.numberOfBlackPieces = numberOfBlackPieces;
        copy.numberOfWhitePieces = numberOfWhitePieces;
        copy.repetitionMap = new HashMap<>(repetitionMap);

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

    public void makeMove(Move move) {
        makeMoveWithEvalGuess(move, false);
    }

    public int makeMoveWithEvalGuess(Move move, boolean isEvalGuess) {
        var to = move.to();
        var from = move.from();
        var capturedPiece = board[to];
        var movePiece = board[from];

        if (enPassantSquare != -1) {
            enPassantSquare = -1;
        }

        if (capturedPiece != Piece.EMPTY) {
            if (capturedPiece.getIsWhite()) {
                numberOfWhitePieces--;
            } else {
                numberOfBlackPieces--;
            }
        }

        var score = switch (move.type()) {
            case CASTLING -> makeCastlingMove(move);
            case DOUBLE_PAWN_PUSH -> makeDoublePawnPush(move);
            case EN_PASSANT -> makeEnPassantMove(move, isEvalGuess);
            case QUEEN_PROMOTION ->
                    makePromotionMove(move, isWhiteTurn ? Piece.WHITE_QUEEN : Piece.BLACK_QUEEN, isEvalGuess);
            case ROOK_PROMOTION ->
                    makePromotionMove(move, isWhiteTurn ? Piece.WHITE_ROOK : Piece.BLACK_ROOK, isEvalGuess);
            case BISHOP_PROMOTION ->
                    makePromotionMove(move, isWhiteTurn ? Piece.WHITE_BISHOP : Piece.BLACK_BISHOP, isEvalGuess);
            case KNIGHT_PROMOTION ->
                    makePromotionMove(move, isWhiteTurn ? Piece.WHITE_KNIGHT : Piece.BLACK_KNIGHT, isEvalGuess);
            case FIRST_MOVE -> makeFirstMove(move, isEvalGuess);
            default -> makeRegularMove(move, isEvalGuess);
        };

        board[from] = Piece.EMPTY;
        if (board[to] == Piece.WHITE_KING) {
            whiteKingIndex = to;
        } else if (board[to] == Piece.BLACK_KING) {
            blackKingIndex = to;
        }
        if (!isWhiteTurn) {
            fullMoveNumber++;
        }
        setWhiteTurn(!isWhiteTurn);

        var hash = Arrays.hashCode(board);
        var repetitionCount = repetitionMap.getOrDefault(hash, (byte) 0);
        repetitionMap.put(hash, ++repetitionCount);

        if (movePiece == Piece.WHITE_PAWN || movePiece == Piece.BLACK_PAWN || capturedPiece != Piece.EMPTY) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
            if (halfMoveClock >= 100) {
                isStalemate = true;
                return 0;
            }
        }

//        if (fullMoveNumber >= 50) {
//            isStalemate = true;
//            return 0;
//        }

        return score;
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

    public int getStaticEvaluation() {
        var score = 0;
        score += getMaterialScore();
        score += getOpponentKingAttackScore();

        return score;
    }

    public int getStaticEvaluation2() {
        var score = 0;
        score += getMaterialScore2();
        score += getOpponentKingAttackScore2();

        return score;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < 64; i++) {
            sb.append(board[i].toFen())
                    .append(" ");
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

    private int makeRegularMove(Move move, boolean isEvalGuess) {
        var capturedPiece = board[move.to()];
        var movePiece = board[move.from()];

        board[move.to()] = board[move.from()];

        if (!isEvalGuess) {
            return 0;
        }

        if (capturedPiece == Piece.EMPTY) {
            return 0;
        }
        return Math.abs(10 * capturedPiece.getValue() - movePiece.getValue());
    }

    private int makePromotionMove(Move move, Piece piece, boolean isEvalGuess) {
        var capturedPiece = board[move.to()];
        var movePiece = board[move.from()];

        board[move.to()] = piece;

        if (!isEvalGuess) {
            return 0;
        }

        if (capturedPiece == Piece.EMPTY) {
            return piece.getValue();
        }

        return Math.abs(10 * capturedPiece.getValue() - movePiece.getValue() + piece.getValue());
    }

    private int makeEnPassantMove(Move move, boolean isEvalGuess) {
        var captureIndex = isWhiteTurn ?
                move.to() + Direction.DOWN.getOffset() :
                move.to() + Direction.UP.getOffset();
        makeRegularMove(move, isEvalGuess);
        board[captureIndex] = Piece.EMPTY;
        return 900;
    }

    private int makeDoublePawnPush(Move move) {
        makeRegularMove(move, false);
        enPassantSquare = isWhiteTurn ?
                move.from() + Direction.UP.getOffset() :
                move.from() + Direction.DOWN.getOffset();
        return 50;
    }

    private int makeCastlingMove(Move move) {
        makeRegularMove(move, false);
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
                board[56] = Piece.EMPTY;
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

        return 1000;
    }

    private int makeFirstMove(Move move, boolean isEvalGuess) {
        var score = makeRegularMove(move, isEvalGuess);
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
        return score;
    }

    private int getMaterialScore() {
        var score = 0;
        for (int i = 0; i < 64; i++) {
            var piece = board[i];
            if (piece == Piece.EMPTY) {
                continue;
            }
            score += piece.getValue() * (piece.getIsWhite() ? 1 : -1);
        }
        return score;
    }

    private int getOpponentKingAttackScore() {
        int opponentKingIndex = isWhiteTurn ? blackKingIndex : whiteKingIndex;
        int numberOfOpponentPieces = isWhiteTurn ? numberOfBlackPieces : numberOfWhitePieces;
        int numberOfFriendlyPieces = isWhiteTurn ? numberOfWhitePieces : numberOfBlackPieces;

        if (numberOfFriendlyPieces <= numberOfOpponentPieces + 1) {
            return 0;
        }

        int distanceBetweenKings = PrecomputedConstants.MANHATTAN_DISTANCE[whiteKingIndex][blackKingIndex];
        int distanceOfEnemyKingFromCenter = PrecomputedConstants.CENTER_MANHATTAN_DISTANCE[opponentKingIndex];

        int score = (int) ((1 - (numberOfOpponentPieces) / 16f) * (40 * (14 - distanceBetweenKings)) + (100 * distanceOfEnemyKingFromCenter));
        return isWhiteTurn ? score : -score;
    }

    private int getMaterialScore2() {
        var score = 0;
        var numberOfOpponentPieces = isWhiteTurn ? numberOfBlackPieces : numberOfWhitePieces;
        var endgameWeight = (1.001 - numberOfOpponentPieces / 16f);

        for (int i = 0; i < 64; i++) {
            var piece = board[i];
            if (piece == Piece.EMPTY) {
                continue;
            }
            score += switch (piece) {
                case WHITE_PAWN -> endgameInterpolation(WHITE_PAWN_EVAL[i], WHITE_PAWN_EVAL_END[i], endgameWeight);
                case BLACK_PAWN -> -endgameInterpolation(BLACK_PAWN_EVAL[i], BLACK_PAWN_EVAL_END[i], endgameWeight);
                case WHITE_KNIGHT -> KNIGHT_EVAL[i];
                case BLACK_KNIGHT -> -KNIGHT_EVAL[i];
                case WHITE_BISHOP -> WHITE_BISHOP_EVAL[i];
                case BLACK_BISHOP -> -BLACK_BISHOP_EVAL[i];
                case WHITE_ROOK -> WHITE_ROOK_EVAL[i];
                case BLACK_ROOK -> -BLACK_ROOK_EVAL[i];
                case WHITE_QUEEN -> WHITE_QUEEN_EVAL[i];
                case BLACK_QUEEN -> -BLACK_QUEEN_EVAL[i];
                case WHITE_KING -> endgameInterpolation(WHITE_KING_EVAL[i], WHITE_KING_EVAL_END[i], endgameWeight);
                case BLACK_KING -> -endgameInterpolation(BLACK_KING_EVAL[i], BLACK_KING_EVAL_END[i], endgameWeight);
                default -> 0;
            };
        }
        return score;
    }

    private int getOpponentKingAttackScore2() {
        int opponentKingIndex = isWhiteTurn ? blackKingIndex : whiteKingIndex;
        int numberOfOpponentPieces = isWhiteTurn ? numberOfBlackPieces : numberOfWhitePieces;
        int numberOfFriendlyPieces = isWhiteTurn ? numberOfWhitePieces : numberOfBlackPieces;

        if (numberOfFriendlyPieces <= numberOfOpponentPieces + 1) {
            return 0;
        }

        int distanceBetweenKings = PrecomputedConstants.MANHATTAN_DISTANCE[whiteKingIndex][blackKingIndex];
        int distanceOfEnemyKingFromCenter = PrecomputedConstants.CENTER_MANHATTAN_DISTANCE[opponentKingIndex];

        int score = (int) ((1 - numberOfOpponentPieces / 16f) * (8 * (14 - distanceBetweenKings) + 15 * distanceOfEnemyKingFromCenter));

        return isWhiteTurn ? score : -score;
    }

    private int endgameInterpolation(int initialScore, int endgameScore, double endgameWeight) {
        return (int) (initialScore * (1 - endgameWeight) + endgameScore * endgameWeight);
    }

}
