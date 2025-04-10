import java.util.Random;

public class WhateverPlayer implements Player {
	Random rand = new Random();
	public WhateverPlayer(){}
	@Override
	public void playTurn(Board board, Mark mark){
		int rand_row = rand.nextInt(board.getSize());
		int rand_col = rand.nextInt(board.getSize());
		while(!board.putMark(mark, rand_row, rand_col)){
			rand_row = rand.nextInt(board.getSize());
			rand_col = rand.nextInt(board.getSize());
		}
	}
}
