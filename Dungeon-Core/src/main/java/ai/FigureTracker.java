package ai;

import dungeon.JDPoint;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.percept.DiePercept;
import figure.percept.FleePercept;
import figure.percept.MovePercept;
import figure.percept.Percept;
import figure.percept.ScoutPercept;

public class FigureTracker extends Tracker {
	
	private final FigureInfo target;
	private TrackingInfo sureInfo = null;
	
	public FigureTracker(FigureInfo f) {
		this.target = f;
		
	}
	
	public TrackingInfo getSureInfo() {
		return sureInfo;
	}

	public boolean evaluatePercept(Percept element) {
		
		//percepts.add(element);
		int round = element.getRound();
		if(element instanceof DiePercept) {
			if(((DiePercept)element).equals(target)){
				return true;
			}
		
		}
			if(element instanceof MovePercept) {
				if(((MovePercept)element).getFigure().equals(target)) {
					JDPoint lastLocation = ((MovePercept)element).getTo().getPoint();
					
					this.sureInfo = new TrackingInfo(target,round,lastLocation);
					
				}
			}
			if(element instanceof FleePercept) {
				if(((FleePercept)element).getFigure().equals(target)) {
					int dir = ((FleePercept)element).getDir();
					RoomInfo r = ((FleePercept)element).getRoom();
					JDPoint lastLocation = r.getNeighbourRoom(dir).getNumber();
					this.sureInfo = new TrackingInfo(target,round,lastLocation);
				}
			}
			if(element instanceof ScoutPercept) {
				if(((ScoutPercept)element).getFigure().equals(target)) {
					int dir = ((ScoutPercept)element).getDir();
					RoomInfo r = ((ScoutPercept)element).getRoom();
					JDPoint lastLocation = r.getNeighbourRoom(dir).getNumber();
					this.sureInfo = new TrackingInfo(target,round,lastLocation);
				}
			}
	
		
		return false;
		
	}

	public FigureInfo getTarget() {
		return target;
	}

}
