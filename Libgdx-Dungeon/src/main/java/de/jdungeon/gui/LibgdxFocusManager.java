package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.Collection;

import event.Event;
import event.EventListener;
import event.EventManager;
import figure.FigureInfo;
import game.RoomInfoEntity;
import graphics.GraphicObject;
import gui.Paragraphable;

import de.jdungeon.app.event.FocusEvent;
import de.jdungeon.app.event.InfoObjectClickedEvent;
import de.jdungeon.app.gui.InfoPanel;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 30.12.17.
 */
public class LibgdxFocusManager implements EventListener {

	private Paragraphable guiFocusObject;

	private GraphicObject graphicObject;

	private RoomInfoEntity worldFocusObject;
	private final LibgdxInfoPanel infoPanel;

	private final FigureInfo figure;
	public LibgdxFocusManager(LibgdxInfoPanel infoPanel, FigureInfo figure) {
		this.infoPanel = infoPanel;
		this.figure = figure;
		EventManager.getInstance().registerListener(this);
	}
	public void setGuiFocusObject(Paragraphable object) {
		if (object == null) {
			this.infoPanel.setContent(null);
			this.guiFocusObject = null;
		}
		else {
			this.guiFocusObject = object;
			this.infoPanel.setContent(object);
		}
		infoPanel.setContent(guiFocusObject);
	}

	public Paragraphable getGuiFocusObject() {
		return guiFocusObject;
	}

	public RoomInfoEntity getWorldFocusObject() {
		if(infoPanel.getContent() == null && worldFocusObject != null) {
			infoPanel.setContent(worldFocusObject);
		}
		return worldFocusObject;
	}

	public void setWorldFocusObject(RoomInfoEntity object) {
		if (object == null || object.equals(figure)) {
			this.infoPanel.setContent(null);
			this.guiFocusObject = null;
		}
		else {
			this.guiFocusObject = object;
			this.infoPanel.setContent(object);
		}
		this.worldFocusObject = object;
	}

	public GraphicObject getGraphicObject() {
		return graphicObject;
	}

	public void setWorldFocusObject(GraphicObject object) {
		graphicObject = object;
		setWorldFocusObject((RoomInfoEntity)object.getClickableObject());
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Collection<Class<? extends Event>> events = new ArrayList<>();
		events.add(FocusEvent.class);
		events.add(InfoObjectClickedEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		if (event instanceof FocusEvent) {
			Paragraphable infoEntity = ((FocusEvent) event).getObject();
			this.setGuiFocusObject(infoEntity);
		}
		if (event instanceof InfoObjectClickedEvent) {
			this.setWorldFocusObject(((InfoObjectClickedEvent) event).getClickedEntity());
		}
	}
}
