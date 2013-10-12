/*
 * Created on 25.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D;


import graphics.AbstractImageLoader;
import gui.StartView;
import gui.audio.AudioEffectsManager;
import gui.engine2D.animation.AnimationSet;
import gui.engine2D.animation.AnimationSetDirections;
import io.PictureLoadDialog;

import java.applet.Applet;
import java.awt.Image;
import java.awt.MediaTracker;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.Dir;

public class ImageManager {
	
	private static ImageManager instance;
	private AbstractImageLoader loader;
	
	public ImageManager(AbstractImageLoader loader2) {
		this.loader = loader2;
	}
	public static ImageManager getInstance(AbstractImageLoader loader) {
		if(instance == null) {
			instance = new ImageManager(loader);
		}
		return instance;
	}

	private static MediaTracker tracker;

	
	public static Image woodTextureImage;

	public static Image fieldImage;
	
	public static Image amulettImage;

	public static Image armorImage;

	public static Image axeImage;

	public static Image bear2Image;

	public static Image [] bearImage;

	public static Image bookImage;

	public static Image caveImage;

	public static Image chest_lockImage;

	public static Image chestImage;

	public static Image clubImage;

	public static Image cristall_blueImage;

	public static Image cristall_greenImage;

	public static Image cristall_redImage;

	public static Image cristall_yellowImage;

	public static Image cursor_clock;

	public static Image cursor_key_Image;

	public static Image cursor_key_not_Image;
	
	public static Image cursor_go_not_Image;
	public static Image cursor_wand;
	public static Image cursor_use_Image;

	public static Image cursor_scout;

	public static Image cursor_sword;

	public static Image dark_dwarfImage;
	
	public static Image finImage;

	public static Image darkMasterImage;

	public static Image dead_druidImage;

	public static Image dead_dwarfImage;

	public static Image dead_mageImage;

	public static Image dead_thiefImage;

	public static Image dead_warriorImage;

	public static Image deathImage;

	public static Image documentImage;

	public static Image door_east;

	public static Image door_east_lock;

	public static Image door_east_none;

	public static Image door_north;

	public static Image door_north_lock;

	public static Image door_north_none;

	public static Image door_south;

	public static Image door_south_lock;

	public static Image door_south_none;

	public static Image door_west;

	public static Image door_west_lock;

	public static Image door_west_none;

	public static AnimationSetDirections druid_been_hit;

	public static AnimationSetDirections druid_pause;

	public static AnimationSetDirections druid_running;

	public static AnimationSetDirections druid_slays;

	public static AnimationSetDirections druid_sorcering;

	public static AnimationSetDirections druid_tipping_over;

	public static AnimationSetDirections druid_using;

	public static AnimationSetDirections druid_walking;

	public static Image  [] druidImage;

	public static Image dummieImage;

	public static Image dustImage;

	public static Image engelImage;

	public static Image falltuerImage;

	public static Image floor_darkImage;

	public static Image floor_darkImage2;

	public static Image floor_darkImage3;

	public static Image floor_darkImage4;

	public static Image floor_darkImage5;

	public static Image floor_darkImage6;

	public static Image floor_darkImage7;

	public static Image floor_mediumImage;

	public static Image floor_mediumImage2;

	public static Image floor_mediumImage3;

	public static Image floor_mediumImage4;

	public static Image floor_mediumImage5;

	public static Image floor_mediumImage6;

	public static Image floor_mediumImage7;

	public static Image floorImage;

	public static Image[] floorImage_darkArray = new Image[8];

	public static Image[] floorImage_mediumArray = new Image[8];

	public static Image floorImage2;

	public static Image floorImage3;

	public static Image floorImage4;

	public static Image floorImage5;

	public static Image floorImage6;

	public static Image floorImage7;

	public static Image[] floorImageArray = new Image[8];

	public static Image fountainImage;

	public static AnimationSetDirections ghul1_been_hit;

	public static AnimationSetDirections ghul1_pause = null;

	public static AnimationSetDirections ghul1_running = null;

	public static AnimationSetDirections ghul1_slays;

	public static AnimationSetDirections ghul1_sorcering = null;

	public static AnimationSetDirections ghul1_tipping_over;

	public static AnimationSetDirections ghul1_using = null;

	public static AnimationSetDirections ghul1_walking = null;

	public static Image  [] ghulImage;

	public static Image graveImage;

	public static Image hand_greift1_Image;

	public static Image hand_zeigt1_Image;

	public static Image cursor_go_Image;
	
	public static Image helmetImage;

	public static Image keyImage;

	public static Image kugelImage;

	public static Image lanceImage;

	public static Image luzia_ball_greyImage;

	public static Image luzia_ball_redImage;

	public static Image luzia_hutImage;

	public static Image luziaImage;

	public static AnimationSetDirections mage_been_hit;

	public static AnimationSetDirections mage_pause;

	public static AnimationSetDirections mage_running;

	public static AnimationSetDirections mage_slays;

	public static AnimationSetDirections mage_sorcering;

	public static AnimationSetDirections mage_tipping_over;

	public static AnimationSetDirections mage_using;

	public static AnimationSetDirections mage_walking;

	public static Image []  mageImage;

	public static AnimationSetDirections ogre1_been_hit;

	public static AnimationSetDirections ogre1_pause = null;

	public static AnimationSetDirections ogre1_running = null;

	public static AnimationSetDirections ogre1_slays;

	public static AnimationSetDirections ogre1_sorcering = null;

	public static AnimationSetDirections ogre1_tipping_over;

	public static AnimationSetDirections ogre1_using = null;

	public static AnimationSetDirections ogre1_walking = null;

	public static AnimationSetDirections spider1_been_hit;

	public static AnimationSetDirections spider1_pause = null;

	public static AnimationSetDirections spider1_running = null;

	public static AnimationSetDirections spider1_slays;

	public static AnimationSetDirections spider1_sorcering = null;

	public static AnimationSetDirections spider1_tipping_over;

	public static AnimationSetDirections spider1_using = null;

	public static AnimationSetDirections spider1_walking = null;

	public static AnimationSetDirections orc1_been_hit;

	public static AnimationSetDirections orc1_pause = null;

	public static AnimationSetDirections orc1_running = null;

	public static AnimationSetDirections orc1_slays;

	public static AnimationSetDirections orc1_sorcering = null;

	public static AnimationSetDirections orc1_tipping_over;

	public static AnimationSetDirections orc1_using = null;

	public static AnimationSetDirections orc1_walking = null;

	public static Image  [] ogreImage;

	public static Image  [] orcImage;

	public static Image pentagrammImage;

	public static Image potion_blueImage;

	public static Image potion_redImage;
	
	public static Image potion_greenImage;
	
	public static Image featherImage;

	public static AnimationSet puff;

	public static Image questionmark;

	public static Image repairImage;

	public static Image rune_blueImage;

	public static Image rune_greenImage;

	public static Image rune_redImage;

	public static Image rune_yellowImage;

	public static Image scrollImage;

	public static Image shieldImage;

	public static Image shrine_blackImage;

	public static Image shrine_blueImage;

	public static Image shrine_greenImage;

	public static Image shrine_lilaImage;

	public static Image shrine_redImage;

	public static Image shrine_small_blueImage;

	public static Image shrine_small_greenImage;

	public static Image shrine_small_redImage;

	public static Image shrine_small_yellowImage;

	public static Image shrine_turkisImage;

	public static Image shrine_whiteImage;

	public static Image shrine_yellowImage;

	public static AnimationSetDirections skel1_been_hit;

	public static AnimationSetDirections skel1_pause = null;

	public static AnimationSetDirections skel1_running = null;

	public static AnimationSetDirections skel1_slays;

	public static AnimationSetDirections skel1_sorcering = null;

	public static AnimationSetDirections skel1_tipping_over;

	public static AnimationSetDirections skel1_using = null;

	public static AnimationSetDirections skel1_walking = null;

	public static Image skel2Image;

	public static Image []  skelImage;

	public static Image sorcLabImage;

	public static Image spotImage;

	public static Image statueImage;

	public static Image swordImage;

	public static AnimationSetDirections thief_been_hit;

	public static AnimationSetDirections thief_pause;

	public static AnimationSetDirections thief_running;

	public static AnimationSetDirections thief_slays;

	public static AnimationSetDirections thief_sorcering;

	public static AnimationSetDirections thief_tipping_over;

	public static AnimationSetDirections thief_using;

	public static AnimationSetDirections thief_walking;

	public static Image  [] thiefImage;

	public static Image traderImage;

	public static Image wall_southImage;

	//public static Image wallImage;

	public static Image wall_sidesImage;
	public static Image wall_northImage;
	
	public static AnimationSetDirections warrior_been_hit;

	public static AnimationSetDirections warrior_pause;

	public static AnimationSetDirections warrior_running;

	public static AnimationSetDirections warrior_slays;

	public static AnimationSetDirections warrior_sorcering;

	public static AnimationSetDirections warrior_tipping_over;

	public static AnimationSetDirections warrior_using;

	public static AnimationSetDirections warrior_walking;

	public static Image[] warriorImage;

	public static AnimationSetDirections wolf1_been_hit;

	public static AnimationSetDirections wolf1_pause = null;

	public static AnimationSetDirections wolf1_running = null;

	public static AnimationSetDirections wolf1_slays;

	public static AnimationSetDirections wolf1_sorcering = null;

	public static AnimationSetDirections wolf1_tipping_over;

	public static AnimationSetDirections wolf1_using = null;

	public static AnimationSetDirections wolf1_walking = null;

	public static Image wolf2Image;

	public static Image  [] wolfImage;

	public static Image wolfknifeImage;

	public static Image xmasImage;

	private static AnimationSetDirections load4Animations(Applet a, String path,
			String pattern, int cnt) {
		System.gc();
		AnimationSet[] set = new AnimationSet[4];
		for (int i = 0; i < 4; i++) {
			set[i] = new AnimationSet(loadArray(a, path, pattern, i + 1, cnt),
					getArray(35, cnt));
		}
		return new AnimationSetDirections(set);
	}

	private static PictureLoadDialog loadDialog;
	public static boolean imagesLoaded = false;
	
	public static void loadImages(Applet a, final StartView board) {
		
		if(!imagesLoaded) {
		
		   
		AWTImageLoader.dialog = loadDialog;
		tracker = new MediaTracker(board);
		AWTImageLoader.setTracker(tracker);

		puff = new AnimationSet(loadArray(a, "wolke", 8), getArray(25, 8));

		warrior_slays = load4Animations(a, "animation/warrior/", "warrior_attack_",
				13);
		thief_slays = load4Animations(a, "animation/thief/", "thief_attack_", 13);
		druid_slays = load4Animations(a, "animation/druid/", "druid_attack_", 13);
		mage_slays = load4Animations(a, "animation/mage/", "mage_attack_", 8);

		warrior_been_hit = load4Animations(a, "animation/warrior/","warrior_swordstan_treffer_", 7);
		warrior_been_hit.addAudioClipHalfTime(AudioEffectsManager.HERO_HURT);
		warrior_been_hit.addAudioClip(AudioEffectsManager.SMASH,1);
		
		
		thief_been_hit = load4Animations(a, "animation/thief/", "thief_treffer_", 7);
		thief_been_hit.addAudioClipHalfTime(AudioEffectsManager.HERO_HURT);
		thief_been_hit.addAudioClip(AudioEffectsManager.SMASH,1);
		
		
		druid_been_hit = load4Animations(a, "animation/druid/", "druid_been_hit_",9);
		druid_been_hit.addAudioClipHalfTime(AudioEffectsManager.HERO_HURT);
		druid_been_hit.addAudioClip(AudioEffectsManager.SMASH,1);
		
		
		mage_been_hit = load4Animations(a, "animation/mage/", "mage_treffer_", 9);
		mage_been_hit.addAudioClipHalfTime(AudioEffectsManager.HERO_HURT);
		mage_been_hit.addAudioClip(AudioEffectsManager.SMASH,1);
		
		
		warrior_tipping_over = load4Animations(a, "animation/warrior/",
				"warrior_swordstan_kippt_um_", 9);
		thief_tipping_over = load4Animations(a, "animation/thief/",
				"thief_kippt_um_", 11);
		druid_tipping_over = load4Animations(a, "animation/druid/",
				"druid_tipping_over_", 13);
		mage_tipping_over = load4Animations(a, "animation/mage/",
				"mage_magier_45_kippt_um_", 10);

		warrior_walking = load4Animations(a, "animation/warrior/",
				"warrior_swordstan_laeuft_", 9);
		thief_walking = load4Animations(a, "animation/thief/", "thief_laeuft_", 9);
		druid_walking = load4Animations(a, "animation/druid/", "druid_walking_", 8);
		mage_walking = load4Animations(a, "animation/mage/", "mage_laeuft_", 8);

		warrior_using = load4Animations(a, "animation/warrior/",
				"warrior_swordstan_spricht_", 7);
		thief_using = load4Animations(a, "animation/thief/", "thief_laeuft_", 9);
		
		druid_using = load4Animations(a, "animation/druid/", "druid_talking_", 8);
		mage_using = load4Animations(a, "animation/mage/", "mage_talking_", 9);

		warrior_pause = load4Animations(a, "animation/warrior/",
				"warrior_stan_strickt_", 7);
		thief_pause = load4Animations(a, "animation/thief/", "thief_laeuft_", 9);
		
		druid_pause = load4Animations(a, "animation/druid/", "druid_paused_", 8);
		mage_pause = load4Animations(a, "animation/mage/", "mage_liest_", 9);

		warrior_sorcering = load4Animations(a, "animation/warrior/",
				"warrior_swordstan_spricht_", 7);
		thief_sorcering = load4Animations(a, "animation/thief/", "thief_laeuft_", 9);
		
		druid_sorcering = load4Animations(a, "animation/druid/",
				"druid_magic_spelling_", 13);
		mage_sorcering = load4Animations(a, "animation/mage/",
				"mage_magieattack_", 9);

		warrior_running = load4Animations(a, "animation/" + "warrior/",
				"warrior_swordstan_rennt_", 8);
		thief_running = load4Animations(a, "animation/thief/", "thief_rennt_", 9);
		druid_running = load4Animations(a, "animation/" + "druid/",
				"druid_running_", 8);
		mage_running = load4Animations(a, "animation/" + "mage/",
				"mage_magieattack_", 8);


		wolf1_been_hit = load4Animations(a, "animation/" + "wolf/",
				"wolf_been_hit_", 9);
		wolf1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
		wolf1_been_hit.addAudioClip(AudioEffectsManager.SMASH,1);
		
		
		wolf1_tipping_over = load4Animations(a, "animation/" + "wolf/",
				"wolf_tipping_over_", 9);
		wolf1_tipping_over.addAudioClip(AudioEffectsManager.WOLF_DIES,1);
		
		wolf1_slays = load4Animations(a, "animation/" + "wolf/",
				"wolf_wolf_attack_", 10);
		wolf1_slays.addAudioClip(AudioEffectsManager.WOLF_ATTACKS,1);
		
		wolf1_walking = load4Animations(a, "animation/" + "wolf/",
				"wolf_rennt_", 10);
		wolf1_running = load4Animations(a, "animation/" + "wolf/",
				"wolf_laeuft_", 10);
		
		wolf1_pause = wolf1_walking;

		skel1_been_hit = load4Animations(a, "animation/" + "skel/",
				"skel_swordskel_treffer_", 7);
		skel1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
		skel1_been_hit.addAudioClip(AudioEffectsManager.SMASH,1);
		
		skel1_tipping_over = load4Animations(a, "animation/" + "skel/",
				"skel_zerfaellt_", 9);
		skel1_slays = load4Animations(a, "animation/" + "skel/",
				"skel_swordskel_attack_", 11);
		skel1_walking = load4Animations(a, "animation/" + "skel/",
		"skel_swordskel_laeuft_", 11);
		skel1_running = load4Animations(a, "animation/" + "skel/",
		"skel_swordskel_rennt_", 11);
		
		skel1_pause = skel1_walking;


		ghul1_been_hit = load4Animations(a, "animation/" + "ghul/",
				"ghul_treffer_", 7);
		ghul1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
		ghul1_been_hit.addAudioClip(AudioEffectsManager.SMASH,1);
		
		
		
		ghul1_tipping_over = load4Animations(a, "animation/" + "ghul/",
				"ghul_mumie_zerfaellt_", 9);
		ghul1_slays = load4Animations(a, "animation/" + "ghul/",
				"ghul_mummy_45_attack_", 10);
		
		ghul1_running = load4Animations(a, "animation/" + "ghul/",
				"ghul_mummy_rennt_", 10);
		ghul1_walking = load4Animations(a, "animation/" + "ghul/",
				"ghul_mummy_45_laeuft_", 10);
		ghul1_pause = ghul1_walking;

		ogre1_been_hit = load4Animations(a, "animation/" + "ogre/",
				"ogre_been_hit_", 7);
		ogre1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
		ogre1_been_hit.addAudioClip(AudioEffectsManager.SMASH,1);
		
		
		ogre1_tipping_over = load4Animations(a, "animation/" + "ogre/",
				"ogre_tipping_over_", 9);
		ogre1_slays = load4Animations(a, "animation/" + "ogre/", "ogre_attack_",
				11);
		ogre1_running = load4Animations(a, "animation/" + "ogre/", "ogre_running_",
				11);
		ogre1_walking = load4Animations(a, "animation/" + "ogre/", "ogre_walking_",
				11);
		ogre1_pause = ogre1_walking;

		orc1_been_hit = load4Animations(a, "animation/" + "orc/",
				"orc_been_hit_", 7);
		orc1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
		orc1_been_hit.addAudioClip(AudioEffectsManager.SMASH,1);
		
		orc1_tipping_over = load4Animations(a, "animation/" + "orc/",
				"orc_tipping_over_", 9);
		orc1_slays = load4Animations(a, "animation/" + "orc/", "orc_attack_", 11);
		orc1_walking = load4Animations(a, "animation/" + "orc/", "orc_walking_", 11);
		orc1_running = load4Animations(a, "animation/" + "orc/", "orc_running_", 11);
		orc1_pause = orc1_walking;

		spider1_been_hit = load4Animations(a, "animation/" + "spider/",
				"spider_been_hit_", 7);
		spider1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
		spider1_been_hit.addAudioClip(AudioEffectsManager.SMASH,1);
		
		
		spider1_tipping_over = load4Animations(a, "animation/" + "spider/",
				"spider_tipping_over_", 9);
		spider1_tipping_over.addAudioClip(AudioEffectsManager.SPIDER_DIES,0);
		
		spider1_slays = load4Animations(a, "animation/" + "spider/",
				"spider_attack_", 11);
		spider1_slays.addAudioClip(AudioEffectsManager.SPIDER_ATTACKS,0);
		
		
		spider1_walking = load4Animations(a, "animation/" + "spider/",
				"spider_walking_", 11);
		spider1_running = spider1_walking;
		spider1_pause = spider1_walking;
		
		
		warriorImage = makePics(warrior_walking);
		thiefImage = makePics(thief_walking);
		druidImage = makePics(druid_walking);
		mageImage = makePics(mage_walking);
		wolfImage = makePics(wolf1_walking);
		orcImage = makePics(orc1_walking);
		ghulImage = makePics(ghul1_walking);
		skelImage = makePics(skel1_walking);
		ogreImage = makePics(ogre1_walking);
		bearImage = makePics(spider1_walking);
		
		
		dead_dwarfImage = AWTImageLoader.loadImage(a, "dead_dwarf.gif");
		dead_warriorImage = AWTImageLoader.loadImage(a, "dead_stan.gif");
		dead_thiefImage = AWTImageLoader.loadImage(a, "dead_blue_pirate.gif");
		dead_druidImage = AWTImageLoader.loadImage(a, "dead_white_mage.gif");
		dead_mageImage = AWTImageLoader.loadImage(a, "dead_black_mage.gif");

		chestImage = AWTImageLoader.loadImage(a, "chest_cut_trans.gif");
		floorImage = AWTImageLoader.loadImage(a, "boden5.gif");
		floor_darkImage = AWTImageLoader.loadImage(a, "boden5_dark.gif");
		floor_mediumImage = AWTImageLoader.loadImage(a, "boden5_dark1.gif");

		floorImage2 = AWTImageLoader.loadImage(a, "boden3.gif");
		floor_darkImage2 = AWTImageLoader.loadImage(a, "boden3_dark.gif");
		floor_mediumImage2 = AWTImageLoader.loadImage(a, "boden3_dark1.gif");

		floorImage3 = AWTImageLoader.loadImage(a, "boden4.gif");
		floor_darkImage3 = AWTImageLoader.loadImage(a, "boden4_dark.gif");
		floor_mediumImage3 = AWTImageLoader.loadImage(a, "boden4_dark1.gif");

		floorImage4 = AWTImageLoader.loadImage(a, "boden6.gif");
		floor_darkImage4 = AWTImageLoader.loadImage(a, "boden6_dark.gif");
		floor_mediumImage4 = AWTImageLoader.loadImage(a, "boden6_dark1.gif");

		floorImage5 = AWTImageLoader.loadImage(a, "boden7.gif");
		floor_darkImage5 = AWTImageLoader.loadImage(a, "boden7_dark.gif");
		floor_mediumImage5 = AWTImageLoader.loadImage(a, "boden7_dark1.gif");

		floorImage6 = AWTImageLoader.loadImage(a, "boden8.gif");
		floor_darkImage6 = AWTImageLoader.loadImage(a, "boden8_dark.gif");
		floor_mediumImage6 = AWTImageLoader.loadImage(a, "boden8_dark1.gif");

		floorImage7 = AWTImageLoader.loadImage(a, "boden9.gif");
		floor_darkImage7 = AWTImageLoader.loadImage(a, "boden9_dark.gif");
		floor_mediumImage7 = AWTImageLoader.loadImage(a, "boden9_dark1.gif");

		makeFloorArrays();

		wall_sidesImage = AWTImageLoader.loadImage(a, "wand_seiten.gif");
		wall_northImage = AWTImageLoader.loadImage(a, "wand_nord.gif");
		wall_southImage = AWTImageLoader.loadImage(a, "wall_south.gif");

		door_north = AWTImageLoader.loadImage(a, "tuer_nord.gif");

		door_north_none = AWTImageLoader.loadImage(a, "tuer_nord_keine.gif");

		door_east = AWTImageLoader.loadImage(a, "tuer_ost.gif");

		door_east_none = AWTImageLoader.loadImage(a, "tuer_ost_keine.gif");

		door_west = AWTImageLoader.loadImage(a, "tuer_west.gif");

		door_west_none = AWTImageLoader.loadImage(a, "tuer_west_keine.gif");
		door_south_none = AWTImageLoader.loadImage(a, "tuer_sued_keine.gif");
		door_south = AWTImageLoader.loadImage(a, "tuer_sued.gif");
		door_south_lock = AWTImageLoader.loadImage(a, "tuer_sued_schloss.gif");

		axeImage = AWTImageLoader.loadImage(a, "axt.gif");
		swordImage = AWTImageLoader.loadImage(a, "schwert.gif");
		lanceImage = AWTImageLoader.loadImage(a, "lanze.gif");
		wolfknifeImage = AWTImageLoader.loadImage(a, "wolfsmesser.gif");
		clubImage = AWTImageLoader.loadImage(a, "knueppel.gif");
		scrollImage = AWTImageLoader.loadImage(a, "scroll_blue.gif");
		documentImage = AWTImageLoader.loadImage(a, "scroll_white.gif");
		potion_redImage = AWTImageLoader.loadImage(a, "potion_rot.gif");
		potion_blueImage = AWTImageLoader.loadImage(a, "potion_blue.gif");
		dustImage = AWTImageLoader.loadImage(a, "dust.gif");
		armorImage = AWTImageLoader.loadImage(a, "ruestung.gif");
		shieldImage = AWTImageLoader.loadImage(a, "shield2.gif");
		helmetImage = AWTImageLoader.loadImage(a, "helm.gif");
		bookImage = AWTImageLoader.loadImage(a, "book_yellow.gif");
		keyImage = AWTImageLoader.loadImage(a, "key.gif");
		falltuerImage = AWTImageLoader.loadImage(a, "falltuer.gif");
		engelImage = AWTImageLoader.loadImage(a, "engel2.gif");
		shrine_blueImage = AWTImageLoader.loadImage(a, "shrine_blue.gif");
		shrine_redImage = AWTImageLoader.loadImage(a, "shrine_red.gif");
		shrine_greenImage = AWTImageLoader.loadImage(a, "shrine_green.gif");
		shrine_yellowImage = AWTImageLoader.loadImage(a, "shrine_yellow.gif");
		shrine_whiteImage = AWTImageLoader.loadImage(a, "shrine_white.gif");
		shrine_blackImage = AWTImageLoader.loadImage(a, "shrine_black.gif");
		shrine_lilaImage = AWTImageLoader.loadImage(a, "shrine_lila.gif");
		shrine_turkisImage = AWTImageLoader.loadImage(a, "shrine_tuerkis.gif");
		shrine_small_redImage = AWTImageLoader.loadImage(a,
				"shrine_small_red.gif");
		shrine_small_blueImage = AWTImageLoader.loadImage(a,
				"shrine_small_blue.gif");
		shrine_small_yellowImage = AWTImageLoader.loadImage(a,
				"shrine_small_yellow.gif");
		shrine_small_greenImage = AWTImageLoader.loadImage(a,
				"shrine_small_green.gif");
		sorcLabImage = AWTImageLoader.loadImage(a, "zauberlabor1.gif");
		amulettImage = AWTImageLoader.loadImage(a, "amulett.gif");

		repairImage = AWTImageLoader.loadImage(a, "amboss.gif");
		fountainImage = AWTImageLoader.loadImage(a, "fountain.gif");
		statueImage = AWTImageLoader.loadImage(a, "statue.gif");
		chest_lockImage = AWTImageLoader.loadImage(a, "chest_schloss.gif");
		door_north_lock = AWTImageLoader.loadImage(a, "tuer_nord_schloss.gif");
		door_east_lock = AWTImageLoader.loadImage(a, "tuer_ost_schloss.gif");
		door_west_lock = AWTImageLoader.loadImage(a, "tuer_west_schloss.gif");
		graveImage = AWTImageLoader.loadImage(a, "grave.gif");
		caveImage = AWTImageLoader.loadImage(a, "cave.gif");
		traderImage = AWTImageLoader.loadImage(a, "haendler2.gif");
		rune_redImage = AWTImageLoader.loadImage(a, "rune_red.gif");
		rune_greenImage = AWTImageLoader.loadImage(a, "rune_green.gif");
		rune_blueImage = AWTImageLoader.loadImage(a, "rune_blue.gif");
		rune_yellowImage = AWTImageLoader.loadImage(a, "rune_yellow.gif");
		cristall_redImage = AWTImageLoader.loadImage(a, "kristall_rot.gif");
		cristall_greenImage = AWTImageLoader.loadImage(a, "kristall_gruen.gif");
		cristall_blueImage = AWTImageLoader.loadImage(a, "kristall_blau.gif");
		cristall_yellowImage = AWTImageLoader.loadImage(a, "kristall_gelb.gif");
		spotImage = AWTImageLoader.loadImage(a, "versteck.gif");
		hand_zeigt1_Image = AWTImageLoader.loadImage(a, "zeigerhand-zeigt1.gif");
		cursor_go_Image = AWTImageLoader.loadImage(a, "zeigerhand_go.gif");
		hand_greift1_Image = AWTImageLoader.loadImage(a,
				"zeigerhand-greift1.gif");
		cursor_key_Image = AWTImageLoader.loadImage(a, "zeiger_key.gif");
		cursor_key_not_Image = AWTImageLoader.loadImage(a,
				"zeiger_key_nicht.gif");
		cursor_sword = AWTImageLoader.loadImage(a, "zeigerhand-schwert.gif");
		cursor_clock = AWTImageLoader.loadImage(a, "zeigerhand-sanduhr.gif");
		cursor_scout = AWTImageLoader.loadImage(a, "zeigerhand-go_scout.gif");
		pentagrammImage = AWTImageLoader.loadImage(a, "pentagramm.gif");
		darkMasterImage = AWTImageLoader.loadImage(a, "mister_death.gif");
		luziaImage = AWTImageLoader.loadImage(a, "luzia.gif");
		luzia_hutImage = AWTImageLoader.loadImage(a, "luzia_hut.gif");
		kugelImage = AWTImageLoader.loadImage(a, "kugel.gif");
		questionmark = AWTImageLoader.loadImage(a, "fragezeichen.gif");
		xmasImage = AWTImageLoader.loadImage(a, "xmas.gif");
		dark_dwarfImage = AWTImageLoader.loadImage(a, "dark_dwarf.gif");
		finImage = AWTImageLoader.loadImage(a, "growing n0007c.gif");
		luzia_ball_greyImage = AWTImageLoader.loadImage(a, "kugel_grau.gif");

		luzia_ball_redImage = AWTImageLoader.loadImage(a, "kugel_rot.gif");

		fieldImage  = AWTImageLoader.loadImage(a, "field3.gif");
		woodTextureImage =  AWTImageLoader.loadImage(a, "theWood3.gif");
		
		cursor_go_not_Image =  AWTImageLoader.loadImage(a, "zeigerhand_go_not.gif");
		cursor_wand =  AWTImageLoader.loadImage(a, "zeiger_zauberstab.gif");
		cursor_use_Image = AWTImageLoader.loadImage(a, "zeigerhand_faust.gif");
		featherImage = AWTImageLoader.loadImage(a, "feder.gif");
		potion_greenImage = AWTImageLoader.loadImage(a, "potion_green.gif");

		}
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(loadDialog!= null) {
		loadDialog.setVisible(false);
		loadDialog.dispose();
		}
		board.imagesLoaded();
		imagesLoaded = true;
	}
	
	private static Image[] makePics(AnimationSetDirections a) {
		AnimationSet[] sets = a.getAnimations();
		Image ims [] = new Image[sets.length];
		for (int i = 0; i < sets.length; i++) {
			ims[i] = sets[i].getImagesNr(0);
		}
		return ims;
	}

	
	//static int[][] bgGIF = { {4, 2, 4}};
	//static BufferedImageOp opGIF = new LookupOp(new MyLookUpTable(bgGIF,4), null);
	//static Graphics g2;
	
//	private static Image performCutOperationsGIF(Image element,String fileName) {
//
//		
//		
//		BufferedImage mBufferedImage = new BufferedImage(
//		element.getWidth(null), element.getHeight(null),
//		BufferedImage.TYPE_INT_ARGB);
//		g2 = mBufferedImage.createGraphics();
//		g2.drawImage(element, 0, 0, null);
//
//		//Image im = bild.createImage(mBufferedImage.getWidth(), mBufferedImage.getHeight());
//		opGIF.filter(mBufferedImage, mBufferedImage);
//		OutputStream output = null;
//		//D:\workspaces\workspaceJD\java_dungeon\pics
//		String dest  = JDImageLoader.LOCAL_PICTURE_PATH+fileName+".gif";
//		System.out.println("writing out : "+dest);
//		try {
//			output = new BufferedOutputStream(
//					new FileOutputStream(dest
//							));
//			
//		} catch (Exception e) {
//			System.out.println("filenotfound!");
//		}
//		BMP_Writer bmpwriter = new BMP_Writer();
//	
//		Gif89Encoder encode  = null;
//		try {
//			
//			encode = new Gif89Encoder(mBufferedImage);
//			
//			encode.encode(output);
//			output.close();
//			System.out.println("ready!");
//		} catch (Exception e) {
//			System.out.println("encode error :");
//			e.printStackTrace();
//			System.out.println(e.toString());
//		}
//		//System.exit(0);
//		return mBufferedImage;
//
//	}
//	//static int[][] bgPNG = { {97, 68, 43}};
//	//static int[][] bgPNG = { {111, 79, 51}};
//	//static int[][] bgPNG = { {106, 76 ,48}};
//	static int[][] bgPNG = { {105, 74, 46}};
//	//static int[][] bgPNG = { {110 ,80, 52}};
//	static BufferedImageOp opPNG = new LookupOp(new MyLookUpTable(bgPNG,0), null);
//	
//	
//	private static void performCutOperationsPNG(Image element,String fileName) {
//
//		BufferedImage mBufferedImage = new BufferedImage(
//		element.getWidth(null), element.getHeight(null),
//		BufferedImage.TYPE_INT_ARGB);
//		g2 = mBufferedImage.createGraphics();
//		g2.drawImage(element, 0, 0, null);
//
//		opPNG.filter(mBufferedImage, mBufferedImage);
//		String dest  = JDImageLoader.LOCAL_PICTURE_PATH+fileName+"_trans.PNG";
//		System.out.println("writing out : "+dest);
//			
//		try {
//				ImageIO.write(mBufferedImage, "png", new File(dest));
//			System.out.println("ready!");
//		} catch (Exception e) {
//			System.out.println("encode error :");
//			e.printStackTrace();
//			System.out.println(e.toString());
//		}
//
//	}
	

	private static Image[] loadArray(Applet a, String path, int cnt) {
		Image[] ims = new Image[cnt];
		for (int i = 0; i < cnt; i++) {

			Image im = (AWTImageLoader.loadImage(a, path + Integer.toString(i)
					+ ".GIF"));

			ims[i] = im;
			if (ims[i] == null) {

				System.out.println("Bild nicht geladen: " + path
						+ Integer.toString(i) + ".GIF");
				return null;
			}
		}

		return ims;
	}

	private static Image[] loadArray(Applet a, String path,
			String fileNamePrefix, int dir, int cnt) {

		List<Image> imageList = new LinkedList<Image>();
		String dirChar = "";
		if (dir == Dir.EAST) {
			dirChar = "e";
		}
		if (dir == Dir.WEST) {
			dirChar = "w";
		}
		if (dir == Dir.NORTH) {
			dirChar = "n";
		}
		if (dir == Dir.SOUTH) {
			dirChar = "s";
		}
		int i = 0;

		String numberStr = Integer.toString(i);
		String suffix = "";
		for (int j = 0; j < 4 - numberStr.length(); j++) {
			suffix += "0";
		}
		suffix += numberStr;
		
		while (i < 15) {
			
			Image im = (AWTImageLoader.loadImage(a,  path + fileNamePrefix + dirChar + suffix+"_trans.GIF"));
			if(im == null || im.getHeight(null) == -1) {
				//System.out.println("breaking loadAni bei: "+i +" - "+f);
				break;
			}
			//loadDialog.setPicName(fileNamePrefix+dirChar+suffix);
			//tracker.addImage(im, 1);
			
			//System.out.println("lade: "+f.toString());
//			Image im = null;
//			try {
//				im = ImageIO.read(f);
//			} catch (Exception e) {
//				System.out.println("laden gescheiter: "+f.toString());
//				// TODO: handle exception
//			}
			
			imageList.add(im);
			
			
//			tracker.addImage(im, 1);
//			try {
//				tracker.waitForAll();
//			} catch (Exception e) {
//				System.out.println("tracker ERROR!");
//			}
			//performCutOperationsGIF(im,fileName.substring(0,fileName.length()-4));
			
			i++;

			numberStr = Integer.toString(i);
			suffix = "";
			for (int j = 0; j < 4 - numberStr.length(); j++) {
				suffix += "0";
			}
			suffix += numberStr;
		}
		

		//System.out.println("Bild nicht gefunden: " + f.toString());
		Image[] ims = new Image[imageList.size()];
		int k = 0;
		for (Iterator<Image> iter = imageList.iterator(); iter.hasNext();) {
			Image element = iter.next();
			ims[k] = element;
			k++;
		}

		return ims;
	}




	public static int[] getArray(int value, int cnt) {
		int[] a = new int[cnt];
		for (int i = 0; i < cnt; i++) {
			a[i] = value;
		}
		return a;
	}

	public static int[] getDelayArray(int value, int cnt) {
		int[] a = new int[cnt];
		for (int i = 0; i < cnt - 1; i++) {
			a[i] = value;
		}
		a[cnt - 1] = 120;
		return a;
	}

	private static void makeFloorArrays() {
		floorImageArray[0] = floorImage;
		floorImageArray[1] = floorImage2;
		floorImageArray[2] = floorImage3;
		floorImageArray[3] = floorImage4;
		floorImageArray[4] = floorImage5;
		floorImageArray[5] = floorImage6;
		floorImageArray[6] = floorImage7;
		floorImageArray[7] = null;

		floorImage_mediumArray[0] = floor_mediumImage;
		floorImage_mediumArray[1] = floor_mediumImage2;
		floorImage_mediumArray[2] = floor_mediumImage3;
		floorImage_mediumArray[3] = floor_mediumImage4;
		floorImage_mediumArray[4] = floor_mediumImage5;
		floorImage_mediumArray[5] = floor_mediumImage6;
		floorImage_mediumArray[6] = floor_mediumImage7;
		floorImage_mediumArray[7] = null;

		floorImage_darkArray[0] = floor_darkImage;
		floorImage_darkArray[1] = floor_darkImage2;
		floorImage_darkArray[2] = floor_darkImage3;
		floorImage_darkArray[3] = floor_darkImage4;
		floorImage_darkArray[4] = floor_darkImage5;
		floorImage_darkArray[5] = floor_darkImage6;
		floorImage_darkArray[6] = floor_darkImage7;
		floorImage_darkArray[7] = null;

	}

	/**
	 * @return Returns the druid_been_hit.
	 * 
	 */
	public static AnimationSet getDruid_been_hit(int dir) {
		return ImageManager.druid_been_hit.get(dir - 1);
	}

	/**
	 * @return Returns the druid_pause.
	 * 
	 */
	public static AnimationSet getDruid_pause(int dir) {
		return ImageManager.druid_pause.get(dir - 1);
	}

	/**
	 * @return Returns the druid_running.
	 * 
	 */
	public static AnimationSet getDruid_running(int dir) {
		return ImageManager.druid_running.get(dir - 1);
	}

	/**
	 * @return Returns the druid_slays.
	 * 
	 */
	public static AnimationSet getDruid_slays(int dir) {
		return ImageManager.druid_slays.get(dir - 1);
	}

	/**
	 * @return Returns the druid_sorcering.
	 * 
	 */
	public static AnimationSet getDruid_sorcering(int dir) {
		return ImageManager.druid_sorcering.get(dir - 1);
	}

	/**
	 * @return Returns the druid_tipping_over.
	 * 
	 */
	public static AnimationSet getDruid_tipping_over(int dir) {
		return ImageManager.druid_tipping_over.get(dir - 1);
	}

	/**
	 * @return Returns the druid_using.
	 * 
	 */
	public static AnimationSet getDruid_using(int dir) {
		return ImageManager.druid_using.get(dir - 1);
	}

	/**
	 * @return Returns the druid_walking.
	 * 
	 */
	public static AnimationSet getDruid_walking(int dir) {
		return ImageManager.druid_walking.get(dir - 1);
	}

	/**
	 * @return Returns the mage_been_hit.
	 * 
	 */
	public static AnimationSet getMage_been_hit(int dir) {
		return ImageManager.mage_been_hit.get(dir - 1);
	}

	/**
	 * @return Returns the mage_pause.
	 * 
	 */
	public static AnimationSet getMage_pause(int dir) {
		return ImageManager.mage_pause.get(dir - 1);
	}

	/**
	 * @return Returns the mage_running.
	 * 
	 */
	public static AnimationSet getMage_running(int dir) {
		return ImageManager.mage_running.get(dir - 1);
	}

	/**
	 * @return Returns the mage_slays.
	 * 
	 */
	public static AnimationSet getMage_slays(int dir) {
		return ImageManager.mage_slays.get(dir - 1);
	}

	/**
	 * @return Returns the mage_sorcering.
	 * 
	 */
	public static AnimationSet getMage_sorcering(int dir) {
		return ImageManager.mage_sorcering.get(dir - 1);
	}

	/**
	 * @return Returns the mage_tipping_over.
	 * 
	 */
	public static AnimationSet getMage_tipping_over(int dir) {
		return ImageManager.mage_tipping_over.get(dir - 1);
	}

	/**
	 * @return Returns the mage_using.
	 * 
	 */
	public static AnimationSet getMage_using(int dir) {
		return ImageManager.mage_using.get(dir - 1);
	}

	/**
	 * @return Returns the mage_walking.
	 * 
	 * @uml.property name="mage_walking"
	 */
	public static AnimationSet getMage_walking(int dir) {
		return ImageManager.mage_walking.get(dir - 1);
	}

	/**
	 * @return Returns the thief_been_hit.
	 * 
	 */
	public static AnimationSet getThief_been_hit(int dir) {
		return ImageManager.thief_been_hit.get(dir - 1);
	}

	/**
	 * @return Returns the thief_pause.
	 * 
	 */
	public static AnimationSet getThief_pause(int dir) {
		return ImageManager.thief_pause.get(dir - 1);
	}

	/**
	 * @return Returns the thief_running.
	 * 
	 */
	public static AnimationSet getThief_running(int dir) {
		return ImageManager.thief_running.get(dir - 1);
	}

	/**
	 * @return Returns the thief_slays.
	 * 
	 */
	public static AnimationSet getThief_slays(int dir) {
		return ImageManager.thief_slays.get(dir - 1);
	}

	/**
	 * @return Returns the thief_sorcering.
	 * 
	 */
	public static AnimationSet getThief_sorcering(int dir) {
		return ImageManager.thief_sorcering.get(dir - 1);
	}

	/**
	 * @return Returns the thief_tipping_over.
	 * 
	 */
	public static AnimationSet getThief_tipping_over(int dir) {
		return ImageManager.thief_tipping_over.get(dir - 1);
	}

	/**
	 * @return Returns the thief_using.
	 * 
	 */
	public static AnimationSet getThief_using(int dir) {
		return ImageManager.thief_using.get(dir - 1);
	}

	/**
	 * @return Returns the thief_walking.
	 * 
	 * @uml.property name="thief_walking"
	 */
	public static AnimationSet getThief_walking(int dir) {
		return ImageManager.thief_walking.get(dir - 1);
	}

	/**
	 * @return Returns the warrior_been_hit.
	 * 
	 */
	public static AnimationSet getWarrior_been_hit(int dir) {
		return ImageManager.warrior_been_hit.get(dir - 1);
	}

	/**
	 * @return Returns the warrior_pause.
	 * 
	 */
	public static AnimationSet getWarrior_pause(int dir) {
		return ImageManager.warrior_pause.get(dir - 1);
	}

	/**
	 * @return Returns the warrior_running.
	 * 
	 */
	public static AnimationSet getWarrior_running(int dir) {
		return ImageManager.warrior_running.get(dir - 1);
	}

	/**
	 * @return Returns the warrior_sorcering.
	 * 
	 */
	public static AnimationSet getWarrior_sorcering(int dir) {
		return ImageManager.warrior_sorcering.get(dir - 1);
	}

	/**
	 * @return Returns the warrior_tipping_over.
	 * 
	 */
	public static AnimationSet getWarrior_tipping_over(int dir) {
		return ImageManager.warrior_tipping_over.get(dir - 1);
	}

	/**
	 * @return Returns the warrior_using.
	 * 
	 */
	public static AnimationSet getWarrior_using(int dir) {
		return ImageManager.warrior_using.get(dir - 1);
	}

	/**
	 * @return Returns the warrior_walking.
	 * 
	 */
	public static AnimationSet getWarrior_walking(int dir) {
		return ImageManager.warrior_walking.get(dir - 1);
	}

	/**
	 * @return Returns the warrior_slays.
	 * 
	 */
	public static AnimationSet getWarrior_slays(int dir) {
		return ImageManager.warrior_slays.get(dir - 1);
	}

}
