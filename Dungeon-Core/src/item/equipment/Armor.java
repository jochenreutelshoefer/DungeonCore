package item.equipment;


import java.util.*;
import java.awt.Color;

import figure.attribute.ItemModification;
import game.JDEnv;
import gui.Paragraph;
public class Armor extends ArmorItem {

    //private String Type;
	
	public Armor(int value, boolean magic) {
		super(value/10, value, magic, value);
		 Type = this.getType(value);

	}
	
	private String getType(int value) {
		String type = "armor type";
		 if (value <= 9) type ="T-Shirt";
		    else if (value <= 20) type = JDEnv.getResourceBundle().getString("armor_shirt");
		    else if(value <=40) type =JDEnv.getResourceBundle().getString("armor2");
		    else if(value <=60) type =JDEnv.getResourceBundle().getString("armor3");
		    else if(value <=80) type =JDEnv.getResourceBundle().getString("armor4");
		    else if(value <=100) type =JDEnv.getResourceBundle().getString("armor5");
		    else if(value <=120) type =JDEnv.getResourceBundle().getString("armor6");
		    else if(value <=140) type =JDEnv.getResourceBundle().getString("armor7");
		    else if(value <=160) type =JDEnv.getResourceBundle().getString("armor8");
		    else if(value <=180) type =JDEnv.getResourceBundle().getString("armor9");
		    else type = JDEnv.getResourceBundle().getString("armor10");
		return type;
	}
	
	public Armor(int value, LinkedList mods) {
		super(value/10, value, true, value);
	    
	    
	    
	 Type = this.getType(value);

		modifications = mods;
	}
	public void setName(String n ) {
		name = n;	
	}
	
	public Paragraph[] getParagraphs() {
		Paragraph []p = new Paragraph[3];
		p[0] = new Paragraph(getName());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(new Color(180,18,46));
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
//		s	+="<html><font color =";
//		
//				String color = "black";
//				if (hit_points.perCent() <= 50) {
//						 color = "red";
//						} else if (hit_points.perCent() <= 70) {
//							color = "yellow";
//						} 
//				s += color+">";
//				//s += "</font></html>";
    	if(unique) {
    		return name;	
    	}
	s += (Type+": "+Integer.toString(this.armorValue));
	if(isMagic()) {
		s += "(m)";
	}
//	s += "</font></html>";
	return s;		
    }

    public String getText(){
    	String s = super.getText(); 
    	//"Rüstung "+armor_value+"\n";
    	if(magic) {
    		for(int i = 0; i < modifications.size(); i++) {
    			ItemModification m = (ItemModification) modifications.get(i);
				s += m.getText() + "\n";
			}
    	}
		
	return s;
	

 	}

}
    
