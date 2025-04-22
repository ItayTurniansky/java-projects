package Bricker.main;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class BrickerGameManager extends GameManager {
	private static final float BALL_SPEED= 100f;
	private static final float SCREEN_CENTER= 0.5f;
	private static final float BALL_SIZE= 20;
	private static final float SCREEN_SIZE= 700;
	private static final float PADDLE_LENGTH= 100;
	private static final float PADDLE_WIDTH= 15;
	private static final float PADDLE_Y= 20;




	public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
		super(windowTitle, windowDimensions);
	}

	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
		//creating ball
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		Renderable ballImage = imageReader.readImage("assets/ball.png", true);
		GameObject ball = new GameObject(Vector2.ZERO,new Vector2(BALL_SIZE,BALL_SIZE), ballImage);
		ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
		Vector2 windowDimensions = windowController.getWindowDimensions();
		Vector2 center = windowDimensions.mult(SCREEN_CENTER);
		ball.setCenter(center);
		gameObjects().addGameObject(ball);

		//creating paddle
		Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
		GameObject paddle = new GameObject(Vector2.ZERO,new Vector2(PADDLE_LENGTH,PADDLE_WIDTH), paddleImage);
		gameObjects().addGameObject(paddle);
		paddle.setCenter(new Vector2(windowDimensions.x()*SCREEN_CENTER,windowDimensions.y()-PADDLE_Y));


	}

	public static void main(String[] args) {
		GameManager gameManager = new BrickerGameManager("Bricker",
				new Vector2(SCREEN_SIZE,SCREEN_SIZE));
		gameManager.run();
	}
}
