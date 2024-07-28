package hr.tvz.popovic.chessengine.model;

import java.util.HashMap;
import java.util.Map;

public enum Piece {
    EMPTY,
    WHITE_KING,
    WHITE_ROOK,
    WHITE_BISHOP,
    WHITE_QUEEN,
    WHITE_KNIGHT,
    WHITE_PAWN,
    BLACK_KING,
    BLACK_ROOK,
    BLACK_BISHOP,
    BLACK_QUEEN,
    BLACK_KNIGHT,
    BLACK_PAWN;

    private static final Map<Piece, Character> FEN_MAP = new HashMap<>();

    static {
        FEN_MAP.put(EMPTY, ' ');
        FEN_MAP.put(WHITE_KING, 'K');
        FEN_MAP.put(WHITE_ROOK, 'R');
        FEN_MAP.put(WHITE_BISHOP, 'B');
        FEN_MAP.put(WHITE_QUEEN, 'Q');
        FEN_MAP.put(WHITE_KNIGHT, 'N');
        FEN_MAP.put(WHITE_PAWN, 'P');
        FEN_MAP.put(BLACK_KING, 'k');
        FEN_MAP.put(BLACK_ROOK, 'r');
        FEN_MAP.put(BLACK_BISHOP, 'b');
        FEN_MAP.put(BLACK_QUEEN, 'q');
        FEN_MAP.put(BLACK_KNIGHT, 'n');
        FEN_MAP.put(BLACK_PAWN, 'p');
    }

    public static Character toFEN(Piece piece) {
        return FEN_MAP.get(piece);
    }
}
