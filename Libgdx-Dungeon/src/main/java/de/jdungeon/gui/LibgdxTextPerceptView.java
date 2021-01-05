package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import dungeon.JDPoint;
import text.Statement;
import util.JDDimension;

import de.jdungeon.asset.Assets;

public class LibgdxTextPerceptView extends AbstractLibgdxGUIElement {

	private final List<Statement> cache = new ArrayList<Statement>();
	private List<Statement> all = new ArrayList<>();
	private Statement currentInsert = null;
	private static final float animationTime = 0.8f;
	private float timer = 0;
	private final int lineHeight;
	private final BitmapFont font = Assets.instance.fonts.defaultSmallFlipped;
	private final GlyphLayout layout;

	private static final JDPoint outPosition = new JDPoint(200, -360);

	public LibgdxTextPerceptView() {
		super(outPosition, new JDDimension(400, 400));
		layout = new GlyphLayout();

		lineHeight = (int) font.getLineHeight() + 4;
	}

	public void addTextPercept(Statement p) {
		cache.add(p);
	}

	private static int getNumberOfLines(Statement p) {
		if (p == null) return 0;
		String text = p.getText();
		String linebreak = "\n";
		if (text.contains(linebreak)) {
			return text.split(linebreak).length;
		}
		return 1;
	}

	@Override
	public boolean needsRepaint() {
		return true;
		//return !cache.isEmpty() || currentInsert != null;
	}

	@Override
	public void paint(ShapeRenderer shapeRenderer) {
		int x = this.position.getX();
		int y = this.position.getY();
		int width = this.dimension.getWidth();
		int height = this.dimension.getHeight();

		shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(x, y, width, height);

		shapeRenderer.set(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(x, y, width, height);
	}

	@Override
	public void paint(SpriteBatch batch) {

		ListIterator<Statement> listIterator = all.listIterator(all.size());

		int timeOffset = 0;
		if (currentInsert != null) {
			timeOffset = (int) (getNumberOfLines(currentInsert) * lineHeight * timer / animationTime);
		}
		Statement textPercept = null;
		if (listIterator.hasPrevious()) {
			textPercept = listIterator.previous();
		}
		int yOffset = lineHeight * getNumberOfLines(textPercept) + 2;
		while (yOffset < this.dimension.getHeight()
				&& textPercept != null) {
			int yCoord = this.position.getY() + this.dimension.getHeight() - yOffset - timeOffset;
			if (yCoord < -lineHeight) {
				break;
			}

			layout.setText(font, toThreeDigitsString(textPercept.getRound()), Color.GRAY, this.dimension.getWidth() * 0.05f, Align.left, true);
			font.draw(batch, layout, getPositionOnScreen().getX() + 2, yCoord);

			String text = textPercept.getText();
			layout.setText(font, text, com.badlogic.gdx.graphics.Color.WHITE, this.dimension.getWidth() * 0.9f, Align.left, true);
			font.draw(batch, layout, getPositionOnScreen().getX() + this.dimension.getWidth() * 0.07f, yCoord);

			if (listIterator.hasPrevious()) {
				textPercept = listIterator.previous();
			} else {
				textPercept = null;
			}

			yOffset +=lineHeight * getNumberOfLines(textPercept);
		}
	}

	private CharSequence toThreeDigitsString(int round) {
		String stringValue = Integer.toString(round);
		if (stringValue.length() == 1) {
			return "00" + stringValue;
		}
		if (stringValue.length() == 2) {
			return "0" + stringValue;
		}
		return stringValue;
	}

	@Override
	public void update(float time) {
		if (currentInsert == null) {
			if (!cache.isEmpty()) {
				animateNextPercept();
			}
		}
		else {
			if (timer > animationTime) {
				all.add(currentInsert);
				if (!cache.isEmpty()) {
					animateNextPercept();
				}
				else {
					currentInsert = null;
				}
			}
			else {
				timer += time * 3;
			}
		}

		if (all.size() > 60) {
			all = all.subList(all.size() - 20, all.size());
		}
	}

	@Override
	public boolean handleClickEvent(int screenX, int screenY) {
		return false;
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
	public boolean handlePanEvent(float x, float y, float dx, float dy) {
		int newY = this.position.getY() + (int) dy;
		if (newY > -380 && newY < 0) {
			this.position.setY(newY);
			return true;
		}
		return false;
	}
}
