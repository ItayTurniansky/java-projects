/**
 * Enum for marking on the tic-tac-toe board (X O BLANK)
 * @author Itay Turniansky
 */
public enum Mark {
	/**
	 * all mark options
	 */
	BLANK, X, O;
	/*
	string literals definition
	 */
	private static final String MARK_ONE = "X";
	private static final String MARK_TWO = "O";

	/**
	 * gets a Mark Enum
	 * @param n mark
	 * @return string of the given value of mark
	 */
	public String toString(Mark n) {
		switch (n) {
			case X:
				return MARK_ONE;
			case O:
				return MARK_TWO;
			default:
				return null;
		}
	}
}
