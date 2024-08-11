package hr.tvz.popovic.chessengine.model;

import hr.tvz.popovic.chessengine.mapper.AlgebraicNotationMapper;

public record Move(int from, int to, Type type) {

    public Move(int from, int to) {
        this(from, to, Type.NORMAL);
    }

    @Override
    public String toString() {
        return AlgebraicNotationMapper.toAlgebraicNotation(from) + AlgebraicNotationMapper.toAlgebraicNotation(to);
    }

    public enum Type {
        NORMAL,
        CASTLING,
        DOUBLE_PAWN_PUSH,
        QUEEN_PROMOTION,
        ROOK_PROMOTION,
        BISHOP_PROMOTION,
    }
}
