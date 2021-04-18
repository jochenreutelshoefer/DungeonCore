/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import de.jdungeon.spell.AbstractSpell;
import de.jdungeon.spell.SpellInfo;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class SpellPercept extends SimpleActorPercept {

    private Figure f;
    private AbstractSpell spell;
    private boolean begins = false;

    public SpellPercept(Figure f, AbstractSpell s, int round) {
        super(f, f.getRoomNumber(), round);
        this.spell = s;
    }

    public SpellPercept(Figure f, AbstractSpell s, boolean begins, int round) {
        super(f, f.getRoomNumber(), round);
        this.spell = s;
        this.begins = begins;
    }

    public SpellInfo getSpell(FigureInfo viewer) {
        return new SpellInfo(spell, viewer.getVisMap());
    }

    public boolean isBegins() {
        return begins;
    }

}
