package ascii_art;
import image.Image;
import java.util.Arrays;

public class AsciiArtCache {
	private static AsciiArtCache instance;

	private Image lastImage;
	private int lastResolution;
	private char[] lastCharset;
	private char[][] lastResult;

	private AsciiArtCache() {
	}

	public static AsciiArtCache getInstance() {
		if (instance == null) {
			instance = new AsciiArtCache();
		}
		return instance;
	}

	public boolean matches(Image image, int resolution, char[] charset) {
		if (lastImage == null || lastCharset == null) return false;
		return this.lastImage.equals(image) &&
				this.lastResolution == resolution &&
				Arrays.equals(this.lastCharset, charset);
	}

	public char[][] getResult() {
		return this.lastResult;
	}

	public void update(Image image, int resolution, char[] charset, char[][] result) {
		this.lastImage = image;
		this.lastResolution = resolution;
		this.lastCharset = charset.clone(); // avoid reference issues
		this.lastResult = result;
	}

}
