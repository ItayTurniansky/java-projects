/**
 * Genius TIC TAC TOE player class
 * implements the Player interface
 * this player is supposed to win against clever player 55% of games in a tournament
 * strategy of this player is filling the first row and column as long that there is space
 * if there isn't space it chooses randomly
 * @author Itay Turniansky
 */
public class GeniusPlayer implements Player {
	/*
	whatever player to use when first row is full
 */
	private final Player playerNoSpace = new WhateverPlayer();

	/**
	 * Default Constructor
	 */
	public GeniusPlayer() {
	}

	@Override
	/**
	 * implements the player strategy
	 * @param board - board to play on
	 * @param mark - player mark (X or O)
	 */
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
		playerNoSpace.playTurn(board, mark);
	}
}