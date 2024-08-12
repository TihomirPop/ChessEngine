package hr.tvz.popovic.chessengine.model;

import hr.tvz.popovic.chessengine.mapper.AlgebraicNotationMapper;

public record Move(int from, int to, Type type) {

    public Move(int from, int to) {
        this(from, to, Type.NORMAL);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder()
                .append(AlgebraicNotationMapper.toAlgebraicNotation(from))
                .append(AlgebraicNotationMapper.toAlgebraicNotation(to));

        switch (type) {
            case QUEEN_PROMOTION -> builder.append("q");
            case ROOK_PROMOTION -> builder.append("r");
            case BISHOP_PROMOTION -> builder.append("b");
            case KNIGHT_PROMOTION -> builder.append("n");
        }

        return builder.toString();
    }

    public enum Type {
        NORMAL,
        CASTLING,
        DOUBLE_PAWN_PUSH,
        QUEEN_PROMOTION,
        ROOK_PROMOTION,
        BISHOP_PROMOTION,
        KNIGHT_PROMOTION,
        EN_PASSANT,
        FIRST_MOVE
    }
}
