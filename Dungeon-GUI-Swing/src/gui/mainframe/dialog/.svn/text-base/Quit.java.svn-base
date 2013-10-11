package gui.mainframe.dialog;
import javax.swing.*;
//import java.awt.*;
import java.awt.event.*;

public class Quit extends JFrame implements ActionListener {

	/**
	 * 
	 * @uml.property name="button1"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JButton button1 = new JButton("Ja");

	/**
	 * 
	 * @uml.property name="button2"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JButton button2 = new JButton("Nein");

	/**
	 * 
	 * @uml.property name="label1"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel label1 = new JLabel();

    public Quit(){
	
	super("Einfache Ereignisse");
	JPanel cp = new JPanel();
	cp.add(label1);
	cp.add(button1);
	cp.add(button2);

	
	button1.addActionListener(this);
	button2.addActionListener(this);
	label1.setText("Moechten sie wirklich beenden?");
	
	setContentPane(cp);

	WindowListener listener = new WindowAdapter(){
		public void windowClosing(WindowEvent we)
		{
		  
		}
	    };

	this.addWindowListener(listener);
	this.setSize(300,100);
	this.setVisible(false);
    }
    
    public void actionPerformed(ActionEvent ae) {
	
	Object quelle = ae.getSource();
	if(quelle == button1)
	    {System.exit(0);}
	if(quelle == button2)
	    {this.setVisible(false);}

    }

 
    
}
