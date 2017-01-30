package ss.connect4;

public class ServerGame implements Runnable{
	
	public ServerConnection[] players;
	private int current = 0;
	private Board board;
	private TUIView view;

	public ServerGame(ServerConnection conn1, ServerConnection conn2) {
        board = new Board();
        view = new TUIView(board);
		players = new ServerConnection[2];
		players[0] = conn1;
		players[1] = conn2;
		//Sets the current players' status to Playing and removes them from readyClients;
		conn1.connectionStatus = ServerConnection.STATUS.PLAYING;
		conn1.server.readyClients.remove(conn1);
		conn2.connectionStatus = ServerConnection.STATUS.PLAYING;
		conn2.server.readyClients.remove(conn2);
		this.run();
	}

	@Override
	public void run() {
    	players[0].sendMessage("GAME START " + players[0].getName() + " " + players[1].getName());
    	players[1].sendMessage("GAME START " + players[0].getName() + " " + players[1].getName());
    	
        while (!board.isFull() && !board.hasWinner()) {
        	players[current % 2].makeMove(board);
        	current++;
        }
        printResult();
		
	}
	
}
