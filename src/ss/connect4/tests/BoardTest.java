package ss.connect4.tests;

import org.junit.Before;
import org.junit.Test;

import connect4.model.Board;
import connect4.model.ComputerPlayer;
import connect4.model.HumanPlayer;
import connect4.model.Mark;
import connect4.model.Player;

import static org.junit.Assert.*;

public class BoardTest {

	
	private Board board;
	private Mark m;

    @Before
    public void setUp() throws Exception {
        board = new Board();
    }


    @Test
    public void testIsField() {
        assertFalse(board.isField(-1));
        assertTrue(board.isField(0));
        assertTrue(board.isField(8));
        assertFalse(board.isField(100));
    }
    
	
    @Test
    public void testSettingFields() {
    	board.setField(3, Mark.O);
    	board.setField(4, Mark.X);
    	assertEquals(Mark.O, board.getField(3));
    	assertEquals(Mark.X, board.getField(4));
    	assertEquals(Mark.E, board.getField(13));
    	
    }
    
    @Test
    public void testIsEmptyField() {
    	board.setField(10, Mark.X);
    	assertFalse(board.isEmptyField(10));
    	assertTrue(board.isEmptyField(12));
    	assertTrue(board.isEmptyField(0));
    	
    }
    
    @Test
    public void testAreNeighbours() {
    		
    	assertTrue(board.areNeighbours(0, 1));
    	assertTrue(board.areNeighbours(0, 4));
    	assertFalse(board.areNeighbours(1, 30));
    	assertFalse(board.areNeighbours(15, 3));
    	
    }
    
	@Test
	public void testIsFull() {
		
		assertFalse(board.isFull());
		for (int i = 0; i < Board.DIM * Board.DIM * Board.DIM; i++) {
			board.setField(i, Mark.O);
		}
		assertTrue(board.isFull());
	}
	
    @Test
    public void testHasRow() {
    	
    	assertFalse(board.hasRow(Mark.X));
    	board.setField(0, Mark.X);
    	board.setField(1, Mark.X);
    	board.setField(2, Mark.X);
    	assertFalse(board.hasRow(Mark.X));
    	board.setField(0, Mark.O);
    	board.setField(1, Mark.O);
    	board.setField(2, Mark.O);
    	board.setField(3, Mark.O);
    	assertTrue(board.hasRow(Mark.O));
    	
    }
	
	@Test
	public void testHasDiagonal() {
		
		assertFalse(board.hasDiagonal(Mark.X));
		board.setField(0, Mark.X);
    	board.setField(5, Mark.X);
    	board.setField(10, Mark.X);
		assertFalse(board.hasDiagonal(Mark.X));
		board.setField(0, Mark.O);
    	board.setField(5, Mark.O);
    	board.setField(10, Mark.O);
    	board.setField(15, Mark.O);
    	assertTrue(board.hasDiagonal(Mark.O));
		
	}
    
	@Test
	public void testHasTrueDiagonal() {
		
		assertFalse(board.hasTrueDiagonal(Mark.X));
		board.setField(0, Mark.X);
    	board.setField(21, Mark.X);
    	board.setField(42, Mark.X);
		assertFalse(board.hasDiagonal(Mark.X));
		board.setField(0, Mark.O);
    	board.setField(21, Mark.O);
    	board.setField(42, Mark.O);
    	board.setField(63, Mark.O);
    	assertTrue(board.hasDiagonal(Mark.O));
		
	}
	
    @Test
    public void testHasWinner() {
    	assertFalse(board.hasWinner());
    	board.setField(0, Mark.O);
    	board.setField(21, Mark.O);
    	board.setField(42, Mark.O);
    	board.setField(63, Mark.O);
    	assertTrue(board.hasWinner());
    	
    }
	
	@Test
	public void testNeighbourFields() {
		
		board.setField(0, Mark.O);
		board.setField(4, Mark.O);
		board.setField(1, Mark.O);
		board.setField(30, Mark.X);
		board.setField(29,Mark.X);
		assertEquals(3, board.neighbourFields(0, Mark.O));
		assertEquals(0, board.neighbourFields(0, Mark.X));
		assertEquals(2, board.neighbourFields(30, Mark.X));
	}
	
    
	@Test
	public void testIsPlayable() {
		
		assertTrue(board.isPlayableField(5));
		board.setField(0, Mark.X);
		board.setField(16, Mark.O);
		assertTrue(board.isPlayableField(32));
		assertFalse(board.isPlayableField(48));
		
	}
    
	
	
}
