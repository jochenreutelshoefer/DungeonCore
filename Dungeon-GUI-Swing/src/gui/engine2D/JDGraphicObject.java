/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package gui.engine2D;


import java.awt.*;
public class JDGraphicObject extends GraphicObject {

	
	JDImageAWT image;

	
	boolean toPaint = true;

	
	public JDGraphicObject(JDImageAWT i,Object ob, Rectangle o, Color c) {
		super(ob,o,c,null);
		image = i;
		
	}
	
	public JDGraphicObject(JDImageAWT i,Object ob, Rectangle o, Color c, Rectangle clickRect) {
		super(ob,o,c,null,clickRect);
		image = i;
		
	}
	public JDGraphicObject(JDImageAWT i,Object ob, Rectangle o, Color c,boolean paint) {
		super(ob,o,c,null);
		toPaint = paint;
		image = i;
		
	}
	
	public JDGraphicObject(JDImageAWT i,Object ob, Rectangle o, Color c,boolean paint, Rectangle clickRect) {
		super(ob,o,c,null,clickRect);
		toPaint = paint;
		image = i;
		
	}


//	public jd_graphicObject(jd_image i, Object ob, Rectangle o, Color c, boolean rim) {
//		super(ob,o,c,false,null);
//		image = i;
//		
//	}
public void setToPaint(boolean b) {
	toPaint = b;
}

	
	public void fill(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
//		if(image == null) {
//		g2D.setColor(c);
//		g2D.fillRect(o.x,o.y,o.width,o.height);
//		if(rim || ((clickedObject instanceof door)&& (((door)clickedObject).hasLock()))) {
		
//		}
//		}
//		else {
			if(image.getImage() == null || !toPaint) {
				//System.out.println("Image is null!! Objekt : "+clickedObject.getClass().toString());
			}
			//else {
				
			//Component c = gui.getMainFrame().getSpielfeld().getSpielfeldBild();
			//System.out.println("drawing image: "+image.getImage().toString());
				g2D.drawImage(image.getImage(),image.getPosX(),image.getPosY(),image.getWidth(),image.getHeight(),null);
			//}
				if(this.clickedObject != null) {
					//System.out.println("male :" +this.clickedObject.toString());
				}
				if(o != null) {
					//System.out.println("draw rect: "+o.toString());
//					g2D.setColor(Color.black);
//					g2D.drawRect(o.x,o.y,o.width,o.height	);
				}	
		}
//	}

}
