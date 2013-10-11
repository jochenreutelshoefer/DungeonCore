package gui.mainframe.dialog;

import item.*;
import item.interfaces.itemReceiver;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.border.*;

import figure.Figure;
import gui.mainframe.MainFrame;


/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ItemChoiceView
	extends JDialog
	implements ActionListener, ListSelectionListener {

	/**
	 * 
	 * @uml.property name="cp"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JDesktopPane cp = new JDesktopPane();

	/**
	 * 
	 * @uml.property name="p"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JPanel p = new JPanel();

	/**
	 * 
	 * @uml.property name="rec"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	itemReceiver rec;

	/**
	 * 
	 * @uml.property name="f"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	MainFrame f;


	boolean takeAway;

	/**
	 * 
	 * @uml.property name="scPane1"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JScrollPane scPane1;

	/**
	 * 
	 * @uml.property name="itemsH"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="item.Item"
	 */
	List<Item> itemsH;

	/**
	 * 
	 * @uml.property name="heroItemL"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JList heroItemL;

	/**
	 * 
	 * @uml.property name="listModel"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	DefaultListModel listModel = new DefaultListModel();

	/**
	 * 
	 * @uml.property name="cool"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JTextArea cool = new JTextArea(8, 25);

	Button ok = new Button("Ok");

	/**
	 * 
	 * @uml.property name="x"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	Figure x;

	public ItemChoiceView(
		MainFrame f,
		String title,
		itemReceiver rec,
		Figure x,
		boolean takeAway) {

		super(f, title, true);
		this.x = x;
		this.f = f;
		this.takeAway = takeAway;
		setContentPane(cp);
		GridLayout grid = new GridLayout(1, 1);
		cp.setLayout(grid);
		cp.setDesktopManager(new DefaultDesktopManager());
		this.rec = rec;
		cp.add(p);
		p.setLayout(new BorderLayout());
		p.add(cool, BorderLayout.NORTH);
		cool.setEditable(false);
		cool.setBorder(new EtchedBorder());
		ok.addActionListener(this);
		heroItemL = new JList(listModel);
		scPane1 =
			new JScrollPane(
				heroItemL,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scPane1.setPreferredSize(new Dimension(160, 70));

		p.add(scPane1, BorderLayout.CENTER);
		p.add(ok, BorderLayout.SOUTH);

		heroItemL.addListSelectionListener(this);
		heroItemL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		repaint();
		this.setSize(400, 400);
		this.setVisible(true);
		positionieren();
	}

	public void valueChanged(ListSelectionEvent lse) {
		JList list = (JList) lse.getSource();
		int i = list.getSelectedIndex();
		if (i != -1) {
			if (list == heroItemL) {
				Item u = ((Item) itemsH.get(i));
				if (u != null) {

					cool.setText(u.getText());
					if (takeAway) {
						x.giveAwayItem(u, null);
					}
				}
			} else
				cool.setText("da passt was nicht");
		}
	}

	public void actionPerformed(ActionEvent ae) {
		int i = heroItemL.getSelectedIndex();
		if (i != -1) {
			rec.tellItem((Item) itemsH.get(i));
			setVisible(false);
		} else {
			repaint();
		}
	}

	public void repaint() {
		itemsH = x.getAllItems();

		int b = itemsH.size();

		listModel.removeAllElements();

		for (int i = 0; i < b; i++) {
			Item ite = ((Item) itemsH.get(i));
			if (ite != null) {

				listModel.addElement(ite.toString());
			}
		}
		//Hier werden Stringrepräsentationen der Items in die listModels gefällt
	}
	public void positionieren() {
		Dimension dimension = new Dimension(getToolkit().getScreenSize());
		int screenWidth = (int) dimension.getWidth();
		int screenHeight = (int) dimension.getHeight();
		int width = this.getWidth();
		int height = this.getHeight();
		setLocation(
			(screenWidth / 2) - (width / 2),
			(screenHeight / 2) - (height / 2));
	}
}
