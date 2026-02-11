package hr.tvz.popovic.chessengine.controller;

import hr.tvz.popovic.chessengine.model.Move;

public record MoveDto(
        byte from,
        byte to,
        String type
) {
    public static MoveDto from(Move move) {
        return new MoveDto(move.from(), move.to(), move.type().name());
    }
}
