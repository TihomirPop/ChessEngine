package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.mapper.FenMapper;
import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlphaBetaPruningTest {

    @Test
    void getBestSingleMove() {
        var board = Board.createEmptyBoard();
        board.setPiece(0, Piece.BLACK_KING);
        board.setPiece(16, Piece.WHITE_KING);
        board.setPiece(18, Piece.WHITE_PAWN);

        var move = AlphaBetaPruning.getBestMove(board, 2);

        assertThat(move).isPresent()
                .contains(new Move(18, 10));
    }

    @Test
    void getBestSingleMoveWithPromotion() {
        var board = Board.createEmptyBoard();
        board.setPiece(0, Piece.BLACK_KING);
        board.setPiece(16, Piece.WHITE_KING);
        board.setPiece(18, Piece.WHITE_PAWN);

        var move = AlphaBetaPruning.getBestMove(board, 2);

        assertThat(move).isPresent()
                .contains(new Move(11, 3, Move.Type.QUEEN_PROMOTION));
    }

    @Test
    void getBestMoves() {
        var board = FenMapper.fromFen("k7/8/K3P3/8/8/8/8/8 w - - 0 1");

        var moves = Generators.generateAllMoves(board);

        var move = AlphaBetaPruning.getBestMove(board, 5);

        assertThat(move).isPresent()
                .contains(new Move(11, 3, Move.Type.QUEEN_PROMOTION));
    }

    @Test
    void getBestMove() {
        //todo: why
//        var board = FenMapper.fromFen("8/k5R1/r7/8/8/8/1R6/2K3N1 b - - 12 98");
        var board = FenMapper.fromFen("2Q1k3/8/8/7p/5Q2/8/8/6NK b - - 24 114");
        //this too 2Q1k3/8/8/7p/5Q2/8/8/6NK b - - 24 114

        var moves = Generators.generateAllMoves(board);

        var move = AlphaBetaPruning.getBestMove(300, board);
    }
}