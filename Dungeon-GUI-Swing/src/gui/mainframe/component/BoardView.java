package gui.mainframe.component;


import gui.JDGUISwing;
import gui.JDJLabel;
import gui.JDJPanel;
import gui.JDJViewport;
import gui.engine2D.GraphBoard;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import dungeon.JDPoint;

/**
 * @author Duke1
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class BoardView extends JDJPanel implements MouseListener,
		AdjustmentListener {

	private final JScrollPane scrollPane;

	private final GraphBoard drawingArea;

	private final JDJViewport v;

	private Point viewPortPoint;

	private final JLabel scale = new JDJLabel("+-");

	private final JScrollBar bar1;

	private final JScrollBar bar2;

	public BoardView(Applet a, JDGUISwing gui) {
		super(gui);
		scale.addMouseListener(this);
		
		scrollPane = new JScrollPane();

		scrollPane.setBackground(JDJPanel.bgColor);
		bar1 = scrollPane.getHorizontalScrollBar();
		bar1.addAdjustmentListener(this);
		
		if(bar1.getComponentCount() > 0) {
			Component c = bar1.getComponent(0);
			c.setBackground(JDJPanel.bgColor);
		}
		if(bar1.getComponentCount() > 1) {
			Component c2 = bar1.getComponent(1);
			c2.setBackground(JDJPanel.bgColor);
		}
		bar1.setBackground(JDJPanel.bgColor);
		bar1.setOpaque(false);

		bar2 = scrollPane.getVerticalScrollBar();
		bar2.addAdjustmentListener(this);
		bar2.setBackground(JDJPanel.bgColor);
		if(bar2.getComponentCount() > 0) {
			Component c3 = bar2.getComponent(0);
			c3.setBackground(JDJPanel.bgColor);
			
		}
		if(bar2.getComponentCount() > 1) {
		Component c4 = bar2.getComponent(1);
		c4.setBackground(JDJPanel.bgColor);
		}

		bar2.setForeground(JDJPanel.bgColor);

		drawingArea = new GraphBoard(a,gui);
		v = new JDJViewport();
		v.setView(drawingArea);
		scrollPane.setViewport(v);
		scrollPane.setPreferredSize(new Dimension(860, 550));
		scrollPane.setMinimumSize(new Dimension(200, 150));
		scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, scale);

		this.add(scrollPane);

	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		gui.stopAllAnimation();
	}

	public void paintIt() {
		Graphics g = drawingArea.getGraphics();
		drawingArea.paint(g);
	}
	
	public void zoomIntoRoom() {
		int targetSizeZoomed = 210;
		resetViewPoint();
		zoomToSize(targetSizeZoomed);
	}
	
	public void zoomOutOfRoom() {
		zoomToSize(GraphBoard.DEFAULT_ROOM_SIZE);
	}

	private void zoomToSize(int targetSize) {
		int currentSize = drawingArea.getRoomSize();
		int diff = targetSize - currentSize;
		int zoomSteps = 8;
		for(int i = 0; i < zoomSteps; i++) {
			int increment = diff/zoomSteps;
			drawingArea.incSize(increment);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		drawingArea.setRoomSize(targetSize);
	}



	public void resetViewPoint() {
		JDPoint p = gui.getFigure().getRoomNumber();
		int roomNumberX = p.getX();
		int roomNumberY = p.getY();
		int offset = drawingArea.getOffset();
		int viewportPositionX = v.getViewPosition().x;
		int viewportPositionY = v.getViewPosition().y;
		int currentRoomSize = drawingArea.getRoomSize();
		int roomCoordinateX = roomNumberX * currentRoomSize + offset;
		int diffX = roomCoordinateX - viewportPositionX;
		int roomCoordinateY = roomNumberY * currentRoomSize + offset;
		int diffY = roomCoordinateY - viewportPositionY;
		int threshold = currentRoomSize * 1;
		Dimension d  = this.getSize();
		if(diffX < threshold || diffY < threshold*1.2 || diffX > d.width - threshold || diffY > d.height - threshold) {
			v.setViewPosition(new Point((currentRoomSize * roomNumberX) - 200 + offset, (currentRoomSize * roomNumberY) - 220
				+ offset));
		}
	}

	public void setViewPoint(int xcoord, int ycoord) {
		this.viewPortPoint = new Point(xcoord, ycoord);
		gui.stopAllAnimation();
		v.setViewPosition(new Point(xcoord, ycoord));
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if (!me.isMetaDown()) {
			drawingArea.incSize();
		} else {
			drawingArea.decSize();
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {

	}

	@Override
	public void mouseEntered(MouseEvent me) {

	}

	@Override
	public void mouseReleased(MouseEvent me) {

	}

	@Override
	public void mouseExited(MouseEvent me) {

	}




	public JDJViewport getViewport() {
		return v;
	}
	
	boolean spellMeta = false;
	public void setSpellMetaDown() {
		spellMeta = true;
	}

	


	public GraphBoard getSpielfeldBild() {
		return drawingArea;
	}

	
	public Point getViewPortPoint() {
		return viewPortPoint;
	}
}