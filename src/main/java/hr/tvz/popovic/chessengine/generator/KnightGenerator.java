package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Move;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class KnightGenerator extends Generator {

    public static List<Move> from(Board board, int from) {
        List<Move> list = new ArrayList<>();
        for (int to : new int[]{from - 17, from - 15, from - 10, from - 6, from + 6, from + 10, from + 15, from + 17}) {
            if (Generator.isIndexInBounds(to) && Generator.isPieceOnIndexNotFriendly(board, to) && isValidKnightMove(from, to)) {
                list.add(new Move(from, to));
            }
        }
        return list;
    }

    public static boolean isValidKnightMove(int from, int to) {
        int fromRow = Board.getRow(from);
        int fromCol = Board.getColumn(from);
        int toRow = Board.getRow(to);
        int toCol = Board.getColumn(to);

        int rowDiff = Math.abs(fromRow - toRow);
        int colDiff = Math.abs(fromCol - toCol);

        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

}
