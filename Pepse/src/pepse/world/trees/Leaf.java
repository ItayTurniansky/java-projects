package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Leaf extends GameObject {
	public static final int SIZE = 30;
	private static final Color LEAF_COLOR = new Color(50, 200, 30);

	public Leaf(Vector2 topLeftCorner) {
		super(topLeftCorner, Vector2.ONES.mult(SIZE), new RectangleRenderable(LEAF_COLOR));

	}
}
