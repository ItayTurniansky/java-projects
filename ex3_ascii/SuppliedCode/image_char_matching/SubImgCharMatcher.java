package image_char_matching;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class SubImgCharMatcher {
	private static final int RESOLUTION = 16;
	private static final double STANDARD_BRIGHTNESS = 0.5;
	private HashMap<Character, Double> charMap;

	public SubImgCharMatcher(char[] charset) {
		this.charMap = new HashMap<>();
		for (int i = 0; i < charset.length; i++) {
			charMap.put(charset[i], 0.0);
		}
		calculateBrightnessMap();
	}

	public char getCharByImageBrightness(double brightness) {
		double minDiff = 1.0;
		char bestChar = 0;
		for (Map.Entry<Character, Double> entry : charMap.entrySet()) {
			double diff = Math.abs(entry.getValue() - brightness);
			if (diff < minDiff) {
				minDiff = diff;
				bestChar = entry.getKey();
			}
		}
		return bestChar;
	}

	public void addChar(char c) {
		if (!charMap.containsKey(c)) {
			charMap.put(c, 0.0);
			calculateBrightnessMap();
		}

	}

	public void removeChar(char c) {
		this.charMap.remove(c);
		calculateBrightnessMap();
	}

	private void calculateBrightnessMap() {
		double maxBrightness = 0.0;
		double minBrightness = 1.0;
		for (Map.Entry<Character, Double> entry : this.charMap.entrySet()) {
			double trueCounter =0;
			boolean[][] tmpBoolArray = CharConverter.convertToBoolArray(entry.getKey());
			for (int i = 0; i < tmpBoolArray.length; i++) {
				for (int j = 0; j < tmpBoolArray[i].length; j++) {
					if (tmpBoolArray[i][j]) {
						trueCounter++;
					}
				}
			}
			double brightness = trueCounter / (RESOLUTION * RESOLUTION);
			if (brightness >= maxBrightness) {
				maxBrightness = brightness;
			}
			if (brightness <= minBrightness) {
				minBrightness = brightness;
			}
			entry.setValue(brightness);

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
			entry.setValue(normalized);		}
	}

}

