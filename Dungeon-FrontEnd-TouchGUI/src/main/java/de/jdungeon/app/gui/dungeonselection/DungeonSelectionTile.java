package de.jdungeon.app.gui.dungeonselection;

import java.util.logging.Logger;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.event.EventManager;
import de.jdungeon.graphics.ImageManager;
import de.jdungeon.level.DungeonFactory;
import de.jdungeon.level.DungeonSelectedEvent;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.ImageGUIElement;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.03.16.
 */
public class DungeonSelectionTile extends ImageGUIElement {

	public static final int TILE_WIDTH = 90;
	public static final int TILE_HEIGHT = 90;

	private final DungeonFactory dungeon;
	private final boolean activeStage;
	boolean fired = false;

	public DungeonSelectionTile(DungeonFactory dungeon, JDPoint position, Image im, boolean activeStage, Game game) {
		super(position, new JDDimension(TILE_WIDTH, TILE_HEIGHT), im, (Image) ImageManager.inventory_box_normal.getImage(), game);
		this.dungeon = dungeon;
		this.activeStage = activeStage;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		if(activeStage) {
			Logger.getLogger(this.getClass().getName()).info("handle touch to fire DungeonSelectedEvent");
			if(!fired) {
				Logger.getLogger(this.getClass().getName()).info("firing DungeonSelectedEvent");
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				EventManager.getInstance().fireEvent(new DungeonSelectedEvent(dungeon));
			}
			fired = true;

		}
		return true;
	}
}
