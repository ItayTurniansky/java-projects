public class Tournament {
	private final int round;
	private final Renderer renderer;
	private final Player player1;
	private final Player player2;
	private static final int ROUND_NUM_INDEX = 0;
	private static final int BOARD_SIZE_INDEX = 1;
	private static final int WIN_STREAK_INDEX = 2;
	private static final int RENDERER_TYPE_INDEX = 3;
	private static final int PLAYER_ONE_INDEX = 4;
	private static final int PLAYER_TWO_INDEX = 5;



	public Tournament(int round, Renderer renderer
	, Player player1, Player player2) {
	this.round = round;
	this.renderer = renderer;
	this.player1 = player1;
	this.player2 = player2;
	}
	public void playTournament(int size, int winStreak,
							   String playerName1, String playerName2) {
		int player1Score = 0;
		int player2Score = 0;
		int drawCount = 0;
		for (int i = 0; i < this.round; i++) {
			if (i % 2 == 0) {
				Game game = new Game(this.player1, this.player2, size, winStreak, this.renderer);
				Mark winner = game.run();
				switch (winner) {
					case X:
						player1Score++;
						break;
					case O:
						player2Score++;
						break;
					case BLANK:
						drawCount++;
				}
			} else {
				Game game = new Game(this.player2, this.player1, size, winStreak, this.renderer);
				Mark winner = game.run();
				switch (winner) {
					case X:
						player2Score++;
						break;
					case O:
						player1Score++;
						break;
					case BLANK:
						drawCount++;
				}
			}
		}
		System.out.println("######### Results #########");
		System.out.println(String.format("Player 1, %s won: %d rounds", this.player1.getClass(), player1Score));
		System.out.println(String.format("Player 2, %s won: %d rounds", this.player2.getClass(), player2Score));
		System.out.println(String.format("%d :Ties ", drawCount));
	}

	public static void main(String[] args) {
		int rounds = Integer.parseInt(args[ROUND_NUM_INDEX]);
		int winStreak = Integer.parseInt(args[WIN_STREAK_INDEX]);
		int boardSize = Integer.parseInt(args[BOARD_SIZE_INDEX]);
		PlayerFactory playerFactory = new PlayerFactory();
		RendererFactory rendererFactory = new RendererFactory();
		Player player1 = playerFactory.buildPlayer(args[PLAYER_ONE_INDEX]);
		Player player2 = playerFactory.buildPlayer(args[PLAYER_TWO_INDEX]);
		Renderer renderer = rendererFactory.buildRenderer(args[RENDERER_TYPE_INDEX], boardSize);
		Tournament tournament = new Tournament(rounds, renderer, player1, player2);
		tournament.playTournament(boardSize, winStreak, args[PLAYER_ONE_INDEX],args[PLAYER_TWO_INDEX] );
	}

}
