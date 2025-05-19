package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import java.util.Arrays;
import java.io.IOException;

public class Shell {
	private static final String EXIT_STATEMENT = "exit";
	private static final String COMMAND_INPUT_STRING = "<<<";
	private static final String CHARS_COMMAND = "chars";
	private static final String ADD_STATEMENT = "add";
	private char[] charset;

	public Shell() {
		this.charset = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	}

	public void run(String imageName){
		try{
			Image image = new Image(imageName);
		}catch(IOException e){
			return;
		}
		System.out.print(COMMAND_INPUT_STRING);
		String input = KeyboardInput.readLine();
		while(!input.equals(EXIT_STATEMENT)){

			if(input.startsWith(CHARS_COMMAND)){
				Arrays.sort(this.charset);
				for(int i = 0; i < this.charset.length; i++){
					System.out.print(this.charset[i]+" ");
				}
				System.out.println();
			}

			if(input.startsWith(ADD_STATEMENT)){
				handleAdd(input);

			}
			System.out.print(COMMAND_INPUT_STRING);
			input = KeyboardInput.readLine();
		}
	}

	private void handleAdd(String input) {
		String[] c = input.split(" ");
	}

	public static void main(String[] args){
		String imageName = args[0];
		Shell shell = new Shell();
		shell.run(imageName);
	}

}
