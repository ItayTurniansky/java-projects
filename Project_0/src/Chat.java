import java.util.Scanner;

/**
 * chat class with main method for creating and using
 * the chatter bot class with two bots creating a chat
 * @author Itay Turniansky
 * @see ChatterBot
 * constants class
 */
class Chat {

	class Constants{
		public static final String[] BOT_1_ILLEGAL_RESPONSES = {"say what? " +
				ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + "? what’s " +
				ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST +
				" ?", "say i should say "};
		public static final String[] BOT_2_ILLEGAL_RESPONSES = {"say what? "
				+ ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + "? what’s " +
				ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST
				+ " ?", "say say "};
		public static final String[] BOT_1_LEGAL_RESPONSES = {"say " +
				ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE + " ? okay: " +
				ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE, "just for you: "
				+ ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE};
		public static final String[] BOT_2_LEGAL_RESPONSES = {"say " +
				ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE + " ? okay: " +
				ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE,
				"i dont want to but here: "
						+ ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE};
		public static final String BOT_1_NAME = "Yossi";
		public static final String BOT_2_NAME = "Moti";
		public static final String FIRST_WORD = "yo";

	}
	/**
	 * main program
	 *
	 * @param args - system
	 */
	public static void main(String[] args) {
        /* using the ChatterBot class,
        defining two bots with possible legal and illegal responses
        starting an infinite chat between the two bots*/
		ChatterBot[] botArr = new ChatterBot[2];
		//defining the two bots
		botArr[0] = new ChatterBot(Constants.BOT_1_NAME, Constants.BOT_1_LEGAL_RESPONSES,
				Constants.BOT_1_ILLEGAL_RESPONSES);
		botArr[1] = new ChatterBot(Constants.BOT_2_NAME, Constants.BOT_2_LEGAL_RESPONSES,
				Constants.BOT_2_ILLEGAL_RESPONSES);
		String statement = Constants.FIRST_WORD;
		Scanner scanner = new Scanner(System.in);
		System.out.println(statement);
		// infinite chat
		while (true) {
			for (ChatterBot bot : botArr) {
				statement = bot.replyTo(statement);
				System.out.print(bot.getName() + ": " + statement);
				scanner.nextLine();
			}
		}
	}
}