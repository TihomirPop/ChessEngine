package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorUtilsTest {

    @Test
    void shouldReturnTrueIfIndexIsInBounds() {
        assertTrue(GeneratorUtils.isIndexInBounds(0));
        assertTrue(GeneratorUtils.isIndexInBounds(63));
        assertTrue(GeneratorUtils.isIndexInBounds(35));
    }

    @Test
    void shouldReturnFalseIfIndexIsNotInBounds() {
        assertFalse(GeneratorUtils.isIndexInBounds(-1));
        assertFalse(GeneratorUtils.isIndexInBounds(64));
        assertFalse(GeneratorUtils.isIndexInBounds(100));
    }

    @Test
    void shouldReturnTrueIfPieceOnIndexIsNotFriendly() {
        var board = Board.createInitialBoard();
        assertTrue(GeneratorUtils.isPieceOnIndexNotFriendly(board, 0));
        assertTrue(GeneratorUtils.isPieceOnIndexNotFriendly(board, 15));
        assertTrue(GeneratorUtils.isPieceOnIndexNotFriendly(board, 16));
    }

    @Test
    void shouldReturnFalseIfPieceOnIndexIsFriendly() {
        var board = Board.createInitialBoard();
        assertFalse(GeneratorUtils.isPieceOnIndexNotFriendly(board, 63));
        assertFalse(GeneratorUtils.isPieceOnIndexNotFriendly(board, 48));
    }

}