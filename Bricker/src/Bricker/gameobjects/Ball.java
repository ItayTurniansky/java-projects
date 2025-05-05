package Bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Ball class represents the main game ball extends game object
 *
 * @author itayt
 */
public class Ball extends GameObject {
	/*
	hit counter and collision sound fields
	 */
	private static int collisionCounter = 0;
	private final Sound collisionSound;


	/**
	 * Construct a new Ball instance.
	 *
	 * @param topLeftCorner  Position of the object, in window coordinates (pixels).
	 *                       Note that (0,0) is the top-left corner of the window.
	 * @param dimensions     Width and height in window coordinates.
	 * @param renderable     The renderable representing the object. Can be null, in which case
	 *                       the GameObject will not be rendered.
	 * @param collisionSound the sound to be made when ball hits.
	 */
	public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				Sound collisionSound) {
		super(topLeftCorner, dimensions, renderable);
		this.collisionSound = collisionSound;
	}

	/**
	 * plays sound when ball hits, changes its velocity and direction
	 *
	 * @param other     The GameObject with which a collision occurred.
	 * @param collision Information regarding this collision.
	 *                  A reasonable elastic behavior can be achieved with:
	 *                  setVelocity(getVelocity().flipped(collision.getNormal()));
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		//setVelocity();
		Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
		setVelocity(newVelocity);
		collisionSound.play();
		collisionCounter++;

	}

	/**
	 * @return collision counter
	 */

	public int getCollisionCounter() {
		return collisionCounter;
	}
}
