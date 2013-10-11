package shrine;
import item.interfaces.Usable;


import java.awt.Color;

import dungeon.*;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.memory.MemoryObject;
import figure.memory.ShrineMemory;
import game.*;
import gui.Paragraph;
import gui.Paragraphable;

/**
 * Abstrakte Oberklasse aller Schreine/Oertlichkeiten. 
 * Ist von Anfang an einem Raum zugeordnet, nicht mobil.
 *
 */
public abstract class Shrine /*extends JDEnv*/ implements Usable,Turnable,InfoProvider{

	
	
	public static final int SHRINE_NOSHRINE = 0;
	public static final int SHRINE_CORPSE= 1;
	public static final int SHRINE_DARK_MASTER = 2;
	public static final int SHRINE_HEALTH_FOUNTAIN = 3;
	public static final int SHRINE_INFO = 4;
	public static final int SHRINE_LUZIA = 5;
	public static final int SHRINE_QUEST = 6;
	public static final int SHRINE_REPAIR = 7;
	public static final int SHRINE_RUNEFINDER = 8;
	public static final int SHRINE_RUNE = 9;
	public static final int SHRINE_SORCER_LAB = 10;
	public static final int SHRINE_STATUE = 11;
	public static final int SHRINE_TRADER = 12;
	public static final int SHRINE_XMAS = 13;
	public static final int SHRINE_BROOD = 14;
	public static final int SHRINE_ANGEL = 15;
	
	
	protected Room location;

	
	int type;

	
	String name;

	
	String story;

	
	String text;

	//public static Game game;

    public Shrine(Room p){
		location = p;
    }
     public Shrine(){
		
    }
     
     public int getSecondIdentifier() {
    	 return type;
     }
    	 
     
     
    

	
	public void setLocation(Room p) {
		if (this instanceof Statue) {
			//System.out.println("setzte location von statue neu auf: "+p.toString());	
		}
		location = p;
	}
	
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new ShrineInfo(this,map);
	}
	
	public ShrineMemory getMemoryObject(FigureInfo info) {
		return new ShrineMemory(this);
	}

    
//    public static void setGame(Game g) {
//    	game  = g;
//    }

	public abstract int getShrineIndex();

    public abstract void turn(int round);

	
	public int getType() {
		return type;
	}

	
	public abstract Color getColor();

	
	public abstract String getStory();

    
    public abstract String toString();
    
//    public Paragraph[] getParagraphs() {
//		Paragraph []p = new Paragraph[2];
//		p[0] = new Paragraph(toString());
//		p[0].setSize(20);
//		p[0].setCentered();
//		p[0].setColor(Color.orange);
//		p[0].setBold();
//		
//		p[1] = new Paragraph(getText());
//		p[1].setSize(16);
//		p[1].setCentered();
//		p[1].setColor(Color.black);
//		p[1].setBold();
//		
//		
//		
//		return p;
//	}

	
	public abstract String getText();

	
	

	//public abstract void metaClick(Figure f);
	
	public abstract String getStatus();

	
	
	
	public Room getRoom() {
		return location;
	}

	public JDPoint getLocation() {
		return location.getNumber();
	}
	
	public String getName() {
		return name;
	}

}

