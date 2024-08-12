package hr.tvz.popovic.chessengine.mapper;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Piece;

public class FenMapper {

    public static String toFen(Board board) {
        var fenBuilder = new StringBuilder();

        piecesToFen(board, fenBuilder);
        activeColorToFen(board, fenBuilder);
        castlingRightsToFen(board, fenBuilder);
        enPassantTargetsToFen(board, fenBuilder);
        halfMoveClockToFen(board, fenBuilder);
        fullMoveClockToFen(board, fenBuilder);

        return fenBuilder.toString();
    }

    public static Board fromFen(String fen) {
        String[] parts = fen.split(" ");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid FEN string: " + fen);
        }

        Board board = Board.createEmptyBoard();

        parsePieces(parts[0], board);
        parseActiveColor(parts[1], board);
        parseCastlingRights(parts[2], board);
        parseEnPassantTarget(parts[3], board);
        board.setHalfMoveClock(Integer.parseInt(parts[4]));
        board.setFullMoveNumber(Integer.parseInt(parts[5]));

        return board;
    }

    private static void piecesToFen(Board board, StringBuilder fenBuilder) {
        var emptySpaces = 0;

        for (var i = 0; i < board.size(); i++) {
            var piece = board.getPiece(i);
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

    private static void activeColorToFen(Board board, StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        fenBuilder.append(board.isWhiteTurn() ? "w" : "b");
    }

    private static void castlingRightsToFen(Board board, StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        boolean hasAnyCastlingRights = appendIfTrue(fenBuilder, board.isWhiteKingSideCastle(), "K") |
                appendIfTrue(fenBuilder, board.isWhiteQueenSideCastle(), "Q") |
                appendIfTrue(fenBuilder, board.isBlackKingSideCastle(), "k") |
                appendIfTrue(fenBuilder, board.isBlackQueenSideCastle(), "q");
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

    private static void enPassantTargetsToFen(Board board, StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        if (board.getEnPassantSquare() != -1) {
            fenBuilder.append(AlgebraicNotationMapper.toAlgebraicNotation(board.getEnPassantSquare()));
        } else {
            fenBuilder.append("-");
        }
    }

    private static void halfMoveClockToFen(Board board, StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        fenBuilder.append(board.getHalfMoveClock());
    }

    private static void fullMoveClockToFen(Board board, StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        fenBuilder.append(board.getFullMoveNumber());
    }

    private static void parsePieces(String pieces, Board board) {
        int squareIndex = 0;
        for (char c : pieces.toCharArray()) {
            if (c == '/') {
                continue;
            } else if (Character.isDigit(c)) {
                squareIndex += c - '0';
            } else {
                board.setPiece(squareIndex, Piece.fromFen(c));
                squareIndex++;
            }
        }
    }

    private static void parseActiveColor(String activeColor, Board board) {
        board.setWhiteTurn(activeColor.equals("w"));
    }

    private static void parseCastlingRights(String castling, Board board) {
        board.setWhiteKingSideCastle(castling.contains("K"));
        board.setWhiteQueenSideCastle(castling.contains("Q"));
        board.setBlackKingSideCastle(castling.contains("k"));
        board.setBlackQueenSideCastle(castling.contains("q"));
    }

    private static void parseEnPassantTarget(String enPassant, Board board) {
        if (enPassant.equals("-")) {
            board.setEnPassantSquare(-1);
        } else {
            board.setEnPassantSquare(AlgebraicNotationMapper.fromAlgebraicNotation(enPassant));
        }
    }
}
