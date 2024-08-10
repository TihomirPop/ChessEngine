package hr.tvz.popovic.chessengine.mapper;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Piece;

public class FenMapper {

    public static String toFen(Board boardState) {
        var fenBuilder = new StringBuilder();

        piecesToFen(boardState, fenBuilder);
        activeColorToFen(boardState, fenBuilder);
        castlingRightsToFen(boardState, fenBuilder);
        enPassantTargetsToFen(boardState, fenBuilder);
        halfMoveClockToFen(boardState, fenBuilder);
        fullMoveClockToFen(boardState, fenBuilder);

        return fenBuilder.toString();
    }

    private static void piecesToFen(Board boardState, StringBuilder fenBuilder) {
        var board = boardState.getBoard();
        var emptySpaces = 0;

        for (int i = 0; i < board.size(); i++) {
            var piece = board.get(i);
            if (piece == Piece.EMPTY) {
                emptySpaces++;
            } else {
                if (emptySpaces > 0) {
                    fenBuilder.append(emptySpaces);
                    emptySpaces = 0;
                }
                fenBuilder.append(piece.toFen());
            }
            if ((i + 1) % 8 == 0) {
                if (emptySpaces > 0) {
                    fenBuilder.append(emptySpaces);
                    emptySpaces = 0;
                }
                if (i != 63) {
                    fenBuilder.append("/");
                }
            }
        }
    }

    private static void activeColorToFen(Board boardState, StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        fenBuilder.append(boardState.isWhiteTurn() ? "w" : "b");
    }

    private static void castlingRightsToFen(Board boardState, StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        boolean hasAnyCastlingRights = appendIfTrue(fenBuilder, boardState.isWhiteKingSideCastle(), "K") |
                appendIfTrue(fenBuilder, boardState.isWhiteQueenSideCastle(), "Q") |
                appendIfTrue(fenBuilder, boardState.isBlackKingSideCastle(), "k") |
                appendIfTrue(fenBuilder, boardState.isBlackQueenSideCastle(), "q");
        if (!hasAnyCastlingRights) {
            fenBuilder.append("-");
        }
    }

    private static boolean appendIfTrue(StringBuilder fenBuilder, boolean condition, String value) {
        if (condition) {
            fenBuilder.append(value);
            return true;
        }
        return false;
    }

    private static void enPassantTargetsToFen(Board boardState, StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        if (boardState.getEnPassantSquare() != -1) {
            fenBuilder.append(AlgebraicNotationMapper.toAlgebraicNotation(boardState.getEnPassantSquare()));
        } else {
            fenBuilder.append("-");
        }
    }

    private static void halfMoveClockToFen(Board boardState, StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        fenBuilder.append(boardState.getHalfMoveClock());
    }

    private static void fullMoveClockToFen(Board boardState, StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        fenBuilder.append(boardState.getFullMoveNumber());
    }

}
