package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import de.jdungeon.asset.Assets;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

public class MissionStatePanel extends LibgdxSlidingInOutGUIElement {

	private BitmapFont font;
	private GlyphLayout layout;

	public MissionStatePanel(JDPoint position, JDDimension dimension) {
		super(position, dimension, new JDPoint(position.getX() + dimension.getWidth() - 80, position.getY()), "window_badge");
		layout = new GlyphLayout();
	}

	@Override
	public void paint(SpriteBatch batch, float deltaTime) {

		this.drawBackground(batch, this.getCurrentX(), this.getPositionOnScreen().getY());

		String headerText = "Mission:";
		font = Assets.instance.fonts.defaultNormalFlipped;
		float headerTargetWidth = this.dimension.getWidth() * 0.8f;
		layout.setText(font, headerText, com.badlogic.gdx.graphics.Color.WHITE, headerTargetWidth, Align.left, true);
		float targetWidthMargin = this.dimension.getWidth() * 0.1f;
		float fontPosX = this.getCurrentX() + targetWidthMargin;
		int fontPosYHeader = position.getY() + (this.dimension.getHeight() / 6);
		font.draw(batch, layout, fontPosX, fontPosYHeader);
	}

	@Override
	public boolean isVisible() {
		return true;
	}
}
