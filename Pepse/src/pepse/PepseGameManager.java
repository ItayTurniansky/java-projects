package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Fruit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PepseGameManager extends GameManager {
	private static final int FRAME_RATE = 120;
	private static final float NIGHT_CYCLE_LENGTH = 30;
	private static final float ENERGY_COUNTER_SIZE = 50;
	private final Random seed = new Random();
	private WindowController windowController;
	private Vector2 windowDimensions;
	private Avatar avatar;
	private Cloud currentCloud;
	private List<GameObject> drops;

	/**
	 * The method will be called once when a GameGUIComponent is created,
	 * and again after every invocation of windowController.resetGame().
	 *
	 * @param imageReader      Contains a single method: readImage, which reads an image from disk.
	 *                         See its documentation for help.
	 * @param soundReader      Contains a single method: readSound, which reads a wav file from
	 *                         disk. See its documentation for help.
	 * @param inputListener    Contains a single method: isKeyPressed, which returns whether
	 *                         a given key is currently pressed by the user or not. See its
	 *                         documentation.
	 * @param windowController Contains an array of helpful, self explanatory methods
	 *                         concerning the window.
	 * @see ImageReader
	 * @see SoundReader
	 * @see UserInputListener
	 * @see WindowController
	 */
	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		this.windowController = windowController;
		this.windowDimensions = windowController.getWindowDimensions();
		drops = new ArrayList<>();
		windowController.setTargetFramerate(FRAME_RATE);

		GameObject sky = Sky.create(windowDimensions);
		gameObjects().addGameObject(sky, Layer.BACKGROUND);

		GameObject sun = Sun.create(windowDimensions, NIGHT_CYCLE_LENGTH);
		gameObjects().addGameObject(sun, Layer.BACKGROUND);

		GameObject sunHalo = SunHalo.create(sun);
		gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
		sunHalo.addComponent((deltaTime -> sunHalo.setCenter(sun.getCenter())));

		Terrain terrain = new Terrain(windowDimensions, seed.nextInt());
		List<Block> block_list = terrain.createInRange(0, (int) windowDimensions.x());
		for (Block b : block_list) {
			gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
		}


		GameObject night = Night.create(windowDimensions, NIGHT_CYCLE_LENGTH);
		gameObjects().addGameObject(night, Layer.UI);

		Vector2 avatarInitialLocation = new Vector2(windowDimensions.x()/2,
				terrain.groundHeightAt(windowDimensions.x()/2)- Avatar.AVATAR_SIZE.y());
		this.avatar = new Avatar(avatarInitialLocation, inputListener, imageReader);

		setCamera(new Camera(avatar,
				windowDimensions.mult(0.5f) .subtract(avatarInitialLocation),
				windowDimensions,windowDimensions));
		gameObjects().addGameObject(avatar, Layer.DEFAULT);


		EnergyCounter energyCounter = new EnergyCounter(String.valueOf((int)avatar.getEnergy()));
		GameObject counterObject = new GameObject(
				Vector2.ZERO,
				new Vector2(ENERGY_COUNTER_SIZE, ENERGY_COUNTER_SIZE),
				energyCounter
		);
		counterObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		counterObject.addComponent((deltaTime -> energyCounter.update((int)avatar.getEnergy())));
		gameObjects().addGameObject(counterObject, Layer.UI);

		Flora flora = new Flora(terrain, avatar, this);
		for (GameObject gameObject : flora.createInRange(0, (int) windowDimensions.x())) {
			if (gameObject.getTag().equals("leaf")){
				gameObjects().addGameObject(gameObject, Layer.DEFAULT);
			}
			if (gameObject.getTag().equals("trunk")){
				gameObjects().addGameObject(gameObject, Layer.STATIC_OBJECTS);
			}
			if (gameObject.getTag().equals("fruit")){
				gameObjects().addGameObject(gameObject, Layer.DEFAULT);
			}

		}
		currentCloud = new Cloud();
		currentCloud.create(windowDimensions,avatar,this);
		for (GameObject gameObject : currentCloud.getBlocks()) {
			gameObjects().addGameObject(gameObject, Layer.DEFAULT);
		}
	}

	public void removeFruitAndComeBack(Fruit fruit) {
		Vector2 fruitPos = fruit.getTopLeftCorner();
		this.gameObjects().removeGameObject(fruit);

		new ScheduledTask(
				avatar,
				NIGHT_CYCLE_LENGTH,
				false,
				() -> {
					Fruit newFruit = new Fruit(fruitPos, avatar, this);
					this.gameObjects().addGameObject(newFruit, Layer.DEFAULT);
				}
		);
	}

	/**
	 * Called once per frame. Any logic is put here. Rendering, on the other hand,
	 * should only be done within 'render'.
	 * Note that the time that passes between subsequent calls to this method is not constant.
	 *
	 * @param deltaTime The time, in seconds, that passed since the last invocation
	 *                  of this method (i.e., since the last frame). This is useful
	 *                  for either accumulating the total time that passed since some
	 *                  event, or for physics integration (i.e., multiply this by
	 *                  the acceleration to get an estimate of the added velocity or
	 *                  by the velocity to get an estimate of the difference in position).
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		for (GameObject gameObject : currentCloud.getBlocks()) {
			if (gameObject.getCenter().x() < windowDimensions.x()) {
				return;
			}
		}
		currentCloud = new Cloud();
		currentCloud.create(windowDimensions, avatar, this);
		for (GameObject gameObject : currentCloud.getBlocks()) {
			gameObjects().addGameObject(gameObject, Layer.DEFAULT);
		}

		List<GameObject> toRemove = new ArrayList<>();
		for (GameObject drop : drops) {
			if (drop.getCenter().y() < windowDimensions.y()) {
				toRemove.add(drop);
				gameObjects().removeGameObject(drop);
			}
		}
		drops.removeAll(toRemove);
	}

	public void updateDrops() {
			List<Block> newDrops = currentCloud.getDrops();
			this.drops.addAll(newDrops);
			for (GameObject drop : newDrops){
				gameObjects().addGameObject(drop, Layer.BACKGROUND);
			}
	}

	public static void main(String[] args) {
		new PepseGameManager().run();
	}


}
