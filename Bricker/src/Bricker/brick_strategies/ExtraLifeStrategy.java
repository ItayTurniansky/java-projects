package Bricker.brick_strategies;

import Bricker.gameobjects.Puck;
import Bricker.main.BrickerGameManager;
import danogl.GameObject;

public class ExtraLifeStrategy implements CollisionStrategy {
	private final BrickerGameManager brickerGameManager;

	public ExtraLifeStrategy(BrickerGameManager brickerGameManager) {
		this.brickerGameManager=brickerGameManager;
	}

	@Override
	public void onCollision(GameObject object1, GameObject object2) {
		this.brickerGameManager.triggerExtraLife(object1);
		if (this.brickerGameManager.deleteStaticObject(object1)){
			this.brickerGameManager.updateBricksCounter();
		}
	}
}
