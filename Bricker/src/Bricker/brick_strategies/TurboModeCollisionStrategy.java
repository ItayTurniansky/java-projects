package Bricker.brick_strategies;

import Bricker.gameobjects.Puck;
import Bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * TurboModeCollisionStrategy class implements the CollisionStrategy interface
 * this strategy gives 2 extra pucks for the player to play with.
 *
 * @author itayt
 */
public class TurboModeCollisionStrategy implements CollisionStrategy {

	private final BrickerGameManager brickerGameManager;

	/**
	 * Constructor
	 *
	 * @param brickerGameManager used to trigger brick strategy in the gameManager Object
	 */
	public TurboModeCollisionStrategy(BrickerGameManager brickerGameManager) {
		this.brickerGameManager = brickerGameManager;
	}


	@Override
	/**
	 * function that sets behaviour on collision
	 * only triggers special behaviour if hit by main ball
	 * not by puck by checking with instance of
	 *
	 * @param object1 first object-got hit
	 * @param object2 second object - the hitter
	 */
	public void onCollision(GameObject object1, GameObject object2) {
		if (object1 instanceof Puck) {
			if (this.brickerGameManager.deleteStaticObject(object1)) {
				this.brickerGameManager.updateBricksCounter();
			}
			return;
		}
		this.brickerGameManager.triggerTurboMode();
		if (this.brickerGameManager.deleteStaticObject(object1)) {
			this.brickerGameManager.updateBricksCounter();
		}
	}
}
