package hr.tvz.popovic.chessengine.mapper;

public class AlgebraicNotationMapper {

    public static String toAlgebraicNotation(int square) {
        int row = 8 - square / 8;
        int column = square % 8;
        char file = (char) ('a' + column);
        return "" + file + row;
    }

}
