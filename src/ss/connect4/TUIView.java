package ss.connect4;

import java.util.Observable;
import java.util.Observer;

public class TUIView implements Observer {
	
	private Board board;
	private static final String[] NUMBERING = {" 0 | x1| x2| x3 ", "---+---+---+---", " y1| - | - | - ", 
		"---+---+---+---", " y2| - | - | - ", "---+---+---+---", " y3| - | - | - " };
	private static final String LINE = NUMBERING[1];
	private static final String DELIM = "     ";
	
	public TUIView(Board b) {
		this.board = b;
		board.addObserver(this);
	}
	
	public String toString() {
		String b = "";
		for (int z = 0; z < board.DIM; z++) {
			String s = "";
			for (int x = 0; x < board.DIM; x++) {
				String row = "";
				for (int y = 0; y < board.DIM; y++) {
					row = row + " " + board.getField(board.index(x, y, z)).toString() + " ";
					if (y < board.DIM - 1) {
						row = row + "|";
					}
				}
				s = s + row + DELIM + NUMBERING[x * 2];
				if (x < board.DIM - 1) {
					s = s + "\n" + LINE + DELIM + NUMBERING[x * 2 + 1] + "\n";
				}
				if (x == board.DIM - 1) {
					s = s + " z = " + z;
				}
			}
			b = b + "\n\n" + s;
		}
		return b;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println(this.toString());
		
	}
	
	/*public static void main(String[] args) {
		Board b = new Board();
		TUIView v = new TUIView(b);
		System.out.print(v);
	}*/
}
