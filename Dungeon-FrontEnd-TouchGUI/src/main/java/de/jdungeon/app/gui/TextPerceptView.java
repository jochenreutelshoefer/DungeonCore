package de.jdungeon.app.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import text.Statement;
import util.JDDimension;

import de.jdungeon.app.screen.GameScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.game.ScrollMotion;

import dungeon.JDPoint;

public class TextPerceptView extends AbstractGUIElement {

	private final List<Statement> cache = new ArrayList<Statement>();
	private List<Statement> all = new ArrayList<>();
	private Statement currentInsert = null;
	private static final float animationTime = 10f;
	private float timer = 0;
	private static final int lineHeight = 20;

	public TextPerceptView(GameScreen screen) {
		super(new JDPoint(200, -340), new JDDimension(600, 400), screen.getGame());
	}

	public void addTextPercept(Statement p) {
		cache.add(p);
	}

	private static int getNumberOfLines(Statement p) {
		/*
		 * TODO:
		 */
		return 1;
	}

	@Override
	public boolean needsRepaint() {
		return !cache.isEmpty() || currentInsert != null;
	}

	@Override
	public void update(float time) {
		if (currentInsert == null) {
			if (!cache.isEmpty()) {
				animateNextPercept();
			}
		} else {
			if (timer > animationTime) {
				all.add(currentInsert);
				if (!cache.isEmpty()) {
					animateNextPercept();
				} else {
					currentInsert = null;

				}
			} else {
				timer += time;
			}
		}

		if (all.size() > 200) {
			all = all.subList(all.size() - 20, all.size());
		}

	}

	private void animateNextPercept() {
		currentInsert = cache.remove(0);
		timer = 0;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		return false;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		g.drawRect(this.position.getX(), this.position.getY(),
				this.dimension.getWidth(), this.dimension.getHeight(),
				Colors.GRAY);
		g.fillRect(this.position.getX()+1, this.position.getY()+1,
				this.dimension.getWidth()-2, this.dimension.getHeight()-2,
				Colors.BLACK);
		
		ListIterator<Statement> listIterator = all.listIterator(all.size());
		int yOffset = lineHeight + 2;
		int timeOffset = 0;
		if (currentInsert != null) {
			timeOffset = (int) (getNumberOfLines(currentInsert) * lineHeight
					* timer / animationTime);
		}
		while (yOffset < this.dimension.getHeight()
				&& listIterator.hasPrevious()) {
			Statement textPercept = listIterator.previous();
			int yCoord = this.position.getY() + this.dimension.getHeight()
					- yOffset - timeOffset;
			if (yCoord < -lineHeight) {
				break;
			}
			g.drawString(textPercept.getText(), this.position.getX() + 6, yCoord, g.getTextPaintGray());
			yOffset += lineHeight;

		}

	}

	@Override
	public boolean handleScrollEvent(ScrollMotion scrolling) {
		ScrollMotion.FloatDimension movement = scrolling.getMovement();
		float movementY = movement.getY();
		int newY = this.position.getY() - (int) movementY;
		if (newY > -380 && newY < 0) {
			this.position.setY(newY);
			return true;
		}
		return false;
	}

}
