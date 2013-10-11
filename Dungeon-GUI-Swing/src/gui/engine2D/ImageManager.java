/*
 * Created on 25.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D;


import gui.StartView;
import gui.audio.AudioEffectsManager;
import gui.engine2D.animation.AnimationSet;
import gui.engine2D.animation.AnimationSetDirections;
import io.JDImageLoader;
import io.MyLookUpTable;
import io.PictureLoadDialog;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import dungeon.Dir;

public class ImageManager {

	private static MediaTracker tracker;

	private static Component bild;
	
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
		
		   
		JDImageLoader.dialog = loadDialog;
		bild = board;
		tracker = new MediaTracker(board);
		JDImageLoader.setTracker(tracker);

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
		
		
		dead_dwarfImage = JDImageLoader.loadImage(a, "dead_dwarf.gif");
		dead_warriorImage = JDImageLoader.loadImage(a, "dead_stan.gif");
		dead_thiefImage = JDImageLoader.loadImage(a, "dead_blue_pirate.gif");
		dead_druidImage = JDImageLoader.loadImage(a, "dead_white_mage.gif");
		dead_mageImage = JDImageLoader.loadImage(a, "dead_black_mage.gif");

		chestImage = JDImageLoader.loadImage(a, "chest_cut_trans.gif");
		floorImage = JDImageLoader.loadImage(a, "boden5.gif");
		floor_darkImage = JDImageLoader.loadImage(a, "boden5_dark.gif");
		floor_mediumImage = JDImageLoader.loadImage(a, "boden5_dark1.gif");

		floorImage2 = JDImageLoader.loadImage(a, "boden3.gif");
		floor_darkImage2 = JDImageLoader.loadImage(a, "boden3_dark.gif");
		floor_mediumImage2 = JDImageLoader.loadImage(a, "boden3_dark1.gif");

		floorImage3 = JDImageLoader.loadImage(a, "boden4.gif");
		floor_darkImage3 = JDImageLoader.loadImage(a, "boden4_dark.gif");
		floor_mediumImage3 = JDImageLoader.loadImage(a, "boden4_dark1.gif");

		floorImage4 = JDImageLoader.loadImage(a, "boden6.gif");
		floor_darkImage4 = JDImageLoader.loadImage(a, "boden6_dark.gif");
		floor_mediumImage4 = JDImageLoader.loadImage(a, "boden6_dark1.gif");

		floorImage5 = JDImageLoader.loadImage(a, "boden7.gif");
		floor_darkImage5 = JDImageLoader.loadImage(a, "boden7_dark.gif");
		floor_mediumImage5 = JDImageLoader.loadImage(a, "boden7_dark1.gif");

		floorImage6 = JDImageLoader.loadImage(a, "boden8.gif");
		floor_darkImage6 = JDImageLoader.loadImage(a, "boden8_dark.gif");
		floor_mediumImage6 = JDImageLoader.loadImage(a, "boden8_dark1.gif");

		floorImage7 = JDImageLoader.loadImage(a, "boden9.gif");
		floor_darkImage7 = JDImageLoader.loadImage(a, "boden9_dark.gif");
		floor_mediumImage7 = JDImageLoader.loadImage(a, "boden9_dark1.gif");

		makeFloorArrays();

		wall_sidesImage = JDImageLoader.loadImage(a, "wand_seiten.gif");
		wall_northImage = JDImageLoader.loadImage(a, "wand_nord.gif");
		wall_southImage = JDImageLoader.loadImage(a, "wall_south.gif");

		door_north = JDImageLoader.loadImage(a, "tuer_nord.gif");
		// door_north = jd_imageLoader.loadImage(a, "hand_zeigt1_Image.gif");

		door_north_none = JDImageLoader.loadImage(a, "tuer_nord_keine.gif");

		door_east = JDImageLoader.loadImage(a, "tuer_ost.gif");

		door_east_none = JDImageLoader.loadImage(a, "tuer_ost_keine.gif");

		door_west = JDImageLoader.loadImage(a, "tuer_west.gif");

		door_west_none = JDImageLoader.loadImage(a, "tuer_west_keine.gif");
		door_south_none = JDImageLoader.loadImage(a, "tuer_sued_keine.gif");
		door_south = JDImageLoader.loadImage(a, "tuer_sued.gif");
		door_south_lock = JDImageLoader.loadImage(a, "tuer_sued_schloss.gif");

		axeImage = JDImageLoader.loadImage(a, "axt.gif");
		;
		swordImage = JDImageLoader.loadImage(a, "schwert.gif");
		;
		lanceImage = JDImageLoader.loadImage(a, "lanze.gif");
		;
		wolfknifeImage = JDImageLoader.loadImage(a, "wolfsmesser.gif");
		;
		clubImage = JDImageLoader.loadImage(a, "knueppel.gif");
		;
		scrollImage = JDImageLoader.loadImage(a, "scroll_blue.gif");
		documentImage = JDImageLoader.loadImage(a, "scroll_white.gif");
		;
		potion_redImage = JDImageLoader.loadImage(a, "potion_rot.gif");
		;
		potion_blueImage = JDImageLoader.loadImage(a, "potion_blue.gif");
		;
		dustImage = JDImageLoader.loadImage(a, "dust.gif");
		;
		armorImage = JDImageLoader.loadImage(a, "ruestung.gif");
		;
		shieldImage = JDImageLoader.loadImage(a, "shield2.gif");
		;
		// //System.out.println("schild noch ok!");
		helmetImage = JDImageLoader.loadImage(a, "helm.gif");
		;
		// //System.out.println("helm noch ok!");
		bookImage = JDImageLoader.loadImage(a, "book_yellow.gif");
		;
		// //System.out.println("buch noch ok!");
		keyImage = JDImageLoader.loadImage(a, "key.gif");
		;
		// //System.out.println("schluessel noch ok!");
		falltuerImage = JDImageLoader.loadImage(a, "falltuer.gif");
		;
		// //System.out.println("falltuer noch ok!");
		engelImage = JDImageLoader.loadImage(a, "engel2.gif");
		;
		// //System.out.println("engel noch ok!");
		shrine_blueImage = JDImageLoader.loadImage(a, "shrine_blue.gif");
		;
		// //System.out.println("shrine_blue noch ok!");
		shrine_redImage = JDImageLoader.loadImage(a, "shrine_red.gif");
		;
		shrine_greenImage = JDImageLoader.loadImage(a, "shrine_green.gif");
		;
		shrine_yellowImage = JDImageLoader.loadImage(a, "shrine_yellow.gif");
		;
		shrine_whiteImage = JDImageLoader.loadImage(a, "shrine_white.gif");
		;
		shrine_blackImage = JDImageLoader.loadImage(a, "shrine_black.gif");
		;
		shrine_lilaImage = JDImageLoader.loadImage(a, "shrine_lila.gif");
		;
		shrine_turkisImage = JDImageLoader.loadImage(a, "shrine_tuerkis.gif");
		;
		shrine_small_redImage = JDImageLoader.loadImage(a,
				"shrine_small_red.gif");
		;
		shrine_small_blueImage = JDImageLoader.loadImage(a,
				"shrine_small_blue.gif");
		;
		shrine_small_yellowImage = JDImageLoader.loadImage(a,
				"shrine_small_yellow.gif");
		;
		shrine_small_greenImage = JDImageLoader.loadImage(a,
				"shrine_small_green.gif");
		;
		sorcLabImage = JDImageLoader.loadImage(a, "zauberlabor1.gif");
		;
		amulettImage = JDImageLoader.loadImage(a, "amulett.gif");
		;

		repairImage = JDImageLoader.loadImage(a, "amboss.gif");
		// System.out.print("repair noch ok!");
		;
		fountainImage = JDImageLoader.loadImage(a, "fountain.gif");
		;
		// System.out.print("fountain noch ok!");
		statueImage = JDImageLoader.loadImage(a, "statue.gif");
		;
		chest_lockImage = JDImageLoader.loadImage(a, "chest_schloss.gif");
		;
		door_north_lock = JDImageLoader.loadImage(a, "tuer_nord_schloss.gif");
		;
		door_east_lock = JDImageLoader.loadImage(a, "tuer_ost_schloss.gif");
		;
		door_west_lock = JDImageLoader.loadImage(a, "tuer_west_schloss.gif");
		;
		graveImage = JDImageLoader.loadImage(a, "grave.gif");
		;
		caveImage = JDImageLoader.loadImage(a, "cave.gif");
		;
		traderImage = JDImageLoader.loadImage(a, "haendler2.gif");
		;
		rune_redImage = JDImageLoader.loadImage(a, "rune_red.gif");
		;
		rune_greenImage = JDImageLoader.loadImage(a, "rune_green.gif");
		;
		rune_blueImage = JDImageLoader.loadImage(a, "rune_blue.gif");
		;
		rune_yellowImage = JDImageLoader.loadImage(a, "rune_yellow.gif");
		;
		cristall_redImage = JDImageLoader.loadImage(a, "kristall_rot.gif");
		;
		cristall_greenImage = JDImageLoader.loadImage(a, "kristall_gruen.gif");
		;
		cristall_blueImage = JDImageLoader.loadImage(a, "kristall_blau.gif");
		;
		cristall_yellowImage = JDImageLoader.loadImage(a, "kristall_gelb.gif");
		;
		spotImage = JDImageLoader.loadImage(a, "versteck.gif");
		;
		hand_zeigt1_Image = JDImageLoader.loadImage(a, "zeigerhand-zeigt1.gif");
		cursor_go_Image = JDImageLoader.loadImage(a, "zeigerhand_go.gif");
		hand_greift1_Image = JDImageLoader.loadImage(a,
				"zeigerhand-greift1.gif");
		cursor_key_Image = JDImageLoader.loadImage(a, "zeiger_key.gif");
		cursor_key_not_Image = JDImageLoader.loadImage(a,
				"zeiger_key_nicht.gif");
		;
		cursor_sword = JDImageLoader.loadImage(a, "zeigerhand-schwert.gif");
		;
		cursor_clock = JDImageLoader.loadImage(a, "zeigerhand-sanduhr.gif");
		;
		cursor_scout = JDImageLoader.loadImage(a, "zeigerhand-go_scout.gif");
		;
		pentagrammImage = JDImageLoader.loadImage(a, "pentagramm.gif");
		darkMasterImage = JDImageLoader.loadImage(a, "mister_death.gif");
		luziaImage = JDImageLoader.loadImage(a, "luzia.gif");
		luzia_hutImage = JDImageLoader.loadImage(a, "luzia_hut.gif");
		kugelImage = JDImageLoader.loadImage(a, "kugel.gif");
		questionmark = JDImageLoader.loadImage(a, "fragezeichen.gif");
		xmasImage = JDImageLoader.loadImage(a, "xmas.gif");
		dark_dwarfImage = JDImageLoader.loadImage(a, "dark_dwarf.gif");
		finImage = JDImageLoader.loadImage(a, "growing n0007c.gif");
		luzia_ball_greyImage = JDImageLoader.loadImage(a, "kugel_grau.gif");

		luzia_ball_redImage = JDImageLoader.loadImage(a, "kugel_rot.gif");

		fieldImage  = JDImageLoader.loadImage(a, "field3.gif");
		woodTextureImage =  JDImageLoader.loadImage(a, "theWood3.gif");
		
		cursor_go_not_Image =  JDImageLoader.loadImage(a, "zeigerhand_go_not.gif");
		cursor_wand =  JDImageLoader.loadImage(a, "zeiger_zauberstab.gif");
		cursor_use_Image = JDImageLoader.loadImage(a, "zeigerhand_faust.gif");
		featherImage = JDImageLoader.loadImage(a, "feder.gif");
		potion_greenImage = JDImageLoader.loadImage(a, "potion_green.gif");

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

	
	static int[][] bgGIF = { {4, 2, 4}};
	static BufferedImageOp opGIF = new LookupOp(new MyLookUpTable(bgGIF,4), null);
	static Graphics g2;
	
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

			Image im = (JDImageLoader.loadImage(a, path + Integer.toString(i)
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

		List imageList = new LinkedList();
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
		String fileName = path + fileNamePrefix + dirChar + suffix + "_trans.GIF";
		File f = new File(JDImageLoader.LOCAL_PICTURE_PATH + fileName);
		
		while (i < 15) {
			
			Image im = (JDImageLoader.loadImage(a,  path + fileNamePrefix + dirChar + suffix+"_trans.GIF"));
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
			if (im == null) {

				break;
				
			}

			numberStr = Integer.toString(i);
			suffix = "";
			for (int j = 0; j < 4 - numberStr.length(); j++) {
				suffix += "0";
			}
			suffix += numberStr;
			fileName = path + fileNamePrefix + dirChar + suffix + "_trans.GIF";
			f = new File(JDImageLoader.LOCAL_PICTURE_PATH + fileName);
		}
		

		//System.out.println("Bild nicht gefunden: " + f.toString());
		Image[] ims = new Image[imageList.size()];
		int k = 0;
		for (Iterator iter = imageList.iterator(); iter.hasNext();) {
			Image element = (Image) iter.next();
			ims[k] = element;
			k++;
		}

		return ims;
	}

	public static Image flipImage(Image offscreenImage) {
		Image off2 = bild.createImage(offscreenImage.getWidth(null),
				offscreenImage.getHeight(null));
		Graphics off2G = off2.getGraphics();

		off2G.drawImage(offscreenImage, offscreenImage.getWidth(null), 0, 0,
				offscreenImage.getHeight(null), 0, 0, offscreenImage
						.getWidth(null), offscreenImage.getHeight(null), null);

		return off2;
	}

//	private static Image[] loadFleeArray(Applet a, String path, int cnt) {
//		Image[] ims = new Image[2 * cnt + 1];
//		// Image testIm = game.getGui().getMainFrame().createImage(100,100);
//		// //Graphics g =
//		// game.getGui().getMainFrame().getSpielfeld().getGraphics();
//		// if(g == null) {
//		// System.out.println("g ist null");
//		// }
//		// String name = "warrior_slays\\warrior_slays";
//		for (int i = 0; i < 2 * cnt; i++) {
//
//			ims[i] = JDImageLoader.loadImage(a, path
//					+ Integer.toString(i % cnt) + ".gif");
//			// g.drawImage(ims[i],0,0,100,100,null);
//			// ims[i].flush();
//			if (ims[i] == null) {
//
//				System.out.println("Bild nicht geladen: " + path
//						+ Integer.toString(i) + ".gif");
//				return null;
//			}
//		}
//		ims[2 * cnt] = getHeroImage(path.charAt(0));
//
//		return ims;
//	}

	private static Image[] loadWarrior_slays(Applet a) {
		// Image[] ims = new Image[13];
		String name = "warrior\\warrior_slays\\warrior_slays";

		return loadArray(a, name, 13);
	}

	private static Image getHeroImage(char c,int dir) {
		// int k = game.getHero().getHeroCode();
		Image im = null;
		if (c == 'w') {
			im = warriorImage[dir-1];
		}
		if (c == 't') {
			im = thiefImage[dir-1];
		}
		if (c == 'd') {
			im = druidImage[dir-1];
		}
		if (c == 'm') {
			im = mageImage[dir-1];
		}

		return im;

	}

	// public static Image flipImage(Image im) {
	//		
	// BufferedImage mBufferedImage = new BufferedImage(im.getWidth(null), im
	// .getHeight(null), BufferedImage.TYPE_INT_ARGB);
	// Graphics2D g2 = mBufferedImage.createGraphics();
	// g2.drawImage(im, null, null);
	//
	// BufferedImage mBufferedImage2 = new BufferedImage(im.getWidth(null), im
	// .getHeight(null), BufferedImage.TYPE_INT_ARGB);
	// AffineTransform trans = new AffineTransform();
	// trans.setToScale(-1, 1);
	// trans.translate((-1)*im.getWidth(null), 0);
	// BufferedImageOp op = new AffineTransformOp(trans,
	// AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	// Image resIm = op.filter(mBufferedImage, mBufferedImage2);
	// return resIm;
	// }

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

	private static int[] deathTimes9 = { 40, 40, 40, 40, 40, 40, 40, 40, 40 };

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
		if(dir < 1) {
			int k = 4 ;
		}
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
