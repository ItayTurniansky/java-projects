package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree {
	private static final int LEAF_GRID_SIZE = 10; // 5x5 square of leaves
	private static final float LEAF_FILL_PROBABILITY = 0.6f;

	private Random rand = new Random();
	private Trunk trunk;
	private List<Leaf> leaves;

	public Tree(Vector2 basePosition) {
		float trunkTopY = basePosition.y() - Trunk.TRUNK_HEIGHT;
		this.trunk = new Trunk(new Vector2(basePosition.x(), basePosition.y() - Trunk.TRUNK_HEIGHT));
		trunk.setTag("trunk");
		this.leaves = new ArrayList<>();

		float centerX = basePosition.x() + Trunk.TRUNK_WIDTH / 2f;
		float centerY = trunkTopY - Leaf.SIZE / 2f;  // raised cluster

		int halfGrid = LEAF_GRID_SIZE / 2;

		for (int dx = -halfGrid; dx <= halfGrid; dx++) {
			for (int dy = -halfGrid; dy <= halfGrid; dy++) {
				if (Math.abs(dx) + Math.abs(dy) <= 2 && rand.nextFloat() < LEAF_FILL_PROBABILITY) {
					float leafX = centerX + dx * Leaf.SIZE - Leaf.SIZE / 2f;
					float leafY = centerY + dy * Leaf.SIZE;
					Vector2 leafPos = new Vector2(leafX, leafY);
					Leaf leaf = new Leaf(leafPos);
					leaf.setTag("leaf");
					leaves.add(leaf);
				}
			}
		}

	}


	public List<GameObject> getGameObjects() {
		List<GameObject> objs = new ArrayList<>();
		objs.add(trunk);
		objs.addAll(leaves);
		return objs;
	}
}
