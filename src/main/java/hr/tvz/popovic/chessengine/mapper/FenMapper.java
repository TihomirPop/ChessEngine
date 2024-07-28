package hr.tvz.popovic.chessengine.mapper;

import hr.tvz.popovic.chessengine.model.Piece;

import java.util.ArrayList;

public class FenMapper {

    public static String toFen(ArrayList<Piece> board) {
        var fen = new StringBuilder();
        var emptySpaces = 0;

        for (int i = 0; i < board.size(); i++) {
            var piece = board.get(i);
            if (piece == Piece.EMPTY) {
                emptySpaces++;
            } else {
                if (emptySpaces > 0) {
                    fen.append(emptySpaces);
                    emptySpaces = 0;
                }
                fen.append(Piece.toFen(piece));
            }
            if ((i + 1) % 8 == 0) {
                if (emptySpaces > 0) {
                    fen.append(emptySpaces);
                    emptySpaces = 0;
                }
                if (i != 63) {
                    fen.append("/");
                }
            }
        }

        return fen.toString();
    }

}
