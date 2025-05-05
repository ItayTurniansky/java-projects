package Bricker.gameobjects;

import Bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
/**
 * Heart class represents the heart object that appears
 * both in the life counter and in the Extra life bricks
 *
 * @author itayt
 */
public class Heart extends GameObject {
	private static final String MAIN_PADDLE_TAG = "MainPaddle";
	private final BrickerGameManager brickerGameManager;

	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner      Position of the object, in window coordinates (pixels).
	 *                           Note that (0,0) is the top-left corner of the window.
	 * @param dimensions         Width and height in window coordinates.
	 * @param renderable         The renderable representing the object. Can be null, in which case
	 *                           the GameObject will not be rendered.
	 * @param brickerGameManager gameManager to trigger movement of the heart in screen
	 */
	public Heart(Vector2 topLeftCorner, Vector2 dimensions,
				 Renderable renderable, BrickerGameManager brickerGameManager) {
		super(topLeftCorner, dimensions, renderable);
		this.brickerGameManager = brickerGameManager;
	}


	@Override
	/**
	 * @param other The other GameObject.
	 * @return true if the object that touched the heart is the main paddle
	 */
	public boolean shouldCollideWith(GameObject other) {
		return other.getTag().equals(MAIN_PADDLE_TAG);
	}


	@Override
	/**
	 * @param other     The GameObject with which a collision occurred.
	 * @param collision Information regarding this collision.
	 *                  A reasonable elastic behavior can be achieved with:
	 *                  setVelocity(getVelocity().flipped(collision.getNormal()));
	 */
	public void onCollisionEnter(GameObject other, Collision collision) {
		this.brickerGameManager.addLife();
		this.brickerGameManager.deleteHeartObject(this);
	}

}
