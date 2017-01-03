package de.jdungeon.implementation;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Paint;
import de.jdungeon.util.PaintBuilder;
import de.jdungeon.util.GifDecoder;

public class AndroidGraphics implements Graphics {
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;


	Rect srcRect = new Rect();
	Rect dstRect = new Rect();

	public static android.graphics.Paint defaultPaint;
	android.graphics.Paint smallPaint = new android.graphics.Paint();
	android.graphics.Paint black = new android.graphics.Paint();
	android.graphics.Paint gray = new android.graphics.Paint();
	android.graphics.Paint labelPaint = new android.graphics.Paint();
	{

		gray.setColor(Color.LTGRAY);


		labelPaint = new android.graphics.Paint();
		labelPaint.setTextSize(10);
		labelPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
		labelPaint.setAntiAlias(true);
		labelPaint.setColor(Color.WHITE);



		black.setColor(Color.BLACK);
		black.setTextSize(12);

		defaultPaint = new android.graphics.Paint();
		defaultPaint.setTextSize(25);
		defaultPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
		defaultPaint.setAntiAlias(true);
		defaultPaint.setColor(Color.RED);


		smallPaint.setColor(Color.RED);
		smallPaint.setTextSize(14);
		smallPaint.setFakeBoldText(true);

	}

	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
	}

	@Override
	public Paint createPaint(PaintBuilder builder) {
		android.graphics.Paint paint = new android.graphics.Paint();
		paint.setColor(AndroidGraphics.convertColor(builder.getColor()));
		paint.setTextSize(builder.getFontSize());

		Paint.Alignment alignment = builder.getAlignment();
		if(alignment == Paint.Alignment.CENTER) {
			paint.setTextAlign(android.graphics.Paint.Align.CENTER);
		}
		if(alignment == Paint.Alignment.LEFT) {
			paint.setTextAlign(android.graphics.Paint.Align.RIGHT);
		}
		if(alignment == Paint.Alignment.RIGHT) {
			paint.setTextAlign(android.graphics.Paint.Align.RIGHT);
		}
		return new AndroidPaint(paint);
	}

	@Override
	public Paint getDefaultPaint() {
		return new AndroidPaint(defaultPaint);
	}

	@Override
	public Paint getSmallPaint() {
		return new AndroidPaint(smallPaint);
	}

	@Override
	public Paint getPaintBlack() {
		return new AndroidPaint(black);
	}

	@Override
	public Paint getPaintWhite() {
		return new AndroidPaint(labelPaint);
	}

	@Override
	public Paint getPaintGray() {
		return new AndroidPaint(gray);
	}

	@Override
	public Image newImage(String fileName, ImageFormat format) {

		Config config = null;
		if (format == ImageFormat.RGB565)
			config = Config.RGB_565;
		else if (format == ImageFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;

		Options options = new Options();
		options.inPreferredConfig = config;
		Bitmap bitmap = null;
		InputStream in = null;
		try {

			in = assets.open(fileName);
			if (fileName.toLowerCase().endsWith("gif")) {
				GifDecoder gd = new GifDecoder();
				gd.read(in);
				bitmap = gd.getBitmap();
			} else {
				bitmap = BitmapFactory.decodeStream(in, null, options);
			}
			if (bitmap == null)
				throw new RuntimeException("Couldn't load bitmap from asset '"
						+ fileName + "'");
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset '"
					+ fileName + "'");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

		if (bitmap.getConfig() == Config.RGB_565)
			format = ImageFormat.RGB565;
		else if (bitmap.getConfig() == Config.ARGB_4444)
			format = ImageFormat.ARGB4444;
		else
			format = ImageFormat.ARGB8888;

		return new AndroidImage(bitmap, format);
	}

	@Override
	public void clearScreen(de.jdungeon.game.Color color) {
		int colorCode = convertColor(color);
		canvas.drawRGB((colorCode & 0xff0000) >> 16, (colorCode & 0xff00) >> 8,
				(colorCode & 0xff));
	}

	@Override
	public void drawLine(int x, int y, int x2, int y2, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint();
		paint.setColor(convertColor(color));
		canvas.drawLine(x, y, x2, y2, paint);
	}

	@Override
	public void drawOval(int x, int y, int width, int height, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint();
		paint.setStyle(Style.STROKE);
		paint.setColor(convertColor(color));
		canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
	}

	@Override
	public void fillOval(int x, int y, int width, int height, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint();
		paint.setColor(convertColor(color));
		canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
	}

	@Override
	public void drawRect(int x, int y, int width, int height, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint();
		paint.setColor(convertColor(color));
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}

	@Override
	public void fillRect(int x, int y, int width, int height, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

		paint.setStrokeWidth(1);
		paint.setColor(convertColor(color));
		paint.setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);

		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x, y);
		path.lineTo(x + width, y);
		path.lineTo(x + width, y + height);
		path.lineTo(x, y + height);
		path.close();

		canvas.drawPath(path, paint);
	}

	@Override
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, de.jdungeon.game.Color color)
	{
		android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

		paint.setStrokeWidth(1);
		paint.setColor(convertColor(color));
		paint.setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);

		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.close();

		canvas.drawPath(path, paint);
	}

	public static int convertColor(de.jdungeon.game.Color color) {
		if(Colors.BLACK.equals(color)) {
			return Color.BLACK;
		}
		if(Colors.BLUE.equals(color)) {
			return Color.BLUE;
		}
		if(Colors.GREEN.equals(color)) {
			return Color.GREEN;
		}
		if(Colors.RED.equals(color)) {
			return Color.RED;
		}
		if(Colors.WHITE.equals(color)) {
			return Color.WHITE;
		}
		if(Colors.YELLOW.equals(color)) {
			return Color.YELLOW;
		}
		if(Colors.GRAY.equals(color)) {
			return Color.GRAY;
		}

		// color not found
		return -1;
	}

	@Override
	public void drawARGB(int a, int r, int g, int b) {
		//android.graphics.Paint paint = new android.graphics.Paint();
		//paint.setStyle(Style.FILL);
		canvas.drawARGB(a, r, g, b);
	}

	@Override
	public void drawString(String text, int x, int y, Paint paint) {
		canvas.drawText(text, x, y, ((AndroidPaint)paint).getPaint());

	}

	@Override
	public void drawImage(Image Image, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight) {
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth;
		srcRect.bottom = srcY + srcHeight;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth;
		dstRect.bottom = y + srcHeight;

		canvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, dstRect, null);
	}

	@Override
	public void drawImage(Image Image, int x, int y) {
		canvas.drawBitmap(((AndroidImage) Image).bitmap, x, y, null);
	}

	@Override
	public void drawScaledImage(Image Image, int x, int y, int width,
			int height, int srcX, int srcY, int srcWidth, int srcHeight) {

		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth;
		srcRect.bottom = srcY + srcHeight;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + width;
		dstRect.bottom = y + height;

		canvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, dstRect, null);

	}

	@Override
	public int getWidth() {
		return frameBuffer.getWidth();
	}

	@Override
	public int getHeight() {
		return frameBuffer.getHeight();
	}
}
