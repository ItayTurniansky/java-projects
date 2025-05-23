package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

/**
 * The AsciiArtAlgorithm class is responsible for converting an image into
 * a 2D array of ASCII characters based on brightness levels.
 * It uses caching to avoid unnecessary recomputation.
 * @author itayt
 */
public class AsciiArtAlgorithm {
	/** Error message for invalid resolution input. */
	private static final String INVALID_RESOLUTION_MSG =
			"Resolution too high for image width.";

	private final int resolution;
	private final Image image;
	private final char[] charset;

	/**
	 * Constructs a new AsciiArtAlgorithm instance.
	 *
	 * @param resolution Number of characters per row in the output
	 * @param image      The input image
	 * @param charset    The set of characters used to represent brightness
	 */
	public AsciiArtAlgorithm(int resolution, Image image, char[] charset) {
		this.resolution = resolution;
		this.image = image;
		this.charset = charset.clone(); // Defensive copy
	}

	/**
	 * Runs the ASCII art generation algorithm.
	 * If inputs have not changed, returns the cached result.
	 *
	 * @return A 2D character array representing the image in ASCII
	 */
	public char[][] run() {
		AsciiArtCache cache = AsciiArtCache.getInstance();

		if (cache.matches(image, resolution, charset)) {
			return cache.getResult();
		}

		Image paddedImage = ImageProcessor.padImage(image);

		if (this.resolution > paddedImage.getWidth()) {
			throw new IllegalArgumentException(INVALID_RESOLUTION_MSG);
		}

		int tileSize = paddedImage.getWidth() / this.resolution;
		Image[][] tiles = ImageProcessor.splitImage(paddedImage, tileSize);
		char[][] result = new char[tiles.length][tiles[0].length];
		SubImgCharMatcher matcher = new SubImgCharMatcher(this.charset);

		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[y].length; x++) {
				double brightness = ImageProcessor.computeBrightness(tiles[y][x]);
				result[y][x] = matcher.getCharByImageBrightness(brightness);
			}
		}

		cache.update(image, resolution, charset, result);
		return result;
	}
}
