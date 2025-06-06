package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import java.util.List;
import java.util.Random;

public class PepseGameManager extends GameManager {
	private static final int FRAME_RATE = 120;
	private static final float NIGHT_CYCLE_LENGTH = 30;
	private static final float ENERGY_COUNTER_SIZE = 50;
	private final Random seed = new Random();
	private WindowController windowController;

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
		windowController.setTargetFramerate(FRAME_RATE);

		GameObject sky = Sky.create(windowController.getWindowDimensions());
		gameObjects().addGameObject(sky, Layer.BACKGROUND);

		GameObject sun = Sun.create(windowController.getWindowDimensions(), NIGHT_CYCLE_LENGTH);
		gameObjects().addGameObject(sun, Layer.BACKGROUND);

		GameObject sunHalo = SunHalo.create(sun);
		gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
		sunHalo.addComponent((deltaTime -> sunHalo.setCenter(sun.getCenter())));

		Terrain terrain = new Terrain(windowController.getWindowDimensions(), seed.nextInt());
		List<Block> block_list = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
		for (Block b : block_list) {
			gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
		}


		GameObject night = Night.create(windowController.getWindowDimensions(), NIGHT_CYCLE_LENGTH);
		gameObjects().addGameObject(night, Layer.UI);

		Vector2 avatarInitialLocation = new Vector2(windowController.getWindowDimensions().x()/2,
				terrain.groundHeightAt(windowController.getWindowDimensions().x()/2)- Avatar.AVATAR_SIZE.y());
		Avatar avatar = new Avatar(avatarInitialLocation, inputListener, imageReader);
		gameObjects().addGameObject(avatar, Layer.DEFAULT);


		EnergyCounter energyCounter = new EnergyCounter(String.valueOf((int)avatar.getEnergy()));
		GameObject counterObject = new GameObject(
				Vector2.ZERO,
				new Vector2(ENERGY_COUNTER_SIZE, ENERGY_COUNTER_SIZE),
				energyCounter
		);
		counterObject.addComponent((deltaTime -> energyCounter.update((int)avatar.getEnergy())));
		gameObjects().addGameObject(counterObject, Layer.UI);

		Flora flora = new Flora(terrain);
		for (GameObject gameObject : flora.createInRange(0, (int) windowController.getWindowDimensions().x())) {
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
	}



	public static void main(String[] args) {
		new PepseGameManager().run();
	}
}
