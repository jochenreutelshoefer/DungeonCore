package de.jdungeon.figure.percept;

import de.jdungeon.skill.Skill;
import de.jdungeon.skill.SkillAction;

public class SkillActionPercept extends SimpleActorPercept {

    private final Skill skill;

    public SkillActionPercept(SkillAction action, int round) {
        super(action.getActor(), action.getActor().getRoom().getRoomNumber(), round);
        this.skill = action.getSkill();
    }

    public Skill getSkill() {
        return skill;
    }


}
