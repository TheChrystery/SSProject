package connect4.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import connect4.model.Board;
import connect4.model.ComputerPlayerAdv;
import connect4.model.HumanPlayer;
import connect4.model.Mark;
import connect4.model.Player;
import connect4.view.TUIView;

/**
 * Class for maintaining the connect4 game.
 * 
 * @author Sjoerd Kruijer & Alin Cadariu
 */
public class SimpleGame {

    // --- Instance variables -----------------------------------------

    public static final int NUMBER_PLAYERS = 2;

    /*@
       private invariant board != null;
     */
    /**
     * The board.
     */
    private Board board;
    
    /**
     * The view.
     */
    private TUIView view;

    /*@
       private invariant players.length == NUMBER_PLAYERS;
       private invariant (\forall int i; 0 <= i && i < NUMBER_PLAYERS; players[i] != null); 
     */
    /**
     * The 2 players of the game.
     */
    private Player[] players;

    /*@
       private invariant 0 <= current  && current < NUMBER_PLAYERS;
     */
    /**
     * Index of the current player.
     */
    private int current;

    // -- Constructors -----------------------------------------------

    /*@
      requires s0 != null;
      requires s1 != null;
     */
    /**
     * Creates a new Game object.
     * 
     * @param s0
     *            the first player
     * @param s1
     *            the second player
     */
    public SimpleGame(Player s0, Player s1) {
        board = new Board();
        view = new TUIView(board);
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
    }

    // -- Commands ---------------------------------------------------

    /**
     * Starts the connect4 game. <br>
     * Asks after each ended game if the user want to continue. Continues until
     * the user does not want to play anymore.
     */
    public void start() {
        boolean continues = true;
        while (continues) {
            reset();
            play();
            continues = readBoolean("\n> Play another time? (y/n)?", "y", "n");
        }
    }

    /**
     * Prints a question which can be answered by yes (true) or no (false).
     * After prompting the question on standard out, this method reads a String
     * from standard in and compares it to the parameters for yes and no. If the
     * user inputs a different value, the prompt is repeated and te method reads
     * input again.
     * 
     * @parom prompt the question to print
     * @param yes
     *            the String corresponding to a yes answer
     * @param no
     *            the String corresponding to a no answer
     * @return true is the yes answer is typed, false if the no answer is typed
     */
    private boolean readBoolean(String prompt, String yes, String no) {
        String answer;
        do {
            System.out.print(prompt);
            try (Scanner in = new Scanner(System.in)) {
                answer = in.hasNextLine() ? in.nextLine() : null;
            }
        } while (answer == null || (!answer.equals(yes) && !answer.equals(no)));
        return answer.equals(yes);
    }
    
    /** read a line from the default input */
    //@ requires @param != null;
    //@ ensures \result != null;
	static public String readString(String tekst) {
		System.out.print(tekst);
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}
		return (antw == null) ? "" : antw;
	}

    /**
     * Resets the game. <br>
     * The board is emptied and player[0] becomes the current player.
     */
    private void reset() {
        current = 0;
        board.reset();
    }

    /**
     * Plays the connect4 game. <br>
     * First the (still empty) board is shown. Then the game is played until it
     * is over. Players can make a move one after the other. After each move,
     * the changed game situation is printed.
     */
    private void play() {
    	System.out.println(this.view.toString() + "\n");
    	int i = 0;
        while (!board.isFull() && !board.hasWinner()) {
        	players[i % 2].makeMove(board);
        	i++;
        }
        printResult();
    }

    /**
     * Prints the result of the last game. <br>
     */
    private void printResult() {
        if (board.hasWinner()) {
            Player winner = board.isWinner(players[0].getMark()) ? players[0] : players[1];
            System.out.println("Player " + winner.getName() + " ("
                    + winner.getMark().toString() + ") has won!");
        } else if (board.isFull()) {
            System.out.println("Draw. There is no winner!");
        }
    }
    
    public static void main(String[] args) {
    	String player1name = readString("Player 1 name?");
    	String player2name = readString("Player 2 name?");
    	SimpleGame test = new SimpleGame(new HumanPlayer(player1name, Mark.X), new HumanPlayer(player2name, Mark.O));
    	test.start();
    }
}
