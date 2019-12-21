package de.jdungeon.app.gui;

import figure.FigureInfo;
import game.RoomInfoEntity;
import gui.Paragraphable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 30.12.17.
 */
public class FocusManager {

	private Paragraphable guiFocusObject;

	private RoomInfoEntity worldFocusObject;

	private final InfoPanel infoPanel;
	private final FigureInfo figure;
	public FocusManager(InfoPanel infoPanel, FigureInfo figure) {
		this.infoPanel = infoPanel;
		this.figure = figure;
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
}
