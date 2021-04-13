/*
 * Copyright (C) 2009 Chair of Artificial Intelligence and Applied Informatics
 * Computer Science VI, University of Wuerzburg
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package de.jdungeon.event;

import de.jdungeon.log.Log;

import java.util.*;


/**
 * A very simple EventManager. Events are represented by Classes
 * 
 * @author Jochen
 * 
 */
public final class EventManager {

	private static EventManager instanceDungeon;

	public static EventManager getInstance() {
		if (instanceDungeon == null) {
			instanceDungeon = new EventManager();
		}
		return instanceDungeon;
	}

	/*
	 * We use WeakHashMaps as Set because this way we don't have to unregister
	 * no longer used EventListener.
	 */
	private final Map<Class<? extends Event>, Map<EventListener, Object>> listenerMap =
			new HashMap<>();

	public void clearAllListeners() {
		listenerMap.clear();
	}

	/**
	 * Creates the listener map by fetching all EventListener extensions from
	 * the PluginManager
	 */
	private EventManager() {

	}

	public synchronized void registerListener(EventListener listener) {
		// Get the classes of the events
		Collection<Class<? extends Event>> eventClasses = listener.getEvents();

		for (Class<? extends Event> eventClass : eventClasses) {
			// Register the listener for the de.jdungeon.event's class
			Map<EventListener, Object> list = listenerMap.get(eventClass);
			if (list == null) {
				list = new HashMap<EventListener, Object>();
				listenerMap.put(eventClass, list);
			}
			list.put(listener, null);
		}
	}

	public synchronized void unregister(EventListener listener) {
		// Get the classes of the events
		Collection<Class<? extends Event>> eventClasses = listener.getEvents();

		for (Class<? extends Event> eventClass : eventClasses) {
			// unregister the listener for the de.jdungeon.event's class
			Map<EventListener, Object> list = listenerMap.get(eventClass);
			list.remove(listener);
		}
	}

	/**
	 * As we cannot use a WeakHashMap (due to GWT compatibility), we carefully need to de-register unused listeners.
	 *
	 * @param clazz
	 */
	public synchronized void unregisterInstances(Class<? extends EventListener> clazz) {
		// Get the classes of the events

		for (Class<? extends Event> eventClass : this.listenerMap.keySet()) {
			// unregister the listener for the de.jdungeon.event's class
			Map<EventListener, Object> map = listenerMap.get(eventClass);
			Set<EventListener> eventListeners = map.keySet();
			List<EventListener> toRemove = new ArrayList<>();
			for (EventListener eventListener : eventListeners) {
				if(eventListener.getClass().equals(clazz)) {
					toRemove.add(eventListener);
				}
			}
			for (EventListener eventListenerToRemove : toRemove) {
				map.remove(eventListenerToRemove);

			}
		}
	}

	/**
	 * Fires events; the calls are distributed in the system where the
	 * corresponding events should be fired (also plugin may fire events)
	 *
	 * @param event the fired de.jdungeon.event
	 */
	public void fireEvent(Event event) {

		ArrayList<EventListener> allListeners = new ArrayList<EventListener>();
		synchronized (this) {
			Class<?> eventClass = event.getClass();
			while (!eventClass.equals(Event.class)) {
				Map<EventListener, Object> listeners = this.listenerMap.get(eventClass);
				if (listeners != null) {
					allListeners.addAll(listeners.keySet());
				}
				eventClass = eventClass.getSuperclass();
			}
		}
		for (EventListener eventListener : allListeners) {
			try {
				eventListener.notify(event);
			}
			catch (Exception e) {
				e.printStackTrace();
				Log.error("Catched exception in EventListener: ", e);
			}
		}

	}
}
