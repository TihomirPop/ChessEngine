package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.MoveEvalPair;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlphaBetaPruning {

    public static Move getBestMove(Board board, int depth) {
        return Generators.generateAllMoves(board)
                .parallelStream()
                .sorted(AlphaBetaPruning::compareMoveScore)
                .map(move -> {
                    var newBoard = board.createCopy();
                    newBoard.makeMoveWithEvalGuess(move);
                    var eval = alphaBetaPruning(newBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    return new MoveEvalPair(move, eval);
                })
                .max((a, b) -> board.isWhiteTurn() ?
                        Integer.compare(a.eval(), b.eval()) :
                        Integer.compare(b.eval(), a.eval())
                )
                .map(MoveEvalPair::move)
                .orElse(null);
    }

    private static int alphaBetaPruning(Board board, int depth, int alpha, int beta) {
        if(board.isCheckMate()) {
            return board.isWhiteTurn() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        } else if (depth == 0) {
            return board.getStaticEvaluation();
        }

        var moves = Generators.generateAllMoves(board);
        moves.sort(AlphaBetaPruning::compareMoveScore);

        if (board.isWhiteTurn()) {
            int maxEval = Integer.MIN_VALUE;
            for (var move : moves) {
                var newBoard = board.createCopy();
                newBoard.makeMoveWithEvalGuess(move);
                var eval = alphaBetaPruning(newBoard, depth - 1, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (var move : moves) {
                var newBoard = board.createCopy();
                newBoard.makeMoveWithEvalGuess(move);
                int eval = alphaBetaPruning(newBoard, depth - 1, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }

    private static int compareMoveScore(Move a, Move b) {
        return Integer.compare(b.getGuessEval(), a.getGuessEval());
    }

}
