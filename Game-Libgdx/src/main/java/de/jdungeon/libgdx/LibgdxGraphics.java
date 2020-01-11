package de.jdungeon.libgdx;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import de.jdungeon.game.Color;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.TextPaint;
import de.jdungeon.util.PaintBuilder;

import static de.jdungeon.libgdx.LibgdxImageLoader.TAG;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxGraphics implements Graphics {

	private static final String TAG = LibgdxGraphics.class.getName();

	protected final OrthographicCamera camera;
	protected final SpriteBatch batch;
	private final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private final BitmapFont font = new BitmapFont();
	private final BitmapFont fontWhite = new BitmapFont(true);
	private final BitmapFont fontGray = new BitmapFont(true);
	private final BitmapFont fontBlack = new BitmapFont(true);

	private final LibgdxTextpaint paintWhite = new LibgdxTextpaint(fontWhite);
	private final LibgdxTextpaint paintGray = new LibgdxTextpaint(fontGray);
	private final LibgdxTextpaint paintBlack = new LibgdxTextpaint(fontBlack);

	private final Map<Color, com.badlogic.gdx.graphics.Color> colorMap = new HashMap<>();


	public LibgdxGraphics(OrthographicCamera camera) {
		this.camera = camera;
		// create the camera and the SpriteBatch
		batch = new SpriteBatch();

		initColors();
	}

	public LibgdxGraphics(OrthographicCamera camera, SpriteBatch batch) {
		this.camera = camera;
		// create the camera and the SpriteBatch
		this.batch = batch;

		initColors();
	}

	public void beginSpriteBatch() {
		batch.begin();
	}

	public void endSpriteBatch() {
		batch.end();
	}

	private void initColors() {
		colorMap.put(Colors.BLACK, com.badlogic.gdx.graphics.Color.BLACK);
		colorMap.put(Colors.BLUE, com.badlogic.gdx.graphics.Color.BLUE);
		colorMap.put(Colors.GRAY, com.badlogic.gdx.graphics.Color.GRAY);
		colorMap.put(Colors.GREEN, com.badlogic.gdx.graphics.Color.GREEN);
		colorMap.put(Colors.RED, com.badlogic.gdx.graphics.Color.RED);
		colorMap.put(Colors.WHITE, com.badlogic.gdx.graphics.Color.WHITE);
		colorMap.put(Colors.YELLOW, com.badlogic.gdx.graphics.Color.YELLOW);

		fontWhite.setColor(com.badlogic.gdx.graphics.Color.WHITE);
		fontGray.setColor(com.badlogic.gdx.graphics.Color.GRAY);
		fontBlack.setColor(com.badlogic.gdx.graphics.Color.BLACK);
	}

	@Override
	public TextPaint getSmallPaint() {
		return paintBlack;
	}

	@Override
	public TextPaint getTextPaintGray() {
		return paintGray;
	}

	@Override
	public TextPaint getTextPaintBlack() {
		return paintBlack;
	}

	@Override
	public TextPaint getTextPaintWhite25() {
		return paintWhite;
	}


	@Override
	public TextPaint createTextPaint(PaintBuilder builder) {
		com.badlogic.gdx.graphics.Color color = colorMap.get(builder.getColor());
		if(color == null) {
			return null;
		}
		BitmapFont font = new BitmapFont();
		font.setColor(color);
		return new LibgdxTextpaint(font);

	}

	private void prepareDraw(Color color) {
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(colorMap.get(color));
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
	}

	private void prepareFill(Color color) {
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(colorMap.get(color));
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	}

	@Override
	public void drawLine(int x, int y, int x2, int y2, Color color) {
		prepareDraw(color);
		shapeRenderer.line(x, y, x2, y2);
		shapeRenderer.end();
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
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		if(image instanceof LibgdxFileImage) {
			batch.draw(((LibgdxFileImage)image).getTexture(), x, y);
		} else if(image instanceof LibgdxAssetImage) {
			batch.draw(((LibgdxAssetImage)image).getAtlasRegion(), x, y);
		}
		batch.end();
	}

	@Override
	public void drawScaledImage(Image image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight, boolean nonTmp) {
		drawScaledImage(image, x, y, width, height, srcX, srcY, srcWidth, srcHeight);
	}

	@Override
	public void drawScaledImage(Image image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if(image instanceof LibgdxFileImage) {
			batch.draw(((LibgdxFileImage)image).getTexture(), x, y, width, height,  srcX,  srcY, srcWidth, srcHeight, false, true);
		} else if(image instanceof LibgdxAssetImage) {
			batch.draw(((LibgdxAssetImage)image).getAtlasRegion(), x, y, width, height);
		}
		batch.end();
	}

	@Override
	public void drawString(String text, int x, int y, TextPaint paint) {
		LibgdxTextpaint textpaint = null;
		if(paint instanceof LibgdxTextpaint) {
			textpaint = (LibgdxTextpaint) paint;
		}
		if(paint instanceof PaintBuilder) {
			textpaint = new LibgdxTextpaint(createBitMapFont(((PaintBuilder)paint)));
		}

		GlyphLayout layout = new GlyphLayout();
		layout.setText(textpaint.getFont(), text ,textpaint.getFont().getColor(), 50, 0, false);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		textpaint.getFont().draw(batch, layout, x, y);
		//textpaint.getFont().draw(batch, text, x, y);
		batch.end();
	}

	private BitmapFont createBitMapFont(PaintBuilder paint) {
		BitmapFont font = new BitmapFont(true);
		font.setColor(colorMap.get(paint.getColor()));
		return font;
	}

	@Override
	public void drawString(String text, int x, int y, int width, TextPaint paint) {
		// todo: consider width
		drawString(text, x, y, paint);
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
