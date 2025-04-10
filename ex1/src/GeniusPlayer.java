public class GeniusPlayer implements Player {
	private final Player player_no_space = new WhateverPlayer();
	private static final int BUFFER = 1;
	private static final int DIVIDER = 2;

	public GeniusPlayer() {}
	public void playTurn(Board board, Mark mark) {
		int board_size = board.getSize();
		if (board_size%2==0) {
			if (board.getMark((board_size/DIVIDER)-BUFFER,
					(board_size/DIVIDER)-BUFFER )==Mark.BLANK){
				board.putMark(mark, (board_size/DIVIDER)-BUFFER,
						(board_size/DIVIDER)-BUFFER );
				return;
			}
			if (board.getMark((board_size/DIVIDER)-BUFFER,
					(board_size/DIVIDER))==Mark.BLANK){
				board.putMark(mark, (board_size/DIVIDER)-BUFFER,
						(board_size/DIVIDER));
				return;
			}
			if (board.getMark((board_size/DIVIDER),
					(board_size/DIVIDER)-BUFFER )==Mark.BLANK){
				board.putMark(mark, (board_size/DIVIDER),
						(board_size/DIVIDER)-BUFFER );
				return;
			}
			if (board.getMark((board_size/DIVIDER),
					(board_size/DIVIDER) )==Mark.BLANK){
				board.putMark(mark, (board_size/DIVIDER),
						(board_size/DIVIDER));
				return;
			}
			}
		if (board.getMark((board_size-BUFFER)/DIVIDER,
				(board_size-BUFFER)/DIVIDER)==Mark.BLANK){
			board.putMark(mark, (board_size-BUFFER)/DIVIDER,
					(board_size-BUFFER)/DIVIDER);
			return;
		}
		player_no_space.playTurn(board, mark);
		}

	}
