import java.util.Scanner;

/**
 * Human Tic Tac Toe Player
 * this class gets input form the user for location to put his mark on board
 * based on location given the game is changed accordingly.
 * @author Itay Turniansky
 */
public class HumanPlayer implements Player {
	/*
	buffers and edges for later indexing and input dividing
	 */
	private static final int INPUT_SPLITTER = 10;
	private static final int BOARD_BUFFER = 1;
	private static final int BOARD_EDGE = 0;
	private static final String INPUT_REQUEST = "Player %s, type coordinates:";
	private static final String LOCATION_ERROR = "Invalid mark position. Please choose a valid position:";
	private static final String LOCATION_FULL_ERROR =
			"Mark position is already occupied. Please choose a valid position:";

	/**
	 * Default Constructor
	 */
	public HumanPlayer() {
	}

	@Override
	/**
	 * gets input location from user and plays the turn based on it
	 * print error according to errors (O.O.B or location full)
	 */
	public void playTurn(Board board, Mark mark) {
		String outputString = String.format(INPUT_REQUEST, mark.toString(mark));
		System.out.println(outputString);
		int input = KeyboardInput.readInt();
		int row = input / INPUT_SPLITTER;
		int col = input % INPUT_SPLITTER;
		while (!board.putMark(mark, row, col)) {
			if (row < BOARD_EDGE || row > board.getSize() - BOARD_BUFFER ||
					col < BOARD_EDGE || col > board.getSize() - BOARD_BUFFER) {
				System.out.println(LOCATION_ERROR);
			} else {
				System.out.println(LOCATION_FULL_ERROR);
			}
			input = KeyboardInput.readInt();
			row = input / INPUT_SPLITTER;
			col = input % INPUT_SPLITTER;

		}
	}
}
