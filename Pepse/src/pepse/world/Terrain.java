package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
	private static final int TERRAIN_DEPTH = 20;
	private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
	private static final float TERRAIN_MIN_HEIGHT = (float) 2 / 3;
	private static final int NOISE_FACTOR = 7;
	private int groundHeightAtX0;
	private int seed;

	public Terrain (Vector2 windowDimensions, int seed){
		 groundHeightAtX0 = (int) (windowDimensions.y()* TERRAIN_MIN_HEIGHT);
		 this.seed = seed;
	}

	public float groundHeightAt(float x){
	NoiseGenerator noise = new NoiseGenerator(this.seed, groundHeightAtX0);
		return (float) (groundHeightAtX0 + noise.noise(x, Block.SIZE* NOISE_FACTOR));
	}

	public List<Block> createInRange(int minX, int maxX){
		List<Block> blocks = new ArrayList<>();

		for (int x = minX; x <= maxX; x+=Block.SIZE){
			RectangleRenderable blockRenderable = new RectangleRenderable(ColorSupplier.approximateColor(
					BASE_GROUND_COLOR));
			float topBlockHeight = groundHeightAt(x);
			Block block = new Block(new Vector2((float)x,topBlockHeight),blockRenderable);
			block.setTag("topGround");
			blocks.add(block);
			for (int i=0; i<TERRAIN_DEPTH; i++){
				blockRenderable = new RectangleRenderable(ColorSupplier.approximateColor(
						BASE_GROUND_COLOR));
				block = new Block(new Vector2((float)x,topBlockHeight+(i*Block.SIZE)),blockRenderable);
				block.setTag("ground");
				blocks.add(block);
			}
		}
		return blocks;

	}
}
