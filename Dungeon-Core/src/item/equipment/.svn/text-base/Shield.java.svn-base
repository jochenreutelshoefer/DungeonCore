package item.equipment;

import item.Item;

import java.util.*;
import java.awt.Color;

import figure.attribute.Attribute;
import figure.attribute.ItemModification;
import game.JDEnv;
import gui.Paragraph;
public class Shield extends EquipmentItem {

	/**
	 * 
	 */
	private int chanceToBlock;

   // private String Type;
    //attribute hit_points;
    private int dmgPerHP = 10;
    private int actualBlocks = 0;

    public Shield (int value, boolean magic) {
    	super(value,magic);
	hitPoints = new Attribute(Attribute.HIT_POINTS,10);
	    	
	chanceToBlock = value / 5;
	
	Type = getType(value);
	}
    
    private String getType(int value) {
    	String Type = "shield type";
    	if(value == 0) Type = "Schild";
    	else if (value <= 20) Type = JDEnv.getResourceBundle().getString("shield1");
    	else if(value <=40) Type =JDEnv.getResourceBundle().getString("shield2");
    	else if(value <=60) Type =JDEnv.getResourceBundle().getString("shield3");
    	else if(value <=80) Type =JDEnv.getResourceBundle().getString("shield4");
    	else if(value <=100) Type =JDEnv.getResourceBundle().getString("shield5");
    	else if(value <=120) Type =JDEnv.getResourceBundle().getString("shield6");
    	else if(value <=140) Type =JDEnv.getResourceBundle().getString("shield7");
    	else if(value <=160) Type =JDEnv.getResourceBundle().getString("shield8");
    	else if(value <=180) Type =JDEnv.getResourceBundle().getString("shield9");
    	else Type = JDEnv.getResourceBundle().getString("shield10");

    	return Type;
    }
	
	 public Shield (int value, boolean magic, int hitpoints) {
    	super(value,magic);
	hitPoints = new Attribute(Attribute.HIT_POINTS,hitpoints);
	    	
	chanceToBlock = value / 5;
	Type = getType(value);


	}
	
	public void madeBlock(int dmg) {
		actualBlocks +=dmg;
		////System.out.println("Schläge: "+actualBlocks);
		if(actualBlocks >= dmgPerHP) {
			////System.out.println("Waffenzustand -1");
			int hp = dmg / dmgPerHP;
			if(hitPoints.getValue() > 0) {
			hitPoints.modValue(-1*hp);
			actualBlocks = dmg % dmgPerHP;
			}
		}
	}
	
	public Paragraph[] getParagraphs() {
		Paragraph []p = new Paragraph[3];
		p[0] = new Paragraph(getName());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(new Color(170,140,60));
		p[0].setBold();
		
		p[1] = new Paragraph(toString());
		p[1].setSize(16);
		p[1].setCentered();
		p[1].setColor(Color.black);
		p[1].setBold();
		
		String s = 	getText();
		
		
		p[2] = new Paragraph(s);
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(Color.black);
			
		return p;
	}
	
	public String getName() {
		if(unique) {
    		return name;	
    	}
    	return Type;	
	}
	
	
	public Attribute getHitPoints() {
		return hitPoints;
	}
	
	public Shield (int value, LinkedList mods) {
    	super(value,true);
    	hitPoints = new Attribute(Attribute.HIT_POINTS,10);
		chanceToBlock = value / 5;
		Type = getType(value);

		modifications = mods;
	}

	/**
	 * 
	 * @uml.property name="chance_to_block"
	 */
	public int getChanceToBlock() {

		if (hitPoints.perCent() >= 70) {
			return chanceToBlock;
		} else if (hitPoints.perCent() >= 50) {
			return (int) (2 * ((float) chanceToBlock) / 3);
		} else if (hitPoints.perCent() >= 40) {
			return (int) (1 * ((float) chanceToBlock) / 3);
		} else if (hitPoints.perCent() >= 30) {
			return (int) (1 * ((float) chanceToBlock) / 4);
		} else if (hitPoints.perCent() >= 20) {
			return (int) (1 * ((float) chanceToBlock) / 6);
		} else {
			return 0;
		}

	}

   	public void setName(String n ) {
		name = n;	
	}
	
//	public Color getStatusColor() {
//				Color color = Color.black;
//					if (hit_points.perCent() <= 50) {
//							 color = Color.red;
//							} else if (hit_points.perCent() <= 70) {
//								color = Color.yellow;
//						} 
//					
//			return color;
//		}

    public String toString(){
    	
		String s = "";
 //s+="<html><font color =";
//		
//					String color = "\"black\"";
//					if (hit_points.perCent() <= 50) {
//							 color = "\"red\"";
//							} else if (hit_points.perCent() <= 70) {
//								color = "\"yellow\"";
//							} 
//					s += color+">";
					
    	if(unique) {
    		s += name;	
    	}
    	else {
			s += (Type); 
    	}
    	s += ": "+chanceToBlock;
		if(isMagic()) {
		s += " (m)";
		}
//		s += "</font></html>";	
	
	return s;		
    
    }
    
    
	public int getBlockValue(){
			if(hitPoints.perCent() >= 70) {
				return chanceToBlock;
			}
			else if(hitPoints.perCent() >= 50) {
				return (int) (2*((float)chanceToBlock)/3);
			}
			else if(hitPoints.perCent() >= 40) {
				return (int) (1*((float)chanceToBlock)/3);
			}
			else if(hitPoints.perCent() >= 30) {
				return (int) (1*((float)chanceToBlock)/4);
			}
			else if(hitPoints.perCent() >= 20) {
				return (int) (1*((float)chanceToBlock)/6);
			}
			else {
				return 0;
			}
    	
		}

    public String getText(){
		String s = (JDEnv.getString("chance_to_block")+": "+getBlockValue()+"/"+chanceToBlock+"%"+"\n"+JDEnv.getString("state")+": "+(int)hitPoints.getValue()+"/"+(int)hitPoints.getBasic()+"\n");
		if(magic) {
    		for(int i = 0; i < modifications.size(); i++) {
    			ItemModification m = (ItemModification) modifications.get(i);
				s += m.getText() + "\n";
			}
    	}
		return s;
    }

	public void takeRelDamage(double d ) {
				double k = (int) (d * hitPoints.getValue());
				hitPoints.modValue((-1)*k); 
			}
}
