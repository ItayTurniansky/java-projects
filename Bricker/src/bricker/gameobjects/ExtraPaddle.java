package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * ExtraPaddle class represents the second paddle that appears
 * when ExtraPaddle strategy is triggered. extends Paddle
 *
 * @author itayt
 */
public class ExtraPaddle extends Paddle {
	private static final String EXTRA_PADDLE_TAG = "ExtraPaddle";
	private int hitsCounter;
	private final BrickerGameManager brickerGameManager;
	private static final int MAX_PADDLE_HITS = 4;


	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner      Position of the object, in window coordinates (pixels).
	 *                           Note that (0,0) is the top-left corner of the window.
	 * @param dimensions         Width and height in window coordinates.
	 * @param renderable         The renderable representing the object. Can be null, in which case
	 *                           the GameObject will not be rendered.
	 * @param inputListener      keyboard keys controller
	 * @param windowWidth        width of window
	 * @param brickerGameManager gameManager to trigger movement of the paddle in screen
	 */
	public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
					   UserInputListener inputListener, float windowWidth,
					   BrickerGameManager brickerGameManager) {
		super(topLeftCorner, dimensions, renderable, inputListener, windowWidth, EXTRA_PADDLE_TAG);
		this.brickerGameManager = brickerGameManager;
		this.hitsCounter = MAX_PADDLE_HITS;
	}


	@Override
	/**
	 * triggers action only when hit by a ball/puck
	 *
	 * @param other     The GameObject with which a collision occurred.
	 * @param collision Information regarding this collision.
	 *                  A reasonable elastic behavior can be achieved with:
	 *                  setVelocity(getVelocity().flipped(collision.getNormal()));
	 */
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if (other instanceof Ball) {
			if (this.hitsCounter == 1) {
				this.brickerGameManager.deletePaddleObject(this);
				return;
			}
			this.hitsCounter -= 1;
		}


	}
}
