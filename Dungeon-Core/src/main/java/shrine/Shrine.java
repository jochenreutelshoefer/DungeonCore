package shrine;
import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.memory.ShrineMemory;
import game.InfoEntity;
import game.InfoProvider;
import game.Turnable;
import item.interfaces.Usable;
import util.JDColor;
import dungeon.JDPoint;
import dungeon.Room;

/**
 * Abstrakte Oberklasse aller Schreine/Oertlichkeiten. 
 * Ist von Anfang an einem Raum zugeordnet, nicht mobil.
 *
 */
public abstract class Shrine implements Usable, Turnable, InfoProvider{


	// TODO: factor out this list, use class objects
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
	public static final int SHRINE_EXIT = 16;
	public static final int SHRINE_REVEALMAP = 17;

	
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
	
	@Override
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

    @Override
	public abstract void turn(int round);

	
	public int getType() {
		return type;
	}

	
	public abstract JDColor getColor();

	
	public abstract String getStory();

    
    @Override
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

	@Override
	public JDPoint getLocation() {
		return location.getNumber();
	}
	
	public String getName() {
		return name;
	}

}

