package hr.tvz.popovic.chessengine.game;

import hr.tvz.popovic.chessengine.generator.Generators;
import hr.tvz.popovic.chessengine.model.Board;
import org.springframework.stereotype.Component;


@Component
public class Game {

    private final Board board;
    private final Generators generators;

    public Game(Generators generators) {
        this.board = Board.createInitialBoard();
        this.generators = generators;
    }

}
