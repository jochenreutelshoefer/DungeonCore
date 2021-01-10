package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import dungeon.JDPoint;
import text.Statement;
import util.JDDimension;

import de.jdungeon.asset.Assets;

public class LibgdxTextPerceptView extends AbstractLibgdxGUIElement {

	// TODO: make relative to screen size!?
	private static final int WIDTH_MAX = 400;
	private static final int HEIGHT_MAX = 400;
	private static final int LINE_PADDING_SINGLE = 5;
	private static final int LINE_PADDING_DOUBLE = 2 * LINE_PADDING_SINGLE;

	private final List<Statement> cache = new ArrayList<>();
	private List<Statement> all = new ArrayList<>();
	private Statement currentInsert = null;
	private static final float animationTime = 0.8f;
	private float timer = 0;
	private final int lineHeight;
	private final BitmapFont font = Assets.instance.fonts.defaultSmallFlipped;

	private Texture offlineTextTexture;
	private final FrameBuffer frameBuffer;
	private final Batch spriteBatch = new SpriteBatch();
	private final GlyphLayout glyphLayoutRoundNumber = new GlyphLayout();
	private final GlyphLayout glyphLayoutStatementText = new GlyphLayout();

	public LibgdxTextPerceptView() {
		super(new JDPoint(Gdx.app.getGraphics()
				.getWidth() / 2 - WIDTH_MAX / 2, -1 * (HEIGHT_MAX - 40)), new JDDimension(WIDTH_MAX, HEIGHT_MAX));
		lineHeight = (int) font.getLineHeight() + 4;

		int framebufferWidth = HEIGHT_MAX * (Gdx.graphics.getWidth() / Gdx.graphics.getHeight());
		int frameBufferHeight = HEIGHT_MAX;
		frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, framebufferWidth, frameBufferHeight, false);

		offlineTextTexture = updateOfflineMessageTexture();
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
		shapeRenderer.rect(x, y, width + 1, height);
	}

	private Texture updateOfflineMessageTexture() {

		// prepare and activate offscreen framebuffer
		frameBuffer.bind();
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();


		// draw actual content
		ListIterator<Statement> listIterator = all.listIterator(all.size());
		int yOffsetUpwards = 0;
		if (listIterator.hasPrevious()) {
			yOffsetUpwards += fetchPrepareAndDrawPreviousStatement(listIterator, yOffsetUpwards, LINE_PADDING_SINGLE);
		}
		while (listIterator.hasPrevious() && yOffsetUpwards < frameBuffer.getHeight()) {
			yOffsetUpwards += fetchPrepareAndDrawPreviousStatement(listIterator, yOffsetUpwards, LINE_PADDING_DOUBLE);
		}

		// close and unbind SpriteBatch and Framebuffer
		spriteBatch.end();
		frameBuffer.unbind();

		return frameBuffer.getColorBufferTexture();
	}

	private int fetchPrepareAndDrawPreviousStatement(ListIterator<Statement> listIterator, int yOffsetUpwardsOverall, int linePadding) {
		Statement textPercept = listIterator.previous();
		glyphLayoutRoundNumber.setText(font, toThreeDigitsString(textPercept.getRound()), Color.GRAY, frameBuffer.getWidth() * 0.05f, Align.left, true);
		glyphLayoutStatementText.setText(font, textPercept.getText(), Color.WHITE, frameBuffer.getWidth() * 0.9f, Align.left, true);

		int yOffsetUpwardsStatementIncrement = (int) glyphLayoutStatementText.height + linePadding;
		yOffsetUpwardsOverall += yOffsetUpwardsStatementIncrement;
		int yCoord = frameBuffer.getHeight() - yOffsetUpwardsOverall;

		font.draw(spriteBatch, glyphLayoutRoundNumber, 4, yCoord);
		font.draw(spriteBatch, glyphLayoutStatementText, frameBuffer.getWidth() * 0.07f, yCoord);
		return yOffsetUpwardsStatementIncrement;
	}

	@Override
	public void paint(SpriteBatch batch) {

		int timeOffset = 0;
		if (currentInsert != null) {
			timeOffset = (int) (getNumberOfLines(currentInsert) * lineHeight * timer / animationTime);
		}
		batch.draw(offlineTextTexture,
				getPositionOnScreen().getX(),
				this.position.getY() - timeOffset - 1,
				WIDTH_MAX,
				HEIGHT_MAX,
				0,
				0,
				WIDTH_MAX,
				HEIGHT_MAX,
				false,
				true);
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
				offlineTextTexture = updateOfflineMessageTexture();
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
