package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A utility class for creating a static sky background in the game world.
 *
 * <p>The sky is rendered as a full-screen blue rectangle that sits in camera space,
 * behind all other game objects.</p>
 *
 * @author itayturni
 */
public class Sky {
	/** The base color used for the sky background */
	private static final Color BASIC_SKY_COLOR = Color.decode("#80c6E5");

	/**
	 * Creates a sky GameObject that spans the entire game window.
	 *
	 * @param windowDimensions The dimensions of the game window
	 * @return A GameObject representing the static sky background
	 */
	public static GameObject create(Vector2 windowDimensions) {
		GameObject sky = new GameObject(
				Vector2.ZERO,
				windowDimensions,
				new RectangleRenderable(BASIC_SKY_COLOR)
		);
		sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sky.setTag("sky");
		return sky;
	}
}
