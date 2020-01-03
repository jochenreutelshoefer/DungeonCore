package de.jdungeon.app.gui.smartcontrol;

import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import util.JDDimension;

import de.jdungeon.app.ActionController;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.app.gui.activity.ExecutableActivity;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.01.18.
 */
public class ActivityControlElement extends AnimatedSmartControlElement {

	private final GUIElement parent;
	private final RouteInstruction.Direction direction;
	private final FigureInfo figure;
	private final ActionController guiControl;
	private final ExecutableActivity activity;
	private final Image skillImage;

	public ActivityControlElement(JDPoint position, final JDDimension dimension, GUIElement parent, RouteInstruction.Direction direction, FigureInfo figure, ActionController guiControl, ExecutableActivity activity, final Image skillImage) {
		super(position, dimension, parent);
		this.parent = parent;
		this.direction = direction;
		this.figure = figure;
		this.guiControl = guiControl;
		this.skillImage = skillImage;
		this.activity = activity;

		for (int i = 0; i < buttonAnimationSizes.length; i++) {
			final int finalI = i;
			animationShapes[i] = new Drawable() {
				@Override
				public void paint(Graphics g, JDPoint viewportPosition) {
					int sizeX = (int) (dimension.getWidth() * buttonAnimationSizes[finalI]);
					int sizeY = (int) (dimension.getHeight() * buttonAnimationSizes[finalI]);
					int diffSizeX = sizeX - getDimension().getWidth();
					int diffSizeY = sizeY - getDimension().getHeight();
					g.drawScaledImage(skillImage, getX() - diffSizeX / 2, getY() - diffSizeY / 2, sizeX, sizeY, 0, 0, skillImage.getWidth(), skillImage.getHeight() );
				}
			};
		}
	}

	@Override
	public boolean isVisible() {
		return activity.isCurrentlyPossible();
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		super.handleTouchEvent(touch);
		if(activity.isCurrentlyPossible()) {
			activity.execute();
		}
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
		int width = (int) (dimension.getWidth() * 0.8);
		int height = (int)(dimension.getHeight() * 0.8);
		g.drawScaledImage(skillImage, getX() + (int)(dimension.getWidth() * 0.1), getY()+ (int)(dimension.getHeight() * 0.1), width, height, 0, 0, skillImage.getWidth(), skillImage.getHeight());

	}
}
