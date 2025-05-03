package Bricker.brick_strategies;

import Bricker.main.BrickerGameManager;
import danogl.GameObject;

import java.util.Random;

public class DoubleActionCollisionStrategy implements CollisionStrategy {
	private static final int MAX_STRATEGIES = 3;
	private final BrickerGameManager brickerGameManager;
	private final CollisionStrategy[] collisionStrategies;

	public DoubleActionCollisionStrategy(BrickerGameManager brickerGameManager) {
		this.brickerGameManager=brickerGameManager;
		this.collisionStrategies = new CollisionStrategy[MAX_STRATEGIES];
		Random rand = new Random();
		for (int i = 0; i < 2; i++) {
			double p = rand.nextDouble();
			if (p < 0.2) {
				collisionStrategies[i] = new TurboModeCollisionStrategy(this.brickerGameManager);
			} else if (p < 0.4) {
				collisionStrategies[i] = new ExtraBallCollisionStrategy(this.brickerGameManager);
			} else if (p < 0.6) {
				collisionStrategies[i] = new ExtraLifeStrategy(this.brickerGameManager);
			} else if (p < 0.8) {
				collisionStrategies[i] = new ExtraPaddleCollisionStrategy(this.brickerGameManager);
			} else {
				CollisionStrategy[] twoCollisionsStrategies = drawnDoubleAction();
				for( CollisionStrategy strategy : twoCollisionsStrategies) {
					collisionStrategies[i] = strategy;
				}
				break;
			}
		}

	}

	private CollisionStrategy[] drawnDoubleAction(){
		CollisionStrategy[] returnCollisionsStrategies = new CollisionStrategy[2];
		Random rand = new Random();
		for (int i = 0; i < 2; i++) {
			double p = rand.nextDouble();
			if (p < 0.25) {
				returnCollisionsStrategies[i] = new TurboModeCollisionStrategy(this.brickerGameManager);
			} else if (p < 0.5) {
				returnCollisionsStrategies[i] = new ExtraBallCollisionStrategy(this.brickerGameManager);
			} else if (p < 0.75) {
				returnCollisionsStrategies[i] = new ExtraLifeStrategy(this.brickerGameManager);
			} else {
				returnCollisionsStrategies[i] = new ExtraPaddleCollisionStrategy(this.brickerGameManager);
			}
		}
		return returnCollisionsStrategies;
	}

	@Override
	public void onCollision(GameObject object1, GameObject object2) {
		for (CollisionStrategy strategy : this.collisionStrategies) {
			if (strategy!=null){
				strategy.onCollision(object1, object2);
			}
		}
	}
}