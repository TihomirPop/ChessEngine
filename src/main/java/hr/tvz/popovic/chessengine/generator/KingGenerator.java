package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
class KingGenerator extends Generator {
    private final CheckGenerator checkGenerator;

    @Override
    public List<Move> from(Board board, int from) {
        var moves = generateCastlingMoves(board, from);
        moves.addAll(generateDirectionMoves(board, from));

        return moves;
    }

    private List<Move> generateCastlingMoves(Board board, int from) {
        List<Move> moves = new ArrayList<>();
        var isWhiteTurn = board.isWhiteTurn();
        var kingStartPos = isWhiteTurn ? 60 : 4;
        var rightOffset = Direction.RIGHT.getOffset();
        var leftOffset = Direction.LEFT.getOffset();
        var kingSidePos1 = kingStartPos + rightOffset;
        var kingSidePos2 = kingSidePos1 + rightOffset;
        var queenSidePos1 = kingStartPos + leftOffset;
        var queenSidePos2 = queenSidePos1 + leftOffset;
        var queenSidePos3 = queenSidePos2 + leftOffset;

        if ((isWhiteTurn && board.isWhiteKingSideCastle()) || (!isWhiteTurn && board.isBlackKingSideCastle())) {
            if (board.getPiece(kingSidePos1) == Piece.EMPTY && board.getPiece(kingSidePos2) == Piece.EMPTY) {
                if (checkGenerator.from(board, kingStartPos).isEmpty() && checkGenerator.from(board, kingSidePos1).isEmpty()) {
                    moves.add(new Move(from, kingSidePos2));
                }
            }
        }

        if ((isWhiteTurn && board.isWhiteQueenSideCastle()) || (!isWhiteTurn && board.isBlackQueenSideCastle())) {
            if (board.getPiece(queenSidePos1) == Piece.EMPTY && board.getPiece(queenSidePos2) == Piece.EMPTY && board.getPiece(queenSidePos3) == Piece.EMPTY) {
                if (checkGenerator.from(board, kingStartPos).isEmpty() && checkGenerator.from(board, queenSidePos1).isEmpty()) {
                    moves.add(new Move(from, queenSidePos2));
                }
            }
        }

        return moves;
    }

    private static List<Move> generateDirectionMoves(Board board, int from) {
        return Arrays.stream(Direction.values())
                .map(direction -> from + direction.getOffset())
                .filter(Generator::isIndexInBounds)
                .filter(to -> Math.abs(Board.getColumn(from) - Board.getColumn(to)) <= 1)
                .filter(to -> Math.abs(Board.getRow(from) - Board.getRow(to)) <= 1)
                .filter(to -> Generator.isPieceOnIndexNotFriendly(board, to))
                .map(to -> new Move(from, to))
                .toList();
    }

}
