package de.jdungeon.item;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.figure.percept.InfoPercept;
import de.jdungeon.game.JDEnv;
import de.jdungeon.item.interfaces.ItemOwner;
import de.jdungeon.item.interfaces.Locatable;
import de.jdungeon.item.interfaces.Usable;
import de.jdungeon.util.MyResourceBundle;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author Duke1
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Key extends Item<Key> implements Locatable, Usable, Json.Serializable {


	private String type;
	private ItemOwner owner;
	private boolean usableOnce;

	public Key(String type) {
		super(100, false);
		this.type = type;
		usableOnce = false;
	}

	public Key(String type, boolean usableOnce) {
		super(100, false);
		this.type = type;
		this.usableOnce = usableOnce;
	}

	public Key() {
		super(0);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Key key = (Key) o;

		return type.equals(key.type);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public Key copy() {
		return new Key(this.getType());
	}



	@Override
	public ItemOwner getOwner() {
		return owner;
	}

	@Override
	public void setOwner(ItemOwner o) {
		owner = o;
	}

	@Override
	public void getsRemoved() {
		owner = null;
	}

	/**
	 * Returns the type.
	 *
	 * @return String
	 */
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		MyResourceBundle resourceBundle = JDEnv.getResourceBundle();
		String key = this.getClass().getSimpleName();
		if(resourceBundle != null) {
			key = resourceBundle.getString("key");
		}
		return  key+ ": " + type;
	}

	public Attribute getHitPoints() {
		return null;
	}

	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("key") + ": " + type;
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
		if (target instanceof Door) {
			Door door = ((Door) target);
			boolean lockMatches = door.lockMatches(this);
			if (doIt) {
				if (lockMatches) {
					boolean wasLocked = door.getLocked();
					door.toggleLock(this);
					if (wasLocked) {
						f.tellPercept(new InfoPercept(InfoPercept.UNLOCKED_DOOR, round));
					}
					else {
						f.tellPercept(new InfoPercept(InfoPercept.LOCKED_DOOR, round));
					}
					return ActionResult.DONE;
				}
				else {
					return ActionResult.FAILED;
				}
			}
			else {
				if (lockMatches) {
					return ActionResult.POSSIBLE;
				}
				else {
					return ActionResult.ITEM;
				}
			}
		}
		return ActionResult.WRONG_TARGET;
	}

	@Override
	public boolean usableOnce() {
		return usableOnce;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return true;
	}

	@Override
	public boolean needsTarget() {
		return true;
	}

	private static final String KEY = "key";
	private static final String USABLE_ONCE = "usable_once";

	@Override
	public void write(Json json) {
		//json.writeValue(KEY, this.type);
		//json.writeValue(USABLE_ONCE, this.usableOnce);

		json.writeFields(this);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		//this.type = jsonData.getString(KEY);
		//this.usableOnce = jsonData.getBoolean(USABLE_ONCE);

		json.readFields(this, jsonData);
	}


	@Deprecated
	public static String[] keyStrings = { "Kupfer", "Eisen", "Silber",
			"Gold", "Platin", "Bronze", "Blech", "Stahl", "Piponium" };

	@Deprecated
	public static List<Key> generateKeylist() {
		List<Key> result = new ArrayList<>();
		for (int i = 0; i < Key.keyStrings.length; i++) {
			result.add(new Key(Key.keyStrings[i] + " groß"));
			result.add(new Key(Key.keyStrings[i] + " klein"));
		}
		return result;
	}

}
