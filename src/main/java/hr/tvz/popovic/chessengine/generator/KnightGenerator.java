package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
class KnightGenerator extends Generator {

    @Override
    public List<Move> from(Board board, int from) {
        return IntStream.of(from - 17, from - 15, from - 10, from - 6, from + 6, from + 10, from + 15, from + 17)
                .filter(Generator::isIndexInBounds)
                .filter(to -> Generator.isPieceOnIndexNotFriendly(board, to))
                .filter(to -> isValidKnightMove(from, to))
                .mapToObj(to -> new Move(from, to))
                .toList();
    }

    private boolean isValidKnightMove(int from, int to) {
        int fromRow = from / 8;
        int fromCol = from % 8;
        int toRow = to / 8;
        int toCol = to % 8;

        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);

        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

}
