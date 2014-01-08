package animation;

import figure.FigureInfo;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import graphics.ImageManager;

public class AnimationUtils {

	public static AnimationSet getFigure_pause(FigureInfo info) {
		int dir = info.getLookDir();

		if (info instanceof MonsterInfo) {
			int mClass = ((MonsterInfo) info).getMonsterClass();
			if (mClass == Monster.WOLF) {
				if (info.getLevel() == 1) {

					return ImageManager.wolf1_pause.get(dir - 1);
				}
			}

			if (mClass == Monster.SKELETON) {
				if (info.getLevel() == 1) {
					return ImageManager.skel1_pause.get(dir - 1);
				}
			}
			if (mClass == Monster.GHUL) {
				if (info.getLevel() == 1) {
					return ImageManager.ghul1_pause.get(dir - 1);
				}
			}
			if (mClass == Monster.OGRE) {
				if (info.getLevel() == 1) {
					return ImageManager.ogre1_pause.get(dir - 1);
				}
			}
			if (mClass == Monster.BEAR) {
				if (info.getLevel() == 1) {
					return ImageManager.spider1_pause.get(dir - 1);
				}
			}
			if (mClass == Monster.ORC) {
				if (info.getLevel() == 1) {
					return ImageManager.orc1_pause.get(dir - 1);
				}
			}
		}
		if (info instanceof HeroInfo) {
			int heroClass = ((HeroInfo) info).getHeroCode();
			if (heroClass == Hero.HEROCODE_WARRIOR) {
				return ImageManager.getWarrior_pause(dir);
			}
			if (heroClass == Hero.HEROCODE_DRUID) {
				return ImageManager.getDruid_pause(dir);
			}
			if (heroClass == Hero.HEROCODE_HUNTER) {
				return ImageManager.getThief_pause(dir);
			}
			if (heroClass == Hero.HEROCODE_MAGE) {
				return ImageManager.getMage_pause(dir);
			}
		}
		return null;

	}

	public static AnimationSet getFigure_been_hit(FigureInfo info) {
		int dir = info.getLookDir();
		if (info instanceof MonsterInfo) {
			int mClass = ((MonsterInfo) info).getMonsterClass();
			if (mClass == Monster.WOLF) {
				if (info.getLevel() == 1) {
					return ImageManager.wolf1_been_hit.get(dir - 1);
				}
			}

			if (mClass == Monster.SKELETON) {
				if (info.getLevel() == 1) {
					return ImageManager.skel1_been_hit.get(dir - 1);
				}
			}
			if (mClass == Monster.GHUL) {
				if (info.getLevel() == 1) {
					return ImageManager.ghul1_been_hit.get(dir - 1);
				}
			}
			if (mClass == Monster.OGRE) {
				if (info.getLevel() == 1) {
					return ImageManager.ogre1_been_hit.get(dir - 1);
				}
			}
			if (mClass == Monster.BEAR) {
				if (info.getLevel() == 1) {
					return ImageManager.spider1_been_hit.get(dir - 1);
				}
			}
			if (mClass == Monster.ORC) {
				if (info.getLevel() == 1) {
					return ImageManager.orc1_been_hit.get(dir - 1);
				}
			}
		}
		if (info instanceof HeroInfo) {
			int heroClass = ((HeroInfo) info).getHeroCode();
			if (heroClass == Hero.HEROCODE_WARRIOR) {
				return ImageManager.getWarrior_been_hit(dir);
			}
			if (heroClass == Hero.HEROCODE_DRUID) {
				return ImageManager.getDruid_been_hit(dir);
			}
			if (heroClass == Hero.HEROCODE_HUNTER) {
				return ImageManager.getThief_been_hit(dir);
			}
			if (heroClass == Hero.HEROCODE_MAGE) {
				return ImageManager.getMage_been_hit(dir);
			}
		}
		return null;

	}

	public static AnimationSet getFigure_walking(FigureInfo info) {
		int dir = info.getLookDir();

		if (info instanceof MonsterInfo) {
			int mClass = ((MonsterInfo) info).getMonsterClass();
			if (mClass == Monster.WOLF) {
				if (info.getLevel() == 1) {
					return ImageManager.wolf1_walking.get(dir - 1);
				}
			}

			if (mClass == Monster.SKELETON) {
				if (info.getLevel() == 1) {
					return ImageManager.skel1_walking.get(dir - 1);
				}
			}
			if (mClass == Monster.GHUL) {
				if (info.getLevel() == 1) {
					return ImageManager.ghul1_walking.get(dir - 1);
				}
			}
			if (mClass == Monster.OGRE) {
				if (info.getLevel() == 1) {
					return ImageManager.ogre1_walking.get(dir - 1);
				}
			}
			if (mClass == Monster.BEAR) {
				if (info.getLevel() == 1) {
					return ImageManager.spider1_walking.get(dir - 1);
				}
			}
			if (mClass == Monster.ORC) {
				if (info.getLevel() == 1) {
					return ImageManager.orc1_walking.get(dir - 1);
				}
			}
		}
		if (info instanceof HeroInfo) {
			int heroClass = ((HeroInfo) info).getHeroCode();
			if (heroClass == Hero.HEROCODE_WARRIOR) {
				return ImageManager.getWarrior_walking(dir);
			}
			if (heroClass == Hero.HEROCODE_DRUID) {
				return ImageManager.getDruid_walking(dir);
			}
			if (heroClass == Hero.HEROCODE_HUNTER) {
				return ImageManager.getThief_walking(dir);
			}
			if (heroClass == Hero.HEROCODE_MAGE) {
				return ImageManager.getMage_walking(dir);
			}

		}
		return null;

	}

	public static AnimationSet getFigure_running(FigureInfo info) {
		int dir = info.getLookDir();
		if (info instanceof MonsterInfo) {
			int mClass = ((MonsterInfo) info).getMonsterClass();
			if (mClass == Monster.WOLF) {
				if (info.getLevel() == 1) {
					return ImageManager.wolf1_running.get(dir - 1);
				}
			}

			if (mClass == Monster.SKELETON) {
				if (info.getLevel() == 1) {
					return ImageManager.skel1_running.get(dir - 1);
				}
			}
			if (mClass == Monster.GHUL) {
				if (info.getLevel() == 1) {
					return ImageManager.ghul1_running.get(dir - 1);
				}
			}
			if (mClass == Monster.OGRE) {
				if (info.getLevel() == 1) {
					return ImageManager.ogre1_running.get(dir - 1);
				}
			}
			if (mClass == Monster.BEAR) {
				if (info.getLevel() == 1) {
					return ImageManager.spider1_running.get(dir - 1);
				}
			}
			if (mClass == Monster.ORC) {
				if (info.getLevel() == 1) {
					return ImageManager.orc1_running.get(dir - 1);
				}
			}
		}
		if (info instanceof HeroInfo) {
			int heroClass = ((HeroInfo) info).getHeroCode();
			if (heroClass == Hero.HEROCODE_WARRIOR) {
				return ImageManager.getWarrior_running(dir);
			}
			if (heroClass == Hero.HEROCODE_DRUID) {
				return ImageManager.getDruid_running(dir);
			}
			if (heroClass == Hero.HEROCODE_HUNTER) {
				return ImageManager.getThief_running(dir);
			}
			if (heroClass == Hero.HEROCODE_MAGE) {
				return ImageManager.getMage_running(dir);
			}
		}
		return null;

	}

	public static AnimationSet getFigure_tipping_over(FigureInfo info) {
		int dir = info.getLookDir();
		if (info instanceof MonsterInfo) {
			int mClass = ((MonsterInfo) info).getMonsterClass();
			if (mClass == Monster.WOLF) {
				return ImageManager.wolf1_tipping_over.get(dir - 1);
			}

			if (mClass == Monster.SKELETON) {
				return ImageManager.skel1_tipping_over.get(dir - 1);
			}
			if (mClass == Monster.GHUL) {
				return ImageManager.ghul1_tipping_over.get(dir - 1);
			}
			if (mClass == Monster.OGRE) {
				return ImageManager.ogre1_tipping_over.get(dir - 1);
			}
			if (mClass == Monster.BEAR) {
				return ImageManager.spider1_tipping_over.get(dir - 1);
			}
			if (mClass == Monster.ORC) {
				return ImageManager.orc1_tipping_over.get(dir - 1);
			}
		}
		if (info instanceof HeroInfo) {
			int heroClass = ((HeroInfo) info).getHeroCode();
			if (heroClass == Hero.HEROCODE_WARRIOR) {
				return ImageManager.getWarrior_tipping_over(dir);
			}
			if (heroClass == Hero.HEROCODE_DRUID) {
				return ImageManager.getDruid_tipping_over(dir);
			}
			if (heroClass == Hero.HEROCODE_HUNTER) {
				return ImageManager.getThief_tipping_over(dir);
			}
			if (heroClass == Hero.HEROCODE_MAGE) {
				return ImageManager.getMage_tipping_over(dir);
			}
		}
		return null;

	}

	public static AnimationSet getFigure_sorcering(FigureInfo info) {
		int dir = info.getLookDir();
		if (info instanceof MonsterInfo) {
			int mClass = ((MonsterInfo) info).getMonsterClass();
			if (mClass == Monster.WOLF) {
				if (info.getLevel() == 1) {
					return ImageManager.wolf1_sorcering.get(dir - 1);
				}
			}

			if (mClass == Monster.SKELETON) {
				if (info.getLevel() == 1) {
					return ImageManager.skel1_sorcering.get(dir - 1);
				}
			}
			if (mClass == Monster.GHUL) {
				if (info.getLevel() == 1) {
					return ImageManager.ghul1_sorcering.get(dir - 1);
				}
			}
			if (mClass == Monster.OGRE) {
				if (info.getLevel() == 1) {
					return ImageManager.ogre1_sorcering.get(dir - 1);
				}
			}
			if (mClass == Monster.BEAR) {
				if (info.getLevel() == 1) {
					return ImageManager.spider1_sorcering.get(dir - 1);
				}
			}
			if (mClass == Monster.ORC) {
				if (info.getLevel() == 1) {
					return ImageManager.orc1_sorcering.get(dir - 1);
				}
			}
		}
		if (info instanceof HeroInfo) {
			int heroClass = ((HeroInfo) info).getHeroCode();
			if (heroClass == Hero.HEROCODE_WARRIOR) {
				return ImageManager.getWarrior_sorcering(dir);
			}
			if (heroClass == Hero.HEROCODE_DRUID) {
				return ImageManager.getDruid_sorcering(dir);
			}
			if (heroClass == Hero.HEROCODE_HUNTER) {
				return ImageManager.getThief_sorcering(dir);
			}
			if (heroClass == Hero.HEROCODE_MAGE) {
				return ImageManager.getMage_sorcering(dir);
			}
		}
		return null;

	}

	public static AnimationSet getFigure_using(FigureInfo info) {
		int dir = info.getLookDir();

		if (info instanceof MonsterInfo) {
			int mClass = ((MonsterInfo) info).getMonsterClass();
			if (mClass == Monster.WOLF) {
				if (info.getLevel() == 1) {
					AnimationSetDirections wolf1_using = ImageManager.wolf1_using;
					if (wolf1_using != null) {
						return wolf1_using.get(dir - 1);
					}
				}
			}

			if (mClass == Monster.SKELETON) {
				if (info.getLevel() == 1) {
					AnimationSetDirections skel1_using = ImageManager.skel1_using;
					if (skel1_using != null) {
						return skel1_using.get(dir - 1);
					}
				}
			}
			if (mClass == Monster.GHUL) {
				if (info.getLevel() == 1) {
					AnimationSetDirections ghul1_using = ImageManager.ghul1_using;
					if (ghul1_using != null) {
						return ghul1_using.get(dir - 1);
					}
				}
			}
			if (mClass == Monster.OGRE) {
				if (info.getLevel() == 1) {
					AnimationSetDirections ogre1_using = ImageManager.ogre1_using;
					if (ogre1_using != null) {
						return ogre1_using.get(dir - 1);
					}
				}
			}
			if (mClass == Monster.BEAR) {
				if (info.getLevel() == 1) {
					AnimationSetDirections spider1_using = ImageManager.spider1_using;
					if (spider1_using != null) {
						return ImageManager.spider1_using.get(dir - 1);
					}
				}
			}
			if (mClass == Monster.ORC) {
				if (info.getLevel() == 1) {
					AnimationSetDirections orc1_using = ImageManager.orc1_using;
					if (orc1_using != null) {
						return orc1_using.get(dir - 1);
					}
				}
			}
		}
		if (info instanceof HeroInfo) {
			int heroClass = ((HeroInfo) info).getHeroCode();
			if (heroClass == Hero.HEROCODE_WARRIOR) {
				return ImageManager.getWarrior_using(dir);
			}
			if (heroClass == Hero.HEROCODE_DRUID) {
				return ImageManager.getDruid_using(dir);
			}
			if (heroClass == Hero.HEROCODE_HUNTER) {
				return ImageManager.getThief_using(dir);
			}
			if (heroClass == Hero.HEROCODE_MAGE) {
				return ImageManager.getMage_using(dir);
			}
		}
		return null;

	}

	public static AnimationSet getFigure_slays(FigureInfo info) {
		int dir = info.getLookDir();
		if (info instanceof MonsterInfo) {
			int mClass = ((MonsterInfo) info).getMonsterClass();
			if (mClass == Monster.WOLF) {
				if (info.getLevel() == 1) {
					return ImageManager.wolf1_slays.get(dir - 1);
				}
			}

			if (mClass == Monster.SKELETON) {
				if (info.getLevel() == 1) {
					return ImageManager.skel1_slays.get(dir - 1);
				}
			}
			if (mClass == Monster.GHUL) {
				if (info.getLevel() == 1) {
					return ImageManager.ghul1_slays.get(dir - 1);
				}
			}
			if (mClass == Monster.OGRE) {
				if (info.getLevel() == 1) {
					return ImageManager.ogre1_slays.get(dir - 1);
				}
			}
			if (mClass == Monster.BEAR) {
				if (info.getLevel() == 1) {
					return ImageManager.spider1_slays.get(dir - 1);
				}
			}
			if (mClass == Monster.ORC) {
				if (info.getLevel() == 1) {
					return ImageManager.orc1_slays.get(dir - 1);
				}
			}
		}
		if (info instanceof HeroInfo) {
			int heroClass = ((HeroInfo) info).getHeroCode();
			if (heroClass == Hero.HEROCODE_WARRIOR) {
				return ImageManager.getWarrior_slays(dir);
			}
			if (heroClass == Hero.HEROCODE_DRUID) {
				return ImageManager.getDruid_slays(dir);
			}
			if (heroClass == Hero.HEROCODE_HUNTER) {
				return ImageManager.getThief_slays(dir);
			}
			if (heroClass == Hero.HEROCODE_MAGE) {
				return ImageManager.getMage_slays(dir);
			}
		}
		return null;

	}

}
