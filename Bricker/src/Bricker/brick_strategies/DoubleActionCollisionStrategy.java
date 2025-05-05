package Bricker.brick_strategies;

import Bricker.main.BrickerGameManager;
import danogl.GameObject;

import java.util.Random;

/**
 * DoubleActionCollisionStrategy implements the CollisionStrategy interface
 * this strategy is used for bricks that has multiple strategies - max 3
 * it randomly selects 2 or 3 strategies (3 if in the first draw it got DoubleAction again)
 *
 * @author itayt
 */
public class DoubleActionCollisionStrategy implements CollisionStrategy {
	private static final int MAX_STRATEGIES = 3;
	private static final int EXTRA_ACTION_INDEX = 2;
	private static final double TURBO_MODE_ODDS = 0.2;
	private static final double TURBO_MODE_ODDS_IF_DOUBLE = 0.25;
	private static final double EXTRA_BALL_ODDS = 0.4;
	private static final double EXTRA_BALL_ODDS_IF_DOUBLE = 0.5;
	private static final double EXTRA_LIFE_ODDS = 0.6;
	private static final double EXTRA_LIFE_ODDS_IF_DOUBLE = 0.75;
	private static final double EXTRA_PADDLE_ODDS = 0.8;
	private final BrickerGameManager brickerGameManager;
	private final CollisionStrategy[] collisionStrategies;

	/**
	 * Constructor - generates the random strategies
	 *
	 * @param brickerGameManager used to trigger brick strategy in the gameManager Object
	 */
	public DoubleActionCollisionStrategy(BrickerGameManager brickerGameManager) {
		this.brickerGameManager = brickerGameManager;
		this.collisionStrategies = new CollisionStrategy[MAX_STRATEGIES];
		Random rand = new Random();
		for (int i = 0; i < EXTRA_ACTION_INDEX; i++) {
			double p = rand.nextDouble();
			if (p < TURBO_MODE_ODDS) {
				collisionStrategies[i] = new TurboModeCollisionStrategy(this.brickerGameManager);
			} else if (p < EXTRA_BALL_ODDS) {
				collisionStrategies[i] = new ExtraBallCollisionStrategy(this.brickerGameManager);
			} else if (p < EXTRA_LIFE_ODDS) {
				collisionStrategies[i] = new ExtraLifeStrategy(this.brickerGameManager);
			} else if (p < EXTRA_PADDLE_ODDS) {
				collisionStrategies[i] = new ExtraPaddleCollisionStrategy(this.brickerGameManager);
			} else {
				CollisionStrategy[] twoCollisionsStrategies = drawnDoubleAction();
				collisionStrategies[i] = twoCollisionsStrategies[0];
				collisionStrategies[EXTRA_ACTION_INDEX] = twoCollisionsStrategies[1];
			}
		}

	}


	@Override
	/**
	 * function that sets behaviour on collision-activates all strategies of brick
	 *
	 * @param object1 first object-got hit
	 * @param object2 second object - the hitter
	 */
	public void onCollision(GameObject object1, GameObject object2) {
		for (CollisionStrategy strategy : this.collisionStrategies) {
			if (strategy != null) {
				strategy.onCollision(object1, object2);
			}
		}
	}

	/*
	private help function that handles the cast where Double Action is
	drawn twice - draw two new strategies without double action.
	 */
	private CollisionStrategy[] drawnDoubleAction() {
		CollisionStrategy[] returnCollisionsStrategies = new CollisionStrategy[2];
		Random rand = new Random();
		for (int i = 0; i < EXTRA_ACTION_INDEX; i++) {
			double p = rand.nextDouble();
			if (p < TURBO_MODE_ODDS_IF_DOUBLE) {
				returnCollisionsStrategies[i] = new TurboModeCollisionStrategy(this.brickerGameManager);
			} else if (p < EXTRA_BALL_ODDS_IF_DOUBLE) {
				returnCollisionsStrategies[i] = new ExtraBallCollisionStrategy(this.brickerGameManager);
			} else if (p < EXTRA_LIFE_ODDS_IF_DOUBLE) {
				returnCollisionsStrategies[i] = new ExtraLifeStrategy(this.brickerGameManager);
			} else {
				returnCollisionsStrategies[i] = new ExtraPaddleCollisionStrategy(this.brickerGameManager);
			}
		}
		return returnCollisionsStrategies;
	}
}