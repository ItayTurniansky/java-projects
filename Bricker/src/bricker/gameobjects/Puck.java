package bricker.gameobjects;

import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Puck class to separate the puck from the main ball when
 * touching a turbo mode brick.
 *
 * @author itayt
 */
public class Puck extends Ball {

	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner  Position of the object, in window coordinates (pixels).
	 *                       Note that (0,0) is the top-left corner of the window.
	 * @param dimensions     Width and height in window coordinates.
	 * @param renderable     The renderable representing the object. Can be null, in which case
	 *                       the GameObject will not be rendered.
	 * @param collisionSound sound made when colliding
	 */
	public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				Sound collisionSound) {
		super(topLeftCorner, dimensions, renderable, collisionSound);
	}

}
