package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {
	private static final Color NIGHT_COLOR = Color.black;
	private static final float MIDNIGHT_OPACITY = 0.5f;

	public static GameObject create(Vector2 windowDimensions, float cycleLength){
		GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
				new RectangleRenderable(NIGHT_COLOR));
		night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		night.setTag("night");
		night.renderer().setOpaqueness(1);
		new Transition<Float>(
				night,
				night.renderer()::setOpaqueness,
				0f,
				MIDNIGHT_OPACITY,
				Transition.CUBIC_INTERPOLATOR_FLOAT,
				cycleLength/2,
				Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
				null
		);
		return night;
	}
}
