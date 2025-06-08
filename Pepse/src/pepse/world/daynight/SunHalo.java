package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A utility class for creating a sun halo GameObject that visually follows the sun
 * to enhance the sunlight effect in the game world.
 *
 * <p>The halo is rendered in a translucent yellow color, slightly larger than the sun.</p>
 *
 * @author itayturni
 */
public class SunHalo {
	/** Diameter of the halo, slightly larger than the sun */
	private static final float HALO_SIZE = 130f;

	/** Color of the sun halo with transparency */
	private static final Color HALO_COLOR = new Color(255, 255, 0, 20);

	/**
	 * Creates a sun halo GameObject positioned at the sun's current center.
	 *
	 * @param sun The sun GameObject to follow
	 * @return A new GameObject representing the sun halo
	 */
	public static GameObject create(GameObject sun) {
		GameObject sunHalo = new GameObject(
				sun.getCenter(),
				new Vector2(HALO_SIZE, HALO_SIZE),
				new OvalRenderable(HALO_COLOR)
		);

		sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		return sunHalo;
	}
}
