package de.jdungeon.dungeon.builder.serialization;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

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
			Class<?> itemClass = ClassReflection.forName(spellClazzName);
			Object newItemInstance = null;
			Constructor[] constructors = ClassReflection.getConstructors(itemClass);
			for (Constructor constructor : constructors) {
				// use the standard constructor if available
				if (constructor.getParameterTypes().length == 0) {
					newItemInstance = constructor.newInstance();
					break;
				}
			}
			spell = (AbstractSpell) newItemInstance;
		}
		catch (ReflectionException e) {
			Log.severe("Could not find location class for name: " + spellClazzName);
			e.printStackTrace();
		}
		return new ScrollMagic(spell);
	}
}
