package connect4.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import connect4.model.Board;
import connect4.model.Mark;
import connect4.view.TUIView;

/**
 * Peer for a simple client-server application
 * 
 * @author Sjoerd Kruijer & Alin Cadariu
 */
public class ClientConnection implements Runnable {
	public static final String EXIT = "exit";
	protected Socket sock;
	protected BufferedReader in;
	protected BufferedWriter out;
	private Board board;
	private TUIView view;
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
					if (input.hasNext()) {
						second = input.next();
						if (first.equals("GAME") && second.equals("START") && input.hasNext()) {
							player1name = input.next();
							if (input.hasNext()) {
								player2name = input.next();
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
			if (send.equals(EXIT)) {
				this.shutDown();
			} else if (s.hasNext()) {
				String first = s.next();
				if (first.equals("CONNECT") && s.hasNext()) {
					String second = s.next();
					System.out.println("hello " + second);
					this.name = second;
				}
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
