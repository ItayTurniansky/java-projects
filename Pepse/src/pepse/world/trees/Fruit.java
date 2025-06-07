package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;

import java.awt.*;
import java.util.Random;

public class Fruit extends GameObject {
	public static final int SIZE = 30;
	private static final Color FRUIT_COLOR_1 = new Color(250, 150, 30);
	private static final Color FRUIT_COLOR_2 = new Color(250, 10, 10);
	private static final float FRUIT_ENERGY = 10;

	private Random rand;
	private PepseGameManager gameManager;
	private Avatar avatar;

	public Fruit(Vector2 topLeftCorner, Avatar avatar, PepseGameManager gameManager, Random rand) {
		super(topLeftCorner, Vector2.ONES.mult(SIZE), new OvalRenderable(FRUIT_COLOR_1));
		this.rand = rand;
		this.avatar = avatar;
		this.gameManager = gameManager;
		if (rand.nextBoolean()) {
			this.renderer().setRenderable(new OvalRenderable(FRUIT_COLOR_2));
		}
		this.setTag("fruit");
	}

	/**
	 * Called on the first frame of a collision.
	 *
	 * @param other     The GameObject with which a collision occurred.
	 * @param collision Information regarding this collision.
	 *                  A reasonable elastic behavior can be achieved with:
	 *                  setVelocity(getVelocity().flipped(collision.getNormal()));
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if (other.getTag().equals("avatar")) {
			avatar.addEnergy(FRUIT_ENERGY);
			gameManager.removeFruitAndComeBack(this);
		}
	}
}
