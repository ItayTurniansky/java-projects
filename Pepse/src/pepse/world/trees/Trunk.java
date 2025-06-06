package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Trunk extends GameObject {
	public static final int TRUNK_HEIGHT = 130;
	public static final int TRUNK_WIDTH = 30;
	private static final Color TRUNK_COLOR = new Color(100, 50, 20);

	public Trunk(Vector2 topLeftCorner) {
		super(topLeftCorner, new Vector2(TRUNK_WIDTH, TRUNK_HEIGHT), new RectangleRenderable(TRUNK_COLOR));
		physics().preventIntersectionsFromDirection(Vector2.ZERO);
		physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
		this.setTag("trunk");
	}
}
