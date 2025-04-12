/**
 * TIC TAC TOE game class
 * this class runs a single tic-tac-toe game
 * @author Itay Turniansky
 */
public class Game {
	/*
		defining private fields and constants for buffer and later functions
	 */
	private Player playerX;
	private Player playerO;
	private Renderer renderer;
	private Board board;
	private int winStreak;
	private static final int DEFAULT_WIN_STREAK = 3;
	private static final int BUFFER = 1;
	private static final int DIAGONAL = 2;

	/**
	 * Default Constructor
	 * @param playerX - first player (X mark)
	 * @param playerO - second player(O mark)
	 * @param renderer - renderer (void/console)
	 */
	public Game(Player playerX, Player playerO, Renderer renderer) {
		this.playerX = playerX;
		this.playerO = playerO;
		this.renderer = renderer;
		this.board = new Board();
		this.winStreak = DEFAULT_WIN_STREAK;
	}

	/**
	 * Constructor
	 * @param playerX - first player (X mark)
	 * @param playerO - second player(O mark)
	 * @param size - board size
	 * @param winStreak - mark streak needed for winning a game
	 * @param renderer - renderer (void/console)
	 */
	public Game(Player playerX, Player playerO, int size, int winStreak, Renderer renderer) {
		this.playerX = playerX;
		this.playerO = playerO;
		this.renderer = renderer;
		this.board = new Board(size);
		this.winStreak = winStreak;
	}

	/**
	 * @return winStreak
	 */
	public int getWinStreak() {
		return winStreak;
	}

	/**
	 * @return boardSize
	 */
	public int getBoardSize() {
		return this.board.getSize();
	}
	/*
	 private function that gets a mark and check all rows on board
	 to see of the mark given has a streak long enough to win
	 return true or false based on check result
	 the function uses a counter to see if streak is achieved
	 */
	private boolean checkRows(Mark mark) {
		for (int i = 0; i < board.getSize(); i++) {
			int count = 0;
			for (int j = 0; j < board.getSize(); j++) {
				if (board.getMark(i, j) == mark) {
					count++;
					if (count == winStreak) {
						return true;
					}
				} else {
					count = 0;
				}

			}
		}
		return false;
	}

	/*
	 private function that gets a mark and check all columns on board
	 to see of the mark given has a streak long enough to win
	 return true or false based on check result
	 the function uses a counter to see if streak is achieved
	 */
	private boolean checkCol(Mark mark) {
		for (int i = 0; i < board.getSize(); i++) {
			int count = 0;
			for (int j = 0; j < board.getSize(); j++) {
				if (board.getMark(j, i) == mark) {
					count++;
					if (count == winStreak) {
						return true;
					}
				} else {
					count = 0;
				}

			}
		}
		return false;
	}
	/*
		 private function that gets a mark and check all diagonal on board
		 to see of the mark given has a streak long enough to win
		 return true or false based on check result
		 the function uses a counter to see if streak is achieved
		 */
	private boolean checkDiag(Mark mark) {
		// Top-left to Bottom-right
		for (int d = -(board.getSize() - BUFFER);
			 d <= (board.getSize() - BUFFER); d++) {
			int count = 0;
			for (int row = 0; row < board.getSize(); row++) {
				int col = row - d;
				if (col >= 0 && col < board.getSize() &&
						board.getMark(row, col) == mark) {
					count++;
					if (count == winStreak) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}

		//Top-right to Bottom-left
		for (int d = 0; d <= (DIAGONAL * board.getSize() - DIAGONAL); d++) {
			int count = 0;
			for (int row = 0; row < board.getSize(); row++) {
				int col = d - row;
				if (col >= 0 && col < board.getSize() &&
						board.getMark(row, col) == mark) {
					count++;
					if (count == winStreak) {
						return true;
					}
				} else {
					count = 0;
				}
			}
		}

		return false;
	}

	/*
	 private function that united all checks:
	 diagonal,rows and columns and return true if streak is achieved in any of them
	 and false if not
	 */
	private boolean checkWin(Mark mark) {
		return checkRows(mark) || checkCol(mark) || checkDiag(mark);
	}

	/**
	 * run a full game of tic-tac-toe
	 * @return the Mark of the winner.
	 * or Mark. BLANK if a draw was achieved
	 */
	public Mark run() {
		//keep track of turn numbers in case board is full
		int turnCount = 0;
		int maxTurns = board.getSize() * board.getSize();
		while (turnCount < maxTurns) {
			this.playerX.playTurn(this.board, Mark.X);
			turnCount++;
			this.renderer.renderBoard(this.board);
			if (checkWin(Mark.X)) {
				return Mark.X;
			}
			if(turnCount == maxTurns) {
				return Mark.BLANK;
			}
			this.playerO.playTurn(this.board, Mark.O);
			turnCount++;
			this.renderer.renderBoard(this.board);
			if (checkWin(Mark.O)) {
				return Mark.O;
			}
		}
		return Mark.BLANK;
	}
}
