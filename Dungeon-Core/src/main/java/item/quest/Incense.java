package item.quest;

import game.JDEnv;

public class Incense extends item.Item {
	
	public Incense(){
		super(1000, false);
	}
	
	public String getText() {
		return JDEnv.getString("incense_text");
	}
	
	public String toString() {
		return JDEnv.getString("incense_name");
	}
	
	

	

}
