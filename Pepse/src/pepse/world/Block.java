package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a basic terrain block in the game world.
 *
 * <p>Blocks are square, immovable, and used to construct the terrain surface.</p>
 *
 * @author itayturni
 */
public class Block extends GameObject {
	/** Length of each side of the square block in pixels */
	public static final int SIZE = 30;

	/**
	 * Constructs a new terrain block at the specified location with the given appearance.
	 *
	 * @param topLeftCorner The top-left corner of the block
	 * @param renderable    The visual representation of the block
	 */
	public Block(Vector2 topLeftCorner, Renderable renderable) {
		super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
		physics().preventIntersectionsFromDirection(Vector2.ZERO);
		physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
	}
}
