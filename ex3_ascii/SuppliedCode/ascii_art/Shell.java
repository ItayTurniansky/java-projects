package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.Arrays;

/**
 * The Shell class provides a command-line interface for generating ASCII art
 * from images. It supports operations such as adding/removing characters,
 * adjusting resolution, switching output formats, and rendering.
 * @author itayt
 */
public class Shell {
	// Constants
	private static final String EXIT_STATEMENT = "exit";
	private static final String COMMAND_INPUT_STRING = ">>> ";
	private static final String CHARS_COMMAND = "chars";
	private static final String ADD_STATEMENT = "add";
	private static final String REMOVE_STATEMENT = "remove";
	private static final String RES_STATEMENT = "res";
	private static final String ROUND_STATEMENT = "round";
	private static final String OUTPUT_STATEMENT = "output";
	private static final String ASCII_ART_STATEMENT = "asciiArt";

	private static final String CHAR_REGEX = "^[\\x20-\\x7E]$";
	private static final String RANGE_REGEX = "^[\\x20-\\x7E]-[\\x20-\\x7E]$";

	private static final String ADD_ALL_STRING = "all";
	private static final String ADD_SPACE_STRING = "space";

	private static final String RES_UP_STRING = "up";
	private static final String RES_DOWN_STRING = "down";

	private static final String ROUND_UP_STRING = "up";
	private static final String ROUND_DOWN_STRING = "down";
	private static final String ROUND_ABS_STRING = "abs";

	private static final String CONSOLE_OUTPUT = "console";
	private static final String HTML_OUTPUT = "html";
	private static final String DEFAULT_FILE_NAME = "out.html";
	private static final String DEFAULT_FONT_NAME = "Courier new";

	private static final int DEFAULT_RES = 2;
	private static final int MAX_ASCII = 126;
	private static final int MIN_ASCII = 32;

	// Fields
	private SubImgCharMatcher charMatcher;
	private int resolution;
	private int imageWidth;
	private int imageHeight;
	private int minCharsInRow;
	private AsciiOutput asciiOutput;
	private Image image;

	/**
	 * Constructs a Shell instance with default settings.
	 */
	public Shell() {
		this.charMatcher = new SubImgCharMatcher(new char[]{
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
		});

		this.resolution = DEFAULT_RES;
		this.asciiOutput = new ConsoleAsciiOutput();
	}

	/**
	 * Starts the interactive shell interface.
	 *
	 * @param imageName Path to the image file to process
	 */
	public void run(String imageName) {
		try {
			this.image = new Image(imageName);
			this.imageWidth = image.getWidth();
			this.imageHeight = image.getHeight();
			this.minCharsInRow = Math.max(1, this.imageWidth / this.imageHeight);
		} catch (IOException e) {
			System.out.println("Error reading image " + imageName);
			return;
		}

		// Initial prompt
		System.out.print(COMMAND_INPUT_STRING);
		String input = KeyboardInput.readLine();

		while (!input.equals(EXIT_STATEMENT)) {
			String[] inputArray = input.split(" ");
			String command = inputArray[0];
			String commandArg = (inputArray.length == 2) ? inputArray[1] : "";
			boolean executed = true;

			switch (command) {
				case CHARS_COMMAND:
					System.out.println(this.charMatcher.getCharMap().keySet());
					break;
				case ADD_STATEMENT:
					try {
						handleAdd(commandArg);
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
					}
					break;
				case REMOVE_STATEMENT:
					try {
						handleRemove(commandArg);
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
					}
					break;
				case RES_STATEMENT:
					try {
						handleRes(commandArg);
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
					}
					break;
				case ROUND_STATEMENT:
					try {
						handleRound(commandArg);
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
					}
					break;
				case OUTPUT_STATEMENT:
					try {
						handleOutput(commandArg);
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
					}
					break;
				case ASCII_ART_STATEMENT:
					try {
						handleAsciiArt();
					} catch (IllegalArgumentException e) {
						System.out.println(e.getMessage());
					}
					break;
				default:
					executed = false;
			}

			if (!executed) {
				System.out.println("Did not execute due to incorrect command.");
			}

			System.out.print(COMMAND_INPUT_STRING);
			input = KeyboardInput.readLine();
		}
	}

	/* Handles the asciiArt command by generating and printing ASCII art. */
	private void handleAsciiArt() {
		if (this.charMatcher.getCharMap().size() < 2) {
			throw new IllegalArgumentException("Did not execute. Charset is too small.");
		}
		char[] charset = new char[this.charMatcher.getCharMap().size()];
		int i = 0;
		for (Character c : this.charMatcher.getCharMap().keySet()) {
			charset[i++] = c;
		}
		AsciiArtAlgorithm algorithm =
				new AsciiArtAlgorithm(this.resolution, this.image, charset);
		this.asciiOutput.out(algorithm.run());
	}

	/* Sets the ASCII output format based on the command argument. */
	private void handleOutput(String commandArg) {
		if (commandArg.equals(CONSOLE_OUTPUT)) {
			this.asciiOutput = new ConsoleAsciiOutput();
		} else if (commandArg.equals(HTML_OUTPUT)) {
			this.asciiOutput = new HtmlAsciiOutput(DEFAULT_FILE_NAME, DEFAULT_FONT_NAME);
		} else {
			throw new IllegalArgumentException(
					"Did not change output method due to incorrect format.");
		}
	}

	/* Changes the rounding strategy used for character brightness matching. */
	private void handleRound(String commandArg) {
		if (commandArg.equals(ROUND_UP_STRING) ||
				commandArg.equals(ROUND_DOWN_STRING) ||
				commandArg.equals(ROUND_ABS_STRING)) {
			this.charMatcher.setRound(commandArg);
		} else {
			throw new IllegalArgumentException(
					"Did not change rounding method due to incorrect format.");
		}
	}

	/* Adjusts the resolution up or down, with boundary checks. */
	private void handleRes(String commandArg) {
		if (commandArg.equals("")) {
			System.out.println("Resolution set to " + this.resolution);
			return;
		}
		if (commandArg.equals(RES_UP_STRING)) {
			if (this.resolution * 2 < this.imageWidth) {
				this.resolution *= 2;
				System.out.println("Resolution set to " + this.resolution);
				return;
			}
			throw new IllegalArgumentException(
					"Did not change resolution due to exceeding boundaries.");
		}
		if (commandArg.equals(RES_DOWN_STRING)) {
			if (this.resolution / 2 > this.minCharsInRow) {
				this.resolution /= 2;
				System.out.println("Resolution set to " + this.resolution);
				return;
			}
			throw new IllegalArgumentException(
					"Did not change resolution due to exceeding boundaries.");
		}
		throw new IllegalArgumentException(
				"Did not change resolution due to incorrect format.");
	}

	/* Adds characters to the character set based on input syntax. */
	private void handleAdd(String commandArg) {
		if (commandArg.matches(CHAR_REGEX)) {
			this.charMatcher.addChar(commandArg.charAt(0));
		} else if (commandArg.equals(ADD_ALL_STRING)) {
			for (int i = MIN_ASCII; i <= MAX_ASCII; i++) {
				this.charMatcher.addChar((char) i);
			}
		} else if (commandArg.equals(ADD_SPACE_STRING)) {
			this.charMatcher.addChar(' ');
		} else if (commandArg.matches(RANGE_REGEX)) {
			char start = commandArg.charAt(0);
			char end = commandArg.charAt(2);
			int from = Math.min(start, end);
			int to = Math.max(start, end);
			for (int i = from; i <= to; i++) {
				this.charMatcher.addChar((char) i);
			}
		} else {
			throw new IllegalArgumentException("Did not add due to incorrect format.");
		}
	}

	/* Removes characters from the character set based on input syntax. */
	private void handleRemove(String commandArg) {
		if (commandArg.matches(CHAR_REGEX)) {
			this.charMatcher.removeChar(commandArg.charAt(0));
		} else if (commandArg.equals(ADD_ALL_STRING)) {
			for (int i = MIN_ASCII; i <= MAX_ASCII; i++) {
				this.charMatcher.removeChar((char) i);
			}
		} else if (commandArg.equals(ADD_SPACE_STRING)) {
			this.charMatcher.removeChar(' ');
		} else if (commandArg.matches(RANGE_REGEX)) {
			char start = commandArg.charAt(0);
			char end = commandArg.charAt(2);
			int from = Math.min(start, end);
			int to = Math.max(start, end);
			for (int i = from; i <= to; i++) {
				this.charMatcher.removeChar((char) i);
			}
		} else {
			throw new IllegalArgumentException("Did not remove due to incorrect format.");
		}
	}

	/**
	 * Main method for launching the ASCII art shell.
	 *
	 * @param args Expects the image path as args[0]
	 */
	public static void main(String[] args) {
		String imageName = args[0];
		Shell shell = new Shell();
		shell.run(imageName);
	}
}
