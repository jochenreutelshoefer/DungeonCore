package gui.mainframe.component;

//import JDGuiJDJButton;
//import JDGuiJDJPanel;
//import JDGuiJDJRadioButton;
//import JDGuiJDJTitledBorder;

import figure.FigureInfo;
import figure.action.EquipmentChangeAction;
import figure.attribute.Attribute;
import figure.hero.HeroInfo;
import game.JDEnv;
import graphics.ImageManager;
import gui.JDGUISwing;
import gui.JDJButton;
import gui.JDJPanel;
import gui.JDJRadioButton;
import gui.JDJTitledBorder;
import gui.MyComboRenderer;
import gui.Paragraph;
import gui.engine2D.DrawUtils;
import item.HealPotion;
//import item.Item;
import item.ItemInfo;
import item.equipment.EquipmentItemInfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import util.JDColor;
import control.AbstractSwingMainFrame;
import control.ActionAssembler;

/**
 * @author Duke1
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class HealthView extends JDJPanel implements ActionListener,
		ItemListener, MouseListener {

	HealthPot pot;

	JDJButton memoryB = new JDJButton("Gedaechtnis");

	JDJButton bigHeal = new JDJButton(" H ");

	boolean mem = false;

	JDJButton benutzen = new JDJButton("Benutzen");

	JComboBox itemCombo = new JComboBox();


	JPanel itemsPanel;

	JPanel potPanel;

	JPanel comboPanel;

	JPanel southPanel;

	JRadioButton weap1 = new JDJRadioButton("leer");

	JRadioButton weap2 = new JDJRadioButton("leer");

	JRadioButton weap3 = new JDJRadioButton("leer");

	

	String empty = "";
	
	ActionAssembler control;
	
	public int getSelectedItemIndex () {
		return itemCombo.getSelectedIndex();
	}

	public HealthView(JDGUISwing gui) {

		super(gui);
		this.control = gui.getActionAssembler();
		memoryB = new JDJButton(JDEnv.getResourceBundle().getString(
				"gui_memory"));
		benutzen = new JDJButton(JDEnv.getResourceBundle().getString("gui_use"));
		empty = JDEnv.getResourceBundle().getString("gui_empty");
		weap1 = new JDJRadioButton(empty);

		weap2 = new JDJRadioButton(empty);

		weap3 = new JDJRadioButton(empty);

		itemsPanel = new JDJPanel(gui);

		potPanel = new JDJPanel(gui);

		comboPanel = new JDJPanel(gui);

	
		this.setLayout(null);
		GridBagConstraints gbc = new GridBagConstraints();
		

		makeItemsPanel();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets.bottom = 1;
		gbc.insets.top = 0;
		gbc.insets.left = 2;
		gbc.insets.right = 2;
		itemsPanel.setBounds(0, 0, 255, 80);
		this.add(itemsPanel, gbc);

		makePotPanel();

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		makeWeaponPanel();
		boxWeap.setBounds(0, 80, 144, 120);
		this.add(boxWeap, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		potPanel.setBounds(147, 80, 110, 140);

		this.add(potPanel, gbc);

		this.setPreferredSize(new Dimension(300, 200));
	

		pot.repaint();

		benutzen.addActionListener(this);
		memoryB.addActionListener(this);

		bigHeal.addMouseListener(this);
		bigHeal.addActionListener(this);
		benutzen.addMouseListener(this);
		memoryB.addMouseListener(this);

		this.setAlignmentX(this.LEFT_ALIGNMENT);

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object quelle = ae.getSource();
	
		
		if (quelle == benutzen) {
			boolean meta = false;
			String s = ae.getActionCommand();
			if(s.equals("meta")) {
				meta = true;
			}
			int i = itemCombo.getSelectedIndex();
			if (i != -1) {
				List<ItemInfo> l = gui.getFigure().getFigureItemList();
				ItemInfo info = l.get(i);
				gui.getActionAssembler().useButtonClicked(info,meta);
			}
		

		}
		if (quelle == memoryB) {
			
		}

		if (quelle == bigHeal) {
			LinkedList l = getHealingList();
			int max = 0;
			int index = -1;
			for (int i = 0; i < l.size(); i++) {
				ItemInfo it = (ItemInfo) l.get(i);
				int w = it.getWorth();
				if (w > max) {
					max = w;
					index = i;
				}
			}
			if (index != -1) {
				ItemInfo seft = (ItemInfo) l.get(index);
				gui.getActionAssembler().wannaUseItem(seft,null,false);
			}
		}

	}


	private LinkedList getHealingList() {
		java.util.List items = Arrays.asList(gui.getFigure().getFigureItems());
		
		LinkedList healings = new LinkedList();
		for (int i = 0; i < items.size(); i++) {
			ItemInfo it = (ItemInfo) items.get(i);
			if (it != null) {
				if ((it.getItemClass().equals(HealPotion.class))) {
					healings.add(it);

				}
			}
		}
		return healings;
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
	
	}

	private void makePotPanel() {
		
		AbstractSwingMainFrame mainFrame = gui.getMainFrame();
		MediaTracker tracker = new MediaTracker(mainFrame);

		Image d = (Image) ImageManager.deathImage.getImage();
		Image potI = (Image) ImageManager.lebenskugel.getImage();
		tracker.addImage(d, 1);
		tracker.addImage(potI, 1);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (d == null) {

			// System.out.println("totenkopf ist null");
		}
		pot = new HealthPot(110, 110, JDColor.red, d, gui, potI);
		// potPanel.setBorder(new EtchedBorder());
		BorderLayout bl = new BorderLayout();
		bl.setHgap(0);
		bl.setVgap(0);
		potPanel.setLayout(new BorderLayout());
		
		potPanel.add(pot, BorderLayout.CENTER);
		potPanel.setPreferredSize(new Dimension(90, 155));
		pot.setBounds(0, 0, 90, 150);
		pot.setLocation(20, 0);

	}

	Box boxWeap;

	private void makeWeaponPanel() {

	
		Dimension maxDim = new Dimension(200, 120);
		boxWeap = Box.createVerticalBox();
		boxWeap.setMaximumSize(maxDim);
		boxWeap.setSize(200, 60);
		boxWeap.setBorder(new JDJTitledBorder(JDEnv.getResourceBundle()
				.getString("gui_weapon")));
		;
		boxWeap.add(weap1);
		boxWeap.add(weap2);
		boxWeap.add(weap3);

		weap1.addItemListener(this);
		weap2.addItemListener(this);
		weap3.addItemListener(this);
		weap1.addMouseListener(this);
		weap2.addMouseListener(this);
		weap3.addMouseListener(this);

		ButtonGroup bgWeap = new ButtonGroup();
		bgWeap.add(weap1);
		bgWeap.add(weap2);
		bgWeap.add(weap3);

	}

	private void makeItemsPanel() {

		GridLayout gl1 = new GridLayout(2, 1);
		gl1.setVgap(5);
		itemsPanel.setLayout(gl1);
		itemsPanel.setPreferredSize(new Dimension(290, 65));
		itemCombo.setPreferredSize(new Dimension(195, 27));
		itemCombo.setRenderer(new MyComboRenderer());
		comboPanel.add(itemCombo);
		comboPanel.add(bigHeal);
		comboPanel.addMouseListener(this);
		itemCombo.addItemListener(this);
		itemsPanel.add(comboPanel);
		JPanel buttonPanel = new JDJPanel(gui);

		GridLayout gl = new GridLayout(1, 2);
		gl.setHgap(5);
		buttonPanel.setLayout(gl);
		itemsPanel.add(buttonPanel);
		buttonPanel.add(memoryB);
		buttonPanel.add(benutzen);

	}

	public void updateView() {
		
		resetItemCombo();
		FigureInfo info = gui.getFigure();
		HeroInfo heroInfo = null;
		int healthVal = 0;
		int healthBase = 0;
		if (info instanceof HeroInfo) {
			heroInfo = ((HeroInfo) info);
			healthBase = (int) heroInfo.getAttributeBasic(Attribute.HEALTH);
			healthVal = (int) heroInfo.getAttributeValue(Attribute.HEALTH);
		}
		pot.setVal(healthVal, healthBase);
		pot.repaint();
		int weaponIndex = -1;
		if (heroInfo != null) {
			weaponIndex = heroInfo.getWeaponIndex();
		}

		if (weaponIndex == 0) {
			weap1.setSelected(true);
		} else if (weaponIndex == 1) {
			weap2.setSelected(true);
		} else if (weaponIndex == 2) {
			weap3.setSelected(true);
		}


		EquipmentItemInfo weapon1 = heroInfo.getWeapon(0);
		if (weapon1 != null) {
			weap1.setForeground(DrawUtils.convertColor(weapon1.getStatusColor()));
			weap1.setText((weapon1.toString()));

		} else {
			weap1.setText(empty);
			weap1.setForeground(Color.BLACK);
		}

		EquipmentItemInfo weapon2 = heroInfo.getWeapon(1);
		if (weapon2 != null) {
			weap2.setForeground(DrawUtils.convertColor(weapon2.getStatusColor()));
			weap2.setText((weapon2.toString()));

		} else {
			weap2.setText(empty);
		}

		EquipmentItemInfo weapon3 = heroInfo.getWeapon(2);
		if (weapon3 != null) {
			weap3.setForeground(DrawUtils.convertColor(weapon3.getStatusColor()));
			weap3.setText((weapon3.toString()));

		} else {
			weap3.setText(empty);
		}

		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		Object quelle = me.getSource();
		if (quelle == weap1) {
			gui.getActionAssembler().wannaSwitchEquipmentItem(
					EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON, 0);

			
		} else if (quelle == weap2) {
			gui.getActionAssembler().wannaSwitchEquipmentItem(
					EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON, 1);

			
		} else if (quelle == weap3) {
			gui.getActionAssembler().wannaSwitchEquipmentItem(
					EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON, 2);

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
		if (quelle == weap1) {
			it = heroInfo.getWeapon(0);
			if (it != null) {
				p = it.getParagraphs();
			}
		} else if (quelle == weap2) {
			it = heroInfo.getWeapon(1);
			if (it != null) {
				p = it.getParagraphs();
			}
		} else if (quelle == weap3) {
			it = heroInfo.getWeapon(2);
			if (it != null) {
				p = it.getParagraphs();
			}

		} else if (quelle == benutzen) {
			p = new Paragraph[1];
			p[0] = new Paragraph(JDEnv.getResourceBundle().getString(
					"gui_use_button_alt"));
			p[0].setSize(12);
			p[0].setColor(new JDColor(255, 255, 255));
			p[0].setCentered();
		} else if (quelle == memoryB) {
			p = new Paragraph[1];
			
			p[0] = new Paragraph("feature not available yet!");
			p[0].setSize(12);
			p[0].setColor(new JDColor(255, 255, 255));
			p[0].setCentered();
		} else if (quelle == bigHeal) {
			p = new Paragraph[1];
			p[0] = new Paragraph(JDEnv.getResourceBundle().getString(
					"gui_big_heal_alt"));
			p[0].setSize(12);
			p[0].setColor(new JDColor(255, 255, 255));
			p[0].setCentered();
		} else {
			if (itemCombo.getItemCount() > 0) {
				ItemInfo sp = (ItemInfo) itemCombo.getSelectedItem();
				p = sp.getParagraphs();
			}

		}

		gui.getMainFrame().setText(p);
	}

	@Override
	public void mouseReleased(MouseEvent me) {

	}

	@Override
	public void mouseExited(MouseEvent me) {
		gui.getMainFrame().setText(null);
	}

	private void resetItemCombo() {
		int cnt = itemCombo.getItemCount();

		List<ItemInfo> l = gui.getFigure().getFigureItemList();
		if (cnt != l.size()) {
			itemCombo.removeAllItems();
			for (int i = 0; i < l.size(); i++) {
				Object o = l.get(i);
				if(o != null) {
					itemCombo.addItem(o);
				}
			}
		}	
		itemCombo.setBackground(MyComboRenderer.bgColor);
	}

	public JComboBox getItemCombo() {
		return itemCombo;
	}

}