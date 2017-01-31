package ss.connect4.tests;

import org.junit.Before;
import org.junit.Test;
import connect4.model.Board;
import connect4.model.ComputerPlayer;
import connect4.model.ComputerPlayerAdv;
import connect4.model.HumanPlayer;
import connect4.model.Mark;
import connect4.model.Player;

import static org.junit.Assert.*;

public class ComputerPlayerTest {

		
	private Board board;
	private Mark m;

	@Before
	public void setUp() throws Exception {
	    board = new Board();
	    
	}

	
	@Test
	public void testSmartBlockMove() {
		Player computerPlayer = new ComputerPlayerAdv("AI", Mark.X);
	    
		board.setField(0, Mark.O);
		board.setField(1, Mark.O);
		board.setField(2, Mark.O);
		computerPlayer.makeMove(board);
		assertEquals(Mark.X, board.getField(3));
		
		
	}
	
	
	@Test
	public void testSmartLegalMove() {
		Player computerPlayer = new ComputerPlayerAdv("AI", Mark.X);
		board.setField(0, Mark.O);
		computerPlayer.makeMove(board);
		assertTrue(board.isPlayableField(computerPlayer.determineMove(board)));
		
		
	}
	
	@Test 
	public void testSmartWinningMove() {
		Player computerPlayer = new ComputerPlayerAdv("AI", Mark.X);
		
		board.setField(0, Mark.X);
		board.setField(1, Mark.X);
		board.setField(2, Mark.X);
		computerPlayer.makeMove(board);
		assertEquals(Mark.X, board.getField(3));
		assertTrue(board.isWinner(Mark.X));
		
		
	}
	
	@Test
	public void testNaiveComputerPlayer() {
		
		Player computerPlayer = new ComputerPlayer("AI", Mark.X);
		board.setField(0, Mark.O);
		computerPlayer.makeMove(board);
		assertTrue(board.isPlayableField(computerPlayer.determineMove(board)));
		
	}
	
	@Test 
	public void testMoveIndex() {
		
	
		assertEquals(9, board.moveIndex(1, 2));
		assertEquals(13, board.moveIndex(1, 3));
		
		
	}
	
	
}
