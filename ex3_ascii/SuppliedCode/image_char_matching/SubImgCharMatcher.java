package image_char_matching;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;
import java.util.TreeMap;

public class SubImgCharMatcher {
	private static final int RESOLUTION = 16;
	private static final double STANDARD_BRIGHTNESS = 0.5;
	private TreeMap<Character, Double> charMap;
	private String round;

	public SubImgCharMatcher(char[] charset) {
		this.charMap = new TreeMap<>();
		for (char c : charset) {
			if (!charMap.containsKey(c)) {
				charMap.put(c, null);
			}
		}
		round ="abs";
		calculateBrightnessMap();
	}

	public char getCharByImageBrightness(double brightness) {
		double minDiff = 1.0;
		char bestChar = 0;
		double diff = 0;
		for (Map.Entry<Character, Double> entry : charMap.entrySet()) {
			if (this.round.equals("abs")) {
				diff = Math.abs(entry.getValue() - brightness);
			}
			if (this.round.equals("up")) {
				diff = Math.nextUp(entry.getValue() - brightness);
			}
			if (this.round.equals("down")) {
				diff = Math.nextDown(entry.getValue() - brightness);
			}
			if (diff < minDiff) {
				minDiff = diff;
				bestChar = entry.getKey();
			}
		}
		return bestChar;
	}
	public void setRound(String round) {
		this.round = round;
	}

	public void addChar(char c) {
		if (!charMap.containsKey(c)) {
			charMap.put(c, null);
			calculateBrightnessMap();
		}

	}

	public void removeChar(char c) {
		this.charMap.remove(c);
		calculateBrightnessMap();
	}

	public TreeMap<Character, Double> getCharMap() {
		return charMap;
	}

	private void calculateBrightnessMap() {
		double maxBrightness = 0.0;
		double minBrightness = 1.0;
		for (Map.Entry<Character, Double> entry : this.charMap.entrySet()) {
			if(entry.getValue() == null) {
				double trueCounter = 0;
				boolean[][] tmpBoolArray = CharConverter.convertToBoolArray(entry.getKey());
				for (int i = 0; i < tmpBoolArray.length; i++) {
					for (int j = 0; j < tmpBoolArray[i].length; j++) {
						if (tmpBoolArray[i][j]) {
							trueCounter++;
						}
					}
				}
				double brightness = trueCounter / (RESOLUTION * RESOLUTION);
				entry.setValue(brightness);
			}
			if (entry.getValue() >= maxBrightness) {
				maxBrightness = entry.getValue();
			}
			if (entry.getValue() <= minBrightness) {
				minBrightness = entry.getValue();
			}

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

