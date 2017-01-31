package connect4.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client class for a simple client-server application
 * @author Sjoerd Kruijer & Alin Cadariu
 */
public class Client {
    private static final String USAGE = "usage: java week7.cmdline.Client <address> <port>";

    /** Starts a Client application. */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(USAGE);
            System.exit(0);
        }
        
        InetAddress addr = null;
        int port = 0;
        Socket sock = null;

        // check args[0] - the IP-adress
        try {
            addr = InetAddress.getByName(args[0]);
        } catch (UnknownHostException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: host " + args[0] + " unknown");
            System.exit(0);
        }

        // parse args[1] - the port
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: port " + args[1]
            		           + " is not an integer"); 
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
