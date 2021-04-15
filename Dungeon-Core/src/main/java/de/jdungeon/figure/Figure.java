package de.jdungeon.figure;

import java.io.Serializable;
import java.util.*;

import de.jdungeon.ai.AI;
import de.jdungeon.ai.AbstractReflexBehavior;
import de.jdungeon.dungeon.Dir;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.DungeonWorldObject;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.dungeon.util.DungeonUtils;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.event.EventManager;
import de.jdungeon.event.WorldChangedEvent;
import de.jdungeon.game.*;
import de.jdungeon.skill.attack.Slap;
import de.jdungeon.skill.attack.SlapResult;
import de.jdungeon.figure.action.AbstractExecutableAction;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.figure.action.ScoutAction;
import de.jdungeon.figure.action.ScoutResult;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.figure.attribute.TimedAttributeModification;
import de.jdungeon.figure.hero.Character;
import de.jdungeon.figure.hero.Inventory;
import de.jdungeon.figure.memory.FigureMemory;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.other.ConjuredMagicFigure;
import de.jdungeon.figure.percept.DiePercept;
import de.jdungeon.figure.percept.DoorSmashPercept;
import de.jdungeon.figure.percept.EntersPercept;
import de.jdungeon.figure.percept.FightEndedPercept;
import de.jdungeon.figure.percept.HitPercept;
import de.jdungeon.figure.percept.LeavesPercept;
import de.jdungeon.figure.percept.MissPercept;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.figure.percept.ShieldBlockPercept;
import de.jdungeon.figure.percept.StepPercept;
import de.jdungeon.figure.percept.WaitPercept;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.gui.Paragraphable;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.item.Key;
import de.jdungeon.item.interfaces.ItemOwner;
import de.jdungeon.log.Log;
import de.jdungeon.skill.*;
import de.jdungeon.skill.attack.AttackSkill;
import de.jdungeon.spell.Spell;
import de.jdungeon.spell.SpellInfo;
import de.jdungeon.util.JDColor;

public abstract class Figure extends DungeonWorldObject
        implements ItemOwner, Turnable, VisibilityModifier, Paragraphable, Serializable, RoomEntity {

    public static final int STATUS_HEALTHY = 4;

    public static final int STATUS_WEAK = 3;

    public static final int STATUS_INJURED = 2;

    public static final int STATUS_WOUNDED = 1;

    public static final int STATUS_CRITICAL = 0;

    public APAgility getAgility() {
        return agility;
    }

    protected APAgility agility;
    protected Position pos = null;

    protected Room room = null;

    protected int blinded = 0;

    protected int cobwebbed = 0;

    protected ControlUnit control;
    private AI ai;
    protected AbstractReflexBehavior reflexReactionUnit;

    private int figureID = -1;

    protected Spellbook spellbook;

    private static int figureID_counter = 1;

    protected int level;

    protected int lookDir = 1;

    private int golden_hit = 0;

    public int escape = 0;

    public boolean double_bonus = false;

    public boolean half_bonus = false;

    public boolean raiding = false;

    private List<Poisoning> poisonings = new LinkedList<Poisoning>();

    protected String status = (" sieht noch recht stark aus.");

    private String shortStatus = ("stark");

    protected boolean dead = false;

    protected DungeonVisibilityMap visibilities;

    protected JDPoint location;

    private boolean bonusLive = false;

    public boolean isBonusLive() {
        return bonusLive;
    }

    protected Room respawnRoom = null;

    public Room getRespawnRoom() {
        return respawnRoom;
    }

    public void setRespawnRoom(Room respawnRoom) {
        this.respawnRoom = respawnRoom;
    }

    @Override
    public FigureMemory getMemoryObject(FigureInfo info) {
        return new FigureMemory(this, info);
    }

    public void setBonusLive(boolean bonusLive) {
        this.bonusLive = bonusLive;
    }

    public abstract String getName();

    @Override
    public Collection<Position> getInteractionPositions() {
        Collection<Position> result = new HashSet<>();
        Position currentPos = this.getPos();
        result.add(currentPos.getNext());
        result.add(currentPos.getPrevious());
        // add current pos also, to allow a de.jdungeon.figure to interact with its own items
        result.add(currentPos);
        return result;
    }

    protected boolean wayPassable(Door d, Room toGo) {

        if ((d != null) && (d.isPassable(this)) && (toGo != null)) {
            return !(toGo.hasStatue() && this instanceof Monster);
        }
        return false;
    }

    @Override
    public boolean stillValid() {
        // a de.jdungeon.figure itself is always a valid visibility modifier for itself
        return true;
    }

    @Override
    public int getVisibilityStatus() {
        return 4;
    }

    public boolean hasKey(String k) {
        List<Item> items = getItems();
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            if (it instanceof Key) {
                if (((Key) it).getType().equals(k)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void decActionPoints(Action action, int round) {
        this.agility.payActionPoint(action, round);
    }

    public void poison(Poisoning p) {
        poisonings.add(p);
    }

    @Override
    public boolean addItems(List<Item> l, ItemOwner o) {
        for (int i = 0; i < l.size(); i++) {
            Item it = (l.get(i));
            this.takeItem(it);
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Figure figure = (Figure) o;
        return figureID == figure.figureID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(figureID);
    }

    private void sufferPoisonings(int round) {
        for (int i = 0; i < poisonings.size(); i++) {
            Poisoning p = (poisonings.get(i));
            if (p.getTime() == 0) {
                poisonings.remove(p);
            } else {
                p.sufferRound(this, round);
            }
        }
    }

    public void healPoisonings() {
        poisonings = new LinkedList<>();
    }

    public abstract Attribute getAttribute(Attribute.Type type);

    public abstract Attribute getPsycho();

    public abstract Attribute getStrength();

    public abstract void recDust(double k);

    @Override
    public abstract boolean removeItem(Item i);

    // todo: unify
    public abstract void receiveSlapResult(SlapResult r);

    @Override
    public String toString() {
        return this.getClass().getName() + " " + getName() + " " + this.pos.toString();
    }

    public void tellPercept(Percept p) {
        p.perceivedBy(this);
        if (control != null) {
            control.tellPercept(p);
        }
    }

    protected abstract List getModificationList();

    public void payActionPoint(Action action, int round) {
        agility.payActionPoint(action, round);
    }

    protected void dieAndLeave() {
        Log.info("Figure dies:  " + this);
        dead = true;
        this.getRoom().figureDies(this);
        getActualDungeon().removeFigureFromIndex(this);
    }

    private boolean payDust(double value) {
        if (this.getDust().getValue() >= value) {
            this.getDust().modValue((-1) * value);
            return true;
        }
        return false;
    }

    public boolean payDust(int val) {
        return payDust((double) val);
    }

    public void heal(double value, int round) {
        Attribute healthAttr = getHealth();
        double before = healthAttr.getValue();
        if (healthAttr.getValue() + value <= healthAttr.getBasic()) {
            healthAttr.modValue(value);
        } else {
            healthAttr.setValue((healthAttr.getBasic()));
        }
        double after = healthAttr.getValue();
        //Log.info(this.getName()+ " recovers from " +before +" to "+ after+ " (max: "+healthAttr.getBasic()+")"+ " [Round: "+round+"]");
    }

    public void heal(int value, int round) {
        heal((double) value, round);
    }

    public int getFigureID() {
        if (this.figureID == -1) {
            throw new IllegalStateException("FigureId not set, check correct Figure initialization.");
        }
        return this.figureID;
    }

    public abstract double getFireResistRate();

    public abstract double getMagicResistRate();

    public abstract double getLightningResistRate();

    public abstract double getPoisonResistRate();

    public abstract boolean isAbleToTakeItem(Item it);

    public abstract boolean isAbleToTakeItemInFight(Item it);

    @Override
    public abstract List<Item> getItems();

    public Room getRoomInfo() {
        return getRoom().getDungeon().getRoom(getRoomNumber());
    }

    public int getLastRoundTurnCompleted() {
        return lastTurnCompleted;
    }

    private int lastTurnCompleted = -1;

    private void timeTick(int round) {

        if (lastTurnCompleted < round) {
            fireModifications();
            recover(round);
            this.sufferPoisonings(round);
            // here the de.jdungeon.figure gets his AP (and oxygen management happens)
            this.agility.turn(round);
        }


    }

    @Override
    public void turn(int round, DungeonWorldUpdater worldUpdater) {
        timeTick(round);

        if (lastTurnCompleted < round) {
            final DungeonVisibilityMap roomVisibility = getRoomVisibility();
            if (roomVisibility != null) {
                roomVisibility.resetTemporalVisibilities();
            }
        }


        // do the actions
        if (getActionPoints() > 0 && !isDead()) {
            Room room = this.getRoom();
            if (room == null) return; // some de.jdungeon.level exit concurrency issue...

            doActions(round, room.fightRunning(), worldUpdater);

            // might be that after an action the de.jdungeon.fight is resolved
            if (room.fightRunning() && !room.checkFightOn()) {
                room.endFight(round);
            }
        }

        // round completed?
        if (worldUpdater.getGameLoopMode() == GameLoopMode.RenderThreadWorldUpdate) {
            if ((getActionPoints() == 0 || isDead())) {
                // in RenderThreadWorldUpdate mode, the round is completed if figure has no APs or dead
                this.lastTurnCompleted = round;

            }
        } else {
            // in DisinctWorldThread mode at this point the round is always completed for this figure
            this.lastTurnCompleted = round;
        }
    }

    protected abstract void sanction(int i);

    private boolean hurt(int value) {
        Log.info(this.getName() + " receives damage: " + value);
        Attribute h = this.getHealth();
        h.modValue(value * (-1));
        this.setStatus(this.getHealthLevel().getValue()); // TODO: refactor
        return (h.getValue() <= 0);
    }

    protected abstract int getAllArmor(Slap s);

    protected abstract void recover(int round);

    public abstract Attribute getDexterity();

    public int calcEludeValue() {
        double dex = this.getDexterity().getValue();
        if (this.blinded > 0) {
            dex *= 0.6;
        }
        return (int) dex;
    }

    protected int filterArmor(int allArmor, int value) {
        if (value < allArmor) {
            return value / 2;
        } else if (value < allArmor * 2) {
            return (int) (value / 1.5);
        } else {

            int v = (int) (value - (1.2 * (allArmor)));
            if (v <= 0) {
                return 1;
            } else {
                return v;
            }
        }
    }

    protected abstract boolean getBlock(int dmg);

    protected int hit(Slap s, int round) {

        int slap = s.getValueStandard();
        int slap_fire = s.getValueFire();
        int slap_lightning = s.getValueLightning();
        int slap_magic = s.getValueMagic();
        int slap_poison = s.getValue_poison();

        int all_armor = getAllArmor(s);

        int slapDmg = filterArmor(all_armor, slap);

        int fireDmg = (int) (slap_fire * getFireResistRate());
        int lightningDmg = (int) (slap_lightning * getLightningResistRate());
        int poisonDmg = (int) (slap_poison * getPoisonResistRate());
        int magicDmg = (int) (slap_magic * getMagicResistRate());

        return slapDmg + fireDmg + lightningDmg + poisonDmg + magicDmg;
    }

    public SlapResult getSlap(Slap s, int round) {

        Figure attacker = s.getActor();

        int eludeValue = calcEludeValue();

        float precision = s.getPrecision();

        int chance = (int) (precision - precision * ((((double) eludeValue) / 100)));

        boolean dies = false;
        SlapResult res = null;
        Percept p = null;
        int allDmg = 0;

        int random = (int) (Math.random() * 100);

        if (random < chance) {
            // we change look-dir towards opponent if slap does not miss
            lookDir = Position.getDirFromTo(getPositionInRoom(),
                    attacker.getPositionInRoom());

            if (getBlock(s.getValueStandard())) {
                p = new ShieldBlockPercept(this, round);
                res = new SlapResult(false, this, allDmg, s);
            } else {
                allDmg = hit(s, round);
                int healthLevelBefore = getHealthLevel().getValue();
                if (allDmg < 0) {
                    allDmg = 0;
                }

                dies = hurt(allDmg);

                int healthLevelAfter = getHealthLevel().getValue();

                if (healthLevelBefore != healthLevelAfter) {
                    sanction(healthLevelBefore - healthLevelAfter);
                }
                if (dies) {
                    getKilled(allDmg);
                    gameOver();
                }
                res = new SlapResult(dies, this, allDmg, s);
                p = new HitPercept(attacker, this, res, round);
            }
        } else {
            res = new SlapResult(false, this, allDmg, s);
            p = new MissPercept(attacker, this, round);
        }

        this.getRoom().distributePercept(p);
        if (dies) {
            this.getRoom().distributePercept(new DiePercept(this, this.getRoom(), round));
        }

        return res;
    }

    public void doActions(int round, boolean fight, DungeonWorldUpdater worldUpdater) {

        while (control != null && !this.isDead() && this.agility.getCurrentAP() > 0) {

            if (control instanceof JDGUI) {
                ((JDGUI) control).onTurn();
            }


            // retrieve action
            Action a = retrieveAction(worldUpdater);

            if (worldUpdater.getGameLoopMode() == GameLoopMode.RenderThreadWorldUpdate && a == null) {
                // we need to quit as we are in the render thread!
                return;
            }

            final Room room = this.getRoom();
            if (room == null || room.getDungeon() == null || room.getDungeon().isGameOver()) {
                // de.jdungeon.figure has left dungeon
                break;
            }

            ActionResult res = executeAction(a, round);
            if (res.equals(ActionResult.DONE)) {
                EventManager.getInstance().fireEvent(new WorldChangedEvent());
            }

            if (control == null) {
                break;
            }
            control.actionProcessed(a, res, round);

            if (isDead() || room.getDungeon().isGameOver()) {
                break;
            }

			/*
			if (a instanceof EndRoundAction) {
				// end round doesn't actually do anything but skip turn (for instance to wait for new action points in the next round).
				Log.info(round + " "+ this.getName() + " " + a.getClass().getSimpleName());
				control.actionProcessed(a, ActionResult.DONE);

				int ap = this.getActionPoints();
				for (int j = 0; j < ap; j++) {
					room.distributePercept(new WaitPercept(this));
				}
			}
			*/
        }
    }

    public abstract int getKnowledgeBalance(Figure f);

    public abstract boolean flee(RouteInstruction.Direction dir, int round);

    public boolean canPayActionPoints(int k) {
        return agility.canPayActionpoints(k);
    }

    public double getAntiFleeValue() {
        return 0;
    }

    public boolean isDead() {
        return dead;
    }

    @Deprecated // todo: refactor unwrapping
    public Item getItemForInfo(ItemInfo item) {
        List<Item> allItems = this.getAllItems();
        //allItems.forEach(i -> System.out.println(i.isMagic()));
        for (Item it : allItems) {
            ItemInfo itemInfo = ItemInfo.makeItemInfo(it, getRoomVisibility());
            if (itemInfo.equals(item)) {
                return it;
            }
        }
        return null;
    }

    public abstract boolean layDown(Item it);

    protected void gameOver() {
        if (control instanceof JDGUI) {
            ((JDGUI) control).gameOver();
        }
    }

    public boolean fightEnded(List<Figure> figures, int round) {
        if (this instanceof ConjuredMagicFigure) {
            ConjuredMagicFigure conjuredMagicFigure = (ConjuredMagicFigure) this;
            if (conjuredMagicFigure.disappearAtEndOfFight()) {
                conjuredMagicFigure.disappear(round);
            }
            return true;
        }
        if (getControl() != null) {
            tellPercept(new FightEndedPercept(FigureInfo.makeInfos(figures, this), round));
        }
        return false;
    }

    public SkillMap getSkillSet() {
        return skillSet;
    }

    private final SkillMap skillSet = new SkillMap();

    public <T extends Skill> T getSkill(Class<T> clazz) {
        return skillSet.get(clazz);
    }

    private boolean doorSmashes(Door d, Figure other, int round, int strengthDiff) {

        Position pos = this.getPos();
        Position posN1 = pos.getPrevious();
        Position posN2 = pos.getNext();
        if (posN1.getFigure() == null && posN2.getFigure() == null) {
            if (Math.random() > 0.5) {
                doStepTo(posN1.getIndex(), pos.getIndex(), round);
            } else {
                doStepTo(posN2.getIndex(), pos.getIndex(), round);
            }
            getDoorSmash(d, other, false, round, strengthDiff);
            return true;
        } else if (posN1.getFigure() == null) {
            doStepTo(posN1.getIndex(), pos.getIndex(), round);
            getDoorSmash(d, other, false, round, strengthDiff);
            return true;
        } else if (posN2.getFigure() == null) {
            doStepTo(posN2.getIndex(), pos.getIndex(), round);
            getDoorSmash(d, other, false, round, strengthDiff);
            return true;
        } else {
            getDoorSmash(d, other, true, round, strengthDiff);
            return false;
        }
    }

    private void doorSmashesBack(Door d, Figure other, int round, int strengthDiff) {
        getDoorSmash(d, other, false, round, strengthDiff);
    }

    private void getDoorSmash(Door d, Figure other, boolean bigSmash, int round, int strengthDiff) {
        int healthBasic = (int) this.getHealth().getBasic();
        int value;
        int strengthDiffValue = (int) (((double) strengthDiff) / 2.5);
        if (bigSmash) {
            value = (healthBasic / 10) + strengthDiffValue;
        } else {
            value = (healthBasic / 20) + strengthDiffValue;
        }
        if (value <= 0) value = 1;

        Percept p = new DoorSmashPercept(this, other, value, round);
        d.getRooms()[0].distributePercept(p);
        d.getRooms()[1].distributePercept(p);
        this.hurt(value);
    }

    ActionResult checkAction(Action action) {
        return processAction(action, false, -1);
    }

    private ActionResult executeAction(Action a, int round) {

		/*
		Set change world transaction lock (thread safety for render loop thread)
		 */
        this.getActualDungeon().setTransactionLock();


		/*
		Do actual action (and change the world accordingly)
		 */
        ActionResult actionResult = processAction(a, true, round);


		/*
		Release change world transaction lock
		 */
        Dungeon actualDungeon = this.getActualDungeon();
        // elvis might have just left the building
        if (actualDungeon != null) {
            actualDungeon.releaseTransactionLock();
        }
        return actionResult;
    }

    public abstract boolean canTakeItem(Item i);

    private ActionResult processAction(Action action, boolean doIt, int round) {

        if (doIt) {
            Log.info(System.currentTimeMillis() + " " + round + " do: " + round + " " + this.getName() + " [AP: " + this.agility
                    .getCurrentAP() + "] " + action.getClass().getSimpleName() + "(" + action.toString() + ")");
        }

        if (action == null) {
            return null;
        }

        // todo: continue refactoring towards skills
        if (action instanceof AbstractExecutableAction) {
            ActionResult actionResult = ((AbstractExecutableAction) action).handle(doIt, round);
            if (doIt && actionResult.getSituation() == ActionResult.Situation.done) {
                this.payActionPoint(action, round);
            }
            return actionResult;
        }

        if (action instanceof SkillAction) {
            return ((SkillAction) action).getSkill().execute(((SkillAction) action), doIt, round);
        }

        if (action instanceof EndRoundAction) {
            if (doIt) {
                this.payActionPoint(action, round);
                Percept p = new WaitPercept(this, round);
                getRoom().distributePercept(p);
                return ActionResult.DONE;
            } else {
                return ActionResult.POSSIBLE;
            }
        }

        return ActionResult.UNKNOWN;
    }

    public abstract boolean isAbleToUseShrine();

    public abstract boolean isAbleToUseChest();

    public void doStepTo(int targetFieldindex, int oldPosIndex, int round) {
        Position newPos = getRoom().getPositions()[targetFieldindex];

        this.lookDir = Position.getDirFromTo(pos.getIndex(), targetFieldindex);
        pos.figureLeaves();
        newPos.setFigure(this);
        StepPercept p = new StepPercept(this, oldPosIndex, targetFieldindex, round);
        pos = newPos;
        getRoom().distributePercept(p);
    }

    /**
     * Returns the position index that is required to be at
     * to move in the given direction
     */
    public static int getDirPos(int dir) {
        if (dir == Dir.NORTH) {
            return 1;
        }
        if (dir == Dir.EAST) {
            return 3;
        }
        if (dir == Dir.SOUTH) {
            return 5;
        }
        if (dir == Dir.WEST) {
            return 7;
        }
        return -1;
    }

    @Deprecated // unify across different de.jdungeon.figure types
    public abstract ScoutResult scout(ScoutAction action, int round);

    public abstract int getKilled(int dmg);

    public abstract Attribute getHealth();

    public abstract void addModification(TimedAttributeModification mod);

    protected Dungeon actualDungeon;

    public Figure(Dungeon d) {
        if (d != null) {
            setActualDungeon(d);
        }
        constrHelp();
    }

    public Figure() {
        constrHelp();
    }

    public Figure(ControlUnit control) {
        this.control = control;
        constrHelp();
    }

    public Figure(AI ai) {
        this.ai = ai;
        constrHelp();
    }

    private void constrHelp() {
        status = JDEnv.getResourceBundle().getString("status_strong");
        shortStatus = JDEnv.getResourceBundle()
                .getString("status_short_strong");

        agility = createAgility();

        this.skillSet.put(AttackSkill.class, new AttackSkill());
        this.skillSet.put(FleeSkill.class, new FleeSkill());
        this.skillSet.put(ScoutSkill.class, new ScoutSkill());
        this.skillSet.put(EagleOwlSkill.class, new EagleOwlSkill());

        this.figureID = figureID_counter;
        figureID_counter++;
    }

    protected abstract APAgility createAgility();

    public void setActualDungeon(Dungeon d) {
        actualDungeon = d;
        if (d != null) {
            createVisibilityMap(d);
            if (ai != null) {
                // TODO: this de.jdungeon.ai field should nou be used
                FigureInfo info = FigureInfo.makeFigureInfo(this, getRoomVisibility());
                ai.setFigure(info);
                this.control = new FigureControl(info, ai);
            }
        }
    }

    /**
     * May not be called during de.jdungeon.game, but only between dungeons
     * for de.jdungeon.level management.
     */
    public void clearVisibilityMaps() {
        visibilities = null;
    }

    public DungeonVisibilityMap createVisibilityMap(Dungeon d) {
        if (visibilities == null) {
            visibilities = new DungeonVisibilityMap(this, d);
        }
        return visibilities;
    }

    public abstract Attribute getDust();

    public abstract float getActualChanceToHit(Figure m);

    public abstract int getActualRangeCapability(int range);

    public RoomObservationStatus getRoomObservationStatus(JDPoint p) {
        return getRoomVisibility().getStatusObject(p);
    }

    public abstract int getSlapStrength(Figure m);

    public Inventory getInventory() {
        return null;
    }

    public Character getCharacter() {
        return null;
    }

    protected int worth;

    @Override
    public Room getRoom() {
        return room;
    }

    public int getGolden_hit() {
        return golden_hit;
    }

    public Spellbook getSpellbook() {
        return spellbook;
    }

    public void setGolden_hit(int golden_hit) {
        this.golden_hit = golden_hit;
    }

    public boolean canPayDust(int amount) {
        return canPayDust((double) amount);
    }

    public boolean canPayDust(double amount) {
        return this.getDust().getValue() >= amount;
    }

    @Override
    public abstract boolean takeItem(Item i);

    public void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }

    public void setEscape(int escape) {
        this.escape = escape;
    }

    public abstract List<Item> getAllItems();

    @Override
    public boolean hasItem(Item i) {
        List<Item> items = getAllItems();
        return items.contains(i);
    }

    public void setDouble_bonus(boolean double_bonus) {
        this.double_bonus = double_bonus;
    }

    public boolean isRaiding() {
        return raiding;
    }

    public void makeRaid(Figure f) {
        raiding = true;
        this.reflexReactionUnit.setRaidAttack(f);
    }

    @Override
    public Paragraph[] getParagraphs() {
        Paragraph[] p = new Paragraph[3];
        p[0] = new Paragraph(getName());
        p[0].setSize(24);
        p[0].setCentered();
        p[0].setColor(JDColor.orange);
        p[0].setBold();

        p[1] = new Paragraph(getMclass());
        p[1].setSize(20);
        p[1].setCentered();
        p[1].setColor(JDColor.black);
        p[1].setBold();

        p[2] = new Paragraph(getStatus());
        p[2].setSize(14);
        p[2].setCentered();
        p[2].setColor(JDColor.black);

        return p;
    }

    public abstract String getMclass();

    public int getActionPoints() {
        return agility.getCurrentAP();
    }

    public HealthLevel getHealthLevel() {
        int i = getCharacter().getHealth().perCent();
        if (i > 70) {
            return HealthLevel.Strong;
        } else if (i > 45) {
            return HealthLevel.Good;
        } else if (i > 25) {
            return HealthLevel.Injured;
        } else if (i > 10) {
            return HealthLevel.Weak;
        } else if (i > 0) {
            return HealthLevel.Dying;
        } else {
            return HealthLevel.Dead;
        }
    }

    protected void setStatus(int i) {
        if (i == 4) {
            status = " " + JDEnv.getResourceBundle().getString("status_strong");
            shortStatus = " "
                    + JDEnv.getResourceBundle()
                    .getString("status_short_strong");
        } else if (i == 3) {
            status = " " + JDEnv.getResourceBundle().getString("status_struck");
            shortStatus = " "
                    + JDEnv.getResourceBundle()
                    .getString("status_short_struck");
        } else if (i == 2) {
            status = " "
                    + JDEnv.getResourceBundle().getString("status_wounded");
            shortStatus = " "
                    + JDEnv.getResourceBundle().getString(
                    "status_short_wounded");
        } else if (i == 1) {
            status = " " + JDEnv.getResourceBundle().getString("status_weak");
            shortStatus = " "
                    + JDEnv.getResourceBundle().getString("status_short_weak");
            ;
        } else if (i == 0) {
            status = " " + JDEnv.getResourceBundle().getString("status_done");
            shortStatus = " "
                    + JDEnv.getResourceBundle().getString("status_short_done");
        }
        if (dead) {
            status = " " + JDEnv.getResourceBundle().getString("status_dead");
            shortStatus = " "
                    + JDEnv.getResourceBundle().getString("status_short_dead");
        }
    }

    public String getStatus() {
        // todo: refactor
        this.setStatus(this.getHealthLevel().getValue());
        return status;
    }

    public DungeonVisibilityMap getRoomVisibility() {
        return visibilities;
    }

    public int getPositionInRoom() {
        if (pos != null) {
            return pos.getIndex();
        }
        if (isDead()) {
            return getRoom().getDeadFigurePos(this);
        }
        return 0;
    }

    public RouteInstruction.Direction getLookDirection() {

        if (lookDir < 1 || lookDir > 4) {
            return RouteInstruction.Direction.fromInteger(1);
        } else {
            return RouteInstruction.Direction.fromInteger(lookDir);
        }
    }

    @Deprecated
    public void setLookDir(int lookDir) {
        this.lookDir = lookDir;
    }

    public void setLookDir(RouteInstruction.Direction dir) {
        setLookDir(dir.getValue());
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public void setControl(ControlUnit control) {
        this.control = control;
    }

    @Deprecated
    public void setLocation(JDPoint location) {
        this.location = location;
        if (location == null) {
            room = null;
        } else {
            room = getRoom().getDungeon().getRoom(location);
        }
    }

    public void setLocation(Room r) {
        if (r == null) {
            location = null;
            room = null;
            return;
        }
        this.location = r.getRoomNumber();
        if (location == null) {
            room = null;
        } else {
            room = r;
        }
    }

    @Override
    public JDPoint getRoomNumber() {
        return location;
    }

    /**
     * @return Returns the control.
     */
    public ControlUnit getControl() {
        return control;
    }


    private Action retrieveAction(DungeonWorldUpdater updater) {
        Action a;
        /*
         * Check behavior override (e.g. by magic)
         */
        Action reflex = reflexReactionUnit.getAction();
        if (reflex != null) {
            a = reflex;
        } else {

            a = control.getAction();

            if (updater.getGameLoopMode() == GameLoopMode.RenderThreadWorldUpdate) {
                if (a == null) {
                    // we need to return as in this mode this is the render thread
                    return null;
                }
            } else {
                /*
                 * we continuously ask the control for an action until one is
                 * specified
                 */
                // TODO: we need a security mechanism here:
                while (a == null) {
                    updater.waitSomeTimeOnGuiAction(150);
                    if (this.getRoom() == null
                            || this.getActualDungeon() == null
                            || this.getRoom().getDungeon().isGameOver()) {
                        // de.jdungeon.game ended or de.jdungeon.figure has left dungeon
                        break;
                    }
                    if (control != null) {
                        a = control.getAction();
                        if (a == null && !(control instanceof JDGUI)) {
                            // some messed up AI returning null;
                            a = new EndRoundAction();
                            break;
                        }
                    }
                }
            }
        }
        return a;
    }

    public boolean wayPassable(int dir) {
        Room toGo = getRoom().getDungeon().getRoomAt(
                getRoom().getDungeon().getRoom(getRoomNumber()),
                RouteInstruction.direction(dir));

        Door d = getRoom().getDungeon().getRoom(getRoomNumber())
                .getConnectionTo(toGo);

        return wayPassable(d, toGo);
    }

    public void move(Room target, int round) {
        Room toLeave = getRoom();

        int dir;
        if (toLeave == null) {
            dir = Dir.NORTH;
        } else {
            dir = DungeonUtils.getNeighbourDirectionFromTo(toLeave, target)
                    .getValue();
        }
        assert toLeave != null;
        Percept leavePercept = new LeavesPercept(this, toLeave, RouteInstruction.Direction.fromInteger(dir), round);

        if (toLeave != target) {
            toLeave.figureLeaves(this);
            toLeave.distributePercept(leavePercept);
        } else {
            Log.warning("toLeave == target");
        }

        target.figureEnters(this, dir, round);
        lookInRoom(round);
        Percept enterPercept = new EntersPercept(this, target, round);
        target.distributePercept(enterPercept);
    }

    public boolean walk(RouteInstruction.Direction dir, int round) {
        boolean passable = wayPassable(dir.getValue());

        Room oldRoom = getRoom();
        Room toGo = getRoom().getDungeon().getRoomAt(getRoom().getDungeon().getRoom(getRoomNumber()), dir);

        this.setLookDir(dir.getValue());

        if (passable) {
            Room before = this.getRoom();
            Door door = before.getDoor(dir);
            Position destPos = door.getPositionAtDoor(toGo, false);
            Figure standing = destPos.getFigure();
            if (standing == null) {
                // the position is free so walk right in
                goThroughDoor(oldRoom, toGo, round);
                return true;
            } else {
                if (standing.getControl().isHostileTo(FigureInfo.makeFigureInfo(this, standing.getRoomVisibility()))
                        || this.getControl()
                        .isHostileTo(FigureInfo.makeFigureInfo(standing, this.getRoomVisibility()))) {

                    // some visibility information for non-active de.jdungeon.figure during door smashing
                    ScoutResult scoutThis = new ScoutResult(standing, RoomObservationStatus.VISIBILITY_FIGURES);
                    standing.getRoomVisibility().addVisibilityModifier(this.getRoomNumber(), scoutThis);

                    boolean raid = this.isRaiding();
                    int thisStr = (int) this.getStrength().getValue();
                    int otherStr = (int) standing.getStrength().getValue();
                    int strengthDiff = thisStr - otherStr;
                    double thisRan = Math.random() * thisStr;
                    double otherRan = Math.random() * otherStr;
                    if (thisRan > otherRan || raid) {
                        // locale de.jdungeon.figure gets smashed
                        boolean moves = standing.doorSmashes(this.getRoom()
                                .getDoor(dir), standing, round, strengthDiff);
                        if (moves) {
                            goThroughDoor(oldRoom, toGo, round);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        // intruder gets smashed and does not enter
                        this.doorSmashesBack(this.getRoom().getDoor(dir), standing, round, -1 * strengthDiff);
                        return false;
                    }
                } else {
                    // no need for violence, just friends
                    goThroughDoor(oldRoom, toGo, round);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    protected abstract void lookInRoom(int round);

    private void goThroughDoor(Room oldRoom, Room toGo, int round) {

        // actual movement
        move(toGo, round);
    }

    public Dungeon getActualDungeon() {
        return actualDungeon;
    }

    public void incBlinded(int k) {
        blinded += k;
    }

    private void fireModifications() {
        List modifications = getModificationList();
        if (!modifications.isEmpty()) {

            TimedAttributeModification m = null;// ((TimedModification)
            Iterator iter = modifications.iterator();
            while (iter.hasNext()) {
                m = (TimedAttributeModification) iter.next();
                if (m.hasFired()) {
                    iter.remove();
                } else {
                    m.newRound();
                }
            }
        }
    }

    public Spell unWrappSpellInfo(SpellInfo a) {
        if (a == null) {
            return null;
        }
        List<Spell> l = this.getSpellbook().getSpells();
        for (Iterator<Spell> iter = l.iterator(); iter.hasNext(); ) {
            Spell element = iter.next();
            if (element != null
                    && a.equals(new SpellInfo(element, this.getRoomVisibility()))) {
                return element;
            }
        }
        return null;
    }

    public AbstractReflexBehavior getReflexReactionUnit() {
        return reflexReactionUnit;
    }

    public abstract int getWorth();

    public void setAI(AI AI) {
        this.ai = AI;
    }

    public abstract FigurePresentation getFigurePresentation();
}