package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

public class AsciiArtAlgorithm {
	private int resolution;
	private Image image;
	private char[] charset;
	public AsciiArtAlgorithm(int resolution, Image image, char[] charset) {
		this.resolution = resolution;
		this.image = image;
		this.charset = charset.clone();
	}

	public char[][] run(){
		AsciiArtCache cache = AsciiArtCache.getInstance();
		if (cache.matches(image, resolution, charset)) {
			return cache.getResult();
		}
		Image paddedImage= ImageProcessor.padImage(image);
		if (this.resolution > paddedImage.getWidth()) {
			throw new IllegalArgumentException("Resolution too high for image width.");
		}

		int tileSize = paddedImage.getWidth() / this.resolution;
		Image[][] tiles = ImageProcessor.splitImage(paddedImage, tileSize);
		char[][] result = new char[tiles.length][tiles[0].length];
		SubImgCharMatcher subImageCharMatcher = new SubImgCharMatcher(this.charset);
		for (int y = 0; y < tiles.length; y++) {
			for (int x = 0; x < tiles[y].length; x++) {
				result[y][x] = subImageCharMatcher.getCharByImageBrightness(ImageProcessor.computeBrightness(tiles[y][x]));
			}
		}
		cache.update(image, resolution, charset, result);
		return result;
	}

}
