package gui.bot;

import figure.hero.Hero;
import figure.hero.HeroUtil;
import figure.hero.Profession;
import figure.hero.Zodiac;
import game.JDEnv;
import graphics.ImageManager;
import gui.engine2D.AWTImageLoader;
import dungeon.DungeonFactory;

public class HeroBotStarterSwing {


	public static void main(String[] args) {

		/*
		 * initialize resources
		 */
		JDEnv.init();
		ImageManager imageManager = ImageManager
				.getInstance(new AWTImageLoader(null));
		imageManager.loadImages();


		/*
		 * load Bot AI for hero
		 */
		/*
		 * Class<?> demoBotClass = null; try { demoBotClass =
		 * Class.forName("gui.bot.MyDemoBot"); } catch (ClassNotFoundException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * 
		 * Object newBotInstance = null; try { newBotInstance =
		 * demoBotClass.newInstance(); } catch (InstantiationException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IllegalAccessException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } /* /* init hero
		 */
			Hero h = HeroUtil.getBasicHero(1, "BotStarterBot", Zodiac.Taurus,
					Profession.Nobleman);

			DungeonFactory factory = new DefaultBotRunDungeonFactory();

			/*
			 * init GUI
			 */
			BotJDGUISwing gui = new BotJDGUISwing(factory);
			gui.startGame(h);

	}


}
