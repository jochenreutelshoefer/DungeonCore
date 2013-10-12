/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package gui.engine2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import dungeon.Door;

public class GraphicObject {

	protected Rectangle o;
	protected Rectangle clickRect;
	protected Color c;

	protected boolean flipped = false;
	protected Object clickedObject;

	protected boolean rim = false;
	protected Image image;

	public GraphicObject(
		Object ob,
		Rectangle o,
		Color c,
		Image i) {
		
		this.o = o;
		this.c = c;
		clickedObject = ob;
		image = i;
	}
	
	public GraphicObject(
			Object ob,
			Rectangle o,
			Color c,
			Image i,Rectangle clickRect) {
			
			this.o = o;
			this.c = c;
			this.clickRect = clickRect;
			clickedObject = ob;
			image = i;
		}

	public GraphicObject(Object ob,	Rectangle o,Color c,boolean rim,Image i) {
		this.rim = rim;
		this.o = o;
		this.c = c;
		clickedObject = ob;
		image = i;
		//this.game = game;
	}

	public GraphicObject(Object ob) {

		clickedObject = ob;
	}

	
	public Object getClickedObject() {
		return clickedObject;
	}

	public void draw(Graphics g) {
		g.setColor(c);
		g.drawRect(o.x, o.y, o.width, o.height);
	}

	public void fill(Graphics g) {
		if (image == null) {
			g.setColor(c);
			g.fillRect(o.x, o.y, o.width, o.height);
			if (rim
				|| ((clickedObject instanceof Door)
					&& (((Door) clickedObject).hasLock()))) {
				g.setColor(Color.black);
				g.drawRect(o.x, o.y, o.width, o.height);
			}
		} else {
//			if(flipped) {
//				drawFlipped(g,image);
//			}
//			else {
			g.setColor(Color.RED);
//			if(clickRect != null) {
//				g.drawRect(clickRect.x, clickRect.y, clickRect.width, clickRect.height);
//			}else if (o != null) {
//				g.drawRect(o.x, o.y, o.width, o.height);
//			} 
			
			
			
			if(o == null) { 
				System.out.println("o ist null!");
			}
			if(g == null ) {
				System.out.println("g ist null!");
			}
			g.drawImage(image,o.x,o.y,o.width,o.height,null);
		}
	}
	
	
	private void drawFlipped(Graphics g, Image im) {
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
	

	public boolean hasPoint(Point p) {
		if(clickRect != null) {
			return clickRect.contains(p);
		}
		if (o != null) {
			return o.contains(p);
		} else {
			return false;
		}
	}
	/**
	 * @return Returns the flipped.
	 */
	public boolean isFlipped() {
		return flipped;
	}
	/**
	 * @param flipped The flipped to set.
	 */
	public void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

}
