package de.jdungeon.item.equipment;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.figure.attribute.ItemModification;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.util.JDColor;

public class Shield extends EquipmentItem<Shield> {

	/**
	 *
	 */
	private final int chanceToBlock;
	private final int dmgPerHP = 10;
	private int actualBlocks = 0;

	public Shield(int value, boolean magic) {
		super(value, magic);

		chanceToBlock = value / 5;

		typeVerbalization = getType(value);
	}

	private String getType(int value) {
		String Type = "shield type";
		if (value == 0) {
			Type = "Schild";
		}
		else if (value <= 20) {
			Type = JDEnv.getResourceBundle().getString("shield1");
		}
		else if (value <= 40) {
			Type = JDEnv.getResourceBundle().getString("shield2");
		}
		else if (value <= 60) {
			Type = JDEnv.getResourceBundle().getString("shield3");
		}
		else if (value <= 80) {
			Type = JDEnv.getResourceBundle().getString("shield4");
		}
		else if (value <= 100) {
			Type = JDEnv.getResourceBundle().getString("shield5");
		}
		else if (value <= 120) {
			Type = JDEnv.getResourceBundle().getString("shield6");
		}
		else if (value <= 140) {
			Type = JDEnv.getResourceBundle().getString("shield7");
		}
		else if (value <= 160) {
			Type = JDEnv.getResourceBundle().getString("shield8");
		}
		else if (value <= 180) {
			Type = JDEnv.getResourceBundle().getString("shield9");
		}
		else {
			Type = JDEnv.getResourceBundle().getString("shield10");
		}

		return Type;
	}

	public Shield(int value, boolean magic, int hitpoints) {
		super(value, magic);

		chanceToBlock = value / 5;
		typeVerbalization = getType(value);
	}

	public void madeBlock(int dmg) {
		actualBlocks += dmg;
		////System.out.println("Schl�ge: "+actualBlocks);
		if (actualBlocks >= dmgPerHP) {
			////System.out.println("Waffenzustand -1");
			int hp = dmg / dmgPerHP;
			actualBlocks = dmg % dmgPerHP;
		}
	}

	@Override
	public Paragraph[] getParagraphs() {
		Paragraph[] p = new Paragraph[3];
		p[0] = new Paragraph(getName());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(new JDColor(170, 140, 60));
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
		return typeVerbalization;
	}

	public Shield(int value, List mods) {
		super(value, true);
		chanceToBlock = value / 5;
		typeVerbalization = getType(value);

		modifications = mods;
	}

	/*
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
*/

	public void setName(String n) {
		name = n;
	}

//	public Color getStatusColor() {
	// Color color = JDColor.black;
//					if (hit_points.perCent() <= 50) {
	// color = JDColor.red;
//							} else if (hit_points.perCent() <= 70) {
	// color = JDColor.yellow;
//						} 
//					
//			return color;
//		}

	@Override
	public String toString() {

		String s = "";
		if (unique) {
			s += name;
		}
		else {
			s += (typeVerbalization);
		}
		s += ": " + chanceToBlock;
		if (isMagic()) {
			s += " (m)";
		}

		return s;
	}

	public int getBlockValue() {
		return chanceToBlock;
	}

	@Override
	public String getText() {
		String s = (JDEnv.getString("chance_to_block") + ": " + getBlockValue() + "/" + chanceToBlock + "%" + "\n" + JDEnv
				.getString("state") + ": " + "\n");
		if (magic) {
			for (int i = 0; i < modifications.size(); i++) {
				ItemModification m = (ItemModification) modifications.get(i);
				s += m.getText() + "\n";
			}
		}
		return s;
	}

	@Override
	public Shield copy() {
		return new Shield(getWorth(), getModifications());
	}
}
