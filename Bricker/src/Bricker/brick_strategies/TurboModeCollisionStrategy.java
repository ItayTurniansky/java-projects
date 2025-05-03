package Bricker.brick_strategies;

import Bricker.gameobjects.Puck;
import Bricker.main.BrickerGameManager;
import danogl.GameObject;

public class TurboModeCollisionStrategy implements CollisionStrategy {

	private final BrickerGameManager brickerGameManager;

	public TurboModeCollisionStrategy(BrickerGameManager brickerGameManager) {
			this.brickerGameManager=brickerGameManager;
		}

		@Override
		public void onCollision(GameObject object1, GameObject object2) {
			if (object1 instanceof Puck ){
				if (this.brickerGameManager.deleteStaticObject(object1)){
					this.brickerGameManager.updateBricksCounter();
				}
				return;
			}
			this.brickerGameManager.triggerTurboMode();
			if (this.brickerGameManager.deleteStaticObject(object1)){
				this.brickerGameManager.updateBricksCounter();
			}
		}
}
