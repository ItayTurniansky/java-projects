import java.util.Random;

/**
 * Whatever player class implements the player interface
 * this player chooses a random legal board location
 * @author Itay Turniansky
 */
public class WhateverPlayer implements Player {
	/*
	random generating
	 */
	Random rand = new Random();

	/**
	 * Default Constructor
	 */
	public WhateverPlayer() {
	}
	@Override
	/**
	 * chooses location randomly and tries to put until it works.
	 */
	public void playTurn(Board board, Mark mark) {
		int randROW = rand.nextInt(board.getSize());
		int ranCOL = rand.nextInt(board.getSize());
		while (!board.putMark(mark, randROW, ranCOL)) {
			randROW = rand.nextInt(board.getSize());
			ranCOL = rand.nextInt(board.getSize());
		}
	}
}
