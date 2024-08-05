package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Piece;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Generators {

    private final Map<Piece, Generator> pieceToGenerator;

    private Generators(
            KingGenerator kingGenerator,
            RookGenerator rookGenerator,
            BishopGenerator bishopGenerator,
            QueenGenerator queenGenerator,
            KnightGenerator knightGenerator,
            PawnGenerator pawnGenerator
    ) {
        pieceToGenerator = HashMap.newHashMap(13);
        pieceToGenerator.put(Piece.WHITE_KING, kingGenerator);
        pieceToGenerator.put(Piece.WHITE_ROOK, rookGenerator);
        pieceToGenerator.put(Piece.WHITE_BISHOP, bishopGenerator);
        pieceToGenerator.put(Piece.WHITE_QUEEN, queenGenerator);
        pieceToGenerator.put(Piece.WHITE_KNIGHT, knightGenerator);
        pieceToGenerator.put(Piece.WHITE_PAWN, pawnGenerator);
        pieceToGenerator.put(Piece.BLACK_KING, kingGenerator);
        pieceToGenerator.put(Piece.BLACK_ROOK, rookGenerator);
        pieceToGenerator.put(Piece.BLACK_BISHOP, bishopGenerator);
        pieceToGenerator.put(Piece.BLACK_QUEEN, queenGenerator);
        pieceToGenerator.put(Piece.BLACK_KNIGHT, knightGenerator);
        pieceToGenerator.put(Piece.BLACK_PAWN, pawnGenerator);
        pieceToGenerator.put(Piece.EMPTY, (board, from) -> List.of());
    }

    public Generator generateFor(Piece piece) {
        return pieceToGenerator.get(piece);
    }

}
