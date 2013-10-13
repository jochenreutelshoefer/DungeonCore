package gui.engine2D;

import graphics.JDImageProxy;
import graphics.JDRectangle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import dungeon.Door;

public class DrawUtils {

	public static void drawFlipped(Graphics g, Image im, Rectangle o) {
		BufferedImage mBufferedImage = new BufferedImage(im.getWidth(null), im
				.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = mBufferedImage.createGraphics();
		g2.drawImage(im, null, null);

		BufferedImage mBufferedImage2 = new BufferedImage(im.getWidth(null), im
				.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		AffineTransform trans = new AffineTransform();
		trans.setToScale(-1, 1);
		trans.translate((-1)*im.getWidth(null), 0);
		BufferedImageOp op = new AffineTransformOp(trans,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		Image resIm = op.filter(mBufferedImage, mBufferedImage2);
		//Component c = gui.getMainFrame().getSpielfeld().getSpielfeldBild();
		g.drawImage(
				resIm,
				o.x,
				o.y,
				o.width,
				o.height,null
				);

		
		
	}
	
	public static void fillGraphicObject(GraphicObject o, Graphics g) {
		if(o instanceof JDGraphicObject) {
			fillJDGraphicObject((JDGraphicObject)o,g);
			return;
		}
		JDImageProxy image = o.getImage();
		JDRectangle rect = o.getRectangle();
		Object clickedObject = o.getClickedObject();
		Color c = o.getColor();
		if (image == null) {
			g.setColor(c);
			g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
			if (o.getRim()
				|| ((clickedObject instanceof Door)
					&& (((Door) clickedObject).hasLock()))) {
				g.setColor(Color.black);
				g.drawRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
			}
		} else {
			g.setColor(Color.RED);
			g.drawImage((Image)image.getImage(),rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight(),null);
		}
	}

	private static void fillJDGraphicObject(JDGraphicObject o, Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		JDImageAWT image = o.getAWTImage();
		
		g2D.drawImage((Image)image.getImage().getImage(), image.getPosX(), image.getPosY(),
				image.getWidth(), image.getHeight(), null);
		
	}

}
