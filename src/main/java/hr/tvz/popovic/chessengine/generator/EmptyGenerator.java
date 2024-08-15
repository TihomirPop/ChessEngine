package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EmptyGenerator extends Generator {

    @Override
    public Set<Move> from(Board board, int from) {
        return Set.of();
    }

}
