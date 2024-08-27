package hr.tvz.popovic.chessengine.service;

import hr.tvz.popovic.chessengine.generator.AlphaBetaPruning;
import hr.tvz.popovic.chessengine.generator.CheckGenerator;
import hr.tvz.popovic.chessengine.generator.Generators;
import hr.tvz.popovic.chessengine.mapper.FenMapper;
import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.MakeMoveResponse;
import hr.tvz.popovic.chessengine.model.Move;
import hr.tvz.popovic.chessengine.model.Piece;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ChessService {

    private Board board;

    public List<Character> start() {
        board = Board.createInitialBoard();
        return Arrays.stream(board.getBoard())
                .map(Piece::toFen)
                .toList();
    }

    public Optional<List<Character>> startFromFen(String fen) {
        try {
            board = FenMapper.fromFen(fen);
            return Optional.of(Arrays.stream(board.getBoard())
                    .map(Piece::toFen)
                    .toList());
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
        var opponentMove = AlphaBetaPruning.getBestMove(thinkingTime, board);

        if (opponentMove.isEmpty()) {
            if (CheckGenerator.from(board, board.getKingIndex())
                    .isEmpty() || board.isStalemate()) {
                System.out.println("Stalemate");
                System.out.println("Stalemate fen: " + FenMapper.toFen(board));
                return new MakeMoveResponse(
                        Arrays.stream(board.getBoard())
                                .map(Piece::toFen)
                                .toList(),
                        "Stalemate"
                );
            }
            System.out.println("Player wins");
            System.out.println("Winning fen: " + FenMapper.toFen(board));
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
            if (CheckGenerator.from(board, board.getKingIndex())
                    .isEmpty() || board.isStalemate()) {
                System.out.println("Stalemate");
                System.out.println("Stalemate fen: " + FenMapper.toFen(board));
                return new MakeMoveResponse(
                        Arrays.stream(board.getBoard())
                                .map(Piece::toFen)
                                .toList(),
                        "Stalemate"
                );
            }
            System.out.println("Opponent wins");
            System.out.println("Winning fen: " + FenMapper.toFen(board));
            return new MakeMoveResponse(
                    Arrays.stream(board.getBoard())
                            .map(Piece::toFen)
                            .toList(),
                    "Opponent wins"
            );

        }

        return new MakeMoveResponse(
                Arrays.stream(board.getBoard())
                        .map(Piece::toFen)
                        .toList(),
                null
        );
    }

}
