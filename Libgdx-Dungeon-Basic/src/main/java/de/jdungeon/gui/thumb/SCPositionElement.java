package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.event.InfoObjectClickedEvent;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.asset.Assets;
import de.jdungeon.dungeon.*;
import de.jdungeon.event.EventManager;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.StepAction;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.gui.LibgdxGUIElement;
import de.jdungeon.util.JDDimension;

import java.util.List;

public class SCPositionElement extends LibgdxSubGUIElement {

    private final ActionAssembler actionAssembler;
    private final int positionIndex;
    private final int ballHeight;
    private final int ballWidth;
    private final int x;
    private final int y;
    private FigureInfo figure;
    private FigureInfo player;
    private boolean isCurrentlyPossible;
    private StepAction action;

    SCPositionElement(JDPoint position, JDDimension dimension, final LibgdxGUIElement parent, ActionAssembler actionAssembler, int positionIndex) {
        super(position, dimension, parent);
        //this.action = target;
        //this.color = color;
        this.actionAssembler = actionAssembler;
        this.positionIndex = positionIndex;
        ballWidth = (int) (dimension.getWidth() / 1.5);
        ballHeight = (int) (dimension.getHeight() / 1.5);
        int ballOffsetX = (dimension.getWidth() - ballWidth) / 2;
        int ballOffsetY = (dimension.getHeight() - ballHeight) / 2;

        player = this.actionAssembler.getFigure();
        action = new StepAction(player, positionIndex);

        JDPoint parentPosition = parent.getPositionOnScreen();
        JDPoint posRelative = this.getPositionOnScreen();
        JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative.getY());
        x = absolutePosition.getX() + ballOffsetX;
        y = absolutePosition.getY() + ballOffsetY;

        final JDPoint positionOnScreen = getPositionOnScreen();
        /*
        // prepare highlight animation drawables
        if (target != null) {
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
         */
    }

    public RoomInfoEntity getClickableObject() {
        RoomInfo roomInfo = this.actionAssembler.getFigure().getRoomInfo();
        PositionInRoomInfo positionInRoom = roomInfo.getPositionInRoom(this.positionIndex);
        FigureInfo figure = positionInRoom.getFigure();
        if (figure != null) {
            return figure;
        }
        return null;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void update(float deltaTime, int round) {
        RoomInfo roomInfo = this.actionAssembler.getFigure().getRoomInfo();
        PositionInRoomInfo positionInRoom = roomInfo.getPositionInRoom(this.positionIndex);
        figure = positionInRoom.getFigure();
        isCurrentlyPossible = action.handle(false, round) == ActionResult.POSSIBLE;
    }

    @Override
    public boolean handleClickEvent(int x, int y) {
        //super.handleClickEvent(x, y);

        if (figure != null) {
            EventManager.getInstance().fireEvent(new InfoObjectClickedEvent(figure));

            if (figure.isHostile(player)) {
                actionAssembler.plugActions(actionAssembler.getActionAssemblerHelper().wannaAttack(figure));
            } else {
                // do not allow to switch position with yourself !
                if (!figure.equals(actionAssembler.getFigure())) {
                    actionAssembler.plugActions(actionAssembler.getActionAssemblerHelper().wannaSwitchPositions(figure));
                }
            }
        } else {
            if (this.positionIndex > -1) {
                List<Action> actions = actionAssembler.getActionAssemblerHelper()
                        .wannaStepToPosition(Position.Pos.fromValue(positionIndex));
                actionAssembler.plugActions(actions);
            }
        }
        return true;
    }

    @Override
    public void paint(ShapeRenderer renderer) {

    }

    @Override
    public void paint(SpriteBatch batch, float deltaTime) {
        String image;
        int width = ballWidth;
        int height = ballHeight;
        int posX = x;
        int posY = y;
            if(figure == null) {
                if(isCurrentlyPossible) {
                    image = GUIImageManager.SC_STEP_ELEMENT_POSSIBLE;
                } else {
                    image = GUIImageManager.SC_CIRCLE_WHITE;
                    // can not do something here, only render dots
                    width = 2;
                    height = 2;
                    posX = x + ballWidth/2 - width/2;
                    posY = y + ballHeight/2 - height/2;
                }
            } else if(figure.equals(player)) {
                image = GUIImageManager.SC_STEP_ELEMENT_SELF;
            } else if(player.isHostile(figure)) {
                image = GUIImageManager.SC_STEP_ELEMENT_ENEMY;

            } else {
                // should be a friend
                image = GUIImageManager.SC_STEP_ELEMENT_FRIEND;
            }
        TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(image, Assets.instance.getGuiAtlas());
        batch.draw(atlasRegion, posX, posY, width, height);
    }
}
