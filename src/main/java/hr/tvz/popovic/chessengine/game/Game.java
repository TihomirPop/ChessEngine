package hr.tvz.popovic.chessengine.game;

import hr.tvz.popovic.chessengine.generator.Generators;
import hr.tvz.popovic.chessengine.helper.Perft;
import hr.tvz.popovic.chessengine.mapper.FenMapper;
import hr.tvz.popovic.chessengine.model.Board;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;


@Component
public class Game {

    private final Board board;
    private final Generators generators;

    public Game(Generators generators, Perft perft) throws ExecutionException, InterruptedException {
        this.generators = generators;
        board = null;

//        board.setPiece(61, Piece.WHITE_ROOK);
//        board.setPiece(63, Piece.EMPTY);
//        board.setWhiteTurn(!board.isWhiteTurn());
////
//        board.setPiece(26, Piece.BLACK_PAWN);
//        board.setPiece(18, Piece.EMPTY);
//        board.setWhiteTurn(!board.isWhiteTurn());

        var from = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            var b = FenMapper.fromFen("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10 ");
            perft.runPerft(b, 4);
        }
        System.out.println("Time: " + (System.currentTimeMillis() - from) + "ms");
    }

}
