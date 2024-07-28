package hr.tvz.popovic.chessengine.mapper;

import hr.tvz.popovic.chessengine.Board;
import hr.tvz.popovic.chessengine.model.Piece;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FenMapper {

    private final Board boardState;

    public String toFen() {
        var fenBuilder = new StringBuilder();

        piecesToFen(fenBuilder);
        activeColorToFen(fenBuilder);
        castlingRightsToFen(fenBuilder);
        enPassantTargetsToFen(fenBuilder);
        halfMoveClockToFen(fenBuilder);
        fullMoveClockToFen(fenBuilder);

        return fenBuilder.toString();
    }

    private void piecesToFen(StringBuilder fenBuilder) {
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
                fenBuilder.append(Piece.toFen(piece));
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

    private void activeColorToFen(StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        fenBuilder.append(boardState.getIsWhiteTurn() ? "w" : "b");
    }

    private void castlingRightsToFen(StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        boolean hasAnyCastlingRights = appendIfTrue(fenBuilder, boardState.getIsWhiteKingSideCastle(), "K") |
                appendIfTrue(fenBuilder, boardState.getIsWhiteQueenSideCastle(), "Q") |
                appendIfTrue(fenBuilder, boardState.getIsBlackKingSideCastle(), "k") |
                appendIfTrue(fenBuilder, boardState.getIsBlackQueenSideCastle(), "q");
        if (!hasAnyCastlingRights) {
            fenBuilder.append("-");
        }
    }

    private boolean appendIfTrue(StringBuilder fenBuilder, boolean condition, String value) {
        if (condition) {
            fenBuilder.append(value);
            return true;
        }
        return false;
    }

    private void enPassantTargetsToFen(StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        if (boardState.getEnPassantSquare() != -1) {
            fenBuilder.append(AlgebraicNotationMapper.toAlgebraicNotation(boardState.getEnPassantSquare()));
        } else {
            fenBuilder.append("-");
        }
    }

    private void halfMoveClockToFen(StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        fenBuilder.append(boardState.getHalfMoveClock());
    }

    private void fullMoveClockToFen(StringBuilder fenBuilder) {
        fenBuilder.append(" ");
        fenBuilder.append(boardState.getFullMoveNumber());
    }

}
