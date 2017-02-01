package connect4.model;

import java.util.ArrayList;

public class ComputerPlayerAdv extends Player {

	private ArrayList<Object> possibleMoves;

	private Board currentBoard;

	public ComputerPlayerAdv(String name, Mark mark) {
		// create a computer player with the given mark and strategy;
		super(name, mark);
	}

	@Override
	/**
	 * returns the best playable move according to this AI's ruleset.
	 */
	public int determineMove(Board board) {
		int smartField = -1;
		currentBoard = board.deepCopy();
		boolean found = false;
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		for (int i = 0; i < board.DIM * board.DIM * board.DIM; i++) {
			if (currentBoard.isPlayableField(i)) {
				possibleMoves.add(i);
			}
		}
		for (int i = 0; i < possibleMoves.size() && !found; i++) {
			currentBoard = board.deepCopy();
			currentBoard.setField((int) possibleMoves.get(i), this.mark);
			if (currentBoard.isWinner(this.mark)) {
				smartField = possibleMoves.get(i);
				found = true;
			} else {
				currentBoard.setField(possibleMoves.get(i), this.mark.other());
				if (currentBoard.isWinner(this.mark.other())) {
					smartField = possibleMoves.get(i);
					found = true;
				}
			}
		}
		if (found) {
			return smartField;
		} else {
			double bestRatio = 0.0;
			double localRatio = 0.0;
			for (int i : possibleMoves) {
				int neighbours = board.neighbourFields(i, this.mark);
				int opponents = board.neighbourFields(i, this.mark.other());
				if (opponents == 0) {
					localRatio = neighbours;
				} else {
					localRatio = (double) neighbours / opponents;
				}
				if (localRatio >= bestRatio) {
					bestRatio = localRatio;
					smartField = i;
				}
			}
		}
		if (!(smartField == -1)) {
			return smartField;
		} else {
			int randomIndex = (int) (Math.random() * possibleMoves.size());
			int smartfield = possibleMoves.get(randomIndex);
			return smartField;
		}
	}
}