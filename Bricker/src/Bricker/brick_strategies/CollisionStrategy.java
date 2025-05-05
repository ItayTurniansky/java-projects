package Bricker.brick_strategies;

import danogl.GameObject;

/**
 * CollisionStrategy interface implemented by all strategies to set
 * what happens when a specific brick is hit.
 */
public interface CollisionStrategy {
	void onCollision(GameObject object1, GameObject object2);
}
