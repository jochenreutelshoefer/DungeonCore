package shrine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeon.Room;
import event.EventManager;
import event.ExitUsedEvent;
import figure.Figure;
import figure.hero.Hero;
import game.DungeonGame;
import game.JDEnv;
import util.JDColor;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public class LevelExit extends Shrine {

	private List<Figure> requiredFigures = new ArrayList<>();

	public LevelExit() {

	}

	/**
	 * Constructor to create exits that required presence of escorted NPCs
	 *
	 * @param figures that need to be in the room to trigger exit
	 */
	public LevelExit(Figure... figures) {
		requiredFigures = Arrays.asList(figures);
	}

	@Override
	public int getShrineIndex() {
		return SHRINE_EXIT;
	}

	@Override
	public void turn(int round) {

	}

	@Override
	public JDColor getColor() {
		return null;
	}

	@Override
	public String getStory() {
		return "";
	}

	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_exit_name");

	}

	@Override
	public String getText() {
		return toString() + "\n"+JDEnv.getResourceBundle().getString("shrine_exit_text");
	}

	@Override
	public String getStatus() {
		return "";
	}

	@Override
	public boolean use(Figure f, Object target, boolean meta) {
		if(checkRequiredFigures()) {
			// some figure to be escorted to exit is not here -> refuse
			return false;
		}

		f.setLocation((Room)null);
		EventManager.getInstance().fireEvent(new ExitUsedEvent(f, this));
		return true;
	}

	private boolean checkRequiredFigures() {
		for (Figure requiredFigure : requiredFigures) {
			if(!this.getRoom().getRoomFigures().contains(requiredFigure)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean usableOnce() {
		return true;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return f instanceof Hero;
	}

	@Override
	public boolean needsTarget() {
		return false;
	}
}
