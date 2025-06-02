package pepse.world;

import danogl.gui.rendering.TextRenderable;

import java.awt.*;

public class EnergyCounter extends TextRenderable {


	/**
	 * @param str first string to display in EnergyCounter
	 */
	public EnergyCounter(String str) {
		super(str);
	}

	/**
	 * @param i the current energy amount to be displayed by EnergyCounter
	 */
	public void update(int i) {
		this.setString(String.valueOf(i));
	}

}
