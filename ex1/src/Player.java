/**
 * player interface implemented by all player types
 * @author Itay Turniansky
 */
public interface Player {
	/**
	 * play next turn by player
	 * @param board - board for player to play on
	 * @param mark - player's mark to play on board
	 */
	void playTurn(Board board, Mark mark);
}
