package de.jdungeon.androidapp.gui.dungeonselection;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import dungeon.JDPoint;
import io.AbstractImageLoader;
import level.DungeonFactory;
import level.DungeonManager;
import user.DungeonSession;

import de.jdungeon.androidapp.io.AndroidImageLoader;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;
import de.jdungeon.implementation.AndroidGame;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.03.16.
 */
public class DungeonSelectionScreen extends StandardScreen {

	private final DungeonSession session;
	private final DungeonManager dungeonManager;
	private int offset;
	private final AbstractImageLoader imageLoader;
	int iconIndex = 0;

	public static Paint labelPaint;
	protected Paint paint;

	{
		labelPaint = new Paint();
		labelPaint.setTextSize(10);
		labelPaint.setTextAlign(Paint.Align.CENTER);
		labelPaint.setAntiAlias(true);
		labelPaint.setColor(Color.WHITE);
	}

	public DungeonSelectionScreen(Game game, DungeonSession session) {
		super(game);
		this.session = session;
		dungeonManager = session.getDungeonManager();
		int stage = session.getCurrentStage();
		offset = 300 + (100 * stage);

		imageLoader = new AndroidImageLoader((AndroidGame)game);
		for(int i = 0; i < dungeonManager.getNumberOfStages(); i++) {
			addGUIElementsForStage(i, dungeonManager.getDungeonOptions(i));
		}
	}

	private void addGUIElementsForStage(int stage, List<DungeonFactory> dungeonOptions) {
		int innerStageIndex = 0;
		for (DungeonFactory dungeonOption : dungeonOptions) {
			Image image = LevelIconImageManager.getInstance().getIcon(imageLoader, iconIndex++);
			//Image image = (Image)ImageManager.engelImage.getImage();
			JDPoint tilePosition = new JDPoint(150 + (innerStageIndex * 100), offset - (100 * stage));
			this.guiElements.add(new DungeonSelectionTile(dungeonOption, tilePosition, image, this.game, session.getCurrentStage() == stage));
			innerStageIndex++;
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void paint(float deltaTime) {
		super.paint(deltaTime);

		game.getGraphics().drawString("Choose your next Dungeon for stage "+session.getCurrentStage(), 465, 165, defaultPaint);
	}

}
