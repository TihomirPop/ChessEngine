package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.MoveEvalPair;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlphaBetaPruning {

    public static Optional<Move> getBestMove(Board board, int depth) {
        return Generators.generateAllMoves(board)
                .parallelStream()
                .sorted(AlphaBetaPruning::compareMoveScore)
                .map(move -> {
                    var newBoard = board.createCopy();
                    newBoard.makeMove(move);
                    var eval = alphaBetaPruning(newBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    return new MoveEvalPair(move, eval);
                })
                .max((a, b) -> board.isWhiteTurn() ?
                        Integer.compare(a.eval(), b.eval()) :
                        Integer.compare(b.eval(), a.eval())
                )
                .map(MoveEvalPair::move);
    }

    public static Optional<Move> getBestMove(int thinkingTime, Board board) {
        var startTime = System.currentTimeMillis();
        var moves =  Generators.generateAllMoves(board);
        var bestEval = board.isWhiteTurn() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Optional<Move> bestMove = Optional.empty();

        for(int depth = 1; depth < Integer.MAX_VALUE; depth++) {
            var finalDepth = depth;
            var finalBestMove = bestMove;
            var moveEvalPair = moves.parallelStream()
                    .peek(move -> {
                        if(finalBestMove.isPresent() && finalBestMove.get().equals(move)) {
                            move.setGuessEval(Integer.MAX_VALUE);
                        }
                    })
                    .sorted(AlphaBetaPruning::compareMoveScore)
                    .map(move -> {
                        var newBoard = board.createCopy();
                        newBoard.makeMove(move);
                        var eval = alphaBetaPruning2(newBoard, finalDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        return new MoveEvalPair(move, eval);
                    })
                    .max((a, b) -> board.isWhiteTurn() ?
                            Integer.compare(a.eval(), b.eval()) :
                            Integer.compare(b.eval(), a.eval())
                    );

            if (moveEvalPair.isPresent()) {
                if(board.isWhiteTurn() && bestEval < Integer.MAX_VALUE) {
                    bestEval = moveEvalPair.get().eval();
                    bestMove = Optional.of(moveEvalPair.get().move());
                } else if(!board.isWhiteTurn() && bestEval > Integer.MIN_VALUE) {
                    bestEval = moveEvalPair.get().eval();
                    bestMove = Optional.of(moveEvalPair.get().move());
                }
            }

            if (System.currentTimeMillis() - startTime > thinkingTime) {
                System.out.println("Depth: " + depth);
                break;
            }
        }

        return bestMove;
    }

    private static int alphaBetaPruning(Board board, int depth, int alpha, int beta) {
        if (depth <= 0) {
            return board.getStaticEvaluation();
        }

        var moves = Generators.generateAllMoves(board);

        if (moves.isEmpty()) {
            if(CheckGenerator.from(board, board.getKingIndex()).isEmpty() || board.isStalemate()) {
                //stalemate
                return 0;
            }
            //checkmate
            return board.isWhiteTurn() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }

        moves.sort(AlphaBetaPruning::compareMoveScore);

        if (board.isWhiteTurn()) {
            int maxEval = Integer.MIN_VALUE;
            for (var move : moves) {
                var newBoard = board.createCopy();
                newBoard.makeMove(move);
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
                newBoard.makeMove(move);
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

    public static Optional<Move> getBestMove2(Board board, int depth) {
        return Generators.generateAllMoves(board)
                .parallelStream()
                .sorted(AlphaBetaPruning::compareMoveScore)
                .map(move -> {
                    var newBoard = board.createCopy();
                    newBoard.makeMove(move);
                    var eval = alphaBetaPruning2(newBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    return new MoveEvalPair(move, eval);
                })
                .max((a, b) -> board.isWhiteTurn() ?
                        Integer.compare(a.eval(), b.eval()) :
                        Integer.compare(b.eval(), a.eval())
                )
                .map(MoveEvalPair::move);
    }

    private static int alphaBetaPruning2(Board board, int depth, int alpha, int beta) {
        if (depth <= 0) {
            return board.getStaticEvaluation2();
        }

        var moves = Generators.generateAllMoves(board);

        if (moves.isEmpty()) {
            if(CheckGenerator.from(board, board.getKingIndex()).isEmpty() || board.isStalemate()) {
                //stalemate
                return 0;
            }
            //checkmate
            return board.isWhiteTurn() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }

        moves.sort(AlphaBetaPruning::compareMoveScore);

        if (board.isWhiteTurn()) {
            int maxEval = Integer.MIN_VALUE;
            for (var move : moves) {
                var newBoard = board.createCopy();
                newBoard.makeMove(move);
                var eval = alphaBetaPruning2(newBoard, depth - 1, alpha, beta);
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
                newBoard.makeMove(move);
                int eval = alphaBetaPruning2(newBoard, depth - 1, alpha, beta);
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
