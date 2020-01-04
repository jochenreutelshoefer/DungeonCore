package de.jdungeon.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import dungeon.RoomInfo;
import figure.RoomObservationStatus;

import de.jdungeon.Constants;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.12.19.
 */
public class WorldRenderer implements Disposable {

	private final PlayerController playerController;
	private final ViewModel viewModel;
	private final OrthographicCamera camera;
	private SpriteBatch batch;
	private final WorldController worldController;

	public WorldRenderer(WorldController worldController, PlayerController playerController, ViewModel viewModel, OrthographicCamera camera) {
		this.worldController = worldController;
		this.playerController = playerController;
		this.viewModel = viewModel;
		this.camera = camera;
		init();
	}

	private void init() {
		batch = new SpriteBatch();
		camera.update();
	}

	public void render() {
		renderDungeon();
		renderTestObjects();
	}

	private void renderDungeon() {
		int dungeonWidth = viewModel.getDungeonWidth();
		int dungeonHeight = viewModel.getDungeonHeight();
		batch.begin();
		for (int x = 0; x < dungeonWidth; x++) {
			for (int y = 0; y < dungeonHeight; y++) {
				ViewRoom room = viewModel.getRoom(x, y);
				Sprite sprite = room.getSprite();
				if (sprite == null) {
					sprite = createRoomSprite(room.getRoomInfo(), x, y);
					room.setSprite(sprite);
				}
				sprite.draw(batch);
			}
		}
		batch.end();
	}

	private Sprite createRoomSprite(RoomInfo roomInfo, int x, int y) {
		int roomSize = 60;
		Pixmap pixmap = new Pixmap(roomSize, roomSize, Pixmap.Format.RGB888);
		pixmap.setColor(0, 0, 0, 1);
		pixmap.fill();
		if (roomInfo.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_ITEMS) {
			pixmap.setColor(0, 0, 1, 1);
		}
		else {
			pixmap.setColor(0, 1, 0, 1);
		}
		drawCross(roomSize, pixmap);
		pixmap.drawRectangle(0, 0, roomSize, roomSize);
		Sprite sprite = new Sprite(new Texture(pixmap));
		sprite.setPosition(roomSize * x, roomSize * y);
		return sprite;
	}

	private void drawCross(int roomSize, Pixmap pixmap) {
		pixmap.drawLine(0, 0, roomSize, roomSize);
		pixmap.drawLine(roomSize, 0, 0, roomSize);
	}

	private void renderTestObjects() {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Sprite sprite : worldController.testSprites) {
			sprite.draw(batch);
		}
		batch.end();
	}

	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
