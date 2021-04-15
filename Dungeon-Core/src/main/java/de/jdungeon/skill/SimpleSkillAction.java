package de.jdungeon.skill;

import de.jdungeon.figure.FigureInfo;
import de.jdungeon.skill.SimpleSkill;
import de.jdungeon.skill.SkillAction;

public class SimpleSkillAction extends SkillAction {

    public SimpleSkillAction(SimpleSkill skill, FigureInfo info) {
        super(skill, info);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + skill.toString();
    }
}
