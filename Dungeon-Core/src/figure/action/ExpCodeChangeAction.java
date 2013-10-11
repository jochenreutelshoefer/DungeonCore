/*
 * Created on 04.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;


public class ExpCodeChangeAction extends Action {
	
	private int index;
	public ExpCodeChangeAction(/*int fighterID,*/ int index) {
		super(/*fighterID*/);
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}

}
