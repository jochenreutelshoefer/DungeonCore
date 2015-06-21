/**
 * Ein Dungeon ist disjunkt in Sektoren unterteilt, die seriell hintereinander liegen.
 * Von Sektor zu Sektor soll der Schwierigkeitsgrad steigen. In den konkreten
 * Subklassen wird dieser festgelegt.
 * Ein Sektor enthaelt mehrere (mindestens eine) Hallen.
 *
 */
package dungeon.generate;

import figure.monster.Monster;
import game.DungeonGame;

import java.util.LinkedList;
import java.util.List;

import shrine.DarkMasterShrine;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.util.RouteInstruction;



public class Sector {

	
	protected Hall mainHall;

	
	protected final List<Hall> halls = new LinkedList<Hall>();

	public int number;

	
	protected JDPoint startRoom;

	
	protected Dungeon d;

	
	protected DungeonGame game;

	
	AbstractDungeonFiller df;

	
	private Room dark_master_room;

	private boolean master_activated = false;

	public Sector(
		Dungeon d,
		JDPoint startRoom,
		int number,
		int avMonsterStrength,
		int mainSize,
		DungeonGame game,
		AbstractDungeonFiller df) {
		this.df = df;
		this.d = d;
		this.game = game;
		this.number = number;
		this.startRoom = startRoom;
		mainHall =
			new Hall(
				d,
				mainSize,
				500,
				0,
				startRoom,
				RouteInstruction.NORTH,
				3,
				this,
				"main",0);
		boolean ok = mainHall.makeArea(null);
		////System.out.println("Erfolgreich haupHalle: "+ok);

	}
	public Sector() {

	}

	public Room getConnectionRoom() {
		return null;
	}
	
	public Monster getRandomSectorMonster(int i) {
		if(i > 100) {
			return null;
		}
		LinkedList allHalls = new LinkedList(halls);
		allHalls.add(mainHall);
		int k = (int)(Math.random()*allHalls.size());
		//System.out.println("k-te Halle: "+k);
		Hall h = ((Hall)allHalls.get(k));
		Monster m = h.getRandomMonster();
		if(m == null) {
			return getRandomSectorMonster(++i);
		}else {
			return m;
		
		}
	}
	

	public void callMaster() {
		if (!master_activated) {

			if (dark_master_room != null) {
//				game.newStatement("Du h�rst pl�tzlich einen langgezogenen dumpfen Schrei, offenbar von der" +
//					" Kreatur, die Du eben halb tot gepr�gelt hast. Der Schrei gellt duch die Gem�uer und als " +
//					"er endlich abklingt hast Du das Gef�hl, dass sich dort tiefer im Dungeon hinter den " +
//					"dunklen Mauern etwas tut. Und Du f�hlst Dich gar nicht wohl dabei.",3);
				((DarkMasterShrine)dark_master_room.getShrine()).callKeys();
				
			}
		}
		if(!((DarkMasterShrine)dark_master_room.getShrine()).isMasterOut()) {
			((DarkMasterShrine)dark_master_room.getShrine()).callKeys();
		}
		master_activated = true;

	}

}
