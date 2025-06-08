package pepse.world;

import danogl.gui.rendering.TextRenderable;

import java.awt.*;

/**
 * A simple energy display component that renders the avatar's current energy as text.
 *
 * <p>Used to visually indicate the player's energy level as an integer string.</p>
 *
 * @author itayturni
 */
public class EnergyCounter extends TextRenderable {

	/**
	 * Constructs a new EnergyCounter with an initial display value.
	 *
	 * @param str Initial string to display (e.g., energy value)
	 */
	public EnergyCounter(String str) {
		super(str);
	}

	/**
	 * Updates the text to reflect the current energy level.
	 *
	 * @param i The current energy amount to display
	 */
	public void update(int i) {
		setString(String.valueOf(i));
	}
}
