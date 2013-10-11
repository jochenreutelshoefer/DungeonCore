/*
 * Created on 08.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ai;
import figure.action.*;
import figure.control.AbstractMonsterFightIntelligence;
import figure.monster.MonsterInfo;
/**
 * Testweise Implementierung einer Monster-Kampf-Steuerung
 */
public class CustomMonsterFightIntelligence extends AbstractMonsterFightIntelligence {
	
	public CustomMonsterFightIntelligence(MonsterInfo m) {
		super(m);
	}
	
	public Action chooseFightAction() {
		return null;
	}

}
