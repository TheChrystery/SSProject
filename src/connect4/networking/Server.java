
package connect4.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Server.
 * 
 * @author Sjoerd Kruijer & Alin Cadariu
 * @version 2005.02.21
 */
public class Server {
	// list of the serverConnections of all connected clients.
	private ArrayList<ServerConnection> clients;
	// list of the serverConnections of all connected clients with STATUS READY.
	private ArrayList<ServerConnection> readyClients;
	private static final String USAGE = "usage: " + Server.class.getName() + " <name> <port>";
	public ServerSocket sock;
	private Lock clientsLock;
	private Lock readyClientsLock;

	public Server(int port) {
		// initialize the clientArrays and locks
		clients = new ArrayList<ServerConnection>();
		clientsLock = new ReentrantLock();
		readyClients = new ArrayList<ServerConnection>();
		readyClientsLock = new ReentrantLock();
		// try to open a Socket to the server
		try {
			sock = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("ERROR: could not create a socket on " + " port " + port);
		}
	}
	
	//@ requires @param != null;
    //@ ensures \result != null;
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

	public ArrayList<ServerConnection> getClients() {
		clientsLock.lock();
		try {
			return clients;
		} finally {
			clientsLock.unlock();
		}
	}

	public void addClient(ServerConnection s) {
		clientsLock.lock();
		try {
			clients.add(s);
		} finally {
			clientsLock.unlock();
		}
	}

	public void removeClient(ServerConnection s) {
		clientsLock.lock();
		try {
			clients.remove(s);
		} finally {
			clientsLock.unlock();
		}
	}

	public ArrayList<ServerConnection> getReadyClients() {
		readyClientsLock.lock();
		try {
			return readyClients;
		} finally {
			readyClientsLock.unlock();
		}
	}

	public void addReadyClient(ServerConnection s) {
		readyClientsLock.lock();
		try {
			readyClients.add(s);
		} finally {
			readyClientsLock.unlock();
		}
	}

	public void removeReadyClient(ServerConnection s) {
		readyClientsLock.lock();
		try {
			readyClients.remove(s);
		} finally {
			readyClientsLock.unlock();
		}
	}

	
	
	/** Starts a Server-application. */
	public static void main(String[] args) {
		String portString = readString("Server port number?");
		int port = 0;
		ServerSocket sock = null;
		// parse portString - the port
		try {
			port = Integer.parseInt(portString);
		} catch (NumberFormatException e) {
			System.out.println(USAGE);
			System.out.println("ERROR: port " + portString + " is not an integer");
			System.exit(0);
		}
		Server server = new Server(port);
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
