package de.jdungeon.gui;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import dungeon.JDPoint;
import figure.attribute.Attribute;
import figure.hero.HeroInfo;
import graphics.ImageManager;
import graphics.JDImageProxy;
import util.JDDimension;

import de.jdungeon.asset.Assets;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;
import de.jdungeon.ui.AbstractLibgdxGUIElement;

public class LibgdxHealthBar extends AbstractLibgdxGUIElement {

	private final HeroInfo figure;
	private final de.jdungeon.app.gui.HealthBar.Kind kind;

	public enum Kind {
		health, dust
	}

	public LibgdxHealthBar(JDPoint position, JDDimension dimension, HeroInfo info,
						   de.jdungeon.app.gui.HealthBar.Kind kind, Game game) {
		super(position, dimension, game);
		this.figure = info;
		this.kind = kind;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(SpriteBatch batch) {

		double baseValue = 1;
		double actualValue = 1;
		JDImageProxy coloredBar = null;
		if (kind == de.jdungeon.app.gui.HealthBar.Kind.health) {
			baseValue = figure.getAttributeBasic(Attribute.HEALTH);
			actualValue = figure.getAttributeValue(Attribute.HEALTH);
			coloredBar = ImageManager.health_bar_red;
		}
		else if (kind == de.jdungeon.app.gui.HealthBar.Kind.dust) {
			baseValue = figure.getAttributeBasic(Attribute.DUST);
			actualValue = figure.getAttributeValue(Attribute.DUST);
			coloredBar = ImageManager.health_bar_yellow;
		}

		if (actualValue < 0) {
			// we do not display values below zero
			actualValue = 0;
		}

		double percentage = (actualValue / baseValue);

		JDImageProxy<?> background = ImageManager.health_bar_empty;
		Image image = (Image) background.getImage();
		TextureAtlas.AtlasRegion emptyBarAtlasRegion = Assets.instance.getAtlasRegion(background, Assets.instance.getGuiAtlas());

		batch.draw(emptyBarAtlasRegion,
				position.getX(),
				position.getY(),
				dimension.getWidth(),
				dimension.getHeight());

		TextureAtlas.AtlasRegion coloredBarAtlasRegion = Assets.instance.getAtlasRegion(coloredBar, Assets.instance.getGuiAtlas());
		batch.draw(coloredBarAtlasRegion,
				position.getX(),
				position.getY(),
				(int) (dimension.getWidth() * percentage),
				dimension.getHeight()
		);
	}

	@Override
	public boolean handleClickEvent(int screenX, int screenY) {
		return false;
	}
}
