package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class KnightGeneratorTest {

    Generator knightGenerator = new KnightGenerator();

    @Test
    void shouldReturnValidKnightMoves() {
        var board = Board.createEmptyBoard();
        var boardPieces = board.getBoard();

        boardPieces.set(1, Piece.WHITE_KNIGHT);
        boardPieces.set(11, Piece.WHITE_PAWN);
        boardPieces.set(16, Piece.BLACK_PAWN);

        var moves = knightGenerator.from(board, 1);

        assertThat(moves).hasSize(2)
                .containsExactlyInAnyOrderElementsOf(List.of(new Move(1, 16), new Move(1, 18)));

    }

}