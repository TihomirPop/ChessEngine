package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Direction;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SlidingGeneratorTest {

    @Test
    void shouldCreateMovesInAllDirections() {
        var board = Board.createEmptyBoard();
        List<Move> moves = new ArrayList<>();

        moves.addAll(SlidingGenerator.generateSlidingMoves(board, 9, Direction.UP));
        moves.addAll(SlidingGenerator.generateSlidingMoves(board, 9, Direction.DOWN));
        moves.addAll(SlidingGenerator.generateSlidingMoves(board, 9, Direction.LEFT));
        moves.addAll(SlidingGenerator.generateSlidingMoves(board, 9, Direction.RIGHT));
        moves.addAll(SlidingGenerator.generateSlidingMoves(board, 9, Direction.UP_LEFT));
        moves.addAll(SlidingGenerator.generateSlidingMoves(board, 9, Direction.UP_RIGHT));
        moves.addAll(SlidingGenerator.generateSlidingMoves(board, 9, Direction.DOWN_LEFT));
        moves.addAll(SlidingGenerator.generateSlidingMoves(board, 9, Direction.DOWN_RIGHT));

        assertThat(moves).hasSize(23)
                .containsExactlyInAnyOrderElementsOf(getExpectedMovesInAllDirections());
    }

    @Test
    void shouldStopBeforeFriendly(){
        var board = Board.createEmptyBoard();

        board.setPiece(0, Piece.WHITE_ROOK);
        board.setPiece(3, Piece.WHITE_QUEEN);

        List<Move> moves = new ArrayList<>(SlidingGenerator.generateSlidingMoves(board, 0, Direction.RIGHT));

        assertThat(moves).hasSize(2)
                .containsExactlyInAnyOrder(new Move(0, 1), new Move(0, 2));
    }

    @Test
    void shouldStopAtOpponent(){
        var board = Board.createEmptyBoard();

        board.setPiece(0, Piece.WHITE_ROOK);
        board.setPiece(3, Piece.BLACK_QUEEN);

        List<Move> moves = new ArrayList<>(SlidingGenerator.generateSlidingMoves(board, 0, Direction.RIGHT));

        assertThat(moves).hasSize(3)
                .containsExactlyInAnyOrder(new Move(0, 1), new Move(0, 2), new Move(0, 3));
    }

    @Test
    void shouldNotGoOutOfBoundWhenInCorner(){
        var board = Board.createEmptyBoard();

        board.setPiece(0, Piece.WHITE_ROOK);

        List<Move> moves = new ArrayList<>(SlidingGenerator.generateSlidingMoves(board, 0, Direction.LEFT));

        assertThat(moves).hasSize(0);
    }

    private static List<Move> getExpectedMovesInAllDirections() {
        return List.of(
                new Move(9, 0),
                new Move(9, 1),
                new Move(9, 2),
                new Move(9, 8),
                new Move(9, 10),
                new Move(9, 11),
                new Move(9, 12),
                new Move(9, 13),
                new Move(9, 14),
                new Move(9, 15),
                new Move(9, 16),
                new Move(9, 17),
                new Move(9, 18),
                new Move(9, 25),
                new Move(9, 27),
                new Move(9, 33),
                new Move(9, 36),
                new Move(9, 41),
                new Move(9, 45),
                new Move(9, 49),
                new Move(9, 54),
                new Move(9, 57),
                new Move(9, 63)
        );
    }

}