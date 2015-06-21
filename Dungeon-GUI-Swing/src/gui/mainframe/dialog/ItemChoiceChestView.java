package gui.mainframe.dialog;

import item.Item;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultDesktopManager;
import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dungeon.Chest;
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
public class ItemChoiceChestView
	extends JDialog
	implements ActionListener, ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @uml.property name="cp"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final JDesktopPane cp = new JDesktopPane();

	/**
	 * 
	 * @uml.property name="p"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final JPanel p = new JPanel();

	/**
	 * 
	 * @uml.property name="rec"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final Figure rec;

	/**
	 * 
	 * @uml.property name="f"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final MainFrame f;


	private final boolean takeAway;

	/**
	 * 
	 * @uml.property name="scPane1"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final JScrollPane scPane1;

	/**
	 * 
	 * @uml.property name="itemsH"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="item.Item"
	 */
	private List<Item> itemsH;

	/**
	 * 
	 * @uml.property name="heroItemL"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final JList heroItemL;

	/**
	 * 
	 * @uml.property name="listModel"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private final DefaultListModel listModel = new DefaultListModel();

	/**
	 * 
	 * @uml.property name="cool"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final JTextArea cool = new JTextArea(8, 25);

	private final Button ok = new Button("Ok");

	/**
	 * 
	 * @uml.property name="x"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	Chest x;

	public ItemChoiceChestView(
		MainFrame f,
		String title,
		Figure rec,
		Chest x,
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

	@Override
	public void valueChanged(ListSelectionEvent lse) {
		JList list = (JList) lse.getSource();
		int i = list.getSelectedIndex();
		if (i != -1) {
			if (list == heroItemL) {
				Item u = (itemsH.get(i));
				cool.setText(u.getText());
				//if (takeAway) {
				//	x.giveAwayItem(u, null);
				//}

			} else
				cool.setText("da passt was nicht");

			//repaint();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		int i = heroItemL.getSelectedIndex();
		if (i != -1) {
			if (rec.takeItem(itemsH.get(i))) {
				x.removeItem(itemsH.get(i))	;
			}
			
			setVisible(false);
		} else {
			repaint();
		}
	}

	@Override
	public void repaint() {
		//System.out.println("repaint()");
		itemsH = x.getItems();

		int b = itemsH.size();

		listModel.removeAllElements();

		for (int i = 0; i < b; i++) {
			Item ite = (itemsH.get(i));
			//System.out.println(ite.toString());
			listModel.addElement(ite.toString());
		}
		//Hier werden Stringrepr�sentationen der Items in die listModels gef�llt
	}
	
	public void positionieren()
	{
		Dimension dimension = new Dimension (getToolkit ().getScreenSize ());
		int screenWidth = (int)dimension.getWidth();
		int screenHeight = (int)dimension.getHeight();
		int width = this.getWidth();
		int height = this.getHeight();
		setLocation ((screenWidth/2)-(width/2), (screenHeight/2)-(height/2));
	}
}

