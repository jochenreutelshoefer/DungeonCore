package animation;

import figure.FigureInfo;
import graphics.ImageManager;

public class AnimationUtils {

	public static DefaultAnimationSet getFigure_pause(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Pause, info.getLookDirection());
	}

	public static DefaultAnimationSet getFigure_been_hit(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.BeingHit, info.getLookDirection());
	}

	public static DefaultAnimationSet getFigure_walking(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Walking, info.getLookDirection());
	}

	public static DefaultAnimationSet getFigure_running(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Running, info.getLookDirection());
	}

	public static DefaultAnimationSet getFigure_tipping_over(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.TippingOver, info.getLookDirection());
	}

	public static DefaultAnimationSet getFigure_sorcering(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Sorcering, info.getLookDirection());
	}

	public static DefaultAnimationSet getFigure_using(FigureInfo info) {
		return ImageManager.getAnimationSet(info, Motion.Using, info.getLookDirection());
	}

	public static DefaultAnimationSet getFigure_slays(FigureInfo info) {
		// TODO: Why has Lioness lookDir 0 later on...
		return ImageManager.getAnimationSet(info, Motion.Slaying, info.getLookDirection());
	}

}
