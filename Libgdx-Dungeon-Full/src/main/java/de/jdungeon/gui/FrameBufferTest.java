package de.jdungeon.gui;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 05.01.21.
 */
public class FrameBufferTest {

	FrameBuffer frameBuffer;

	SpriteBatch spriteBatch;
	BitmapFont font;

	TextureRegion bufferTextureRegion;
	Texture texture;
	OrthographicCamera cam;

	public void create() {

		//cam=new OrthographicCamera(Gdx.de.jdungeon.graphics.getWidth(),Gdx.de.jdungeon.graphics.getHeight());
		//cam.setToOrtho(false);

		spriteBatch=new SpriteBatch();
		font=new BitmapFont();

		int w=texture.getWidth();
		int h=texture.getHeight();

		frameBuffer=new FrameBuffer(Pixmap.Format.RGBA8888,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),false) ;
		frameBuffer.begin();

		Gdx.gl.glClearColor(0f,0f,0f,0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		font.draw(spriteBatch,"Score :100",100,100);
		spriteBatch.end();

		bufferTextureRegion = new TextureRegion(frameBuffer.getColorBufferTexture(),0,0,frameBuffer.getWidth(),frameBuffer.getHeight());
		bufferTextureRegion.flip(false,true);

		ByteBuffer buf;
		Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGB888);
		buf = pixmap.getPixels();
		Gdx.gl.glReadPixels(0, 0, w, h, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, buf);

		frameBuffer.end();

		PixmapIO.writePNG(Gdx.files.external("output.png"), pixmap);
	}
}
