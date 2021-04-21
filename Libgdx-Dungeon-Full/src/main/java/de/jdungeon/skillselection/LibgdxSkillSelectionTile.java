package de.jdungeon.skillselection;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.event.EventManager;
import de.jdungeon.graphics.ImageManager;
import de.jdungeon.graphics.JDImageProxy;
import de.jdungeon.spell.Spell;
import de.jdungeon.spell.SpellInfo;
import de.jdungeon.util.JDColor;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.gui.ColorConverter;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.skillselection.SkillImageManager;
import de.jdungeon.app.gui.skillselection.SkillSelectedEvent;
import de.jdungeon.asset.Assets;
import de.jdungeon.game.Game;
import de.jdungeon.game.Paint;
import de.jdungeon.gui.AbstractLibgdxGUIElement;
import de.jdungeon.util.PaintBuilder;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.18.
 */
public class LibgdxSkillSelectionTile extends AbstractLibgdxGUIElement {

	private final Spell skill;
	private final PaintBuilder headerPaint;
	private final PaintBuilder textPaint;
	private final SkillImageManager imageManager;

	GlyphLayout layout;

	public LibgdxSkillSelectionTile(JDPoint position, JDDimension dimension, Game game, Spell skill) {
		super(position, dimension);
		this.skill = skill;
		headerPaint = new PaintBuilder();
		headerPaint.setColor(ColorConverter.getColor(JDColor.black));
		headerPaint.setFontSize(24);

		textPaint = new PaintBuilder();
		textPaint.setColor(ColorConverter.getColor(JDColor.black));
		textPaint.setFontSize(18);
		textPaint.setAlignment(Paint.Alignment.LEFT);

		imageManager = new SkillImageManager(new GUIImageManager(game.getFileIO().getImageLoader()));

		layout = new GlyphLayout();

	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleClickEvent(int screenX, int screenY) {
		EventManager.getInstance().fireEvent(new SkillSelectedEvent(skill));
		return true;
	}


	@Override
	public void paint(SpriteBatch batch, float deltaTime) {

		this.drawBackground(batch, position.getX(), position.getY());


		int iconSize = dimension.getWidth()/2;
		int iconPosX = position.getX()+ dimension.getWidth()/2 - iconSize/2 ;
		int iconPosY = position.getY() + (dimension.getHeight()/2) - iconSize / 2;
		JDImageProxy skillBackgroundImage = ImageManager.inventory_box_normal;
		TextureAtlas.AtlasRegion boxAtlasRegion = Assets.instance.getAtlasRegion(skillBackgroundImage, Assets.instance.getGuiAtlas());

		batch.draw(boxAtlasRegion, iconPosX, iconPosY, 0 , 0 , iconSize, iconSize, 1f , 1f, 0);
		String skillImage = imageManager.getImage(new SpellInfo(skill, null));
		TextureAtlas.AtlasRegion skillAtlasRegion = Assets.instance.getAtlasRegion(skillImage, Assets.instance.getGuiAtlas());

		int iconSizeInternal = (int)(iconSize * 0.8);
		batch.draw(skillAtlasRegion, iconPosX + ((iconSize - iconSizeInternal)/2), iconPosY + ((iconSize - iconSizeInternal)/2), 0 , 0 ,iconSizeInternal, iconSizeInternal, 1f , 1f, 0);

		BitmapFont headerFont = Assets.instance.fonts.defaultBigFlipped;
		int headerWidth = 130;
		layout.setText(headerFont, skill.getName(), Color.WHITE, headerWidth, Align.center, true);
		int headerStartX = position.getX() + dimension.getWidth() / 2 - headerWidth / 2;
		headerFont.draw(batch, layout, headerStartX, position.getY() + 50);

		BitmapFont descriptionFont = Assets.instance.fonts.defaultSmallFlipped;

		int targetWidth = 130;
		layout.setText(descriptionFont, skill.getText(), Color.WHITE, targetWidth, Align.center, true);
		int textStartX = position.getX() + dimension.getWidth() / 2 - targetWidth / 2;
		int textStartY = iconPosY+iconSize + 15;
		descriptionFont.draw(batch, layout, textStartX, textStartY);
		//descriptionFont.draw(batch, de.jdungeon.skill.getText(), textStartX + 5, iconPosY+iconSize + 15); // width : dimension.getWidth()*2/3
	}
}
