package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;

public class Fruit extends GameObject {
	public static final int SIZE = 30;
	private static final Color FRUIT_COLOR_1 = new Color(250, 150, 30);
	private static final Color FRUIT_COLOR_2 = new Color(250, 10, 10);
	private Random rand = new Random();

	public Fruit(Vector2 topLeftCorner) {
		super(topLeftCorner, Vector2.ONES.mult(SIZE), new OvalRenderable(FRUIT_COLOR_1));
		if (rand.nextBoolean()) {
			this.renderer().setRenderable(new OvalRenderable(FRUIT_COLOR_2));
		}
		this.setTag("fruit");
	}
	public void  dissapearAndComeBack()

}
