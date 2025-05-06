package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * ExtraLifeStrategy class implements the CollisionStrategy interface
 * this strategy gives 2 extra pucks for the player to play with.
 *
 * @author itayt
 */
public class ExtraLifeStrategy implements CollisionStrategy {
	private final BrickerGameManager brickerGameManager;

	/**
	 * Constructor
	 *
	 * @param brickerGameManager used to trigger brick strategy in the gameManager Object
	 */
	public ExtraLifeStrategy(BrickerGameManager brickerGameManager) {
		this.brickerGameManager = brickerGameManager;
	}


	@Override
	/**
	 * function that sets behaviour on collision
	 *
	 * @param object1 first object-got hit
	 * @param object2 second object - the hitter
	 */
	public void onCollision(GameObject object1, GameObject object2) {
		this.brickerGameManager.triggerExtraLife(object1);
		if (this.brickerGameManager.deleteStaticObject(object1)) {
			this.brickerGameManager.updateBricksCounter();
		}
	}
}
