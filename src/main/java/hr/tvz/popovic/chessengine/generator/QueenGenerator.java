package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class QueenGenerator extends SlidingGenerator {

    @Override
    public List<Move> from(Board board, int from) {
        return List.of();
    }

}
