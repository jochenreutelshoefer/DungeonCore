package gui.mainframe.component;

//import JDGuiJDJButton;
//import JDGuiJDJPanel;
//import JDGuiJDJRadioButton;

import figure.FigureInfo;
import figure.action.EquipmentChangeAction;
import figure.hero.HeroInfo;
import game.JDEnv;
import gui.JDJButton;
import gui.JDJPanel;
import gui.JDJRadioButton;
import gui.MyJDGui;
import gui.Paragraph;
import gui.engine2D.DrawUtils;
import item.ItemInfo;
import item.equipment.EquipmentItemInfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import control.ActionAssembler;

/**
 * @author Duke1
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class InventoryView extends JDJPanel implements ActionListener,
		ItemListener, MouseListener, ListSelectionListener {

	// Game game;

	// Hero h;

	JPanel panelR1;

	JPanel panelR2;

	JPanel panelR3;

	JPanel panelR4;

	JPanel panelR5;

	JPanel panelR6;

	private int weaponIndex;

	private int armorIndex;

	private int helmetIndex;

	private int shieldIndex;

	JDJRadioButton Bshield[] = new JDJRadioButton[3];

	JDJRadioButton Barmor[] = new JDJRadioButton[3];

	ButtonGroup bgshield = new ButtonGroup();

	Box shieldBox = Box.createVerticalBox();

	ButtonGroup bgarmor = new ButtonGroup();

	Box armorBox = Box.createVerticalBox();

	JDJRadioButton[] Bhelmet = new JDJRadioButton[3];

	ButtonGroup bghelmet = new ButtonGroup();

	Box helmetBox = Box.createVerticalBox();

	JDJRadioButton[] Bweapon = new JDJRadioButton[3];

	ButtonGroup bgweapon = new ButtonGroup();

	Box weaponBox = Box.createVerticalBox();

	// JList 2mal f�r Gegenst�nde im Raum und im Rucksack
	// eigentlich enthalten die listmodels die Strings
	// es werden die itemlisten zu Strings auf die listmodels abgebildet
	DefaultListModel listModel2 = new DefaultListModel();

	DefaultListModel listModel = new DefaultListModel();

	JList roomItemL;

	JList heroItemL;

	JScrollPane scPane1;

	JScrollPane scPane2;

	List items; // enthält die echte ItemListe des Raumes

	List<ItemInfo> itemsH; // enthält die echte ItemListe des Helden

	JDJButton take = new JDJButton("Aufnehmen");

	JDJButton use = new JDJButton("Benutzen");

	JDJButton layDownWeapon = new JDJButton("Ablegen");

	JDJButton layDownArmor = new JDJButton("Ablegen");

	JDJButton layDownHelmet = new JDJButton("Ablegen");

	JDJButton layDownItem = new JDJButton("Ablegen");

	JDJButton layDownShield = new JDJButton("Ablegen");

	ActionAssembler control;

	private void fillRadioButtons(JDJRadioButton[] butts) {
		for (int i = 0; i < butts.length; i++) {
			butts[i] = new JDJRadioButton();
		}

	}

	private void addMouseListener(JDJRadioButton[] butts) {
		for (int i = 0; i < butts.length; i++) {
			butts[i].addMouseListener(this);
		}
	}

	private void addToBox(JDJRadioButton[] butts, Box box) {
		for (int i = 0; i < butts.length; i++) {
			box.add(butts[i]);
		}
	}

	private void addItemListener(JDJRadioButton[] butts) {
		for (int i = 0; i < butts.length; i++) {
			butts[i].addItemListener(this);
		}
	}

	private void addToButtonGroup(JDJRadioButton[] butts, ButtonGroup g) {
		for (int i = 0; i < butts.length; i++) {
			g.add(butts[i]);
		}
	}

	String takeItem = "-";
	String layDown = "-";

	public InventoryView(MyJDGui gui) {
		super(gui);
		takeItem = JDEnv.getResourceBundle().getString("gui_take");
		layDown = JDEnv.getResourceBundle().getString("gui_layDown");
		this.control = gui.getControl();
		take = new JDJButton(takeItem);

		use = new JDJButton(JDEnv.getResourceBundle().getString("gui_use"));

		layDownWeapon = new JDJButton(layDown);

		layDownArmor = new JDJButton(layDown);

		layDownHelmet = new JDJButton(layDown);

		layDownItem = new JDJButton(layDown);

		layDownShield = new JDJButton(layDown);

		fillRadioButtons(Bshield);
		fillRadioButtons(Barmor);
		fillRadioButtons(Bhelmet);
		fillRadioButtons(Bweapon);

		panelR1 = new JDJPanel(gui);

		panelR2 = new JDJPanel(gui);

		panelR3 = new JDJPanel(gui);

		panelR4 = new JDJPanel(gui);

		panelR5 = new JDJPanel(gui);

		panelR6 = new JDJPanel(gui);

		take.addActionListener(this);
		use.addActionListener(this);
		layDownWeapon.addActionListener(this);
		layDownHelmet.addActionListener(this);
		layDownItem.addActionListener(this);
		layDownArmor.addActionListener(this);
		layDownShield.addActionListener(this);

		setBorder(new TitledBorder(JDEnv.getResourceBundle().getString(
				"gui_inventory")));
		setLayout(new GridLayout(3, 2));

		add(panelR1);
		add(panelR2);
		add(panelR3);
		add(panelR4);
		add(panelR5);
		add(panelR6);

		panelR1.setBorder(new TitledBorder(JDEnv.getResourceBundle().getString(
				"gui_shield")));
		panelR2.setBorder(new TitledBorder(JDEnv.getResourceBundle().getString(
				"gui_armor")));
		panelR3.setBorder(new TitledBorder(JDEnv.getResourceBundle().getString(
				"gui_helmet")));
		panelR4.setBorder(new TitledBorder(JDEnv.getResourceBundle().getString(
				"gui_weapon")));
		panelR5.setBorder(new TitledBorder(JDEnv.getResourceBundle().getString(
				"gui_items")));
		panelR6.setBorder(new TitledBorder(JDEnv.getResourceBundle().getString(
				"gui_room")));

		// panelR1.setPreferredSize(new Dimension(160,160));
		// panelR2.setPreferredSize(new Dimension(160,160));
		// panelR3.setPreferredSize(new Dimension(160,160));
		// panelR4.setPreferredSize(new Dimension(160,160));
		// panelR5.setPreferredSize(new Dimension(160,160));
		// panelR6.setPreferredSize(new Dimension(160,160));

		panelR1.setLayout(new BorderLayout());
		panelR2.setLayout(new BorderLayout());
		panelR3.setLayout(new BorderLayout());
		panelR4.setLayout(new BorderLayout());
		panelR5.setLayout(new BorderLayout());
		panelR6.setLayout(new BorderLayout());

		addItemListener(Bshield);

		addMouseListener(Bshield);

		addToButtonGroup(Bshield, bgshield);

		addToBox(Bshield, shieldBox);

		panelR1.add(shieldBox, BorderLayout.CENTER);
		panelR1.add(layDownShield, BorderLayout.SOUTH);

		addItemListener(Barmor);

		addMouseListener(Barmor);

		addToButtonGroup(Barmor, bgarmor);

		addToBox(Barmor, armorBox);

		panelR2.add(armorBox, BorderLayout.CENTER);
		panelR2.add(layDownArmor, BorderLayout.SOUTH);

		addItemListener(Bhelmet);

		addMouseListener(Bhelmet);
		addToButtonGroup(Bhelmet, bghelmet);

		addToBox(Bhelmet, helmetBox);

		panelR3.add(helmetBox, BorderLayout.CENTER);
		panelR3.add(layDownHelmet, BorderLayout.SOUTH);

		addItemListener(Bweapon);

		addMouseListener(Bweapon);

		addToButtonGroup(Bweapon, bgweapon);

		addToBox(Bweapon, weaponBox);

		panelR4.add(weaponBox, BorderLayout.CENTER);
		panelR4.add(layDownWeapon, BorderLayout.SOUTH);

		heroItemL = new JList(listModel2);
		heroItemL.setBackground(new Color(220, 220, 220));
		heroItemL.addListSelectionListener(this);
		heroItemL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		roomItemL = new JList(listModel);
		roomItemL.setBackground(new Color(220, 220, 220));
		roomItemL.addListSelectionListener(this);
		roomItemL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scPane1 = new JScrollPane(roomItemL,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scPane1.setMaximumSize(new Dimension(10, 10));

		JScrollBar bar1 = scPane1.getVerticalScrollBar();
		bar1.setBackground(JDJPanel.bgColor);
		if (bar1.getComponentCount() > 1) {
			Component c = bar1.getComponent(0);
			c.setBackground(JDJPanel.bgColor);
			Component c2 = bar1.getComponent(1);
			c2.setBackground(JDJPanel.bgColor);
		}
		scPane2 = new JScrollPane(heroItemL,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollBar bar2 = scPane2.getVerticalScrollBar();

		if (bar2.getComponentCount() > 1) {
			bar2.setBackground(JDJPanel.bgColor);
			Component c3 = bar2.getComponent(0);
			c3.setBackground(JDJPanel.bgColor);
			Component c4 = bar2.getComponent(1);
			c4.setBackground(JDJPanel.bgColor);
		}
		scPane1.setPreferredSize(new Dimension(160, 70));
		scPane2.setPreferredSize(new Dimension(160, 70));

		panelR6.add(scPane1, BorderLayout.CENTER);

		JPanel knopfPanel = new JDJPanel(gui);

		knopfPanel.setLayout(new BoxLayout(knopfPanel, BoxLayout.Y_AXIS));
		panelR5.add(scPane2, BorderLayout.CENTER);
		panelR5.add(knopfPanel, BorderLayout.SOUTH);

		knopfPanel.add(layDownItem);
		knopfPanel.add(use);
		panelR6.add(take, BorderLayout.SOUTH);

	}

	public void updateView() {
		// if (game != null) {

		FigureInfo info = gui.getFigure();
		HeroInfo heroInfo = null;
		if (info instanceof HeroInfo) {
			heroInfo = ((HeroInfo) info);

		}

		if (heroInfo != null) {
			weaponIndex = heroInfo.getWeaponIndex();
			armorIndex = heroInfo.getArmorIndex();
			helmetIndex = heroInfo.getHelmetIndex();
			shieldIndex = heroInfo.getShieldIndex();
		}

		Bweapon[weaponIndex].setSelected(true);

		Barmor[armorIndex].setSelected(true);

		Bhelmet[helmetIndex].setSelected(true);

		Bshield[shieldIndex].setSelected(true);

		if (info != null) {
			items = Arrays.asList(info.getRoomItems());

			itemsH = info.getFigureItemList();

			int a = items.size();
			int b = itemsH.size();

			listModel2.removeAllElements();
			listModel.removeAllElements();

			for (int i = 0; i < a; i++) {
				String it = ((ItemInfo) items.get(i)).toString();
				listModel.addElement(it);
			}
			// Hier werden Stringrepr�sentationen der Items in die listModels
			// gef�llt
			for (int i = 0; i < b; i++) {
				ItemInfo it = itemsH.get(i);
				if (it != null) {
					listModel2.addElement(it.toString());
				}
			}

			setButtonTexts(Bshield, heroInfo.getShields());
			setButtonTexts(Bweapon, heroInfo.getWeapons());
			setButtonTexts(Barmor, heroInfo.getArmors());
			setButtonTexts(Bhelmet, heroInfo.getHelmets());

		}

	}

	@Override
	public void mouseClicked(MouseEvent me) {
		Object quelle = me.getSource();
		Paragraph[] p = null;

		FigureInfo info = gui.getFigure();
		HeroInfo heroInfo = null;
		if (info instanceof HeroInfo) {
			heroInfo = (HeroInfo) info;
		}
		if (heroInfo != null) {
			for (int i = 0; i < Bshield.length; i++) {
				if (quelle == Bshield[i]) {
					gui.getControl().wannaSwitchEquipmentItem(
							EquipmentChangeAction.EQUIPMENT_TYPE_SHIELD, i);
				}
			}
			for (int i = 0; i < Barmor.length; i++) {
				if (quelle == Barmor[i]) {
					gui.getControl().wannaSwitchEquipmentItem(
							EquipmentChangeAction.EQUIPMENT_TYPE_ARMOR, i);

				}
			}

			for (int i = 0; i < Bweapon.length; i++) {
				if (quelle == Bweapon[i]) {
					gui.getControl().wannaSwitchEquipmentItem(
							EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON, i);

				}
			}
			for (int i = 0; i < Bhelmet.length; i++) {
				if (quelle == Bhelmet[i]) {
					gui.getControl().wannaSwitchEquipmentItem(
							EquipmentChangeAction.EQUIPMENT_TYPE_HELMET, i);

				}
			}

		}
	}

	private void setButtonTexts(JDJRadioButton[] butts, EquipmentItemInfo[] its) {
		for (int i = 0; i < its.length; i++) {
			setText(butts[i], its[i]);
		}
	}

	private void setText(JDJRadioButton butt, EquipmentItemInfo it) {
		if (it == null) {
			butt.setText(JDEnv.getResourceBundle().getString("empty"));
			butt.setForeground(Color.BLACK);
		} else {
			butt.setForeground(DrawUtils.convertColor(it.getStatusColor()));
			butt.setText(getSizedString(it.toString()));
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {

	}

	@Override
	public void mouseEntered(MouseEvent me) {
		Object quelle = me.getSource();
		Paragraph[] p = null;
		ItemInfo it = null;

		FigureInfo info = gui.getFigure();
		HeroInfo heroInfo = null;
		if (info instanceof HeroInfo) {
			heroInfo = ((HeroInfo) info);
		}

		for (int i = 0; i < Bshield.length; i++) {
			if (quelle == Bshield[i]) {
				it = heroInfo.getShield(i);
				if (it != null) {
					p = it.getParagraphs();
				}
			}
		}
		for (int i = 0; i < Barmor.length; i++) {
			if (quelle == Barmor[i]) {
				it = heroInfo.getArmor(i);
				if (it != null) {
					p = it.getParagraphs();
				}
			}
		}

		for (int i = 0; i < Bhelmet.length; i++) {
			if (quelle == Bhelmet[i]) {
				it = heroInfo.getHelmet(i);
				if (it != null) {
					p = it.getParagraphs();
				}
			}
		}

		for (int i = 0; i < Bweapon.length; i++) {
			if (quelle == Bweapon[i]) {
				it = heroInfo.getWeapon(i);
				if (it != null) {
					p = it.getParagraphs();
				}
			}
		}

		gui.getMainFrame().getText().setText(p);
	}

	@Override
	public void mouseReleased(MouseEvent me) {

	}

	@Override
	public void mouseExited(MouseEvent me) {
		gui.getMainFrame().getText().resetText();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object quelle = ae.getSource();

		if (gui.getMainFrame().isNoControl()) {
			quelle = null;
		}
		if (quelle == layDownWeapon) {

			gui.getControl().wannaLayDownEquipmentItem(
					EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON);

		}
		if (quelle == layDownArmor) {
			gui.getControl().wannaLayDownEquipmentItem(
					EquipmentChangeAction.EQUIPMENT_TYPE_ARMOR);

		}
		if (quelle == layDownHelmet) {
			gui.getControl().wannaLayDownEquipmentItem(
					EquipmentChangeAction.EQUIPMENT_TYPE_HELMET);

		}
		if (quelle == layDownShield) {
			gui.getControl().wannaLayDownEquipmentItem(
					EquipmentChangeAction.EQUIPMENT_TYPE_SHIELD);
		}
		if (quelle == layDownItem) {

			int i = heroItemL.getSelectedIndex();
			if (i != -1) {
				gui.getControl().wannaLayDownItem(i);
			}

		}
		if (quelle == use) {
			int i = heroItemL.getSelectedIndex();
			if (i != -1) {
				ItemInfo info = itemsH.get(i);
				gui.getControl().useButtonClicked(info, false);

				gui.getMainFrame().getGesundheit().itemCombo
						.setSelectedIndex(i);
			}

		}
		if (quelle == take) {
			int i = roomItemL.getSelectedIndex();
			if (i != -1) {
				ItemInfo[] items = gui.getFigure().getRoomInfo().getItems();
				ItemInfo it = items[i];

				gui.getControl().itemClicked(it, false);
			}

		}

	}

	public static String getSizedString(String s) {

		if (s.length() > 22) {
			String n = new String(s.substring(0, 19) + "...");
			return n;
		} else {
			return s;
		}
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		Object quelle = ie.getSource();
	}

	@Override
	public void valueChanged(ListSelectionEvent le) {
		JList list = (JList) le.getSource();
		int i = list.getSelectedIndex();
		if (i != -1) {
			if (list == roomItemL) {
				gui.getMainFrame().getText()
						.setText(((ItemInfo) items.get(i)).getParagraphs());
			} else if (list == heroItemL) {
				gui.getMainFrame().getText()
						.setText(itemsH.get(i).getParagraphs());
			}

		}

	}
}
