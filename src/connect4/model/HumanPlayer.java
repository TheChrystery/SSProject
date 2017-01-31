package connect4.model;

import java.util.Scanner;

/**
 * Class for maintaining a human player in Tic Tac Toe. Module 2 lab assignment
 * 
 * @author Theo Ruys
 * @version $Revision: 1.4 $
 */
public class HumanPlayer extends Player {

	// -- Constructors -----------------------------------------------

	/**
	 * Creates a new human player object.
	 * 
	 */
	public HumanPlayer(String name, Mark mark) {
		super(name, mark);
	}

	// -- Commands ---------------------------------------------------


	/**
	 * Asks the user to input two integers for x and y, 
	 * done using the standard input/output. \
	 * 
	 * @param board
	 *            the game board
	 * @return the playable field corresponding to the input
	 */
	public int determineMove(Board board) {
		int[] xy = readInts("Player " + getName() + ":" + getMark().toString() + " what is your x y ?");
		int index = board.moveIndex(xy[0], xy[1]);
		boolean valid = board.isPlayableField(index);
		while (!valid) {
			System.out.println("ERROR: " + xy[0] + ", " + xy[1] + " is not a valid choice.");
			xy = readInts("Player " + getName() + ":" + getMark().toString() + " what is your move x y ?");
			index = board.moveIndex(xy[0], xy[1]);
			valid = board.isPlayableField(index);
		}
		return index;
	}

	/**
	 * Writes a prompt to standard out and tries to read two int values from
	 * standard in. This is repeated until two int values are entered.
	 * 
	 * @param prompt
	 *            the question to prompt the user
	 * @return an array of the int values entered by the user
	 */
	private int[] readInts(String prompt) {
		int[] values = new int[2];
		boolean intRead1 = false;
		boolean intRead2 = false;
		Scanner line = new Scanner(System.in);
		System.out.print(prompt);
		String s = line.nextLine();
		Scanner scannerLine = new Scanner(s);
		if (s.equals("help") || s.equals("HELP")) {
			System.out.println("\nChoose an x and a y. Your mark will be played in the first legal (x,y,z), if one exists. Players take turns.\n"
					+ "You win the game when you connect 4 of your marks in either a straight or diagonal line in any direction. \n"
					+ "if the board is full, i.e. there are no legal moves left to play, the game ends in a draw.\n");
			return new int[]{-1, -1};
		} else {
			while (!intRead1) {
			if (scannerLine.hasNextInt()) {
				intRead1 = true;
				values[0] = scannerLine.nextInt();
				}
			}
		while (!intRead2) {
			if (scannerLine.hasNextInt()) {
				intRead2 = true;
				values[1] = scannerLine.nextInt();
				}
			}
		return values;
		}
	}
}
