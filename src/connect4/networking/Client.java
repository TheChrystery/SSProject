package connect4.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client class for a simple client-server application
 * @author Sjoerd Kruijer & Alin Cadariu
 */
public class Client {
    private static final String USAGE = "usage: java week7.cmdline.Client <address> <port>";
    
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

    /** Starts a Client application. */
    public static void main(String[] args) {
        /*if (args.length != 2) {
            System.out.println(USAGE);
            System.exit(0);
        }*/
        
        InetAddress addr = null;
        int port = 0;
        Socket sock = null;
        String addrString = readString("server IP-adress? type 'self' for your own IP.");
        String portString = readString("server port number?");
        if (addrString.equals("self")) {
        	addrString = "127.0.0.1";
        }
        // check addrString - the IP-adress
        try {
            addr = InetAddress.getByName(addrString);
        } catch (UnknownHostException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: host " + addrString + " unknown");
            System.exit(0);
        }
        // parse portString - the port
        try {
            port = Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: port " + portString + " is not an integer"); 
            System.exit(0);
        }

        // try to open a Socket to the server
        try {
            sock = new Socket(addr, port);
        } catch (IOException e) {
            System.out.println("ERROR: could not create a socket on " + addr
                    + " and port " + port);
        }

        // create ClientConnection object and start the two-way communication
        try {
            ClientConnection clientConn = new ClientConnection(sock);
            Thread streamInputHandler = new Thread(clientConn);
            streamInputHandler.start();
            clientConn.handleTerminalInput();
            clientConn.shutDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} // end of class Client
