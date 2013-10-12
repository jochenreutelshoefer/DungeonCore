/*
 * Created on 25.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D;


import graphics.AbstractImageLoader;
import graphics.JDImageProxy;
import gui.StartView;
import gui.audio.AudioEffectsManager;

//import gui.audio.AudioEffectsManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import animation.AnimationSet;
import animation.AnimationSetDirections;
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


	
	public static JDImageProxy woodTextureImage;

	public static JDImageProxy fieldImage;
	
	public static JDImageProxy amulettImage;

	public static JDImageProxy armorImage;

	public static JDImageProxy axeImage;

	public static JDImageProxy bear2Image;

	public static JDImageProxy [] bearImage;

	public static JDImageProxy bookImage;

	public static JDImageProxy caveImage;

	public static JDImageProxy chest_lockImage;

	public static JDImageProxy chestImage;

	public static JDImageProxy clubImage;

	public static JDImageProxy cristall_blueImage;

	public static JDImageProxy cristall_greenImage;

	public static JDImageProxy cristall_redImage;

	public static JDImageProxy cristall_yellowImage;

	public static JDImageProxy cursor_clock;

	public static JDImageProxy cursor_key_Image;

	public static JDImageProxy cursor_key_not_Image;
	
	public static JDImageProxy cursor_go_not_Image;
	public static JDImageProxy cursor_wand;
	public static JDImageProxy cursor_use_Image;

	public static JDImageProxy cursor_scout;

	public static JDImageProxy cursor_sword;

	public static JDImageProxy dark_dwarfImage;
	
	public static JDImageProxy finImage;

	public static JDImageProxy darkMasterImage;

	public static JDImageProxy dead_druidImage;

	public static JDImageProxy dead_dwarfImage;

	public static JDImageProxy dead_mageImage;

	public static JDImageProxy dead_thiefImage;

	public static JDImageProxy dead_warriorImage;

	public static JDImageProxy deathImage;

	public static JDImageProxy documentImage;

	public static JDImageProxy door_east;

	public static JDImageProxy door_east_lock;

	public static JDImageProxy door_east_none;

	public static JDImageProxy door_north;

	public static JDImageProxy door_north_lock;

	public static JDImageProxy door_north_none;

	public static JDImageProxy door_south;

	public static JDImageProxy door_south_lock;

	public static JDImageProxy door_south_none;

	public static JDImageProxy door_west;

	public static JDImageProxy door_west_lock;

	public static JDImageProxy door_west_none;

	public static AnimationSetDirections druid_been_hit;

	public static AnimationSetDirections druid_pause;

	public static AnimationSetDirections druid_running;

	public static AnimationSetDirections druid_slays;

	public static AnimationSetDirections druid_sorcering;

	public static AnimationSetDirections druid_tipping_over;

	public static AnimationSetDirections druid_using;

	public static AnimationSetDirections druid_walking;

	public static JDImageProxy  [] druidImage;

	public static JDImageProxy dummieImage;

	public static JDImageProxy dustImage;

	public static JDImageProxy engelImage;

	public static JDImageProxy falltuerImage;

	public static JDImageProxy floor_darkImage;

	public static JDImageProxy floor_darkImage2;

	public static JDImageProxy floor_darkImage3;

	public static JDImageProxy floor_darkImage4;

	public static JDImageProxy floor_darkImage5;

	public static JDImageProxy floor_darkImage6;

	public static JDImageProxy floor_darkImage7;

	public static JDImageProxy floor_mediumImage;

	public static JDImageProxy floor_mediumImage2;

	public static JDImageProxy floor_mediumImage3;

	public static JDImageProxy floor_mediumImage4;

	public static JDImageProxy floor_mediumImage5;

	public static JDImageProxy floor_mediumImage6;

	public static JDImageProxy floor_mediumImage7;

	public static JDImageProxy floorImage;

	public static JDImageProxy[] floorImage_darkArray = new JDImageProxy[8];

	public static JDImageProxy[] floorImage_mediumArray = new JDImageProxy[8];

	public static JDImageProxy floorImage2;

	public static JDImageProxy floorImage3;

	public static JDImageProxy floorImage4;

	public static JDImageProxy floorImage5;

	public static JDImageProxy floorImage6;

	public static JDImageProxy floorImage7;

	public static JDImageProxy[] floorImageArray = new JDImageProxy[8];

	public static JDImageProxy fountainImage;

	public static AnimationSetDirections ghul1_been_hit;

	public static AnimationSetDirections ghul1_pause = null;

	public static AnimationSetDirections ghul1_running = null;

	public static AnimationSetDirections ghul1_slays;

	public static AnimationSetDirections ghul1_sorcering = null;

	public static AnimationSetDirections ghul1_tipping_over;

	public static AnimationSetDirections ghul1_using = null;

	public static AnimationSetDirections ghul1_walking = null;

	public static JDImageProxy  [] ghulImage;

	public static JDImageProxy graveImage;

	public static JDImageProxy hand_greift1_Image;

	public static JDImageProxy hand_zeigt1_Image;

	public static JDImageProxy cursor_go_Image;
	
	public static JDImageProxy helmetImage;

	public static JDImageProxy keyImage;

	public static JDImageProxy kugelImage;

	public static JDImageProxy lanceImage;

	public static JDImageProxy luzia_ball_greyImage;

	public static JDImageProxy luzia_ball_redImage;

	public static JDImageProxy luzia_hutImage;

	public static JDImageProxy luziaImage;

	public static AnimationSetDirections mage_been_hit;

	public static AnimationSetDirections mage_pause;

	public static AnimationSetDirections mage_running;

	public static AnimationSetDirections mage_slays;

	public static AnimationSetDirections mage_sorcering;

	public static AnimationSetDirections mage_tipping_over;

	public static AnimationSetDirections mage_using;

	public static AnimationSetDirections mage_walking;

	public static JDImageProxy []  mageImage;

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

	public static JDImageProxy  [] ogreImage;

	public static JDImageProxy  [] orcImage;

	public static JDImageProxy pentagrammImage;

	public static JDImageProxy potion_blueImage;

	public static JDImageProxy potion_redImage;
	
	public static JDImageProxy potion_greenImage;
	
	public static JDImageProxy featherImage;

	public static AnimationSet puff;

	public static JDImageProxy questionmark;

	public static JDImageProxy repairImage;

	public static JDImageProxy rune_blueImage;

	public static JDImageProxy rune_greenImage;

	public static JDImageProxy rune_redImage;

	public static JDImageProxy rune_yellowImage;

	public static JDImageProxy scrollImage;

	public static JDImageProxy shieldImage;

	public static JDImageProxy shrine_blackImage;

	public static JDImageProxy shrine_blueImage;

	public static JDImageProxy shrine_greenImage;

	public static JDImageProxy shrine_lilaImage;

	public static JDImageProxy shrine_redImage;

	public static JDImageProxy shrine_small_blueImage;

	public static JDImageProxy shrine_small_greenImage;

	public static JDImageProxy shrine_small_redImage;

	public static JDImageProxy shrine_small_yellowImage;

	public static JDImageProxy shrine_turkisImage;

	public static JDImageProxy shrine_whiteImage;

	public static JDImageProxy shrine_yellowImage;

	public static AnimationSetDirections skel1_been_hit;

	public static AnimationSetDirections skel1_pause = null;

	public static AnimationSetDirections skel1_running = null;

	public static AnimationSetDirections skel1_slays;

	public static AnimationSetDirections skel1_sorcering = null;

	public static AnimationSetDirections skel1_tipping_over;

	public static AnimationSetDirections skel1_using = null;

	public static AnimationSetDirections skel1_walking = null;

	public static JDImageProxy skel2Image;

	public static JDImageProxy []  skelImage;

	public static JDImageProxy sorcLabImage;

	public static JDImageProxy spotImage;

	public static JDImageProxy statueImage;

	public static JDImageProxy swordImage;

	public static AnimationSetDirections thief_been_hit;

	public static AnimationSetDirections thief_pause;

	public static AnimationSetDirections thief_running;

	public static AnimationSetDirections thief_slays;

	public static AnimationSetDirections thief_sorcering;

	public static AnimationSetDirections thief_tipping_over;

	public static AnimationSetDirections thief_using;

	public static AnimationSetDirections thief_walking;

	public static JDImageProxy  [] thiefImage;

	public static JDImageProxy traderImage;

	public static JDImageProxy wall_southImage;

	//public static JDImageProxy wallImage;

	public static JDImageProxy wall_sidesImage;
	public static JDImageProxy wall_northImage;
	
	public static AnimationSetDirections warrior_been_hit;

	public static AnimationSetDirections warrior_pause;

	public static AnimationSetDirections warrior_running;

	public static AnimationSetDirections warrior_slays;

	public static AnimationSetDirections warrior_sorcering;

	public static AnimationSetDirections warrior_tipping_over;

	public static AnimationSetDirections warrior_using;

	public static AnimationSetDirections warrior_walking;

	public static JDImageProxy[] warriorImage;

	public static AnimationSetDirections wolf1_been_hit;

	public static AnimationSetDirections wolf1_pause = null;

	public static AnimationSetDirections wolf1_running = null;

	public static AnimationSetDirections wolf1_slays;

	public static AnimationSetDirections wolf1_sorcering = null;

	public static AnimationSetDirections wolf1_tipping_over;

	public static AnimationSetDirections wolf1_using = null;

	public static AnimationSetDirections wolf1_walking = null;

	public static JDImageProxy wolf2Image;

	public static JDImageProxy  [] wolfImage;

	public static JDImageProxy wolfknifeImage;

	public static JDImageProxy xmasImage;

	private static AnimationSetDirections load4Animations(AbstractImageLoader a, String path,
			String pattern, int cnt) {
		System.gc();
		AnimationSet[] set = new AnimationSet[4];
		for (int i = 0; i < 4; i++) {
			set[i] = new AnimationSet(loadArray(a, path, pattern, i + 1, cnt),
					getArray(35, cnt));
		}
		return new AnimationSetDirections(set);
	}

	public static boolean imagesLoaded = false;
	
	public void loadImages(final StartView board) {
		
		if(!imagesLoaded) {
		
		   
		
		AbstractImageLoader a = this.loader;

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
		
		
		dead_dwarfImage = new JDImageProxy(a, "dead_dwarf.gif");
		dead_warriorImage = new JDImageProxy(a, "dead_stan.gif");
		dead_thiefImage = new JDImageProxy(a, "dead_blue_pirate.gif");
		dead_druidImage = new JDImageProxy(a, "dead_white_mage.gif");
		dead_mageImage = new JDImageProxy(a, "dead_black_mage.gif");

		chestImage = new JDImageProxy(a, "chest_cut_trans.gif");
		floorImage = new JDImageProxy(a, "boden5.gif");
		floor_darkImage = new JDImageProxy(a, "boden5_dark.gif");
		floor_mediumImage = new JDImageProxy(a, "boden5_dark1.gif");

		floorImage2 = new JDImageProxy(a, "boden3.gif");
		floor_darkImage2 = new JDImageProxy(a, "boden3_dark.gif");
		floor_mediumImage2 = new JDImageProxy(a, "boden3_dark1.gif");

		floorImage3 = new JDImageProxy(a, "boden4.gif");
		floor_darkImage3 = new JDImageProxy(a, "boden4_dark.gif");
		floor_mediumImage3 = new JDImageProxy(a, "boden4_dark1.gif");

		floorImage4 = new JDImageProxy(a, "boden6.gif");
		floor_darkImage4 = new JDImageProxy(a, "boden6_dark.gif");
		floor_mediumImage4 = new JDImageProxy(a, "boden6_dark1.gif");

		floorImage5 = new JDImageProxy(a, "boden7.gif");
		floor_darkImage5 = new JDImageProxy(a, "boden7_dark.gif");
		floor_mediumImage5 = new JDImageProxy(a, "boden7_dark1.gif");

		floorImage6 = new JDImageProxy(a, "boden8.gif");
		floor_darkImage6 = new JDImageProxy(a, "boden8_dark.gif");
		floor_mediumImage6 = new JDImageProxy(a, "boden8_dark1.gif");

		floorImage7 = new JDImageProxy(a, "boden9.gif");
		floor_darkImage7 = new JDImageProxy(a, "boden9_dark.gif");
		floor_mediumImage7 = new JDImageProxy(a, "boden9_dark1.gif");

		makeFloorArrays();

		wall_sidesImage = new JDImageProxy(a, "wand_seiten.gif");
		wall_northImage = new JDImageProxy(a, "wand_nord.gif");
		wall_southImage = new JDImageProxy(a, "wall_south.gif");

		door_north = new JDImageProxy(a, "tuer_nord.gif");

		door_north_none = new JDImageProxy(a, "tuer_nord_keine.gif");

		door_east = new JDImageProxy(a, "tuer_ost.gif");

		door_east_none = new JDImageProxy(a, "tuer_ost_keine.gif");

		door_west = new JDImageProxy(a, "tuer_west.gif");

		door_west_none = new JDImageProxy(a, "tuer_west_keine.gif");
		door_south_none = new JDImageProxy(a, "tuer_sued_keine.gif");
		door_south = new JDImageProxy(a, "tuer_sued.gif");
		door_south_lock = new JDImageProxy(a, "tuer_sued_schloss.gif");

		axeImage = new JDImageProxy(a, "axt.gif");
		swordImage = new JDImageProxy(a, "schwert.gif");
		lanceImage = new JDImageProxy(a, "lanze.gif");
		wolfknifeImage = new JDImageProxy(a, "wolfsmesser.gif");
		clubImage = new JDImageProxy(a, "knueppel.gif");
		scrollImage = new JDImageProxy(a, "scroll_blue.gif");
		documentImage = new JDImageProxy(a, "scroll_white.gif");
		potion_redImage = new JDImageProxy(a, "potion_rot.gif");
		potion_blueImage = new JDImageProxy(a, "potion_blue.gif");
		dustImage = new JDImageProxy(a, "dust.gif");
		armorImage = new JDImageProxy(a, "ruestung.gif");
		shieldImage = new JDImageProxy(a, "shield2.gif");
		helmetImage = new JDImageProxy(a, "helm.gif");
		bookImage = new JDImageProxy(a, "book_yellow.gif");
		keyImage = new JDImageProxy(a, "key.gif");
		falltuerImage = new JDImageProxy(a, "falltuer.gif");
		engelImage = new JDImageProxy(a, "engel2.gif");
		shrine_blueImage = new JDImageProxy(a, "shrine_blue.gif");
		shrine_redImage = new JDImageProxy(a, "shrine_red.gif");
		shrine_greenImage = new JDImageProxy(a, "shrine_green.gif");
		shrine_yellowImage = new JDImageProxy(a, "shrine_yellow.gif");
		shrine_whiteImage = new JDImageProxy(a, "shrine_white.gif");
		shrine_blackImage = new JDImageProxy(a, "shrine_black.gif");
		shrine_lilaImage = new JDImageProxy(a, "shrine_lila.gif");
		shrine_turkisImage = new JDImageProxy(a, "shrine_tuerkis.gif");
		shrine_small_redImage = new JDImageProxy(a,
				"shrine_small_red.gif");
		shrine_small_blueImage = new JDImageProxy(a,
				"shrine_small_blue.gif");
		shrine_small_yellowImage = new JDImageProxy(a,
				"shrine_small_yellow.gif");
		shrine_small_greenImage = new JDImageProxy(a,
				"shrine_small_green.gif");
		sorcLabImage = new JDImageProxy(a, "zauberlabor1.gif");
		amulettImage = new JDImageProxy(a, "amulett.gif");

		repairImage = new JDImageProxy(a, "amboss.gif");
		fountainImage = new JDImageProxy(a, "fountain.gif");
		statueImage = new JDImageProxy(a, "statue.gif");
		chest_lockImage = new JDImageProxy(a, "chest_schloss.gif");
		door_north_lock = new JDImageProxy(a, "tuer_nord_schloss.gif");
		door_east_lock = new JDImageProxy(a, "tuer_ost_schloss.gif");
		door_west_lock = new JDImageProxy(a, "tuer_west_schloss.gif");
		graveImage = new JDImageProxy(a, "grave.gif");
		caveImage = new JDImageProxy(a, "cave.gif");
		traderImage = new JDImageProxy(a, "haendler2.gif");
		rune_redImage = new JDImageProxy(a, "rune_red.gif");
		rune_greenImage = new JDImageProxy(a, "rune_green.gif");
		rune_blueImage = new JDImageProxy(a, "rune_blue.gif");
		rune_yellowImage = new JDImageProxy(a, "rune_yellow.gif");
		cristall_redImage = new JDImageProxy(a, "kristall_rot.gif");
		cristall_greenImage = new JDImageProxy(a, "kristall_gruen.gif");
		cristall_blueImage = new JDImageProxy(a, "kristall_blau.gif");
		cristall_yellowImage = new JDImageProxy(a, "kristall_gelb.gif");
		spotImage = new JDImageProxy(a, "versteck.gif");
		hand_zeigt1_Image = new JDImageProxy(a, "zeigerhand-zeigt1.gif");
		cursor_go_Image = new JDImageProxy(a, "zeigerhand_go.gif");
		hand_greift1_Image = new JDImageProxy(a,
				"zeigerhand-greift1.gif");
		cursor_key_Image = new JDImageProxy(a, "zeiger_key.gif");
		cursor_key_not_Image = new JDImageProxy(a,
				"zeiger_key_nicht.gif");
		cursor_sword = new JDImageProxy(a, "zeigerhand-schwert.gif");
		cursor_clock = new JDImageProxy(a, "zeigerhand-sanduhr.gif");
		cursor_scout = new JDImageProxy(a, "zeigerhand-go_scout.gif");
		cursor_go_not_Image =  new JDImageProxy(a, "zeigerhand_go_not.gif");
		cursor_wand =  new JDImageProxy(a, "zeiger_zauberstab.gif");
		cursor_use_Image = new JDImageProxy(a, "zeigerhand_faust.gif");

		pentagrammImage = new JDImageProxy(a, "pentagramm.gif");
		darkMasterImage = new JDImageProxy(a, "mister_death.gif");
		luziaImage = new JDImageProxy(a, "luzia.gif");
		luzia_hutImage = new JDImageProxy(a, "luzia_hut.gif");
		kugelImage = new JDImageProxy(a, "kugel.gif");
		questionmark = new JDImageProxy(a, "fragezeichen.gif");
		xmasImage = new JDImageProxy(a, "xmas.gif");
		dark_dwarfImage = new JDImageProxy(a, "dark_dwarf.gif");
		finImage = new JDImageProxy(a, "growing n0007c.gif");
		luzia_ball_greyImage = new JDImageProxy(a, "kugel_grau.gif");

		luzia_ball_redImage = new JDImageProxy(a, "kugel_rot.gif");

		fieldImage  = new JDImageProxy(a, "field3.gif");
		woodTextureImage =  new JDImageProxy(a, "theWood3.gif");
		
		featherImage = new JDImageProxy(a, "feder.gif");
		potion_greenImage = new JDImageProxy(a, "potion_green.gif");

		}
		
		board.imagesLoaded();
		imagesLoaded = true;
	}
	
	private static JDImageProxy[] makePics(AnimationSetDirections a) {
		AnimationSet[] sets = a.getAnimations();
		JDImageProxy ims [] = new JDImageProxy[sets.length];
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
	

	private static JDImageProxy[] loadArray(AbstractImageLoader a, String path, int cnt) {
		JDImageProxy[] ims = new JDImageProxy[cnt];
		for (int i = 0; i < cnt; i++) {

			JDImageProxy im = new JDImageProxy(path + Integer.toString(i)+ ".GIF",a);

			ims[i] = im;
			if (ims[i] == null) {

				System.out.println("Bild nicht geladen: " + path
						+ Integer.toString(i) + ".GIF");
				return null;
			}
		}

		return ims;
	}

	private static JDImageProxy[] loadArray(AbstractImageLoader a, String path,
			String fileNamePrefix, int dir, int cnt) {

		List<JDImageProxy> imageList = new LinkedList<JDImageProxy>();
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
			
			JDImageProxy im = new JDImageProxy(path + fileNamePrefix + dirChar + suffix+"_trans.GIF",a);
			if(im.fileExists()) {
				imageList.add(im);			
			}
			i++;

			numberStr = Integer.toString(i);
			suffix = "";
			for (int j = 0; j < 4 - numberStr.length(); j++) {
				suffix += "0";
			}
			suffix += numberStr;
		}
		

		JDImageProxy[] ims = new JDImageProxy[imageList.size()];
		int k = 0;
		for (Iterator<JDImageProxy> iter = imageList.iterator(); iter.hasNext();) {
			JDImageProxy element = iter.next();
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
