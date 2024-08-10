package hr.tvz.popovic.chessengine.model;

import hr.tvz.popovic.chessengine.mapper.AlgebraicNotationMapper;

public record Move(int from, int to) {

    @Override
    public String toString() {
        return AlgebraicNotationMapper.toAlgebraicNotation(from) + AlgebraicNotationMapper.toAlgebraicNotation(to);
    }

}
