package hr.tvz.popovic.chessengine.model;

import java.util.List;

public record MakeMoveResponse(
        List<Character> board,
        String endGameMessage
) {

}
