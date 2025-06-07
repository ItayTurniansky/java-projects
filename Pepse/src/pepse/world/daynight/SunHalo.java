package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {
	private static final float HALO_SIZE = 130f;
	private final static Color HALO_COLOR = new Color (255,255,0,20);

	public static GameObject create(GameObject sun){
		GameObject sunHalo = new GameObject(sun.getCenter(),new Vector2(HALO_SIZE,HALO_SIZE)
		,new OvalRenderable(HALO_COLOR));
		sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		return sunHalo;
	}
}
