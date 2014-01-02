package de.jdungeon.androidapp;

import figure.FigureInfo;
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
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import dungeon.JDPoint;
import dungeon.RoomInfo;

public class DrawUtils {

	public static void drawImageNormalized(Graphics g, GraphicObject ob) {
		JDRectangle destinationRectOrig = ob.getRectangle();
	}

	private static void drawObjects(Graphics g,
			List<GraphicObject> graphicObjectsForRoom,
			JDPoint viewportPosition, RoomInfo roomInfo, int roomSize,
			int roomOffsetX, int roomOffsetY) {
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

					g.drawScaledImage(im,
							image.getPosX() - viewportPosition.getX(),
							image.getPosY() - viewportPosition.getY(),
							image.getWidth(), image.getHeight(), 0, 0,
							im.getWidth(), im.getHeight());

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
				screen.getViewportPosition(), roomInfo, screen.getRoomSize(),
				roomOffsetX, roomOffsetY);
	}
}
