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
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import org.apache.log4j.Logger;

import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Paint;
import de.jdungeon.util.GifDecoder;
import de.jdungeon.util.PaintBuilder;

public class AndroidGraphics implements Graphics {
	private final AssetManager assets;
	private final Bitmap frameBuffer;
	private final Canvas canvas;

	private final Rect srcRect = new Rect();
	private final Rect dstRect = new Rect();
	private final Rect tmpDstRect = new Rect();

	private final android.text.TextPaint defaultPaint = new android.text.TextPaint();;
	private final android.text.TextPaint smallPaint = new android.text.TextPaint();
	private final android.graphics.Paint black = new android.graphics.Paint();
	private final android.text.TextPaint blackText = new TextPaint();
	private final android.text.TextPaint whiteText = new TextPaint();
	private final android.text.TextPaint whiteText25 = new TextPaint();
	private final android.text.TextPaint redText = new TextPaint();
	private final android.text.TextPaint grayText = new TextPaint();
	private final android.graphics.Paint gray = new android.graphics.Paint();
	private android.graphics.Paint labelPaint = new android.text.TextPaint();
	private Bitmap tmpBitmap;
	private Canvas tmpCanvas;
	private int tmpCanvasX;
	private int tmpCanvasY;

	{

		gray.setColor(Color.LTGRAY);
		grayText.setColor(Color.LTGRAY);

		labelPaint = new android.graphics.Paint();
		labelPaint.setTextSize(10);
		labelPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
		labelPaint.setAntiAlias(true);
		labelPaint.setColor(Color.WHITE);

		black.setColor(Color.BLACK);
		black.setTextSize(12);
		blackText.setColor(Color.BLACK);
		blackText.setTextSize(12);

		defaultPaint.setTextSize(25);
		defaultPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
		defaultPaint.setAntiAlias(true);
		defaultPaint.setColor(Color.RED);

		whiteText25.setTextSize(25);
		whiteText25.setTextAlign(android.graphics.Paint.Align.CENTER);
		whiteText25.setAntiAlias(true);
		whiteText25.setColor(Color.WHITE);

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
	public de.jdungeon.game.TextPaint createTextPaint(PaintBuilder builder) {
		android.text.TextPaint paint = new android.text.TextPaint();
		paint.setColor(AndroidGraphics.convertColor(builder.getColor()));
		paint.setTextSize(builder.getFontSize());

		Paint.Alignment alignment = builder.getAlignment();
		if (alignment == Paint.Alignment.CENTER) {
			paint.setTextAlign(android.graphics.Paint.Align.CENTER);
		}
		if (alignment == Paint.Alignment.LEFT) {
			paint.setTextAlign(android.graphics.Paint.Align.LEFT);
		}
		if (alignment == Paint.Alignment.RIGHT) {
			paint.setTextAlign(android.graphics.Paint.Align.RIGHT);
		}
		return new AndroidTextPaint(paint, builder.getFontSize());
	}


	@Override
	public de.jdungeon.game.TextPaint getSmallPaint() {
		return new AndroidTextPaint(smallPaint);
	}


	@Override
	public de.jdungeon.game.TextPaint getTextPaintWhite25() {
		return new AndroidTextPaint(whiteText25);
	}



	@Override
	public de.jdungeon.game.TextPaint getTextPaintBlack() {
		return new AndroidTextPaint(blackText);
	}

	@Override
	public de.jdungeon.game.TextPaint getTextPaintGray() {
		return new AndroidTextPaint(grayText);
	}

	public Image newImage(String fileName, ImageFormat format) {

		Config config = null;
		if (format == ImageFormat.RGB565) {
			config = Config.RGB_565;
		}
		else if (format == ImageFormat.ARGB4444) {
			config = Config.ARGB_4444;
		}
		else {
			config = Config.ARGB_8888;
		}

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
			}
			else {
				bitmap = BitmapFactory.decodeStream(in, null, options);
			}
			if (bitmap == null) {
				throw new RuntimeException("Couldn't load bitmap from asset '"
						+ fileName + "'");
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset '"
					+ fileName + "'");
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (IOException e) {
					Logger.getLogger(this.getClass()).warn("Could close InputStream of bitmap loading from asset", e);
				}
			}
		}
		if (bitmap.getConfig() == Config.RGB_565) {
			format = ImageFormat.RGB565;
		}
		else if (bitmap.getConfig() == Config.ARGB_4444) {
			format = ImageFormat.ARGB4444;
		}
		else {
			format = ImageFormat.ARGB8888;
		}
		return new AndroidImage(bitmap, format);
	}


	@Override
	public void drawLine(int x, int y, int x2, int y2, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint();
		paint.setColor(convertColor(color));
		if (tmpCanvas == null) {
			canvas.drawLine(x, y, x2, y2, paint);
		}
		else {
			tmpCanvas.drawLine(x - tmpCanvasX, y - tmpCanvasY, x2 - tmpCanvasX, y2 - tmpCanvasY, paint);
		}
	}

	@Override
	public void drawOval(int x, int y, int width, int height, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint();
		paint.setStyle(Style.STROKE);
		paint.setColor(convertColor(color));
		if (tmpCanvas == null) {
			canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
		}
		else {
			tmpCanvas.drawOval(new RectF(x - tmpCanvasX, y - tmpCanvasY, x + width - tmpCanvasX, y + height - tmpCanvasY), paint);
		}
	}

	@Override
	public void fillOval(int x, int y, int width, int height, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint();
		paint.setColor(convertColor(color));
		if (tmpCanvas == null) {
			canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
		}
		else {
			tmpCanvas.drawOval(new RectF(x - tmpCanvasX, y - tmpCanvasY, x + width - tmpCanvasX, y + height - tmpCanvasY), paint);
		}
	}

	@Override
	public void drawRect(int x, int y, int width, int height, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint();
		paint.setColor(convertColor(color));
		paint.setStyle(Style.STROKE);
		if (tmpCanvas == null) {
			canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
		}
		else {
			tmpCanvas.drawRect(x - tmpCanvasX, y - tmpCanvasY, x + width - 1 - tmpCanvasX, y + height - 1 - tmpCanvasY, paint);
		}
	}

	@Override
	public void fillRect(int x, int y, int width, int height, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

		paint.setStrokeWidth(1);
		paint.setColor(convertColor(color));
		paint.setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);

		if (tmpCanvas == null) {
			Path path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
			path.moveTo(x, y);
			path.lineTo(x + width, y);
			path.lineTo(x + width, y + height);
			path.lineTo(x, y + height);
			path.close();
			canvas.drawPath(path, paint);
		}
		else {
			Path pathTmp = new Path();
			pathTmp.setFillType(Path.FillType.EVEN_ODD);
			pathTmp.moveTo(x - tmpCanvasX, y - tmpCanvasY);
			pathTmp.lineTo(x + width - tmpCanvasX, y - tmpCanvasY);
			pathTmp.lineTo(x + width - tmpCanvasX, y + height - tmpCanvasY);
			pathTmp.lineTo(x - tmpCanvasX, y + height - tmpCanvasY);
			pathTmp.close();
			tmpCanvas.drawPath(pathTmp, paint);
		}
	}

	@Override
	public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

		paint.setStrokeWidth(1);
		paint.setColor(convertColor(color));
		paint.setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);

		if (tmpCanvas == null) {
			Path path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
			path.moveTo(x1, y1);
			path.lineTo(x2, y2);
			path.lineTo(x3, y3);
			path.close();
			canvas.drawPath(path, paint);
		}
		else {
			Path pathTmp = new Path();
			pathTmp.setFillType(Path.FillType.EVEN_ODD);
			pathTmp.moveTo(x1 - tmpCanvasX, y1 - tmpCanvasY);
			pathTmp.lineTo(x2 - tmpCanvasX, y2 - tmpCanvasY);
			pathTmp.lineTo(x3 - tmpCanvasX, y3 - tmpCanvasY);
			pathTmp.close();
			canvas.drawPath(pathTmp, paint);
		}
	}

	@Override
	public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, de.jdungeon.game.Color color) {
		android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

		paint.setStrokeWidth(1);
		paint.setColor(convertColor(color));
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);

		if (tmpCanvas == null) {
			Path path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
			path.moveTo(x1, y1);
			path.lineTo(x2, y2);
			path.lineTo(x3, y3);
			path.close();
			canvas.drawPath(path, paint);
		}
		else {
			Path pathTmp = new Path();
			pathTmp.setFillType(Path.FillType.EVEN_ODD);
			pathTmp.moveTo(x1 - tmpCanvasX, y1 - tmpCanvasY);
			pathTmp.lineTo(x2 - tmpCanvasX, y2 - tmpCanvasY);
			pathTmp.lineTo(x3 - tmpCanvasX, y3 - tmpCanvasY);
			pathTmp.close();
			canvas.drawPath(pathTmp, paint);
		}
	}

	public static int convertColor(de.jdungeon.game.Color color) {
		if (Colors.BLACK.equals(color)) {
			return Color.BLACK;
		}
		if (Colors.BLUE.equals(color)) {
			return Color.BLUE;
		}
		if (Colors.GREEN.equals(color)) {
			return Color.GREEN;
		}
		if (Colors.RED.equals(color)) {
			return Color.RED;
		}
		if (Colors.WHITE.equals(color)) {
			return Color.WHITE;
		}
		if (Colors.YELLOW.equals(color)) {
			return Color.YELLOW;
		}
		if (Colors.GRAY.equals(color)) {
			return Color.GRAY;
		}

		// color not found
		return -1;
	}


	@Override
	public void setTempCanvas(int x, int y, int width, int height) {
		if (tmpBitmap != null && tmpCanvas != null) {
			flushAndResetTempCanvas();
		}
		tmpCanvasX = x;
		tmpCanvasY = y;
		tmpBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
		tmpCanvas = new Canvas(tmpBitmap);
		// TODO: not working unfortunately
		//canvas.drawColor(Color.TRANSPARENT);


	}

	@Override
	public void flushAndResetTempCanvas() {
		if(tmpBitmap != null) {
			canvas.drawBitmap(tmpBitmap, tmpCanvasX, tmpCanvasY, null);
		}
		tmpBitmap = null;
		tmpCanvas = null;
	}

	@Override
	public Image getTempImage() {
		if(tmpBitmap == null) {
			return null;
		}
		ImageFormat format;
		if (tmpBitmap.getConfig() == Config.RGB_565) {
			format = ImageFormat.RGB565;
		}
		else if (tmpBitmap.getConfig() == Config.ARGB_4444) {
			format = ImageFormat.ARGB4444;
		}
		else {
			format = ImageFormat.ARGB8888;
		}
		if(tmpBitmap != null) {
			return new AndroidImage(tmpBitmap, format);

		} else {
			return null;
		}
	}

	@Override
	public void drawString(String text, int x, int y, de.jdungeon.game.TextPaint paint) {
		de.jdungeon.game.TextPaint p;
		if (paint instanceof PaintBuilder) {
			p = createTextPaint(((PaintBuilder) paint));
		}
		else {
			p = paint;
		}
		TextPaint textPaint = ((AndroidTextPaint) p).getPaint();
		if (tmpCanvas == null) {
			canvas.drawText(text, x, y, textPaint);
		}
		else {
			tmpCanvas.drawText(text, x - tmpCanvasX, y - tmpCanvasY, textPaint);
		}
	}

	@Override
	public void drawString(String text, int x, int y, int width, de.jdungeon.game.TextPaint paint) {

		de.jdungeon.game.TextPaint p;
		if (paint instanceof PaintBuilder) {
			p = createTextPaint(((PaintBuilder) paint));
		}
		else {
			p = paint;
		}



		TextPaint textPaint = ((AndroidTextPaint) p).getPaint();
		int fontsize = ((AndroidTextPaint) p).getFontsize();
		textPaint.setTextSize(fontsize);

		Layout.Alignment layoutAlignment = Layout.Alignment.ALIGN_CENTER;
		if(textPaint.getTextAlign() == android.graphics.Paint.Align.LEFT) {
			layoutAlignment = Layout.Alignment.ALIGN_NORMAL;
		}
		if(textPaint.getTextAlign() == android.graphics.Paint.Align.RIGHT) {
			layoutAlignment = Layout.Alignment.ALIGN_OPPOSITE;
		}
		StaticLayout layout = new StaticLayout(text, textPaint, width,
				layoutAlignment, 1, 1, true);

		if (tmpCanvas == null) {
			canvas.save();
			canvas.translate(x, y);
			layout.draw(canvas);
			canvas.restore();
			//canvas.drawText(text, x, y, textPaint);
		}
		else {

			tmpCanvas.save();
			tmpCanvas.translate(x - tmpCanvasX, y - tmpCanvasY);
			layout.draw(tmpCanvas);
			tmpCanvas.restore();

			//tmpCanvas.drawText(text, x - tmpCanvasX, y - tmpCanvasY, textPaint);
		}
	}


	@Override
	public void drawImage(Image image, int x, int y, boolean nonTmp) {
		if (tmpCanvas == null || nonTmp) {
			canvas.drawBitmap(((AndroidImage) image).bitmap, x, y, null);
		}
		else {
			tmpCanvas.drawBitmap(((AndroidImage) image).bitmap, x - tmpCanvasX, y - tmpCanvasY, null);
		}
	}

	@Override
	public void drawScaledImage(Image Image, int x, int y, int width,
								int height, int srcX, int srcY, int srcWidth, int srcHeight) {
		drawScaledImage( Image,  x,  y,  width,
		 height,  srcX,  srcY,  srcWidth,  srcHeight, false);
	}


	@Override
	public void drawScaledImage(Image Image, int x, int y, int width,
								int height, int srcX, int srcY, int srcWidth, int srcHeight, boolean nonTmp) {

		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth;
		srcRect.bottom = srcY + srcHeight;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + width;
		dstRect.bottom = y + height;

		if (tmpCanvas == null || nonTmp) {
			canvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, dstRect, null);
		}
		else {
			tmpDstRect.left = dstRect.left - tmpCanvasX;
			tmpDstRect.top = dstRect.top - tmpCanvasY;
			tmpDstRect.right = dstRect.right - tmpCanvasX;
			tmpDstRect.bottom = dstRect.bottom - tmpCanvasY;
			tmpCanvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, tmpDstRect, null);
		}
	}

}
