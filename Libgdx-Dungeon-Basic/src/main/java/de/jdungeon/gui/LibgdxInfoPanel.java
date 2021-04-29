package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import de.jdungeon.dungeon.ChestInfo;
import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.InfoEntity;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.graphics.ImageManager;
import de.jdungeon.graphics.JDImageProxy;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.location.LocationInfo;
import de.jdungeon.log.Log;
import de.jdungeon.util.JDColor;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.gui.ColorConverter;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.app.gui.skillselection.SkillImageManager;
import de.jdungeon.asset.Assets;
import de.jdungeon.game.Color;
import de.jdungeon.game.LibgdxGraphics;
import de.jdungeon.util.PaintBuilder;
import de.jdungeon.util.Pair;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.02.20.
 */
public class LibgdxInfoPanel extends LibgdxSlidingInOutGUIElement {

    private static final String WINDOW_BUBBLE = "win-bubble";

    private final InventoryImageManager inventoryImageManager;
    private PlayerController player;
    private final GUIImageManager guiImageManager;
    private final TextureAtlas.AtlasRegion bubble;
    private final GlyphLayout layout;
    private LibgdxHealthBar healthBar;
    private LibgdxHealthBar oxygenBar;
    private final int bubbleSizeX;
    private final int bubbleSizeY;
    private final int bubblePosX;
    private final int bubblePosY;

    public LibgdxInfoPanel(JDPoint position, JDDimension dimension, GUIImageManager guiImageManager, PlayerController player) {
        super(position, dimension, new JDPoint(position.getX()
                - dimension.getWidth() + 10, position.getY()));
        inventoryImageManager = new InventoryImageManager(guiImageManager);
        this.player = player;
        SkillImageManager skillImageManager = new SkillImageManager(guiImageManager);
        this.guiImageManager = guiImageManager;
        layout = new GlyphLayout();

        // init border
        bubble = Assets.instance.getAtlasRegion(WINDOW_BUBBLE, Assets.instance.getGuiAtlas());

        bubbleSizeX = (int) (this.getDimension().getWidth() / 1.5);
        bubbleSizeY = (int) (this.getDimension().getHeight() / 2);
        bubblePosX = this.getCurrentX() + this.getDimension().getWidth() / 2 - bubbleSizeX / 2;
        bubblePosY = this.position.getY() - bubbleSizeY / 2;
    }

    private Paragraphable content;

    public void setContent(Paragraphable entity) {
        this.content = entity;
        if (content == null) {
            healthBar = null;
            oxygenBar = null;
            slideOut();
        } else {
            slideIn();
        }

        if (entity instanceof FigureInfo) {
            int posY = this.getPositionOnScreen().getY() - bubbleSizeY / 8;
            int barLength = bubbleSizeX / 3;
            int posX = bubblePosX + (bubbleSizeX / 2 - barLength / 2) + 2;
            healthBar = new LibgdxHealthBar(new JDPoint(posX, posY), new JDDimension(barLength, 8), (FigureInfo) entity, LibgdxHealthBar.Kind.health);
            oxygenBar = new LibgdxHealthBar(new JDPoint(posX, posY + 11), new JDDimension(barLength, 8), (FigureInfo) entity, LibgdxHealthBar.Kind.oxygen);
        }
    }

    public Paragraphable getContent() {
        return content;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public void paint(SpriteBatch batch, float deltaTime) {

        // trigger animation update
        super.paint(batch, deltaTime);

        /*
         * draw background
         */
        int x = getCurrentX();
        this.drawBackground(batch, x, position.getY());

        Pair<String, RenderInfo> im = getImage();
        if (im != null) {


            batch.draw(bubble, bubblePosX, bubblePosY, bubbleSizeX / 2, bubbleSizeY / 2, bubbleSizeX, bubbleSizeY, 1f, 1f, 180);

            TextureAtlas.AtlasRegion atlasRegion = Assets.instance.findTexture(im.getA(), true);
            if (atlasRegion != null) {
                RenderInfo renderInfo = im.getB();
                int imageSize = (int) ((bubbleSizeX / 2.5) * renderInfo.getScaleFactor());
                int imagePosX = 2 + this.getCurrentX() + this.getDimension().getWidth() / 2 - imageSize / 2;
                int imagePosY = this.position.getY() - imageSize / 2 + bubbleSizeY / 8;
                //batch.draw(atlasRegion, imagePosX, imagePosY, imageSize, imageSize);
                float scaleY = 1.0f;
                if (renderInfo.isFlipY()) {
                    scaleY = -1.0f;
                    imagePosY += imageSize;
                }
                batch.draw(atlasRegion, imagePosX, imagePosY, 0, 0, imageSize, imageSize, 1f, scaleY, 0);
            }
        }

        /*
         * print information
         */
        if (this.content != null) {
            Paragraph[] paragraphs = this.content.getParagraphs();

            if (paragraphs != null) {
                int posCounterY = 35;
                BitmapFont font = Assets.instance.fonts.defaultNormalFlipped;


                // draw header text
                String headerText = this.content.getHeaderName();
                font = Assets.instance.fonts.defaultTitle;
                float headerTargetWidth = this.dimension.getWidth() * 0.8f;
                layout.setText(font, headerText, com.badlogic.gdx.graphics.Color.WHITE, headerTargetWidth, Align.center, true);
                float targetWidthMargin = this.dimension.getWidth() * 0.1f;
                float fontPosX = x + targetWidthMargin;
                int fontPosYHeader = position.getY() + (this.dimension.getHeight() / 6) + posCounterY;
                float fontBeginsX = (this.dimension.getWidth() + targetWidthMargin) / 2 - layout.width / 2 - 2; // why the hell -2 ???
                int badgeMarginX = 15;
                int badgeMarginY = 10;
                float windowBadgeHeight = layout.height + 2 * badgeMarginY;
                if (window_badge != null) {
                    batch.draw(window_badge, fontBeginsX - badgeMarginX, fontPosYHeader - badgeMarginY, layout.width + 2 * badgeMarginX, windowBadgeHeight);
                }
                font.draw(batch, layout, fontPosX, fontPosYHeader);


                font = Assets.instance.fonts.defaultNormalFlipped;

                // draw role
                posCounterY += windowBadgeHeight;

                String roleLineText = content.getRole();
                if (roleLineText != headerText) {
                    if (content instanceof FigureInfo) {
                        boolean hostile = ((FigureInfo) content).isHostile(this.player.getFigure());
                        if (hostile) {
                            roleLineText += " (fies)";
                        } else {
                            roleLineText += " (freundlich)";
                        }
                    }
                    layout.setText(font, roleLineText, com.badlogic.gdx.graphics.Color.WHITE, headerTargetWidth, Align.center, true);
                    font.draw(batch, layout, fontPosX, fontPosYHeader + posCounterY);
                    posCounterY += layout.height + badgeMarginY;
                }

                // draw description
                String status = content.getStatus();
                if (status != null && status.length() > 0) {
                    layout.setText(font, "Status: " + status, com.badlogic.gdx.graphics.Color.WHITE, headerTargetWidth, Align.center, true);
                    font.draw(batch, layout, fontPosX, fontPosYHeader + posCounterY);

                }

                posCounterY += layout.height + badgeMarginY;

                // draw description
                String description = content.getDescription();
                if (description != null && description.length() > 0) {
                    layout.setText(font, description, com.badlogic.gdx.graphics.Color.WHITE, headerTargetWidth, Align.center, true);
                    font.draw(batch, layout, fontPosX, fontPosYHeader + posCounterY);

                }
            }
        }

        if (healthBar != null) {
            healthBar.paint(batch, deltaTime);
        }
        if (oxygenBar != null) {
            oxygenBar.paint(batch, deltaTime);
        }
    }

    static class RenderInfo {
        float scaleFactor;

        boolean flipY = false;

        public RenderInfo(float scaleFactor) {
            this.scaleFactor = scaleFactor;
        }

        public RenderInfo(float scaleFactor, boolean flipY) {
            this.scaleFactor = scaleFactor;
            this.flipY = flipY;
        }

        public float getScaleFactor() {
            return scaleFactor;
        }

        public boolean isFlipY() {
            return flipY;
        }
    }

    private Pair<String, RenderInfo> getImage() {
        if (content == null) {
            return null;
        }
        // wtf, I have no idea why some entities need to be flippedY to be shown correctly and others not...
        if (content instanceof DoorInfo) {
            return new Pair<>(ImageManager.getImage((DoorInfo) content).getFilenameBlank(), new RenderInfo(1.0f, true));
        }
        if (content instanceof LocationInfo) {
            return new Pair<>(ImageManager.getImage(((LocationInfo) content).getShrineClass()).getFilenameBlank(), new RenderInfo(1.0f));
        }
        if (content instanceof ChestInfo) {
            return new Pair<>(ImageManager.getImage((ChestInfo) content).getFilenameBlank(), new RenderInfo(1.0f, false));
        }
        if (content instanceof FigureInfo) {
            try {
                if (((FigureInfo) content).isDead()) {
                    return new Pair<>(ImageManager.deathImage.getFilenameBlank(), new RenderInfo(1.5f, true));
                }
            } catch (NullPointerException e) {
                return null;
            }
            JDImageProxy<?> image = ImageManager.getImage((FigureInfo) content, RouteInstruction.Direction.South);
            if (image == null) {
                Log.severe("Image was null for de.jdungeon.figure: " + content + " dir: " + RouteInstruction.Direction.South.name());
                return null;
            }
            return new Pair<>(image.getFilenameBlank(), new RenderInfo(1.5f, true));
        }
        if (content instanceof ItemInfo) {
            String image = inventoryImageManager.getJDImage((ItemInfo) content).getFilenameBlank();
            if (image.equals(guiImageManager.getJDImage(GUIImageManager.NO_IMAGE).getFilenameBlank())) {
                image = ImageManager.getImage((ItemInfo) content).getFilenameBlank();
            }
            return new Pair<>(image, new RenderInfo(1.0f));
        }
        return null;
    }
}
