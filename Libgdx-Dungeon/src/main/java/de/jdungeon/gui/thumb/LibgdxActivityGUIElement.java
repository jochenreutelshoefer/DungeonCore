package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.app.gui.SubGUIElement;
import de.jdungeon.app.gui.activity.ActivityPresenter;
import de.jdungeon.app.gui.activity.ExecutableActivity;
import de.jdungeon.asset.Assets;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.01.18.
 */
public class LibgdxActivityGUIElement extends LibgdxSubGUIElement {

	private final ExecutableActivity activity;
	private final String activityImage;
	private final String itemBg;

	public LibgdxActivityGUIElement(JDPoint position, JDDimension dimension, LibgdxActivityPresenter parent, ExecutableActivity activity, String activityImage, String itemBg) {
		super(position, dimension, parent);
		this.activity = activity;
		this.activityImage = activityImage;
		this.itemBg = itemBg;
	}

	@Override
	public void paint(SpriteBatch batch) {
		if(itemBg != null) {
			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(itemBg, Assets.instance.getGuiAtlas());
			batch.draw(atlasRegion, getX(), getY(), this.getDimension().getWidth(), this.getDimension().getHeight());
		}
		TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(activityImage, Assets.instance.getGuiAtlas());
		batch.draw(atlasRegion, getX(), getY(), this.getDimension().getWidth(), this.getDimension().getHeight());

	}

	@Override
	public boolean isVisible() {
		return activity.isCurrentlyPossible();
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		activity.execute();
		return true;
	}

}
