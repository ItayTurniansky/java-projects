public class CleverPlayer implements Player {
	private final Player player_no_space = new WhateverPlayer();
	public CleverPlayer() {}
	public void playTurn(Board board, Mark mark) {
		for (int row = 0; row < board.getSize(); row++) {
			if (board.getMark(row,0)==Mark.BLANK){
				board.putMark(mark,row,0);
				return;
			}
		}
		player_no_space.playTurn(board,mark);
	}
}
