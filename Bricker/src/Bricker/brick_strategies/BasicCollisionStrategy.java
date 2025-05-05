package Bricker.brick_strategies;

import Bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * BasicCollisionStrategy class implements the CollisionStrategy interface
 * this strategy is the basic brick behaviour - disappearing when hit.
 *
 * @author itayt
 */
public class BasicCollisionStrategy implements CollisionStrategy {
	private final BrickerGameManager brickerGameManager;

	/**
	 * Constructor
	 *
	 * @param brickerGameManager used to trigger brick strategy in the gameManager Object
	 */

	public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
		this.brickerGameManager = brickerGameManager;
	}

	/**
	 * function that sets behaviour on collision
	 *
	 * @param object1 first object-got hit
	 * @param object2 second object - the hitter
	 */
	@Override
	public void onCollision(GameObject object1, GameObject object2) {
		if (this.brickerGameManager.deleteStaticObject(object1)) {
			this.brickerGameManager.updateBricksCounter();
		}
		;
	}
}
