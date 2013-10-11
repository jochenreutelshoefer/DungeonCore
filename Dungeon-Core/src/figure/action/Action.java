/*
 * Abstrakte Superklasse fuer alle Aktionen
 *
 * Stellt Methoden zum erstellen von konkreten Aktionen zur Verfuegung
 * 
 */
package figure.action;

import item.ItemInfo;

/**
 * Abstrakte Superklasse fuer alle Aktionen
 *
 * Stellt Methoden zum erstellen von konkreten Aktionen zur Verfuegung
 */
public abstract class Action {
	
	/**
	 * Liefert ein neues ActionAttack-Objekt
	 * 
	 * @param fighterIndex Identifikationsindex der Figur 
	 * @param target Ziel des Angriffs
	 * @return Angriffsaktion
	 */
	
	
	
//	public Action () {
//		//fighterIndex = fighterID;
//	}
	
	private boolean unanimated = false;
	
	public void setUnanimated()  {
		unanimated = true;
		
	}
	 
	 
	/**
	 * @return Returns the fighterIndex.
	 */
//	public int getFighterIndex() {
//		return fighterIndex;
//	}
	/**
	 * Liefert eine neues ActionAttack-Objekt
	 * 
	 * @param fighterIndex Identifikationsindex der Angreifer-Figur 
	 * @param target Gegner
	 * @return Angriffssaktion
	 */
	public static AttackAction makeActionAttack(/*int fighterIndex, */int target) {
		//Fighter attacker = game.getFighter(fighterIndex);
		//Fighter targetF = attacker.getRoom().getFighterNumber(target);
//		if(game.getFight() != null && targetF != null) {
//			if(attacker.getRoom() == game.getFight().getFightRoom()) {
//				
					return new AttackAction(/*fighterIndex,*/target);
				
//			}
//		}
//		else {
//			if(attacker instanceof Hero) {
//				game.newStatement("Hier gibts grad nichts zu kämpfen",1);
//			}
//		}
//		return null;
	}
	
	/**
	 * Liefert eine neues ActionMove-Objekt
	 * Richtungen:
	 * 1 --> Norden;
	 * 2 --> Osten;
	 * 3 --> Sueden;
	 * 4 --> Westen;
	 * 
	 * @param fighterIndex Identifikationsindex der Figur 
	 * @param dir Richtung fuer die Bewegung
	 * @return Bewegungsaktion
	 */
	public static MoveAction makeActionMove(int fighterIndex, int dir) {
//		Fighter f = game.getFighter(fighterIndex);
//		//System.out.println("making moveAction: dir: "+dir );
//		if(game.getFight() == null || game.getFight().getFightRoom() != f.getRoom()) {
//			
				return new MoveAction(/*fighterIndex,*/dir);
//			
//			
//		}
//		System.out.println("returning null");
//		return null;
	}
	
	
	/**
	 * Liefert eine neues ActionFlee-Objekt
	 * Richtungen:
	 * 1 --> Norden;
	 * 2 --> Osten;
	 * 3 --> Sueden;
	 * 4 --> Westen;
	 * 
	 * @param fighterIndex Identifikationsindex der Figur 
	 * @param dir Richtung fuer die Flucht
	 * @param panic ob Flucht panisch sein soll
	 * @return Fluchtaktion
	 */
	public static FleeAction makeActionFlee(/*int fighterIndex, int dir,*/ boolean panic) {
//		Fighter f = game.getFighter(fighterIndex);
//		
//		if(game.getFight() != null) {
//			if(f.getRoom() == game.getFight().getFightRoom()) {
				
					return new FleeAction(/*fighterIndex,dir,*/ panic);
				
//				
//			}
//		}
		//return null;
	}
	
	/**
	 * Liefert ein neues ActionFlee-Objekt
	 * Richtungen:
	 * 1 --> Norden;
	 * 2 --> Osten;
	 * 3 --> Sueden;
	 * 4 --> Westen;
	 * 
	 * @param fighterIndex Identifikationsindex der Figur 
	 * @param dir Richtung fuer die Flucht
	 * @return Fluchtaktion
	 */
	public static FleeAction makeActionFlee(/*int fighterIndex, int dir*/) {
		return makeActionFlee(/*fighterIndex,dir,*/false);
	}
	
	/**
	 * Liefert eine neues ActionUseItem-Objekt
	 * 
	 * 
	 * @param fighterIndex Identifikationsindex der Figur
	 * @param itemIndex Nummer des Gegenstandes im Inventar
	 * @return Gegenstand-Benutzen-Aktion
	 */
	
	public static UseItemAction makeActionUseItem(/*int fighterIndex,*/ ItemInfo info) {
		//Fighter f = game.getFighter(fighterIndex);
		
		return new UseItemAction(/*fighterIndex,*/info);
	}
	
	/**
	 * Liefert eine neues ActionUseItem-Objekt
	 * 
	 * 
	 * @param fighterIndex Identifikationsindex der Figur
	 * @param it Gegenstand
	 * @return Gegenstand-Benutzen-Aktion
	 */
	
//	public static UseItemAction makeActionUseItem(int fighterIndex, Item it) {
//		
//		return new UseItemAction(fighterIndex,it);
//	}
	
	/**
	 * Liefert eine neues ActionTakeItem-Objekt
	 * 
	 * 
	 * @param fighterIndex Identifikationsindex der Figur
	 * @param index Nummer des Gegenstandes im Raum
	 * @return Gegenstand-Aufnehm-Aktion
	 */
	public static TakeItemAction makeActionTakeItem(/*int fighterIndex,*/ ItemInfo info) {
		//Fighter f = game.getFighter(fighterIndex);
		//Item it = f.getRoom().getItemNumber(index);
		return new TakeItemAction(/*fighterIndex,*/info);
	}
	
	
	

	public String toString() {
		return this.getClass().toString();
	}
	
	/**
	 * Liefert eine neues EndRoundAction-Objekt
	 * 
	 * 
	 * @param fighterIndex Identifikationsindex der Figur
	 * @return Rundenende-Aktion
	 */
	public static EndRoundAction makeEndRoundAction(/*int fighterIndex*/) {
		
		return new EndRoundAction(/*fighterIndex*/);
	}


	/**
	 * @return Returns the unanimated.
	 */
	public boolean isUnanimated() {
		return unanimated;
	}
	
	
}
