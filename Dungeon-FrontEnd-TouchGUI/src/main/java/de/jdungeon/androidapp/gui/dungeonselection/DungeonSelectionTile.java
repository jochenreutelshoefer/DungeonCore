package de.jdungeon.androidapp.gui.dungeonselection;

import dungeon.JDPoint;
import event.EventManager;
import graphics.ImageManager;
import level.DungeonFactory;
import level.DungeonSelectedEvent;
import util.JDDimension;

import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.gui.GUIImageManager;
import de.jdungeon.androidapp.gui.ImageGUIElement;
import de.jdungeon.androidapp.gui.InventoryImageManager;
import de.jdungeon.game.Colors;
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

	public DungeonSelectionTile(DungeonFactory dungeon, JDPoint position, Image im, boolean activeStage) {
		super(position, new JDDimension(TILE_WIDTH, TILE_HEIGHT), im, (Image) ImageManager.inventory_box_normal.getImage());
		this.dungeon = dungeon;
		this.activeStage = activeStage;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
		//g.drawString(dungeon.getName(), this.getPositionOnScreen().getX()+getDimension().getWidth()/2, this.getPositionOnScreen().getY() + TILE_HEIGHT + 20 , g.getPaintWhite());
		//g.drawRect(this.position.getX(), this.position.getY(),
		//		this.dimension.getWidth(), this.dimension.getHeight(), Colors.YELLOW);
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		if(activeStage) {
			EventManager.getInstance().fireEvent(new DungeonSelectedEvent(dungeon));
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);

		} else {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);
		}
		return true;
	}
}
