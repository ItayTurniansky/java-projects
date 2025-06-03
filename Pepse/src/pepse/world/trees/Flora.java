package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flora {
	private static final float TREE_PROBABILITY = 0.2f;
	private static final int TREE_SPACING = 3 * Leaf.SIZE;

	private Random rand = new Random();
	private Terrain terrain;

	public Flora(Terrain terrain) {
		this.terrain = terrain;

	}

	public List<GameObject> createInRange(int minX, int maxX){
		List<GameObject> gameObjects = new ArrayList<>();
		for (int i = minX; i <= maxX; i += TREE_SPACING) {
			if (rand.nextFloat()<TREE_PROBABILITY){
				Tree tree = new Tree(new Vector2(i, terrain.groundHeightAt(i)));
				gameObjects.addAll(tree.getGameObjects());
			}
		}
		return gameObjects;
	}
}
