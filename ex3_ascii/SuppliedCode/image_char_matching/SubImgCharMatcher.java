package image_char_matching;

import java.util.Map;
import java.util.TreeMap;

/**
 * The SubImgCharMatcher class maps characters to brightness values based
 * on their rendered 2D binary representation and provides the character
 * closest in brightness to a given tile brightness.
 * @author itayt
 */
public class SubImgCharMatcher {
	private static final int RESOLUTION = 16;
	private static final double STANDARD_BRIGHTNESS = 0.5;

	private final TreeMap<Character, Double> charMap;
	private String round;

	/**
	 * Constructs the matcher and calculates brightness for each character.
	 *
	 * @param charset The set of characters to be used
	 */
	public SubImgCharMatcher(char[] charset) {
		this.charMap = new TreeMap<>();
		for (char c : charset) {
			if (!charMap.containsKey(c)) {
				charMap.put(c, null); // Mark as needing computation
			}
		}
		this.round = "abs";
		calculateBrightnessMap();
	}

	/**
	 * Returns the character whose brightness is closest to the given value.
	 *
	 * @param brightness The target brightness [0.0, 1.0]
	 * @return The character with the closest brightness
	 */
	public char getCharByImageBrightness(double brightness) {
		double minDiff = Double.MAX_VALUE;
		char bestChar = 0;

		for (Map.Entry<Character, Double> entry : charMap.entrySet()) {
			double charBrightness = entry.getValue();
			double diff;

			switch (this.round) {
				case "up":
					diff = Math.nextUp(charBrightness - brightness);
					break;
				case "down":
					diff = Math.nextDown(charBrightness - brightness);
					break;
				case "abs":
				default:
					diff = Math.abs(charBrightness - brightness);
					break;
			}

			if (diff < minDiff) {
				minDiff = diff;
				bestChar = entry.getKey();
			}
		}

		return bestChar;
	}

	/**
	 * Updates the rounding strategy for character matching.
	 *
	 * @param round One of: "abs", "up", "down"
	 */
	public void setRound(String round) {
		this.round = round;
	}

	/**
	 * Adds a character to the charset if not already present.
	 *
	 * @param c The character to add
	 */
	public void addChar(char c) {
		if (!charMap.containsKey(c)) {
			charMap.put(c, null);
			calculateBrightnessMap();
		}
	}

	/**
	 * Removes a character from the charset.
	 *
	 * @param c The character to remove
	 */
	public void removeChar(char c) {
		charMap.remove(c);
		calculateBrightnessMap();
	}

	/**
	 * Returns the current mapping of characters to brightness.
	 *
	 * @return A TreeMap with character-to-brightness mappings
	 */
	public TreeMap<Character, Double> getCharMap() {
		return charMap;
	}

	/* Calculates and normalizes brightness for all characters in the map. */
	private void calculateBrightnessMap() {
		double maxBrightness = 0.0;
		double minBrightness = 1.0;

		for (Map.Entry<Character, Double> entry : charMap.entrySet()) {
			if (entry.getValue() == null) {
				double trueCount = 0;
				boolean[][] binary = CharConverter.convertToBoolArray(entry.getKey());
				for (boolean[] row : binary) {
					for (boolean pixel : row) {
						if (pixel) trueCount++;
					}
				}
				double brightness = trueCount / (RESOLUTION * RESOLUTION);
				entry.setValue(brightness);
			}

			double val = entry.getValue();
			if (val > maxBrightness) maxBrightness = val;
			if (val < minBrightness) minBrightness = val;
		}

		double range = maxBrightness - minBrightness;

		if (range == 0) {
			for (Map.Entry<Character, Double> entry : charMap.entrySet()) {
				entry.setValue(STANDARD_BRIGHTNESS);
			}
			return;
		}

		for (Map.Entry<Character, Double> entry : charMap.entrySet()) {
			double normalized = (entry.getValue() - minBrightness) / range;
			entry.setValue(normalized);
		}
	}
}
