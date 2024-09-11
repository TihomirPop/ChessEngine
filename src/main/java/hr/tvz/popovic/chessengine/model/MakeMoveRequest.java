package hr.tvz.popovic.chessengine.model;

public record MakeMoveRequest(
        int from,
        int to,
        int thinkingTime
) {

}
