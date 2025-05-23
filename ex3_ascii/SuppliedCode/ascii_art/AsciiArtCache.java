package ascii_art;

import image.Image;
import java.util.Arrays;

/**
 * A Singleton class that caches the result of the most recent ASCII art
 * generation. It avoids recomputation when the input image, resolution,
 * and charset are identical to the last run.
 * @author itayt
 */
public class AsciiArtCache {
	/** The single instance of this class (Singleton pattern). */
	private static AsciiArtCache instance;

	private Image lastImage;
	private int lastResolution;
	private char[] lastCharset;
	private char[][] lastResult;

	/**
	 * Private constructor to prevent external instantiation.
	 */
	private AsciiArtCache() {}

	/**
	 * Returns the singleton instance of AsciiArtCache.
	 *
	 * @return The singleton instance
	 */
	public static AsciiArtCache getInstance() {
		if (instance == null) {
			instance = new AsciiArtCache();
		}
		return instance;
	}

	/**
	 * Checks whether the provided input parameters match the cached state.
	 *
	 * @param image      The input image
	 * @param resolution The resolution used for the ASCII art
	 * @param charset    The character set used
	 * @return True if the inputs match the last run; false otherwise
	 */
	public boolean matches(Image image, int resolution, char[] charset) {
		if (lastImage == null || lastCharset == null) {
			return false;
		}
		return this.lastImage.equals(image) &&
				this.lastResolution == resolution &&
				Arrays.equals(this.lastCharset, charset);
	}

	/**
	 * Returns the cached result of the last ASCII art run.
	 *
	 * @return A 2D character array representing the ASCII output
	 */
	public char[][] getResult() {
		return this.lastResult;
	}

	/**
	 * Updates the cache with new input parameters and the resulting ASCII output.
	 *
	 * @param image      The input image
	 * @param resolution The resolution used for rendering
	 * @param charset    The character set used
	 * @param result     The generated ASCII character grid
	 */
	public void update(Image image, int resolution, char[] charset, char[][] result) {
		this.lastImage = image;
		this.lastResolution = resolution;
		this.lastCharset = charset.clone();
		this.lastResult = result;
	}
}
