package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a moving cloud made of blocks, which drops rain-like particles when the avatar jumps.
 *
 * <p>The cloud floats across the screen and reacts to avatar jumps by probabilistically emitting
 * animated raindrop blocks that fade over time.</p>
 *
 * @author itayturni
 */
public class Cloud implements JumpListener {
	/** Base color for the cloud blocks */
	private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);

	/** Defines the pixel-like shape of the cloud using a 2D grid of 1s and 0s */
	private static final List<List<Integer>> CLOUD_SHAPE = List.of(
			List.of(0, 1, 1, 0, 0, 0),
			List.of(1, 1, 1, 0, 1, 0),
			List.of(1, 1, 1, 1, 1, 1),
			List.of(1, 1, 1, 1, 1, 1),
			List.of(0, 1, 1, 1, 0, 0),
			List.of(0, 0, 0, 0, 0, 0)
	);

	private static final int CLOUD_BLOCK_SIZE = Block.SIZE;
	private static final int CLOUD_Y_BUFFER = 100;
	private static final int CLOUD_X_BUFFER = 300;
	private static final float CLOUD_SPEED = 60f;
	private static final double DROP_PROBABILITY = 0.4;
	private static final float DROP_SPEED = 100f;
	private static final float DROP_TIME = 4.5f;

	private List<Block> blocks;
	private List<Block> drops;
	private PepseGameManager gameManager;
	private Avatar avatar;
	private Vector2 windowDimensions;

	/**
	 * Creates and initializes the cloud blocks and attaches to avatar jump events.
	 *
	 * @param windowDimensions The dimensions of the game window
	 * @param avatar           The avatar whose jumps trigger rain
	 * @param gameManager      The game manager for drop updates
	 */
	public void create(Vector2 windowDimensions, Avatar avatar, PepseGameManager gameManager) {
		this.windowDimensions = windowDimensions;
		this.avatar = avatar;
		this.gameManager = gameManager;

		avatar.addJumpListener(this);
		this.blocks = new ArrayList<>();
		this.drops = new ArrayList<>();

		for (int row = 0; row < CLOUD_SHAPE.size(); row++) {
			for (int col = 0; col < CLOUD_SHAPE.get(row).size(); col++) {
				if (CLOUD_SHAPE.get(row).get(col) == 1) {
					Vector2 position = new Vector2(
							col * CLOUD_BLOCK_SIZE - CLOUD_X_BUFFER,
							row * CLOUD_BLOCK_SIZE + CLOUD_Y_BUFFER
					);
					Block block = new Block(
							position,
							new RectangleRenderable(ColorSupplier.approximateColor(BASE_CLOUD_COLOR))
					);
					block.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
					block.setTag("cloudBlock");
					block.transform().setVelocityX(CLOUD_SPEED);
					blocks.add(block);
				}
			}
		}
	}

	/**
	 * Returns the current cloud blocks.
	 *
	 * @return A list of cloud blocks
	 */
	public List<Block> getBlocks() {
		return blocks;
	}

	/**
	 * Returns the currently active rain drops.
	 *
	 * @return A list of active drop blocks
	 */
	public List<Block> getDrops() {
		return drops;
	}

	/**
	 * Responds to a jump by generating a new set of rain drops based on a probability.
	 * Drops fade out over time and trigger an update in the game manager.
	 */
	@Override
	public void onJump() {
		drops = new ArrayList<>();

		for (Block cloudBlock : blocks) {
			if (Math.random() < DROP_PROBABILITY) {
				Vector2 dropPos = new Vector2(
						cloudBlock.getTopLeftCorner().x() + CLOUD_BLOCK_SIZE / 2f - 2,
						cloudBlock.getTopLeftCorner().y() + CLOUD_BLOCK_SIZE
				);

				Block drop = new Block(dropPos, new OvalRenderable(new Color(100, 100, 255)));
				drop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
				drop.setTag("raindrop");
				drop.transform().setVelocityY(DROP_SPEED);

				new Transition<>(
						drop,
						drop.renderer()::setOpaqueness,
						1f,
						0f,
						Transition.LINEAR_INTERPOLATOR_FLOAT,
						DROP_TIME,
						Transition.TransitionType.TRANSITION_ONCE,
						null
				);

				drops.add(drop);
			}
		}

		gameManager.updateDrops();
	}
}
