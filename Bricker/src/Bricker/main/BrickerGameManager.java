package Bricker.main;

import Bricker.gameobjects.Ball;
import Bricker.gameobjects.UserPaddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

public class BrickerGameManager extends GameManager {
	private static final float BALL_SPEED= 100f;
	private static final float SCREEN_CENTER= 0.5f;
	private static final float BALL_SIZE= 20;
	private static final float SCREEN_LENGTH= 300;
	private static final float SCREEN_WIDTH= 500;
	private static final float PADDLE_LENGTH= 100;
	private static final float PADDLE_WIDTH= 15;
	private static final float PADDLE_Y= 20;
	private static final float WALL_WIDTH= 10;
	private static final Color BORDER_COLOR = Color.CYAN;




	public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
		super(windowTitle, windowDimensions);
	}

	private void createWalls(){
		//create walls
		GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(WALL_WIDTH, SCREEN_LENGTH),new RectangleRenderable(BORDER_COLOR));
		gameObjects().addGameObject(leftWall,Layer.STATIC_OBJECTS);

		GameObject rightWall = new GameObject(new Vector2(SCREEN_WIDTH-WALL_WIDTH, 0), new Vector2(WALL_WIDTH, SCREEN_LENGTH),new RectangleRenderable(BORDER_COLOR));
		gameObjects().addGameObject(rightWall,Layer.STATIC_OBJECTS);

		GameObject upWall = new GameObject(Vector2.UP, new Vector2(SCREEN_WIDTH, WALL_WIDTH),new RectangleRenderable(BORDER_COLOR));
		gameObjects().addGameObject(upWall,Layer.STATIC_OBJECTS);
	}

	private void createBall(ImageReader imageReader, SoundReader soundReader,WindowController windowController){
		Renderable ballImage = imageReader.readImage("assets/ball.png", true);
		Sound collisionSound = soundReader.readSound("assets/blop.wav");
		GameObject ball = new Ball(Vector2.ZERO,new Vector2(BALL_SIZE,BALL_SIZE), ballImage, collisionSound);

		Vector2 windowDimensions = windowController.getWindowDimensions();
		Vector2 center = windowDimensions.mult(SCREEN_CENTER);
		ball.setCenter(center);
		float ballVelX = BALL_SPEED;
		float ballVelY= BALL_SPEED;
		Random ran = new Random();
		if (ran.nextBoolean()){
			ballVelX *= -1;
		}
		if (ran.nextBoolean()){
			ballVelY *= -1;
		}
		ball.setVelocity(new Vector2(ballVelX, ballVelY));
		gameObjects().addGameObject(ball);
	}

	private void createUserPaddle(ImageReader imageReader, WindowController windowController, UserInputListener inputListener){
		Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
		Vector2 windowDimensions = windowController.getWindowDimensions();
		GameObject userPaddle = new UserPaddle(Vector2.ZERO,new Vector2(PADDLE_LENGTH,PADDLE_WIDTH), paddleImage, inputListener, SCREEN_WIDTH);
		gameObjects().addGameObject(userPaddle);
		userPaddle.setCenter(new Vector2(windowDimensions.x()*SCREEN_CENTER,windowDimensions.y()-PADDLE_Y));
	}

	private void createAiPaddle(ImageReader imageReader, WindowController windowController, UserInputListener inputListener){
		Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
		Vector2 windowDimensions = windowController.getWindowDimensions();
		GameObject aiPaddle = new GameObject(Vector2.ZERO,new Vector2(PADDLE_LENGTH,PADDLE_WIDTH), paddleImage);
		gameObjects().addGameObject(aiPaddle);
		aiPaddle.setCenter(new Vector2(windowDimensions.x()*SCREEN_CENTER,PADDLE_Y));
	}

	private void createBackground(ImageReader imageReader){
		Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", true);
		GameObject background = new GameObject(Vector2.ZERO,new Vector2(SCREEN_WIDTH,SCREEN_LENGTH),backgroundImage);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		gameObjects().addGameObject(background, Layer.BACKGROUND);
	}

	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		createBall(imageReader,soundReader, windowController);
		createUserPaddle(imageReader, windowController, inputListener);
		createAiPaddle(imageReader, windowController, inputListener);
		createWalls();
		createBackground(imageReader);
	}

	public static void main(String[] args) {
		GameManager gameManager = new BrickerGameManager("Bricker",
				new Vector2(SCREEN_WIDTH,SCREEN_LENGTH));
		gameManager.run();
	}
}
