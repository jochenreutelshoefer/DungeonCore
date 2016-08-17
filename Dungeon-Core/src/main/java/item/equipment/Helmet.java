package item.equipment;

import figure.attribute.ItemModification;
import game.JDEnv;
import gui.Paragraph;

import java.util.LinkedList;

import util.JDColor;

public class Helmet extends ArmorItem {

	

	public Helmet(int value, boolean magic) {

		super(value / 10, value, magic, 2 * value / 3);
		Type = getType(value);
	}

	public Helmet(int value) {
		super(value / 10, value, false, 2 * value / 3);
		Type = getType(value);
	}

	public Helmet(int value, LinkedList mods) {
		super(value / 10, value, true, 2 * value / 3);
		Type = getType(value);

		modifications = mods;
	}

	private String getType(int value) {
		String Type = "helmet type";
		if (value <= 10)
			Type = JDEnv.getResourceBundle().getString("helmet1");
		else if (value <= 20)
			Type = JDEnv.getResourceBundle().getString("helmet2");
		else if (value <= 40)
			Type = JDEnv.getResourceBundle().getString("helmet3");
		else if (value <= 60)
			Type = JDEnv.getResourceBundle().getString("helmet4");
		else if (value <= 80)
			Type = JDEnv.getResourceBundle().getString("helmet5");
		else if (value <= 100)
			Type = JDEnv.getResourceBundle().getString("helmet6");
		else if (value <= 120)
			Type = JDEnv.getResourceBundle().getString("helmet7");
		else if (value <= 140)
			Type = JDEnv.getResourceBundle().getString("helmet8");
		else if (value <= 160)
			Type = JDEnv.getResourceBundle().getString("helmet9");
		else if (value <= 180)
			Type = JDEnv.getResourceBundle().getString("helmet10");
		else
			Type = JDEnv.getResourceBundle().getString("helmet11");
		return Type;
	}

	@Override
	public Paragraph[] getParagraphs() {
		Paragraph[] p = new Paragraph[3];
		p[0] = new Paragraph(getName());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(new JDColor(100, 70, 40));
		p[0].setBold();

		p[1] = new Paragraph(toString());
		p[1].setSize(16);
		p[1].setCentered();
		p[1].setColor(JDColor.black);
		p[1].setBold();

		String s = getText();

		p[2] = new Paragraph(s);
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(JDColor.black);

		return p;
	}

	@Override
	public String getName() {
		if (unique) {
			return name;
		}
		return Type;

	}

	// public Color getStatusColor() {
	// Color color = Color.black;
	// if (hit_points.perCent() <= 50) {
	// color = Color.red;
	// } else if (hit_points.perCent() <= 70) {
	// color = Color.yellow;
	// }
	//					
	// return color;
	// }

	@Override
	public String toString() {
		String s = "";

		// s+= "<html><font color =";
		//		
		// String color = "\"black\"";
		// if (hit_points.perCent() <= 50) {
		// color = "\"red\"";
		// } else if (hit_points.perCent() <= 70) {
		// color = "\"yellow\"";
		// }
		// s += color+">";
		// //s += "</font></html>";
		if (unique) {
			return name;
		}
		s += (Type + ": " + Integer.toString(this.armorValue));
		if (isMagic()) {
			s += "(m)";
		}
		// s += "</font></html>";
		return s;
	}

	public void setName(String n) {
		name = n;
	}

	@Override
	public String getText() {
		String s = super.getText(); 
		
		if (magic) {
			for (int i = 0; i < modifications.size(); i++) {
				ItemModification m = (ItemModification) modifications.get(i);
				s += m.getText() + "\n";
			}
		}

		return s;

	}

}
