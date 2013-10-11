/*
 * Created on 26.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package applet;

import java.util.Iterator;
import java.util.LinkedList;

public class LigaStatus {
	
	long exp;
	JDTime time = new JDTime(0);
	long kills;
	long warrior;
	long thief;
	long druid;
	long sorcerer;
	long rounds;
	long games;
	LinkedList ratings = new LinkedList();
	//String player;
	public LigaStatus() {
		//player = name;
	}
	
	public void addGame(long exp, JDTime time, long kills, String type, long rating,long rounds) {
		games++;
		this.exp += exp;
		
		this.time.addTime(time);
		
		this.kills += kills;
		this.rounds += rounds;
		ratings.add(new Long(rating));
		if(type.equals("Dieb")) {
			thief++;
		}
		if(type.equals("Krieger")) {
			warrior++;
		}
		if(type.equals("Druide")) {
			druid++;
		}
		if(type.equals("Magier")) {
			sorcerer++;
		}
	}
	
	public long getTotalRating() {
		double cnt = ratings.size();
		double sum = 0;
		for (Iterator iter = ratings.iterator(); iter.hasNext();) {
			Long element = (Long) iter.next();
			sum += element.longValue();
			
		}
		long av = (long)(sum / cnt);
		return (long)Math.sqrt(cnt)*av;
	}

	/**
	 * @return Returns the sorcerer.
	 */
	public long getSorcerer() {
		return sorcerer;
	}

	/**
	 * @param sorcerer The sorcerer to set.
	 */
	public void setSorcerer(long sorcerer) {
		this.sorcerer = sorcerer;
	}

	/**
	 * @return Returns the druid.
	 */
	public long getDruid() {
		return druid;
	}

	/**
	 * @return Returns the exp.
	 */
	public long getExp() {
		return exp;
	}

	/**
	 * @return Returns the kills.
	 */
	public long getKills() {
		return kills;
	}

	/**
	 * @return Returns the thief.
	 */
	public long getThief() {
		return thief;
	}

	/**
	 * @return Returns the time.
	 */
	public JDTime getTime() {
		return time;
	}

	/**
	 * @return Returns the warrior.
	 */
	public long getWarrior() {
		return warrior;
	}

	/**
	 * @return Returns the rounds.
	 */
	public long getRounds() {
		return rounds;
	}

	/**
	 * @return Returns the games.
	 */
	public long getGames() {
		return games;
	}
	

}
