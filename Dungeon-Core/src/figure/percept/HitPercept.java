/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.LinkedList;
import java.util.List;

import fight.SlapResult;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;

public class HitPercept extends OpticalPercept {
	
	private Figure attacker;
	private Figure victim;
	private SlapResult res;
	private boolean indirect = false;
	
	public HitPercept(Figure a, Figure b, SlapResult res) {
		attacker = a;
		victim = b;
		this.res = res;
	}
	
	public HitPercept(Figure a, Figure b, SlapResult res, boolean indirect) {
		attacker = a;
		victim = b;
		this.res = res;
	}
	
	public FigureInfo getAttacker() {
		return FigureInfo.makeFigureInfo(attacker, viewer.getRoomVisibility());
	}
	public FigureInfo getVictim() {
		return FigureInfo.makeFigureInfo(victim,viewer.getRoomVisibility());
	}
	
    public int getDamage() {
    	return res.getValue();
    }
    
    public int getStandardDamage() {
    	return res.getSlap().getValueStandard();
    }
    
    public int getFireDamage() {
    	return res.getSlap().getValueFire();
    }
    
    public int getLightningDamage() {
    	return res.getSlap().getValueLightning();
    }
    public int getMagicDamage() {
    	return res.getSlap().getValueMagic();
    }
    public int getPoisonDamage() {
    	return res.getSlap().getValue_poison();
    }
    public int getTumbleValue() {
    	return res.getSlap().getValue_tumble();
    }

	public boolean isIndirect() {
		return indirect;
	}

	public List getInvolvedFigures() {
		List l = new LinkedList();
		l.add(getAttacker());
		l.add(getVictim());
		return l;
	}
}
