package de.jdungeon.game;

import de.jdungeon.util.PaintBuilder;

public interface Graphics {
    enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }

	// TODO: refactor use of Paint objects
	Paint getDefaultPaint();

	Paint getSmallPaint();

	Paint getPaintBlack();

	Paint getPaintWhite();
	Paint getPaintGray();

	Paint createPaint(PaintBuilder builder);

    Image newImage(String fileName, ImageFormat format);

    void clearScreen(Color color);

    void drawLine(int x, int y, int x2, int y2, Color color);

    void drawRect(int x, int y, int width, int height, Color color);

	void fillRect(int x, int y, int width, int height, Color color);

	void drawOval(int x, int y, int width, int height, Color color);

	void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color);

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
