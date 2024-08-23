package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Generators {

    public static List<Move> generateAllMoves(Board board) {
        List<Move> allMoves = new ArrayList<>();
        Piece piece;
        for (var i = 0; i < 64; i++) {
            piece = board.getPiece(i);
            if(piece == Piece.EMPTY) {
                continue;
            }
            if (board.isWhiteTurn() ? Piece.isWhitePiece(piece) : Piece.isBlackPiece(piece)) {
                var moves = generateMoves(piece, board, i);
                for (var move : moves) {
                    var newBoard = board.createCopy();
                    var guessEval = newBoard.makeMoveWithEvalGuess(move);
                    move.setGuessEval(guessEval);
                    newBoard.setWhiteTurn(!newBoard.isWhiteTurn());
                    if (CheckGenerator.from(newBoard, newBoard.getKingIndex()).isEmpty()) {
                        allMoves.add(move);
                    }
                }
            }
        }
        return allMoves;
    }

    public static List<Move> generateMoves(Piece piece, Board board, int i) {
        return switch (piece) {
            case WHITE_KING, BLACK_KING -> KingGenerator.from(board, i);
            case WHITE_ROOK, BLACK_ROOK -> RookGenerator.from(board, i);
            case WHITE_BISHOP, BLACK_BISHOP -> BishopGenerator.from(board, i);
            case WHITE_QUEEN, BLACK_QUEEN -> QueenGenerator.from(board, i);
            case WHITE_KNIGHT, BLACK_KNIGHT -> KnightGenerator.from(board, i);
            case WHITE_PAWN, BLACK_PAWN -> PawnGenerator.from(board, i);
            default -> throw new IllegalArgumentException("Invalid piece: " + piece);
        };
    }

}
