package de.jdungeon.libgdx.adapter;

import android.graphics.Bitmap;

import de.jdungeon.game.Graphics.ImageFormat;
import de.jdungeon.game.Image;

public class LibgdxImage implements Image {
	public Bitmap bitmap;
    ImageFormat format;
   
    public LibgdxImage(Bitmap bitmap, ImageFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public ImageFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }      
}
