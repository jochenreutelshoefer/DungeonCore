package figure.action;



public class ShrineAction extends Action {

	private final Object target;

	private final boolean meta;

	public ShrineAction(Object target, boolean meta) {

		this.target = target;
		this.meta = meta;

	}



	public Object getTarget() {
		return target;
	}

	public boolean isMeta() {
		return meta;
	}

}
