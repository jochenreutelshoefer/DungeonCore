package de.jdungeon.adapter.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import de.jdungeon.game.Color;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.TextPaint;
import de.jdungeon.util.PaintBuilder;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxGraphics implements Graphics {

	private final OrthographicCamera camera;
	private final SpriteBatch batch;
	private final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private final BitmapFont font = new BitmapFont();

	public LibgdxGraphics() {
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
	}

	@Override
	public TextPaint getSmallPaint() {
		throw new NotImplementedException();
	}

	@Override
	public TextPaint getTextPaintGray() {
		throw new NotImplementedException();
	}

	@Override
	public TextPaint getTextPaintBlack() {
		throw new NotImplementedException();
	}


	@Override
	public TextPaint getTextPaintWhite25() {
		throw new NotImplementedException();
	}


	@Override
	public TextPaint createTextPaint(PaintBuilder builder) {
		throw new NotImplementedException();
	}

	private void prepareDraw(Color color) {
		shapeRenderer.setColor(((LibgdxColor)color).getColor());
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
	}

	private void prepareFill(Color color) {
		shapeRenderer.setColor(((LibgdxColor)color).getColor());
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	}

	@Override
	public void drawLine(int x, int y, int x2, int y2, Color color) {
		prepareDraw(color);
		shapeRenderer.line(x, y, x2, y2);
	}

	@Override
	public void drawRect(int x, int y, int width, int height, Color color) {
		prepareDraw(color);
		shapeRenderer.rect(x, y, width, height);
		shapeRenderer.end();
	}

	@Override
	public void fillRect(int x, int y, int width, int height, Color color) {
		prepareFill(color);
		shapeRenderer.rect(x, y, width, height);
		shapeRenderer.end();
	}

	@Override
	public void drawOval(int x, int y, int width, int height, Color color) {
		prepareDraw(color);
		shapeRenderer.ellipse(x, y, width, height);
		shapeRenderer.end();
	}

	@Override
	public void fillOval(int x, int y, int width, int height, Color color) {
		prepareFill(color);
		shapeRenderer.ellipse(x, y, width, height);
		shapeRenderer.end();
	}

	@Override
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
		prepareFill(color);
		shapeRenderer.triangle(x1, y1, x2, y2, y3, y3);
		shapeRenderer.end();
	}

	@Override
	public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
		prepareDraw(color);
		shapeRenderer.triangle(x1, y1, x2, y2, y3, y3);
		shapeRenderer.end();
	}

	@Override
	public void drawImage(Image image, int x, int y, boolean nonTmp) {
		batch.begin();
		batch.draw(((LibgdxImage)image).getTexture(), x, y);
		batch.end();
	}

	@Override
	public void drawScaledImage(Image image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight, boolean nonTmp) {
		// todo: consider scale
		batch.begin();
		batch.draw(((LibgdxImage)image).getTexture(), x, y);
		batch.end();
	}

	@Override
	public void drawScaledImage(Image image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
		// todo: consider scale
		batch.begin();
		batch.draw(((LibgdxImage)image).getTexture(), x, y);
		batch.end();
	}

	@Override
	public void drawString(String text, int x, int y, TextPaint paint) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.draw(batch, text, x, y);
	}

	@Override
	public void drawString(String text, int x, int y, int width, TextPaint paint) {
		// TODO: consider width
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.draw(batch, text, x, y);
	}

	@Override
	public void setTempCanvas(int x, int y, int widhth, int height) {
		throw new NotImplementedException();
	}

	@Override
	public void flushAndResetTempCanvas() {
		throw new NotImplementedException();
	}

	@Override
	public Image getTempImage() {
		throw new NotImplementedException();
	}
}
