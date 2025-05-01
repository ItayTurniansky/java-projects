package Bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Counter extends TextRenderable {

	public Counter(String str) {
		super(str);
	}

	public void update(int i) {
		this.setString(String.valueOf(i));
		if (i >= 3) {
			this.setColor(Color.GREEN);
		} else if (i == 2) {
			this.setColor(Color.YELLOW);
		} else {
			this.setColor(Color.RED);
		}
	}

}
