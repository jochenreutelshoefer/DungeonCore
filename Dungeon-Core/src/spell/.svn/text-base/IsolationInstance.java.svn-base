package spell;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.Door;
import dungeon.DoorBlock;

public class IsolationInstance extends TimedSpellInstance {
	
	private List doors = new LinkedList();
	private int duration;
	
	public IsolationInstance(int time, Door [] d) {
		this.duration = time;
		for (int i = 0; i < d.length; i++) {
			if(d[i] != null) {
				d[i].addBlocking(new DoorBlock(this));
				doors.add(d[i]);
			}
			
		}
	}

	public void stopEffect() {
		for (Iterator iter = doors.iterator(); iter.hasNext();) {
			Door element = (Door) iter.next();
			element.removeBlocking(this);
			
		}

	}

	public int getDuration() {
		// TODO Auto-generated method stub
		return duration;
	}

}
