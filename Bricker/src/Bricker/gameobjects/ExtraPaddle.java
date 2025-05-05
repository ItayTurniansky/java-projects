package Bricker.gameobjects;

import Bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class ExtraPaddle extends Paddle{
	private int hitsCounter;
	private final BrickerGameManager brickerGameManager;

	private static final int MAX_PADDLE_HITS =4;


	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner Position of the object, in window coordinates (pixels).
	 *                      Note that (0,0) is the top-left corner of the window.
	 * @param dimensions    Width and height in window coordinates.
	 * @param renderable    The renderable representing the object. Can be null, in which case
	 *                      the GameObject will not be rendered.
	 * @param inputListener
	 * @param windowWidth
	 */
	public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener, float windowWidth, BrickerGameManager brickerGameManager) {
		super(topLeftCorner, dimensions, renderable, inputListener, windowWidth, "ExtraPaddle");
		this.brickerGameManager = brickerGameManager;
		this.hitsCounter = MAX_PADDLE_HITS;
	}

	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if (other instanceof Ball){
			if (this.hitsCounter == 1){
				this.brickerGameManager.deletePaddleObject(this);
				return;
			}
			this.hitsCounter -=1 ;
		}


	}
}
