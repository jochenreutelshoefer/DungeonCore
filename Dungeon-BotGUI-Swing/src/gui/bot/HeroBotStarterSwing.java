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
import gui.engine2D.AWTImageLoader;
import ai.AI;
import dungeon.Dungeon;
import dungeon.DungeonManager;
import dungeon.JDPoint;
import dungeon.generate.DungeonGenerationFailedException;

public class HeroBotStarterSwing {

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


		try {
			dungeonGame.fillDungeon(derDungeon);
		} catch (DungeonGenerationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		if (newBotInstance instanceof AI) {
			/*
			 * init GUI
			 */
			BotJDGUISwing gui = new BotJDGUISwing();
			gui.setFigure(figureInfo);
			gui.initGui(h.getName());

			HeroControlWithSpectator control = new HeroControlWithSpectator(
					figureInfo, (AI) newBotInstance, gui);
			h.setControl(control);
			((AI) newBotInstance).setFigure(figureInfo);
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
