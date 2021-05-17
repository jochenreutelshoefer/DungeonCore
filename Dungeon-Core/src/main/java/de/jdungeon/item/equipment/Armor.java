package de.jdungeon.item.equipment;


import de.jdungeon.figure.attribute.ItemModification;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Paragraph;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.util.JDColor;

public class Armor extends ArmorItem<Armor> {

    //private String Type;

    public Armor(int value, boolean magic) {
        super(value / 10, value, magic, value);

    }

    private String getType(int value) {
        String type = "armor type";
        if (value <= 9) type = "T-Shirt";
        else if (value <= 20) type = JDEnv.getResourceBundle().getString("armor_shirt");
        else if (value <= 40) type = JDEnv.getResourceBundle().getString("armor2");
        else if (value <= 60) type = JDEnv.getResourceBundle().getString("armor3");
        else if (value <= 80) type = JDEnv.getResourceBundle().getString("armor4");
        else if (value <= 100) type = JDEnv.getResourceBundle().getString("armor5");
        else if (value <= 120) type = JDEnv.getResourceBundle().getString("armor6");
        else if (value <= 140) type = JDEnv.getResourceBundle().getString("armor7");
        else if (value <= 160) type = JDEnv.getResourceBundle().getString("armor8");
        else if (value <= 180) type = JDEnv.getResourceBundle().getString("armor9");
        else type = JDEnv.getResourceBundle().getString("armor10");
        return type;
    }

    @Override
    public Armor copy() {
        return new Armor(this.getWorth(), this.getModifications());
    }

    @Override
    protected String getTypeVerbalization() {
        return getType(getWorth());
    }

    public Armor(int value, List mods) {
        super(value / 10, value, true, value);
        modifications = mods;
    }

    public void setName(String n) {
        name = n;
    }

    @Override
    public Paragraph[] getParagraphs() {
        Paragraph[] p = new Paragraph[3];
        p[0] = new Paragraph(getName());
        p[0].setSize(24);
        p[0].setCentered();
        p[0].setColor(new JDColor(180, 18, 46));
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
        return getType(getWorth());

    }

    @Override
    public String toString() {
        String s = "";
        if (unique) {
            return name;
        }
        s += (getType(getWorth()) + ": " + this.armorValue);
        if (isMagic()) {
            s += "(m)";
        }
        return s;
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
    
