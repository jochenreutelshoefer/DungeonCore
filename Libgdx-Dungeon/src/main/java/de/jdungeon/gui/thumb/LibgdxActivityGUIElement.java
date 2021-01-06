package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import dungeon.JDPoint;
import figure.action.result.ActionResult;
import figure.percept.TextPercept;
import text.Statement;
import text.StatementManager;
import util.JDDimension;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.asset.Assets;
import de.jdungeon.gui.ImageLibgdxGUIElement;
import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.activity.ActivityPlan;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.01.18.
 */
public class LibgdxActivityGUIElement extends ImageLibgdxGUIElement {

	private final Activity activity;
	private final String bgInactiveImage;

	public LibgdxActivityGUIElement(JDPoint position, JDDimension dimension, Activity activity, String activityImage, String bgImage, String bgInactiveImage) {
		super(position, dimension, activityImage, bgImage);
		if (activity == null) {
			throw new IllegalArgumentException("activity may not be null!");
		}
		this.activity = activity;
		this.bgInactiveImage = bgInactiveImage;
	}

	@Override
	protected String getBackgroundImage() {
		if (activity.possible(null).equals(ActionResult.POSSIBLE)) {
			return backGround;
		}
		else {
			return bgInactiveImage;
		}
	}

	@Override
	public void paint(SpriteBatch batch) {
		super.paint(batch);
		ActionResult possibleState = activity.possible(null);
		ActivityPlan executionPlan = activity.createExecutionPlan(false, null);
		if (executionPlan == null) return; // level exit problem

		int tileWidth = this.getDimension().getWidth();
		int tileHeight = this.getDimension().getHeight();

		int dustCosts = executionPlan.totalDustCosts();
		if (dustCosts > 0 && (possibleState == ActionResult.POSSIBLE || possibleState == ActionResult.DUST)) {
			float scaleFactorDust = 1.9f;
			if (possibleState == ActionResult.POSSIBLE) {
				drawIconAndValue(batch, scaleFactorDust, tileWidth, tileHeight, GUIImageManager.POTION_DUST, dustCosts, true, false);
			}
			if (possibleState == ActionResult.DUST) {
				drawIconAndValue(batch, scaleFactorDust, tileWidth, tileHeight, GUIImageManager.POTION_DUST_SW, dustCosts, true, true);
			}
		}
		if (executionPlan.getLength() > 1 && possibleState == ActionResult.POSSIBLE) {
			float scaleFactorHourGlass = 2.2f;
			drawIconAndValue(batch, scaleFactorHourGlass, tileWidth, tileHeight, GUIImageManager.HOUR_GLASS_EMPTY, executionPlan
					.getLength(), false, false);
		}
	}

	private void drawIconAndValue(SpriteBatch batch, float scaleFactor, int tileWidth, int tileHeight, String image, int value, boolean right, boolean fontColorRed) {
		int x = this.position.getX() + relativeOffsetX;
		int y = this.position.getY() + relativeOffsetY;
		int width = this.dimension.getWidth();

		TextureAtlas.AtlasRegion atlasRegionDust = Assets.instance.getAtlasRegion(image, Assets.instance.getGuiAtlas());
		float dustWidthScale = ((float) atlasRegionDust.originalWidth) / atlasRegionDust.originalHeight;
		float dustIconWidth = tileWidth / scaleFactor * dustWidthScale;

		int xPositionDustIcon = (int) (x + width - 2 * dustIconWidth / 3);
		int xPositionDustNumber = (int) (xPositionDustIcon + dustIconWidth / 2 - 5);
		if (!right) {
			xPositionDustIcon = (int) (x - dustIconWidth / 3);
			xPositionDustNumber = (int) (xPositionDustIcon + dustIconWidth / 2 - 3);
		}

		float dustIconHeight = tileHeight / scaleFactor;
		int yPositionDustIcon = (int) (y - dustIconHeight / 3);
		batch.draw(atlasRegionDust, xPositionDustIcon, yPositionDustIcon,
				dustIconWidth,
				dustIconHeight);

		int yPositionDustNumber = (int) (yPositionDustIcon + dustIconHeight / 2 - 5);
		BitmapFont font = Assets.instance.fonts.defaultNormalFlipped;

		// store normal font color
		Color baseColor = new Color(font.getColor());
		if (fontColorRed) {
			font.setColor(Color.RED);
		}

		// actually draw number; todo: use GlyphLayout
		font.draw(batch, "" + value, xPositionDustNumber, yPositionDustNumber);

		// restore normal font color
		if (fontColorRed) {
			font.setColor(baseColor);
		}
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		ActionResult result = activity.plugToController(null);
		if (result.getSituation() == ActionResult.Situation.possible) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		}
		else {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);
			int round = activity.getPlayerController().getRound();
			Statement statement = StatementManager.getStatement(result, round);
			activity.getPlayerController().tellPercept(new TextPercept(statement.getText(), round));
		}

		return true;
	}
}
