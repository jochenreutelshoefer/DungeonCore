package gui.mainframe.dialog;
////import JDGuimainFrame;

import gui.mainframe.MainFrame;
import item.Item;

import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.*;

import shrine.Trader;
//import javax.swing.border.*;
import java.util.*;
import java.util.List;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Trader_window
	extends JDialog
	implements ActionListener, ListSelectionListener {

	/**
	 * 
	 * @uml.property name="trader"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	Trader trader;

	/**
	 * 
	 * @uml.property name="f"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	MainFrame f;

	/**
	 * 
	 * @uml.property name="cp1"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JDesktopPane cp1 = new JDesktopPane();

	//Panel cp = new Panel();
	List heroItems;
	List traderItems;
	List give = new LinkedList();
	List want = new LinkedList();

	/**
	 * 
	 * @uml.property name="listModel_hero"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="item.Item"
	 */
	DefaultListModel listModel_hero = new DefaultListModel();

	/**
	 * 
	 * @uml.property name="listModel_give"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="item.Item"
	 */
	DefaultListModel listModel_give = new DefaultListModel();

	/**
	 * 
	 * @uml.property name="listModel_want"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="item.Item"
	 */
	DefaultListModel listModel_want = new DefaultListModel();

	/**
	 * 
	 * @uml.property name="listModel_trader"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="item.Item"
	 */
	DefaultListModel listModel_trader = new DefaultListModel();

	/**
	 * 
	 * @uml.property name="heroL"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JList heroL;

	/**
	 * 
	 * @uml.property name="giveL"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JList giveL;

	/**
	 * 
	 * @uml.property name="wantL"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JList wantL;

	/**
	 * 
	 * @uml.property name="traderL"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JList traderL;

	/**
	 * 
	 * @uml.property name="scPaneHero"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JScrollPane scPaneHero;

	/**
	 * 
	 * @uml.property name="scPaneGive"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JScrollPane scPaneGive;

	/**
	 * 
	 * @uml.property name="scPaneWant"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JScrollPane scPaneWant;

	/**
	 * 
	 * @uml.property name="scPaneTrader"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JScrollPane scPaneTrader;

	/**
	 * 
	 * @uml.property name="toGive"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JButton toGive = new JButton("bieten >");

	/**
	 * 
	 * @uml.property name="toHero"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JButton toHero = new JButton("< behalten");

	/**
	 * 
	 * @uml.property name="toWant"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JButton toWant = new JButton("< kaufen");

	/**
	 * 
	 * @uml.property name="toTrader"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JButton toTrader = new JButton("nicht kaufen >");

	/**
	 * 
	 * @uml.property name="ok"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JButton ok = new JButton("Tauschen");

	/**
	 * 
	 * @uml.property name="cancel"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JButton cancel = new JButton("Tschüß");

	/**
	 * 
	 * @uml.property name="answer"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel answer = new JLabel();

	public Trader_window(MainFrame f, Trader s) {
		super(f, "Tauschhändler", true);
		//super(f);
		trader = s;
		this.f = f;
		//this.setUndecorated(true);
		
		heroItems = f.getGui().getFigure().getAllItems();
		
		traderItems = trader.getItems();
	
		//this.add(cp);
		JPanel cp = new JPanel();
		cp1.add(cp);
		//cp1.setBorder(BorderFactory.createLineBorder(Color.black,3));
		cp.setBorder(BorderFactory.createLineBorder(Color.gray,1));
		
		toGive.addActionListener(this);
		toGive.setPreferredSize(new Dimension(80, 15));
		toHero.addActionListener(this);
		toHero.setPreferredSize(new Dimension(80, 15));
		toWant.addActionListener(this);
		toWant.setPreferredSize(new Dimension(80, 15));
		toTrader.addActionListener(this);
		toTrader.setPreferredSize(new Dimension(80, 15));
		
		ok.addActionListener(this);
		cancel.addActionListener(this);

		heroL = new JList(listModel_hero);
		heroL.addListSelectionListener(this);
		heroL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scPaneHero =
			new JScrollPane(
				heroL,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scPaneHero.setPreferredSize(new Dimension(200, 100));

		giveL = new JList(listModel_give);
		giveL.addListSelectionListener(this);
		giveL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scPaneGive =
			new JScrollPane(
				giveL,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scPaneGive.setPreferredSize(new Dimension(200, 100));
		scPaneGive.setMaximumSize(new Dimension(200, 100));

		wantL = new JList(listModel_want);
		wantL.addListSelectionListener(this);
		wantL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scPaneWant =
			new JScrollPane(
				wantL,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scPaneWant.setPreferredSize(new Dimension(200, 100));
		scPaneWant.setMaximumSize(new Dimension(200, 100));

		traderL = new JList(listModel_trader);
		traderL.addListSelectionListener(this);
		traderL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scPaneTrader =
			new JScrollPane(
				traderL,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scPaneTrader.setPreferredSize(new Dimension(200, 100));

		for (int i = 0; i < heroItems.size(); i++) {
			listModel_hero.addElement(heroItems.get(i));
		}
		for (int i = 0; i < traderItems.size(); i++) {
			listModel_trader.addElement(traderItems.get(i));
		}

		cp.setLayout(new BorderLayout());
		JPanel west = new JPanel();
		west.setLayout(new BorderLayout());
		JPanel center = new JPanel();
		JPanel east = new JPanel();
		east.setLayout(new BorderLayout());
		JPanel south = new JPanel();
		cp.add(west, BorderLayout.WEST);
		cp.add(center, BorderLayout.CENTER);
		cp.add(east, BorderLayout.EAST);
		cp.add(south, BorderLayout.SOUTH);

		JPanel toWantP = new JPanel();
		toWantP.add(toWant);
		JPanel toGiveP = new JPanel();
		toGiveP.add(toGive);
		JPanel toHeroP = new JPanel();
		toHeroP.add(toHero);
		JPanel toTraderP = new JPanel();
		toTraderP.add(toTrader);

		west.add(scPaneHero, BorderLayout.CENTER);
		west.add(toGiveP, BorderLayout.SOUTH);
		east.add(scPaneTrader, BorderLayout.CENTER);
		east.add(toWantP, BorderLayout.SOUTH);

		center.setLayout(new GridLayout(1, 2));
		JPanel centerWest = new JPanel();
		centerWest.setLayout(new BorderLayout());
		JPanel centerEast = new JPanel();
		centerEast.setLayout(new BorderLayout());

		centerWest.add(scPaneGive, BorderLayout.CENTER);
		centerWest.add(toHeroP, BorderLayout.SOUTH);

		centerEast.add(scPaneWant, BorderLayout.CENTER);
		centerEast.add(toTraderP, BorderLayout.SOUTH);

		center.add(centerWest);
		center.add(centerEast);
		
		JPanel answerP = new JPanel();
		GridLayout grid = new GridLayout(1,1);
		grid.setVgap(10);
		grid.setHgap(10);
		answerP.setLayout(grid);
		answerP.add(answer);
		
		south.add(answerP);
		south.add(ok);
		south.add(cancel);
		

		//answerP.setBorder(new EtchedBorder());
		//answer.setEditable(false);
		answer.setText(trader.getBeginText());

		setContentPane(cp);
		this.setLocation(10,330);
		this.setSize(1000, 200);
		this.setVisible(true);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == toHero) {
			int i = giveL.getSelectedIndex();
			Item it = (Item) (listModel_give.remove(i));
			listModel_hero.addElement(it);

		}
		if (o == toGive) {
			int i = heroL.getSelectedIndex();
			Item it = (Item) (listModel_hero.remove(i));
			listModel_give.addElement(it);
		}
		if (o == toWant) {
			int i = traderL.getSelectedIndex();
			Item it = (Item) (listModel_trader.remove(i));
			listModel_want.addElement(it);
		}
		if (o == toTrader) {
			int i = wantL.getSelectedIndex();
			Item it = (Item) (listModel_want.remove(i));
			listModel_trader.addElement(it);
		}
		if (o == cancel) {
			this.dispose();
		}
		if (o == ok) {
			if(trader.getOk()) {
				trader.makeTrade(give,want);
				this.dispose();	
			}
		} else {

			if (listModel_give.size() > 0 && listModel_want.size() > 0) {
				want = new LinkedList();
				give = new LinkedList();
				for (int i = 0; i < listModel_want.size(); i++) {
					want.add(listModel_want.getElementAt(i));
				}
				for (int i = 0; i < listModel_give.size(); i++) {
					give.add(listModel_give.getElementAt(i));
				}
				answer.setText(trader.setTrade(give, want));
			} else {
				if (listModel_give.size() != 0) {
					answer.setText(trader.getWhatText());
				} else if (listModel_want.size() != 0) {
					answer.setText(trader.getWantText());

				} else {
					answer.setText(trader.getEmptyText());
				}

			}
		}
	}

	public void valueChanged(ListSelectionEvent le) {
		JList list = (JList) le.getSource();
		int i = list.getSelectedIndex();
		if (i != -1) {
			if (list == heroL) {
				f.getText().setText(
					((Item) listModel_hero.get(i)).getParagraphs());
			} else if (list == giveL) {
				f.getText().setText(
					((Item) listModel_give.get(i)).getParagraphs());
			
			} else if (list == wantL) {
				f.getText().setText(
					((Item) listModel_want.get(i)).getParagraphs());
			
			} else if (list == traderL) {
				f.getText().setText(
					((Item) listModel_trader.get(i)).getParagraphs());
			}

		}
	}

}
