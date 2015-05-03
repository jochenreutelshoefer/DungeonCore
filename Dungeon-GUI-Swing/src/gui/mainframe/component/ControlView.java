package gui.mainframe.component;
//import JDGuimainFrame;
//import JDGuiJDJPanel;

import gui.JDGUISwing;
import gui.JDJPanel;
import gui.mainframe.MainFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import dungeon.util.RouteInstruction;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */


public class ControlView extends JDJPanel implements ActionListener {

	
	public JButton round = new JButton("...");

	public JButton scoutNorth = new JButton("Sp�hen");

	
	public JButton scoutEast = new JButton("Sp�hen");

	
	public JButton scoutSouth = new JButton("Sp�hen");

	
	public JButton scoutWest = new JButton("Sp�hen");

	
	public JButton north = new JButton("Norden");

	
	public JButton east = new JButton("Osten");

	
	public JButton west = new JButton("Westen");

	
	public JButton south = new JButton("S�den");
	
	JPanel inner = new JPanel();

	MainFrame main;

	
	public ControlView(MainFrame m, JDGUISwing gui) {
		super(gui);
		main = m;
		this.setLayout(new BorderLayout());
		inner.setLayout(new BorderLayout());
		inner.add(round,BorderLayout.CENTER);
		inner.add(scoutNorth, BorderLayout.NORTH);
		inner.add(scoutEast, BorderLayout.EAST);
		inner.add(scoutWest, BorderLayout.WEST);
		inner.add(scoutSouth, BorderLayout.SOUTH);
		this.add(inner,BorderLayout.CENTER);
		this.add(north,BorderLayout.NORTH);
		this.add(south,BorderLayout.SOUTH);
		this.add(east,BorderLayout.EAST);
		this.add(west,BorderLayout.WEST);
		
		round.addActionListener(this);
		scoutNorth.addActionListener(this);
		scoutSouth.addActionListener(this);
		scoutWest.addActionListener(this);
		scoutEast.addActionListener(this);
		north.addActionListener(this);
		east.addActionListener(this);
		west.addActionListener(this);
		south.addActionListener(this);
		
		round.setOpaque(false);
		scoutNorth.setOpaque(false);
		scoutSouth.setOpaque(false);
		scoutWest.setOpaque(false);
		scoutEast.setOpaque(false);
		north.setOpaque(false);
		east.setOpaque(false);
		west.setOpaque(false);
		south.setOpaque(false);
		
		
		round.setBackground(JDJPanel.bgColor);
		scoutNorth.setBackground(JDJPanel.bgColor);
		scoutSouth.setBackground(JDJPanel.bgColor);
		scoutWest.setBackground(JDJPanel.bgColor);
		scoutEast.setBackground(JDJPanel.bgColor);
		north.setBackground(JDJPanel.bgColor);
		east.setBackground(JDJPanel.bgColor);
		west.setBackground(JDJPanel.bgColor);
		south.setBackground(JDJPanel.bgColor);
		
		this.setPreferredSize(new Dimension(330,180));
		
		
		
		
		
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object quelle = e.getSource();
		////System.out.println("ActionPerformed! "+e.toString());
		if (quelle == north) {
			gui.getActionAssembler().wannaWalk(RouteInstruction.NORTH);
			
		}
		if (quelle == east) {

			gui.getActionAssembler().wannaWalk(RouteInstruction.EAST);

		}

		if (quelle == south) {

			gui.getActionAssembler().wannaWalk(RouteInstruction.SOUTH);

				}
		if (quelle == west) {

			gui.getActionAssembler().wannaWalk(RouteInstruction.WEST);

		}
		if (quelle == round) {
			gui.getActionAssembler().wannaEndRound();
		}
		if (quelle == scoutSouth) {
			gui.getActionAssembler().wannaScout(RouteInstruction.SOUTH);
		}
		if (quelle == scoutEast) {
			gui.getActionAssembler().wannaScout(RouteInstruction.EAST);
		}
		if (quelle == scoutNorth) {
			gui.getActionAssembler().wannaScout(RouteInstruction.NORTH);
		}
		if (quelle == scoutWest) {
			gui.getActionAssembler().wannaScout(RouteInstruction.WEST);
		}
		
	}

	
//	public void setGame(Game game) {
//		this.game = game;
//	}

	
	
	
	public void scout(int direction) {
		gui.getActionAssembler().wannaScout(direction);
	}

}
