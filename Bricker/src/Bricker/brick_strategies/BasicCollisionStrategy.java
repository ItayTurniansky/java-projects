package Bricker.brick_strategies;

import Bricker.main.BrickerGameManager;
import danogl.GameObject;

public class BasicCollisionStrategy implements CollisionStrategy {


	private final BrickerGameManager brickerGameManager;

	public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
		this.brickerGameManager = brickerGameManager;
	}

	@Override
	public void onCollision(GameObject object1, GameObject object2) {
		this.brickerGameManager.deleteObject(object1);
		this.brickerGameManager.updateBricksCounter();
	}
}
