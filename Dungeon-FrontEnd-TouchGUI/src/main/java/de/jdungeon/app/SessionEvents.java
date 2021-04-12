package de.jdungeon.app;

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.app.event.LevelAbortEvent;
import de.jdungeon.event.Event;
import de.jdungeon.event.ExitUsedEvent;
import de.jdungeon.event.PlayerDiedEvent;
import de.jdungeon.level.DungeonStartEvent;

import de.jdungeon.app.event.QuitGameEvent;
import de.jdungeon.app.event.StartNewGameEvent;
import de.jdungeon.app.gui.skillselection.SkillSelectedEvent;
import de.jdungeon.app.screen.start.HeroCategorySelectedEvent;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.20.
 */
public class SessionEvents {

	public static List<Class<? extends Event>> getSessionEventClasses() {
		List<Class<? extends Event>> events = new ArrayList<Class<? extends Event>>();
		events.add(ExitUsedEvent.class);
		events.add(PlayerDiedEvent.class);
		events.add(LevelAbortEvent.class);
		events.add(StartNewGameEvent.class);
		events.add(QuitGameEvent.class);
		events.add(DungeonStartEvent.class);
		events.add(HeroCategorySelectedEvent.class);
		events.add(SkillSelectedEvent.class);
		return events;
	}
}
