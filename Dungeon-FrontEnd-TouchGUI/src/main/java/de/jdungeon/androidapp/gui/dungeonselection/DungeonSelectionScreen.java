package de.jdungeon.androidapp.gui.dungeonselection;

import java.util.List;

import animation.AnimationSet;
import animation.Motion;
import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import figure.hero.Hero;
import graphics.ImageManager;
import graphics.JDImageProxy;
import level.DungeonFactory;
import level.DungeonManager;
import user.DungeonSession;
import util.JDDimension;

import de.jdungeon.androidapp.gui.DrawGUIElement;
import de.jdungeon.androidapp.gui.ImageGUIElement;
import de.jdungeon.androidapp.gui.SimpleImageGUIElement;
import de.jdungeon.androidapp.screen.start.MenuScreen;
import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.03.16.
 */
public class DungeonSelectionScreen extends MenuScreen {


	private DungeonSession session;
	private final DungeonManager dungeonManager;
	private final int offset;
	private final AbstractImageLoader imageLoader;
	int iconIndex = 0;
	private final int stageHeightOffset;
	private final int xCenterValue;
	private int dotX;
	private int dotSize;

	public DungeonSelectionScreen(Game game, DungeonSession session) {
		super(game);
		this.session = session;
		dungeonManager = session.getDungeonManager();
		int currentStage = session.getCurrentStage();
		stageHeightOffset = 180;
		offset = game.getScreenHeight()*2/3 + (stageHeightOffset * currentStage);
		xCenterValue = game.getScreenWidth()/2;

		AnimationSet animationSet = ImageManager.getAnimationSet(Hero.HeroCategory.fromValue(session.getCurrentHero()
				.getHeroCode()), Motion.Walking, RouteInstruction.Direction.North);
		JDImageProxy heroImage = animationSet.getImagesNr(0);

		imageLoader = game.getFileIO().getImageLoader();
		for(int i = 0; i < dungeonManager.getNumberOfStages(); i++) {
			addGUIElementsForStage(i, dungeonManager.getDungeonOptions(i));
			if(i == currentStage) {
				JDDimension heroDimension = new JDDimension(180, 180);
				guiElements.add(new SimpleImageGUIElement(new JDPoint(xCenterValue-heroDimension.getWidth()/2, getDotY(getY(i)) + dotSize - heroDimension.getHeight()*2/3 ), heroDimension, (Image)heroImage.getImage()));
			}
		}
	}

	private void addGUIElementsForStage(int stage, List<DungeonFactory> dungeonOptions) {



		int xDistance = 150;
		int [] [] coordinatesX  = {
				{ xCenterValue, -1, -1},
				{ xCenterValue - xDistance/2,  xCenterValue + xDistance/2, -1 },
				{ xCenterValue - xDistance, xCenterValue, xCenterValue + xDistance },
		};

		final int y = getY(stage);

		dotX = xCenterValue;
		final int dotY = getDotY(y);
		dotSize = 16;

		// we draw the track lines between the tiles also as GUIElements
		this.guiElements.add(new DrawGUIElement() {
			@Override
			public void paint(Graphics g, JDPoint viewportPosition) {
				g.drawOval(dotX - dotSize /2, dotY - dotSize /2, dotSize, dotSize, Colors.WHITE);
			}
		});

		//
		int innerStageIndex = 0;
		for (DungeonFactory dungeonOption : dungeonOptions) {
			final int x = coordinatesX[dungeonOptions.size()-1][innerStageIndex];

			// we draw the track lines between the tiles also as GUIElements
			this.guiElements.add(new DrawGUIElement() {
				@Override
				public void paint(Graphics g, JDPoint viewportPosition) {
					g.drawLine(x, y+DungeonSelectionTile.TILE_WIDTH/10, dotX, dotY - stageHeightOffset, Colors.WHITE);
					g.drawLine(x, y+DungeonSelectionTile.TILE_WIDTH*9/10, dotX, dotY, Colors.WHITE);
				}
			});

			Image image = LevelIconImageManager.getInstance().getIcon(imageLoader, iconIndex++);
			//Image image = (Image)ImageManager.engelImage.getImage();
			//int x = xDistance + (innerStageIndex * stageHeightOffset);
			JDPoint tilePosition = new JDPoint(x-DungeonSelectionTile.TILE_WIDTH/2, y);
			this.guiElements.add(new DungeonSelectionTile(dungeonOption, tilePosition, image, session.getCurrentStage() == stage));





			innerStageIndex++;
		}
	}

	private int getY(int stage) {
		return offset - (stageHeightOffset * stage);
	}

	private int getDotY(int y) {
		return y + stageHeightOffset*14/18;
	}

	@Override
	public void init() {

	}


	@Override
	protected String getHeaderString() {
		return "Dungeon wählen für Ebene " + (session.getCurrentStage()+1);
	}

}
