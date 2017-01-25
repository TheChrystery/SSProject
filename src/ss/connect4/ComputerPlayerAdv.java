package ss.connect4;

import java.util.ArrayList;

import ss.connect4.Board;
import ss.connect4.Mark;



public class ComputerPlayerAdv extends Player {

	
	private ArrayList<Object> possibleMoves;
	
    public static final int DIM = 4;
	private Board currentBoard;
	private int smartField;
	private boolean found;
	
	
	public ComputerPlayerAdv(String name, Mark mark) {
		 //create a computer player with the given mark and strategy;
		super(name, mark);
	}
	
	@Override
	/**
	 * returns the best playable move according to this AI's ruleset.
	 */
	public int determineMove(Board board) {
	
		
		currentBoard = board.deepCopy();
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
			for(int i=0; i < DIM*DIM*DIM; i++) {
				if(currentBoard.isPlayableField(i)) {
					possibleMoves.add(i);
				}	
			}
			
		found = false;
		for(int i=0; i < possibleMoves.size() && !found; i++) {
			currentBoard = board.deepCopy();
			currentBoard.setField((int)possibleMoves.get(i), this.mark);
			if(currentBoard.hasDiagonal(this.mark) || currentBoard.hasRow(this.mark)) {
				smartField = (int) possibleMoves.get(i);
				found = true;
			}
			else {
				currentBoard.setField((int)possibleMoves.get(i), this.mark.other());
				if(currentBoard.hasDiagonal(this.mark.other()) || currentBoard.hasRow(this.mark.other())) {
					smartField = (int) possibleMoves.get(i); 
					found = true;
				}
			}
		}

			// look for 1 or 2 on the same row, diagonal or column
			
		if(!found) {
		smartField = possibleMoves.get((int)(Math.random() * possibleMoves.size()));
		
	    }
	    return smartField;
	
	}
}