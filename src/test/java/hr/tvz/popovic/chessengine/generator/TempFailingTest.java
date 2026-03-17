package hr.tvz.popovic.chessengine.generator;

import org.junit.jupiter.api.Test;

public class TempFailingTest {

    @Test
    public void shouldFail() {
        throw new RuntimeException("This test is expected to fail");
    }

}
