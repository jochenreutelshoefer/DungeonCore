package control;

import game.PerceptHandler;
import dungeon.RoomInfo;

public interface AnimationPerceptHandler extends PerceptHandler {

	public void stopAllAnimtation();

	public boolean currentAnimationThreadRunning(RoomInfo r);

}
