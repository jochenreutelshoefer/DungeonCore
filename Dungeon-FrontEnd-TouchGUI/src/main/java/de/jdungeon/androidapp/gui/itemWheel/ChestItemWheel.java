package de.jdungeon.androidapp.gui.itemWheel;

import java.util.List;

import dungeon.JDPoint;
import event.EventManager;
import figure.hero.HeroInfo;
import util.JDDimension;

import de.jdungeon.androidapp.gui.activity.Activity;
import de.jdungeon.androidapp.gui.activity.ActivityProvider;
import de.jdungeon.androidapp.gui.activity.ExecutableActivity;
import de.jdungeon.androidapp.gui.smartcontrol.ToggleChestViewEvent;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.01.18.
 */
public class ChestItemWheel extends ItemWheel {

	private final Image wheelBackgroundImage;

	public ChestItemWheel(JDPoint position, JDDimension dim, HeroInfo info, StandardScreen screen, Game game, ActivityProvider provider, int selectedIndex, Image itemBackground, Image wheelBackgroundImage, String title) {
		super(position, dim, info, screen, game, provider, selectedIndex, itemBackground, wheelBackgroundImage, title, position, new JDDimension( dim.getWidth(),100), CenterPositionMode.center, dim.getWidth()/4, 16);
		this.wheelBackgroundImage = wheelBackgroundImage;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		super.handleTouchEvent(touch);
		boolean itemAreaTouched = super.isInDrawBounds(touch.x, touch.y);
		if(!itemAreaTouched) {
			EventManager.getInstance().fireEvent(new ToggleChestViewEvent());
		} else {
			// hack remove, when chest item wheel completed/fixed
			List<Activity> activities = provider.getActivities();
			if(!activities.isEmpty()) {
				if(activities.size() == 1) {
					// was last one
					EventManager.getInstance().fireEvent(new ToggleChestViewEvent());
				}
				((ExecutableActivity)activities.iterator().next()).execute();
			}
		}
		return true;
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		// check whether point p is in rectangle
		return p.getX() >= position.getX()
				&& p.getX() <= position.getX() + dimension.getWidth()
				&& p.getY() >= position.getY()
				&& p.getY() <= position.getY() + dimension.getHeight();
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {

		/*
		g.drawRect(this.getPositionOnScreen().getX(), this.getPositionOnScreen().getY(), getDimension().getWidth(), getDimension().getHeight(), Colors.BLUE);
		*/
		/*
		 * draw item wheel background if existing
		 */
		if (wheelBackgroundImage != null) {
			g.drawScaledImage(
					wheelBackgroundImage,
					position.getX(),
					position.getY(),
					dimension.getWidth(),
					dimension.getHeight(),
					0, 0,
					wheelBackgroundImage.getWidth(),
					wheelBackgroundImage.getHeight());
		}

		super.paint(g, viewportPosition);
	}
}
