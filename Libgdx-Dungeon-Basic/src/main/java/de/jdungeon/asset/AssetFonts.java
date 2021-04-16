package de.jdungeon.asset;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.12.19.
 */
public class AssetFonts {

    public static final float FONT_SIZE_BIG = 2f;
    public static final String FONT_FILE_ARIAL = "font/arial-15.fnt";
    public static final String FONT_FILE_ARIAL_BOLD = "font/FontsFree-Net-arial-bold.ttf";
    public static final float FONT_SIZE_NORMAL = 1f;
    public static final float FONT_SIZE_SMALL = 0.75f;
    public static final float FONT_SIZE_SMALLER = 0.5f;
    public final BitmapFont defaultSmallFlipped;
    public BitmapFont defaultTitle;
    public final BitmapFont hit;
    public final BitmapFont defaultNormalFlipped;
    public final BitmapFont defaultBigFlipped;

    public final BitmapFont defaultSmall;
    public final BitmapFont defaultNormal;
    public final BitmapFont defaultBig;

    public static AssetFonts instance = new AssetFonts();


    public AssetFonts() {
        FileHandle fileHandleArial = getFileHandle(FONT_FILE_ARIAL);
        defaultBigFlipped = new BitmapFont(fileHandleArial, true);
        defaultBigFlipped.getData().setScale(FONT_SIZE_BIG);
        setFilter(defaultBigFlipped);

        defaultBig = new BitmapFont(fileHandleArial, false);
        defaultBig.getData().setScale(FONT_SIZE_BIG);
        setFilter(defaultBig);

        defaultNormalFlipped = new BitmapFont(fileHandleArial, true);
        defaultNormalFlipped.getData().setScale(FONT_SIZE_NORMAL);
        setFilter(defaultNormalFlipped);

        defaultNormal = new BitmapFont(fileHandleArial, false);
        defaultNormal.getData().setScale(FONT_SIZE_NORMAL);
        setFilter(defaultNormal);

        defaultSmallFlipped = new BitmapFont(fileHandleArial, true);
        defaultSmallFlipped.getData().setScale(FONT_SIZE_SMALL);
        setFilter(defaultSmallFlipped);


        hit = new BitmapFont(fileHandleArial, true);
        hit.getData().setScale(FONT_SIZE_SMALLER);
        setFilter(hit);
        hit.setColor(Color.RED);

        defaultSmall = new BitmapFont(fileHandleArial, false);
        defaultSmall.getData().setScale(FONT_SIZE_SMALL);
        setFilter(defaultSmall);

        defaultTitle = defaultBigFlipped;

		/*
		if(Gdx.app.getType() == Application.ApplicationType.Android) {
			defaultTitle = defaultBigFlipped;
		} else {
			FileHandle fontFile = Gdx.files.internal(FONT_FILE_ARIAL_BOLD);
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
			FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameter.size = 18;
			parameter.flip = true;
			defaultTitle = generator.generateFont(parameter);
			setFilter(defaultTitle);
			generator.dispose();
		}

		 */


    }

    private FileHandle getFileHandle(String path) {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            path = "assets/" + path;
        }
        return Gdx.files.internal(path);
    }

    private void setFilter(BitmapFont defaultBigFlipped) {
        defaultBigFlipped.getRegion()
                .getTexture()
                .setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
}
