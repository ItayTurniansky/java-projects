package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;

import java.util.List;
import java.util.Random;

public class PepseGameManager extends GameManager {
	private static final int FRAME_RATE = 120;
	private static final float NIGHT_CYCLE_LENGTH = 30;
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
		createTerrain();
		GameObject night = Night.create(windowController.getWindowDimensions(), NIGHT_CYCLE_LENGTH);
		gameObjects().addGameObject(night, Layer.UI);

	}

	private void createTerrain() {
		Terrain terrain = new Terrain(windowController.getWindowDimensions(), seed.nextInt());
		List<Block> block_list = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
		for (Block b : block_list) {
			gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
		}
	}


	public static void main(String[] args) {
		new PepseGameManager().run();
	}
}
