package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sky{
	private static final Color BASIC_SKY_COLOR = Color.decode("#80c6E5");

	public static GameObject create(Vector2 windowDimensions){
		GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
				new RectangleRenderable(BASIC_SKY_COLOR));
		sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sky.setTag("sky");
		return sky;
	}
}
