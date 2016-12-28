package de.jdungeon.game;

import android.graphics.Paint;

public interface Graphics {
    public static enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }

    Image newImage(String fileName, ImageFormat format);

    void clearScreen(int color);

    void drawLine(int x, int y, int x2, int y2, int color);

    void drawRect(int x, int y, int width, int height, int color);
    
	void drawOval(int x, int y, int width, int height, int color);

	void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color);

    void drawImage(Image image, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);

    void drawImage(Image Image, int x, int y);
    
    void drawScaledImage(Image Image, int x, int y, int width,
			int height, int srcX, int srcY, int srcWidth, int srcHeight);

    void drawString(String text, int x, int y, Paint paint);

    int getWidth();

    int getHeight();

    void drawARGB(int i, int j, int k, int l);

}
