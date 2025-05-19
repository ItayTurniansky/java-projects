package image;
import java.awt.*;

public class ImageProcessor {
	private static final int WHITE = 255;
	private static final double RED_TO_GREY = 0.2126;
	private static final double GREEN_TO_GREY = 0.7152;
	private static final double BLUE_TO_GREY = 0.0722;
	private static final double MAX_RGB = 255;

	public static Image padImage(Image original) {
		int newHeight = nextPowerOfTwo(original.getHeight());
		int newWidth = nextPowerOfTwo(original.getWidth());
		Color[][] newColors = new Color[newHeight][newWidth];
		for (int i = 0; i < newHeight; i++) {
			for (int j = 0; j < newWidth; j++) {
				newColors[i][j] = new Color(WHITE, WHITE, WHITE); // white
			}
		}
		int yOffset = (newHeight - original.getHeight()) / 2;
		int xOffset = (newWidth - original.getWidth()) / 2;
		for (int y = 0; y < original.getHeight(); y++) {
			for (int x = 0; x < original.getWidth(); x++) {
				newColors[yOffset + y][xOffset + x] = original.getPixel(y, x);
			}
		}
		return new Image(newColors, newWidth, newHeight);
	}


	public static Image[][] splitImage(Image padded, int resolution) {
		int numRows = padded.getHeight() / resolution;
		int numCols = padded.getWidth() / resolution;
		Image[][] tiles = new Image[numRows][numCols];
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				Color[][] tilePixels = new Color[resolution][resolution];
				for (int y = 0; y < resolution; y++) {
					for (int x = 0; x < resolution; x++) {
						int srcX = col * resolution + x;
						int srcY = row * resolution + y;
						tilePixels[y][x] = padded.getPixel(srcY, srcX);
					}
				}

				tiles[row][col] = new Image(tilePixels, resolution, resolution);
			}
		}
		return tiles;
	}

	public static double computeBrightness(Image tile) {
		double totalGrey =0.0;
		for (int y = 0; y < tile.getHeight(); y++) {
			for (int x = 0; x < tile.getWidth(); x++) {
				Color c = tile.getPixel(y, x);
				double greyPixel = c.getRed()* RED_TO_GREY +
						c.getGreen() * GREEN_TO_GREY +
						c.getBlue() * BLUE_TO_GREY;
				totalGrey += greyPixel;
			}
		}
		double avgGrey = totalGrey / (tile.getHeight() * tile.getWidth());
		return avgGrey / MAX_RGB;
	}

	private static int nextPowerOfTwo(int x) {
		int power = 1;
		while (power < x) {
			power *= 2;
		}
		return power;
	}

}
