package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.asset.Assets;
import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.event.EventManager;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.event.InfoObjectClickedEvent;
import de.jdungeon.gui.LibgdxDrawUtils;
import de.jdungeon.gui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class LibgdxDoorElement extends LibgdxAnimatedSmartControlElement {

    private boolean locked;
    private boolean hasKey;
    private final ActionAssembler guiControl;
    private String image;
    private FigureInfo player;
    private DoorInfo door;
    private RouteInstruction.Direction direction;
    private TextureRegion textureRegion;
    private int absolutePositionX;
    private int absolutePositionY;
    private int dimensionX;
    private int dimensionY;

    public LibgdxDoorElement(JDPoint position, final JDDimension dimension, final LibgdxGUIElement parent, RouteInstruction.Direction direction, ActionAssembler guiControl) {
        super(position, dimension, parent);
        this.direction = direction;

        this.guiControl = guiControl;
        player = guiControl.getFigure();

        /*
        // prepare highlight animation drawables
        for (int i = 0; i < animationShapes.length; i++) {
            final int finalI = i;
            animationShapes[i] = new LibgdxDrawable() {
                @Override
                public void paint(ShapeRenderer renderer) {
                    JDPoint parentPosition = parent.getPositionOnScreen();
                    JDPoint posRelative = getPositionOnScreen();
                    JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative.getY());
                    int width = (int) ((dimension.getWidth()) * buttonAnimationSizes[finalI]);
                    int height = (int) ((dimension.getHeight()) * buttonAnimationSizes[finalI]);
                    int x = absolutePosition.getX() - ((width - dimension.getWidth()) / 2);
                    int y = absolutePosition.getY() - ((height - dimension.getHeight()) / 2);
                    renderer.set(ShapeRenderer.ShapeType.Line);
                    renderer.setColor(Color.WHITE);
                    renderer.rect(x, y, width, height);
                }

                @Override
                public void paint(SpriteBatch batch) {

                }
            };
        }

         */
    }

    @Override
    public boolean handleClickEvent(int x, int y) {
        super.handleClickEvent(x, y);
        guiControl.plugActions(guiControl.getActionAssemblerHelper().wannaLockDoor(door));
        if (door != null) {
            EventManager.getInstance().fireEvent(new InfoObjectClickedEvent(door));
        }
        return true;
    }


    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void update(float time, int round) {
        this.door = player.getRoomInfo().getDoor(direction);
        if (door == null) return;
        if (!door.hasLock()) return;

        boolean locked = door.isLocked();
        boolean hasKey = this.player.hasKey(door);
        image = null;
        if (locked && hasKey) {
            image = GUIImageManager.SC_DOOR_CLOSED_KEY;
        } else if (!locked && hasKey) {
            image = GUIImageManager.SC_DOOR_OPEN_KEY;
        } else if (locked && !hasKey) {
            image = GUIImageManager.SC_DOOR_CLOSED_NOKEY;
        } else {
            // !locked && !hasKey
            image = GUIImageManager.SC_DOOR_OPEN_NOKEY;
        }
        textureRegion = Assets.instance.getAtlasRegion(image, Assets.instance.getGuiAtlas());

        absolutePositionX = parent.getPositionOnScreen().getX() + this.getPositionOnScreen().getX();
        absolutePositionY = parent.getPositionOnScreen().getY() + this.getPositionOnScreen().getY();
        dimensionX = this.getDimension().getWidth();
        dimensionY = this.getDimension().getHeight();
    }

    @Override
    public void paint(SpriteBatch batch, float deltaTime) {
        if (door == null) return;
        if (!door.hasLock()) return;

        batch.draw(textureRegion, absolutePositionX, absolutePositionY, dimensionX, dimensionY);

    }

    @Override
    public void paint(ShapeRenderer renderer) {
        /*
        super.paint(renderer);
        JDPoint parentPosition = parent.getPositionOnScreen();
        JDPoint posRelative = this.getPositionOnScreen();
        JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative.getY());
        JDDimension dimension = this.getDimension();
        Color borderColor = locked ? Color.RED : Color.GREEN;
        Color fillColor = hasKey ? Color.GREEN : Color.RED;
        LibgdxDrawUtils.fillRectangle(renderer, borderColor, absolutePosition, dimension);
        LibgdxDrawUtils.fillRectangle(renderer, fillColor, new JDPoint(absolutePosition.getX() + 2, absolutePosition.getY() + 2), new JDDimension(dimension.getWidth() - 4, dimension.getHeight() - 4));
    */
    }

}
