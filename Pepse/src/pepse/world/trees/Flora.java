package pepse.world.trees;

import danogl.GameManager;
import danogl.GameObject;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flora {
	private static final float TREE_PROBABILITY = 0.2f;
	private static final int TREE_SPACING = 3 * Leaf.SIZE;

	private Random rand = new Random();
	private Terrain terrain;
	private Avatar avatar;
	private PepseGameManager gameManager;

	public Flora(Terrain terrain, Avatar avatar, PepseGameManager gameManager) {
		this.terrain = terrain;
		this.avatar = avatar;
		this.gameManager = gameManager;

	}

	public List<GameObject> createInRange(int minX, int maxX){
		List<GameObject> gameObjects = new ArrayList<>();
		for (int i = minX; i <= maxX; i += TREE_SPACING) {
			if (rand.nextFloat()<TREE_PROBABILITY){
				Tree tree = new Tree(new Vector2(i, terrain.groundHeightAt(i)), avatar, gameManager);
				gameObjects.addAll(tree.getGameObjects());
			}
		}
		return gameObjects;
	}
}
