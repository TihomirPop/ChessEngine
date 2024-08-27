package hr.tvz.popovic.chessengine.game;

import hr.tvz.popovic.chessengine.generator.AlphaBetaPruning;
import hr.tvz.popovic.chessengine.generator.CheckGenerator;
import hr.tvz.popovic.chessengine.mapper.FenMapper;
import hr.tvz.popovic.chessengine.model.Board;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;


@Component
public class Game {

    private final Set<String> fenSet = Set.of(
            "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
            "rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1",
            "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2",
            "rnbqkbnr/ppp1pppp/8/3p4/3P4/8/PPP1PPPP/RNBQKBNR w KQkq d6 0 2",
            "rnbqkbnr/pppp1ppp/8/4p3/2P5/8/PP1PPPPP/RNBQKBNR w KQkq e6 0 2",
            "rnbqkb1r/pppppppp/5n2/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 1 2",
            "rnbqkbnr/ppp1pppp/8/3p4/2PP4/8/PP2PPPP/RNBQKBNR b KQkq c3 0 2",
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2",
            "rnbqkbnr/pppp1ppp/8/4p3/3P4/2N5/PPP1PPPP/R1BQKBNR b KQkq - 1 2",
            "rnbqkbnr/pppp1ppp/4p3/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2",
            "rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2",
            "rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR b KQkq - 0 1",
            "rnbqkbnr/pppppppp/8/8/2P5/8/PP1PPPPP/RNBQKBNR b KQkq c3 0 1",
            "rnbqkbnr/pp1ppppp/2p5/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2",
            "rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1",
            "rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2",
            "rnbqkbnr/ppp1pppp/8/3p4/8/2N5/PPPPPPPP/R1BQKBNR w KQkq d6 0 2",
            "rnbqkbnr/pppppppp/8/8/8/2N5/PPPPPPPP/R1BQKBNR b KQkq - 1 1",
            "rnbqkb1r/pppppppp/5n2/8/3P4/8/PPP1PPPP/RNBQKBNR w KQkq - 1 2",
            "rnbqkbnr/ppp1pppp/3p4/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2",
            "rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/RNBQKBNR b KQkq - 0 1",
            "rnbqkbnr/pppppppp/8/8/1P6/8/P1PPPPPP/RNBQKBNR b KQkq b3 0 1",
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2",
            "rnbqkbnr/pppp1ppp/8/4p3/3PP3/8/PPP2PPP/RNBQKBNR b KQkq d3 0 2",
            "rnbqkbnr/ppp1pppp/3p4/8/3P4/8/PPP1PPPP/RNBQKBNR w KQkq - 0 2",
            "rnbqkbnr/pppppp1p/6p1/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2",
            "rnbqkbnr/pppppppp/8/8/8/7N/PPPPPPPP/RNBQKB1R b KQkq - 1 1",
            "rnbqkbnr/ppp1pppp/8/3p4/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2",
            "rnbqkbnr/pppp1ppp/8/4p3/8/5N2/PPPPPPPP/RNBQKB1R w KQkq e6 0 2",
            "rnbqkbnr/pppppppp/8/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq a3 0 1",
            "rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR b KQkq - 0 1",
            "rnbqkbnr/pppppppp/8/8/8/1P6/P1PPPPPP/RNBQKBNR b KQkq - 0 1",
            "rnbqkbnr/pppp1ppp/4p3/8/3P4/8/PPP1PPPP/RNBQKBNR w KQkq - 0 2",
            "rnbqkbnr/ppp1pppp/8/3p4/4P3/2N5/PPPP1PPP/R1BQKBNR b KQkq - 1 2",
            "rnbqkbnr/pp1ppppp/2p5/8/3P4/8/PPP1PPPP/RNBQKBNR w KQkq - 0 2",
            "rnbqkbnr/pppp1ppp/8/4p3/4P3/2N5/PPPP1PPP/R1BQKBNR b KQkq - 1 2",
            "rnbqkbnr/pp1ppppp/2p5/8/2P5/8/PP1PPPPP/RNBQKBNR w KQkq - 0 2"
    );

    public Game() {
        System.out.println("number of games: " + fenSet.size());
        var results = fenSet.stream()
                .map(FenMapper::fromFen)
                .map(this::playGame)
                .toList();

        var firstPlayerWins = results.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(b -> b)
                .count();

        var secondPlayerWins = results.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(b -> !b)
                .count();

        var stalemates = results.stream()
                .filter(Optional::isEmpty)
                .count();

        System.out.println("First player wins: " + firstPlayerWins);
        System.out.println("Second player wins: " + secondPlayerWins);
        System.out.println("Stalemates: " + stalemates);

//        playGame(FenMapper.fromFen("k7/8/K3P3/8/8/8/8/8 w - - 0 1"));
//        playGame(FenMapper.fromFen("k7/2B5/K7/8/8/8/8/8 b - - 0 1"));

//        var b = FenMapper.fromFen("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10 ");
//        var b = Board.createInitialBoard();

//        Perft.runPerft(b, 5);
//        AlphaBetaPruning.getBestMove(b, 5);
//        var from = System.currentTimeMillis();
        //print size of Move class instance in bytes
//        var b = Board.createEmptyBoard();
//        b.setPiece(0, Piece.BLACK_ROOK);
//        b.setPiece(63, Piece.WHITE_BISHOP);
//        var from = System.currentTimeMillis();
//        System.out.println(AlphaBetaPruning.getBestMove(b, 6));
//        System.out.println("Time: " + (System.currentTimeMillis() - from) + "ms");
//        from = System.currentTimeMillis();
//        System.out.println(AlphaBetaPruning.getBestMoveMM(b, 6));
//        System.out.println("Time: " + (System.currentTimeMillis() - from) + "ms");
//        System.out.println("Time: " + (System.currentTimeMillis() - from) + "ms");
    }

    private Optional<Boolean> playGame(Board board) {
        System.out.println("Starting game with fen: " + FenMapper.toFen(board));
        while (true) {
            var bestMove = AlphaBetaPruning.getBestMove(300, board);
            if (bestMove.isEmpty()) {
                if (CheckGenerator.from(board, board.getKingIndex())
                        .isEmpty() || board.isStalemate()) {
                    System.out.println("Stalemate");
                    System.out.println("Stalemate fen: " + FenMapper.toFen(board));
                    return Optional.empty();
                }
                System.out.println("Second player wins");
                System.out.println("Winning fen: " + FenMapper.toFen(board));
                return Optional.of(false);
            }
            board.makeMove(bestMove.get());

            bestMove = AlphaBetaPruning.getBestMove(board, 5);
            if (bestMove.isEmpty()) {
                if (CheckGenerator.from(board, board.getKingIndex())
                        .isEmpty() || board.isStalemate()) {
                    System.out.println("Stalemate");
                    System.out.println("Stalemate fen: " + FenMapper.toFen(board));
                    return Optional.empty();
                }
                System.out.println("First player wins");
                System.out.println("Winning fen: " + FenMapper.toFen(board));
                return Optional.of(true);
            }
            board.makeMove(bestMove.get());
        }
    }

}
