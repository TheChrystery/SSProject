
package ss.connect4;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Server.
 * 
 * @author Theo Ruys
 * @version 2005.02.21
 */
public class Server {
	public ArrayList<ServerConnection> clients; 
	private static final String USAGE = "usage: " + Server.class.getName() + " <name> <port>";
	public ServerSocket sock;
	private String name;
	
	public Server(String name, int port) {
		// try to open a Socket to the server
		this.name = name;
		clients = new ArrayList<ServerConnection>();
		try {
			sock = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("ERROR: could not create a socket on " + " port " + port);
		}
	}
	/** Starts a Server-application. */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println(USAGE);
			System.exit(0);
		}
		String name = args[0];
		int port = 0;
		ServerSocket sock = null;
		// parse args[1] - the port
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println(USAGE);
			System.out.println("ERROR: port " + args[1] + " is not an integer");
			System.exit(0);
		}
		Server server = new Server(name, port);
		// create connections and start communication
		while (true) {
			try {
				ServerConnection clientConnection = new ServerConnection(server);
				Thread streamInputHandler = new Thread(clientConnection);
				streamInputHandler.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
} // end of class Server
