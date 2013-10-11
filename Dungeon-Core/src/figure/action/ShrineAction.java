package figure.action;



public class ShrineAction extends Action {

	private Object target;

	private boolean meta;

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
