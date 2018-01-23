package item.quest;

import java.util.Iterator;
import java.util.List;

import dungeon.RoomEntity;
import shrine.Luzia;
import shrine.Shrine;
import dungeon.Room;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.hero.Hero;
import figure.monster.Monster;
import game.JDEnv;
import game.Turnable;

@Deprecated
public class LuziaAmulett extends Thing implements Turnable{
	
	int charge = 90;
	final int cost = 30;
	final int maxCharge = 90;
	public LuziaAmulett(Luzia witch) {
		super("Luzia's Amulett",witch);
	}
	
	@Override
	public void turn(int round) {
		if(charge < maxCharge) {
			charge++;
		}
	}
	
	@Override
	public String getText() {
		return JDEnv.getString("loads")+": "+charge/cost+"/"+maxCharge/cost;
	}

	
	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta) {
		if (meta) {
			if (f.getRoom().getShrine() == this.getSup()) {
				((Shrine) getSup()).use(f, this, meta);
				return true;
			}
		}else {
			if(f instanceof Hero) {
				
				if(charge >= cost) {
					charge -= cost;
				}else {
					return false;
				}
			}
			Room location = this.getOwner().getRoom();
			List neighbours = location.getNeighboursWithDoor();
			for (Iterator iter = neighbours.iterator(); iter.hasNext();) {
				 Room element = (Room) iter.next();
				if(location.hasOpenConnectionTo(element)) {
					f.getRoomObservationStatus(element.getPoint()).setVisibilityStatus(RoomObservationStatus.VISIBILITY_FIGURES);
					//f.addScoutedRoom(element);
				}
			}
			return true;
		}
		return false;

	}
}
