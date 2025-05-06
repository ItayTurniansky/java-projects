package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.Random;

/**
 * BrickerGameManger class for controlling the bricker game
 *
 * @author itayt
 */
public class BrickerGameManager extends GameManager {
	/*
	constants for sizes, speed, buffers etc...
	 */
	private static final float BALL_SPEED = 250;
	private static final float SCREEN_CENTER = 0.5f;
	private static final float BALL_SIZE = 20;
	private static final float SCREEN_LENGTH = 600;
	private static final float SCREEN_WIDTH = 750;
	private static final float PADDLE_LENGTH = 100;
	private static final float PADDLE_WIDTH = 15;
	private static final float PADDLE_Y = 20;
	private static final float WALL_WIDTH = 22;
	private static final Color BORDER_COLOR = Color.cyan;
	private static final float BRICK_HEIGHT = 15;
	private static final int DEFAULT_BRICKS_ROW_NUM = 7;
	private static final int DEFAULT_BRICKS_PER_ROW = 8;
	private static final float BRICK_BUFFER_SIZE = 5;
	private static final int START_LIVES = 3;
	private static final int TARGET_FRAMERATE = 55;
	private static final int MAX_HEARTS = 4;
	private static final float HEART_START = 20;
	private static final float HEART_BUFFER = 30;
	private static final float HEART_SIZE = 25;
	private static final float PUCK_MULTIPLAYER = 0.75f;
	private static final float TURBO_FACTOR = 1.4f;
	private static final float HEART_SPEED = 100f;
	private static final int TURBO_MAX_HEATS = 6;
	private static final int BRICK_LOST_PER_HIT = 1;
	private static final double EXTRA_BALL_ODDS = 0.1;
	private static final double EXTRA_PADDLE_ODDS = 0.2;
	private static final double TURBO_MODE_ODDS = 0.3;
	private static final double EXTRA_LIFE_ODDS = 0.4;
	private static final double DOUBLE_ACTION_ODDS = 0.5;
	private static final int ARGS_NUM = 2;
	private static final String MOCK_BALL_PATH = "assets/mockBall.png";
	private static final String SOUND_PATH = "assets/blop.wav";
	private static final String RED_BALL_PATH = "assets/redball.png";
	private static final String GAME_TITLE = "bricker";
	private static final String MAIN_BALL_PATH = "assets/ball.png";
	private static final String WINNING_MASSAGE = "You Win! Play again?";
	private static final String LOSING_MASSAGE = "You lose! Play again?";
	private static final String PADDLE_PATH = "assets/paddle.png";
	private static final String MAIN_PADDLE_TAG = "MainPaddle";
	private static final String BRICK_PATH = "assets/brick.png";
	private static final String BACKGROUND_PATH = "assets/DARK_BG2_small.jpeg";
	private static final String HEART_PATH = "assets/heart.png";


	/*
	class fields for maintaining objects and transferring them to
	the relevant functions
	 */
	private int bricksLeft;
	private final int rowNum;
	private final int bricksPerRows;
	private int lives = START_LIVES;
	private Heart[] hearts;
	private Counter counter;
	private Renderable heartImage;
	private Ball ball;
	private Paddle userPaddle;
	private WindowController windowController;
	private Vector2 windowDimensions;
	private UserInputListener inputListener;
	private ImageRenderable ballImage;
	private Sound collisionSound;
	private ImageReader imageReader;
	private SoundReader soundReader;
	private List<Puck> activePucks = new ArrayList<>();
	private List<Heart> activeHearts = new ArrayList<>();
	private ImageRenderable paddleImage;
	private ExtraPaddle currentExtraPaddle;
	private int turboCounter = 0;
	private boolean isTurbo = false;

	/**
	 * Constructor
	 *
	 * @param windowTitle      Title of the game window
	 * @param windowDimensions vector of width and length of the game window dimensions
	 * @param rowNum           number of bricks rows
	 * @param bricksPerRow     number of bricks per row
	 */
	public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
							  int rowNum, int bricksPerRow) {
		super(windowTitle, windowDimensions);
		this.rowNum = rowNum;
		this.bricksPerRows = bricksPerRow;
		this.bricksLeft = rowNum * bricksPerRow;
	}


	@Override
	/**
	 * @param imageReader      Contains a single method: readImage, which reads an image from disk.
	 *                         See its documentation for help.
	 * @param soundReader      Contains a single method: readSound, which reads a wav file from
	 *                         disk. See its documentation for help.
	 * @param inputListener    Contains a single method: isKeyPressed, which returns whether
	 *                         a given key is currently pressed by the user or not. See its
	 *                         documentation.
	 * @param windowController Contains an array of helpful, self explanatory methods
	 *                         concerning the window.
	 */
	public void initializeGame(ImageReader imageReader, SoundReader soundReader,
							   UserInputListener inputListener, WindowController windowController) {
		this.imageReader = imageReader;
		this.soundReader = soundReader;
		this.inputListener = inputListener;
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		windowController.setTargetFramerate(TARGET_FRAMERATE);
		this.windowController = windowController;
		this.windowDimensions = windowController.getWindowDimensions();
		createUserPaddle(imageReader, inputListener);
		createWalls();
		createBackground(imageReader);
		createBricks(imageReader, rowNum, bricksPerRows);
		createBall(imageReader, soundReader);
		createLivesDisplay(imageReader);
	}


	@Override
	/**
	 * @param deltaTime The time, in seconds, that passed since the last invocation
	 *                  of this method (i.e., since the last frame). This is useful
	 *                  for either accumulating the total time that passed since some
	 *                  event, or for physics integration (i.e., multiply this by
	 *                  the acceleration to get an estimate of the added velocity or
	 *                  by the velocity to get an estimate of the difference in position).
	 */
	public void update(float deltaTime) {
		super.update(deltaTime);
		checkLose();
		checkWin();
		updateLives();
		checkPucks();
		checkTurbo();
		checkFallingHearts();
	}

	/**
	 * @param object static object to delete from screen
	 * @return true if it was deleted and false if it wasn't
	 */
	public boolean deleteStaticObject(GameObject object) {
		return gameObjects().removeGameObject(object, Layer.STATIC_OBJECTS);
	}

	/**
	 * @param paddle paddle object to delete
	 */
	public void deletePaddleObject(Paddle paddle) {
		gameObjects().removeGameObject(paddle);
		currentExtraPaddle = null;

	}

	/**
	 * @param heart object to delete
	 */
	public void deleteHeartObject(Heart heart) {
		Iterator<Heart> heartIterator = activeHearts.iterator();
		while (heartIterator.hasNext()) {
			Heart tmpHeart = heartIterator.next();
			if (heart == tmpHeart) {
				gameObjects().removeGameObject(heart);
				heartIterator.remove();
			}
		}
	}

	/**
	 * decrease the number of bricks left on the screen by BRICK_LOST_PER_HIT.
	 */
	public void updateBricksCounter() {
		this.bricksLeft -= BRICK_LOST_PER_HIT;
	}

	/**
	 * handles an ExtraBall brick behaviour when its hit
	 *
	 * @param object used to get the center of the brick so the balls will start from it
	 */
	public void triggerExtraBall(GameObject object) {
		Renderable puckImage = imageReader.readImage(MOCK_BALL_PATH, true);
		Sound collisionSound = soundReader.readSound(SOUND_PATH);
		for (int i = 0; i < 2; i++) {
			Puck puck = new Puck(Vector2.ZERO, new Vector2(BALL_SIZE * PUCK_MULTIPLAYER,
					BALL_SIZE * PUCK_MULTIPLAYER), puckImage, collisionSound);
			gameObjects().addGameObject(puck);
			activePucks.add(puck);
			Vector2 center = object.getCenter();
			puck.setCenter(center);
			Random ran = new Random();
			double angle = ran.nextDouble() * Math.PI;
			float velocityX = (float) Math.cos(angle) * BALL_SPEED;
			float velocityY = (float) Math.sin(angle) * BALL_SPEED;
			puck.setVelocity(new Vector2(velocityX, velocityY));

		}

	}

	/**
	 * handles an Extra Paddle brick behaviour when its hit
	 */
	public void triggerExtraPaddle() {
		if (currentExtraPaddle == null) {
			ExtraPaddle extraPaddle = new ExtraPaddle(Vector2.ZERO,
					new Vector2(PADDLE_LENGTH, PADDLE_WIDTH), paddleImage, inputListener,
					SCREEN_WIDTH, this);
			gameObjects().addGameObject(extraPaddle);
			extraPaddle.setCenter(
					new Vector2(windowDimensions.x() * SCREEN_CENTER, windowDimensions.y() -
							SCREEN_LENGTH / 2));
			this.currentExtraPaddle = extraPaddle;
		}
	}

	/**
	 * handles a TurboMode brick behaviour when its hit
	 */
	public void triggerTurboMode() {
		if (!isTurbo) {
			ball.renderer().setRenderable(imageReader.readImage(RED_BALL_PATH,
					true));
			ball.setVelocity(ball.getVelocity().mult(TURBO_FACTOR));
			isTurbo = true;
			turboCounter = ball.getCollisionCounter();
		}
	}

	/**
	 * handles a ExtraLife brick behaviour when its hit
	 *
	 * @param object used to set the heart starting point
	 */
	public void triggerExtraLife(GameObject object) {
		Heart heart = new Heart(object.getCenter(), new Vector2(HEART_SIZE, HEART_SIZE),
				heartImage, this);
		gameObjects().addGameObject(heart);
		heart.setVelocity(new Vector2(0, HEART_SPEED));
		activeHearts.add(heart);
	}

	/**
	 * adds one life
	 */
	public void addLife() {
		if (lives < MAX_HEARTS) {
			lives++;
		}
	}

	/**
	 * @param args input CLI arguments
	 */
	public static void main(String[] args) {
		if (args.length == ARGS_NUM) {
			int rowNum = Integer.parseInt(args[0]);
			int bricksPerRow = Integer.parseInt(args[1]);
			GameManager gameManager = new BrickerGameManager(GAME_TITLE,
					new Vector2(SCREEN_WIDTH, SCREEN_LENGTH), rowNum, bricksPerRow);
			gameManager.run();
		} else {
			GameManager gameManager = new BrickerGameManager(GAME_TITLE,
					new Vector2(SCREEN_WIDTH, SCREEN_LENGTH), DEFAULT_BRICKS_ROW_NUM, DEFAULT_BRICKS_PER_ROW);
			gameManager.run();
		}
	}

	/*
	 *
	 * restores the ball to normal after turbo mode is done
	 */
	private void restoreTurboMode() {
		if (isTurbo) {
			isTurbo = false;
			ball.renderer().setRenderable(imageReader.readImage(MAIN_BALL_PATH, true));
			ball.setVelocity(ball.getVelocity().mult(1 / TURBO_FACTOR));
		}
	}

	/*
	checks and removes falling hearts out of bounds
 	*/
	private void checkFallingHearts() {
		Iterator<Heart> heartIterator = activeHearts.iterator();
		while (heartIterator.hasNext()) {
			Heart heart = heartIterator.next();
			if (heart.getTopLeftCorner().y() > windowDimensions.y()) {
				gameObjects().removeGameObject(heart);
				heartIterator.remove();
			}
		}
	}

	/*
	checks and disables ball turbo mode
 	*/
	private void checkTurbo() {
		if (isTurbo) {
			if (ball.getCollisionCounter() - turboCounter >= TURBO_MAX_HEATS) {
				restoreTurboMode();
			}
		}
	}
	/*
	checks and removes pucks out of bounds
 	*/

	private void checkPucks() {
		Iterator<Puck> puckIterator = activePucks.iterator();
		while (puckIterator.hasNext()) {
			Puck puck = puckIterator.next();
			if (puck.getTopLeftCorner().y() > windowDimensions.y()) {
				gameObjects().removeGameObject(puck);
				puckIterator.remove();
			}
		}
	}
	/*
	checks if player won and handles it
 	*/

	private void checkWin() {
		if (bricksLeft == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
			if (windowController.openYesNoDialog(WINNING_MASSAGE)) {
				this.lives = START_LIVES;
				this.bricksLeft = rowNum * bricksPerRows;
				restGameHelper();
				windowController.resetGame();
			} else {
				windowController.closeWindow();
			}
		}
	}

	/*
	checks if player lost and handles it
 	*/
	private void checkLose() {
		double ballHeight = ball.getCenter().y();
		if (ballHeight > windowDimensions.y()) {
			if (lives > 1) {
				centerBall(ball);
				lives -= 1;
			} else {
				if (windowController.openYesNoDialog(LOSING_MASSAGE)) {
					this.lives = START_LIVES;
					restGameHelper();
					windowController.resetGame();
				} else {
					windowController.closeWindow();
				}
			}
		}
	}

	/*
	helper function that resets the extraPaddle when game restarts.
	 */
	private void restGameHelper() {
		this.currentExtraPaddle = null;
	}

	/*
		create game walls.
	 */
	private void createWalls() {
		GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(WALL_WIDTH, SCREEN_LENGTH),
				new RectangleRenderable(BORDER_COLOR));
		gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);

		GameObject rightWall = new GameObject(new Vector2(SCREEN_WIDTH - WALL_WIDTH, 0),
				new Vector2(WALL_WIDTH, SCREEN_LENGTH), new RectangleRenderable(BORDER_COLOR));
		gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);

		GameObject upWall = new GameObject(Vector2.UP, new Vector2(SCREEN_WIDTH,
				WALL_WIDTH), new RectangleRenderable(BORDER_COLOR));
		gameObjects().addGameObject(upWall, Layer.STATIC_OBJECTS);
	}
	/*
	create game ball
	 */

	private void createBall(ImageReader imageReader, SoundReader soundReader) {
		ballImage = imageReader.readImage(MAIN_BALL_PATH, true);
		collisionSound = soundReader.readSound(SOUND_PATH);
		ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound);
		gameObjects().addGameObject(ball);
		centerBall(ball);

	}
	/*
	center game ball
	 */

	private void centerBall(Ball ball) {
		Vector2 center = windowDimensions.mult(SCREEN_CENTER);
		ball.setCenter(center);
		float ballVelX = BALL_SPEED;
		float ballVelY = BALL_SPEED;
		Random ran = new Random();
		if (ran.nextBoolean()) ballVelX *= -1;
		if (ran.nextBoolean()) ballVelY *= -1;
		ball.setVelocity(new Vector2(ballVelX, ballVelY));
	}
	/*
	create main user paddle
	 */

	private void createUserPaddle(ImageReader imageReader, UserInputListener inputListener) {
		paddleImage = imageReader.readImage(PADDLE_PATH, true);
		userPaddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_LENGTH, PADDLE_WIDTH),
				paddleImage, inputListener, SCREEN_WIDTH, MAIN_PADDLE_TAG);
		gameObjects().addGameObject(userPaddle);
		userPaddle.setCenter(new Vector2(windowDimensions.x() * SCREEN_CENTER,
				windowDimensions.y() - PADDLE_Y));
	}
	/*
	create all bricks
	 */

	private void createBricks(ImageReader imageReader, int numRows, int numBricksPerRow) {
		Renderable brickImage = imageReader.readImage(BRICK_PATH, false);
		float availableWidth = SCREEN_WIDTH - 2 * WALL_WIDTH - (numBricksPerRow + 1) * BRICK_BUFFER_SIZE;
		float brickWidth = availableWidth / numBricksPerRow;
		float startX = WALL_WIDTH + BRICK_BUFFER_SIZE;
		float startY = WALL_WIDTH + BRICK_BUFFER_SIZE;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numBricksPerRow; j++) {
				Vector2 brickTopLeft = new Vector2(
						startX + j * (brickWidth + BRICK_BUFFER_SIZE),
						startY + i * (BRICK_HEIGHT + BRICK_BUFFER_SIZE)
				);
				CollisionStrategy collisionStrategy = randomCollisionStrategy();
				GameObject brick = new Brick(brickTopLeft,
						new Vector2(brickWidth, BRICK_HEIGHT), brickImage, collisionStrategy);
				gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
			}
		}
	}

	/*
	create game background
	 */
	private void createBackground(ImageReader imageReader) {
		Renderable backgroundImage = imageReader.readImage
				(BACKGROUND_PATH, true);
		GameObject background = new GameObject(Vector2.ZERO,
				new Vector2(SCREEN_WIDTH, SCREEN_LENGTH), backgroundImage);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		gameObjects().addGameObject(background, Layer.BACKGROUND);
	}
	/*
	generate random collision strategy based on odds.
	 */

	private CollisionStrategy randomCollisionStrategy() {
		Random rand = new Random();
		double p = rand.nextDouble();

		if (p < EXTRA_BALL_ODDS) {
			return new ExtraBallCollisionStrategy(this);
		} else if (p < EXTRA_PADDLE_ODDS) {
			return new ExtraPaddleCollisionStrategy(this);
		} else if (p < TURBO_MODE_ODDS) {
			return new TurboModeCollisionStrategy(this);
		} else if (p < EXTRA_LIFE_ODDS) {
			return new ExtraLifeStrategy(this);
		} else if (p < DOUBLE_ACTION_ODDS) {
			return new DoubleActionCollisionStrategy(this);
		} else {
			return new BasicCollisionStrategy(this);

		}

	}

	/*
	create the life display
	 */
	private void createLivesDisplay(ImageReader imageReader) {
		heartImage = imageReader.readImage(HEART_PATH, true);
		hearts = new Heart[MAX_HEARTS];

		for (int i = 0; i < lives; i++) {
			addHeart(i);
		}

		counter = new Counter(String.valueOf(lives));
		GameObject counterObject = new GameObject(
				new Vector2(HEART_START, SCREEN_LENGTH - HEART_BUFFER * 2),
				new Vector2(HEART_BUFFER, HEART_BUFFER),
				counter
		);
		gameObjects().addGameObject(counterObject, Layer.UI);
	}


	/*
	add heart to display
	 */
	private void addHeart(int index) {
		if (hearts[index] == null) {
			Vector2 heartPos = new Vector2(HEART_START + HEART_BUFFER * index, SCREEN_LENGTH - HEART_BUFFER);
			Heart heart = new Heart(heartPos, new Vector2(HEART_SIZE, HEART_SIZE), heartImage, this);
			hearts[index] = heart;
			gameObjects().addGameObject(heart, Layer.UI);
		}
	}

	/*
	update life counter and display
	 */
	private void updateLives() {
		counter.update(lives);
		for (int i = 0; i < hearts.length; i++) {
			if (i < lives) {
				addHeart(i);
			} else {
				if (hearts[i] != null) {
					gameObjects().removeGameObject(hearts[i], Layer.UI);
					hearts[i] = null;
				}
			}
		}
	}


}