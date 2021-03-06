//import java.de.jdungeon.util.*;
package de.jdungeon.location;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.monster.Ghul;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.monster.Ogre;
import de.jdungeon.figure.monster.Orc;
import de.jdungeon.figure.monster.Skeleton;
import de.jdungeon.figure.monster.Spider;
import de.jdungeon.figure.monster.Wolf;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.JDEnv;
import de.jdungeon.item.Item;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.util.RouteInstruction;
@Deprecated
public class Brood extends Location {

	private final int type;
	//String Type;
	int points;
	int grow = 40;
	//int grow = 0;
	boolean blocked = false;
	public final static int BROOD_CREATURE = 1;
	public final static int BROOD_NATURE = 2;
	public final static int BROOD_UNDEAD = 3;
	
	
	int blockTime;

	
	
	int delay = 500;
	//int delay = 0;


	public Brood(int Type, Room location) {
		super(location);
		
		this.type = Type;
		
		points = 0;
		
		setStory();
	}
	
	public Brood(int Type) {
		super();
		
		this.type = Type;
	
		points = 0;
		
		setStory();
	}
	@Override
	public boolean needsTarget() {
		return false;
	}
	
	private void setStory() {
		String typeString ="";
		if(type == BROOD_CREATURE) {
			typeString = "creature";
		}
		if(type == BROOD_NATURE) {
			typeString = "nature";
		}
		if(type == BROOD_UNDEAD) {
			typeString = "undead";
		}
		
		story = JDEnv.getResourceBundle().getString("see_brood_"+typeString);
	}
	
	public void metaClick(Figure f){
		
	}

	
	public void block(int t) {
		blocked = true;
		blockTime = t;
	}

	@Override
	public String getStory() {
		return story;
	}
	@Override
	public boolean usableOnce() {
		return false;
	}

	@Override
	public void turn(int round, DungeonWorldUpdater mode) {
		////System.out.println(
		//	"brood: " + Type + " Raum: " + raum.getNumber().toString());
		if (delay > 0) {
			delay -= 1;
		}
		if (delay == 0) {
			if (blocked) {
				blockTime--;
				if (blockTime == 0)
					blocked = false;
			} else {
				points += grow;
				////System.out.println(Type + Integer.toString(points));
				if ((((int) (Math.random() * 100)) < 10) && (points > 2200)) {
					////System.out.println(Type + "spitting monster");
					Monster m = spitMonster(points);
					addItem(m);
					addWay(m);
					location.figureEnters(m,0, -1);
					points = 0;
				}
			}
		}
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
		//
		return ActionResult.OTHER;
	}

	private void addWay(Monster m) {
		//System.out.println("adding RouteInstructions");
		int way[] = new int[4];

		int x = (int) (Math.random() * 2);
		if (x == 0) {
			way[0] += (int) (Math.random() * 5);
		} else {
			way[2] += (int) (Math.random() * 5);
		}

		if (x == 0) {
			way[1] += (int) (Math.random() * 5);
		} else {
			way[3] += (int) (Math.random() * 5);
		}

		boolean e = true;
		while (e) {
			e = false;
			if (way[0] > 0) {
				m.addRouteInstruction(new RouteInstruction(1));
				//System.out.print("  nach norden");
				way[0]--;
				e = true;
			}
			if (way[1] > 0) {
				m.addRouteInstruction(new RouteInstruction(2));
				//System.out.print("  nach osten");
				way[1]--;
				e = true;
			}
			if (way[2] > 0) {
				m.addRouteInstruction(new RouteInstruction(3));
				//System.out.print("  nach suden");
				way[2]--;
				e = true;
			}
			if (way[3] > 0) {
				m.addRouteInstruction(new RouteInstruction(4));
				//System.out.print("  nach westen");
				way[3]--;
				e = true;
			}
		}
	}

	private void addItem(Monster m) {
		m.takeItem(Item.newItem(m.getWorth() / 50));
	}
	
	
	private Monster spitMonster(int worth) {
		int i = (int) (Math.random() * 2);
		if (type==Brood.BROOD_NATURE) {
			if (i < 1) {
				return new Wolf(worth);
			} else {
				return (
					new Spider(worth));
			}
		} else if (type == Brood.BROOD_CREATURE) {
			if (i < 1) {
				return 
					new Orc(
						worth
						);
			} else {
				return (new Ogre(worth));
			}
		}
		if (type == Brood.BROOD_UNDEAD) {
			if (i < 1) {
				return (
					new Skeleton(
						worth
						));
			} else {
				return new Ghul(worth);
			}
		} else
			return null;
	}

	@Override
	public String toString() {
		String name = "-";
		if (type == Brood.BROOD_NATURE)
			name = JDEnv.getResourceBundle().getString("shrine_brood_nature");
		if (type == Brood.BROOD_CREATURE)
			name = JDEnv.getResourceBundle().getString("shrine_brood_creature");
		if (type == Brood.BROOD_UNDEAD)
			name = JDEnv.getResourceBundle().getString("shrine_brood_undead");
		return name/*+"\n Out of order- N/A"*/;
	}

	@Override
	public int getType() {
		return type;
	}
	@Override
	public String getText() {
		return text;
	}

	
	@Override
	public boolean canBeUsedBy(Figure f) {
		   return false;
	   }

	@Override
	public String getStatus() {
		if (blocked) {
			return JDEnv.getResourceBundle().getString("shrine_brood_blocked");
		} else {
			return JDEnv.getResourceBundle().getString("shrine_brood_open");
		}
	}
}
