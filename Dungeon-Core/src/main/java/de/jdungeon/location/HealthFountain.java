/**
 * @author Duke1
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package de.jdungeon.location;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.JDEnv;

/*
The health fountain allows the de.jdungeon.user to fill up his health value.
It has a limited capacity. After being used it fills up again over time.
Further, the de.jdungeon.user's oxygen attribute will be refilled (always completely).
 */
public class HealthFountain extends Location {

	private final Attribute healthReserve;

	private final double rate;

	public HealthFountain(int max, double rate) {
		super();
		healthReserve = new Attribute(Attribute.Type.Fountain, max);
		this.rate = rate;
		story = JDEnv.getResourceBundle().getString("see_health_fountain");
	}

	public HealthFountain() {
		this(30, 0.3);
	}

	@Override
	public boolean needsTarget() {
		return false;
	}

	@Override
	public int getSecondIdentifier() {
		// TODO: WTF?
		return -1;
	}

	@Override
	public String getText() {
		return toString() + " " + healthReserve.getValue() + " / " + healthReserve.getBasic();
	}

	@Override
	public String getStory() {
		return story;
	}

	@Override
	public boolean usableOnce() {
		return false;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return true;
	}

	int lastCompletedRound = -1;

	@Override
	public void turn(int round, DungeonWorldUpdater mode) {
		if (round > lastCompletedRound) {
			if ((healthReserve.getBasic() - healthReserve.getValue()) > rate) {
				healthReserve.modValue(rate);
			}
			else if (healthReserve.getBasic() > healthReserve.getValue()) {
				healthReserve.setValue(healthReserve.getBasic());
			}
		}
		lastCompletedRound = round;
	}

	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_fountain_name");
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
		if (doIt) {

			Attribute h = f.getHealth();
			double act = h.getValue();
			double max = h.getBasic();
			int missingFigureHealth = (int) (max - act);
			double healingAmount = 0;
			if (healthReserve.getValue() >= missingFigureHealth) {
				healthReserve.modValue((-1) * missingFigureHealth);
				f.heal(missingFigureHealth, round);
				healingAmount = missingFigureHealth;
			}
			else {
				healingAmount = healthReserve.getValue();
				f.heal((int) healingAmount, round);
				healthReserve.setValue(0);
			}

			double percentageHealed = healingAmount / max;

			// we also fill up user's oxygen completely
			Attribute oxygenAttribute = f.getAttribute(Attribute.Type.Oxygen);
			oxygenAttribute.setValue(oxygenAttribute.getBasic());

			return ActionResult.DONE;
		}
		else {
			return ActionResult.POSSIBLE;
		}
	}

	@Override
	public String getStatus() {
		return toString() + "\n" + healthReserve.toString();
	}
}
