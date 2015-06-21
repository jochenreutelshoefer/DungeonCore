package gui.bot;

import figure.HeroControlWithSpectator;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.hero.HeroUtil;
import figure.hero.Profession;
import figure.hero.Zodiac;
import game.DungeonGame;
import game.JDEnv;
import graphics.ImageManager;
import gui.MyJDGui;
import gui.engine2D.AWTImageLoader;
import ai.GuiAI;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.generate.DungeonGenerationFailedException;
import dungeon.generate.SectorDungeonFiller1;
import dungeon.util.DungeonManager;

public class HeroBotStarter {

	public static int DungeonSizeX = 30;

	public static int DungeonSizeY = 40;

	public static void main(String[] args) {

		/*
		 * initialize resources
		 */
		JDEnv.init();
		ImageManager imageManager = ImageManager
				.getInstance(new AWTImageLoader(null));
		imageManager.loadImages();

		/*
		 * init hero
		 */
		Hero h = HeroUtil.getBasicHero(1, "BotStarterBot", Zodiac.Taurus,
				Profession.Nobleman);

		/*
		 * init game and dungeon
		 */
		DungeonGame dungeonGame = DungeonGame.getInstance();

		Dungeon derDungeon = new Dungeon(DungeonSizeX, DungeonSizeY, 18, 39,
				dungeonGame);
		dungeonGame.setDungeon(derDungeon);

		try {
			SectorDungeonFiller1 filler = new SectorDungeonFiller1(derDungeon,
					SectorDungeonFiller1.getValueForDungeon(1), dungeonGame, 1);
			filler.fillDungeon();
		} catch (DungeonGenerationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dungeonGame.init(derDungeon);

		HeroInfo figureInfo = DungeonManager.enterDungeon(h, derDungeon,
				new JDPoint(18, 39));
		/*
		 * load Bot AI for hero
		 */
		Class<?> demoBotClass = null;
		try {
			demoBotClass = Class.forName("gui.bot.MyDemoBot");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object newBotInstance = null;
		try {
			newBotInstance = demoBotClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (newBotInstance instanceof GuiAI) {
			/*
			 * init GUI
			 */
			MyJDGui gui = new MyJDGui(figureInfo);
			gui.initGui(null, null, h.getName());
			HeroControlWithSpectator control = new HeroControlWithSpectator(
					figureInfo, (GuiAI) newBotInstance, gui);
			h.setControl(control);
			((GuiAI) newBotInstance).setFigure(figureInfo);
			dungeonGame.putGuiFigure(h, gui);
			// new StartView(h.getName(), 0, null, false)

			/*
			 * start game
			 */
			Thread th = new Thread(dungeonGame);
			th.start();
		}

	}
}
