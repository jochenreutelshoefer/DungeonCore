package de.jdungeon.androidapp.gui.dungeonselection;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import animation.DefaultAnimationSet;
import animation.Motion;
import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import event.Event;
import event.EventListener;
import event.EventManager;
import figure.hero.Hero;
import graphics.ImageManager;
import level.DungeonFactory;
import level.DungeonManager;
import level.DungeonSelectedEvent;
import level.DungeonStartEvent;
import user.DungeonSession;
import util.JDDimension;

import de.jdungeon.androidapp.gui.AnimationGUIElement;
import de.jdungeon.androidapp.gui.DrawGUIElement;
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
public class DungeonSelectionScreen extends MenuScreen implements EventListener {

	public static final JDDimension HERO_DIMENSION = new JDDimension(140, 140);
	public static final int X_DISTANCE = 150;

	private final DungeonSession session;
	private final DungeonManager dungeonManager;
	private final int offset;
	private final AbstractImageLoader imageLoader;
	private final int stageHeightOffset;
	private final int xCenterValue;
	private final DungeonSelectionHeroFigure heroFigure;

	private int dotX;
	private int dotSize;
	private int[][] coordinatesX;
	private int iconIndex = 0;

	public DungeonSelectionScreen(Game game, DungeonSession session) {
		super(game);
		EventManager.getInstance().registerListener(this);
		this.session = session;
		dungeonManager = session.getDungeonManager();
		int currentStage = session.getCurrentStage();
		stageHeightOffset = 180;
		offset = game.getScreenHeight()*1/2 + (stageHeightOffset * currentStage);
		xCenterValue = game.getScreenWidth()/2;

		DefaultAnimationSet animationSet = ImageManager.getAnimationSet(Hero.HeroCategory.fromValue(session.getCurrentHero()
				.getHeroCode()), Motion.Walking, RouteInstruction.Direction.North);

		imageLoader = game.getFileIO().getImageLoader();
		for(int i = 0; i < dungeonManager.getNumberOfStages(); i++) {
			addGUIElementsForStage(i, dungeonManager.getDungeonOptions(i));
		}

		//guiElements.add(new SimpleImageGUIElement(new JDPoint(xCenterValue-heroDimension.getWidth()/2, getDotY(getY(i)) + dotSize - heroDimension.getHeight()*2/3 ), heroDimension, (Image)heroImage.getImage()));
		JDPoint heroSelectionPos = new JDPoint(xCenterValue - HERO_DIMENSION.getWidth() / 2, getDotY(getY(currentStage)) + dotSize - HERO_DIMENSION
				.getHeight() * 2 / 3);
		heroFigure = new DungeonSelectionHeroFigure(heroSelectionPos, HERO_DIMENSION, animationSet);
		guiElements.add(heroFigure);

		heroFigure.addTask(getHeroStartPoint(), heroSelectionPos, 4);
	}

	private JDPoint getHeroStartPoint() {
		int currentStage = session.getCurrentStage();
		if(currentStage == 0) {
			return new JDPoint(xCenterValue - HERO_DIMENSION.getWidth() / 2, getDotY(getY(currentStage)) + dotSize );
		}
		else {
			DungeonFactory lastCompleted = session.getLastCompleted();
			return getDungeonTileHeroPosition(currentStage-1, lastCompleted);
		}
	}

	private JDPoint getDungeonTileHeroPosition(int level, DungeonFactory dungeonFactory) {
		List<DungeonFactory> dungeonOptions = session.getDungeonManager().getDungeonOptions(level);
		int selectionIndex = dungeonOptions.indexOf(dungeonFactory);

		return new JDPoint(coordinatesX[dungeonOptions.size()-1][selectionIndex] - HERO_DIMENSION.getWidth() / 2, getY(level) - HERO_DIMENSION.getHeight() / 5);
	}

	private void addGUIElementsForStage(int stage, List<DungeonFactory> dungeonOptions) {

		coordinatesX = new int[][] {
				{ xCenterValue, -1, -1},
				{ xCenterValue - X_DISTANCE /2,  xCenterValue + X_DISTANCE /2, -1 },
				{ xCenterValue - X_DISTANCE, xCenterValue, xCenterValue + X_DISTANCE },
		};

		final int y = getY(stage);

		dotX = xCenterValue;
		final int dotY = getDotY(y);
		dotSize = 16;

		// we draw the track lines between the tiles also as GUIElements
		this.guiElements.add(new DrawGUIElement() {
			@Override
			public void paint(Graphics g, JDPoint viewportPosition) {
				g.fillOval(dotX - dotSize /2, dotY - dotSize /2, dotSize, dotSize, Colors.WHITE);
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

		/*
		For testing loaded sounds only

		List<AbstractAudioSet> allSounds = AudioEffectsManager.allSounds;
		for (AbstractAudioSet soundSet : allSounds) {
			List<Sound>  sounds  = soundSet.getAllSounds();
			for (Sound sound : sounds) {
				sound.play(1);
				Log.i("info", "Playing Sound: "+sound.getName()+" ("+sound.getId()+")");
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		*/
	}


	@Override
	protected String getHeaderString() {
		return "Dungeon wählen für Ebene " + (session.getCurrentStage()+1);
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Collection<Class<? extends Event>> result = new HashSet<>();
		result.add(DungeonSelectedEvent.class);
		return result;
	}

	@Override
	public void notify(Event event) {
		if(event instanceof  DungeonSelectedEvent) {
			DungeonSelectedEvent selectedEvent = (DungeonSelectedEvent) event;
			DungeonFactory dungeonFactory = selectedEvent.getDungeon();
			int currentStage = session.getCurrentStage();
			JDPoint dungeonTileHeroPosition = getDungeonTileHeroPosition(currentStage, dungeonFactory);
			heroFigure.addTask(heroFigure.getPositionOnScreen(), dungeonTileHeroPosition, 4, new DungeonStartEvent(selectedEvent));

		}
	}
}
