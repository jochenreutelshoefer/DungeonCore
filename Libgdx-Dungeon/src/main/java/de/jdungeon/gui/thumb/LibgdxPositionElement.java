package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.JDPoint;
import dungeon.Position;
import event.EventManager;
import figure.FigureInfo;
import game.RoomInfoEntity;
import util.JDDimension;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.event.InfoObjectClickedEvent;
import de.jdungeon.gui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */
public class LibgdxPositionElement extends LibgdxAnimatedSmartControlElement {

    private final RoomInfoEntity action;
    private final Color color;

    private final RoomInfoEntity clickableObject;
    private final ActionAssembler actionAssembler;
    private final int positionIndex;
    private final int ballWidth;
    private final int ballHeight;
    private final int ballOffsetX;
    private final int ballOffsetY;
    private final int x;
    private final int y;

    public LibgdxPositionElement(JDPoint position, JDDimension dimension, final LibgdxGUIElement parent, RoomInfoEntity action, final Color color, RoomInfoEntity clickableObject, ActionAssembler actionAssembler) {
        this(position, dimension, parent, action, color, clickableObject, actionAssembler, -1);
    }

    public LibgdxPositionElement(JDPoint position, JDDimension dimension, final LibgdxGUIElement parent, RoomInfoEntity action, final Color color, RoomInfoEntity clickableObject, ActionAssembler actionAssembler, int positionIndex) {
        super(position, dimension, parent);
        this.action = action;
        this.color = color;
        this.clickableObject = clickableObject;
        this.actionAssembler = actionAssembler;
        this.positionIndex = positionIndex;
        ballWidth = (int) (dimension.getWidth() / 1.5);
        ballHeight = (int) (dimension.getHeight() / 1.5);
        ballOffsetX = (dimension.getWidth() - ballWidth) / 2;
        ballOffsetY = (dimension.getHeight() - ballHeight) / 2;

        JDPoint parentPosition = parent.getPositionOnScreen();
        JDPoint posRelative = this.getPositionOnScreen();
        JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative
                .getY());
        x = absolutePosition.getX() + ballOffsetX;
        y = absolutePosition.getY() + ballOffsetY;

        final JDPoint positionOnScreen = getPositionOnScreen();

        // prepare highlight animation drawables
        if (action != null) {
            for (int i = 0; i < animationShapes.length; i++) {
                final int finalI = i;
                animationShapes[i] = new LibgdxDrawable() {
                    @Override
                    public void paint(ShapeRenderer renderer) {
                        JDPoint parentPosition = parent.getPositionOnScreen();
                        JDPoint absolutePosition = new JDPoint(parentPosition.getX() + positionOnScreen.getX(), parentPosition.getY() + positionOnScreen.getY());
                        int x = absolutePosition.getX() + ballOffsetX;
                        int y = absolutePosition.getY() + ballOffsetY;

                        int scaledSizeX = (int) (ballWidth * buttonAnimationSizes[finalI]);
                        int scaledSizeY = (int) (ballHeight * buttonAnimationSizes[finalI]);
                        renderer.setColor(color);
                        renderer.ellipse(x - ((scaledSizeX - ballWidth) / 2), y - ((scaledSizeY - ballHeight) / 2), scaledSizeX, scaledSizeY);
                    }

                    @Override
                    public void paint(SpriteBatch batch) {

                    }
                };
            }
        }
    }

    public RoomInfoEntity getClickableObject() {
        return clickableObject;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean handleClickEvent(int x, int y) {
        super.handleClickEvent(x, y);
        if (action != null) {
            if (action instanceof FigureInfo) {
                EventManager.getInstance().fireEvent(new InfoObjectClickedEvent(action));
                FigureInfo otherFigure = (FigureInfo) this.action;
                if (otherFigure.isHostile(this.actionAssembler.getFigure())) {
                    actionAssembler.plugActions(actionAssembler.getActionAssemblerHelper().wannaAttack(otherFigure));
                } else {
                    // do not allow to switch position with yourself !
                    if (!otherFigure.equals(actionAssembler.getFigure())) {
                        actionAssembler.plugActions(actionAssembler.getActionAssemblerHelper().wannaSwitchPositions(otherFigure));
                    }
                }
            }
        } else {
            if (this.positionIndex > -1) {
                actionAssembler.plugActions(actionAssembler.getActionAssemblerHelper()
                        .wannaStepToPosition(Position.Pos.fromValue(positionIndex)));
                if (clickableObject != null) {
                    EventManager.getInstance().fireEvent(new InfoObjectClickedEvent(clickableObject));
                }
            }
        }
        return true;
    }

    @Override
    public void paint(ShapeRenderer renderer) {
        super.paint(renderer);
        if (clickableObject instanceof FigureInfo) {
            renderer.setColor(color);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.ellipse(x, y, ballWidth, ballHeight);
        } else {
            renderer.setColor(color);
            renderer.set(ShapeRenderer.ShapeType.Line);
            renderer.ellipse(x, y, ballWidth, ballHeight);
        }
    }

    @Override
    public void paint(SpriteBatch batch) {

    }
}
