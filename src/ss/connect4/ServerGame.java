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
		//Sets the current players' status to Playing, connects them to this game and removes them from readyClients;
		conn1.connectionStatus = ServerConnection.STATUS.PLAYING;
		conn1.server.readyClients.remove(conn1);
		conn1.game = this;
		conn2.connectionStatus = ServerConnection.STATUS.PLAYING;
		conn2.server.readyClients.remove(conn2);
		conn2.game = this;
		this.run();
	}

	@Override
	public void run() {
    	players[0].sendMessage("GAME START " + players[0].getName() + " " + players[1].getName());
    	players[1].sendMessage("GAME START " + players[0].getName() + " " + players[1].getName());
	}
	
	public boolean isTurn(ServerConnection player) {
		if (players[current % 2].equals(player)) {
			return true;
		}
		return false;
	}
	
	public Mark getMark(ServerConnection player) {
		if (player.equals(players[0])) {
			return Mark.X;
		} else if (player.equals(players[1])) {
			return Mark.O;
		} else {
			return null;
		}
	}
	
	public ServerConnection getPlayer(Mark m) {
		if (m.equals(Mark.X)) {
			return players[0];
		} else if (m.equals(Mark.O)) {
			return players[1];
		} else {
			return null;
		}
	}
	
	/**
	 * Ends the game and kicks out the clients.
	 */
	public void end() {
		//future implementation.
	}
	
	public void makeMove(ServerConnection player, int x, int y) {
		if (isTurn(player) && board.isPlayableField(board.moveIndex(x, y))) {
			board.setField(board.moveIndex(x,y), getMark(player));
			current++;
			if (board.hasWinner()) {
				if (board.isWinner(Mark.X)) {
					players[0].sendMessage("GAME END " + players[0].getName());
					players[1].sendMessage("GAME END " + players[0].getName());
				} else if (board.isWinner(Mark.O)) {
					players[0].sendMessage("GAME END " + players[1].getName());
					players[1].sendMessage("GAME END " + players[1].getName());
				}
			} else if (board.isFull()) {
				players[0].sendMessage("GAME END DRAW");
				players[1].sendMessage("GAME END DRAW");
			}
		}
	}
	
}
 