/*
 * Created on 04.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action.result;

public class ActionResult {
	
	
	public static final int VALUE_POSSIBLE = 1;
	public static final int VALUE_IMPOSSIBLE = 2;
	public static final int VALUE_DONE = 3;
	public static final int VALUE_FAILED = 4;
	
	public static final ActionResult FAILED = new ActionResult(ActionResult.VALUE_FAILED);
	public static final ActionResult DONE = new ActionResult(ActionResult.VALUE_DONE);
	public static final ActionResult POSSIBLE = new ActionResult(ActionResult.VALUE_POSSIBLE);
	public static final ActionResult NOAP = new ActionResult(ActionResult.VALUE_IMPOSSIBLE,ActionResult.IMPOSSIBLE_REASON_NOAP);
	public static final ActionResult OTHER = new ActionResult(ActionResult.VALUE_IMPOSSIBLE,ActionResult.IMPOSSIBLE_REASON_OTHER);
	public static final ActionResult TARGET = new ActionResult(ActionResult.VALUE_IMPOSSIBLE,ActionResult.IMPOSSIBLE_REASON_WRONGTARGET);
	public static final ActionResult POSITION = new ActionResult(ActionResult.VALUE_IMPOSSIBLE,ActionResult.IMPOSSIBLE_REASON_WRONGPOSITION);
	public static final ActionResult INVALID = new ActionResult(ActionResult.VALUE_IMPOSSIBLE,ActionResult.IMPOSSIBLE_REASON_INVALIDACTION);
	public static final ActionResult MODE = new ActionResult(ActionResult.VALUE_IMPOSSIBLE,ActionResult.IMPOSSIBLE_REASON_WRONGMODE);
	public static final ActionResult KNOWLEDGE = new ActionResult(ActionResult.VALUE_IMPOSSIBLE,ActionResult.IMPOSSIBLE_REASON_NOKNOWLEDGE);
	public static final ActionResult DUST = new ActionResult(ActionResult.VALUE_IMPOSSIBLE,ActionResult.IMPOSSIBLE_REASON_NODUST);
	public static final ActionResult ITEM = new ActionResult(ActionResult.VALUE_IMPOSSIBLE,ActionResult.IMPOSSIBLE_REASON_NOITEM);
	public static final ActionResult DISTANCE = new ActionResult(ActionResult.VALUE_IMPOSSIBLE,ActionResult.IMPOSSIBLE_REASON_DISTANCE);
	
	public static final int IMPOSSIBLE_REASON_OTHER = 9;
	public static final int IMPOSSIBLE_REASON_NOAP = 10;
	public static final int IMPOSSIBLE_REASON_WRONGTARGET = 11;
	public static final int IMPOSSIBLE_REASON_NOKNOWLEDGE = 12;
	public static final int IMPOSSIBLE_REASON_NOITEM = 13;
	public static final int IMPOSSIBLE_REASON_ACTIONNULL = 14;
	public static final int IMPOSSIBLE_REASON_INVALIDACTION = 15;
	public static final int IMPOSSIBLE_REASON_WRONGMODE = 16;
	public static final int IMPOSSIBLE_REASON_WRONGPOSITION = 17;
	public static final int IMPOSSIBLE_REASON_NODUST = 18;
	public static final int IMPOSSIBLE_REASON_DISTANCE = 19;
	
	private int key1;
	private int key2;
	
	public ActionResult(int key) {
		this.key1 = key;
	}
	public ActionResult(int key1,int key2) {
		this.key1 = key1;
		this.key2 = key2;
	}

	/**
	 * @return Returns the key2.
	 */
	public int getReason() {
		return key2;
	}

	/**
	 * @param key2 The key2 to set.
	 */
	public void setReason(int key2) {
		this.key2 = key2;
	}

	/**
	 * @return Returns the key1.
	 */
	public int getValue() {
		return key1;
	}
	
	


}
