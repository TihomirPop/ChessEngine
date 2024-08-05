package hr.tvz.popovic.chessengine.generator;

import hr.tvz.popovic.chessengine.model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest {

    @Test
    void shouldReturnTrueIfIndexIsInBounds() {
        assertTrue(Generator.isIndexInBounds(0));
        assertTrue(Generator.isIndexInBounds(63));
        assertTrue(Generator.isIndexInBounds(35));
    }

    @Test
    void shouldReturnFalseIfIndexIsNotInBounds() {
        assertFalse(Generator.isIndexInBounds(-1));
        assertFalse(Generator.isIndexInBounds(64));
        assertFalse(Generator.isIndexInBounds(100));
    }

    @Test
    void shouldReturnTrueIfPieceOnIndexIsNotFriendly() {
        var board = Board.createInitialBoard();
        assertTrue(Generator.isPieceOnIndexNotFriendly(board, 0));
        assertTrue(Generator.isPieceOnIndexNotFriendly(board, 15));
        assertTrue(Generator.isPieceOnIndexNotFriendly(board, 16));
    }

    @Test
    void shouldReturnFalseIfPieceOnIndexIsFriendly() {
        var board = Board.createInitialBoard();
        assertFalse(Generator.isPieceOnIndexNotFriendly(board, 63));
        assertFalse(Generator.isPieceOnIndexNotFriendly(board, 48));
    }

}