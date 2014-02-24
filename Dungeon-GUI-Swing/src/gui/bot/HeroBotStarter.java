package gui.bot;

import figure.DungeonVisibilityMap;
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
import ai.AI;
import dungeon.Dungeon;
import dungeon.generate.DungeonGenerationFailedException;

public class HeroBotStarter {

	public static int DungeonSizeX = 30;

	public static int DungeonSizeY = 40;

	public static void main(String[] args) {
		JDEnv.init();
		ImageManager imageManager = ImageManager
				.getInstance(new AWTImageLoader(null));
		imageManager.loadImages();
		Hero h = HeroUtil.getBasicHero(1, "BotStarterBot", Zodiac.Taurus,
				Profession.Nobleman);

		DungeonGame dungeonGame = DungeonGame.newInstance();
		dungeonGame.run();
		JDEnv.setGame(dungeonGame);

		Dungeon derDungeon = new Dungeon(DungeonSizeX, DungeonSizeY, 18, 39,
				dungeonGame);

		dungeonGame.setDungeon(derDungeon);

		h.setActualDungeon(derDungeon);
		DungeonVisibilityMap heroVisMap = h.getRoomVisibility();
		HeroInfo figureInfo = new HeroInfo(h, heroVisMap);


		try {
			dungeonGame.fillDungeon(derDungeon);
		} catch (DungeonGenerationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		h.move(derDungeon.getRoomNr(18, 39));

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
			MyJDGui gui = new MyJDGui(figureInfo);
			HeroControlWithSpectator control = new HeroControlWithSpectator(
					figureInfo, (AI) newBotInstance, gui);
			h.setControl(control);
			((AI) newBotInstance).setFigure(figureInfo);
			dungeonGame.putGuiFigure(h, gui);
			// new StartView(h.getName(), 0, null, false)
			gui.initGui(null, null, h.getName());

			Thread th = new Thread(dungeonGame);
			th.start();
		}

	}
}
