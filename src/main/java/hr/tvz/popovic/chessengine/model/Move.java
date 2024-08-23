package hr.tvz.popovic.chessengine.model;

import hr.tvz.popovic.chessengine.mapper.AlgebraicNotationMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@AllArgsConstructor
@RequiredArgsConstructor
public final class Move {

    private final byte from;
    private final byte to;
    private final Type type;

    public Move(int from, int to) {
        this((byte) from, (byte) to, Type.NORMAL);
    }

    public Move(int from, int to, Type type) {
        this((byte) from, (byte) to, type);
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

    public byte from() {
        return from;
    }

    public byte to() {
        return to;
    }

    public Type type() {
        return type;
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
