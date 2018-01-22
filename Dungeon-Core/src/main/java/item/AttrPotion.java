package item;

import figure.Figure;
import figure.attribute.Attribute;
import figure.attribute.TimedAttributeModification;
import figure.percept.TextPercept;
import game.JDEnv;
import gui.Paragraph;
import gui.Texts;
import item.interfaces.Usable;

import java.util.LinkedList;

import util.JDColor;

public class AttrPotion extends Item implements Usable {

	private final int a;

	
	public static final int delay = 30;

	
	public AttrPotion(int attribute, int value) {
		super(value, false);
		a = attribute;

		
		Type = setType();
	}
	
	@Override
	public boolean needsTarget() {
		return false;
	}


	@Override
	public int getItemKey() {
		return Item.ITEM_KEY_ATTRPOTION;
	}

	public Attribute getHitPoints() {
		return null;
	}
	
	@Override
	public boolean canBeUsedBy(Figure f) {
		return f.getAttribute(a) != null;
	}

	@Override
	public boolean usableOnce() {
		return true;
	}

	public int getAttribut() {
		return a;
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure h,Object target,boolean meta) {
		//Hero held = (Hero) h;
		h.tellPercept(new TextPercept(Texts.getPoitionDrinkString(a)));

		LinkedList modifications = getModifications(h);
		int l = modifications.size();
		for (int i = 0; i < l; i++) {
			h.addModification(
					new TimedAttributeModification(h.getAttribute(a),
							(PotionMod) modifications.get(i)));
		}

		h.removeItem(this);
		return true;
	}

	@Override
	public String toString() {
		return Type;
	}

	@Override
	public String getText() {
		return toString();
	}

	@Override
	public String getName() {
		if (a ==(Attribute.HEALTH)) {
			return JDEnv.getResourceBundle().getString("heal_potion");
		}
		return JDEnv.getResourceBundle().getString("elixier")+" " + Texts.getAttributeName(a);
	}

	@Override
	public Paragraph[] getParagraphs() {
		Paragraph[] p = new Paragraph[2];
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

		return p;
	}

	public LinkedList getModifications(Figure held) {
		LinkedList mods = new LinkedList();
		if ((a ==(Attribute.STRENGTH))
				|| (a==(Attribute.DEXTERITY))
				|| (a==(Attribute.PSYCHO))) {

			int sum = worth / 5;
			int half = sum / 2;
			int rest = sum - half;
			int quarter1 = rest / 2 + 1;
			int quarter2 = rest - quarter1;

			mods.add(new PotionMod(half, 0));
			mods.add(new PotionMod(quarter1, 1));
			mods.add(new PotionMod(quarter2, 2));

			int i = 0;
			while (i < sum) {
				mods.add(new PotionMod(-1, i + delay));
				i++;
			}

		} else if ((a == (Attribute.AXE) || (a == (Attribute.CLUB))
				|| (a == (Attribute.LANCE)) || (a == (Attribute.SWORD)) || (a
				== (Attribute.WOLFKNIFE)))) {

			int sum = 5 * worth / 7;
			int half = sum / 2;
			int rest = sum - half;
			int quarter1 = rest / 2 + 1;
			int quarter2 = rest - quarter1;

			mods.add(new PotionMod(half, 0));
			mods.add(new PotionMod(quarter1, 1));
			mods.add(new PotionMod(quarter2, 2));

			int i = 0;
			while (i < sum) {

				mods.add(new PotionMod(-1, i + delay));
				i++;
			}

		} else if ((a == (Attribute.NATURE_KNOWLEDGE))
				|| (a == (Attribute.CREATURE_KNOWLEDGE))
				|| (a == (Attribute.UNDEAD_KNOWLEDGE))) {

			int sum = 5 * worth / 8;
			int half = sum / 2;
			int rest = sum - half;
			int quarter1 = rest / 2 + 1;
			int quarter2 = rest - quarter1;

			mods.add(new PotionMod(half, 0));
			mods.add(new PotionMod(quarter1, 1));
			mods.add(new PotionMod(quarter2, 2));

			int i = 0;
			// //System.out.println("�bergabe-while:");
			while (i < sum) {
				// System.out.println(Integer.toString(i));
				mods.add(new PotionMod(-1, i + delay));
				i++;
			}
		} else if (a == (Attribute.SCOUT)) {
			int sum = worth / 10;
			int half = worth / 2;
			int rest = sum - half;
			int quarter1 = rest / 2 + 1;
			int quarter2 = rest - quarter1;

			mods.add(new PotionMod(half, 0));
			mods.add(new PotionMod(quarter1, 1));
			mods.add(new PotionMod(quarter2, 2));

			int i = 0;
			while (i < sum) {
				mods.add(new PotionMod(-1, i + 4));
				i++;
			}

		} else if (a == (Attribute.THREAT)) {
			int sum = worth / 9;
			int half = sum / 2;
			int rest = sum - half;
			int quarter1 = rest / 2 + 1;
			int quarter2 = rest - quarter1;

			mods.add(new PotionMod(half, 0));
			mods.add(new PotionMod(quarter1, 1));
			mods.add(new PotionMod(quarter2, 2));

			int i = 0;
			while (i < sum) {
				mods.add(new PotionMod(-1, i + delay));
				i++;
			}

		} else if (a == (Attribute.HEALTH)) {

			int max = (int) (held.getHealth().getBasic());
			int h = worth;
			int one = worth / 3;
			// //System.out.println("attHelps: ");
			PotionMod m = new PotionMod(one, 0);
			// //System.out.println("erstes");
			mods.add(m);
			h -= one;
			int i = 1;
			while (h > 0) {
				one--;
				if (h >= one) { // wenn rest groesser h
					if (one == 0) {
						one = 1;
					}
					// //System.out.println("new attHelp, if");
					// Damit jeder Punkt 2 Prozent gibt
					mods.add(new PotionMod(
							(int) ((one * 2 * ((double) max) / 100)), i)); //
					h -= one;
				} else {
					// //System.out.println("new attHelp, else");
					mods.add(new PotionMod(
							(int) ((h * 2 * ((double) max) / 100)), i));
					h = 0;
				}
				i++;
			}

		}

		return mods;
	}

	private String setType() {

		if (a == Attribute.HEALTH) {

			if (worth <= 10) {
				return JDEnv.getResourceBundle().getString("heal_potion_small");
			} else if (worth <= 20) {
				return JDEnv.getResourceBundle().getString("heal_potion");
			} else if (worth <= 30) {
				return JDEnv.getResourceBundle().getString("heal_potion_big");
			} else if (worth <= 40) {
				return JDEnv.getResourceBundle().getString("heal_potion_strong");
			} else
				return JDEnv.getResourceBundle().getString("heal_potion_con");
		} else {
			if (worth <= 10) {
				return new String(JDEnv.getResourceBundle().getString("elixier_small")+": " + Texts.getAttributeName(a));
			} else if (worth <= 20) {
				return new String(JDEnv.getResourceBundle().getString("elixier")+": " + Texts.getAttributeName(a));
			} else if (worth <= 30) {
				return new String(JDEnv.getResourceBundle().getString("elixier_big")+": " + Texts.getAttributeName(a));
			} else if (worth <= 40) {
				return new String(JDEnv.getResourceBundle().getString("elixier_strong")+": " + Texts.getAttributeName(a));
			} else
				return new String(JDEnv.getResourceBundle().getString("elixier_con")+": "
						+ Texts.getAttributeName(a));
		}
	}
}
