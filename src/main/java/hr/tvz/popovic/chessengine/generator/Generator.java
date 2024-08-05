package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;

import java.util.List;

public interface Generator {

    List<Move> from(Board board, int from);

}
