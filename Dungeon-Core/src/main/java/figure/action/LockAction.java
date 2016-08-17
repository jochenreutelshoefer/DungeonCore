package figure.action;

public class LockAction extends Action {
	
	private int dir;
	
	public LockAction(int dir) {
		this.dir = dir;
	}

	public int getDir() {
		return dir;
	}

}
