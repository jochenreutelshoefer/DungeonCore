package figure.action;

import game.RoomInfoEntity;

public class ShrineAction extends Action {

	private final RoomInfoEntity target;

	private final boolean meta;

	public ShrineAction(RoomInfoEntity target, boolean meta) {

		this.target = target;
		this.meta = meta;

	}



	public RoomInfoEntity getTarget() {
		return target;
	}

	public boolean isMeta() {
		return meta;
	}

}
