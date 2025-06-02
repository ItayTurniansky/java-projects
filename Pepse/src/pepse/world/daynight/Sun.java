package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;


public class Sun {
	private static final float SUN_SIZE = 100f;
	private static final Float MAX_DEGREES = 360f;

	public static GameObject create(Vector2 windowDimension, float cycleLength) {
		Vector2 initialSunCenter = new Vector2(windowDimension.x()/2, windowDimension.y()/2);
		GameObject sun = new GameObject(initialSunCenter,
				new Vector2(SUN_SIZE, SUN_SIZE),new OvalRenderable(Color.YELLOW));
		sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sun.setTag("sun");
		Vector2 cycleCenter = new Vector2(windowDimension.x()/2, windowDimension.y()*Terrain.TERRAIN_MIN_HEIGHT);
		new Transition<Float>(
				sun,
				(Float angle) -> sun.setCenter
						(initialSunCenter.subtract(cycleCenter)
								.rotated(angle)
								.add(cycleCenter)),

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
