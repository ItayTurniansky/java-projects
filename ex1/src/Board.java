/**
 * TIC TAC TOE board class, square board with given size
 * handles marks putting and getting
 * data is stored in a 2D array of Marks.
 * @author Itay Turniansky
 * @see Mark
 */
public class Board {

	/*
	 * Default board size is 3X3
	 */
	private static final int DEFAULT_BOARD_SIZE = 3;
	private final int boardSize;
	private Mark[][] board;

	/**
	 * Default Constructor.
	 * initializes values to Mark.BLANK
	 */
	public Board() {
		this.boardSize = DEFAULT_BOARD_SIZE;
		this.board = new Mark[DEFAULT_BOARD_SIZE][DEFAULT_BOARD_SIZE];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				this.board[i][j] = Mark.BLANK;
			}
		}
	}

	/**
	 * Constructor.
	 * @param boardSize - input for board size.
	 * initializes values to Mark.BLANK
	 */
	public Board(int boardSize) {
		this.boardSize = boardSize;
		this.board = new Mark[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				this.board[i][j] = Mark.BLANK;
			}
		}
	}

	/**
	 * @return boardSize
	 */
	public int getSize() {
		return this.boardSize;
	}

	/**
	 * @param mark - mark yo put on board
	 * @param row - row index for mark
	 * @param col - column index for mark
	 * @return true-if mark was put, false-if location is taken or out of bounds.
	 */
	public boolean putMark(Mark mark, int row, int col) {
		if (row < this.boardSize && col < this.boardSize && row >= 0 && col >= 0) {
			if (this.board[row][col] == Mark.BLANK) {
				this.board[row][col] = mark;
				return true;
			}
		}
		return false;
	}

	/**
	 * get mark at board location
	 * @param row - row index to get mark from
	 * @param col - column index to get mark from
	 * @return mark at location
	 */
	public Mark getMark(int row, int col) {
		if (row > this.boardSize || col > this.boardSize || row < 0 || col < 0) {
			return Mark.BLANK;
		}
		return this.board[row][col];
	}


}
