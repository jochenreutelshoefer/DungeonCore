package de.jdungeon.app;

import dungeon.RoomInfo;
import figure.FigureInfo;
import game.InfoEntity;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.JDGraphicObject;
import graphics.JDImageLocated;
import graphics.JDImageProxy;
import graphics.util.DrawingRectangle;

import java.util.List;

import animation.AnimationFrame;
import animation.AnimationManager;
import de.jdungeon.app.screen.GameScreen;
import de.jdungeon.game.Color;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import gui.Paragraphable;
import util.JDDimension;

public class DrawUtils {

	private static final boolean useTileDrawingCache = false;

	public static Image drawObjects(Graphics g,
									List<GraphicObject> graphicObjectsForRoom,
									JDPoint viewportPosition,
									int roomOffsetX, int roomOffsetY, GameScreen screen, GraphicObjectRenderer renderer, int roomSize, RoomInfo roomInfo) {

		// we init a tmp offscreen image for this room for caching and later re-use
		if(useTileDrawingCache) {
			g.setTempCanvas(roomOffsetX - viewportPosition.getX(), roomOffsetY - viewportPosition.getY(), roomSize, roomSize);
		}


		for (GraphicObject graphicObject : graphicObjectsForRoom) {
			if (graphicObject != null) {

				JDImageProxy<?> imageProxy = graphicObject.getImage();
				if (graphicObject instanceof JDGraphicObject) {
					JDGraphicObject jdGraphicObject = (JDGraphicObject) graphicObject;
					JDImageLocated jdImage = jdGraphicObject.getLocatedImage();
					Image imageResource = (Image) jdImage.getImage().getImage();

					// check for animation image
					Object clickedObject = jdGraphicObject.getClickableObject();
					String showText = null;
					JDPoint textOffset = null;
					if (clickedObject instanceof FigureInfo) {
						FigureInfo figureInfo = (FigureInfo) clickedObject;
						AnimationFrame frame = AnimationManager.getInstance().getAnimationImage(figureInfo, roomInfo);

						if (frame != null) {
							JDImageProxy<?> animationImage = frame.getImage();
							JDDimension figureInfoSize = renderer.getFigureInfoSize(figureInfo);
							// we override the image variable here
							JDImageLocated locatedImage = frame.getLocatedImage(roomOffsetX, roomOffsetY, figureInfoSize.getWidth(), figureInfoSize.getHeight());
							if(locatedImage != null) {
								// might be null if that room is not visible
								jdImage = locatedImage;
							}
							imageResource = (Image) animationImage.getImage();
							if (frame.getText() != null) {
								showText = frame.getText();
								textOffset = frame.getTextCoordinatesOffset();
							}
							AnimationManager.getInstance().framePool.free(frame);
						}
					}



					boolean drawHighlightBoxOnTopOfItem = clickedObject instanceof DoorInfo;
					if (!drawHighlightBoxOnTopOfItem) {
						drawHighlightBox(g, viewportPosition, screen,
								jdGraphicObject, clickedObject, roomOffsetX, roomOffsetY);
					}

					/*
					 * draw the image
					 */
					g.drawScaledImage(imageResource,
							jdImage.getX(roomOffsetX) - viewportPosition.getX(),
							jdImage.getY(roomOffsetY) - viewportPosition.getY(),
							jdImage.getWidth(), jdImage.getHeight(), 0, 0,
							imageResource.getWidth(), imageResource.getHeight());

					if (drawHighlightBoxOnTopOfItem) {
						drawHighlightBox(g, viewportPosition, screen,
								jdGraphicObject, clickedObject, roomOffsetX, roomOffsetY);
					}

					if (showText != null) {
						g.drawString(showText, jdImage.getX(roomOffsetX)
								- viewportPosition.getX() + textOffset.getX(),
								jdImage.getY(roomOffsetY) - viewportPosition.getY()
										+ textOffset.getY(), g.getSmallPaint());
					}

				} else {
					if (imageProxy == null) {
						// problems with image loading??
						continue;
					}
					Image image = (Image) imageProxy.getImage();

					// check for animation image
					Object clickedObject = graphicObject.getClickableObject();
					if (clickedObject instanceof FigureInfo) {
						AnimationFrame animationFrame = AnimationManager
								.getInstance().getAnimationImage(
										(FigureInfo) clickedObject, roomInfo);
						JDImageProxy<?> animationImage = animationFrame.getImage();
						if (animationImage != null) {
							image = (Image) animationImage.getImage();
						}
					}

					if (image != null) {
						DrawingRectangle destinationRectangle = graphicObject
								.getRectangle();
						/*
						 * draw highlight background if necessary
						 */
						if (clickedObject instanceof InfoEntity) {
							Paragraphable highlightedEntity = screen
									.getFocusManager().getWorldFocusObject();
							highlight(g, viewportPosition, clickedObject, highlightedEntity, destinationRectangle, roomOffsetX, roomOffsetY);
						}

						/*
						 * draw the image
						 */

						g.drawScaledImage(
								image,
								destinationRectangle.getX(roomOffsetX)
										- viewportPosition.getX(),
								destinationRectangle.getY(roomOffsetY)
										- viewportPosition.getY(),
								destinationRectangle.getWidth(),
								destinationRectangle.getHeight(), 0, 0,
								image.getWidth(), image.getHeight());
					}
				}
			}
		}

		// some hack to get just killed figures painted (dying animation and dead bodies)
		// this is necessary as they have already been removed from the model after their death instantly
		// and hence are not called for being painted any more
		// here we just flush their queued animations
		/*
		Collection<FigureInfo> deadFigures = AnimationManager.getInstance().getDeadFigures();
		for (FigureInfo deadFigure : deadFigures) {
			if(roomInfo.equals(deadFigure.getRoomInfo())) {
				AnimationFrame animationImage = AnimationManager.getInstance()
						.getAnimationImage(deadFigure, deadFigure.getRoomInfo());
				JDDimension figureInfoSize = renderer.getFigureInfoSize(deadFigure);
				int sizeX = figureInfoSize.getWidth();
				int sizeY = figureInfoSize.getHeight();
				JDPoint positionCoordModified = renderer.getPositionCoordModified(deadFigure.getPositionInRoomIndex());
				JDPoint relativeCoordinates = new JDPoint(
						positionCoordModified.getX(),
						positionCoordModified.getY());
				RelativeRectangle destinationRectangle = new RelativeRectangle(new JDPoint(relativeCoordinates.getX() - (sizeX / 2), relativeCoordinates.getY() - (sizeY / 2)), sizeX, sizeY);
				JDImageLocated locatedImage = new JDImageLocated(ImageManager.getImage(deadFigure, deadFigure.getLookDirection()), destinationRectangle);
				if(animationImage != null) {
					locatedImage = animationImage.getLocatedImage(roomOffsetX, roomOffsetY, figureInfoSize.getWidth(), figureInfoSize.getHeight(), renderer);
				}
				if(locatedImage != null) {

					JDImageProxy<?> imagePraxy = locatedImage.getImage();
					if(imagePraxy != null) {


						Image im = (Image) imagePraxy.getImage();
						g.drawScaledImage(im,
								destinationRectangle.getX(roomOffsetX)
										- viewportPosition.getX(),
								destinationRectangle.getY(roomOffsetY)
										- viewportPosition.getY(),
								destinationRectangle.getWidth(),
								destinationRectangle.getHeight(), 0, 0,
								im.getWidth(), im.getHeight());
					} else {
						Logger.getLogger(DrawUtils.class).error("ImageProxy of LocatedImage is null: "+locatedImage);
					}
				} else {
					Logger.getLogger(DrawUtils.class).error("Could not find image for dead figure: "+ deadFigure + " (Direction: "+deadFigure.getLookDirection()+")");
				}
			}

		}
		*/
		// we return the off screen image of this room for later reuse
		return g.getTempImage();
	}

	private static void drawHighlightBox(Graphics g, JDPoint viewportPosition,
										 GameScreen screen, JDGraphicObject jdGraphicObject,
										 Object clickedObject, int roomOffsetX, int roomOffsetY) {
		/*
		 * draw highlight background if necessary
		 */
		if (clickedObject instanceof InfoEntity) {
			Paragraphable highlightedEntity = screen.getFocusManager().getWorldFocusObject();
			DrawingRectangle rectangle = jdGraphicObject.getRectangle();
			highlight(g, viewportPosition, clickedObject, highlightedEntity, rectangle, roomOffsetX, roomOffsetY);
		}
	}

	private static void highlight(Graphics g, JDPoint viewportPosition, Object clickedObject, Paragraphable highlightedEntity, DrawingRectangle rectangle, int roomOffsetX, int roomOffsetY) {
		if (highlightedEntity != null) {
			if (clickedObject.equals(highlightedEntity)) {
				int x1 = rectangle.getX(roomOffsetX) - viewportPosition.getX();
				int y1 = rectangle.getY(roomOffsetY) - viewportPosition.getY();
				int x2 = x1 + rectangle.getWidth();
				int y2 = y1 + rectangle.getHeight();

				drawRectangle(g, Colors.YELLOW, x1, y1, x2, y2);
			}
		}
	}
	public static void drawRectangle(Graphics g, Color yellow, JDPoint point, JDDimension dimension) {
		g.drawRect(point.getX(), point.getY(), dimension.getWidth(), dimension.getHeight(), yellow);
	}

	public static void fillRectangle(Graphics g, Color yellow, JDPoint point, JDDimension dimension) {
		g.fillRect(point.getX(), point.getY(), dimension.getWidth(), dimension.getHeight(), yellow);
	}

	public static void drawRectangle(Graphics g, de.jdungeon.game.Color yellow, int x1, int y1, int x2, int y2) {
		// TODO: refactor
		g.drawLine(x1, y1, x2, y1, yellow);
		g.drawLine(x1, y1, x1, y2, yellow);
		g.drawLine(x1, y2, x2, y2, yellow);
		g.drawLine(x2, y1, x2, y2, yellow);
	}


}
