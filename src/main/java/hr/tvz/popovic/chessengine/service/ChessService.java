package hr.tvz.popovic.chessengine.service;

import hr.tvz.popovic.chessengine.evaluation.AlphaBetaPruning;
import hr.tvz.popovic.chessengine.generator.CheckGenerator;
import hr.tvz.popovic.chessengine.generator.Generators;
import hr.tvz.popovic.chessengine.mapper.FenMapper;
import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.MakeMoveResponse;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChessService {

    private Board board;

    public List<Character> start() {
        board = Board.createInitialBoard();
        return Arrays.stream(board.getBoard())
                .map(Piece::toFen)
                .toList();
    }

    public Optional<MakeMoveResponse> startFromFen(String fen, boolean isWhite) {
        try {
            board = FenMapper.fromFen(fen);
            if(isWhite != board.isWhiteTurn()) {
                var makeMoveResponse = makeOpponentMove(600);
                if (makeMoveResponse != null) {
                    return Optional.of(makeMoveResponse);
                }
            }
            return Optional.of(new MakeMoveResponse(
                    Arrays.stream(board.getBoard())
                            .map(Piece::toFen)
                            .toList(),
                    null
            ));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Byte> getMoves(int from) {
        return Generators.generateAllMoves(board)
                .stream()
                .filter(move -> move.from() == from)
                .map(Move::to)
                .toList();
    }

    public MakeMoveResponse makeMove(int from, int to, int thinkingTime) {
        var move = Generators.generateAllMoves(board)
                .stream()
                .filter(m -> m.from() == from && m.to() == to)
                .filter(m -> m.type() != Move.Type.BISHOP_PROMOTION &&
                        m.type() != Move.Type.ROOK_PROMOTION &&
                        m.type() != Move.Type.KNIGHT_PROMOTION
                )
                .findFirst();

        if (move.isEmpty()) {
            return new MakeMoveResponse(
                    Arrays.stream(board.getBoard())
                            .map(Piece::toFen)
                            .toList(),
                    null
            );
        }

        board.makeMove(move.get());

        var makeMoveResponse = makeOpponentMove(thinkingTime);
        if (makeMoveResponse != null) {
            return makeMoveResponse;
        }

        return new MakeMoveResponse(
                Arrays.stream(board.getBoard())
                        .map(Piece::toFen)
                        .toList(),
                null
        );
    }

    private MakeMoveResponse makeOpponentMove(int thinkingTime) {
        var opponentMove = AlphaBetaPruning.getBestMove(board, thinkingTime);

        if (opponentMove.isEmpty()) {
            if (checkStalemate()) {
                return new MakeMoveResponse(
                        Arrays.stream(board.getBoard())
                                .map(Piece::toFen)
                                .toList(),
                        "Stalemate"
                );
            }
            log.debug("Player wins");
            log.debug("Winning fen: {}", FenMapper.toFen(board));
            return new MakeMoveResponse(
                    Arrays.stream(board.getBoard())
                            .map(Piece::toFen)
                            .toList(),
                    "Player wins"
            );

        }

        board.makeMove(opponentMove.get());
        var playerMoves = Generators.generateAllMoves(board);
        if (playerMoves.isEmpty()) {
            if (checkStalemate()) {
                return new MakeMoveResponse(
                        Arrays.stream(board.getBoard())
                                .map(Piece::toFen)
                                .toList(),
                        "Stalemate"
                );
            }
            log.debug("Opponent wins");
            log.debug("Winning fen: {}", FenMapper.toFen(board));
            return new MakeMoveResponse(
                    Arrays.stream(board.getBoard())
                            .map(Piece::toFen)
                            .toList(),
                    "Opponent wins"
            );

        }
        return null;
    }

    private boolean checkStalemate() {
        if (CheckGenerator.from(board, board.getKingIndex()).isEmpty() || board.isStalemate()) {
            log.debug("Stalemate");
            log.debug("Stalemate fen: {}", FenMapper.toFen(board));
            return true;
        }
        return false;
    }

}
