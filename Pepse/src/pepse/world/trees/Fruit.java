package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;

import java.awt.*;
import java.util.Random;

/**
 * Represents a fruit object in the game world that the avatar can collect.
 *
 * <p>Fruits come in one of two random colors and restore energy when collected.
 * After collection, they are removed and can reappear later via the game manager.</p>
 *
 * @author itayturni
 */
public class Fruit extends GameObject {
	/** Diameter of the fruit in pixels */
	public static final int SIZE = 30;

	/** First possible fruit color */
	private static final Color FRUIT_COLOR_1 = new Color(250, 150, 30);

	/** Second possible fruit color */
	private static final Color FRUIT_COLOR_2 = new Color(250, 10, 10);

	/** Energy the avatar gains from collecting the fruit */
	private static final float FRUIT_ENERGY = 10f;

	private final Random rand;
	private final PepseGameManager gameManager;
	private final Avatar avatar;

	/**
	 * Constructs a Fruit object placed at the given top-left corner.
	 *
	 * @param topLeftCorner Position of the fruit
	 * @param avatar        The avatar that can collect this fruit
	 * @param gameManager   Game manager handling fruit reappearance
	 * @param rand          Random instance for color assignment
	 */
	public Fruit(Vector2 topLeftCorner, Avatar avatar, PepseGameManager gameManager, Random rand) {
		super(topLeftCorner, Vector2.ONES.mult(SIZE), new OvalRenderable(FRUIT_COLOR_1));
		this.rand = rand;
		this.avatar = avatar;
		this.gameManager = gameManager;

		if (rand.nextBoolean()) {
			renderer().setRenderable(new OvalRenderable(FRUIT_COLOR_2));
		}

		setTag("fruit");
	}

	/**
	 * Handles the event of the avatar colliding with the fruit.
	 * The fruit grants energy to the avatar and is removed, scheduled for future reappearance.
	 *
	 * @param other     The GameObject that collided with the fruit
	 * @param collision Collision information between the objects
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);

		if ("avatar".equals(other.getTag())) {
			avatar.addEnergy(FRUIT_ENERGY);
			gameManager.removeFruitAndComeBack(this);
		}
	}
}
