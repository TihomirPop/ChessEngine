package hr.tvz.popovic.chessengine.mapper;

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

        int column = file - 'a';  // 'a' corresponds to column 0, 'b' to 1, and so on.
        int row = 8 - Character.getNumericValue(rank);  // '8' corresponds to row 0, '7' to 1, and so on.

        return row * 8 + column;
    }


}
