package de.jdungeon.skill;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.skill.ActionBuilder;
import de.jdungeon.skill.SimpleSkill;
import de.jdungeon.skill.SimpleSkillAction;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.04.20.
 */
public class SimpleSkillActionBuilder<SKILL extends SimpleSkill, ACTION extends SimpleSkillAction> extends ActionBuilder<SKILL, ACTION> {

    private final Class<ACTION> clazz;

    public SimpleSkillActionBuilder(SKILL skill, FigureInfo actor, Class<ACTION> clazz) {
        super(skill, actor);
        this.clazz = clazz;
    }

    @Override
    public ACTION get() {
        return (ACTION)new SimpleSkillAction(this.skill, this.actor);
        /*
        try {
            com.badlogic.gdx.utils.reflect.Constructor constructor = ClassReflection.getConstructor(clazz, SimpleSkill.class, FigureInfo.class);
            return (ACTION) constructor.newInstance(this.skill, actor);
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
         */
        /*
        try {
            Constructor constructor = clazz.getConstructor(new Class[]{SimpleSkill.class, FigureInfo.class});
            return (ACTION)constructor.newInstance(this.skill, actor);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }

         */
        //return null;


    }
}
