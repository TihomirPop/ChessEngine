package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class Generators {

    private final Map<Piece, Generator> pieceToGenerator;

    private Generators(
            KingGenerator kingGenerator,
            RookGenerator rookGenerator,
            BishopGenerator bishopGenerator,
            QueenGenerator queenGenerator,
            KnightGenerator knightGenerator,
            PawnGenerator pawnGenerator,
            EmptyGenerator emptyGenerator
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
        pieceToGenerator.put(Piece.EMPTY, emptyGenerator);
    }

    public Generator generateFor(Piece piece) {
        return pieceToGenerator.get(piece);
    }

    public List<Move> generateAllMovesWithoutCheckValidation(Board board) {
        return IntStream.range(0, 64)
                .filter(board::isCurrentPlayerPiece)
                .mapToObj(i -> generateFor(board.getBoard().get(i)).from(board, i))
                .flatMap(List::stream)
                .toList();
    }

}
