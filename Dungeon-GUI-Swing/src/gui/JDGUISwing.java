package gui;

import java.awt.Point;

import control.JDGUIEngine2D;

public interface JDGUISwing extends JDGUIEngine2D {

	void updateGui();
	
	void repaintPicture();
	
	Point getViewportPosition();
}
