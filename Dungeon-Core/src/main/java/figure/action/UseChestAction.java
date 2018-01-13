package figure.action;

public class UseChestAction extends Action {
	
	private final boolean meta;

	public UseChestAction( boolean meta) {

		this.meta = meta;

	}

	public boolean isMeta() {
		return meta;
	}


}
