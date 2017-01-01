package de.jdungeon.androidapp.gui.dungeonselection;

import java.util.List;

import dungeon.JDPoint;
import level.DungeonFactory;
import level.DungeonManager;
import user.DungeonSession;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.androidapp.screen.start.MenuScreen;
import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.03.16.
 */
public class DungeonSelectionScreen extends MenuScreen {

	private final DungeonSession session;
	private final DungeonManager dungeonManager;
	private final int offset;
	private final AbstractImageLoader imageLoader;
	int iconIndex = 0;
	private final int stageHeightOffset;

	public DungeonSelectionScreen(Game game, DungeonSession session) {
		super(game);
		this.session = session;
		dungeonManager = session.getDungeonManager();
		int stage = session.getCurrentStage();
		stageHeightOffset = 150;
		offset = game.getScreenHeight()/2 + (stageHeightOffset * stage);

		imageLoader = game.getFileIO().getImageLoader();
		for(int i = 0; i < dungeonManager.getNumberOfStages(); i++) {
			addGUIElementsForStage(i, dungeonManager.getDungeonOptions(i));
		}
	}

	private void addGUIElementsForStage(int stage, List<DungeonFactory> dungeonOptions) {
		int innerStageIndex = 0;
		for (DungeonFactory dungeonOption : dungeonOptions) {
			Image image = LevelIconImageManager.getInstance().getIcon(imageLoader, iconIndex++);
			//Image image = (Image)ImageManager.engelImage.getImage();
			JDPoint tilePosition = new JDPoint(150 + (innerStageIndex * stageHeightOffset), offset - (stageHeightOffset * stage));
			this.guiElements.add(new DungeonSelectionTile(dungeonOption, tilePosition, image, this.game, session.getCurrentStage() == stage));
			innerStageIndex++;
		}
	}

	@Override
	public void init() {

	}


	@Override
	protected String getHeaderString() {
		return "Dungeon wählen für Ebene " + (session.getCurrentStage()+1);
	}

}
