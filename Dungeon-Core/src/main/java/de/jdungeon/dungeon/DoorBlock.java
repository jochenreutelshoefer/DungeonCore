
package de.jdungeon.dungeon;

import java.util.LinkedList;

import de.jdungeon.figure.Figure;


/**
 * Temporaere Blockierung einer Tuer
 *
 */
public class DoorBlock {
	
	Object source;
	LinkedList excludes = new LinkedList();
	
	public DoorBlock(Object o) {
		source = o;
	}
	
	public DoorBlock(Object o, Figure ex)  {
		source = o;
		excludes.add(ex);
	}
	
	public void addExclude(Figure f) {
		excludes.add(f);
	}
	
	public boolean containsExclude(Figure f) {
		return excludes.contains(f);
	}

	/**
	 * @return Returns the source.
	 */
	public Object getSource() {
		return source;
	}
}
