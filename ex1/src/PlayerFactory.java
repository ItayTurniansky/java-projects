public class PlayerFactory {
	public PlayerFactory() {}
	public Player buildPlayer(String type){
		switch (type) {
			case "human":
				return new HumanPlayer();
			case "whatever":
				return new WhateverPlayer();
			case "genius":
				return new GeniusPlayer();
			case "clever":
				return new CleverPlayer();
		}
		return null;

	}
}
