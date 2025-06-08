package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a full tree structure in the game world, composed of a trunk, leaves, and optional fruits.
 *
 * <p>Leaves are placed in a grid pattern with random animation and appearance. Fruits are placed with
 * a separate probability and are interactable via the Avatar.</p>
 *
 * @author itayturni
 */
public class Tree {
	private static final int LEAF_GRID_SIZE = 10;
	private static final float LEAF_FILL_PROBABILITY = 0.8f;
	private static final float FRUIT_FILL_PROBABILITY = 0.7f;
	private static final float MAX_LEAF_ANGLE = 15f;
	private static final float LEAF_MOVE_CYCLE = 1.2f;
	private static final float LEAF_MOVE_MAX = 10f;
	private static final float MIN_LEAF_DELAY = 0.2f;
	private static final float MAX_LEAF_DELAY = 2.0f;
	private static final float LEAF_FACTOR_1 = 0.8f;
	private static final float LEAF_FACTOR_2 = 0.4f;

	private final Trunk trunk;
	private final List<Leaf> leaves;
	private final List<Fruit> fruits;

	/**
	 * Constructs a Tree object with a trunk, leaves, and fruits generated in a reproducible,
	 * seed-based layout.
	 *
	 * @param basePosition The bottom-center position of the tree trunk
	 * @param avatar       The player avatar for fruit interaction
	 * @param gameManager  The game manager for context and scheduling
	 * @param seed         The seed used for deterministic randomness
	 */
	public Tree(Vector2 basePosition, Avatar avatar, PepseGameManager gameManager, int seed) {
		float trunkTopY = basePosition.y() - Trunk.TRUNK_HEIGHT;
		this.trunk = new Trunk(new Vector2(basePosition.x(), trunkTopY));
		this.leaves = new ArrayList<>();
		this.fruits = new ArrayList<>();

		float centerX = basePosition.x() + Trunk.TRUNK_WIDTH / 2f;
		float centerY = trunkTopY - Leaf.SIZE / 2f;
		int halfGrid = LEAF_GRID_SIZE / 2;

		for (int dx = -halfGrid; dx <= halfGrid; dx++) {
			for (int dy = -halfGrid; dy <= halfGrid; dy++) {
				if (Math.abs(dx) + Math.abs(dy) <= 2) {
					int hash = Objects.hash((int) basePosition.x(), dx, dy, seed);
					Random cellRand = new Random(hash);
					float chance = cellRand.nextFloat();

					float objectX = centerX + dx * Leaf.SIZE - Leaf.SIZE / 2f;
					float objectY = centerY + dy * Leaf.SIZE;
					Vector2 objectPos = new Vector2(objectX, objectY);

					if (chance < LEAF_FILL_PROBABILITY) {
						Leaf leaf = new Leaf(objectPos);
						leaves.add(leaf);

						float waitTime = MIN_LEAF_DELAY +
								cellRand.nextFloat() * (MAX_LEAF_DELAY - MIN_LEAF_DELAY);
						float leafAngle = MAX_LEAF_ANGLE *
								(LEAF_FACTOR_1 + cellRand.nextFloat() * LEAF_FACTOR_2);
						float leafCycle = LEAF_MOVE_CYCLE *
								(LEAF_FACTOR_1 + cellRand.nextFloat() * LEAF_FACTOR_2);

						new ScheduledTask(
								leaf,
								waitTime,
								false,
								() -> {
									new Transition<>(
											leaf,
											angle -> leaf.renderer().setRenderableAngle(angle),
											0f,
											leafAngle,
											Transition.LINEAR_INTERPOLATOR_FLOAT,
											leafCycle,
											Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
											null
									);
									new Transition<>(
											leaf,
											stretch -> leaf.setDimensions(
													new Vector2(Leaf.SIZE + stretch, Leaf.SIZE)
											),
											0f,
											LEAF_MOVE_MAX,
											Transition.LINEAR_INTERPOLATOR_FLOAT,
											leafCycle,
											Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
											null
									);
								}
						);
					} else if (chance < LEAF_FILL_PROBABILITY + FRUIT_FILL_PROBABILITY) {
						Fruit fruit = new Fruit(objectPos, avatar, gameManager, cellRand);
						fruits.add(fruit);
					}
				}
			}
		}
	}

	/**
	 * Returns all GameObjects (trunk, leaves, fruits) that make up the tree.
	 *
	 * @return A list of GameObjects representing the full tree
	 */
	public List<GameObject> getGameObjects() {
		List<GameObject> objs = new ArrayList<>();
		objs.add(trunk);
		objs.addAll(leaves);
		objs.addAll(fruits);
		return objs;
	}
}
