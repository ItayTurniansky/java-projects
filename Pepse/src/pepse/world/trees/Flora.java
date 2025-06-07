package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;
import pepse.world.Terrain;

import java.util.*;

public class Flora {
	private static final float TREE_PROBABILITY = 0.2f;
	private static final int TREE_SPACING = 4 * Leaf.SIZE;

	private Terrain terrain;
	private Avatar avatar;
	private PepseGameManager gameManager;
	private int seed;
	private final Set<Integer> generatedTreeXs = new HashSet<>();

	public Flora(Terrain terrain, Avatar avatar, PepseGameManager gameManager, Random rand, int seed) {
		this.terrain = terrain;
		this.avatar = avatar;
		this.gameManager = gameManager;
		this.seed = seed;
	}

	public List<GameObject> createInRange(int minX, int maxX, Random chunkRandom) {
		List<GameObject> gameObjects = new ArrayList<>();

		int start = (int) (Math.ceil(minX / (double) TREE_SPACING) * TREE_SPACING);
		int end = (int) (Math.floor(maxX / (double) TREE_SPACING) * TREE_SPACING);

		for (int i = start; i <= end; i += TREE_SPACING) {
			Random localRand = new Random(Objects.hash(i, seed));
			if (localRand.nextFloat() < TREE_PROBABILITY) {
				Tree tree = new Tree(new Vector2(i, terrain.groundHeightAt(i)), avatar, gameManager, seed);
				gameObjects.addAll(tree.getGameObjects());
			}
		}

		return gameObjects;
	}
}
