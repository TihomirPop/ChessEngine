package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class KingGenerator extends Generator {

    public static List<Move> from(Board board, int from) {
        var moves = generateCastlingMoves(board, from);
        moves.addAll(generateDirectionMoves(board, from));

        return moves;
    }

    private static List<Move> generateCastlingMoves(Board board, int from) {
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
        var kingSideRookPos = isWhiteTurn ? 63 : 7;
        var queenSideRookPos = isWhiteTurn ? 56 : 0;

        if ((isWhiteTurn && board.isWhiteKingSideCastle()) || (!isWhiteTurn && board.isBlackKingSideCastle())) {
            if (board.getPiece(kingSidePos1) == Piece.EMPTY && board.getPiece(kingSidePos2) == Piece.EMPTY &&
                    board.getPiece(kingSideRookPos) == (isWhiteTurn ? Piece.WHITE_ROOK : Piece.BLACK_ROOK)) {
                if (CheckGenerator.from(board, kingStartPos).isEmpty() && CheckGenerator.from(board, kingSidePos1).isEmpty()) {
                    moves.add(new Move(from, kingSidePos2, Move.Type.CASTLING));
                }
            }
        }

        if ((isWhiteTurn && board.isWhiteQueenSideCastle()) || (!isWhiteTurn && board.isBlackQueenSideCastle())) {
            if (board.getPiece(queenSidePos1) == Piece.EMPTY && board.getPiece(queenSidePos2) == Piece.EMPTY && board.getPiece(queenSidePos3) == Piece.EMPTY &&
                    board.getPiece(queenSideRookPos) == (isWhiteTurn ? Piece.WHITE_ROOK : Piece.BLACK_ROOK)) {
                if (CheckGenerator.from(board, kingStartPos).isEmpty() && CheckGenerator.from(board, queenSidePos1).isEmpty()) {
                    moves.add(new Move(from, queenSidePos2, Move.Type.CASTLING));
                }
            }
        }

        return moves;
    }

    private static List<Move> generateDirectionMoves(Board board, int from) {
        var isFirstMove = board.isWhiteTurn() ?
                board.isWhiteQueenSideCastle() || board.isWhiteKingSideCastle() :
                board.isBlackQueenSideCastle() || board.isBlackKingSideCastle();

        List<Move> list = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            int to = from + direction.getOffset();
            if (Generator.isIndexInBounds(to) && (Math.abs(Board.getColumn(from) - Board.getColumn(to)) <= 1) && (Math.abs(Board.getRow(from) - Board.getRow(to)) <= 1) && Generator.isPieceOnIndexNotFriendly(board, to)) {
                Move move = isFirstMove ? new Move(from, to, Move.Type.FIRST_MOVE) : new Move(from, to);
                list.add(move);
            }
        }
        return list;
    }

}
