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
import java.util.*;

/**
 * The main game manager class for the Pepse game.
 * Handles the initialization and frame-by-frame update logic.
 * Responsible for world generation, avatar interaction, environment management,
 * and efficient chunk loading/unloading for terrain and flora.
 * @author itayturni
 */
public class PepseGameManager extends GameManager {
	private static final int FRAME_RATE = 30;
	private static final float NIGHT_CYCLE_LENGTH = 30;
	private static final float ENERGY_COUNTER_SIZE = 50;
	private static final int SEED = 444;
	private static final int CHUNK_WIDTH = 500;
	private static final int LOAD_RADIUS = 2;

	private final List<Chunk> chunks = new ArrayList<>();
	private Terrain terrain;
	private Flora flora;
	private Random rand;
	private Vector2 windowDimensions;
	private Avatar avatar;
	private Cloud currentCloud;
	private List<GameObject> drops;

	/**
	 * Removes a collected fruit and schedules a new one to appear after a full night cycle.
	 *
	 * @param fruit The fruit GameObject to remove and regenerate.
	 */
	public void removeFruitAndComeBack(Fruit fruit) {
		Vector2 fruitPos = fruit.getTopLeftCorner();
		this.gameObjects().removeGameObject(fruit);

		new ScheduledTask(
				avatar,
				NIGHT_CYCLE_LENGTH,
				false,
				() -> {
					Fruit newFruit = new Fruit(fruitPos, avatar, this, rand);
					this.gameObjects().addGameObject(newFruit, Layer.DEFAULT);
				}
		);
	}

	/**
	 * Updates the current cloud's drops by adding them to the scene.
	 */
	public void updateDrops() {
		List<Block> newDrops = currentCloud.getDrops();
		this.drops.addAll(newDrops);
		for (GameObject drop : newDrops) {
			gameObjects().addGameObject(drop, Layer.BACKGROUND);
		}
	}

	/**
	 * Launch the Pepse game.
	 *
	 * @param args Command line arguments (not used).
	 */
	public static void main(String[] args) {
		new PepseGameManager().run();
	}

	/**
	 * Initializes all components of the game world.
	 */
	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader,
							   UserInputListener inputListener, WindowController windowController) {
		this.rand = new Random(SEED);
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		this.windowDimensions = windowController.getWindowDimensions();
		this.drops = new ArrayList<>();
		windowController.setTargetFramerate(FRAME_RATE);
		createSkyAndLights();
		createTerrain();
		createAvatar(inputListener, imageReader);
		createFlora();
		loadInitialChunks();
		createEnergyCounter();
		createCloud();
	}

	/**
	 * Updates the state of the world each frame.
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		updateCloud();
		removeDrops();
		updateChunks();
	}

	/* Creates the sky, sun, sun halo, and night filter objects. */
	private void createSkyAndLights() {
		GameObject sky = Sky.create(windowDimensions);
		gameObjects().addGameObject(sky, Layer.BACKGROUND);

		GameObject sun = Sun.create(windowDimensions, NIGHT_CYCLE_LENGTH);
		gameObjects().addGameObject(sun, Layer.BACKGROUND);

		GameObject sunHalo = SunHalo.create(sun);
		gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
		sunHalo.addComponent((deltaTime) -> sunHalo.setCenter(sun.getCenter()));

		GameObject night = Night.create(windowDimensions, NIGHT_CYCLE_LENGTH);
		gameObjects().addGameObject(night, Layer.UI);
	}

	/* Initializes the terrain generator. */
	private void createTerrain() {
		this.terrain = new Terrain(windowDimensions, SEED);
	}

	/* Creates the avatar and sets the camera to follow it. */
	private void createAvatar(UserInputListener inputListener, ImageReader imageReader) {
		Vector2 avatarInitialLocation = new Vector2(
				windowDimensions.x() / 2,
				terrain.groundHeightAt(windowDimensions.x() / 2)
						- Avatar.AVATAR_SIZE.y()
		);
		this.avatar = new Avatar(avatarInitialLocation, inputListener, imageReader);
		gameObjects().addGameObject(avatar, Layer.DEFAULT);

		setCamera(new Camera(
				avatar,
				windowDimensions.mult(0.5f).subtract(avatarInitialLocation),
				windowDimensions,
				windowDimensions
		));
	}

	/* Creates the flora generator. */
	private void createFlora() {
		this.flora = new Flora(terrain, avatar, this, rand, SEED);
	}

	/* Creates the energy counter and places it on screen. */
	private void createEnergyCounter() {
		EnergyCounter energyCounter =
				new EnergyCounter(String.valueOf((int) avatar.getEnergy()));
		GameObject counterObject = new GameObject(
				Vector2.ZERO,
				new Vector2(ENERGY_COUNTER_SIZE, ENERGY_COUNTER_SIZE),
				energyCounter
		);
		counterObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		counterObject.addComponent((deltaTime) ->
				energyCounter.update((int) avatar.getEnergy()));
		gameObjects().addGameObject(counterObject, Layer.UI);
	}

	/* Creates a moving cloud with attached rain-drop logic. */
	private void createCloud() {
		currentCloud = new Cloud();
		currentCloud.create(windowDimensions, avatar, this);
		for (GameObject gameObject : currentCloud.getBlocks()) {
			gameObjects().addGameObject(gameObject, Layer.DEFAULT);
		}
	}

	/* Replaces the cloud if it has moved off-screen. */
	private void updateCloud() {
		for (GameObject gameObject : currentCloud.getBlocks()) {
			if (gameObject.getCenter().x() < windowDimensions.x()) {
				return;
			}
		}
		currentCloud = new Cloud();
		currentCloud.create(windowDimensions, avatar, this);
		for (GameObject gameObject : currentCloud.getBlocks()) {
			gameObjects().addGameObject(gameObject, Layer.BACKGROUND);
		}
	}

	/* Removes raindrop objects that are off-screen. */
	private void removeDrops() {
		List<GameObject> toRemove = new ArrayList<>();
		for (GameObject drop : drops) {
			if (drop.getCenter().y() < windowDimensions.y()) {
				toRemove.add(drop);
				gameObjects().removeGameObject(drop);
			}
		}
		drops.removeAll(toRemove);
	}

	/* Loads the terrain and flora around the initial avatar chunk. */
	private void loadInitialChunks() {
		int avatarChunk = (int) (avatar.getCenter().x() / CHUNK_WIDTH);
		for (int i = avatarChunk - LOAD_RADIUS; i <= avatarChunk + LOAD_RADIUS; i++) {
			loadChunk(i);
		}
	}

	/* Loads and unloads chunks as the avatar moves across the terrain. */
	private void updateChunks() {
		final int avatarChunk = (int) (avatar.getCenter().x() / CHUNK_WIDTH);

		for (int i = avatarChunk - LOAD_RADIUS; i <= avatarChunk + LOAD_RADIUS; i++) {
			final int chunkIndex = i;
			if (chunks.stream().noneMatch(c -> c.index == chunkIndex)) {
				loadChunk(chunkIndex);
			}
		}

		chunks.removeIf(chunk -> {
			if (chunk.isFarFrom(avatarChunk)) {
				for (GameObject obj : chunk.objects) {
					gameObjects().removeGameObject(obj);
				}
				return true;
			}
			return false;
		});
	}

	/* Loads a specific chunk of terrain and flora. */
	private void loadChunk(int chunkIndex) {
		int minX = chunkIndex * CHUNK_WIDTH;
		int maxX = minX + CHUNK_WIDTH;
		List<GameObject> chunkObjects = new ArrayList<>();
		Random chunkRandom = new Random(SEED + chunkIndex);

		for (GameObject obj : terrain.createInRange(minX, maxX)) {
			gameObjects().addGameObject(obj, Layer.STATIC_OBJECTS);
			chunkObjects.add(obj);
		}

		for (GameObject obj : flora.createInRange(minX, maxX, chunkRandom)) {
			switch (obj.getTag()) {
				case "leaf", "fruit" -> gameObjects().addGameObject(obj, Layer.DEFAULT);
				case "trunk" -> gameObjects().addGameObject(obj, Layer.STATIC_OBJECTS);
			}
			chunkObjects.add(obj);
		}

		chunks.add(new Chunk(chunkIndex, chunkObjects));
	}

	/*
	 * Represents a chunk of terrain and associated objects in the world.
	 */
	private static class Chunk {
		int index;
		List<GameObject> objects;

		Chunk(int index, List<GameObject> objects) {
			this.index = index;
			this.objects = objects;
		}

		/* Determines whether the chunk is outside the avatar's load radius. */
		boolean isFarFrom(int avatarChunkIndex) {
			return Math.abs(index - avatarChunkIndex) > LOAD_RADIUS;
		}
	}
}
