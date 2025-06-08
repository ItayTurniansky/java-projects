package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates terrain blocks in the game world based on Perlin-style noise.
 *
 * <p>The terrain includes a visible top layer and several underground layers to give the world depth.
 * It also provides a method to compute terrain height at any X coordinate.</p>
 *
 * @author itayturni
 */
public class Terrain {
	/** Minimum fraction (of window height) where terrain starts */
	public static final float TERRAIN_MIN_HEIGHT = 2f / 3;

	private static final int TERRAIN_DEPTH = 20;
	private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
	private static final int NOISE_FACTOR = 7;

	private final int groundHeightAtX0;
	private final int seed;

	/**
	 * Constructs a terrain object to generate blocks using the given seed and window height.
	 *
	 * @param windowDimensions The dimensions of the game window
	 * @param seed             Seed for noise-based height generation
	 */
	public Terrain(Vector2 windowDimensions, int seed) {
		this.groundHeightAtX0 = (int) (windowDimensions.y() * TERRAIN_MIN_HEIGHT);
		this.seed = seed;
	}

	/**
	 * Calculates the terrain height at a given X coordinate using a noise function.
	 *
	 * @param x The X coordinate for which to determine height
	 * @return The Y coordinate of the terrain's surface at that X
	 */
	public float groundHeightAt(float x) {
		NoiseGenerator noise = new NoiseGenerator(this.seed, groundHeightAtX0);
		return (float) (groundHeightAtX0 + noise.noise(x, Block.SIZE * NOISE_FACTOR));
	}

	/**
	 * Generates terrain blocks between minX and maxX, including a top layer and underground depth.
	 *
	 * @param minX The minimum X coordinate of the terrain segment
	 * @param maxX The maximum X coordinate of the terrain segment
	 * @return A list of blocks representing the terrain
	 */
	public List<Block> createInRange(int minX, int maxX) {
		List<Block> blocks = new ArrayList<>();

		for (int x = minX; x <= maxX; x += Block.SIZE) {
			float topBlockHeight = groundHeightAt(x);

			// Create top ground block
			RectangleRenderable topRenderable =
					new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
			Block topBlock = new Block(new Vector2(x, topBlockHeight), topRenderable);
			topBlock.setTag("topGround");
			blocks.add(topBlock);

			// Create underground blocks
			for (int i = 0; i < TERRAIN_DEPTH; i++) {
				RectangleRenderable groundRenderable =
						new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
				Block groundBlock = new Block(
						new Vector2(x, topBlockHeight + i * Block.SIZE),
						groundRenderable
				);
				groundBlock.setTag("ground");
				blocks.add(groundBlock);
			}
		}

		return blocks;
	}
}
