/**
 * @author Duke1
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package de.jdungeon.item.paper;


import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.item.Item;
import de.jdungeon.item.interfaces.UsableWithTarget;
import de.jdungeon.spell.AbstractSpell;
import de.jdungeon.spell.TargetScope;
import de.jdungeon.spell.TargetSpell;
import de.jdungeon.util.JDColor;

public class Scroll extends Item<Scroll> implements UsableWithTarget {

    final AbstractSpell theSpell;

    public Scroll(AbstractSpell s, int cost) {
        super(s.getCost(), false);
        this.theSpell = s;
        s.setCost((int) (cost * 0.5));
    }

    @Override
    public boolean canBeUsedBy(Figure f) {
        return theSpell.isAbleToCast(f);
    }

    @Override
    public boolean usableOnce() {
        return true;
    }

    @Override
    public int dustCosts() {
        return theSpell.getCost();
    }

    @Override
    public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
        return theSpell.fire(f, target, doIt, round);
    }

    @Override
    public boolean needsTarget() {
        return theSpell instanceof TargetSpell;
    }

    @Override
    public TargetScope getTargetScope() {
        if (theSpell instanceof TargetSpell) {
            return ((TargetSpell) theSpell).getTargetScope();
        }
        return null;
    }


    private static String scroll = null;

    public static String scroll() {
        if (scroll == null) {
            scroll = JDEnv.getResourceBundle().getString("scroll");
        }
        return scroll;
    }

    @Override
    public String getText() {
       return this.theSpell.getText();
    }

    @Override
    public String toString() {
        String a = (scroll() + ": " + theSpell.getHeaderName() + "(" + theSpell.getLevel() + ")(" + theSpell.getCost() + ")");
        return a;
    }

    @Override
    public Scroll copy() {
        return new Scroll(theSpell, getWorth());
    }

    @Override
    public Paragraph[] getParagraphs() {
        Paragraph[] p = new Paragraph[3];
        p[0] = new Paragraph(scroll() + ": " + theSpell.getHeaderName());
        p[0].setSize(24);
        p[0].setCentered();
        p[0].setColor(JDColor.blue);
        p[0].setBold();

        p[1] = new Paragraph(JDEnv.getResourceBundle().getString("de/jdungeon/spell") + JDEnv.getResourceBundle().getString("level") + ": " + theSpell.getLevel());
        p[1].setSize(16);
        p[1].setCentered();
        p[1].setColor(JDColor.black);
        p[1].setBold();

        p[2] = new Paragraph((JDEnv.getResourceBundle().getString("cost") + ": " + theSpell.getCost()));
        p[2].setSize(14);
        p[2].setCentered();
        p[2].setColor(JDColor.black);


        return p;
    }


}
