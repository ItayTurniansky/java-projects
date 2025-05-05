package Bricker.main;

import Bricker.brick_strategies.*;
import Bricker.gameobjects.*;
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
import  java.util.*;
import java.util.List;
import java.util.Random;

public class BrickerGameManager extends GameManager {
	private static final float BALL_SPEED = 300;
	private static final float SCREEN_CENTER = 0.5f;
	private static final float BALL_SIZE = 20;
	private static final float SCREEN_LENGTH = 800;
	private static final float SCREEN_WIDTH = 1200;
	private static final float PADDLE_LENGTH = 100;
	private static final float PADDLE_WIDTH = 15;
	private static final float PADDLE_Y = 20;
	private static final float WALL_WIDTH = 20;
	private static final Color BORDER_COLOR = Color.CYAN;
	private static final float BRICK_HEIGHT = 15;
	private static final int DEFAULT_BRICKS_ROW_NUM = 7;
	private static final int DEFAULT_BRICKS_PER_ROW = 8;
	private static final float BRICK_BUFFER_SIZE = 5;
	private static final int START_LIVES = 3;
	public static final int TARGET_FRAMERATE = 60;
	private static final int MAX_HEARTS = 4;
	private static final float HEART_START = 20;
	private static final float HEART_BUFFER = 30;
	private static final float HEART_SIZE = 25 ;
	private static final float PUCK_MULTIPLAYER = 0.75f;
	private static final float TURBO_FACTOR = 1.4f;
	private static final float HEART_SPEED = 100f;
	private static final int TURBO_MAX_HEATS = 6;

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
	private int turboCounter=0;
	private boolean isTurbo=false;


	public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int rowNum, int bricksPerRow) {
		super(windowTitle, windowDimensions);
		this.rowNum = rowNum;
		this.bricksPerRows = bricksPerRow;
		this.bricksLeft = rowNum*bricksPerRow;
	}

	private void createWalls() {
		GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(WALL_WIDTH, SCREEN_LENGTH), new RectangleRenderable(BORDER_COLOR));
		gameObjects().addGameObject(leftWall, Layer.STATIC_OBJECTS);

		GameObject rightWall = new GameObject(new Vector2(SCREEN_WIDTH - WALL_WIDTH, 0), new Vector2(WALL_WIDTH, SCREEN_LENGTH), new RectangleRenderable(BORDER_COLOR));
		gameObjects().addGameObject(rightWall, Layer.STATIC_OBJECTS);

		GameObject upWall = new GameObject(Vector2.UP, new Vector2(SCREEN_WIDTH, WALL_WIDTH), new RectangleRenderable(BORDER_COLOR));
		gameObjects().addGameObject(upWall, Layer.STATIC_OBJECTS);
	}

	private void createBall(ImageReader imageReader, SoundReader soundReader) {
		ballImage = imageReader.readImage("assets/ball.png", true);
		collisionSound = soundReader.readSound("assets/blop.wav");
		ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE, BALL_SIZE), ballImage, collisionSound);
		gameObjects().addGameObject(ball);
		centerBall(ball);

	}

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

	private void createUserPaddle(ImageReader imageReader, UserInputListener inputListener) {
		paddleImage = imageReader.readImage("assets/paddle.png", true);
		userPaddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_LENGTH, PADDLE_WIDTH), paddleImage, inputListener, SCREEN_WIDTH, "mainPaddle");
		gameObjects().addGameObject(userPaddle);
		userPaddle.setCenter(new Vector2(windowDimensions.x() * SCREEN_CENTER, windowDimensions.y() - PADDLE_Y));
	}

	private void createBricks(ImageReader imageReader, int numRows, int numBricksPerRow) {
		Renderable brickImage = imageReader.readImage("assets/brick.png", false);
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
				GameObject brick = new Brick(brickTopLeft, new Vector2(brickWidth, BRICK_HEIGHT), brickImage, collisionStrategy);
				gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
			}
		}
	}

	private CollisionStrategy randomCollisionStrategy() {
		Random rand = new Random();
		double p = rand.nextDouble();

		if (p < 0.1) {
			return new ExtraBallCollisionStrategy(this);
		} else if (p < 0.2) {
			return new ExtraPaddleCollisionStrategy(this);
		} else if (p < 0.3) {
			return new TurboModeCollisionStrategy(this);
		} else if (p < 0.4) {
			return new ExtraLifeStrategy(this);
		} else if (p < 0.5) {
			return new DoubleActionCollisionStrategy(this);
		} else {
			return new BasicCollisionStrategy(this);

		}

	}

	private void createBackground(ImageReader imageReader) {
		Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", true);
		GameObject background = new GameObject(Vector2.ZERO, new Vector2(SCREEN_WIDTH, SCREEN_LENGTH), backgroundImage);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		gameObjects().addGameObject(background, Layer.BACKGROUND);
	}

	private void addHeart(int index) {
			if (hearts[index] == null) {
				Vector2 heartPos = new Vector2(HEART_START + HEART_BUFFER * index, SCREEN_LENGTH - HEART_BUFFER);
				Heart heart = new Heart(heartPos, new Vector2(HEART_SIZE, HEART_SIZE), heartImage, this);
				hearts[index] = heart;
				gameObjects().addGameObject(heart, Layer.UI);
			}
	}

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


	private void createLivesDisplay(ImageReader imageReader) {
		heartImage = imageReader.readImage("assets/heart.png", true);
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


	public boolean deleteStaticObject(GameObject object) {
		return gameObjects().removeGameObject(object, Layer.STATIC_OBJECTS);
	}

	public void deletePaddleObject(Paddle paddle) {
		gameObjects().removeGameObject(paddle);
		currentExtraPaddle = null;

	}

	public void deleteHeartObject(Heart heart) {
		Iterator<Heart> heartIterator = activeHearts.iterator();
		while (heartIterator.hasNext()) {
			Heart tmpHeart = heartIterator.next();
			if (heart== tmpHeart) {
				gameObjects().removeGameObject(heart);
				heartIterator.remove();
			}
		}
	}

	public void updateBricksCounter() {
		this.bricksLeft -=1;
	}


	public void triggerExtraBall(GameObject object) {
		Renderable puckImage = imageReader.readImage("assets/mockBall.png", true);
		Sound collisionSound = soundReader.readSound("assets/blop.wav");
		for (int i=0; i<2; i++){
			Puck puck = new Puck(Vector2.ZERO, new Vector2(BALL_SIZE* PUCK_MULTIPLAYER, BALL_SIZE*PUCK_MULTIPLAYER), puckImage, collisionSound);
			gameObjects().addGameObject(puck);
			activePucks.add(puck);
			Vector2 center = object.getCenter();
			puck.setCenter(center);
			Random ran = new Random();
			double angle = ran.nextDouble()* Math.PI;
			float velocityX = (float)Math.cos(angle) * BALL_SPEED;
			float velocityY = (float)Math.sin(angle) * BALL_SPEED;
			puck.setVelocity(new Vector2(velocityX, velocityY));

		}

	}

	public void triggerExtraPaddle(GameObject object) {
		if (currentExtraPaddle==null){
			ExtraPaddle extraPaddle = new ExtraPaddle(Vector2.ZERO,
					new Vector2(PADDLE_LENGTH, PADDLE_WIDTH), paddleImage, inputListener, SCREEN_WIDTH, this);
			gameObjects().addGameObject(extraPaddle);
			extraPaddle.setCenter(new Vector2(windowDimensions.x() * SCREEN_CENTER, windowDimensions.y() - SCREEN_LENGTH/2));
			this.currentExtraPaddle = extraPaddle;
		}
	}

	public void triggerTurboMode(){
		if (!isTurbo){
			ball.renderer().setRenderable(imageReader.readImage("assets/redball.png", true));
			ball.setVelocity(ball.getVelocity().mult(TURBO_FACTOR));
			isTurbo = true;
			turboCounter = ball.getCollisionCounter();
		}
	}

	public void restoreTurboMode(){
		if (isTurbo){
			isTurbo = false;
			ball.renderer().setRenderable(imageReader.readImage("assets/ball.png", true));
			ball.setVelocity(ball.getVelocity().mult(1/TURBO_FACTOR));
		}
	}
	
	public void triggerExtraLife(GameObject object){
		Heart heart = new Heart(object.getCenter(), new Vector2(HEART_SIZE, HEART_SIZE), heartImage, this);
		gameObjects().addGameObject(heart);
		heart.setVelocity(new Vector2(0,HEART_SPEED));
		activeHearts.add(heart);
	}
	
	public void addLife(){
		if (lives<MAX_HEARTS){
			lives++;
		}
	}


	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
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
	public void update(float deltaTime) {
		super.update(deltaTime);
		checkLose();
		checkWin();
		updateLives();
		checkPucks();
		checkTurbo();
		checkFallingHearts();
	}

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

	private void checkTurbo() {
		if (isTurbo){
			if (ball.getCollisionCounter()-turboCounter >= TURBO_MAX_HEATS){
				restoreTurboMode();
			}
		}
	}

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

	private void checkWin() {
		if (bricksLeft == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
			if (windowController.openYesNoDialog("You Win! Play again?")) {
				this.lives = START_LIVES;
				this.bricksLeft=rowNum*bricksPerRows;
				restGameHelper();
				windowController.resetGame();
			} else {
				windowController.closeWindow();
			}
		}
	}

	private void checkLose() {
		double ballHeight = ball.getCenter().y();
		if(ballHeight> windowDimensions.y()){
			if(lives > 1){
				centerBall(ball);
				lives -= 1;
			}
			else{
				if(windowController.openYesNoDialog("You lose! Play again?")){
					this.lives = START_LIVES;
					restGameHelper();
					windowController.resetGame();
				}
				else{
					windowController.closeWindow();
				}
			}
		}
	}

	private void restGameHelper(){
		this.currentExtraPaddle = null;
	}

	public static void main(String[] args) {
		if (args.length == 2) {
			int rowNum = Integer.parseInt(args[0]);
			int bricksPerRow = Integer.parseInt(args[1]);
			GameManager gameManager = new BrickerGameManager("Bricker", new Vector2(SCREEN_WIDTH, SCREEN_LENGTH), rowNum, bricksPerRow);
			gameManager.run();
		} else {
			GameManager gameManager = new BrickerGameManager("Bricker", new Vector2(SCREEN_WIDTH, SCREEN_LENGTH), DEFAULT_BRICKS_ROW_NUM, DEFAULT_BRICKS_PER_ROW);
			gameManager.run();
		}
	}

}