import java.util.Scanner;

public class HumanPlayer implements Player {
	private static final int INPUT_SPLITTER = 10;
	private static final int BOARD_BUFFER = 1;
	private static final int BOARD_EDGE = 0;
	public HumanPlayer() {}
	@Override
	public void playTurn(Board board, Mark mark) {
		Scanner scanner = new Scanner(System.in);
		String outputString = String.format("Player %s, type coordinates:",mark.toString(mark));
		System.out.println(outputString);
		int input = KeyboardInput.readInt();
		int row = input/INPUT_SPLITTER;
		int col = input%INPUT_SPLITTER;
		while( row<BOARD_EDGE || row> board.getSize()-BOARD_BUFFER
				||col<BOARD_EDGE || col> board.getSize()-BOARD_BUFFER) {
			System.out.println("Invalid mark position. Please choose a valid position:");
			input = KeyboardInput.readInt();
			row = input/INPUT_SPLITTER;
			col = input%INPUT_SPLITTER;
		}
		while (!board.putMark(mark, row,col)) {
			System.out.println("Mark position is already occupied. Please choose a valid position:");
			input = KeyboardInput.readInt();
			row = input/INPUT_SPLITTER;
			col = input%INPUT_SPLITTER;
		}

	}
}
