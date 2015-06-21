package spell;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.Door;
import dungeon.DoorBlock;

public class IsolationInstance extends TimedSpellInstance {
	
	private final List<Door> doors = new LinkedList<Door>();
	private final int duration;
	
	public IsolationInstance(int time, Door [] d) {
		this.duration = time;
		for (int i = 0; i < d.length; i++) {
			if(d[i] != null) {
				d[i].addBlocking(new DoorBlock(this));
				doors.add(d[i]);
			}
			
		}
	}

	@Override
	public void stopEffect() {
		for (Iterator<Door> iter = doors.iterator(); iter.hasNext();) {
			Door element = iter.next();
			element.removeBlocking(this);
			
		}

	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return duration;
	}

}
