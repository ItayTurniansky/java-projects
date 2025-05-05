package Bricker.gameobjects;

import danogl.gui.rendering.TextRenderable;

import java.awt.*;

/**
 * Counter class represents the life counter extends TextRenderable
 *
 * @author itayt
 */
public class Counter extends TextRenderable {
	private static final int GREEN_MIN = 3;
	private static final int YELLOW_NUM = 2;

	/**
	 * @param str first string to display in counter
	 */
	public Counter(String str) {
		super(str);
	}

	/**
	 * @param i the current life amount to be displayed by counter
	 */
	public void update(int i) {
		this.setString(String.valueOf(i));
		if (i >= GREEN_MIN) {
			this.setColor(Color.GREEN);
		} else if (i == YELLOW_NUM) {
			this.setColor(Color.YELLOW);
		} else {
			this.setColor(Color.RED);
		}
	}

}
