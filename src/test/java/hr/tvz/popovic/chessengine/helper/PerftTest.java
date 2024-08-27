package hr.tvz.popovic.chessengine.helper;

import hr.tvz.popovic.chessengine.mapper.FenMapper;
import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.Piece;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PerftTest {

    @ParameterizedTest
    @MethodSource("getSource")
    void shouldReturnCorrectPerftResults(String fen, int depth, int expected) {
        var board = FenMapper.fromFen(fen);
        var result = Perft.runPerft(board, depth);

        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> getSource() {
        return Stream.of(
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 5, 4865609),
                Arguments.of("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1", 4, 4085603),
                Arguments.of("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1", 6, 11030083),
                Arguments.of("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1", 5, 15833292),
                Arguments.of("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8", 5, 89941194),
                Arguments.of("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10", 5, 164075551)
        );
    }

    @Test
    void test() {
        var board = FenMapper.fromFen("8/2k5/8/8/8/4K3/4R3/4R3 w - - 0 1");

        board.getStaticEvaluation();
    }
}