package de.jdungeon.item.paper;
import de.jdungeon.spell.AbstractSpell;

public class ScrollMagic extends Scroll {
	
	public ScrollMagic(AbstractSpell s) {
		super(s, 0);
	}
	

	public String toString() {
		String a = (Scroll.scroll()+": "+theSpell.getHeaderName()+"("+theSpell.getLevel()+")");
		return a;
	}
}
