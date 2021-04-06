/**
 * @author Duke1
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package item;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dungeon.Door;
import dungeon.Lock;
import dungeon.LockInfo;
import dungeon.Position;
import dungeon.RoomEntity;
import dungeon.util.InfoUnitUnwrapper;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import figure.attribute.Attribute;
import game.JDEnv;
import game.RoomInfoEntity;
import item.interfaces.Locatable;
import item.interfaces.UsableWithTarget;
import spell.AbstractTargetScope;
import spell.TargetScope;

@Deprecated
public class Bunch extends Item implements Serializable, UsableWithTarget {

    private final Set<Key> keys = new HashSet<>();

    public Bunch() {
        super(0, false);
    }

    public Attribute getHitPoints() {
        return null;
    }

    public Collection<Key> getKeys() {
        return keys;
    }

	/*
	public boolean tryUnlockDoor(Door d, boolean doIt) {
		boolean b = false;
		for (Key k : this.keys) {
			b = d.toggleLock(k, doIt);
			if (b) return true;
		}
		return false;
	}

	 */

    public void addKey(Key k) {
        keys.add(k);
    }

    public boolean hasKey(Key k) {
        return keys.contains(k);
    }

    public boolean hasKey(String k) {
        for (Key ke : this.keys) {
            if (ke.getType().equals(k)) {
                return true;

            }
        }
        return false;
    }

    public boolean removeKey(Key k) {
        return keys.remove(k);
    }

    @Override
    public String toString() {
        return JDEnv.getResourceBundle().getString("bunch") + ": " + keys.size();
    }

    /**
     * @see Item#getText()
     */
    @Override
    public String getText() {
        String text = new String();
        for (Key k : this.keys) {
            String one = k.toString();
            String cut = cut(one);
            text += " , " + cut;
        }
        return text;
    }

    private String cut(String s) {
        return s.substring(11);
    }

    public void clear() {
        keys.clear();
    }

    @Override
    public TargetScope getTargetScope() {
        return AbstractTargetScope.createDefaultScope(LockInfo.class);
    }

    @Override
    public int dustCosts() {
        return 0;
    }

    @Override
    public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
        return ActionResult.OTHER;
        // todo: remove
		/*
		List<LockInfo> targetLocks = AbstractTargetScope.getTargetLocks(FigureInfo.makeFigureInfo(f, f.getRoomVisibility()));
		if (targetLocks.size() == 1) {
			RoomInfoEntity lockInfo = targetLocks.iterator().next();
			Lock lock = (Lock) new InfoUnitUnwrapper(f.getActualDungeon()).unwrappObject(lockInfo);
			Locatable lockableObject = lock.getLockableObject();
			if (lockableObject instanceof Door) {
				Door door = (Door) lockableObject;
				Position positionAtDoor = door.getPositionAtDoor(f.getRoom(), false);
				if(positionAtDoor.equals(f.getPos())) {
					return this.tryUnlockDoor(door, true);
				}
			}
		}
		return false;

		 */
    }

    @Override
    public boolean usableOnce() {
        return false;
    }

    @Override
    public boolean canBeUsedBy(Figure f) {
        return true;
    }

    @Override
    public boolean needsTarget() {
        return true;
    }
}
