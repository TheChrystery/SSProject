package connect4.model;

public enum Mark {

	E, X, O;

	/*
	 * @ ensures this == Mark.XX ==> \result == Mark.OO; ensures this == Mark.OO
	 * ==> \result == Mark.XX; ensures this == Mark.EM ==> \result == Mark.EM;
	 */
	/**
	 * Returns the other mark.
	 * 
	 * @return the other mark is this mark is not EMPTY or EMPTY
	 */
	public Mark other() {
		if (this == X) {
			return O;
		} else if (this == O) {
			return X;
		} else {
			return E;
		}
	}
}
