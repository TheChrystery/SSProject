package connect4.networking;

import connect4.model.Board;
import connect4.model.Mark;
import connect4.networking.ServerConnection.STATUS;
import connect4.view.TUIView;

/**
 * 
 * @author Sjoerd Kruijer & Alin Cadariu
 *
 */
public class ServerGame {
	
	public ServerConnection[] players;
	private int current = 0;
	private Board board;
	private TUIView view;
	
	//@ requires conn1 != null && conn2 != null && !conn1.equals(conn2);
	//@ ensures !players[0].equals(null) && !players[1].equals(null);
	//@ ensures players[0].equals(conn1) && players[1].equals(conn2);
	//@ ensures !players[0].equals(players[1]);
	//@ ensures !conn1.server.getReadyClients().contains(conn1) && !conn2.server.getReadyClients.contains(conn2);
	//@ ensures conn1.connectionStatus == conn2.connectionStatus == serverConnection.STATUS.PLAYING; 
	/**
	 * The constructor of a servergame. Takes two serverconnections as parameters, adjusts the players' stati
	 * @param conn1
	 * @param conn2
	 */
	public ServerGame(ServerConnection conn1, ServerConnection conn2) {
        board = new Board();
        view = new TUIView(board);
		players = new ServerConnection[2];
		players[0] = conn1;
		players[1] = conn2;
		//Sets the current players' status to Playing, connects them to this game and removes them from readyClients;
		conn1.connectionStatus = ServerConnection.STATUS.PLAYING;
		conn1.server.removeReadyClient(conn1);
		conn1.game = this;
		conn2.connectionStatus = ServerConnection.STATUS.PLAYING;
		conn2.server.removeReadyClient(conn2);
		conn2.game = this;
		players[0].sendMessage("GAME START " + players[0].getName() + " " + players[1].getName());
    	players[1].sendMessage("GAME START " + players[0].getName() + " " + players[1].getName());
	}
	
	//@ensures !player.equals(players[0]) && !player.equals(players[1] => \result == false;
	/**
	 * A simple method to check whether a given ServerConnection is a player in this game.
	 * @param ServerConnection player
	 * @return true if player, false if not
	 */
	public boolean isPlayer(ServerConnection player) {
		if (players[0].equals(player)) {
			return true;
		} else if (players[1].equals(player)){
			return true;
		} else {
			return false;
		}
	}
	
	//@ensures !player.equals(players[0]) && !player.equals(players[1] => \result == null;
	/**
	 * A simple method to return the opponent of a given player, if that player is in this ServerGame.
	 * @param ServerConnection player
	 * @return ServerConnection opponent
	 */
	public ServerConnection otherPlayer(ServerConnection player) {
		if (players[0].equals(player)) {
			return players[1];
		} else if (players[1].equals(player)){
			return players[0];
		} else {
			return null;
		}
	}
	
	//@ ensures !player.equals(players[0]) && !player.equals(players[1] => \result == false;
	//@ ensures player.equals(null) => \result == false;
	//@ ensures players[current % 2].equals(otherPlayer(player)) => \result == false;
	/**
	 * Returns whether it is currently the given player's turn.
	 * @param player
	 * @return true/false
	 */
	public boolean isTurn(ServerConnection player) {
		if (players[current % 2].equals(player)) {
			return true;
		}
		return false;
	}
	
	/**
	 * return the Mark of player given as a parameter. The starting player always has mark X.
	 * @param player
	 * @return Mark
	 */
	public Mark getMark(ServerConnection player) {
		if (player.equals(players[0])) {
			return Mark.X;
		} else if (player.equals(players[1])) {
			return Mark.O;
		} else {
			return null;
		}
	}
	
	//@ ensures m.equals(Mark.E) => \result == null; 
	/**
	 * Returns the player corresponding to the given Mark in this game.
	 * @param Mark m
	 * @return ServerConnection
	 */
	public ServerConnection getPlayer(Mark m) {
		if (m.equals(Mark.X)) {
			return players[0];
		} else if (m.equals(Mark.O)) {
			return players[1];
		} else {
			return null;
		}
	}
	
	//@ requires players[0].connectionStatus == players[1].connectionStatus == STATUS.PLAYING;
	//@ ensures players[0].connectionStatus == players[1].connectionStatus == STATUS.NAMED;
	/**
	 * Ends the game and kicks out the clients.
	 */
	public void end() {
		players[0].connectionStatus = STATUS.NAMED;
		players[1].connectionStatus = STATUS.NAMED;
	}
	
	
	//@ ensures !isPlayer(player) || !isTurn(Player) || !board.isPlayableField(board.moveIndex(x,y)) => board.fields.equals(\old board.fields);
	//@ ensures board.hasWinner() => players[0].connectionStatus == players[1].connectionStatus == STATUS.PLAYING;
	/**
	 * This method allows players to make moves. It takes a player, and x and y coordinates as parameters. If it is this players turn, 
	 * and (x,y) is a valid move, than the move is played and the players are notified. After each move it also checks whether the game
	 * has ended yet, and notifies the players of the result if so. 
	 * @param player
	 * @param x
	 * @param y
	 */
	public synchronized void makeMove(ServerConnection player, int x, int y) {
		if (isTurn(player) && board.isPlayableField(board.moveIndex(x, y))) {
			board.setField(board.moveIndex(x,y), getMark(player));
			current++;
			if (board.hasWinner()) {
				if (board.isWinner(Mark.X)) {
					players[0].sendMessage("GAME MOVE " + player.getName() + " " + x + " " + y);
					players[1].sendMessage("GAME MOVE " + player.getName() + " " + x + " " + y);
					players[0].sendMessage("GAME END " + players[0].getName());
					players[1].sendMessage("GAME END " + players[0].getName());
					end();
					
				} else if (board.isWinner(Mark.O)) {
					players[0].sendMessage("GAME MOVE " + player.getName() + " " + x + " " + y);
					players[1].sendMessage("GAME MOVE " + player.getName() + " " + x + " " + y);
					players[0].sendMessage("GAME END " + players[1].getName());
					players[1].sendMessage("GAME END " + players[1].getName());
					end();
				}
			} else if (board.isFull()) {
				players[0].sendMessage("GAME MOVE " + player.getName() + " " + x + " " + y);
				players[1].sendMessage("GAME MOVE " + player.getName() + " " + x + " " + y);
				players[0].sendMessage("GAME END DRAW");
				players[1].sendMessage("GAME END DRAW");
				end();
			} else {
				players[0].sendMessage("GAME MOVE " + player.getName() + " " + x + " " + y + " " + otherPlayer(player).getName());
				players[1].sendMessage("GAME MOVE " + player.getName() + " " + x + " " + y + " " + otherPlayer(player).getName());
			}
		}
	}
	
}
 