public class Game {
	private Player playerX;
	private Player playerO;
	private Renderer renderer;
	private Board board;
	private int winStreak;
	private static final int DEFAULT_WIN_STREAK = 3;
	private static final int BUFFER = 1;
	private static final int DIAGONAL = 2;



	public Game(Player playerX, Player playerO, Renderer renderer) {
		this.playerX = playerX;
		this.playerO = playerO;
		this.renderer = renderer;
		this.board = new Board();
		this.winStreak = DEFAULT_WIN_STREAK;
	}
	public Game(Player playerX, Player playerO,int size, int winStreak, Renderer renderer ) {
		this.playerX = playerX;
		this.playerO = playerO;
		this.renderer = renderer;
		this.board = new Board(size);
		this.winStreak = winStreak;
	}
	public int getWinStreak(){
		return winStreak;
	}
	public int getBoardSize(){
		return this.board.getSize();
	}

	private boolean checkRows(Mark mark) {
		for (int i = 0; i < board.getSize(); i++) {
			int count = 0;
			for (int j = 0; j < board.getSize(); j++) {
				if (board.getMark(i, j) == mark) {
					count++;
					if (count == winStreak) {
						return true;
					}
				}else{
					count=0;
				}

			}
		}
		return false;
	}

	private boolean checkCol(Mark mark) {
		for (int i = 0; i < board.getSize(); i++) {
			int count = 0;
			for (int j = 0; j < board.getSize(); j++) {
				if (board.getMark(j, i) == mark) {
					count++;
					if (count == winStreak) {
						return true;
					}
				}else{
					count=0;
				}

			}
		}
		return false;
	}

	private boolean checkDiag(Mark mark) {
		// Check Diagonals (Top-left to Bottom-right)
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

		// Check Diagonals (Top-right to Bottom-left)
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



	private boolean checkWin(Mark mark) {
		return checkRows(mark)||checkCol(mark)||checkDiag(mark);
	}


	public Mark run(){
		int turn_count =0;
		int max_turns = board.getSize()*board.getSize();
	while(turn_count<max_turns){
		this.playerX.playTurn(this.board, Mark.X);
		turn_count++;
		this.renderer.renderBoard(this.board);
		if (checkWin(Mark.X)){
			return Mark.X;
		}
		this.playerO.playTurn(this.board, Mark.O);
		turn_count++;
		this.renderer.renderBoard(this.board);
		if (checkWin(Mark.O)){
			return Mark.O;
		}
	}
	return Mark.BLANK;
	}
}
