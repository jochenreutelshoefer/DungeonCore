package de.jdungeon.androidapp.gui.skillselection;


import dungeon.JDPoint;
import event.EventManager;
import graphics.ImageManager;
import spell.Spell;
import spell.SpellInfo;
import util.JDColor;
import util.JDDimension;

import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.androidapp.gui.ColorConverter;
import de.jdungeon.androidapp.gui.GUIImageManager;
import de.jdungeon.androidapp.gui.GUIUtils;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;
import de.jdungeon.game.Paint;
import de.jdungeon.util.PaintBuilder;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.18.
 */
public class SkillSelectionTile extends AbstractGUIElement {

	private final Spell skill;
	private final PaintBuilder headerPaint;
	private final PaintBuilder textPaint;
	private final SkillImageManager imageManager;

	public SkillSelectionTile(JDPoint position, JDDimension dimension,
							  StandardScreen screen, Game game, Spell skill) {
		super(position, dimension, screen, game);
		this.skill = skill;
		headerPaint = new PaintBuilder();
		headerPaint.setColor(ColorConverter.getColor(JDColor.black));
		headerPaint.setFontSize(24);

		textPaint = new PaintBuilder();
		textPaint.setColor(ColorConverter.getColor(JDColor.black));
		textPaint.setFontSize(18);
		textPaint.setAlignment(Paint.Alignment.LEFT);

		imageManager = new SkillImageManager(new GUIImageManager(game.getFileIO().getImageLoader()));

	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		EventManager.getInstance().fireEvent(new SkillSelectedEvent(skill));
		return super.handleTouchEvent(touch);
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {

		this.drawBackground(g);

		this.drawBorder(g);


		int iconSize = dimension.getWidth()/2;
		int iconPosX = position.getX()+ dimension.getWidth()/2 - iconSize/2 ;
		int iconPosY = position.getY() + dimension.getHeight()/4;
		Image skillBackgroundImage = (Image) ImageManager.inventory_box_normal.getImage();
		g.drawScaledImage(skillBackgroundImage, iconPosX, iconPosY, iconSize, iconSize, 0, 0, skillBackgroundImage.getWidth(), skillBackgroundImage.getHeight());
		Image skillImage = imageManager.getImage(new SpellInfo(skill, null));
		int iconSizeInternal = (int)(iconSize * 0.8);
		g.drawScaledImage(skillImage, iconPosX + ((iconSize-iconSizeInternal)/2), iconPosY + ((iconSize-iconSizeInternal)/2), iconSizeInternal, iconSizeInternal, 0, 0, skillImage.getWidth(), skillImage.getHeight());
		g.drawString(skill.getName(), iconPosX + iconSize/2, position.getY() + 50, headerPaint);
		int textStartX = position.getX() + dimension.getWidth()/6;
		g.drawString(skill.getText(), textStartX + 5, iconPosY+iconSize + 15, dimension.getWidth()*2/3, textPaint);
	}
}
