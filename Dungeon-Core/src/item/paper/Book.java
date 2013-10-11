package item.paper;
import item.Item;
import item.interfaces.Usable;
import figure.Figure;
import figure.attribute.Attribute;
import game.JDEnv;

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
	public abstract String getText();
	
	
	private static  String book = null; 
	protected static String book() {
		if(book == null) {
			book = JDEnv.getResourceBundle().getString("book");
		}
		return book;
		
	}
	
	

}
