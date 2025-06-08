package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents a single leaf GameObject in the game world.
 * A leaf is a green square of fixed size, used as part of a tree canopy.
 *
 * <p>Leaves are static visual elements and do not interact with physics by default.</p>
 *
 * @author itayturni
 */
public class Leaf extends GameObject {
	/** Length of each side of the square leaf in pixels */
	public static final int SIZE = 30;

	/** Color used to render the leaf */
	private static final Color LEAF_COLOR = new Color(50, 200, 30);

	/**
	 * Constructs a new Leaf object at the specified top-left corner.
	 *
	 * @param topLeftCorner The top-left corner where the leaf is placed
	 */
	public Leaf(Vector2 topLeftCorner) {
		super(
				topLeftCorner,
				Vector2.ONES.mult(SIZE),
				new RectangleRenderable(LEAF_COLOR)
		);
		setTag("leaf");
	}
}
