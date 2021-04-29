package de.jdungeon.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.asset.Assets;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */

public abstract class AbstractLibgdxGUIElement implements LibgdxGUIElement {

	protected JDPoint position;
	protected final JDDimension dimension;
	TextureRegion bgTexture;
	protected TextureAtlas.AtlasRegion window_badge = Assets.instance.getAtlasRegion("window_badge", Assets.instance.getGuiAtlas());

	public AbstractLibgdxGUIElement(JDPoint position, JDDimension dimension) {
		super();
		this.position = position;
		this.dimension = dimension;
		init();
	}

	public void init() {
		createBackgroundTexture();
	}

	private void createBackgroundTexture() {

		bgTexture = Assets.instance.getAtlasRegion("window_border5", Assets.instance.getGuiAtlas());
		/*
		// init edge
		edgeLT = Assets.instance.getAtlasRegion(WINDOW_EDGE_LT, Assets.instance.getGuiAtlas());
		edgeRT = new TextureRegion(edgeLT);
		edgeRT.flip(true, false);

		edgeRB = new TextureRegion(edgeLT);
		edgeRB.flip(true, true);

		edgeLB = new TextureRegion(edgeLT);
		edgeLB.flip(false, true);

		// init border
		borderLeft = Assets.instance.getAtlasRegion(WINDOW_BORDER_LEFT, Assets.instance.getGuiAtlas());
		borderRight = new TextureRegion(borderLeft);
		borderRight.flip(true, false);

		borderTop = Assets.instance.getAtlasRegion(WINDOW_BORDER_TOP, Assets.instance.getGuiAtlas());
		borderBottom = new TextureRegion(borderTop);
		borderBottom.flip(true, false);



		// init corners
		cornerLB = Assets.instance.getAtlasRegion(WINDOW_BG_LOWER_LEFT, Assets.instance.getGuiAtlas());
		cornerRB = new TextureRegion(cornerLB);
		cornerRB.flip(true, false);

		cornerLT = new TextureRegion(cornerLB);
		cornerLT.flip(false, true);

		cornerRT = new TextureRegion(cornerLB);
		cornerRT.flip(true, true);

		// init sides
		sideL = Assets.instance.getAtlasRegion(WINDOW_BG_LEFT, Assets.instance.getGuiAtlas());
		sideR = new TextureRegion(sideL);
		sideR.flip(true, false);

		// init bottom/top
		bottom = Assets.instance.getAtlasRegion(WINDOW_BG_LOWER, Assets.instance.getGuiAtlas());
		top = new TextureRegion(bottom);
		top.flip(false, true);

		// init inside
		inside = Assets.instance.getAtlasRegion(WINDOW_BG_INNER, Assets.instance.getGuiAtlas());
		insideFlipped = new TextureRegion(inside);
		insideFlipped.flip(false, true);

		 */

		/*
		int width = getDimension().getWidth();
		int height = getDimension().getHeight();
		Pixmap bgPixmap = new Pixmap(width, height, Pixmap.Format.RGB888);
		bgPixmap.setColor(Color.WHITE);
		bgPixmap.fill();
		bgPixmap.setColor(com.badlogic.gdx.de.jdungeon.graphics.Color.WHITE);
		bgPixmap.drawRectangle(0, 0, width, height);
		this.bgTexture = new Texture(bgPixmap);
		*/
	}

	@Override
	public boolean handlePanEvent(float x, float y, float dx, float dy) {
		// do nothing usually
		return false;
	}




	@Override
	public boolean isAnimated() {
		return false;
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		// check whether point p is in rectangle
		return p.getX() >= position.getX()
				&& p.getX() <= position.getX() + dimension.getWidth()
				&& p.getY() >= position.getY()
				&& p.getY() <= position.getY() + dimension.getHeight();
	}


	@Override
	public JDPoint getPositionOnScreen() {
		return position;
	}

	@Override
	public JDDimension getDimension() {
		return dimension;
	}

	@Override
	public void update(float time, int round) {
		// TODO Auto-generated method stub
	}


	protected void drawBackground(SpriteBatch batch, int currentX, int currentY) {
		if(bgTexture != null) {
			batch.draw(bgTexture, currentX, currentY, this.getDimension().getWidth(), this.dimension.getHeight());
		}
	}





}

