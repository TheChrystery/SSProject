package ss.connect4;

import java.util.Observable;

/**
 * Board for a 3dimensional connect4 game.
 *
 * @author Sjoerd Kruijer & Alin Cadariu
 * @version 1
 */
public class Board extends Observable {
	public static final int DIM = 4;
	/**
	 * the array that stores the mark of each field of the board.
     */
	private Mark[] fields;

    /**
     * Creates a board, fills fields with the empty mark.
     */
	public Board() {
		fields = new Mark[DIM * DIM * DIM];
		for (int i = 0; 0 <= i && i < DIM * DIM * DIM; i++) {
			fields[i] = Mark.E;
		}
	}

    /**
     * Creates a copy of the board, useful for writing an AI.
     * @return a copy of the Board
     */
	public Board deepCopy() {
		Board b = new Board();
		for (int i = 0; i < DIM * DIM * DIM; i++) {
			b.setField(i, this.getField(i));
		}
		return b;
	}

    /**
     * returns the index corresponding to the (x,y,z) coordinates.
     * @param int x 
     * @param int y 
     * @param int z 
     * @return index, 0 <= index < DIM^3
     */
	public int index(int x, int y, int z) {
		return x + (DIM * y) + (DIM * DIM * z);
	}

	/**
	 * Checks whether an index is valid for this board.
	 * @param int index
	 * @return true if 0 <= index < DIM^3
	 */
	public boolean isField(int index) {
		if (0 <= index && index <= (DIM * DIM * DIM) - 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * returns the Mark occupying field i, if it is a valid field.
	 * @param int index
	 * @return Mark or null
	 */
	public Mark getField(int i) {
		if (isField(i)) {
			return fields[i];
		} else {
			return null;
		}
	}

	/**
	 * overwrites the Mark in fields[i] with m, if it exists.
	 * @param int index
	 * @param Mark m
	 */
	public void setField(int i, Mark m) {
		if (this.isField(i)) {
			fields[i] = m;
		    setChanged();
		    notifyObservers("Board changed");
		}
	}

	/**
	 * Checks whether a field is valid and empty.
	 * @param int index
	 * @return true if fields[i] is empty.
	 */
	public boolean isEmptyField(int i) {
		return isField(i) && getField(i) == Mark.E;
	}

	/**
	 * Checks whether the board is full.
	 * @return true if the board is full.
	 */
	public boolean isFull() {
		for (int i = 0; i < DIM * DIM * DIM; i++) {
			if (!isEmptyField(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * checks whether there is an x-row of Mark m.
	 * @param Mark m
	 * @return true if there is an x-row
	 */
	public boolean hasXRow(Mark m) {
		for (int z = 0; z < DIM; z++) {
			for (int y = 0; y < DIM; y++) {
				int counter = 0;
				for (int x = 0; x < DIM; x++) {
					if (getField(index(x, y, z)) == m) {
						counter++;
					}
				}
				if (counter == 4) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * checks whether there is an y-row of Mark m.
	 * @param Mark m
	 * @return true if there is an y-row
	 */
	public boolean hasYRow(Mark m) {
		for (int z = 0; z < DIM; z++) {
			for (int x = 0; x < DIM; x++) {
				int counter = 0;
				for (int y = 0; y < DIM; y++) {
					if (getField(index(x, y, z)) == m) {
						counter++;
					}
				}
				if (counter == 4) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * checks whether there is an z-row of Mark m.
	 * @param Mark m
	 * @return true if there is an z-row
	 */
	public boolean hasZRow(Mark m) {
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				int counter = 0;
				for (int z = 0; z < DIM; z++) {
					if (getField(index(x, y, z)) == m) {
						counter++;
					}
				}
				if (counter == 4) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks whether Mark m has any kind of row.
	 * @param Mark m
	 * @return true if Mark m has a row
	 */
	public boolean hasRow(Mark m) {
		if (hasXRow(m) || hasYRow(m) || hasZRow(m)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether Mark m has an X Diagonal.
	 * @param Mark m
	 * @return true if Mark m has an X Diagonal
	 */
	public boolean hasXDiagonal(Mark m) {
		for (int x = 0; x < DIM; x++) {
			int counter1 = 0;
			int counter2 = 0;
			for (int yz = 0; yz < DIM; yz++) {
				if (getField(index(x, yz, yz)) == m) {
					counter1++;
				}
				if (getField(index(x, yz, DIM - 1 - yz)) == m) {
					counter2++;
				}
			}
			if (counter1 == 4 || counter2 == 4) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether Mark m has an Y Diagonal.
	 * @param Mark m
	 * @return true if Mark m has an Y Diagonal
	 */
	public boolean hasYDiagonal(Mark m) {
		for (int y = 0; y < DIM; y++) {
			int counter1 = 0;
			int counter2 = 0;
			for (int xz = 0; xz < DIM; xz++) {
				if (getField(index(xz, y, xz)) == m) {
					counter1++;
				}
				if (getField(index(xz, y, DIM - 1 - xz)) == m) {
					counter2++;
				}
			}
			if (counter1 == 4 || counter2 == 4) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether Mark m has an Z Diagonal.
	 * @param Mark m
	 * @return true if Mark m has an Z Diagonal
	 */
	public boolean hasZDiagonal(Mark m) {
		for (int z = 0; z < DIM; z++) {
			int counter1 = 0;
			int counter2 = 0;
			for (int xy = 0; xy < DIM; xy++) {
				if (getField(index(xy, xy, z)) == m) {
					counter1++;
				}
				if (getField(index(xy, DIM - 1 - xy, z)) == m) {
					counter2++;
				}
			}
			if (counter1 == 4 || counter2 == 4) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether Mark m has a True Diagonal; a diagonal to every plane.
	 * @param Mark m
	 * @return true if Mark m has such a diagonal
	 */
	public boolean hasTrueDiagonal(Mark m) {
		int counter1 = 0;
		int counter2 = 0;
		int counter3 = 0;
		int counter4 = 0;
		for (int i = 0; i < DIM; i++) {
			if (getField(index(i, i, i)) == m) {
				counter1++;
			}
			if (getField(index(DIM - 1 - i, i, i)) == m) {
				counter2++;
			}
			if (getField(index(i, DIM - 1 - i, i)) == m) {
				counter3++;
			}
			if (getField(index(i, i, DIM - 1 - i)) == m) {
				counter4++;
			}
		}
		if (counter1 == 4 || counter2 == 4 || counter3 == 4 || counter4 == 4) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether Mark m has any diagonal.
	 * @param Mark m
	 * @return true if Mark m has any diagonal
	 */
	public boolean hasDiagonal(Mark m) {
		if (hasXDiagonal(m) || hasYDiagonal(m) || hasZDiagonal(m) || hasTrueDiagonal(m)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether Mark m has won.
	 * @param Mark m
	 * @return true if Mark m has won
	 */
	public boolean isWinner(Mark m) {
		return hasRow(m) || hasDiagonal(m);
	}

	/**
	 * Checks whether this game has a winner.
	 * @param Mark m
	 * @return true if there is a winner
	 */
	public boolean hasWinner() {
		return isWinner(Mark.X) || isWinner(Mark.O);
	}

	/**
	 * Checks whether this game is over.
	 * @param Mark m
	 * @return true if there is a winner or the board is full
	 */
	public boolean gameOver() {
		if (this.isFull() || this.hasWinner()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * fills fields[] with the empty Mark.
	 */
	public void reset() {
		for (int i = 0; i < DIM * DIM * DIM; i++) {
			this.setField(i, Mark.E);
		}
	}

}
