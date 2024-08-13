package hr.tvz.popovic.chessengine.helper;

import hr.tvz.popovic.chessengine.generator.Generators;
import hr.tvz.popovic.chessengine.model.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Perft {

    private final Generators generators;

    public void runPerft(Board board, int depth) {
        log.info("Perft of depth {} for board:\n{}", depth, board);
        var from = System.currentTimeMillis();
        var totalNodes = generators.generateAllMoves(board)
                        .parallelStream()
                        .mapToLong(move -> {
                            var newBoard = board.createCopy();
                            newBoard.makeMove(move);
                            var nodes = perft(newBoard, depth - 1);
                            log.info("{}: {}", move, nodes);
                            return nodes;
                        })
                        .sum();
log.info("Total Nodes: {}", totalNodes);
        log.info("Time: {}ms", System.currentTimeMillis() - from);
    }


    private long perft(Board board, int depth) {
        if (depth == 0) {
            return 1;
        }

        var nodes = 0L;
        var moves = generators.generateAllMoves(board);

        for (var move : moves) {
            var newBoard = board.createCopy();
            newBoard.makeMove(move);
            nodes += perft(newBoard, depth - 1);
        }

        return nodes;
    }

}
