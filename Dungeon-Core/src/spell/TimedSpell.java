/*
 * Created on 30.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spell;
/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class TimedSpell extends Spell  {
	
	int timer = 0;
	
	public TimedSpell(int level, int[] values) {
		super(level,values);
	}
	

}
