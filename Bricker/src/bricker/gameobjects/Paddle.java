package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


import java.awt.event.KeyEvent;

/**
 * Paddle class represents a Paddle extends GameObject.
 *
 * @author itayt
 */
public class Paddle extends GameObject {
	/*
	Paddle speed, keyboard listener and window width
	 */
	private static final float PADDLE_SPEED = 500;
	private final UserInputListener inputListener;
	private final float windowWidth;

	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner Position of the object, in window coordinates (pixels).
	 *                      Note that (0,0) is the top-left corner of the window.
	 * @param dimensions    Width and height in window coordinates.
	 * @param renderable    The renderable representing the object. Can be null, in which case
	 *                      the GameObject will not be rendered.
	 * @param inputListener controlling keyboard keys.
	 * @param windowWidth   width of screen to control paddle
	 * @param tag           tagging the paddle type to make sure it collides with proper object.
	 */
	public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				  UserInputListener inputListener, float windowWidth, String tag) {
		super(topLeftCorner, dimensions, renderable);
		this.inputListener = inputListener;
		this.windowWidth = windowWidth;
		this.setTag(tag);
	}



	@Override
	/**
	 * controls the paddle
	 *
	 * @param deltaTime The time elapsed, in seconds, since the last frame. Can
	 *                  be used to determine a new position/velocity by multiplying
	 *                  this delta with the velocity/acceleration respectively
	 *                  and adding to the position/velocity:
	 *                  velocity += deltaTime*acceleration
	 *                  pos += deltaTime*velocity
	 */
	public void update(float deltaTime) {
		super.update(deltaTime);
		Vector2 movementDir = Vector2.ZERO;
		if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
			movementDir = movementDir.add(Vector2.LEFT);
		}
		if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
			movementDir = movementDir.add(Vector2.RIGHT);
		}
		setVelocity(movementDir.mult(PADDLE_SPEED));

		Vector2 topLeft = getTopLeftCorner();
		Vector2 dimensions = getDimensions();
		if (topLeft.x() < 0) {
			setTopLeftCorner(new Vector2(0, topLeft.y()));
		} else if (topLeft.x() + dimensions.x() > windowWidth) {
			setTopLeftCorner(new Vector2(windowWidth - dimensions.x(), topLeft.y()));
		}
	}

}
