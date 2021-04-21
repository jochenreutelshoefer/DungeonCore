package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.asset.Assets;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.gui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class LibgdxMoveElement extends LibgdxAnimatedSmartControlElement {

    private final RouteInstruction.Direction direction;
    private final FigureInfo figure;
    private final ActionAssembler guiControl;
    private float rotation;

    public LibgdxMoveElement(JDPoint position, JDDimension dimension, LibgdxGUIElement parent, RouteInstruction.Direction direction, FigureInfo figure, ActionAssembler guiControl) {
        super(position, dimension, parent);
        this.direction = direction;
        this.figure = figure;
        this.guiControl = guiControl;

        JDDimension iconDimension = new JDDimension((int) (dimension.getWidth() * 0.8), (int) (dimension.getHeight() * 0.8));
        JDPoint[] triangle;
        if (direction == RouteInstruction.Direction.West) {
            rotation = 270f;
            for (int i = 0; i < buttonAnimationSizes.length; i++) {
                animationShapes[i] = new LibgdxTriangle(getTriangleWest(position, iconDimension, buttonAnimationSizes[i]), parent);
            }
        }
        if (direction == RouteInstruction.Direction.East) {
            rotation = 90f;
            for (int i = 0; i < buttonAnimationSizes.length; i++) {
                animationShapes[i] = new LibgdxTriangle(getTriangleEast(position, iconDimension, buttonAnimationSizes[i]), parent);
            }
        }
        if (direction == RouteInstruction.Direction.North) {
            rotation = 0f;
            for (int i = 0; i < buttonAnimationSizes.length; i++) {
                animationShapes[i] = new LibgdxTriangle(getTriangleNorth(position, iconDimension, buttonAnimationSizes[i]), parent);
            }
        }
        if (direction == RouteInstruction.Direction.South) {
            rotation = 180f;
            for (int i = 0; i < buttonAnimationSizes.length; i++) {
                animationShapes[i] = new LibgdxTriangle(getTriangleSouth(position, iconDimension, buttonAnimationSizes[i]), parent);
            }
        }

        int parentX = parent.getPositionOnScreen().getX();
        int parentY = parent.getPositionOnScreen().getY();
        /*
        x0 = parentX + triangle[0].getX();
        y0 = parentY + triangle[0].getY();
        x1 = parentX + triangle[1].getX();
        y1 = parentY + triangle[1].getY();
        x2 = parentX + triangle[2].getX();
        y2 = parentY + triangle[2].getY();

         */
    }

    private JDPoint[] getTriangleSouth(JDPoint position, JDDimension clickAreaDimension, double drawScale) {
        int sizeX = clickAreaDimension.getWidth();
        int sizeY = clickAreaDimension.getHeight();

        int centerX = position.getX() + getDimension().getWidth() / 2;
        int centerY = position.getY() + getDimension().getHeight() / 2;

        JDPoint[] result = new JDPoint[3];
        result[0] = new JDPoint(centerX, centerY + ((sizeY / 3) * drawScale)); // peak to bottom
        double y = centerY - ((sizeY / 3) * drawScale);
        result[1] = new JDPoint(centerX + ((sizeX / 2) * drawScale), y); // upper right
        result[2] = new JDPoint(centerX - ((sizeX / 2) * drawScale), y); // upper left
        return result;
    }

    private JDPoint[] getTriangleNorth(JDPoint position, JDDimension clickAreaDimension, double drawScale) {
        int sizeX = clickAreaDimension.getWidth();
        int sizeY = clickAreaDimension.getHeight();

        int centerX = position.getX() + getDimension().getWidth() / 2;
        int centerY = position.getY() + getDimension().getHeight() / 2;

        JDPoint[] result = new JDPoint[3];
        result[0] = new JDPoint(centerX, centerY - ((sizeY / 3) * drawScale)); // peak to top
        double y = centerY + ((sizeY / 3) * drawScale);
        result[1] = new JDPoint(centerX + ((sizeX / 2) * drawScale), y); // lower right
        result[2] = new JDPoint(centerX - ((sizeX / 2) * drawScale), y); // lower left
        return result;
    }

    private JDPoint[] getTriangleEast(JDPoint position, JDDimension clickAreaDimension, double drawScale) {
        int sizeX = clickAreaDimension.getWidth();
        int sizeY = clickAreaDimension.getHeight();

        int centerX = position.getX() + getDimension().getWidth() / 2;
        int centerY = position.getY() + getDimension().getHeight() / 2;

        JDPoint[] result = new JDPoint[3];
        result[0] = new JDPoint(centerX + ((sizeX / 3) * drawScale), centerY); // peak to right
        double x = centerX - ((sizeX / 3) * drawScale);
        result[1] = new JDPoint(x, centerY - ((sizeY / 2) * drawScale)); // upper
        result[2] = new JDPoint(x, centerY + ((sizeY / 2) * drawScale)); // lower
        return result;
    }

    private JDPoint[] getTriangleWest(JDPoint position, JDDimension triangleDimension, double drawScale) {
        int sizeX = triangleDimension.getWidth();
        int sizeY = triangleDimension.getHeight();

        int centerX = position.getX() + getDimension().getWidth() / 2;
        int centerY = position.getY() + getDimension().getHeight() / 2;

        JDPoint[] result = new JDPoint[3];
        result[0] = new JDPoint(centerX - ((sizeX / 3) * drawScale), centerY); // peak to left
        double x = centerX + ((sizeX / 3) * drawScale);
        result[1] = new JDPoint(x, centerY - ((sizeY / 2) * drawScale)); // upper
        result[2] = new JDPoint(x, centerY + ((sizeY / 2) * drawScale)); // lower
        return result;
    }

    @Override
    public boolean handleClickEvent(int x, int y) {
        super.handleClickEvent(x, y);
        final Boolean fightRunning = figure.getRoomInfo().fightRunning();
        if (fightRunning != null && fightRunning) {
            guiControl.plugActions(guiControl.getActionAssemblerHelper().wannaFlee());
        } else {
            guiControl.plugActions(guiControl.getActionAssemblerHelper().wannaWalk(direction.getValue()));
        }
        return true;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void paint(ShapeRenderer renderer) {
        super.paint(renderer);

        /*
        renderer.setColor(Color.RED);
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.rect(this.getX(), this.getY(), this.getDimension().getWidth(), this.getDimension().getHeight());
        renderer.setColor(Color.ORANGE);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.triangle(x0, y0, x1, y1, x2, y2);

         */
    }

    @Override
    public void paint(SpriteBatch batch, float deltaTime) {
        TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(GUIImageManager.SC_MOVE, Assets.instance.getGuiAtlas());
        //batch.draw(atlasRegion, this.getX(), this.getY(), this.iconDimension.getWidth(), this.iconDimension.getHeight());
        batch.draw(atlasRegion, this.getX(), this.getY(), this.getDimension().getWidth() / 2, this.getDimension().getHeight() / 2,  this.getDimension().getWidth(), this.getDimension().getHeight(), 1.0f, 1.0f, rotation);
    }
}
