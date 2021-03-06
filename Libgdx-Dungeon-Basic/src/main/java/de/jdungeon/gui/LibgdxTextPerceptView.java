package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.log.Log;
import de.jdungeon.text.Statement;
import de.jdungeon.util.JDDimension;

import de.jdungeon.asset.Assets;

public class LibgdxTextPerceptView extends AbstractLibgdxGUIElement {

    private static final int LINE_PADDING_SINGLE = 5;
    private static final int LINE_PADDING_DOUBLE = 2 * LINE_PADDING_SINGLE;

    private final List<Statement> cache = new ArrayList<>();
    private List<Statement> all = new ArrayList<>();
    private Statement currentInsert = null;
    private static final float animationTime = 0.8f;
    private float timer = 0;
    private final int lineHeight;
    private final BitmapFont font = Assets.instance.fonts.defaultNormalFlipped;

    private Texture offlineTextTexture;
    private final FrameBuffer frameBuffer;
    private final Batch spriteBatch = new SpriteBatch();
    private final GlyphLayout glyphLayoutRoundNumber = new GlyphLayout();
    private final GlyphLayout glyphLayoutStatementText = new GlyphLayout();

    private final TextureRegion backgroundTx;
    private final TextureRegion borderTx;

    private static JDPoint position() {
        return new JDPoint(
                Gdx.app.getGraphics().getWidth() / 2 - Gdx.app.getGraphics().getWidth() / 4,
                -1 * (Gdx.app.getGraphics().getWidth() / 2 - 44));
    }

    private static JDDimension dimension() {
        return new JDDimension(Gdx.app.getGraphics()
                .getWidth() / 2, Gdx.app.getGraphics()
                .getWidth() / 2);
    }

    public LibgdxTextPerceptView() {
        super(position(), dimension());
        lineHeight = (int) font.getLineHeight() + 4;

        int height = Gdx.app.getGraphics()
                .getWidth() / 2;


        //backgroundTx = Assets.instance.getAtlasRegion(GUIImageManager.BLACK_BACKGROUND, Assets.instance.getGuiAtlas());
        backgroundTx = Assets.instance.getAtlasRegion("window_badge", Assets.instance.getGuiAtlas());
        borderTx = Assets.instance.getAtlasRegion(GUIImageManager.SC_RECT, Assets.instance.getGuiAtlas());

        int framebufferWidth = (int) (height * (((float) Gdx.graphics.getWidth()) / Gdx.graphics.getHeight()));
        int frameBufferHeight = height;
        Log.info("Initialising world offscreen FrameBuffer of size: " + framebufferWidth + " / " + frameBufferHeight);
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, framebufferWidth, frameBufferHeight, false);
        offlineTextTexture = updateOfflineMessageTexture();



        // TODO: fix resize issue!
    }

    public void addTextPercept(Statement p) {
        cache.add(p);
    }


    private static int getNumberOfLines(Statement p) {
        if (p == null) return 0;
        String text = p.getText();
        String linebreak = "\n";
        if (text.contains(linebreak)) {
            return text.split(linebreak).length;
        }
        return 1;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    private Texture updateOfflineMessageTexture() {

        // prepare and activate offscreen framebuffer
        frameBuffer.bind();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        spriteBatch.begin();

        // only draw the inner badge background color (the frame is rendered outside the framebuffer)
        if(backgroundTx != null) {
            spriteBatch.draw(backgroundTx, -6, 0, this.getDimension().getWidth() + 12, this.getDimension().getHeight() - this.getPositionOnScreen().getY());
        }

        // draw actual content
        ListIterator<Statement> listIterator = all.listIterator(all.size());
        int yOffsetUpwards = 0;
        if (listIterator.hasPrevious()) {
            yOffsetUpwards += fetchPrepareAndDrawPreviousStatement(listIterator, yOffsetUpwards, LINE_PADDING_SINGLE);
        }
        while (listIterator.hasPrevious() && yOffsetUpwards < frameBuffer.getHeight()) {
            yOffsetUpwards += fetchPrepareAndDrawPreviousStatement(listIterator, yOffsetUpwards, LINE_PADDING_DOUBLE);
        }

        // close and unbind SpriteBatch and Framebuffer
        spriteBatch.end();
        frameBuffer.unbind();

        return frameBuffer.getColorBufferTexture();
    }

    private int fetchPrepareAndDrawPreviousStatement(ListIterator<Statement> listIterator, int yOffsetUpwardsOverall, int linePadding) {
        Statement textPercept = listIterator.previous();
        // TODO: use constant width for three-digit round number (to have always enough space without wrap!)
        glyphLayoutRoundNumber.setText(font, toThreeDigitsString(textPercept.getRound()), Color.GRAY, frameBuffer.getWidth() * 0.05f, Align.left, true);
        glyphLayoutStatementText.setText(font, textPercept.getText(), Color.WHITE, frameBuffer.getWidth() * 0.9f, Align.left, true);

        // TODO: use all the rest of the width which is not used be the three-digit round number.
        int yOffsetUpwardsStatementIncrement = (int) glyphLayoutStatementText.height + linePadding;
        yOffsetUpwardsOverall += yOffsetUpwardsStatementIncrement;
        int yCoord = frameBuffer.getHeight() - yOffsetUpwardsOverall;

        font.draw(spriteBatch, glyphLayoutRoundNumber, 6, yCoord);
        font.draw(spriteBatch, glyphLayoutStatementText, frameBuffer.getWidth() * 0.07f, yCoord);
        return yOffsetUpwardsStatementIncrement;
    }

    @Override
    public void paint(SpriteBatch batch, float deltaTime) {

        int x = this.position.getX();
        int y = this.position.getY();
        int width = this.dimension.getWidth();
        int height = this.dimension.getHeight();

        if(backgroundTx != null) {
            batch.draw(backgroundTx, x - 6, -6, width + 12, this.getDimension().getHeight() + this.getPositionOnScreen().getY() + 12);
        }
        //batch.draw(borderTx, x-4, y, width + 6, height);

        int timeOffset = 0;
        if (currentInsert != null) {
            timeOffset = (int) (getNumberOfLines(currentInsert) * lineHeight * timer / animationTime);
        }
        batch.draw(offlineTextTexture,
                getPositionOnScreen().getX(),
                this.position.getY() - timeOffset - 1,
                width,
                height,
                0,
                0,
                width,
                height,
                false,
                true);
    }

    private CharSequence toThreeDigitsString(int round) {
        String stringValue = Integer.toString(round);
        if (stringValue.length() == 1) {
            return "00" + stringValue;
        }
        if (stringValue.length() == 2) {
            return "0" + stringValue;
        }
        return stringValue;
    }

    @Override
    public void update(float deltaTime, int round) {
        if (currentInsert == null) {
            if (!cache.isEmpty()) {
                animateNextPercept();
            }
        } else {
            if (timer > animationTime) {
                all.add(currentInsert);
                offlineTextTexture = updateOfflineMessageTexture();
                if (!cache.isEmpty()) {
                    animateNextPercept();
                } else {
                    currentInsert = null;
                }
            } else {
                timer += deltaTime * 3;
            }
        }

        if (all.size() > 60) {
            all = all.subList(all.size() - 20, all.size());
        }
    }

    @Override
    public boolean handleClickEvent(int screenX, int screenY) {
        return false;
    }

    private void animateNextPercept() {
        currentInsert = cache.remove(0);
        timer = 0;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean handlePanEvent(float x, float y, float dx, float dy) {
        int newY = this.position.getY() + (int) dy;
        if (newY > (-1 * this.getDimension().getHeight()) && newY < 0) {
            this.position.setY(newY);
            return true;
        }
        return false;
    }
}
