package de.jdungeon.item.quest;

import de.jdungeon.game.JDEnv;

public class Incense extends de.jdungeon.item.Item {
	
	public Incense(){
		super(1000, false);
	}
	
	@Override
	public String getText() {
		return JDEnv.getString("incense_text");
	}
	
	public String toString() {
		return JDEnv.getString("incense_name");
	}
	
	

	

}
