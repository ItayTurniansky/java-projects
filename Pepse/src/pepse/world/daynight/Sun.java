package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;


public class Sun {
	private static final float SUN_SIZE = 70f;

	public static GameObject create(Vector2 windowDimension, float cycleLength) {
		GameObject sun = new GameObject(new Vector2(windowDimension.x()/2, windowDimension.y()/2),
				new Vector2(SUN_SIZE, SUN_SIZE),new OvalRenderable(Color.YELLOW));
		sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sun.setTag("sun");
		return sun;
	}
}
