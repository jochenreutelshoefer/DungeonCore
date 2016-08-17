/*
 * Created on 01.12.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

public class StepAction extends Action {
	

	int targetIndex = -1;
	

	public StepAction( int index) {
		super();
		targetIndex = index;
	}
	
	


	public int getTargetIndex() {
		return targetIndex;
	}
	
	

}
