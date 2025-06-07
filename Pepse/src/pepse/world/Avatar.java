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
import pepse.world.trees.Fruit;

import java.awt.event.KeyEvent;
import java.awt.image.renderable.RenderableImage;
import java.util.ArrayList;
import java.util.List;

public class Avatar extends GameObject {
	public static final Vector2 AVATAR_SIZE = new Vector2(64, 64);
	private static final float VELOCITY_X = 400;
	private static final float VELOCITY_Y = -650;
	private static final float GRAVITY = 600;
	private static final float RUN_ENERGY = 0.5f;
	private static final float JUMP_ENERGY = 10;
	private static final double RENDER_TIME = 0.1;
	private static final float MAX_ENERGY = 100;
	private static final float LIFE_REGEN_DELAY = 1;

	private UserInputListener inputListener;
	private float energy;
	private AnimationRenderable sidewaysRenderable;
	private AnimationRenderable upDownRenderable;
	private AnimationRenderable idleRenderable;
	private final List<JumpListener> jumpListeners = new ArrayList<>();


	public void addJumpListener(JumpListener listener) {
		jumpListeners.add(listener);
	}

	public void removeJumpListener(JumpListener listener) {
		jumpListeners.remove(listener);
	}

	private void notifyJumpListeners() {
		for (JumpListener listener : jumpListeners) {
			listener.onJump();
		}
	}



	public Avatar(Vector2 topLeftCorner,
				  UserInputListener inputListener,
				  ImageReader imageReader){
		super(topLeftCorner, AVATAR_SIZE, imageReader.readImage("assets/idle_0.png", false));
		physics().preventIntersectionsFromDirection(Vector2.ZERO);
		transform().setAccelerationY(GRAVITY);
		this.setTag("avatar");
		this.inputListener = inputListener;
		this.energy = MAX_ENERGY;
		this.idleRenderable = new AnimationRenderable(new Renderable[] {
				imageReader.readImage("assets/idle_0.png", false),
				imageReader.readImage("assets/idle_1.png", false),
				imageReader.readImage("assets/idle_2.png", false),
				imageReader.readImage("assets/idle_3.png", false)
		}, RENDER_TIME);

		this.sidewaysRenderable = new AnimationRenderable(new Renderable[] {
				imageReader.readImage("assets/run_0.png", false),
				imageReader.readImage("assets/run_1.png", false),
				imageReader.readImage("assets/run_2.png", false),
				imageReader.readImage("assets/run_3.png", false),
				imageReader.readImage("assets/run_4.png", false),
				imageReader.readImage("assets/run_5.png", false)

		}, RENDER_TIME);

		this.upDownRenderable = new AnimationRenderable(new Renderable[] {
				imageReader.readImage("assets/jump_0.png", false),
				imageReader.readImage("assets/jump_1.png", false),
				imageReader.readImage("assets/jump_2.png", false),
				imageReader.readImage("assets/jump_3.png", false)
		}, RENDER_TIME);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		float xVel = 0;
		if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)&& energy > RUN_ENERGY) {
			energy -= RUN_ENERGY;
			xVel -= VELOCITY_X;
			this.renderer().setRenderable(sidewaysRenderable);
			this.renderer().setIsFlippedHorizontally(true);
		}
		if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)&&energy> RUN_ENERGY) {
			xVel += VELOCITY_X;
			energy -= RUN_ENERGY;
			this.renderer().setRenderable(sidewaysRenderable);
			this.renderer().setIsFlippedHorizontally(false);

		}
		transform().setVelocityX(xVel);
		if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0
				&& energy > JUMP_ENERGY){
			energy -= JUMP_ENERGY;
			transform().setVelocityY(VELOCITY_Y);
			this.renderer().setRenderable(upDownRenderable);
		}
		if (energy < 100 && !inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
		!inputListener.isKeyPressed(KeyEvent.VK_LEFT) && !inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
			new ScheduledTask(
					this,
					LIFE_REGEN_DELAY,
					false,
					() -> {
						energy += 1;
					}
			);
			this.renderer().setRenderable(idleRenderable);
			notifyJumpListeners();
		}
		if(energy>MAX_ENERGY){
			energy = MAX_ENERGY;
		}
	}

	public float getEnergy() {
		return energy;
	}


	public void addEnergy(float energy) {
		this.energy += energy;
	}

	/**
	 * Called on the first frame of a collision.
	 *
	 * @param other     The GameObject with which a collision occurred.
	 * @param collision Information regarding this collision.
	 *                  A reasonable elastic behavior can be achieved with:
	 *                  setVelocity(getVelocity().flipped(collision.getNormal()));
	 */
	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if(other.getTag().equals("ground")){
			this.transform().setVelocityY(0);
		}


	}
}
