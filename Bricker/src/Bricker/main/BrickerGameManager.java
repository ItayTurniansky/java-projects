package Bricker.main;

import Bricker.gameobjects.Ball;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

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


	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
		//creating ball
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		Renderable ballImage = imageReader.readImage("assets/ball.png", true);
		GameObject ball = new Ball(Vector2.ZERO,new Vector2(BALL_SIZE,BALL_SIZE), ballImage);
		ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
		Vector2 windowDimensions = windowController.getWindowDimensions();
		Vector2 center = windowDimensions.mult(SCREEN_CENTER);
		ball.setCenter(center);
		gameObjects().addGameObject(ball);


		//creating paddles
		float[] paddleHeights = new float[]{windowDimensions.y()-PADDLE_Y, PADDLE_Y};
		Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
		for (int i = 0; i < paddleHeights.length; i++) {
			GameObject paddle = new GameObject(Vector2.ZERO,new Vector2(PADDLE_LENGTH,PADDLE_WIDTH), paddleImage);
			gameObjects().addGameObject(paddle);
			paddle.setCenter(new Vector2(windowDimensions.x()*SCREEN_CENTER,paddleHeights[i]));
		}

		createWalls();
		Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", true);
		GameObject background = new GameObject(Vector2.ZERO,new Vector2(SCREEN_WIDTH,SCREEN_LENGTH),backgroundImage);
		background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		gameObjects().addGameObject(background, Layer.BACKGROUND);






	}

	public static void main(String[] args) {
		GameManager gameManager = new BrickerGameManager("Bricker",
				new Vector2(SCREEN_WIDTH,SCREEN_LENGTH));
		gameManager.run();
	}
}
