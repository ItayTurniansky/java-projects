public class Board {
	static final int DEFAULT_BOARD_SIZE = 3;
	private final int boardSize;
	private Mark[][] board;

	public Board() {
		this.boardSize = DEFAULT_BOARD_SIZE;
		this.board = new Mark[DEFAULT_BOARD_SIZE][DEFAULT_BOARD_SIZE];
	}
	public Board(int boardSize) {
		this.boardSize = boardSize;
		this.board = new Mark[boardSize][boardSize];
	}
	public int getSize(){
		return this.boardSize;
	}
	public boolean putMark(Mark mark, int row, int col) {
		if (row>this.boardSize || col>this.boardSize || row<0 || col<0) {
			if (this.board[row][col] == Mark.BLANK) {
				this.board[row][col] = mark;
				return true;
			}
		}
		return false;
	}
	public Mark getMark(int row, int col) {
		if (row>this.boardSize || col>this.boardSize || row<0 || col<0) {
			return Mark.BLANK;
		}
		return this.board[row][col];
	}


}
