package Bricker.brick_strategies;

import Bricker.main.BrickerGameManager;
import danogl.GameObject;

public class ExtraPaddleCollisionStrategy implements CollisionStrategy {
	private final BrickerGameManager brickerGameManager;

	public ExtraPaddleCollisionStrategy(BrickerGameManager brickerGameManager) {
		this.brickerGameManager=brickerGameManager;
	}

	@Override
	public void onCollision(GameObject object1, GameObject object2) {
		this.brickerGameManager.triggerExtraPaddle(object1);
		if (this.brickerGameManager.deleteStaticObject(object1)){
			this.brickerGameManager.updateBricksCounter();
		};
	}
}
