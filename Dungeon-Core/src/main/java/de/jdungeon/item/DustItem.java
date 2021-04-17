/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package de.jdungeon.item;

import de.jdungeon.util.JDColor;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Paragraph;
public class DustItem extends Item<DustItem> {

	public static final double ratio = 2;

	private double cnt;


	@Override
	public DustItem copy() {
		return new DustItem(cnt);
	}

	public DustItem(int value) {
		super(value,false);
		cnt = (value/ratio);
	}
	
	public DustItem(double cnt) {
		super((int)(cnt* ratio),false);
		this.cnt = cnt;
	}
	
	public Attribute getHitPoints() {
		return null;
	}
	
	 @Override
	public Paragraph[] getParagraphs() {
		Paragraph []p = new Paragraph[2];
		p[0] = new Paragraph(getText());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(new JDColor(170, 190, 40));
		p[0].setBold();
		
		p[1] = new Paragraph(getText());
		p[1].setSize(16);
		p[1].setCentered();
		p[1].setColor(JDColor.black);
		p[1].setBold();
			
		return p;
	}

	/**
	 * @see Item#getText()
	 */
	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("dust")+": "+Math.round(cnt);
	}
	
	@Override
	public String toString() {
		return getText();
	}


	
	public double getCount() {
		return cnt;
	}


}
