/*
 * Created on 17.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package item.paper;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import dungeon.RoomEntity;
import item.Item;
import item.interfaces.Usable;
import figure.Figure;
import figure.attribute.Attribute;
import figure.percept.TextPercept;
import game.JDEnv;
public class InfoScroll extends Item implements Usable{


	private final String content;
	private final String title;

	public InfoScroll(String title, String text) {
		super(5,false);
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
	public boolean use(Figure f, RoomEntity target, boolean meta) {
		f.tellPercept(new TextPercept(content));
		return true;
	}

	@Override
	public String getText() {
		return toString();
	}
	public String toString() {
		return JDEnv.getResourceBundle().getString("document")+": "+title;
	}
}
