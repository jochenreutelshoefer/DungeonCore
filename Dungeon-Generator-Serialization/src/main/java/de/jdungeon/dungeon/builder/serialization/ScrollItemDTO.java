package de.jdungeon.dungeon.builder.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.jdungeon.item.paper.ScrollMagic;
import de.jdungeon.log.Log;
import de.jdungeon.spell.AbstractSpell;

public class ScrollItemDTO extends ItemDTO {

	String spellClazzName;

	public ScrollItemDTO(Class<? extends AbstractSpell> spell) {
		super(spell);
		this.spellClazzName = spell.getName();
	}

	/**
	 * Required for JSON serialization
	 */
	public ScrollItemDTO() {
		super();
	}

	@Override
	public ScrollMagic create() {
		AbstractSpell spell = null;
		try {
			Class<?> itemClass = Class.forName(spellClazzName);
			Object newItemInstance = null;

			Constructor<?>[] constructors = itemClass.getConstructors();

			for (Constructor<?> constructor : constructors) {
				// use the standard constructor if available
				if (constructor.getParameterCount() == 0) {
					newItemInstance = constructor.newInstance();
					break;
				}
			}
			spell = (AbstractSpell) newItemInstance;
		}
		catch (ClassNotFoundException e) {
			Log.severe("Could not find location class for name: " + spellClazzName);
			e.printStackTrace();
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			Log.severe("Could not find/execute constructor for location class: " + spellClazzName);
			e.printStackTrace();
		}
		return new ScrollMagic(spell);
	}
}
