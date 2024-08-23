package hr.tvz.popovic.chessengine.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlgebraicNotationMapper {

    public static String toAlgebraicNotation(int square) {
        int row = 8 - square / 8;
        int column = square % 8;
        char file = (char) ('a' + column);
        return "" + file + row;
    }

    public static int fromAlgebraicNotation(String notation) {
        if (notation.length() != 2) {
            throw new IllegalArgumentException("Invalid algebraic notation: " + notation);
        }

        char file = notation.charAt(0);
        char rank = notation.charAt(1);

        int column = file - 'a';
        int row = 8 - Character.getNumericValue(rank);

        return row * 8 + column;
    }

}
