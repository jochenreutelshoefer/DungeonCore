package animation;

import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import graphics.ImageManager;

public class AnimationUtils {

	public static AnimationSet getFigure_pause(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Pause, info.getLookDirection());
	}

	public static AnimationSet getFigure_been_hit(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.BeingHit, info.getLookDirection());
	}

	public static AnimationSet getFigure_walking(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Walking, info.getLookDirection());
	}

	public static AnimationSet getFigure_running(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Running, info.getLookDirection());
	}

	public static AnimationSet getFigure_tipping_over(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.TippingOver, info.getLookDirection());
	}

	public static AnimationSet getFigure_sorcering(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Sorcering, info.getLookDirection());
	}

	public static AnimationSet getFigure_using(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Using, info.getLookDirection());
	}

	public static AnimationSet getFigure_slays(FigureInfo info) {
		// TODO: Why has Lioness lookDir 0 later on...
		return ImageManager.getAnimationSet(info, Motion.Slaying, info.getLookDirection());
	}

}
