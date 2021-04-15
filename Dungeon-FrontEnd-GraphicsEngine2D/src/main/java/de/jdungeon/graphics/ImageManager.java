/*
 * Created on 25.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


import de.jdungeon.animation.AnimationSetDirections;
import de.jdungeon.animation.DefaultAnimationSet;
import de.jdungeon.animation.Motion;
import de.jdungeon.audio.AudioEffectsManager;
import de.jdungeon.dungeon.ChestInfo;
import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.FigurePresentation;
import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.item.*;
import de.jdungeon.item.equipment.Armor;
import de.jdungeon.item.equipment.Helmet;
import de.jdungeon.item.equipment.Shield;
import de.jdungeon.item.equipment.weapon.*;
import de.jdungeon.item.map.AncientMapFragment;
import de.jdungeon.item.paper.Book;
import de.jdungeon.item.paper.InfoScroll;
import de.jdungeon.item.quest.*;
import de.jdungeon.location.*;
import de.jdungeon.location.defender.DefenderLocation;
import de.jdungeon.log.Log;
import de.jdungeon.util.Clazz;

public class ImageManager {

	private static ImageManager instance;
	private final AbstractImageLoader<?> loader;

	public ImageManager(AbstractImageLoader<?> loader2) {
		this.loader = loader2;
	}

	public static ImageManager getInstance(AbstractImageLoader<?> loader) {
		if (instance == null) {
			instance = new ImageManager(loader);
		}
		return instance;
	}

	public static JDImageProxy<?> button1;

	public static JDImageProxy<?> woodTextureImage;

	public static JDImageProxy<?> fieldImage;

	public static JDImageProxy<?> amulettImage;

	public static JDImageProxy<?> armorImage;

	public static JDImageProxy<?> axeImage;

	public static JDImageProxy<?> bear2Image;

	public static JDImageProxy<?>[] bearImage;

	public static JDImageProxy<?> bookImage;

	public static JDImageProxy<?> caveImage;

	public static JDImageProxy<?> chest_lockImage;

	public static JDImageProxy<?> chestImage;

	public static JDImageProxy<?> clubImage;

	public static JDImageProxy<?> cristall_blueImage;

	public static JDImageProxy<?> cristall_greenImage;

	public static JDImageProxy<?> cristall_redImage;

	public static JDImageProxy<?> cristall_yellowImage;

	public static JDImageProxy<?> cursor_clock;

	public static JDImageProxy<?> cursor_key_Image;

	public static JDImageProxy<?> cursor_key_not_Image;

	public static JDImageProxy<?> cursor_go_not_Image;
	public static JDImageProxy<?> cursor_wand;
	public static JDImageProxy<?> cursor_use_Image;

	public static JDImageProxy<?> cursor_scout;

	public static JDImageProxy<?> cursor_sword;

	public static JDImageProxy<?> dark_dwarfImage;

	public static JDImageProxy<?> firImage;

	public static JDImageProxy<?> darkMasterImage;

	public static JDImageProxy<?> dead_druidImage;

	public static JDImageProxy<?> dead_dwarfImage;

	public static JDImageProxy<?> dead_mageImage;

	public static JDImageProxy<?> dead_thiefImage;

	public static JDImageProxy<?> dead_warriorImage;

	public static JDImageProxy<?> deathImage;

	public static JDImageProxy<?> documentImage;

	public static JDImageProxy<?> door_east;

	public static JDImageProxy<?> door_east_lock;

	public static JDImageProxy<?> door_east_none;

	public static JDImageProxy<?> door_north;
	public static JDImageProxy<?> door_north_solo;

	public static JDImageProxy<?> door_north_lock;

	public static JDImageProxy<?> door_north_lock_solo;

	public static JDImageProxy<?> door_north_none;

	public static JDImageProxy<?> door_south;

	public static JDImageProxy<?> door_south_lock;

	public static JDImageProxy<?> door_south_none;

	public static JDImageProxy<?> door_west;

	public static JDImageProxy<?> door_west_lock;

	public static JDImageProxy<?> door_west_none;

	public static AnimationSetDirections druid_been_hit;

	public static AnimationSetDirections druid_pause;

	public static AnimationSetDirections druid_running;

	public static AnimationSetDirections druid_slays;

	public static AnimationSetDirections druid_sorcering;

	public static AnimationSetDirections druid_tipping_over;

	public static AnimationSetDirections druid_using;

	public static AnimationSetDirections druid_walking;

	public static JDImageProxy<?>[] druidImage;

	public static JDImageProxy<?> dummieImage;

	public static JDImageProxy<?> dustImage;

	public static JDImageProxy<?> engelImage;

	public static JDImageProxy<?> falltuerImage;

	public static JDImageProxy<?> floor_darkImage;

	public static JDImageProxy<?> floor_darkImage2;

	public static JDImageProxy<?> floor_darkImage3;

	public static JDImageProxy<?> floor_darkImage4;

	public static JDImageProxy<?> floor_darkImage5;

	public static JDImageProxy<?> floor_darkImage6;

	public static JDImageProxy<?> floor_darkImage7;

	public static JDImageProxy<?> floor_mediumImage;

	public static JDImageProxy<?> floor_mediumImage2;

	public static JDImageProxy<?> floor_mediumImage3;

	public static JDImageProxy<?> floor_mediumImage4;

	public static JDImageProxy<?> floor_mediumImage5;

	public static JDImageProxy<?> floor_mediumImage6;

	public static JDImageProxy<?> floor_mediumImage7;

	public static JDImageProxy<?> floorImage;

	public static JDImageProxy<?>[] floorImageArray = new JDImageProxy<?>[8];

	public static JDImageProxy<?>[] floorImage_darkArray = new JDImageProxy<?>[8];

	public static JDImageProxy<?>[] floorImage_mediumArray = new JDImageProxy<?>[8];

	public static JDImageProxy<?> floorImage2;

	public static JDImageProxy<?> floorImage3;

	public static JDImageProxy<?> floorImage4;

	public static JDImageProxy<?> floorImage5;

	public static JDImageProxy<?> floorImage6;

	public static JDImageProxy<?> floorImage7;

	public static JDImageProxy<?> fountainImage;
	public static JDImageProxy<?> saeuleImage;

	public static AnimationSetDirections ghul1_been_hit;

	public static AnimationSetDirections ghul1_pause = null;

	public static AnimationSetDirections ghul1_running = null;

	public static AnimationSetDirections ghul1_slays;

	public static AnimationSetDirections ghul1_sorcering = null;

	public static AnimationSetDirections ghul1_tipping_over;

	public static AnimationSetDirections ghul1_using = null;

	public static AnimationSetDirections ghul1_walking = null;

	public static JDImageProxy<?>[] ghulImage;

	public static JDImageProxy<?> graveImage;

	public static JDImageProxy<?> hand_greift1_Image;

	public static JDImageProxy<?> hand_zeigt1_Image;

	public static JDImageProxy<?> cursor_go_Image;

	public static JDImageProxy<?> helmetImage;

	public static JDImageProxy<?> keyImage;

	public static JDImageProxy<?> kugelImage;

	public static JDImageProxy<?> lanceImage;

	public static JDImageProxy<?> luzia_ball_greyImage;

	public static JDImageProxy<?> luzia_ball_redImage;

	public static JDImageProxy<?> luzia_hutImage;

	public static JDImageProxy<?> luziaImage;

	public static AnimationSetDirections mage_been_hit;

	public static AnimationSetDirections mage_pause;

	public static AnimationSetDirections mage_running;

	public static AnimationSetDirections mage_slays;

	public static AnimationSetDirections mage_sorcering;

	public static AnimationSetDirections mage_tipping_over;

	public static AnimationSetDirections mage_using;

	public static AnimationSetDirections mage_walking;

	public static JDImageProxy<?>[] mageImage;

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

	public static AnimationSetDirections lioness_been_hit;

	public static AnimationSetDirections lioness_pause = null;

	public static AnimationSetDirections lioness_running = null;

	public static AnimationSetDirections lioness_slays;

	public static AnimationSetDirections lioness_sorcering = null;

	public static AnimationSetDirections lioness_tipping_over;

	public static AnimationSetDirections lioness_using = null;

	public static AnimationSetDirections lioness_walking = null;

	public static AnimationSetDirections ddwarf_been_hit;

	public static AnimationSetDirections ddwarf_pause = null;

	public static AnimationSetDirections ddwarf_running = null;

	public static AnimationSetDirections ddwarf_slays;

	public static AnimationSetDirections ddwarf_sorcering = null;

	public static AnimationSetDirections ddwarf_tipping_over;

	public static AnimationSetDirections ddwarf_using = null;

	public static AnimationSetDirections ddwarf_walking = null;

	public static JDImageProxy<?>[] ogreImage;
	public static JDImageProxy<?>[] lionessImage;

	public static JDImageProxy<?>[] orcImage;

	public static JDImageProxy<?> pentagrammImage;

	public static JDImageProxy<?> potion_blueImage;

	public static JDImageProxy<?> potion_redImage;

	public static JDImageProxy<?> potion_greenImage;

	public static JDImageProxy<?> featherImage;

	public static DefaultAnimationSet puff;

	public static JDImageProxy<?> questionmark;

	public static JDImageProxy<?> repairImage;

	public static JDImageProxy<?> rune_blueImage;

	public static JDImageProxy<?> rune_greenImage;

	public static JDImageProxy<?> rune_redImage;

	public static JDImageProxy<?> rune_yellowImage;

	public static JDImageProxy<?> scrollImage;

	public static JDImageProxy<?> shieldImage;

	public static JDImageProxy<?> shrine_blackImage;

	public static JDImageProxy<?> shrine_blueImage;

	public static JDImageProxy<?> shrine_greenImage;

	public static JDImageProxy<?> shrine_lilaImage;

	public static JDImageProxy<?> shrine_redImage;

	public static JDImageProxy<?> shrine_small_blueImage;

	public static JDImageProxy<?> shrine_small_greenImage;

	public static JDImageProxy<?> shrine_small_redImage;

	public static JDImageProxy<?> shrine_small_yellowImage;

	public static JDImageProxy<?> shrine_turkisImage;

	public static JDImageProxy<?> shrine_whiteImage;

	public static JDImageProxy<?> shrine_yellowImage;

	public static AnimationSetDirections skel1_been_hit;

	public static AnimationSetDirections skel1_pause = null;

	public static AnimationSetDirections skel1_running = null;

	public static AnimationSetDirections skel1_slays;

	public static AnimationSetDirections skel1_sorcering = null;

	public static AnimationSetDirections skel1_tipping_over;

	public static AnimationSetDirections skel1_using = null;

	public static AnimationSetDirections skel1_walking = null;

	public static JDImageProxy<?> skel2Image;

	public static JDImageProxy<?>[] skelImage;

	public static JDImageProxy<?> sorcLabImage;

	public static JDImageProxy<?> spotImage;

	public static JDImageProxy<?> statueImage;

	public static JDImageProxy<?> swordImage;

	public static AnimationSetDirections thief_been_hit;

	public static AnimationSetDirections thief_pause;

	public static AnimationSetDirections thief_running;

	public static AnimationSetDirections thief_slays;

	public static AnimationSetDirections thief_sorcering;

	public static AnimationSetDirections thief_tipping_over;

	public static AnimationSetDirections thief_using;

	public static AnimationSetDirections thief_walking;

	public static JDImageProxy<?>[] thiefImage;

	public static JDImageProxy<?> traderImage;

	public static JDImageProxy<?> wall_southImage;

	// public static JDImageProxy<?> wallImage;

	public static JDImageProxy<?> wall_sidesImage;
	public static JDImageProxy<?> wall_northImage;

	public static AnimationSetDirections warrior_been_hit;

	public static AnimationSetDirections warrior_pause;

	public static AnimationSetDirections warrior_running;

	public static AnimationSetDirections warrior_slays;

	public static AnimationSetDirections warrior_sorcering;

	public static AnimationSetDirections warrior_tipping_over;

	public static AnimationSetDirections warrior_using;

	public static AnimationSetDirections warrior_walking;

	public static JDImageProxy<?>[] warriorImage;

	public static AnimationSetDirections wolf1_been_hit;

	public static AnimationSetDirections wolf1_pause = null;

	public static AnimationSetDirections wolf1_running = null;

	public static AnimationSetDirections wolf1_slays;

	public static AnimationSetDirections wolf1_sorcering = null;

	public static AnimationSetDirections wolf1_tipping_over;

	public static AnimationSetDirections wolf1_using = null;

	public static AnimationSetDirections wolf1_walking = null;

	public static JDImageProxy<?> wolf2Image;

	public static JDImageProxy<?>[] wolfImage;

	public static JDImageProxy<?> wolfknifeImage;

	public static JDImageProxy<?> xmasImage;

	public static JDImageProxy<?> paperBackground;

	public static JDImageProxy<?> border_double_left_upper_corner;
	public static JDImageProxy<?> border_double_left_lower_corner;
	public static JDImageProxy<?> border_double_right_upper_corner;
	public static JDImageProxy<?> border_double_right_lower_corner;
	public static JDImageProxy<?> border_double_top;
	public static JDImageProxy<?> border_double_bottom;
	public static JDImageProxy<?> border_double_left;
	public static JDImageProxy<?> border_double_right;

	public static JDImageProxy<?> health_bar_empty;
	public static JDImageProxy<?> health_bar_yellow;
	public static JDImageProxy<?> health_bar_red;
	public static JDImageProxy<?> health_bar_blue;

	public static JDImageProxy<?> inventory_figure_background;

	public static JDImageProxy<?> inventory_box_select;
	public static JDImageProxy<?> inventory_box_hover;
	public static JDImageProxy<?> inventory_box_normal;
	public static JDImageProxy<?> inventory_box_normalInactive;

	public static JDImageProxy<?> inventory_empty_helmet;
	public static JDImageProxy<?> inventory_empty_armor;
	public static JDImageProxy<?> inventory_empty_shield;
	public static JDImageProxy<?> inventory_empty_weapon;

	public static JDImageProxy<?> inventory_sword1;
	public static JDImageProxy<?> inventory_armor1;
	public static JDImageProxy<?> inventory_knife1;
	public static JDImageProxy<?> inventory_shield1;
	public static JDImageProxy<?> inventory_lance1;
	public static JDImageProxy<?> inventory_axe1;
	public static JDImageProxy<?> inventory_club1;
	public static JDImageProxy<?> inventory_helmet1;
	public static JDImageProxy<?> defenderLocation_active;
	public static JDImageProxy<?> defenderLocation_inactive;
	public static JDImageProxy<?> runeFinderLocation;

	public static JDImageProxy<?> lebenskugel;

	private static AnimationSetDirections load4Animations(
			AbstractImageLoader<?> a, String path, String pattern) {
		return load4Animations(a, path, pattern, 45);
	}

	private static AnimationSetDirections load4Animations(
			AbstractImageLoader<?> a, String path, String pattern, int stepDuration) {
		DefaultAnimationSet[] set = new DefaultAnimationSet[4];
		for (int i = 0; i < 4; i++) {
			JDImageProxy<?>[] loadedImages = ImageManagerUtils.loadArray(a, path, pattern, i + 1);
			set[i] = new DefaultAnimationSet(loadedImages,
					getArray(45, loadedImages.length));
		}
		return new AnimationSetDirections(set);
	}

	public static boolean imagesLoaded = false;

	public void loadImages() {

		if (!imagesLoaded) {
			long startTime = System.currentTimeMillis();
			Logger.getLogger(ImageManager.class.getName()).info("Start initializing images..");
			AbstractImageLoader<?> a = this.loader;

			puff = new DefaultAnimationSet(loadArray(a, "wolke", 8), getArray(25, 8));

			warrior_slays = load4Animations(a, "animation/warrior/", "warrior_attack_");
			Logger.getLogger(ImageManager.class.getName()).info("Initializing images: completed warrior_slays");

			thief_slays = load4Animations(a, "animation/thief/", "thief_attack_");
			druid_slays = load4Animations(a, "animation/druid/", "druid_attack_");
			mage_slays = load4Animations(a, "animation/mage/", "mage_attack_");

			warrior_been_hit = load4Animations(a, "animation/warrior/", "warrior_swordstan_treffer_");
			warrior_been_hit.addAudioClipHalfTime(AudioEffectsManager.HERO_HURT);
			warrior_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);

			thief_been_hit = load4Animations(a, "animation/thief/", "thief_treffer_");
			thief_been_hit.addAudioClipHalfTime(AudioEffectsManager.HERO_HURT);
			thief_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);

			druid_been_hit = load4Animations(a, "animation/druid/", "druid_been_hit_");
			druid_been_hit.addAudioClipHalfTime(AudioEffectsManager.HERO_HURT);
			druid_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);

			mage_been_hit = load4Animations(a, "animation/mage/", "mage_treffer_");
			mage_been_hit.addAudioClipHalfTime(AudioEffectsManager.HERO_HURT);
			mage_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);

			warrior_tipping_over = load4Animations(a, "animation/warrior/", "warrior_swordstan_kippt_um_", 60);
			thief_tipping_over = load4Animations(a, "animation/thief/", "thief_kippt_um_");
			druid_tipping_over = load4Animations(a, "animation/druid/", "druid_tipping_over_");
			mage_tipping_over = load4Animations(a, "animation/mage/", "mage_magier_45_kippt_um_", 60);

			warrior_walking = load4Animations(a, "animation/warrior/", "warrior_swordstan_laeuft_");
			thief_walking = load4Animations(a, "animation/thief/", "thief_laeuft_");
			druid_walking = load4Animations(a, "animation/druid/", "druid_walking_");
			mage_walking = load4Animations(a, "animation/mage/", "mage_laeuft_");

			warrior_using = load4Animations(a, "animation/warrior/", "warrior_swordstan_spricht_");
			thief_using = load4Animations(a, "animation/thief/", "thief_laeuft_");

			druid_using = load4Animations(a, "animation/druid/", "druid_talking_");
			mage_using = load4Animations(a, "animation/mage/", "mage_talking_");

			warrior_pause = load4Animations(a, "animation/warrior/", "warrior_stan_strickt_");
			thief_pause = load4Animations(a, "animation/thief/", "thief_laeuft_");

			druid_pause = load4Animations(a, "animation/druid/", "druid_paused_");
			mage_pause = load4Animations(a, "animation/mage/", "mage_liest_");

			warrior_sorcering = load4Animations(a, "animation/warrior/", "warrior_swordstan_spricht_");
			thief_sorcering = load4Animations(a, "animation/thief/", "thief_laeuft_");

			druid_sorcering = load4Animations(a, "animation/druid/", "druid_magic_spelling_");
			mage_sorcering = load4Animations(a, "animation/mage/", "mage_magieattack_");

			warrior_running = load4Animations(a, "animation/" + "warrior/", "warrior_swordstan_rennt_");
			thief_running = load4Animations(a, "animation/thief/", "thief_rennt_");
			druid_running = load4Animations(a, "animation/" + "druid/", "druid_running_");
			mage_running = load4Animations(a, "animation/" + "mage/", "mage_magieattack_");

			wolf1_been_hit = load4Animations(a, "animation/" + "wolf/", "wolf_been_hit_");
			wolf1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
			wolf1_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);

			wolf1_tipping_over = load4Animations(a, "animation/" + "wolf/", "wolf_tipping_over_", 60);
			wolf1_tipping_over.addAudioClip(AudioEffectsManager.WOLF_DIES, 1);

			wolf1_slays = load4Animations(a, "animation/" + "wolf/", "wolf_wolf_attack_");
			wolf1_slays.addAudioClip(AudioEffectsManager.WOLF_ATTACKS, 1);

			wolf1_walking = load4Animations(a, "animation/" + "wolf/", "wolf_rennt_");
			wolf1_running = load4Animations(a, "animation/" + "wolf/", "wolf_laeuft_");

			wolf1_pause = wolf1_walking;

			skel1_been_hit = load4Animations(a, "animation/" + "skel/", "skel_swordskel_treffer_");
			skel1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
			skel1_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);

			skel1_tipping_over = load4Animations(a, "animation/" + "skel/", "skel_zerfaellt_", 60);
			skel1_slays = load4Animations(a, "animation/" + "skel/", "skel_swordskel_attack_");
			skel1_walking = load4Animations(a, "animation/" + "skel/", "skel_swordskel_laeuft_");
			skel1_running = load4Animations(a, "animation/" + "skel/", "skel_swordskel_rennt_");

			skel1_pause = skel1_walking;

			ghul1_been_hit = load4Animations(a, "animation/" + "ghul/", "ghul_treffer_");
			ghul1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
			ghul1_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);

			ghul1_tipping_over = load4Animations(a, "animation/" + "ghul/", "ghul_mumie_zerfaellt_", 60);
			ghul1_slays = load4Animations(a, "animation/" + "ghul/", "ghul_mummy_45_attack_");

			ghul1_running = load4Animations(a, "animation/" + "ghul/", "ghul_mummy_rennt_");
			ghul1_walking = load4Animations(a, "animation/" + "ghul/", "ghul_mummy_45_laeuft_");
			ghul1_pause = ghul1_walking;

			ogre1_been_hit = load4Animations(a, "animation/" + "ogre/", "ogre_been_hit_");
			ogre1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
			ogre1_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);

			ogre1_tipping_over = load4Animations(a, "animation/" + "ogre/", "ogre_tipping_over_", 60);
			ogre1_slays = load4Animations(a, "animation/" + "ogre/", "ogre_attack_");
			ogre1_running = load4Animations(a, "animation/" + "ogre/", "ogre_running_");
			ogre1_walking = load4Animations(a, "animation/" + "ogre/", "ogre_walking_");
			ogre1_pause = ogre1_walking;

			orc1_been_hit = load4Animations(a, "animation/" + "orc/", "orc_been_hit_");
			orc1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
			orc1_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);

			orc1_tipping_over = load4Animations(a, "animation/" + "orc/", "orc_tipping_over_");
			orc1_slays = load4Animations(a, "animation/" + "orc/", "orc_attack_");
			orc1_walking = load4Animations(a, "animation/" + "orc/", "orc_walking_");
			orc1_running = load4Animations(a, "animation/" + "orc/", "orc_running_");
			orc1_pause = orc1_walking;

			spider1_been_hit = load4Animations(a, "animation/" + "spider/", "spider_been_hit_");
			spider1_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
			spider1_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);

			spider1_tipping_over = load4Animations(a, "animation/" + "spider/", "spider_tipping_over_");
			spider1_tipping_over.addAudioClip(AudioEffectsManager.SPIDER_DIES, 0);

			spider1_slays = load4Animations(a, "animation/" + "spider/", "spider_attack_");
			spider1_slays.addAudioClip(AudioEffectsManager.SPIDER_ATTACKS, 0);

			spider1_walking = load4Animations(a, "animation/" + "spider/", "spider_walking_");
			spider1_running = spider1_walking;
			spider1_pause = spider1_walking;

			lioness_been_hit = load4Animations(a, "animation/" + "lioness/", "been hit");
			lioness_been_hit.addAudioClipHalfTime(AudioEffectsManager.MONSTER_HURT);
			lioness_been_hit.addAudioClip(AudioEffectsManager.SMASH, 1);
			lioness_tipping_over = load4Animations(a, "animation/" + "lioness/", "tipping over", 60);
			lioness_tipping_over.addAudioClip(AudioEffectsManager.WOLF_DIES, 0);
			lioness_slays = load4Animations(a, "animation/" + "lioness/", "attack");
			lioness_slays.addAudioClip(AudioEffectsManager.WOLF_ATTACKS, 0);
			lioness_walking = load4Animations(a, "animation/" + "lioness/", "walking");
			lioness_running = load4Animations(a, "animation/" + "lioness/", "running");
			lioness_pause = load4Animations(a, "animation/" + "lioness/", "roaring");

			ddwarf_been_hit = load4Animations(a, "animation/" + "darkdwarf/", "been hit");
			ddwarf_tipping_over = load4Animations(a, "animation/" + "darkdwarf/", "tipping over", 60);
			ddwarf_slays = load4Animations(a, "animation/" + "darkdwarf/", "attack");
			ddwarf_walking = load4Animations(a, "animation/" + "darkdwarf/", "walking");
			ddwarf_running = load4Animations(a, "animation/" + "darkdwarf/", "running");
			ddwarf_pause = load4Animations(a, "animation/" + "darkdwarf/", "talking");

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
			lionessImage = makePics(lioness_walking);

			dead_dwarfImage = new JDImageProxy<>(a, "dead_dwarf.gif");
			dead_warriorImage = new JDImageProxy<>(a, "dead_stan.gif");
			dead_thiefImage = new JDImageProxy<>(a, "dead_blue_pirate.gif");
			dead_druidImage = new JDImageProxy<>(a, "dead_white_mage.gif");
			dead_mageImage = new JDImageProxy<>(a, "dead_black_mage.gif");

			chestImage = new JDImageProxy<>(a, "chest_cut_trans.gif");
			floorImage = new JDImageProxy<>(a, "boden5.gif");
			floor_darkImage = new JDImageProxy<>(a, "boden5_dark.gif");
			floor_mediumImage = new JDImageProxy<>(a, "boden5_dark1.gif");

			floorImage2 = new JDImageProxy<>(a, "boden3.gif");
			floor_darkImage2 = new JDImageProxy<>(a, "boden3_dark.gif");
			floor_mediumImage2 = new JDImageProxy<>(a, "boden3_dark1.gif");

			floorImage3 = new JDImageProxy<>(a, "boden4.gif");
			floor_darkImage3 = new JDImageProxy<>(a, "boden4_dark.gif");
			floor_mediumImage3 = new JDImageProxy<>(a, "boden4_dark1.gif");

			floorImage4 = new JDImageProxy<>(a, "boden6.gif");
			floor_darkImage4 = new JDImageProxy<>(a, "boden6_dark.gif");
			floor_mediumImage4 = new JDImageProxy<>(a, "boden6_dark1.gif");

			floorImage5 = new JDImageProxy<>(a, "boden7.gif");
			floor_darkImage5 = new JDImageProxy<>(a, "boden7_dark.gif");
			floor_mediumImage5 = new JDImageProxy<>(a, "boden7_dark1.gif");

			floorImage6 = new JDImageProxy<>(a, "boden8.gif");
			floor_darkImage6 = new JDImageProxy<>(a, "boden8_dark.gif");
			floor_mediumImage6 = new JDImageProxy<>(a, "boden8_dark1.gif");

			floorImage7 = new JDImageProxy<>(a, "boden9.gif");
			floor_darkImage7 = new JDImageProxy<>(a, "boden9_dark.gif");
			floor_mediumImage7 = new JDImageProxy<>(a, "boden9_dark1.gif");

			makeFloorArrays();

			wall_sidesImage = new JDImageProxy<>(a, "wand_seiten.gif");
			wall_northImage = new JDImageProxy<>(a, "wand_nord.gif");
			wall_southImage = new JDImageProxy<>(a, "wall_south.gif");

			door_north = new JDImageProxy<>(a, "tuer_nord.gif");
			door_north_solo = new JDImageProxy<>(a, "tuer_nord_solo.gif");

			door_north_none = new JDImageProxy<>(a, "tuer_nord_keine.gif");

			door_east = new JDImageProxy<>(a, "tuer_ost.gif");

			door_east_none = new JDImageProxy<>(a, "tuer_ost_keine.gif");

			door_west = new JDImageProxy<>(a, "tuer_west.gif");

			door_west_none = new JDImageProxy<>(a, "tuer_west_keine.gif");
			door_south_none = new JDImageProxy<>(a, "tuer_sued_keine.gif");
			door_south = new JDImageProxy<>(a, "tuer_sued.gif");
			door_south_lock = new JDImageProxy<>(a, "tuer_sued_schloss.gif");

			axeImage = new JDImageProxy<>(a, "axt.gif");
			swordImage = new JDImageProxy<>(a, "schwert.gif");
			lanceImage = new JDImageProxy<>(a, "lanze.gif");
			wolfknifeImage = new JDImageProxy<>(a, "wolfsmesser.gif");
			clubImage = new JDImageProxy<>(a, "knueppel.gif");
			scrollImage = new JDImageProxy<>(a, "scroll_blue.gif");
			documentImage = new JDImageProxy<>(a, "scroll_white.gif");
			potion_redImage = new JDImageProxy<>(a, "potion_rot.gif");
			potion_blueImage = new JDImageProxy<>(a, "potion_blue.gif");
			dustImage = new JDImageProxy<>(a, "dust.gif");
			armorImage = new JDImageProxy<>(a, "ruestung.gif");
			shieldImage = new JDImageProxy<>(a, "shield2.gif");
			helmetImage = new JDImageProxy<>(a, "helm.gif");
			bookImage = new JDImageProxy<>(a, "book_yellow.gif");
			keyImage = new JDImageProxy<>(a, "key.gif");
			falltuerImage = new JDImageProxy<>(a, "falltuer.gif");
			engelImage = new JDImageProxy<>(a, "engel2.gif");
			shrine_blueImage = new JDImageProxy<>(a, "shrine_blue.gif");
			shrine_redImage = new JDImageProxy<>(a, "shrine_red.gif");
			shrine_greenImage = new JDImageProxy<>(a, "shrine_green.gif");
			shrine_yellowImage = new JDImageProxy<>(a, "shrine_yellow.gif");
			shrine_whiteImage = new JDImageProxy<>(a, "shrine_white.gif");
			shrine_blackImage = new JDImageProxy<>(a, "shrine_black.gif");
			shrine_lilaImage = new JDImageProxy<>(a, "shrine_lila.gif");
			shrine_turkisImage = new JDImageProxy<>(a, "shrine_tuerkis.gif");
			shrine_small_redImage = new JDImageProxy<>(a, "shrine_small_red.gif");
			shrine_small_blueImage = new JDImageProxy<>(a,
					"shrine_small_blue.gif");
			shrine_small_yellowImage = new JDImageProxy<>(a,
					"shrine_small_yellow.gif");
			shrine_small_greenImage = new JDImageProxy<>(a,
					"shrine_small_green.gif");
			sorcLabImage = new JDImageProxy<>(a, "zauberlabor1.gif");
			amulettImage = new JDImageProxy<>(a, "amulett.gif");

			repairImage = new JDImageProxy<>(a, "amboss.gif");
			fountainImage = new JDImageProxy<>(a, "fountain.gif");
			saeuleImage = new JDImageProxy<>(a, "sauele.gif");
			statueImage = new JDImageProxy<>(a, "statue.gif");
			chest_lockImage = new JDImageProxy<>(a, "chest_schloss.gif");
			door_north_lock = new JDImageProxy<>(a, "tuer_nord_schloss.gif");
			door_north_lock_solo = new JDImageProxy<>(a, "tuer_nord_schloss_solo.gif");
			door_east_lock = new JDImageProxy<>(a, "tuer_ost_schloss.gif");
			door_west_lock = new JDImageProxy<>(a, "tuer_west_schloss.gif");
			graveImage = new JDImageProxy<>(a, "grave.gif");
			caveImage = new JDImageProxy<>(a, "cave.gif");
			traderImage = new JDImageProxy<>(a, "haendler2.gif");
			rune_redImage = new JDImageProxy<>(a, "rune_red.gif");
			rune_greenImage = new JDImageProxy<>(a, "rune_green.gif");
			rune_blueImage = new JDImageProxy<>(a, "rune_blue.gif");
			rune_yellowImage = new JDImageProxy<>(a, "rune_yellow.gif");
			cristall_redImage = new JDImageProxy<>(a, "kristall_rot.gif");
			cristall_greenImage = new JDImageProxy<>(a, "kristall_gruen.gif");
			cristall_blueImage = new JDImageProxy<>(a, "kristall_blau.gif");
			cristall_yellowImage = new JDImageProxy<>(a, "kristall_gelb.gif");
			spotImage = new JDImageProxy<>(a, "versteck.gif");
			hand_zeigt1_Image = new JDImageProxy<>(a, "zeigerhand-zeigt1.gif");
			cursor_go_Image = new JDImageProxy<>(a, "zeigerhand_go.gif");
			hand_greift1_Image = new JDImageProxy<>(a, "zeigerhand-greift1.gif");
			cursor_key_Image = new JDImageProxy<>(a, "zeiger_key.gif");
			cursor_key_not_Image = new JDImageProxy<>(a, "zeiger_key_nicht.gif");
			cursor_sword = new JDImageProxy<>(a, "zeigerhand-schwert.gif");
			cursor_clock = new JDImageProxy<>(a, "zeigerhand-sanduhr.gif");
			cursor_scout = new JDImageProxy<>(a, "zeigerhand-go_scout.gif");
			cursor_go_not_Image = new JDImageProxy<>(a, "zeigerhand_go_not.gif");
			cursor_wand = new JDImageProxy<>(a, "zeiger_zauberstab.gif");
			cursor_use_Image = new JDImageProxy<>(a, "zeigerhand_faust.gif");

			pentagrammImage = new JDImageProxy<>(a, "pentagramm.gif");
			darkMasterImage = new JDImageProxy<>(a, "mister_death.gif");
			luziaImage = new JDImageProxy<>(a, "luzia.gif");
			luzia_hutImage = new JDImageProxy<>(a, "luzia_hut.gif");
			kugelImage = new JDImageProxy<>(a, "kugel.gif");
			questionmark = new JDImageProxy<>(a, "fragezeichen.gif");
			xmasImage = new JDImageProxy<>(a, "xmas.gif");
			dark_dwarfImage = new JDImageProxy<>(a, "dark_dwarf.gif");
			firImage = new JDImageProxy<>(a, "growing_n0007b.gif");
			luzia_ball_greyImage = new JDImageProxy<>(a, "kugel_grau.gif");

			luzia_ball_redImage = new JDImageProxy<>(a, "kugel_rot.gif");

			fieldImage = new JDImageProxy<>(a, "field3.gif");
			woodTextureImage = new JDImageProxy<>(a, "theWood3.gif");

			featherImage = new JDImageProxy<>(a, "feder.gif");
			potion_greenImage = new JDImageProxy<>(a, "potion_green.gif");

			paperBackground = new JDImageProxy<>(a, "paper_background.gif");

			border_double_left_upper_corner = new JDImageProxy<>(a, "border_double_left_upper_corner.gif");
			border_double_left_lower_corner = new JDImageProxy<>(a, "border_double_left_lower_corner.gif");
			border_double_right_upper_corner = new JDImageProxy<>(a, "border_double_right_upper_corner.gif");
			border_double_right_lower_corner = new JDImageProxy<>(a, "border_double_right_lower_corner.gif");
			border_double_top = new JDImageProxy<>(a, "border_double_top.gif");
			border_double_bottom = new JDImageProxy<>(a, "border_double_bottom.gif");
			border_double_left = new JDImageProxy<>(a, "border_double_left.gif");
			border_double_right = new JDImageProxy<>(a, "border_double_right.gif");

			health_bar_empty = new JDImageProxy<>(a, "guiItems/health_bar_empty.gif");
			health_bar_red = new JDImageProxy<>(a, "guiItems/health_bar_bare_red.gif");
			health_bar_yellow = new JDImageProxy<>(a, "guiItems/health_bar_bare_yellow.gif");
			health_bar_blue = new JDImageProxy<>(a, "guiItems/health_bar_bare_blue.gif");

			inventory_figure_background = new JDImageProxy<>(a,
					"figure-shadow1.gif");

			inventory_box_select = new JDImageProxy<>(a, "boxSelect.gif");
			inventory_box_hover = new JDImageProxy<>(a, "boxHover.gif");
			inventory_box_normal = new JDImageProxy<>(a, "boxNormal.gif");
			inventory_box_normalInactive = new JDImageProxy<>(a, "boxNormalBW.gif");

			inventory_empty_helmet = new JDImageProxy<>(a, "guiItems/helmet.gif");
			inventory_empty_armor = new JDImageProxy<>(a, "guiItems/armor.gif");
			inventory_empty_shield = new JDImageProxy<>(a, "guiItems/shield.gif");
			inventory_empty_weapon = new JDImageProxy<>(a, "guiItems/weapon.gif");

			inventory_sword1 = new JDImageProxy<>(a, "guiItems/shortsword.gif");
			inventory_armor1 = new JDImageProxy<>(a, "guiItems/cloth_chest1.gif");
			inventory_knife1 = new JDImageProxy<>(a, "guiItems/dagger.gif");
			inventory_shield1 = new JDImageProxy<>(a, "guiItems/buckler.gif");
			inventory_lance1 = new JDImageProxy<>(a, "guiItems/wand.gif");
			inventory_axe1 = new JDImageProxy<>(a, "guiItems/axe1.gif");
			inventory_club1 = new JDImageProxy<>(a, "guiItems/club1.gif");
			inventory_helmet1 = new JDImageProxy<>(a, "guiItems/helmet1.gif");

			deathImage = new JDImageProxy<>(a, "tot1.gif");

			lebenskugel = new JDImageProxy<>(a, "lebenskugel.gif");

			button1 = new JDImageProxy<>(a, "button1.gif");

			defenderLocation_active = new JDImageProxy<>(a, "DefenderLocationActive.gif");
			defenderLocation_inactive = new JDImageProxy<>(a, "DefenderLocationInactive.gif");
			runeFinderLocation = new JDImageProxy<>(a, "RuneFinderLocation.gif");

			createItemClassMap(a);
			createHeroAnimationMap(a);
			createMonsterAnimationMap(a);
			createShrineClassMap();

			Logger.getLogger(ImageManager.class.getName())
					.info("Completed initializing images in: " + Long.toString(System.currentTimeMillis() - startTime));
		}

		imagesLoaded = true;
	}

	private static JDImageProxy<?>[] makePics(AnimationSetDirections a) {
		DefaultAnimationSet[] sets = a.getAnimations();
		JDImageProxy<?> ims[] = new JDImageProxy<?>[sets.length];
		for (int i = 0; i < sets.length; i++) {
			ims[i] = sets[i].getImagesNr(0);
		}
		return ims;
	}

	private static JDImageProxy<?>[] loadArray(AbstractImageLoader a,
											   String path, int cnt) {
		JDImageProxy<?>[] ims = new JDImageProxy[cnt];
		for (int i = 0; i < cnt; i++) {

			JDImageProxy<?> im = new JDImageProxy<>(path + Integer.toString(i)
					+ ".GIF", a);

			ims[i] = im;
			if (ims[i] == null) {

				Log.warning("Bild nicht geladen: " + path
						+ Integer.toString(i) + ".GIF");
				return null;
			}
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

	private static final Map<FigurePresentation, CharacterAnimationSet> figureAnimationMap = new HashMap<>();

	private static void createMonsterAnimationMap(AbstractImageLoader<?> a) {
		figureAnimationMap.put(FigurePresentation.Skeleton, new DefaultCharacterAnimationSet());
		figureAnimationMap.put(FigurePresentation.Orc, new DefaultCharacterAnimationSet());
		figureAnimationMap.put(FigurePresentation.WolfGrey, new DefaultCharacterAnimationSet());
		figureAnimationMap.put(FigurePresentation.Troll, new DefaultCharacterAnimationSet());
		figureAnimationMap.put(FigurePresentation.Ghul, new DefaultCharacterAnimationSet());
		figureAnimationMap.put(FigurePresentation.Spider, new DefaultCharacterAnimationSet());
		figureAnimationMap.put(FigurePresentation.Lioness, new DefaultCharacterAnimationSet());
		figureAnimationMap.put(FigurePresentation.DarkDwarf, new CharacterAnimationSetBuilder("darkdwarf", a).build());

		figureAnimationMap.get(FigurePresentation.Skeleton).put(Motion.BeingHit, ImageManager.skel1_been_hit);
		figureAnimationMap.get(FigurePresentation.Skeleton).put(Motion.Pause, ImageManager.skel1_pause);
		figureAnimationMap.get(FigurePresentation.Skeleton).put(Motion.TippingOver, ImageManager.skel1_tipping_over);
		figureAnimationMap.get(FigurePresentation.Skeleton).put(Motion.Walking, ImageManager.skel1_walking);
		figureAnimationMap.get(FigurePresentation.Skeleton).put(Motion.Running, ImageManager.skel1_running);
		figureAnimationMap.get(FigurePresentation.Skeleton).put(Motion.Using, ImageManager.skel1_using);
		figureAnimationMap.get(FigurePresentation.Skeleton).put(Motion.Slaying, ImageManager.skel1_slays);
		figureAnimationMap.get(FigurePresentation.Skeleton).put(Motion.Sorcering, ImageManager.skel1_sorcering);

		figureAnimationMap.get(FigurePresentation.Orc).put(Motion.BeingHit, ImageManager.orc1_been_hit);
		figureAnimationMap.get(FigurePresentation.Orc).put(Motion.Pause, ImageManager.orc1_pause);
		figureAnimationMap.get(FigurePresentation.Orc).put(Motion.TippingOver, ImageManager.orc1_tipping_over);
		figureAnimationMap.get(FigurePresentation.Orc).put(Motion.Walking, ImageManager.orc1_walking);
		figureAnimationMap.get(FigurePresentation.Orc).put(Motion.Running, ImageManager.orc1_running);
		figureAnimationMap.get(FigurePresentation.Orc).put(Motion.Using, ImageManager.orc1_using);
		figureAnimationMap.get(FigurePresentation.Orc).put(Motion.Slaying, ImageManager.orc1_slays);
		figureAnimationMap.get(FigurePresentation.Orc).put(Motion.Sorcering, ImageManager.orc1_sorcering);

		figureAnimationMap.get(FigurePresentation.WolfGrey).put(Motion.BeingHit, ImageManager.wolf1_been_hit);
		figureAnimationMap.get(FigurePresentation.WolfGrey).put(Motion.Pause, ImageManager.wolf1_pause);
		figureAnimationMap.get(FigurePresentation.WolfGrey).put(Motion.TippingOver, ImageManager.wolf1_tipping_over);
		figureAnimationMap.get(FigurePresentation.WolfGrey).put(Motion.Walking, ImageManager.wolf1_walking);
		figureAnimationMap.get(FigurePresentation.WolfGrey).put(Motion.Running, ImageManager.wolf1_running);
		figureAnimationMap.get(FigurePresentation.WolfGrey).put(Motion.Using, ImageManager.wolf1_using);
		figureAnimationMap.get(FigurePresentation.WolfGrey).put(Motion.Slaying, ImageManager.wolf1_slays);
		figureAnimationMap.get(FigurePresentation.WolfGrey).put(Motion.Sorcering, ImageManager.wolf1_sorcering);

		figureAnimationMap.get(FigurePresentation.Troll).put(Motion.BeingHit, ImageManager.ogre1_been_hit);
		figureAnimationMap.get(FigurePresentation.Troll).put(Motion.Pause, ImageManager.ogre1_pause);
		figureAnimationMap.get(FigurePresentation.Troll).put(Motion.TippingOver, ImageManager.ogre1_tipping_over);
		figureAnimationMap.get(FigurePresentation.Troll).put(Motion.Walking, ImageManager.ogre1_walking);
		figureAnimationMap.get(FigurePresentation.Troll).put(Motion.Running, ImageManager.ogre1_running);
		figureAnimationMap.get(FigurePresentation.Troll).put(Motion.Using, ImageManager.ogre1_using);
		figureAnimationMap.get(FigurePresentation.Troll).put(Motion.Slaying, ImageManager.ogre1_slays);
		figureAnimationMap.get(FigurePresentation.Troll).put(Motion.Sorcering, ImageManager.ogre1_sorcering);

		figureAnimationMap.get(FigurePresentation.Ghul).put(Motion.BeingHit, ImageManager.ghul1_been_hit);
		figureAnimationMap.get(FigurePresentation.Ghul).put(Motion.Pause, ImageManager.ghul1_pause);
		figureAnimationMap.get(FigurePresentation.Ghul).put(Motion.TippingOver, ImageManager.ghul1_tipping_over);
		figureAnimationMap.get(FigurePresentation.Ghul).put(Motion.Walking, ImageManager.ghul1_walking);
		figureAnimationMap.get(FigurePresentation.Ghul).put(Motion.Running, ImageManager.ghul1_running);
		figureAnimationMap.get(FigurePresentation.Ghul).put(Motion.Using, ImageManager.ghul1_using);
		figureAnimationMap.get(FigurePresentation.Ghul).put(Motion.Slaying, ImageManager.ghul1_slays);
		figureAnimationMap.get(FigurePresentation.Ghul).put(Motion.Sorcering, ImageManager.ghul1_sorcering);

		figureAnimationMap.get(FigurePresentation.Spider).put(Motion.BeingHit, ImageManager.spider1_been_hit);
		figureAnimationMap.get(FigurePresentation.Spider).put(Motion.Pause, ImageManager.spider1_pause);
		figureAnimationMap.get(FigurePresentation.Spider).put(Motion.TippingOver, ImageManager.spider1_tipping_over);
		figureAnimationMap.get(FigurePresentation.Spider).put(Motion.Walking, ImageManager.spider1_walking);
		figureAnimationMap.get(FigurePresentation.Spider).put(Motion.Running, ImageManager.spider1_running);
		figureAnimationMap.get(FigurePresentation.Spider).put(Motion.Using, ImageManager.spider1_using);
		figureAnimationMap.get(FigurePresentation.Spider).put(Motion.Slaying, ImageManager.spider1_slays);
		figureAnimationMap.get(FigurePresentation.Spider).put(Motion.Sorcering, ImageManager.spider1_sorcering);

		figureAnimationMap.get(FigurePresentation.Lioness).put(Motion.BeingHit, ImageManager.lioness_been_hit);
		figureAnimationMap.get(FigurePresentation.Lioness).put(Motion.Pause, ImageManager.lioness_pause);
		figureAnimationMap.get(FigurePresentation.Lioness).put(Motion.TippingOver, ImageManager.lioness_tipping_over);
		figureAnimationMap.get(FigurePresentation.Lioness).put(Motion.Walking, ImageManager.lioness_walking);
		figureAnimationMap.get(FigurePresentation.Lioness).put(Motion.Running, ImageManager.lioness_running);
		figureAnimationMap.get(FigurePresentation.Lioness).put(Motion.Using, ImageManager.lioness_using);
		figureAnimationMap.get(FigurePresentation.Lioness).put(Motion.Slaying, ImageManager.lioness_slays);
		figureAnimationMap.get(FigurePresentation.Lioness).put(Motion.Sorcering, ImageManager.lioness_sorcering);
	}

	//private static final Map<FigurePresentation, CharacterAnimationSet> heroAnimationMap = new HashMap<>();

	private static void createHeroAnimationMap(AbstractImageLoader<?> a) {
		figureAnimationMap.put(FigurePresentation.Warrior, new DefaultCharacterAnimationSet());
		figureAnimationMap.put(FigurePresentation.Sailor, new DefaultCharacterAnimationSet());
		figureAnimationMap.put(FigurePresentation.Druid, new DefaultCharacterAnimationSet());
		//heroAnimationMap.put(FigurePresentation.Druid, new CharacterAnimationSetBuilder(FigurePresentation.Druid.getFilepath(), a).build());
		figureAnimationMap.put(FigurePresentation.Mage, new DefaultCharacterAnimationSet());

		/*
		for the figures treated below, the filename pattern do not match the pattern defined in the CharacterAnimationSetBuilder
		therefore they have to be specified explicitely.
		 */

		figureAnimationMap.get(FigurePresentation.Warrior).put(Motion.BeingHit, ImageManager.warrior_been_hit);
		figureAnimationMap.get(FigurePresentation.Warrior).put(Motion.Pause, ImageManager.warrior_pause);
		figureAnimationMap.get(FigurePresentation.Warrior).put(Motion.TippingOver, ImageManager.warrior_tipping_over);
		figureAnimationMap.get(FigurePresentation.Warrior).put(Motion.Walking, ImageManager.warrior_walking);
		figureAnimationMap.get(FigurePresentation.Warrior).put(Motion.Running, ImageManager.warrior_running);
		figureAnimationMap.get(FigurePresentation.Warrior).put(Motion.Using, ImageManager.warrior_using);
		figureAnimationMap.get(FigurePresentation.Warrior).put(Motion.Slaying, ImageManager.warrior_slays);
		figureAnimationMap.get(FigurePresentation.Warrior).put(Motion.Sorcering, ImageManager.warrior_sorcering);

		figureAnimationMap.get(FigurePresentation.Sailor).put(Motion.BeingHit, ImageManager.thief_been_hit);
		figureAnimationMap.get(FigurePresentation.Sailor).put(Motion.Pause, ImageManager.thief_pause);
		figureAnimationMap.get(FigurePresentation.Sailor).put(Motion.TippingOver, ImageManager.thief_tipping_over);
		figureAnimationMap.get(FigurePresentation.Sailor).put(Motion.Walking, ImageManager.thief_walking);
		figureAnimationMap.get(FigurePresentation.Sailor).put(Motion.Running, ImageManager.thief_running);
		figureAnimationMap.get(FigurePresentation.Sailor).put(Motion.Using, ImageManager.thief_using);
		figureAnimationMap.get(FigurePresentation.Sailor).put(Motion.Slaying, ImageManager.thief_slays);
		figureAnimationMap.get(FigurePresentation.Sailor).put(Motion.Sorcering, ImageManager.thief_sorcering);

		figureAnimationMap.get(FigurePresentation.Druid).put(Motion.BeingHit, ImageManager.druid_been_hit);
		figureAnimationMap.get(FigurePresentation.Druid).put(Motion.Pause, ImageManager.druid_pause);
		figureAnimationMap.get(FigurePresentation.Druid).put(Motion.TippingOver, ImageManager.druid_tipping_over);
		figureAnimationMap.get(FigurePresentation.Druid).put(Motion.Walking, ImageManager.druid_walking);
		figureAnimationMap.get(FigurePresentation.Druid).put(Motion.Running, ImageManager.druid_running);
		figureAnimationMap.get(FigurePresentation.Druid).put(Motion.Using, ImageManager.druid_using);
		figureAnimationMap.get(FigurePresentation.Druid).put(Motion.Slaying, ImageManager.druid_slays);
		figureAnimationMap.get(FigurePresentation.Druid).put(Motion.Sorcering, ImageManager.druid_sorcering);

		figureAnimationMap.get(FigurePresentation.Mage).put(Motion.BeingHit, ImageManager.mage_been_hit);
		figureAnimationMap.get(FigurePresentation.Mage).put(Motion.Pause, ImageManager.mage_pause);
		figureAnimationMap.get(FigurePresentation.Mage).put(Motion.TippingOver, ImageManager.mage_tipping_over);
		figureAnimationMap.get(FigurePresentation.Mage).put(Motion.Walking, ImageManager.mage_walking);
		figureAnimationMap.get(FigurePresentation.Mage).put(Motion.Running, ImageManager.mage_running);
		figureAnimationMap.get(FigurePresentation.Mage).put(Motion.Using, ImageManager.mage_using);
		figureAnimationMap.get(FigurePresentation.Mage).put(Motion.Slaying, ImageManager.mage_slays);
		figureAnimationMap.get(FigurePresentation.Mage).put(Motion.Sorcering, ImageManager.mage_sorcering);
	}

	public static DefaultAnimationSet getAnimationSet(FigureInfo info, Motion motion, RouteInstruction.Direction direction) {
		FigurePresentation figurePresentation = info.getFigurePresentation();
		if (figurePresentation == null) {
			Log.severe("Unknown FigureInfo for AnimationSet: " + info);
			return null;
		}
		return ImageManager.getAnimationSet(figurePresentation, motion, direction);
	}

	public static DefaultAnimationSet getAnimationSet(FigurePresentation figurePresentation, Motion motion, RouteInstruction.Direction direction) {
		if (figureAnimationMap.containsKey(figurePresentation)) {
			CharacterAnimationSet motionAnimationSetDirectionsMap = figureAnimationMap.get(figurePresentation);
			if (motionAnimationSetDirectionsMap != null && motionAnimationSetDirectionsMap.containsMotion(motion)) {
				return motionAnimationSetDirectionsMap.getAnimationSet(motion, direction);
			}
		}
		return null;
	}

	public static Map<Class<? extends Location>, JDImageProxy<?>> shrineMap = new HashMap<>();

	private void createShrineClassMap() {
		shrineMap.put(HealthFountain.class, ImageManager.fountainImage);
		shrineMap.put(Statue.class, ImageManager.statueImage);
		shrineMap.put(Angel.class, ImageManager.engelImage);
		shrineMap.put(SorcerLab.class, ImageManager.sorcLabImage);
		shrineMap.put(Trader.class, ImageManager.traderImage);
		//shrineMap.put(QuestShrine.class, ImageManager.shrine_blackImage);
		shrineMap.put(RevealMapShrine.class, ImageManager.shrine_blackImage);
		//shrineMap.put(Xmas.class, ImageManager.xmasImage);
		//shrineMap.put(DarkMasterShrine.class, ImageManager.pentagrammImage);
		shrineMap.put(LevelExit.class, ImageManager.falltuerImage);
		shrineMap.put(ScoutShrine.class, ImageManager.saeuleImage);
		shrineMap.put(Corpse.class, ImageManager.dead_dwarfImage);
		shrineMap.put(MoonRuneFinderShrine.class, ImageManager.runeFinderLocation);
		//shrineMap.put(RuneFinder.class, ImageManager.shrine_small_yellowImage);
		shrineMap.put(DefenderLocation.class, ImageManager.defenderLocation_inactive);
	}

	public static JDImageProxy<?> getImage(DoorInfo s) {
		if (s.hasLock()) {
			return ImageManager.door_north_lock_solo;
		}
		else {
			return ImageManager.door_north_solo;
		}
	}

	public static JDImageProxy<?> getImage(ChestInfo s) {
		if (s.hasLock()) {
			return ImageManager.chest_lockImage;
		}
		else {
			return ImageManager.chestImage;
		}
	}

	public static JDImageProxy<?> getImage(Class<? extends Location> s) {
		return shrineMap.get(s);
	}

	public static JDImageProxy<?> getImage(FigureInfo figure, RouteInstruction.Direction dir) {
		if(figure == null) {
			throw new IllegalArgumentException("figure may not be null;");
		}
		FigurePresentation figurePresentation = figure.getFigurePresentation();
		CharacterAnimationSet characterAnimationSet = figureAnimationMap.get(figurePresentation);
		if(characterAnimationSet != null) {
			DefaultAnimationSet animationSet = characterAnimationSet
					.getAnimationSet(Motion.Walking, dir);
			if (animationSet != null) {
				return animationSet.getImagesNr(0);
			}
		}

		// todo: create animations?
		if (figurePresentation == FigurePresentation.Fir) {
			return ImageManager.firImage;
		}
		// fallback to show
		return ImageManager.engelImage;
	}

	private static Map<Class<? extends Item>, JDImageProxy<?>> itemMap = new HashMap<>();

	private void createItemClassMap(AbstractImageLoader loader) {
		itemMap.put(DustItem.class, ImageManager.dustImage);
		itemMap.put(Sword.class, ImageManager.swordImage);
		itemMap.put(Axe.class, ImageManager.axeImage);
		itemMap.put(Club.class, ImageManager.clubImage);
		itemMap.put(Lance.class, ImageManager.lanceImage);
		itemMap.put(Wolfknife.class, ImageManager.wolfknifeImage);
		itemMap.put(Armor.class, ImageManager.armorImage);
		itemMap.put(Shield.class, ImageManager.shieldImage);
		itemMap.put(Helmet.class, ImageManager.helmetImage);
		itemMap.put(InfoScroll.class, ImageManager.documentImage);
		itemMap.put(AncientMapFragment.class, ImageManager.documentImage);
		itemMap.put(Feather.class, ImageManager.featherImage);
		itemMap.put(Incense.class, ImageManager.potion_greenImage);
		itemMap.put(Key.class, ImageManager.keyImage);
		itemMap.put(DarkMasterKey.class, ImageManager.cristall_redImage);
		itemMap.put(Book.class, ImageManager.bookImage);
		itemMap.put(Thing.class, ImageManager.amulettImage);
		itemMap.put(MoonRune.class, new JDImageProxy<>("kristall_blau.gif", loader));
	}

	public static JDImageProxy<?> getImage(ItemInfo item) {
		Class itemClazz = item.getItemClass();
		if (itemMap.get(itemClazz) != null) {
			return itemMap.get(itemClazz);
		}

		Set<Class<? extends Item>> classes = itemMap.keySet();
		for (Class<? extends Item> aClass : classes) {
			if (Clazz.isAssignableFrom(aClass, itemClazz)) {
				return itemMap.get(aClass);
			}
		}
		JDImageProxy<?> im = null;
		if (Clazz.isAssignableFrom(AttrPotion.class, item.getItemClass())) {
			if (((item).getItemKey() == Item.ITEM_KEY_HEALPOTION)) {
				im = ImageManager.potion_redImage;
			}
			else {
				im = ImageManager.potion_blueImage;
			}
		}
		else if (item.getItemClass().equals(Rune.class)) {
			if ((item).toString().indexOf('J') != -1) {
				im = ImageManager.rune_yellowImage;
			}
			else if ((item).toString().indexOf('A') != -1) {
				im = ImageManager.rune_greenImage;
			}
			else if ((item).toString().indexOf('V') != -1) {
				im = ImageManager.rune_redImage;
			}
		}
		else {
			im = ImageManager.questionmark;
		}
		return im;
	}

	interface CharacterAnimationSet {
		DefaultAnimationSet getAnimationSet(Motion motion, RouteInstruction.Direction direction);

		boolean containsMotion(Motion motion);

		// TODO: method should not be in this interface
		void put(Motion motion, AnimationSetDirections ani);
	}

	static class CharacterAnimationSetBuilder {

		/*
		this builder only works if the image files in the folder are named according to the naming pattern
		given by the motionFilenameMap!
		 */

		private final String foldername;
		private final AbstractImageLoader<?> loader;

		private final Map<Motion, String> motionFilenameMap;

		public CharacterAnimationSetBuilder(String foldername, AbstractImageLoader<?> loader) {
			this.foldername = foldername;
			this.loader = loader;
			motionFilenameMap = new HashMap<>();
			motionFilenameMap.put(Motion.Slaying, "attack");
			motionFilenameMap.put(Motion.BeingHit, "been hit");
			motionFilenameMap.put(Motion.Using, "looking");
			motionFilenameMap.put(Motion.Running, "running");
			motionFilenameMap.put(Motion.Sorcering, "talking");
			motionFilenameMap.put(Motion.TippingOver, "tipping over");
			motionFilenameMap.put(Motion.Walking, "walking");
		}

		public CharacterAnimationSetBuilder(String foldername, AbstractImageLoader<?> loader, Map<Motion, String> map) {
			this.foldername = foldername;
			this.motionFilenameMap = map;
			this.loader = loader;
		}

		public CharacterAnimationSet build() {
			DefaultCharacterAnimationSet result = new DefaultCharacterAnimationSet();
			Set<Motion> motions = motionFilenameMap.keySet();
			for (Motion motion : motions) {
				AnimationSetDirections set = load4Animations(loader, foldername, motionFilenameMap
						.get(motion));
				if (set != null) {
					result.put(motion, set);
				}
			}
			return result;
		}
	}

	static class DefaultCharacterAnimationSet implements CharacterAnimationSet {

		private final Map<Motion, AnimationSetDirections> animations;

		public DefaultCharacterAnimationSet(Map<Motion, AnimationSetDirections> animations) {
			this.animations = animations;
		}

		public DefaultCharacterAnimationSet() {
			this.animations = new HashMap<>();
		}

		@Override
		public void put(Motion motion, AnimationSetDirections ani) {
			animations.put(motion, ani);
		}

		@Override
		public DefaultAnimationSet getAnimationSet(Motion motion, RouteInstruction.Direction direction) {
			AnimationSetDirections animationSetDirections = animations.get(motion);
			if (animationSetDirections != null) {
				return animationSetDirections.get(direction);
			}
			else {
				return null;
			}
		}

		@Override
		public boolean containsMotion(Motion motion) {
			return animations.containsKey(motion);
		}
	}
}
