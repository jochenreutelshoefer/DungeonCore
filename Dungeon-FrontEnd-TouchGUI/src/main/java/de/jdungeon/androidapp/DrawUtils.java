package de.jdungeon.androidapp;

import figure.FigureInfo;
import game.InfoEntity;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.ImageManager;
import graphics.JDGraphicObject;
import graphics.JDImageLocated;
import graphics.JDImageProxy;
import graphics.util.JDRectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import animation.AnimationFrame;
import animation.AnimationManager;
import de.jdungeon.androidapp.gui.GUIImageManager;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.game.Color;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import org.apache.log4j.Logger;
import util.JDDimension;

public class DrawUtils {

	public static void drawImageNormalized(Graphics g, GraphicObject ob) {
		JDRectangle destinationRectOrig = ob.getRectangle();
	}


	private static void drawObjects(Graphics g,
			List<GraphicObject> graphicObjectsForRoom,
			JDPoint viewportPosition, RoomInfo roomInfo, int roomSize,
			int roomOffsetX, int roomOffsetY, GameScreen screen) {

		for (GraphicObject graphicObject : graphicObjectsForRoom) {
			if (graphicObject != null) {

				JDImageProxy<?> imageProxy = graphicObject.getImage();
				if (graphicObject instanceof JDGraphicObject) {
					JDGraphicObject jdGraphicObject = (JDGraphicObject) graphicObject;

					JDImageLocated image = jdGraphicObject.getAWTImage();
					Image im = (Image) image.getImage().getImage();

					// check for animation image
					Object clickedObject = jdGraphicObject.getClickableObject();
					String showText = null;
					JDPoint textOffset = null;
					if (clickedObject instanceof FigureInfo) {
						FigureInfo figureInfo = (FigureInfo) clickedObject;
						AnimationFrame frame = AnimationManager.getInstance()
								.getAnimationImage(figureInfo, roomInfo);

						if (frame != null) {
							JDImageProxy<?> animationImage = frame.getImage();
							JDDimension figureInfoSize = GraphicObjectRenderer.getFigureInfoSize(figureInfo, roomSize);
							// we override the image variable here
							JDImageLocated locatedImage = frame.getLocatedImage(roomOffsetX, roomOffsetY, figureInfoSize.getWidth(), figureInfoSize.getHeight(), roomSize);
							if(locatedImage != null) {
								// might be null if that room is not visible
								image = locatedImage;
							}
							im = (Image) animationImage.getImage();
							if (frame.getText() != null) {
								showText = frame.getText();
								textOffset = frame.getTextCoordinatesOffset();
							}
						}
					}

					boolean drawHighlightBoxOnTopOfItem = clickedObject instanceof DoorInfo;
					if (!drawHighlightBoxOnTopOfItem) {
						drawHighlightBox(g, viewportPosition, screen,
								jdGraphicObject, clickedObject);
					}

					/*
					 * draw the image
					 */
					g.drawScaledImage(im,
							image.getPosX() - viewportPosition.getX(),
							image.getPosY() - viewportPosition.getY(),
							image.getWidth(), image.getHeight(), 0, 0,
							im.getWidth(), im.getHeight());

					if (drawHighlightBoxOnTopOfItem) {
						drawHighlightBox(g, viewportPosition, screen,
								jdGraphicObject, clickedObject);
					}

					if (showText != null) {
						g.drawString(showText, image.getPosX()
								- viewportPosition.getX() + textOffset.getX(),
								image.getPosY() - viewportPosition.getY()
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
						JDImageProxy<?> animationImage = animationFrame
								.getImage();
						if (animationImage != null) {
							image = (Image) animationImage.getImage();
						}
					}

					if (image != null) {
						JDRectangle destinationRectangle = graphicObject
								.getRectangle();
						/*
						 * draw highlight background if necessary
						 */
						if (clickedObject instanceof InfoEntity) {
							InfoEntity highlightedEntity = screen
									.getHighlightedEntity();
							highlight(g, viewportPosition, screen, clickedObject, highlightedEntity, destinationRectangle);
						}

						/*
						 * draw the image
						 */

						g.drawScaledImage(
								image,
								destinationRectangle.getX()
										- viewportPosition.getX(),
								destinationRectangle.getY()
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
		Collection<FigureInfo> deadFigures = AnimationManager.getInstance().getDeadFigures();
		for (FigureInfo deadFigure : deadFigures) {
			if(roomInfo.equals(deadFigure.getRoomInfo())) {
				AnimationFrame animationImage = AnimationManager.getInstance()
						.getAnimationImage(deadFigure, deadFigure.getRoomInfo());
				JDDimension figureInfoSize = GraphicObjectRenderer.getFigureInfoSize(deadFigure, roomSize);
				int sizeX = figureInfoSize.getWidth();
				int sizeY = figureInfoSize.getHeight();
				GraphicObjectRenderer renderer = new GraphicObjectRenderer(roomSize);
				JDPoint positionCoordModified = renderer.getPositionCoordModified(deadFigure.getPositionInRoomIndex());
				JDPoint p = new JDPoint(
						positionCoordModified.getX() + roomOffsetX,
						positionCoordModified.getY() + roomOffsetY);
				JDRectangle destinationRectangle = new JDRectangle(new JDPoint(p.getX() - (sizeX / 2), p.getY() - (sizeY / 2)), sizeX, sizeY);
				JDImageLocated locatedImage = new JDImageLocated(ImageManager.getImage(deadFigure, deadFigure.getLookDirection()), destinationRectangle);
				if(animationImage != null) {
					locatedImage = animationImage.getLocatedImage(roomOffsetX, roomOffsetY, figureInfoSize.getWidth(), figureInfoSize.getHeight(), roomSize);
				}
				if(locatedImage != null) {

					JDImageProxy<?> imagePraxy = locatedImage.getImage();
					if(imagePraxy != null) {


						Image im = (Image) imagePraxy.getImage();
						g.drawScaledImage(im,
								destinationRectangle.getX()
										- viewportPosition.getX(),
								destinationRectangle.getY()
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
		/*
		Paint p = new Paint();
		p.setColor(Color.RED);
		g.drawString(roomInfo.getNumber().getX() + "/"
				+ roomInfo.getNumber().getY(), roomOffsetX + roomSize / 2,
				roomOffsetY + roomSize / 2, p);
				*/
	}

	private static void drawHighlightBox(Graphics g, JDPoint viewportPosition,
			GameScreen screen, JDGraphicObject jdGraphicObject,
			Object clickedObject) {
		/*
		 * draw highlight background if necessary
		 */
		if (clickedObject instanceof InfoEntity) {
			InfoEntity highlightedEntity = screen.getHighlightedEntity();
			JDRectangle rectangle = jdGraphicObject.getRectangle();
			highlight(g, viewportPosition, screen, clickedObject, highlightedEntity, rectangle);
		}
	}

	private static void highlight(Graphics g, JDPoint viewportPosition, GameScreen screen, Object clickedObject, InfoEntity highlightedEntity, JDRectangle rectangle) {
		if (highlightedEntity != null) {
			if (clickedObject.equals(highlightedEntity)) {
				Image highlightImage = screen.getGuiImageManager().getImage(
						GUIImageManager.HIGHLIGHT);


				int x1 = rectangle.getX() - viewportPosition.getX();
				int y1 = rectangle.getY() - viewportPosition.getY();
				int x2 = x1 + rectangle.getWidth();
				int y2 = y1 + rectangle.getHeight();

				// int x1 = image.getPosX()
				// - viewportPosition.getX();
				// int y1 = image.getPosY()
				// - viewportPosition.getY();
				// int x2 = x1
				// + image.getWidth();
				// int y2 = y1
				// + image.getHeight();

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

	private static void clearNulls(List<?> l) {
		Iterator<?> iterator = l.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			if (o == null) {
				iterator.remove();
			}
		}
	}

	public static void paintSingleRoom(GameScreen screen, Graphics g,
			RoomInfo roomInfo, int roomOffsetX, int roomOffsetY) {
		List<GraphicObject> graphicObjectsForRoom = screen.getDungeonRenderer()
				.createGraphicObjectsForRoom(roomInfo, null, roomOffsetX,
						roomOffsetY, new ArrayList<>());
		clearNulls(graphicObjectsForRoom);
		screen.getDrawnObjects()
				.put(roomInfo.getPoint(), graphicObjectsForRoom);
		DrawUtils.drawObjects(g, graphicObjectsForRoom,
				screen.getViewportPosition(), roomInfo, (int)screen.getRoomSize(),
				roomOffsetX, roomOffsetY, screen);
	}
}
