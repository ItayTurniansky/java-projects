public class GeniusPlayer implements Player {
	private final Player player_no_space = new WhateverPlayer();
	private static final int BUFFER = 1;
	private static final int DIVIDER = 2;

	public GeniusPlayer() {
	}

	public void playTurn(Board board, Mark mark) {
		for (int row = 0; row < board.getSize(); row++) {
			if (board.getMark(row, 0) == Mark.BLANK) {
				board.putMark(mark, row, 0);
				return;
			}
		}
		for (int col = 0; col < board.getSize(); col++) {
			if (board.getMark(0, col) == Mark.BLANK) {
				board.putMark(mark, 0, col);
				return;
			}
		}
		player_no_space.playTurn(board, mark);
	}
}