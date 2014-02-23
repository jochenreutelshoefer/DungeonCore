/*
 * Created on 05.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.mainframe.component;

import figure.FigureInfo;
import figure.hero.HeroInfo;
import game.JDEnv;
import gui.JDJLabel;
import gui.JDJPanel;
import gui.MyJDGui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import spell.SpellInfo;

/**
 * @author Jochen
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SpellsView extends JDJPanel implements ActionListener {

	// Hero h;

	private JButton[] butts;

	private List<SpellInfo> spells;
	
	// private ActionAssembler control;

	public SpellsView(MyJDGui gui) {
		super(gui);
		gui.getControl();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (spells != null) {
			Object sc = ae.getSource();

			for (int i = 0; i < butts.length; i++) {
				if (sc == butts[i]) {
					gui.getControl().wannaLernSpell(spells.get(i));
				}
			}
		}
	}

	public void updateView() {

		this.removeAll();

		FigureInfo info = gui.getFigure();
		HeroInfo heroInfo = null;
		if (info instanceof HeroInfo) {
			heroInfo = (HeroInfo) info;
			spells = heroInfo.getSpellBuffer();

		}

		LayoutManager layout = new GridLayout(10, 1);

		// BorderLayout bl = new BorderLayout();
		this.setLayout(layout);
		JDJPanel panels[] = new JDJPanel[10];
		for (int i = 0; i < 10; i++) {
			panels[i] = new JDJPanel(gui);
			panels[i].setLayout(new FlowLayout());
			this.add(panels[i]);
		}
		// JDJPanel p = new JDJPanel();
		// this.add(p,BorderLayout.CENTER);
		// p.setLayout(layout);
		if (heroInfo != null) {
			panels[0].add(new JDJLabel(JDEnv.getResourceBundle().getString(
					"gui_learn_spell")));
			panels[1].add(new JDJLabel(JDEnv.getResourceBundle().getString(
					"gui_learning_points")
					+ heroInfo.getSpellPoints() + "</b></html>"));
		}
		if (spells != null) {
			butts = new JButton[spells.size()];

			// System.out.println("spells.size(): "+spells.size());
			for (int i = 0; i < spells.size(); i++) {
				SpellInfo s = (spells.get(i));
				butts[i] = new JButton("<html>" + s.toString() + " :  <b>"
						+ s.getLernCost() + "</b></html>");
				// System.out.println("i: "+i+ " butt:"+butts[i].toString());
				// butts[i] = butts[i];
				// JButton butt = butts[i];
				Dimension d = new Dimension(200, 40);
				butts[i].setPreferredSize(d);
				butts[i].setMaximumSize(d);
				butts[i].addActionListener(this);
				//
				String text = "<html><b><u>"
						+ s.toString()
						+ ":</b></u><br>"
						+ JDEnv.getResourceBundle().getString(
								"gui_learning_cost") + ": " + s.getLernCost()
						+ "<br>"
						+ JDEnv.getResourceBundle().getString("gui_fight")
						+ " ";
				boolean fight = s.isFight();
				boolean normal = s.isNormal();
				if (fight) {
					text += JDEnv.getResourceBundle().getString("gui_yes");
				} else {
					text += JDEnv.getResourceBundle().getString("gui_no");
				}
				text += "<br>"
						+ JDEnv.getResourceBundle().getString("gui_else")
						+ ": ";
				if (normal) {
					text += JDEnv.getResourceBundle().getString("gui_yes")
							+ "<br>";
				} else {
					text += JDEnv.getResourceBundle().getString("gui_no")
							+ "<br>";
				}
				text += s.getText() + "</html>";
				butts[i].setToolTipText(text);
				panels[i + 2].add(butts[i]);
			}
		}

	}

	// public void setHero(Hero held) {
	// h = held;
	//
	// }

}