package connect4.model;

import java.util.*;

public class ComputerPlayer extends Player {

	private ArrayList<Object> possibleMoves;

	public static final int DIM = 4;
	private Board currentBoard;
	
	

	public ComputerPlayer(String name, Mark mark) {
		// create a computer player with the given mark and strategy;
		super(name, mark);
	}

	@Override
	/**
	 * returns a random playable move.
	 */
	public int determineMove(Board board) {
		
		currentBoard = board.deepCopy();
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		for(int i=0; i < DIM*DIM*DIM; i++) {
			if(currentBoard.isPlayableField(i)) {
				possibleMoves.add(i);
			}	
		}
		int index = (int) (Math.random() * possibleMoves.size());
		int move = possibleMoves.get(index);
	    return move; 

	}
}