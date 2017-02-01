package connect4.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import connect4.model.Board;
import connect4.model.ComputerPlayer;
import connect4.model.ComputerPlayerAdv;
import connect4.model.Mark;
import connect4.view.TUIView;

/**
 * Peer for a simple client-server application
 * 
 * @author Sjoerd Kruijer & Alin Cadariu
 */
public class ClientConnection implements Runnable {
	protected Socket sock;
	protected BufferedReader in;
	protected BufferedWriter out;
	private Board board;
	private TUIView view;
	private ComputerPlayerAdv smartComp;
	private ComputerPlayer naiveComp;
	private String name;
	private String player1name;
	private Mark player1mark = Mark.X;
	private String player2name;
	private Mark player2mark = Mark.O;

	/*
	 * @ requires (nameArg != null) && (sockArg != null);
	 */
	/**
	 * Constructor. creates a peer object based in the given parameters.
	 * 
	 * @param nameArg
	 *            name of the Peer-proces
	 * @param sockArg
	 *            Socket of the Peer-proces
	 */
	public ClientConnection(Socket sock) throws IOException {
		this.sock = sock;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		board = new Board();
		view = new TUIView(board);
	}

	/**
	 * Reads strings of the stream of the socket-connection and writes the
	 * characters to the default output.
	 */
	public void run() {
		while (!sock.isClosed()) {
			try {
				String s = in.readLine();
				System.out.println(s);
				Scanner input = new Scanner(s);
				String first = "";
				String second = "";
				String third = "";
				if (input.hasNext()) {
					first = input.next();
					if (first.equals("CONFIRM")) {
						System.out.println("Type 'GAME READY' to be assigned a random game ASAP.");
					} else if (input.hasNext()) {
						second = input.next();
						if (first.equals("GAME") && second.equals("START") && input.hasNext()) {
							player1name = input.next();
							if (input.hasNext()) {
								player2name = input.next();
								System.out.println("Type 'GAME MOVE x y' to place your mark in the lowest available Z-plane when it is your turn.\n"
										+ "When a move is made, the Server sends 'GAME MOVE player x y otherplayer', indicating 'player' just played (x,y),\n"
										+ "and it is now 'otherplayer's' turn. Type help at any time for more details");
								// the field 0 is already empty, but invoking
								// this method will print the board once.
								board.playField(0, Mark.E);
								if (player1name.equals(name)) {
									this.smartComp = new ComputerPlayerAdv("smartComp", Mark.X);
									this.naiveComp = new ComputerPlayer("naiveComp", Mark.X);
								} else {
									this.smartComp = new ComputerPlayerAdv("smartComp", Mark.O);
									this.naiveComp = new ComputerPlayer("naiveComp", Mark.O);
								}
							}
						} else if (second.equals("MOVE") && input.hasNext()) {
							third = input.next();
							if (input.hasNextInt()) {
								int x = input.nextInt();
								if (input.hasNextInt()) {
									int y = input.nextInt();
									if (third.equals(player1name)) {
										board.playField(board.moveIndex(x, y), player1mark);
									} else if (third.equals(player2name)) {
										board.playField(board.moveIndex(x, y), player2mark);
									}
								}
							}
						}
					}
				}
			} catch (IOException e) {
				this.shutDown();
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads a string from the console and sends this string over the
	 * socket-connection to the Peer process. On Peer.EXIT the method ends
	 */
	public void handleTerminalInput() {
		System.out.println("Connection to Server established");
		while (!sock.isClosed()) {
			String send = readString("");
			Scanner s = new Scanner(send);
			if (s.hasNext()) {
				String first = s.next();
				if (first.equals("CONNECT") && s.hasNext()) {
					String second = s.next();
					if (second.equals("me")) {
						send = "CONNECT TheMemeParty";
					} else {
						System.out.println("hello " + second);
						this.name = second;
					}
				} else if (first.equals("HELP") || first.equals("help")) {
					System.out.println(
							"\nAfter connecting to the server, the command 'CONNECT playername' declares your name to the server.\n"
									+ "afterwards 'GAME READY' tries to start a new game with another waiting ready player. If there is none,\n "
									+ "the game will start as soon as there is another ready player. When in game, use 'GAME MOVE x y' to play that move.\n"
									+ "the game will continue until the board has a winner, is full, or a client disconnects. When ready but not in game,\n"
									+ "and you wish not to be assigned a random game, use 'GAME UNREADY'\n"
									+ "to disconnect use 'DISCONNECT at any time. You can also request the list of all connected players using\n"
									+ "'PLAYERS ALL', or narrow your search using 'PLAYERS extension(s)'\n");
				}
			}
			if (send.equals("SMARTMOVE")) {
				int moveIndex = smartComp.determineMove(board);
				int[] movexy = new int[] { board.coordinates(moveIndex)[0], board.coordinates(moveIndex)[1] };
				send = "GAME MOVE " + movexy[0] + " " + movexy[1];
				System.out.println("smartmove: " + movexy[0] + movexy[1]);
			}
			if (send.equals("RANDOMMOVE")) {
				int moveIndex = naiveComp.determineMove(board);
				int[] movexy = new int[] { board.coordinates(moveIndex)[0], board.coordinates(moveIndex)[1] };
				send = "GAME MOVE " + movexy[0] + " " + movexy[1];
				System.out.println("random move: " + movexy[0] + movexy[1]);
			}
			if (send.equals("DISCONNECT")) {
				this.shutDown();
			}
			try {
				out.write(send);
				out.newLine();
				out.flush();
			} catch (IOException e) {
				this.shutDown();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * Closes the connection, the sockets will be terminated
	 */
	public void shutDown() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** read a line from the default input */
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
}
