package de.jdungeon.item.equipment.weapon;

import de.jdungeon.game.JDEnv;

import java.util.*;

public class Axe extends Weapon<Axe> {

    private static int BearModifierS = 30;
    private static int OgreModifierS = 0;
    private static int SkeletonModifierS = 0;
    private static int WolfModifierS = -30;
    private static int OrcModifierS = 0;
    private static int GhulModifierS = 50;


    public Axe(int worth, boolean magic) {
        super(worth, magic, getHitPoints(worth));
        tumbleBasicValue = 30;
        averageDamage = worth / (2 + (int) (Math.random() * 4));
        scatter = 1 + (int) (Math.random() * (averageDamage / 4));
        //Min_Damage = average - scatter;
        //Max_Damage = average + scatter;
        chanceToHit = (int) (((float) worth / averageDamage) * 6);
        setModifiers();
        name = JDEnv.getResourceBundle().getString("axe");

    }



    public Axe(int value, List mods) {

        super(value, true, getHitpoints(value));
        tumbleBasicValue = 30;
        averageDamage = worth / (2 + (int) (Math.random() * 4));
        scatter = 1 + (int) (Math.random() * (averageDamage / 4));
        //Min_Damage = average - scatter;
        //Max_Damage = average + scatter;
        chanceToHit = (int) (((float) worth / averageDamage) * 6);
        setModifiers();
        name = JDEnv.getResourceBundle().getString("axe");

        modifications = mods;
    }

    private static int getHitpoints(int value) {
        return 2 * value / 3;
    }

    protected void setModifiers() {
        bearModifier = BearModifierS;
        ogreModifier = OgreModifierS;
        skeletonModifier = SkeletonModifierS;
        wolfModifier = WolfModifierS;
        orcModifier = OrcModifierS;
        ghulModifier = GhulModifierS;

        RANGE_NEAR = 100;
        RANGE_FAR = 40;
        RANGE_MID = 140;

    }


    @Override
    public Axe copy() {
        return new Axe(getWorth(), getModifications());
    }
}
