package de.jdungeon.androidapp.gui;

import java.util.ArrayList;
import java.util.List;

import util.JDDimension;
import android.graphics.Color;
import android.graphics.Paint;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import dungeon.JDPoint;
import figure.hero.HeroInfo;

public class CharAttributeView extends SlidingGUIElement {

	private final HeroInfo info;

	private static final JDDimension size = new JDDimension(300, 360);

	private static final int yPosition = 150;

	private final Paint black;

	public CharAttributeView(HeroInfo info, GameScreen screen) {
		super(new JDPoint(0, yPosition), size, new JDPoint(
				(size.getWidth() * -1) + 20, yPosition), screen);
		this.info = info;
		black = new Paint();
		black.setColor(Color.BLACK);
		black.setTextSize(12);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		screen.scrollTo(info.getRoomNumber(), 50f);
		super.handleTouchEvent(touch);
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		
		int x = this.getCurrentX();
		GUIUtils.drawBackground(g, x, position.getY(), dimension);

		GUIUtils.drawDoubleBorder(g, x, position.getY(), dimension, 20);

		g.drawString("Punkte: " + info.getTotalExp(), this.getCurrentX() + 15,
				position.getY() + 25, black);

		/*
		 * TODO: refactor attributes to enum
		 */
		List<String> texts = new ArrayList<String>();
		texts.add("Name");
		texts.add("STRENGTH");//1;
		texts.add("DEXTERITY");//2;
		texts.add("PSYCHO");//3;
		texts.add("AXE");//4;
		texts.add("CLUB");//5;
		texts.add("LANCE");//6;
		texts.add("SWORD");//7;
		texts.add("WOLFKNIFE");//8;
		texts.add("NATURE_KNOWLEDGE");//9;
		texts.add("CREATURE_KNOWLEDGE");//10;
		texts.add("UNDEAD_KNOWLEDGE");//11;
		texts.add("SCOUT");//12;
		texts.add("THREAT");//13;
		texts.add("HEALTH");//14;
		texts.add("DUST");//15;
		texts.add("DUSTREG");//16;
		texts.add("HEALTHREG");//17;
		texts.add("BRAVE");//18;
		texts.add("CHANCE_TO_HIT");//19;
		texts.add("HIT_POINTS");//20;
		texts.add("FOUNTAIN");// = 21;

		writeLine(0, texts, g);

		for (int i = 1; i <= 21; i++) {
			writeLine(i, texts, g);
		}


	}

	private void writeLine(int i, List<String> texts, Graphics g) {

		int textStartRow = this.position.getY() + 35;
		int textStartX = this.getCurrentX() + 15;
		int lineHeight = 17;

		double value = info.getAttributeValue(i);

		double basic = info.getAttributeBasic(i);
		if (basic == -1) {
			// figure has not such attribute
			return;
		}
		String text = texts.get(i) + ": " + value + "/" + basic;
		if (i == 0) {
			text = texts.get(i) + ": " + info.getName();
		}
		g.drawString(text, textStartX, textStartRow + i * lineHeight, black);

	}

}
