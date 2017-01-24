package ss.connect4;

import ss.connect4.Board;
import ss.connect4.Mark;
import java.util.*;

public class ComputerPlayer extends Player {

	private ArrayList<Object> emptyFields;

	public static final int DIM = 4;
	private Board currentBoard;
	private int smartField;
	private boolean found;

	public ComputerPlayer(String name, Mark mark) {
		// create a computer player with the given mark and strategy;
		super(name, mark);

	}

	@Override
	/**
	 * returns the best playable move according to this AI's ruleset.
	 */
	public int determineMove(Board board) {
		// TODO Auto-generated method stub

		currentBoard = board.deepCopy();
		// if(currentBoard.isEmptyField(4) == true) {
		// smartField = 4;
		// }
		// else {
		emptyFields = new ArrayList<>();
		for (int i = 0; i < DIM * DIM * DIM; i++) {
			if (currentBoard.getField(i) == Mark.E) {
				emptyFields.add(i);
			}
		}
		found = false;
		for (int i = 0; i < emptyFields.size() && !found; i++) {
			currentBoard = board.deepCopy();
			currentBoard.setField((int) emptyFields.get(i), this.mark);
			if (currentBoard.hasDiagonal(this.mark) || currentBoard.hasRow(this.mark)) {
				smartField = (int) emptyFields.get(i);
				found = true;
			} else {
				currentBoard.setField((int) emptyFields.get(i), this.mark.other());
				if (currentBoard.hasDiagonal(this.mark.other()) || currentBoard.hasRow(this.mark.other())) {
					smartField = (int) emptyFields.get(i);
					found = true;
				}
			}
		}
		// if(!found){
		// look for 1 or 2 on the same row, diagonal or column

		// }

		if (!found) {
			smartField = (int) (Math.random() * emptyFields.size() + 1);

		}
		return smartField;

	}
}