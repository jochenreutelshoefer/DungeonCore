package de.jdungeon.androidapp;

import figure.FigureInfo;
import game.InfoEntity;
import graphics.GraphicObject;
import graphics.JDGraphicObject;
import graphics.JDImageLocated;
import graphics.JDImageProxy;
import graphics.util.JDRectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import de.jdungeon.androidapp.animation.AnimationFrame;
import de.jdungeon.androidapp.animation.AnimationManager;
import de.jdungeon.androidapp.gui.GUIImageManager;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.RoomInfo;

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

					imageProxy = jdGraphicObject.getAWTImage().getImage();
					JDImageLocated image = jdGraphicObject.getAWTImage();
					Image im = (Image) image.getImage().getImage();

					// check for animation image
					Object clickedObject = jdGraphicObject.getClickedObject();
					String showText = null;
					JDPoint textOffset = null;
					if (clickedObject instanceof FigureInfo) {
						AnimationFrame frame = AnimationManager.getInstance()
								.getAnimationImage((FigureInfo) clickedObject);
						if (frame != null) {
							JDImageProxy<?> animationImage = frame.getImage();
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
						/*
						 * TODO: optimize (and restructure) use of paint object
						 */
						Paint p = new Paint();
						p.setColor(Color.RED);
						p.setTextSize(14);
						p.setFakeBoldText(true);
						g.drawString(showText, image.getPosX()
								- viewportPosition.getX() + textOffset.getX(),
								image.getPosY() - viewportPosition.getY()
										+ textOffset.getY(), p);
					}

				} else {
					if (imageProxy == null) {
						// problems with image loading??
						continue;
					}
					Image image = (Image) imageProxy.getImage();

					// check for animation image
					Object clickedObject = graphicObject.getClickedObject();
					if (clickedObject instanceof FigureInfo) {
						AnimationFrame animationFrame = AnimationManager
								.getInstance().getAnimationImage(
										(FigureInfo) clickedObject);
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
		Paint p = new Paint();
		p.setColor(Color.RED);
		g.drawString(roomInfo.getNumber().getX() + "/"
				+ roomInfo.getNumber().getY(), roomOffsetX + roomSize / 2,
				roomOffsetY + roomSize / 2, p);
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
				Image highlightImage = GUIImageManager.getImage(
						GUIImageManager.HIGHLIGHT, screen.getGame());

				int yellow = Color.YELLOW;

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

				g.drawLine(x1, y1, x2, y1, yellow);
				g.drawLine(x1, y1, x1, y2, yellow);
				g.drawLine(x1, y2, x2, y2, yellow);
				g.drawLine(x2, y1, x2, y2, yellow);
			}
		}
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
						roomOffsetY, new ArrayList<Object>());
		clearNulls(graphicObjectsForRoom);
		screen.getDrawnObjects()
				.put(roomInfo.getPoint(), graphicObjectsForRoom);
		DrawUtils.drawObjects(g, graphicObjectsForRoom,
				screen.getViewportPosition(), roomInfo, (int)screen.getRoomSize(),
				roomOffsetX, roomOffsetY, screen);
	}
}
