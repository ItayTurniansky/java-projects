/**
 * Player Factory class used for generating players
 * @author Itay Turniansky
 */
public class PlayerFactory {
	private static final String TYPE_ONE = "human";
	private static final String TYPE_TWO = "whatever";
	private static final String TYPE_THREE = "genius";
	private static final String TYPE_FOUR = "clever";
	/**
	 * Default Constructor
	 */
	public PlayerFactory() {
	}

	/**
	 * build a player based on type
	 * @param type - player type
	 * @return - the player of given type
	 */

	public Player buildPlayer(String type) {
		switch (type) {
			case TYPE_ONE:
				return new HumanPlayer();
			case TYPE_TWO:
				return new WhateverPlayer();
			case TYPE_THREE:
				return new GeniusPlayer();
			case TYPE_FOUR:
				return new CleverPlayer();
		}
		return null;

	}
}
