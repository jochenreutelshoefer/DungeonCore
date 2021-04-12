package de.jdungeon.gui;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.graphics.ImageManager;
import de.jdungeon.graphics.JDImageProxy;
import de.jdungeon.util.JDDimension;

import de.jdungeon.asset.Assets;

public class LibgdxHealthBar extends AbstractLibgdxGUIElement {

	private final HeroInfo figure;
	private final LibgdxHealthBar.Kind kind;

	public enum Kind {
		health, dust, oxygen
	}

	public LibgdxHealthBar(JDPoint position, JDDimension dimension, HeroInfo info,
						   LibgdxHealthBar.Kind kind) {
		super(position, dimension);
		this.figure = info;
		this.kind = kind;
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

		double baseValue = 1;
		double actualValue = 1;
		JDImageProxy coloredBar = null;
		if (kind == Kind.health) {
			baseValue = figure.getAttributeBasic(Attribute.Type.Health);
			actualValue = figure.getAttributeValue(Attribute.Type.Health);
			coloredBar = ImageManager.health_bar_red;
		}
		else if (kind == Kind.dust) {
			baseValue = figure.getAttributeBasic(Attribute.Type.Dust);
			actualValue = figure.getAttributeValue(Attribute.Type.Dust);
			coloredBar = ImageManager.health_bar_yellow;
		}else if (kind == Kind.oxygen) {
			Attribute oxygen = figure.getAgility().getOxygen();
			baseValue = oxygen.getBasic();
			actualValue = oxygen.getValue();
			coloredBar = ImageManager.health_bar_blue;
		}

		if (actualValue < 0) {
			// we do not display values below zero
			actualValue = 0;
		}

		double percentage = (actualValue / baseValue);

		JDImageProxy<?> background = ImageManager.health_bar_empty;
		TextureAtlas.AtlasRegion emptyBarAtlasRegion = Assets.instance.getAtlasRegion(background, Assets.instance.getGuiAtlas());

		batch.draw(emptyBarAtlasRegion,
				position.getX(),
				position.getY(),
				dimension.getWidth(),
				dimension.getHeight());

		TextureAtlas.AtlasRegion coloredBarAtlasRegion = Assets.instance.getAtlasRegion(coloredBar, Assets.instance.getGuiAtlas());
		double widthPercentage = dimension.getWidth() * percentage;
		batch.draw(coloredBarAtlasRegion,
				position.getX(),
				position.getY(),
				(int) widthPercentage,
				dimension.getHeight()
		);
	}

	@Override
	public boolean handleClickEvent(int screenX, int screenY) {
		return false;
	}
}

