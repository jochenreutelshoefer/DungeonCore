package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.JDPoint;
import event.EventManager;
import figure.hero.HeroInfo;
import game.DungeonGame;
import util.JDDimension;

import de.jdungeon.app.event.EndRoundEvent;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.asset.Assets;
import de.jdungeon.game.Colors;
import de.jdungeon.util.PaintBuilder;

public class LibgdxHourGlassTimer extends AbstractLibgdxGUIElement {

	private final HeroInfo hero;
	private final GUIImageManager imageManager;
	private final PaintBuilder paint;

	public LibgdxHourGlassTimer(JDPoint position, JDDimension dimension, HeroInfo hero, GUIImageManager imageManager) {
		super(position, dimension);
		this.hero = hero;
		this.imageManager = imageManager;
		paint = new PaintBuilder();
		paint.setFontSize(14);
		paint.setColor(Colors.WHITE);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(ShapeRenderer shapeRenderer) {
		// do nothing
	}




	@Override
	public void paint(SpriteBatch batch) {
		String image = null;
		int actionPoints = hero.getActionPoints();
		if (actionPoints == 0) {
			image = GUIImageManager.HOUR_GLASS_EMPTY;
		}
		else if (actionPoints == 1) {
			image = GUIImageManager.HOUR_GLASS_HALF;
		}
		else if (actionPoints == 2) {
			image = GUIImageManager.HOUR_GLASS_FULL;
		}
		if (image != null) {

			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(image, Assets.instance.getGuiAtlas());

			batch.draw(atlasRegion,
					this.position.getX(),
					this.position.getY(),
					this.getDimension().getWidth(),
					this.getDimension().getHeight());

			//fpsFont.setColor(textpaint.getFont().getColor());
			int screenX = this.position.getX() + this.getDimension().getWidth() / 2 - 5;
			int screenY = this.getPositionOnScreen().getY() + this.getDimension().getHeight() + 4;
			Assets.instance.fonts.defaultSmallFlipped.draw(batch, "" + DungeonGame.getInstance().getRound(), screenX, screenY);
		}
	}

	@Override
	public boolean handleClickEvent(int screenX, int screenY) {
		EventManager.getInstance().fireEvent(new EndRoundEvent());
		return true;
	}
}
