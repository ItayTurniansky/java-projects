package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

/**
 * A utility class for creating a Sun GameObject that moves in a circular arc to simulate
 * a sun cycle throughout the day.
 *
 * <p>The sun rotates continuously around a central point based on the given cycle length.</p>
 *
 * @author itayturni
 */
public class Sun {
	/** Diameter of the sun object */
	private static final float SUN_SIZE = 100f;

	/** Maximum angle in degrees for a full circular rotation */
	private static final float MAX_DEGREES = 360f;

	/**
	 * Creates a sun GameObject that moves in a circular path over time.
	 *
	 * @param windowDimension The dimensions of the game window
	 * @param cycleLength     Length of the full sun cycle in seconds
	 * @return A GameObject representing the sun
	 */
	public static GameObject create(Vector2 windowDimension, float cycleLength) {
		Vector2 initialSunCenter = new Vector2(
				windowDimension.x() / 2,
				windowDimension.y() / 2
		);

		GameObject sun = new GameObject(
				initialSunCenter,
				new Vector2(SUN_SIZE, SUN_SIZE),
				new OvalRenderable(Color.YELLOW)
		);

		sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sun.setTag("sun");

		Vector2 cycleCenter = new Vector2(
				windowDimension.x() / 2,
				windowDimension.y() * Terrain.TERRAIN_MIN_HEIGHT
		);

		new Transition<>(
				sun,
				angle -> sun.setCenter(
						initialSunCenter
								.subtract(cycleCenter)
								.rotated(angle)
								.add(cycleCenter)
				),
				0f,
				MAX_DEGREES,
				Transition.LINEAR_INTERPOLATOR_FLOAT,
				cycleLength,
				Transition.TransitionType.TRANSITION_LOOP,
				null
		);

		return sun;
	}
}
