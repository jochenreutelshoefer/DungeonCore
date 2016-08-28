package animation;

import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import graphics.ImageManager;

public class AnimationUtils {

	public static AnimationSet getFigure_pause(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Pause, RouteInstruction.Direction.fromInteger(info.getLookDir()));
	}

	public static AnimationSet getFigure_been_hit(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.BeingHit, RouteInstruction.Direction.fromInteger(info.getLookDir()));
	}

	public static AnimationSet getFigure_walking(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Walking, RouteInstruction.Direction.fromInteger(info.getLookDir()));
	}

	public static AnimationSet getFigure_running(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Running, RouteInstruction.Direction.fromInteger(info.getLookDir()));
	}

	public static AnimationSet getFigure_tipping_over(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.TippingOver, RouteInstruction.Direction.fromInteger(info.getLookDir()));
	}

	public static AnimationSet getFigure_sorcering(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Sorcering, RouteInstruction.Direction.fromInteger(info.getLookDir()));
	}

	public static AnimationSet getFigure_using(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Using, RouteInstruction.Direction.fromInteger(info.getLookDir()));
	}

	public static AnimationSet getFigure_slays(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Slaying, RouteInstruction.Direction.fromInteger(info.getLookDir()));
	}

}
