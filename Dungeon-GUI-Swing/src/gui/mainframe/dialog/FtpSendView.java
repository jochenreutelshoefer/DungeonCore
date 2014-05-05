/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package gui.mainframe.dialog;
import gui.init.StartView;
import gui.mainframe.MainFrame;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.applet.*;
public class FtpSendView extends JDialog implements ActionListener, KeyListener{
	Container cp;

	
	JTextField name;

	
	JButton start = new JButton("Abschicken");

	
	JLabel label = new JLabel(
		"Das Spielergebnis wird nun an die High-Score-Liste gesendet.\n Kommentar zum Spiel?");

	
	boolean appletRunning;

	Applet applet;

	
	MainFrame frame;

	public FtpSendView(MainFrame m) {
		//super(m.appletRunning,m.applet) ;
		super(m,"Eintrag in High-Score-Liste",true);
		cp = getContentPane();
		frame = m;
		this.appletRunning = m.getApplet() != null;
		applet = m.getApplet();
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3,1));
		cp.add(panel);
		//name = new JTextField("godmode",20);
		name = new JTextField("",20);
		label.setPreferredSize(new Dimension(200,40));
		name.setPreferredSize(new Dimension(200,20));
		start.setPreferredSize(new Dimension(30,20));
		start.setSize(new Dimension(30,20));
		start.setMinimumSize(new Dimension(30,20));
		start.setMaximumSize(new Dimension(30,20));
		panel.add(label,BorderLayout.NORTH);
		panel.add(name, BorderLayout.CENTER);
		panel.add(start,BorderLayout.SOUTH);
		start.addActionListener(this);
		name.addActionListener(this);
		name.setSelectionStart(0);
		name.setSelectionEnd(name.getText().length());
		
		
		this.setResizable(false);
		this.setSize(500,140);
		//pack();
		positionieren();	
		this.setVisible(true);	
		
		
	}
	
	public String getText() {
		return name.getText();	
	}
	
	public void actionPerformed(ActionEvent ae) {
		//frame.sendScoreData(name.getText());
		this.setVisible(false);
		
	}

	/**
	 * 
	 * @uml.property name="appletRunning"
	 */
	public boolean isAppletRunning() {
		return appletRunning;
	}

	
	public void keyPressed(KeyEvent ke) {
		
	}
	
	public void keyReleased(KeyEvent ke) {
		
	}
	
	public void keyTyped(KeyEvent ke) {
		//System.out.println("keyTyped: "+ke.toString());
		int o = ke.getKeyCode();
		if(o == KeyEvent.VK_ENTER) {
			//System.out.println("Enter gedr�ckt!");
			actionPerformed(null);	
		}
		
	}


	/**
	 * Die Methode positionieren sorgt dafuer, dass "this" genau in der
	 * Mitte des Bildschirms befindet
	 */
	public void positionieren()
	{
		Dimension dimension = new Dimension (getToolkit ().getScreenSize ());
		int screenWidth = (int)dimension.getWidth();
		int screenHeight = (int)dimension.getHeight();
		int width = this.getWidth();
		int height = this.getHeight();
		setLocation ((screenWidth/2)-(width/2), (screenHeight/2)-(height/2));
	}
	
	public static void main(String[]args) {
		//StartView start = new StartView(false,null);
	}	

}
