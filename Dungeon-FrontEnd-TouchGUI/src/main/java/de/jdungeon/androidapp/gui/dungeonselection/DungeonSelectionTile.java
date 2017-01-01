package de.jdungeon.androidapp.gui.dungeonselection;

import dungeon.JDPoint;
import event.EventManager;
import level.DungeonFactory;
import level.DungeonSelectedEvent;
import util.JDDimension;

import de.jdungeon.androidapp.gui.ImageGUIElement;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.03.16.
 */
public class DungeonSelectionTile extends ImageGUIElement {

	public static final int TILE_WIDTH = 60;
	public static final int TILE_HEIGHT = 60;

	private final DungeonFactory dungeon;
	private final boolean activeStage;

	public DungeonSelectionTile(DungeonFactory dungeon, JDPoint position, Image im, Game dungeonGame, boolean activeStage) {
		super(position, new JDDimension(TILE_WIDTH, TILE_HEIGHT), im, dungeonGame);
		this.dungeon = dungeon;
		this.activeStage = activeStage;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
		g.drawString(dungeon.getName(),this.getPositionOnScreen().getX(), this.getPositionOnScreen().getY() + TILE_HEIGHT + 20 , g.getPaintWhite());
		//g.drawRect(this.position.getX(), this.position.getY(),
		//		this.dimension.getWidth(), this.dimension.getHeight(), Color.YELLOW);
	}

	@Override
	public void handleTouchEvent(Input.TouchEvent touch) {
		if(activeStage) {
			EventManager.getInstance().fireEvent(new DungeonSelectedEvent(dungeon));
		}
	}
}