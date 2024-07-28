package hr.tvz.popovic.chessengine;

import hr.tvz.popovic.chessengine.model.Piece;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Data
@Component
public class Board {

    private final List<Piece> board = Arrays.asList(new Piece[64]);
    private Boolean isWhiteTurn = true;
    private Boolean isWhiteKingSideCastle = true;
    private Boolean isWhiteQueenSideCastle = true;
    private Boolean isBlackKingSideCastle = true;
    private Boolean isBlackQueenSideCastle = true;
    private Integer enPassantSquare = -1;
    private Integer halfMoveClock = 0;
    private Integer fullMoveNumber = 1;

    public Board() {
        initializeBoard();
    }

    private void initializeBoard() {
        addPieces(
                Piece.BLACK_ROOK,
                Piece.BLACK_KNIGHT,
                Piece.BLACK_BISHOP,
                Piece.BLACK_QUEEN,
                Piece.BLACK_KING,
                Piece.BLACK_BISHOP,
                Piece.BLACK_KNIGHT,
                Piece.BLACK_ROOK
        );
        addInitialPawns(Piece.BLACK_PAWN);
        addInitialEmptySpaces();
        addInitialPawns(Piece.WHITE_PAWN);
        addPieces(
                Piece.WHITE_ROOK,
                Piece.WHITE_KNIGHT,
                Piece.WHITE_BISHOP,
                Piece.WHITE_QUEEN,
                Piece.WHITE_KING,
                Piece.WHITE_BISHOP,
                Piece.WHITE_KNIGHT,
                Piece.WHITE_ROOK
        );
    }

    private void addPieces(Piece... pieces) {
        board.addAll(Arrays.asList(pieces));
    }

    private void addInitialPawns(Piece pawn) {
        for (int i = 0; i < 8; i++) {
            board.add(pawn);
        }
    }

    private void addInitialEmptySpaces() {
        for (int i = 0; i < 32; i++) {
            board.add(Piece.EMPTY);
        }
    }

}
