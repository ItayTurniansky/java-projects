package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the player's avatar in the game world.
 *
 * <p>The avatar can move left/right, jump, gain and lose energy, and regenerates energy when idle.
 * It also supports external listeners for jump events, enabling features like leaf drops or sound.</p>
 *
 * @author itayturni
 */
public class Avatar extends GameObject {
	/** Size of the avatar in pixels */
	public static final Vector2 AVATAR_SIZE = new Vector2(64, 64);

	private static final float VELOCITY_X = 400;
	private static final float VELOCITY_Y = -650;
	private static final float GRAVITY = 600;
	private static final float RUN_ENERGY = 0.5f;
	private static final float JUMP_ENERGY = 10;
	private static final double RENDER_TIME = 0.1;
	private static final float MAX_ENERGY = 100;
	private static final float LIFE_REGEN_DELAY = 0.2f;

	private final UserInputListener inputListener;
	private final List<JumpListener> jumpListeners = new ArrayList<>();
	private final Random rand = new Random();

	private float energy;
	private final AnimationRenderable sidewaysRenderable;
	private final AnimationRenderable upDownRenderable;
	private final AnimationRenderable idleRenderable;

	/**
	 * Constructs an avatar GameObject with movement and animation logic.
	 *
	 * @param topLeftCorner Initial position of the avatar
	 * @param inputListener Listener for keyboard input
	 * @param imageReader   Used to load the animation images
	 */
	public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
		super(topLeftCorner, AVATAR_SIZE, imageReader.readImage("assets/idle_0.png", false));

		this.inputListener = inputListener;
		this.energy = MAX_ENERGY;

		physics().preventIntersectionsFromDirection(Vector2.ZERO);
		transform().setAccelerationY(GRAVITY);
		setTag("avatar");

		this.idleRenderable = new AnimationRenderable(new Renderable[]{
				imageReader.readImage("assets/idle_0.png", false),
				imageReader.readImage("assets/idle_1.png", false),
				imageReader.readImage("assets/idle_2.png", false),
				imageReader.readImage("assets/idle_3.png", false)
		}, RENDER_TIME);

		this.sidewaysRenderable = new AnimationRenderable(new Renderable[]{
				imageReader.readImage("assets/run_0.png", false),
				imageReader.readImage("assets/run_1.png", false),
				imageReader.readImage("assets/run_2.png", false),
				imageReader.readImage("assets/run_3.png", false),
				imageReader.readImage("assets/run_4.png", false),
				imageReader.readImage("assets/run_5.png", false)
		}, RENDER_TIME);

		this.upDownRenderable = new AnimationRenderable(new Renderable[]{
				imageReader.readImage("assets/jump_0.png", false),
				imageReader.readImage("assets/jump_1.png", false),
				imageReader.readImage("assets/jump_2.png", false),
				imageReader.readImage("assets/jump_3.png", false)
		}, RENDER_TIME);
	}

	/**
	 * Updates avatar's movement, animation, and energy regeneration every frame.
	 *
	 * @param deltaTime Time passed since the last frame
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		float xVel = 0;

		if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy > RUN_ENERGY) {
			energy -= RUN_ENERGY;
			xVel -= VELOCITY_X;
			renderer().setRenderable(sidewaysRenderable);
			renderer().setIsFlippedHorizontally(true);
		}

		if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energy > RUN_ENERGY) {
			xVel += VELOCITY_X;
			energy -= RUN_ENERGY;
			renderer().setRenderable(sidewaysRenderable);
			renderer().setIsFlippedHorizontally(false);
		}

		transform().setVelocityX(xVel);

		if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
				getVelocity().y() == 0 && energy > JUMP_ENERGY) {
			energy -= JUMP_ENERGY;
			transform().setVelocityY(VELOCITY_Y);
			renderer().setRenderable(upDownRenderable);
			notifyJumpListeners();
		}

		boolean isIdle = !inputListener.isKeyPressed(KeyEvent.VK_LEFT)
				&& !inputListener.isKeyPressed(KeyEvent.VK_RIGHT)
				&& !inputListener.isKeyPressed(KeyEvent.VK_SPACE);

		if (energy < MAX_ENERGY && isIdle) {
			if (rand.nextBoolean()) {
				new ScheduledTask(this, LIFE_REGEN_DELAY, false, () -> energy += 1);
			}
			renderer().setRenderable(idleRenderable);
		}

		if (energy > MAX_ENERGY) {
			energy = MAX_ENERGY;
		}
	}

	/**
	 * @return The current energy level of the avatar
	 */
	public float getEnergy() {
		return energy;
	}

	/**
	 * Adds energy to the avatar (e.g., from eating a fruit).
	 *
	 * @param energy The amount of energy to add
	 */
	public void addEnergy(float energy) {
		this.energy += energy;
	}

	/**
	 * Handles collisions. Resets vertical velocity on ground collision.
	 *
	 * @param other     The GameObject with which a collision occurred
	 * @param collision Details of the collision
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if ("ground".equals(other.getTag())) {
			transform().setVelocityY(0);
		}
	}

	/**
	 * Registers a new listener to be notified when the avatar jumps.
	 *
	 * @param listener The listener to add
	 */
	public void addJumpListener(JumpListener listener) {
		jumpListeners.add(listener);
	}

	/**
	 * Removes a previously registered jump listener.
	 *
	 * @param listener The listener to remove
	 */
	public void removeJumpListener(JumpListener listener) {
		jumpListeners.remove(listener);
	}

	/** Notifies all registered listeners of a jump event. */
	private void notifyJumpListeners() {
		for (JumpListener listener : jumpListeners) {
			listener.onJump();
		}
	}
}
