package connect4.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Peer for a simple client-server application
 * 
 * @author Sjoerd Kruijer & Alin Cadariu
 */
public class ServerConnection implements Runnable {
	public static final String EXIT = "exit";

	protected Server server;
	protected String name = "";
	protected String extensions;
	protected boolean ext0 = false;
	protected boolean ext1 = false;
	protected boolean ext2 = false;
	protected Socket sock;
	protected BufferedReader in;
	protected BufferedWriter out;
	protected ServerGame game = null;

	public enum STATUS {
		EMPTY, NAMED, READY, PLAYING
	};

	public STATUS connectionStatus = STATUS.EMPTY;

	/*
	 * @ requires (nameArg != null) && (sockArg != null);
	 */
	/**
	 * Constructor. creates a ServerConnection object based in the given
	 * parameters.
	 * 
	 * @param nameArg
	 *            The Server this is a ServerConnection to
	 */
	public ServerConnection(Server param) throws IOException {
		System.out.println("new ServerConnection established");
		this.server = param;
		this.sock = param.sock.accept();
		this.server.addClient(this);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	/**
	 * Reads strings of the stream of the socket-connection and writes the
	 * characters to the default output.
	 */
	public void run() {
		Scanner input;
		while (!sock.isClosed()) {
			try {
				input = new Scanner(in.readLine());
				if (input.hasNext()) {
					String first = input.next();
					String second = "";
					int third = -1;
					int fourth = -1;
					boolean secondRead = false;
					// legal commands for all stati
					if (first.equals("STATUS")) {
						sendMessage("" + this.connectionStatus);
					} else if (first.equals("DISCONNECT")) {
						this.shutDown();
					} else if (first.equals("PLAYERS")) {
						if (input.hasNext()) {
							second = input.next();
							secondRead = true;
							sendMessage(getExtensions(second));
						}
					} else
					// legal commands for a player with status EMPTY
					if (this.connectionStatus.equals(STATUS.EMPTY)) {
						this.name = first;
						this.connectionStatus = STATUS.NAMED;
						extensions = "";
						if (!secondRead && input.hasNext()) {
							second = input.next();
							secondRead = true;
						}
						if (!second.equals("")) {
							if (second.contains("0")) {
								extensions = extensions + "0";
								ext0 = true;
							}
							if (second.contains("1")) {
								extensions = extensions + "1";
								ext1 = true;
							}
							if (second.contains("2")) {
								extensions = extensions + "2";
								ext2 = true;
							}
						}
						sendMessage("CONFIRM");
					}
					// legal commands beginning with GAME
					if (first.equals("GAME")) {
						if (!secondRead) {
							second = input.next();
						}
						if (this.connectionStatus.equals(STATUS.NAMED) && second.equals("READY")) {
							this.connectionStatus = STATUS.READY;
							this.server.addReadyClient(this);
							// look for opponents. If this method doesn't
							// start a game, it waits for an opponent to
							// start one with this player.
							for (ServerConnection opponent : server.getReadyClients()) {
								if (!opponent.equals(this)) {
									new ServerGame(this, opponent);
									break;
								}
							}
						}
						if (this.connectionStatus.equals(STATUS.READY) && second.equals("UNREADY")) {
							this.connectionStatus = STATUS.NAMED;
							this.server.removeReadyClient(this);
						}
						// PLAYING client makes move.
						if (this.connectionStatus.equals(STATUS.PLAYING)) {
							if (first.equals("GAME") && second.equals("MOVE")) {
								if (input.hasNextInt()) {
									third = input.nextInt();
									if (input.hasNextInt()) {
										fourth = input.nextInt();
										this.game.makeMove(this, third, fourth);
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
	 * Sends the String given as a parameter over the socket-connection to the
	 * Peer process. On ServerConnection.EXIT the Connection is terminated.
	 * 
	 * @param String
	 *            message
	 */
	public void sendMessage(String message) {
		if (!sock.isClosed()) {
			String send = message;
			if (send.equals(EXIT)) {
				this.shutDown();
			} else {
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

	public String getExtensions(String filter) {
		String players = "";
		Set<Integer> filterExts = new HashSet<Integer>();
		for (int i = 0; i < filter.length(); i++) {
			filterExts.add((int) filter.charAt(i));
		}

		if (filter.equals("ALL")) {
			for (int i = 0; i < server.getClients().size(); i++) {
				players = players + " " + server.getClients().get(i).getName();
			}
			return players;
		} else {
			for (ServerConnection conn : this.server.getClients()) {
				int counter = 0;
				Set<Integer> connExts = new HashSet<Integer>();
				for (int j = 0; j < conn.extensions.length(); j++) {
					filterExts.add((int) conn.extensions.charAt(j));
				}
				for (int i : filterExts) {
					if (connExts.contains(i)) {
						counter++;
					}
				}
				if (counter == filter.length()) {
					players = players + " " + conn.getName();
				}
			}
			return players;
		}
	}

	/**
	 * Returns the name of this ServerConnection
	 * 
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the game this ServerConnection is currently playing, if there is
	 * one.
	 * 
	 * @return ServerGame game
	 */
	public ServerGame getGame() {
		return this.game;
	}

	/**
	 * Prints the String given as a parameter on system.out, Reads a string from
	 * system.in and returns it
	 * 
	 * @param String
	 *            tekst
	 * @return String antw
	 */
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