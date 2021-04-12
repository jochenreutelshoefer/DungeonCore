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


	private static final String WINDOW_BG_LOWER_LEFT = "win-bg-lu";
	private static final String WINDOW_BG_LEFT = "win-bg-l";
	private static final String WINDOW_BG_LOWER = "win-bg-u";
	private static final String WINDOW_BG_INNER = "win-bg-innen";
	private static final String WINDOW_BORDER_LEFT = "win-border2-l";
	private static final String WINDOW_BORDER_TOP = "win-border2-t";
	private static final String WINDOW_EDGE_LT = "win-ecke";

	protected JDPoint position;
	protected final JDDimension dimension;
	Texture bgTexture;
	private TextureAtlas.AtlasRegion cornerLB;
	private TextureRegion cornerRB;
	private TextureRegion cornerLT;
	private TextureRegion cornerRT;
	private TextureAtlas.AtlasRegion sideL;
	private TextureRegion sideR;
	private TextureAtlas.AtlasRegion bottom;
	private TextureRegion top;
	private TextureAtlas.AtlasRegion inside;
	private TextureRegion insideFlipped;
	private TextureAtlas.AtlasRegion borderLeft;
	private TextureAtlas.AtlasRegion borderTop;
	private TextureRegion borderRight;
	private TextureRegion borderBottom;
	private TextureAtlas.AtlasRegion edgeLT;
	private TextureRegion edgeRT;
	private TextureRegion edgeRB;
	private TextureRegion edgeLB;

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
	public boolean needsRepaint() {
		return true;
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
	public void update(float time) {
		// TODO Auto-generated method stub
	}


	protected void drawBackground(SpriteBatch batch, int currentX, int currentY) {

		//batch.draw(bgTexture, currentX, currentY);

		int borderWidth = borderLeft.originalWidth;


		int dimensionBGSheetX  = this.dimension.getWidth() - 2 * borderWidth;
		int dimensionBGSheetY  = this.dimension.getHeight() - 2 * borderWidth;


		int bgSheetX = currentX + borderWidth;
		int bgSheetY = currentY + borderWidth;


		int innerBottomY = bgSheetY + dimensionBGSheetY - cornerLB.originalHeight;
		int innerRightX = bgSheetX + dimensionBGSheetX - cornerLB.originalWidth;


		// TODO: refactor using scale -1 to create the flipped textures which is more efficient and requires less assets

		// inside bottom
		batch.draw(inside, bgSheetX + cornerLB.originalWidth,  innerBottomY - inside.originalHeight,  0 , 0 ,dimensionBGSheetX - 2 * cornerLB.originalWidth, inside.originalHeight, 1f , 1f, 0);

		// inside top
		//batch.draw(inside, bgSheetX + cornerLB.originalWidth, innerBottomY - 2 * inside.originalHeight,0 , 0 ,dimensionBGSheetX - 2 * cornerLB.originalWidth, inside.originalHeight, 1f , -1f, 0);
		int insideTopHeight = dimensionBGSheetY - cornerLB.originalHeight - inside.originalHeight ; // + 1 for rounding issues
		batch.draw(insideFlipped, bgSheetX + cornerLB.originalWidth, innerBottomY - (cornerLB.originalHeight + insideTopHeight),dimensionBGSheetX - 2 * cornerLB.originalWidth, insideTopHeight + 1 /*+1 for rounding issues*/);

		// bottom
		batch.draw(bottom, bgSheetX + cornerLB.originalWidth, innerBottomY, 0 , 0 ,dimensionBGSheetX - 2 * cornerLB.originalWidth, bottom.originalHeight, 1f , 1f, 0);

		// top
		batch.draw(top, bgSheetX + cornerLB.originalWidth, bgSheetY, 0 , 0 ,dimensionBGSheetX - 2 * cornerLB.originalWidth, bottom.originalHeight + 1, 1f , 1f, 0);
		//batch.draw(bottom, bgSheetX + cornerLB.originalWidth, bgSheetY, 0 , 0 ,dimensionBGSheetX - 2 * cornerLB.originalWidth, bottom.originalHeight, 1f , -1f, 0);

		// left side
		int insideHeight = dimensionBGSheetY - 2 * cornerLB.originalHeight;
		batch.draw(sideL, bgSheetX, innerBottomY - insideHeight, 0 , 0 , sideL.originalWidth, insideHeight, 1f , 1f, 0);

		// right side
		batch.draw(sideR, innerRightX, innerBottomY - insideHeight, 0 , 0 , sideL.originalWidth, insideHeight, 1f , 1f, 0);

		// left bottom corner
		batch.draw(cornerLB, bgSheetX, innerBottomY);

		// right bottom corner
		batch.draw(cornerRB, innerRightX, innerBottomY);

		// right top corner
		batch.draw(cornerRT, innerRightX, bgSheetY);

		// left top corner
		batch.draw(cornerLT, bgSheetX , bgSheetY);



		// draw border
		batch.draw(borderLeft, currentX, currentY,  borderWidth, this.dimension.getHeight());
		batch.draw(borderRight, currentX + this.dimension.getWidth()  - borderWidth, currentY, borderWidth, this.dimension.getHeight());
		batch.draw(borderTop, currentX, currentY , this.dimension.getWidth(),  borderWidth);
		batch.draw(borderBottom, currentX, currentY + this.dimension.getHeight() - borderWidth, this.dimension.getWidth(), borderWidth);


		// draw edges


		int edgeSize = edgeLT.originalWidth * 2 / 3;
		batch.draw(edgeLT, currentX - 3, currentY - 3, 0 , 0, edgeSize, edgeSize, 1f , 1f, 0 );
		batch.draw(edgeLB, currentX - 3, currentY + this.dimension.getHeight() - edgeSize + 3, 0 , 0, edgeSize, edgeSize, 1f , 1f, 0 );
		batch.draw(edgeRT, currentX + this.dimension.getWidth() - edgeSize + 3, currentY - 3, 0 , 0, edgeSize, edgeSize, 1f , 1f, 0 );
		batch.draw(edgeRB, currentX + this.dimension.getWidth() - edgeSize + 3, currentY + this.dimension.getHeight() - edgeSize + 3, 0 , 0, edgeSize, edgeSize, 1f , 1f, 0 );

	}





}

