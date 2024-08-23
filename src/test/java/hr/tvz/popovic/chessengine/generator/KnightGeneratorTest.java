package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class KnightGeneratorTest {

    @Test
    void shouldReturnValidKnightMoves() {
        var board = Board.createEmptyBoard();

        board.setPiece(1, Piece.WHITE_KNIGHT);
        board.setPiece(11, Piece.WHITE_PAWN);
        board.setPiece(16, Piece.BLACK_PAWN);

        var moves = KnightGenerator.from(board, 1);

        assertThat(moves).hasSize(2)
                .containsExactlyInAnyOrderElementsOf(List.of(new Move(1, 16), new Move(1, 18)));

    }

}