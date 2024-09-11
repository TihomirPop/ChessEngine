package hr.tvz.popovic.chessengine.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static hr.tvz.popovic.chessengine.evaluation.PrecomputedConstants.*;

@Getter
@RequiredArgsConstructor
public enum Piece {
    EMPTY(0, null),
    WHITE_KING(KING_VALUE, true),
    WHITE_ROOK(ROOK_VALUE, true),
    WHITE_BISHOP(BISHOP_VALUE, true),
    WHITE_QUEEN(QUEEN_VALUE, true),
    WHITE_KNIGHT(KNIGHT_VALUE, true),
    WHITE_PAWN(PAWN_VALUE, true),
    BLACK_KING(KING_VALUE, false),
    BLACK_ROOK(ROOK_VALUE, false),
    BLACK_BISHOP(BISHOP_VALUE, false),
    BLACK_QUEEN(QUEEN_VALUE, false),
    BLACK_KNIGHT(KNIGHT_VALUE, false),
    BLACK_PAWN(PAWN_VALUE, false);

    private final int value;
    private final Boolean isWhite;

    private static final Map<Piece, Character> PIECE_TO_FEN = new HashMap<>();
    private static final Map<Character, Piece> FEN_TO_PIECE = new HashMap<>();
    public static final Set<Piece> WHITE_PIECES = Set.of(
            WHITE_KING,
            WHITE_ROOK,
            WHITE_BISHOP,
            WHITE_QUEEN,
            WHITE_KNIGHT,
            WHITE_PAWN
    );
    public static final Set<Piece> BLACK_PIECES = Set.of(
            BLACK_KING,
            BLACK_ROOK,
            BLACK_BISHOP,
            BLACK_QUEEN,
            BLACK_KNIGHT,
            BLACK_PAWN
    );

    static {
        PIECE_TO_FEN.put(WHITE_KING, 'K');
        PIECE_TO_FEN.put(WHITE_ROOK, 'R');
        PIECE_TO_FEN.put(WHITE_BISHOP, 'B');
        PIECE_TO_FEN.put(WHITE_QUEEN, 'Q');
        PIECE_TO_FEN.put(WHITE_KNIGHT, 'N');
        PIECE_TO_FEN.put(WHITE_PAWN, 'P');
        PIECE_TO_FEN.put(BLACK_KING, 'k');
        PIECE_TO_FEN.put(BLACK_ROOK, 'r');
        PIECE_TO_FEN.put(BLACK_BISHOP, 'b');
        PIECE_TO_FEN.put(BLACK_QUEEN, 'q');
        PIECE_TO_FEN.put(BLACK_KNIGHT, 'n');
        PIECE_TO_FEN.put(BLACK_PAWN, 'p');
        PIECE_TO_FEN.put(EMPTY, '_');
        PIECE_TO_FEN.forEach((piece, c) -> FEN_TO_PIECE.put(c, piece));
    }

    public Character toFen() {
        return PIECE_TO_FEN.get(this);
    }

    public static Piece fromFen(Character c) {
        return FEN_TO_PIECE.get(c);
    }

}
