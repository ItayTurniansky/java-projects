package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;
import pepse.world.Terrain;

import java.util.*;

/**
 * Manages the procedural generation of trees (flora) in a specific range of the game world.
 *
 * <p>Uses a fixed tree spacing and seed-based randomness to ensure consistent tree layout across runs.
 * Each tree consists of a trunk, leaves, and possibly fruits, and is added to the world based on a
 * probability.</p>
 *
 * @author itayturni
 */
public class Flora {
	/** Probability to generate a tree at each available grid location */
	private static final float TREE_PROBABILITY = 0.2f;

	/** Horizontal spacing between possible tree positions */
	private static final int TREE_SPACING = 4 * Leaf.SIZE;

	private final Terrain terrain;
	private final Avatar avatar;
	private final PepseGameManager gameManager;
	private final int seed;
	private final Set<Integer> generatedTreeXs = new HashSet<>();

	/**
	 * Constructs a Flora generator tied to a specific terrain, avatar, and game manager.
	 *
	 * @param terrain     The terrain on which to place trees
	 * @param avatar      The player avatar, for fruit interaction
	 * @param gameManager The game manager, used for fruit scheduling
	 * @param rand        Unused (but expected) random instance for future expansion
	 * @param seed        Seed to ensure deterministic tree layout
	 */
	public Flora(Terrain terrain, Avatar avatar, PepseGameManager gameManager, Random rand, int seed) {
		this.terrain = terrain;
		this.avatar = avatar;
		this.gameManager = gameManager;
		this.seed = seed;
	}

	/**
	 * Generates all tree GameObjects to appear within a horizontal range.
	 *
	 * @param minX         The minimum X coordinate to consider
	 * @param maxX         The maximum X coordinate to consider
	 * @param chunkRandom  Random generator for future extensions (currently unused)
	 * @return A list of GameObjects representing trunks, leaves, and fruits
	 */
	public List<GameObject> createInRange(int minX, int maxX, Random chunkRandom) {
		List<GameObject> gameObjects = new ArrayList<>();

		int start = (int) (Math.ceil(minX / (double) TREE_SPACING) * TREE_SPACING);
		int end = (int) (Math.floor(maxX / (double) TREE_SPACING) * TREE_SPACING);

		for (int i = start; i <= end; i += TREE_SPACING) {
			Random localRand = new Random(Objects.hash(i, seed));
			if (localRand.nextFloat() < TREE_PROBABILITY) {
				Tree tree = new Tree(
						new Vector2(i, terrain.groundHeightAt(i)),
						avatar,
						gameManager,
						seed
				);
				gameObjects.addAll(tree.getGameObjects());
			}
		}

		return gameObjects;
	}
}
