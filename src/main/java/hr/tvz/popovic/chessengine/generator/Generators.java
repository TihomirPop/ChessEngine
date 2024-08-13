package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Generators {

    private final Map<Piece, Generator> pieceToGenerator;
    private final CheckGenerator checkGenerator;

    private Generators(
            KingGenerator kingGenerator,
            RookGenerator rookGenerator,
            BishopGenerator bishopGenerator,
            QueenGenerator queenGenerator,
            KnightGenerator knightGenerator,
            PawnGenerator pawnGenerator,
            CheckGenerator checkGenerator
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
        this.checkGenerator = checkGenerator;
    }

    public Generator generateFor(Piece piece) {
        return pieceToGenerator.get(piece);
    }

    public List<Move> generateAllMoves(Board board) {
        List<Move> list = new ArrayList<>();
        Piece piece;
        var playerPieces = board.isWhiteTurn() ? Piece.WHITE_PIECES : Piece.BLACK_PIECES;
        for (int i = 0; i < 64; i++) {
            piece = board.getPiece(i);
            if(piece == Piece.EMPTY) {
                continue;
            }
            if (playerPieces.contains(piece)) {
                List<Move> from = generateFor(piece).from(board, i);
                for (Move move1 : from) {
                    var newBoard = board.createCopy();
                    newBoard.makeMove(move1);
                    newBoard.setWhiteTurn(!newBoard.isWhiteTurn());
                    if (checkGenerator.from(newBoard, newBoard.getKingIndex())
                            .isEmpty()) {
                        list.add(move1);
                    }
                }
            }
        }
        return list;
    }


}
