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
	private WindowController windowController;
	private Vector2 windowDimensions;
	private Avatar avatar;
	private Cloud currentCloud;
	private List<GameObject> drops;

	private static class Chunk {
		int index;
		List<GameObject> objects;

		Chunk(int index, List<GameObject> objects) {
			this.index = index;
			this.objects = objects;
		}

		boolean isFarFrom(int avatarChunkIndex) {
			return Math.abs(index - avatarChunkIndex) > LOAD_RADIUS;
		}
	}

	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
		this.rand = new Random(SEED);
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
		GameObject night = Night.create(windowDimensions, NIGHT_CYCLE_LENGTH);
		gameObjects().addGameObject(night, Layer.UI);

		this.terrain = new Terrain(windowDimensions, SEED);

		Vector2 avatarInitialLocation = new Vector2(windowDimensions.x() / 2,
				terrain.groundHeightAt(windowDimensions.x() / 2) - Avatar.AVATAR_SIZE.y());
		this.avatar = new Avatar(avatarInitialLocation, inputListener, imageReader);

		setCamera(new Camera(avatar,
				windowDimensions.mult(0.5f).subtract(avatarInitialLocation),
				windowDimensions, windowDimensions));
		gameObjects().addGameObject(avatar, Layer.DEFAULT);

		this.flora = new Flora(terrain, avatar, this, rand, SEED);

		final int avatarChunk = (int) (avatar.getCenter().x() / CHUNK_WIDTH);
		for (int i = avatarChunk - LOAD_RADIUS; i <= avatarChunk + LOAD_RADIUS; i++) {
			loadChunk(i);
		}

		EnergyCounter energyCounter = new EnergyCounter(String.valueOf((int) avatar.getEnergy()));
		GameObject counterObject = new GameObject(
				Vector2.ZERO,
				new Vector2(ENERGY_COUNTER_SIZE, ENERGY_COUNTER_SIZE),
				energyCounter
		);
		counterObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		counterObject.addComponent((deltaTime -> energyCounter.update((int) avatar.getEnergy())));
		gameObjects().addGameObject(counterObject, Layer.UI);

		currentCloud = new Cloud();
		currentCloud.create(windowDimensions, avatar, this);
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
					Fruit newFruit = new Fruit(fruitPos, avatar, this, rand);
					this.gameObjects().addGameObject(newFruit, Layer.DEFAULT);
				}
		);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		updateCloud();
		removeDrops();
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

	public void updateDrops() {
		List<Block> newDrops = currentCloud.getDrops();
		this.drops.addAll(newDrops);
		for (GameObject drop : newDrops) {
			gameObjects().addGameObject(drop, Layer.BACKGROUND);
		}
	}

	public static void main(String[] args) {
		new PepseGameManager().run();
	}
}
