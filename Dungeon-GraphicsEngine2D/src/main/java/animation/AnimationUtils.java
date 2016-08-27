package animation;

import dungeon.util.RouteInstruction;
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
			return ImageManager.getAnimationSet(((HeroInfo) info), Motion.Pause, RouteInstruction.direction(dir));
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
			return ImageManager.getAnimationSet(((HeroInfo) info), Motion.BeingHit, RouteInstruction.direction(dir));
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
			return ImageManager.getAnimationSet(((HeroInfo) info), Motion.Walking, RouteInstruction.direction(dir));
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
			return ImageManager.getAnimationSet(((HeroInfo) info), Motion.Running, RouteInstruction.direction(dir));
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
			return ImageManager.getAnimationSet(((HeroInfo) info), Motion.TippingOver, RouteInstruction.direction(dir));
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
			return ImageManager.getAnimationSet(((HeroInfo) info), Motion.Sorcering, RouteInstruction.direction(dir));

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
			return ImageManager.getAnimationSet(((HeroInfo) info), Motion.Using, RouteInstruction.direction(dir));
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
			return ImageManager.getAnimationSet(((HeroInfo) info), Motion.Slaying, RouteInstruction.direction(dir));
		}
		return null;

	}

}
