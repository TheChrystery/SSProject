package connect4.view;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import connect4.model.Board;
import connect4.model.Mark;

public class TUIView implements Observer {

	private Board board;
	private static final String RowSeparator = "     ---+---+---+---    ---+---+---+---    ---+---+---+---    ---+---+---+---"
			+ "     -----+-----+-----+-----";
	private static final String Separator = "      ";
	private static final String Zline = "          z = 1              z = 2              z = 3              z = 4";

	public TUIView(Board b) {
		this.board = b;
		board.addObserver(this);
	}

	/**
	 * Constructs a string based on the board this is the view of and returns it. 
	 * @return String board
	 */
	public String toString() {
		String b = "";
		for (int y = 0; y < board.DIM; y++) {
			String row = "";
			for (int z = 0; z < board.DIM; z++) {
				int xcounter = 0;
				for (int x = 0; x < board.DIM; x++) {
					if (x == 0) {
						row = row + Separator + board.getField(board.getIndex(x, y, z)).toString();
						xcounter ++;
					} else if (z == 3 && x == 3) {
						row = row + " | " + board.getField(board.getIndex(x, y, z)).toString() 
						+ Separator + "(0," + y + ")" + "|" + "(1," + y + ")" + "|" + "(2," + y + ")" + "|" + "(3," + y + ")";
					} else {
						row = row + " | " + board.getField(board.getIndex(x, y, z)).toString();
					}
				}
			}
			if (b.equals("")) {
				b = "\n" + row;
			} else {
				b = b + "\n" + RowSeparator + "\n" + row;
			}
		}
		b = b + "\n" + Zline;
		return b + "\n";
	}

	@Override
	/**
	 * will print the view whenever the method playField of board is invoked.
	 */
	public void update(Observable arg0, Object arg1) {
		System.out.println(this.toString());

	}
}
