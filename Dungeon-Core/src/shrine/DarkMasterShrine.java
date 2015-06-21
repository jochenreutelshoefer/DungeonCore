package shrine;

import item.Item;
import item.ItemPool;
import item.quest.DarkMasterKey;
import util.JDColor;
import dungeon.Room;
import figure.Figure;
import figure.monster.DarkMaster;
import figure.monster.Monster;

/*
 * Created on 29.07.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Jochen
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DarkMasterShrine extends Shrine {

	/**
	 * @param actualP
	 * 
	 */
	DarkMasterKey key1;

	/**
	 * 
		 */
	DarkMasterKey key2;

	/**
	 * 
		 */
	Item item1;

	/**
	 * 
	 */
	boolean masterOut = false;


	/**
	 * 
	 */
	Item item2;

	public DarkMasterShrine(Room p, DarkMasterKey k1, DarkMasterKey k2) {
		super(p);
		// TODO Auto-generated constructor stub

		key1 = k1;
		key2 = k2;

	}
public void metaClick(Figure f){
		
	}

@Override
public boolean needsTarget() {
	return false;
}
	public void plugKey(DarkMasterKey k) {
		if (item1 == null) {
			item1 = k;
//			this.location
//			.getDungeon()
//			.getGame()
//			.newStatement(
//					"Du h�rst ein dumpfen Ton, wie aus"
//							+ " einer riesigen Kehle, es scheint fasst ein Lachen zu sein. Der dunkle Dungeon scheint"
//							+ " Dir pl�tzlich noch dusterer zu werden. Du musst sie aufhalten! Jetzt ist es nicht mehr "
//							+ "lange bis das Unheil �ber Dich hereinbricht. Im Siegel des Portals des Dunklen Meisters "
//							+ "fehlt nur noch ein Stein!", 3);
			
			
			
//			ItemOwner io = key2.getOwner();
//			if (io instanceof Monster) {
//				
//				((Monster) io).setMissionIndex(Monster.MISSION_DARK_MASTER);
//			}
		} else if (item2 == null) {
			item2 = k;
		} else {

		}

		checkActivation();
	}
	
	@Override
	public boolean canBeUsedBy(Figure f) {
		   return true;
	   }

	private void checkActivation() {

		if ((item1 == key1 && item2 == key2)
				|| (item1 == key2 && item2 == key1)) {
			//System.out.println("All dark_master_keys plugged! Calling
			// master!");
			Monster master = new DarkMaster(location.getDungeon().getGame(),
					location);
			master.takeItem(ItemPool.getGift(60, 2));
			location.figureEnters(master,0);
			masterOut= true;
//			newStatement(
//							"Ein lautes dunkles Lachen erhebt sich, es scheint "
//									+ "von allen Seiten zu kommen, und es l�sst Dir die Haare zu Berge stehen. DU bist gemeint.",
//							3);
			//System.out.println("Der Meister ist gekommen!!");
		}
	}

	public void callKeys() {
//		if (item1 == key1) {
//			System.out.println("Key1 gesetzt, rufe key2");
//			ItemOwner io = key2.getOwner();
//			if (io instanceof Monster) {
//
//				((Monster) io).setMissionIndex(Monster.MISSION_DARK_MASTER);
//			}
//		} else {
//			ItemOwner io = key1.getOwner();
//			if (io instanceof Monster) {
//
//				((Monster) io).setMissionIndex(Monster.MISSION_DARK_MASTER);
//			}
//		}
	}

	/**
	 *  
	 */
	public DarkMasterShrine() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#turn(int)
	 */
	@Override
	public void turn(int round) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public int getShrineIndex() {
		return Shrine.SHRINE_DARK_MASTER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getColor()
	 */
	@Override
	public JDColor getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getStory()
	 */
	@Override
	public String getStory() {
		// TODO Auto-generated method stub
		return "Das unheimliche geometrische Gebilde in der Ecke fesselt sofort Deinen Blick"
				+ " und obwohl das Bestreben sich davon abzuwenden deutlich in Dir anw�chst, erkennst Du"
				+ " den seltsamen Schimmer �ber der Mitte des Kreises und das Siegel, in dem offenbar 2 "
				+ "Gegenst�nde eingesetzt werden k�nnen. Du fragst Dich ob Du wirklich wissen willst, was dann"
				+ " passiert. ";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Pentagramm";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getText()
	 */
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return "Portal des Dunklen Meisters\n" + getStatus();
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getStatus()
	 */
	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		int keys = 0;
		if (item1 != null) {
			if (item2 != null) {
				keys = 2;
			} else {
				keys = 1;
			}
		}
		String s = "Status: ";
		if (keys < 2) {
			s += "Versiegelt";
		} else {
			s += "GE�FFNET";
		}
		s += "\nSiegelsteine: " + keys + "/2";
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see usable#use(fighter)
	 */
	@Override
	public boolean use(Figure f,Object target,boolean meta) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see usable#usableOnce()
	 */
	@Override
	public boolean usableOnce() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return Returns the masterOut.
	 * 
	 * @uml.property name="masterOut"
	 */
	public boolean isMasterOut() {
		return masterOut;
	}

}