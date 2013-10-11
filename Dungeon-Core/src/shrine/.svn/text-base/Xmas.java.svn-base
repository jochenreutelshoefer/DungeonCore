/*
 * 
 * Created on 17.12.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package shrine;
import item.*;
import item.interfaces.Usable;

import java.awt.Color;

import dungeon.Room;

import figure.Figure;
import figure.hero.Hero;
import figure.percept.TextPercept;
import game.JDEnv;


/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Xmas extends Shrine {

	
	Item it;

		public Xmas(Item it,Room r) {
			super(r);
			this.it = it;
		}
		public void metaClick(Figure f){
			
		}
		
	
	   public Color getColor() {
		   return Color.gray;
	   }
	   

	   /**
		* @see Shrine#getStory()
		*/
	   public String getStory() {
		   return JDEnv.getResourceBundle().getString("shrine_xmas_story");
	   }

	   /**
		* @see java.lang.Object#toString()
		*/
	   public String toString() {
		   return JDEnv.getResourceBundle().getString("shrine_xmas_name");
	   }

	   /**
		* @see Shrine#getText()
		*/
	   public String getText() {
		   return toString();
	   }
	   
	   public int getShrineIndex() {
	   	return Shrine.SHRINE_XMAS;
	   }

	   /**
		* @see Shrine#clicked(fighter)
		*/
	   public  boolean use(Figure f,Object target,boolean meta) {
	   	if(it != null) {
	   		f.tellPercept(new TextPercept(JDEnv.getResourceBundle().getString("shrine_xmas_use")));
	   		this.location.addItem(it);
	   		it = null;
	   	}
	   	else {
	   		//System.out.println("Item ist null!");
	   	}
	   	return true;
	   }
	   
	   public boolean needsTarget() {
			return false;
		}
	   
	   public boolean canBeUsedBy(Figure f) {
		   return f instanceof Hero;
	   }

	   /**
		* @see Shrine#getStatus()
		*/
	   public String getStatus() {
		   return null;
	   }

	  
		public void turn(int k) {
			
		}
	   /**
		* @see Usable#usableOnce()
		*/
	   public boolean usableOnce() {
		   return false;
	   }
	
		
		
		
		
}
