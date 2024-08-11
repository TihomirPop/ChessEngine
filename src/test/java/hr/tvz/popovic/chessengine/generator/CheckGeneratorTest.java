package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CheckGeneratorTest {

    CheckGenerator checkGenerator = new CheckGenerator();

    @Test
    void shouldReturnCheckingSlidingMoves() {
        var board = Board.createEmptyBoard();

        board.setPiece(35, Piece.WHITE_KING);
        board.setPiece(32, Piece.BLACK_ROOK);
        board.setPiece(39, Piece.BLACK_ROOK);
        board.setPiece(38, Piece.WHITE_ROOK);
        board.setPiece(44, Piece.BLACK_PAWN);
        board.setPiece(51, Piece.BLACK_QUEEN);
        board.setPiece(28, Piece.BLACK_PAWN);
        board.setPiece(25, Piece.BLACK_KNIGHT);
        board.setPiece(17, Piece.BLACK_BISHOP);
        board.setPiece(53, Piece.BLACK_QUEEN);
        board.setPiece(42, Piece.BLACK_KING);

        var moves = checkGenerator.from(board, 35);

        assertThat(moves).hasSize(7)
                .containsExactlyInAnyOrder(
                        new Move(35, 32),
                        new Move(35, 51),
                        new Move(35, 28),
                        new Move(35, 25),
                        new Move(35, 17),
                        new Move(35, 53),
                        new Move(35, 42)
                );
    }

}