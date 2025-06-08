package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents a single tree trunk GameObject in the game world.
 * The trunk is a vertical rectangle with fixed dimensions and an immovable mass.
 *
 * <p>The trunk is used as the base of a tree structure in the world.</p>
 *
 * @author itayturni
 */
public class Trunk extends GameObject {
	/** Height of the trunk in pixels */
	public static final int TRUNK_HEIGHT = 130;

	/** Width of the trunk in pixels */
	public static final int TRUNK_WIDTH = 30;

	/** Color used to render the trunk */
	private static final Color TRUNK_COLOR = new Color(100, 50, 20);

	/**
	 * Constructs a new Trunk object located at the given top-left corner.
	 * The trunk is rendered as a brown rectangle and does not move or allow intersection.
	 *
	 * @param topLeftCorner The top-left position of the trunk in the game world
	 */
	public Trunk(Vector2 topLeftCorner) {
		super(
				topLeftCorner,
				new Vector2(TRUNK_WIDTH, TRUNK_HEIGHT),
				new RectangleRenderable(TRUNK_COLOR)
		);
		physics().preventIntersectionsFromDirection(Vector2.ZERO);
		physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
		setTag("trunk");
	}
}
