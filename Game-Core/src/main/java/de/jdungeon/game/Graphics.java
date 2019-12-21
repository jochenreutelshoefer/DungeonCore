package de.jdungeon.game;

import de.jdungeon.util.PaintBuilder;

public interface Graphics {
    enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }


	TextPaint getSmallPaint();

	TextPaint getTextPaintGray();

	TextPaint getTextPaintBlack();


	TextPaint getTextPaintWhite25();


	TextPaint createTextPaint(PaintBuilder builder);


    void drawLine(int x, int y, int x2, int y2, Color color);

    void drawRect(int x, int y, int width, int height, Color color);

	void fillRect(int x, int y, int width, int height, Color color);

	void drawOval(int x, int y, int width, int height, Color color);

	void fillOval(int x, int y, int width, int height, Color color);

	void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color);

	void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color);


    void drawImage(Image Image, int x, int y, boolean nonTmp);
    
    void drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight, boolean nonTmp);

	void drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight);

    void drawString(String text, int x, int y, TextPaint paint);

	void drawString(String text, int x, int y, int width, TextPaint paint);

	void setTempCanvas(int x, int y, int widhth, int height);

	void flushAndResetTempCanvas();

	Image getTempImage();

}
