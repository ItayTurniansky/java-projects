package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A utility class for creating a night overlay GameObject that simulates a day-night cycle.
 * The night effect gradually changes its opacity to represent the passage of time.
 *
 * <p>The created GameObject is tagged "night" and uses camera coordinates.</p>
 *
 * @author itayturni
 */
public class Night {
	/** Color used for the night overlay */
	private static final Color NIGHT_COLOR = Color.BLACK;

	/** Maximum opacity during midnight */
	private static final float MIDNIGHT_OPACITY = 0.5f;

	/**
	 * Creates a GameObject representing the night overlay, which cycles between transparent and
	 * semi-opaque to simulate nightfall and daylight.
	 *
	 * @param windowDimensions The dimensions of the game window
	 * @param cycleLength Length of a full day-night cycle in seconds
	 * @return A GameObject that visually simulates nighttime using opacity transitions
	 */
	public static GameObject create(Vector2 windowDimensions, float cycleLength) {
		GameObject night = new GameObject(
				Vector2.ZERO,
				windowDimensions,
				new RectangleRenderable(NIGHT_COLOR)
		);

		night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		night.setTag("night");
		night.renderer().setOpaqueness(1);

		new Transition<>(
				night,
				night.renderer()::setOpaqueness,
				0f,
				MIDNIGHT_OPACITY,
				Transition.CUBIC_INTERPOLATOR_FLOAT,
				cycleLength / 2,
				Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
				null
		);

		return night;
	}
}
