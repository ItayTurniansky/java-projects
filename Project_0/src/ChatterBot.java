import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * if it starts with REQUEST_PREFIX2 it return the following statement.
 * if it starts with REQUEST_PREFIX the bot returns one of
 * a few possible replies as supplied to it via its constructor.
 * in all illegal cases the bot returns one of
 * a few possible replies as supplied to it via its constructor
 *
 * @author Itay Turniansky
 * @see Chat
 */
class ChatterBot {
	static final String PLACEHOLDER_FOR_ILLEGAL_REQUEST = "<request>";
	static final String PLACEHOLDER_FOR_REQUESTED_PHRASE = "<phrase>";
	static final String REQUEST_PREFIX = "say ";
	static final String REQUEST_PREFIX2 = "echo ";
	String name;
	Random rand = new Random();
	String[] illegalRequestsReplies;
	String[] legalRequestsReplies;

	/**
	 * Placeholders and prefixes used for replacing words in possible replies.
	 * Constructor
	 *
	 * @param name                   - bot name
	 * @param legalRequestsReplies   - array of possible replies to legal request
	 * @param illegalRequestsReplies - array of possible replies to illegal request
	 */

	ChatterBot(String name, String[] legalRequestsReplies,
			   String[] illegalRequestsReplies) {
		this.name = name;
		this.legalRequestsReplies = new String[legalRequestsReplies.length];
		for (int i = 0; i < legalRequestsReplies.length; i = i + 1) {
			this.legalRequestsReplies[i] = legalRequestsReplies[i];
		}
		this.illegalRequestsReplies = new String[illegalRequestsReplies.length];
		for (int i = 0; i < illegalRequestsReplies.length; i = i + 1) {
			this.illegalRequestsReplies[i] = illegalRequestsReplies[i];
		}

	}

	/**
	 * @param statement - request
	 * @return the correct reply according the bot settings and string arrays.
	 * this function navigates to the correct function use according to the input
	 */

	String replyTo(String statement) {
		if (statement.startsWith(REQUEST_PREFIX)) {
			String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
			return replyToLegalRequest(phrase);
		}
		if (statement.startsWith(REQUEST_PREFIX2)) {
			return statement.replaceFirst(REQUEST_PREFIX2, "");
		}

		return replyToIllegalRequest(statement);
	}

	/**
	 * @param statement - input
	 * @return - random response from the legal responses array
	 */

	String replyToLegalRequest(String statement) {
		return replacePlaceholderInARandomPattern(legalRequestsReplies,
				PLACEHOLDER_FOR_REQUESTED_PHRASE, statement);
	}

	/**
	 * @param statement - input
	 * @return - random response from the illegal responses array
	 */
	String replyToIllegalRequest(String statement) {
		return replacePlaceholderInARandomPattern(illegalRequestsReplies,
				PLACEHOLDER_FOR_ILLEGAL_REQUEST, statement);
	}

	/**
	 * chooses random string and replaces it placeholder with statement
	 *
	 * @param patterns    - array of strings
	 * @param placeholder - placeholder in strings
	 * @param replacement - the statement to insert instead of placeholder
	 * @return - final string
	 */

	String replacePlaceholderInARandomPattern(String[] patterns, String placeholder, String replacement) {
		int randomIndex = rand.nextInt(patterns.length);
		return patterns[randomIndex].replaceAll(placeholder, replacement);
	}

	/**
	 * @return - bot name.
	 */
	String getName() {
		return name;
	}
}
