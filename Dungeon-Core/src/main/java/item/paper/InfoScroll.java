/*
 * Created on 17.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package item.paper;

/**
 * @author Jochen
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import dungeon.RoomEntity;
import figure.action.result.ActionResult;
import item.Item;
import item.interfaces.Usable;
import figure.Figure;
import figure.attribute.Attribute;
import figure.percept.TextPercept;
import game.JDEnv;

public class InfoScroll extends Item implements Usable {


    private final String content;
    private final String title;

    public InfoScroll(String title, String text) {
        super(5, false);
        content = text;
        this.title = title;
    }


    public Attribute getHitPoints() {
        return null;
    }

    @Override
    public boolean canBeUsedBy(Figure f) {
        return true;
    }

    @Override
    public boolean usableOnce() {
        return false;
    }

    @Override
    public boolean needsTarget() {
        return false;
    }

    @Override
    public int dustCosts() {
        return 0;
    }

    @Override
    public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
        if (doIt) {
            f.tellPercept(new TextPercept(content, -1));
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }
    }

    @Override
    public String getText() {
        return toString();
    }

    public String toString() {
        return JDEnv.getResourceBundle().getString("document") + ": " + title;
    }
}
