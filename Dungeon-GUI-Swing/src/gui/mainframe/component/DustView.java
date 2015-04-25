package gui.mainframe.component;

import figure.FigureInfo;
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
import item.HealPotion;
//import item.Item;
import item.ItemInfo;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.event.MouseInputAdapter;

import spell.SpellInfo;
import util.JDColor;
import control.ActionAssembler;

/**
 * @author Duke1
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class DustView extends JDJPanel implements MouseListener,
		ActionListener, ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HealthPot pot;

	JDJButton apB = new JDJButton("AP:   -  ");

	JDJButton zaubern = new JDJButton("Zaubern");

	JDJButton smallHeal = new JDJButton(" h ");

	JComboBox sorcCombo = new JComboBox();

	JPanel sorcPanel;

	JPanel potPanel;

	JPanel comboPanel;

	// Box expBox;

	// Game game;

	ActionAssembler control;

	public DustView(JDGUISwing gui) {
		this(gui, getRightPanel(gui));
	}

	public DustView(JDGUISwing gui, JDJPanel rightPanel) {

		super(gui);
		control = gui.getActionAssembler();

		zaubern = new JDJButton(JDEnv.getResourceBundle().getString("sorcer"));
		sorcPanel = new JDJPanel(gui);

		potPanel = new JDJPanel(gui);

		comboPanel = new JDJPanel(gui);

		this.setLayout(null);
		GridBagConstraints gbc = new GridBagConstraints();

		makeSorcPanel();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.insets.bottom = 1;
		gbc.insets.top = 0;
		gbc.insets.left = 2;
		gbc.insets.right = 2;
		gbc.fill = GridBagConstraints.BOTH;
		sorcPanel.setBounds(0, 0, 255, 80);
		this.add(sorcPanel, gbc);

		makePotPanel();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		potPanel.setBounds(0, 80, 110, 110);
		this.add(potPanel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.EAST;
		rightPanel.setBounds(113, 80, 142, 120);
		this.add(rightPanel, gbc);
		this.add(rightPanel);

		this.setPreferredSize(new Dimension(300, 200));
		pot.repaint();
		zaubern.addActionListener(this);
		smallHeal.addActionListener(this);

		smallHeal.addMouseListener(this);
		apB.addActionListener(this);
		zaubern.addMouseListener(this);
		apB.addMouseListener(this);

	}

	private List<ItemInfo> getHealingList() {
		List<ItemInfo> items = Arrays.asList(gui.getFigure().getFigureItems());
		List<ItemInfo> healings = new LinkedList<ItemInfo>();
		for (int i = 0; i < items.size(); i++) {
			ItemInfo it = items.get(i);
			if (it != null) {
				if ((it.getItemClass().equals(HealPotion.class))) {
					healings.add(it);

				}
			}
		}
		return healings;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object sc = e.getSource();
		if (sc == smallHeal) {
			List<ItemInfo> l = getHealingList();
			int min = 10000;
			int index = -1;
			for (int i = 0; i < l.size(); i++) {
				ItemInfo it = l.get(i);
				int w = it.getWorth();
				if (w < min) {
					min = w;
					index = i;
				}
			}
			if (index != -1) {
				ItemInfo seft = l.get(index);
				gui.getActionAssembler().wannaUseItem(seft, null, false);
			}
		}
		if (sc == zaubern) {

			// int index = sorcCombo.getSelectedIndex();
			Object o = sorcCombo.getSelectedItem();
			if (o instanceof SpellInfo) {
				gui.getActionAssembler().sorcButtonClicked((SpellInfo) o);
			}

		}
		if (sc == apB) {
			gui.getActionAssembler().wannaEndRound();
			// game.endRound();
		}

	}

	/**
	 * @see java.awt.event.ItemListener#itemStateChanged(ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {

	}

	private void makePotPanel() {

		Image potI = (Image) ImageManager.lebenskugel.getImage();
		pot = new HealthPot(110, 110, JDColor.yellow, gui, potI);
		potPanel.setLayout(null);
		potPanel.add(pot);
		pot.setBounds(0, 0, 120, 120);

	}

	private static JDJPanel getRightPanel(final JDGUISwing gui) {

		final JRadioButton exp1 = new JDJRadioButton(JDEnv.getResourceBundle()
				.getString("self"));

		final JRadioButton exp2 = new JDJRadioButton(JDEnv.getResourceBundle()
				.getString("weapon"));

		final JRadioButton exp3 = new JDJRadioButton(JDEnv.getResourceBundle()
				.getString("enemy"));

		furnishRadioButtons(gui, exp1, exp2, exp3);
		FigureInfo info = gui.getFigure();
		if (info instanceof HeroInfo) {
			final HeroInfo heroInfo = ((HeroInfo) info);
			JDJPanel boxContainer = new JDJPanel(gui) {
				@Override
				public void updateView() {
					int j = 0;
					if (heroInfo != null) {
						j = heroInfo.getExpCode();
					}
					if (j == 0) {
						exp1.setSelected(true);
					} else if (j == 1) {
						exp2.setSelected(true);
					} else if (j == 2) {
						exp3.setSelected(true);
					}
					if (heroInfo != null
							&& heroInfo.hasSkillPoints().booleanValue()) {
						exp1.setText(JDEnv.getResourceBundle().getString(
								"gui_self_blue"));
					} else {
						exp1.setText(JDEnv.getResourceBundle().getString(
								"gui_self"));
					}
					super.updateView();
				}
			};
			Box expBox = Box.createVerticalBox();
			expBox.add(exp1);
			expBox.add(exp2);
			expBox.add(exp3);
			boxContainer.add(expBox);
			expBox.setBorder(new JDJTitledBorder(JDEnv.getResourceBundle()
					.getString("gui_learn")));
			return boxContainer;
		}
		return null;

	}

	private static void furnishRadioButtons(final JDGUISwing gui,
			final JRadioButton exp1, final JRadioButton exp2,
			final JRadioButton exp3) {
		String selbstText = JDEnv.getResourceBundle().getString(
				"gui_exp_self_alt");

		String waffeText = JDEnv.getResourceBundle().getString(
				"gui_exp_weap_alt");

		String gegnerText = JDEnv.getResourceBundle().getString(
				"gui_exp_enemy_alt");

		exp1.setToolTipText(selbstText);
		exp2.setToolTipText(waffeText);
		exp3.setToolTipText(gegnerText);

		exp1.addMouseListener(new MouseInputAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				gui.getActionAssembler().wannaChangeExpCode(0);
			}
		});
		exp2.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gui.getActionAssembler().wannaChangeExpCode(1);
			}
		});
		exp3.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gui.getActionAssembler().wannaChangeExpCode(2);
			}
		});

		ButtonGroup bgWeap = new ButtonGroup();
		bgWeap.add(exp1);
		bgWeap.add(exp2);
		bgWeap.add(exp3);
	}

	private void makeSorcPanel() {
		GridLayout gl1 = new GridLayout(2, 1);
		gl1.setVgap(5);
		sorcPanel.setLayout(gl1);
		sorcPanel.setPreferredSize(new Dimension(290, 65));
		comboPanel.setPreferredSize(new Dimension(195, 27));
		sorcCombo.setRenderer(new MyComboRenderer());
		comboPanel.addMouseListener(this);
		comboPanel.add(smallHeal);
		sorcCombo.setPreferredSize(new Dimension(170, 28));
		sorcCombo.setOpaque(false);
		comboPanel.add(sorcCombo);
		sorcPanel.add(comboPanel);
		sorcCombo.addItemListener(this);
		sorcCombo.addMouseListener(this);
		JPanel buttonPanel = new JDJPanel(gui);
		GridLayout gl = new GridLayout(1, 2);
		gl.setHgap(5);
		buttonPanel.setLayout(gl);
		sorcPanel.add(buttonPanel);
		buttonPanel.add(apB);
		buttonPanel.add(zaubern);
		sorcCombo.setBackground(MyComboRenderer.bgColor);

	}

	@Override
	public void updateView() {

		resetSorcCombo();
		FigureInfo info = gui.getFigure();
		HeroInfo heroInfo = null;
		int dustVal = 0;
		int dustBase = 0;
		if (info instanceof HeroInfo) {
			heroInfo = ((HeroInfo) info);
			dustBase = (int) heroInfo.getAttributeBasic(Attribute.DUST);
			dustVal = (int) heroInfo.getAttributeValue(Attribute.DUST);
		}

		pot.setVal(dustVal, dustBase);

		pot.repaint();

		int k = info.getActionPoints();
		// System.out.println("setzte ap auf: "+k);

		apB.setText("AP: " + k + " (" + gui.getFigure().getGameRound() + ")");
		apB.repaint();

		super.updateView();
		// }
	}

	@Override
	public void mouseClicked(MouseEvent me) {

		Paragraph[] p = null;
		SpellInfo sp = (SpellInfo) sorcCombo.getSelectedItem();
		// nachtr�glich eingef�gt, weil NullpointerException wenn Item
		// hinzugef�gt
		if (sp != null) {
			p = sp.getParagraphs();
			gui.getMainFrame().setText(p);

		}
	}

	@Override
	public void mousePressed(MouseEvent me) {

	}

	@Override
	public void mouseEntered(MouseEvent me) {
		Object quelle = me.getSource();
		Paragraph[] p = null;
		if (quelle == comboPanel) {
			SpellInfo sp = (SpellInfo) sorcCombo.getSelectedItem();
			p = sp.getParagraphs();
		} else if (quelle == apB) {
			p = new Paragraph[1];
			p[0] = new Paragraph(JDEnv.getResourceBundle().getString(
					"gui_ap_button_alt"));
			p[0].setSize(12);
			p[0].setColor(new JDColor(255, 255, 255));
			p[0].setCentered();
		} else if (quelle == smallHeal) {
			p = new Paragraph[1];
			p[0] = new Paragraph(JDEnv.getResourceBundle().getString(
					"gui_small_heal_alt"));
			p[0].setSize(12);
			p[0].setColor(new JDColor(255, 255, 255));
			p[0].setCentered();
		} else if (quelle == zaubern) {
			p = new Paragraph[1];
			p[0] = new Paragraph(JDEnv.getResourceBundle().getString(
					"gui_sorc_button_alt"));
			p[0].setSize(12);
			p[0].setColor(new JDColor(255, 255, 255));
			p[0].setCentered();
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

	private void resetSorcCombo() {
		int cnt = sorcCombo.getItemCount();
		FigureInfo figureInfo = gui.getFigure();
		HeroInfo heroInfo = null;
		if (figureInfo instanceof HeroInfo) {
			heroInfo = (HeroInfo) figureInfo;
		}
		List<SpellInfo> l = heroInfo.getSpells();

		if (heroInfo != null) {
			l = heroInfo.getSpells();
		}
		if (cnt != l.size()) {
			sorcCombo.removeAllItems();
			for (int i = 0; i < l.size(); i++) {
				Object o = l.get(i);
				if (o != null) {
					sorcCombo.addItem(o);
				}
			}
		}

	}

	/**
	 * @return Returns the sorcCombo.
	 * 
	 */
	public JComboBox getSorcCombo() {
		return sorcCombo;
	}

}
