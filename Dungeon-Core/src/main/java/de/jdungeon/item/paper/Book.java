package de.jdungeon.item.paper;
import de.jdungeon.item.Item;
import de.jdungeon.item.interfaces.Usable;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.game.JDEnv;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class Book extends Item implements Usable{
	
	
	public Book(int worth) {
		super(worth,false);
	}

public Attribute getHitPoints() {
		return null;
	}
	/**
	 * @see Item#getText()
	 */
	@Override
	public abstract String getText();
	
	
	private static  String book = null; 
	protected static String book() {
		if(book == null) {
			book = JDEnv.getResourceBundle().getString("book");
		}
		return book;
		
	}
	
	

}
