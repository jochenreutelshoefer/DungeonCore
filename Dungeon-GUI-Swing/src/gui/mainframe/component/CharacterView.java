package gui.mainframe.component;

import figure.FigureInfo;
import figure.attribute.Attribute;
import figure.hero.Character;
import figure.hero.HeroInfo;
import gui.MyJDGui;
import gui.JDJLabel;
import gui.JDJPanel;
import gui.Paragraph;
import item.ItemInfo;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import control.ActionAssembler;

/**
 * @author Duke1
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */

public class CharacterView extends JDJPanel implements MouseMotionListener,
		MouseListener {

	// Character c;

	// Hero h;

	JPanel left;

	JPanel right;

	JPanel box3;

	JPanel panel;

	// Box box1 = Box.createVerticalBox();
	// Box box2 = Box.createHorizontalGlue();
	// Box box3 = Box.createVerticalBox();
	SkillExpView levelV;

	SkillExpView selfV;

	SkillExpView selfV2;

	SkillExpView selfV3;

	SkillExpView natureV;

	SkillExpView creatureV;

	SkillExpView undeadV;

	SkillExpView axeV;

	SkillExpView clubV;

	SkillExpView lanceV;

	SkillExpView swordV;

	SkillExpView wolfknifeV;

	SkillExpView zeroV;
	
	ActionAssembler control;

	public CharacterView(MyJDGui gui) {
		super(gui);
		control = gui.getControl();
		levelV = new SkillExpView(0, 1, gui);

	
		natureV = new SkillExpView(0, 1, gui);

		creatureV = new SkillExpView(0, 1, gui);

		undeadV = new SkillExpView(0, 1, gui);

		axeV = new SkillExpView(0, 1, gui);

		clubV = new SkillExpView(0, 1, gui);

		lanceV = new SkillExpView(0, 1, gui);

		swordV = new SkillExpView(0, 1, gui);

		wolfknifeV = new SkillExpView(0, 1, gui);

		zeroV = new SkillExpView(0, 1, gui);

		left = new JDJPanel(gui);

		right = new JDJPanel(gui);

		box3 = new JDJPanel(gui);

		panel = new JDJPanel(gui);

		selfV = new SkillExpView(0, 1, gui);

		selfV2 = new SkillExpView(0, 1, gui);

		selfV3 = new SkillExpView(0, 1, gui);

		GridLayout gl = new GridLayout(29, 3);
		

		this.setLayout(gl);
		// gl.setHgap(0);
		// Insets in = this.getInsets();
		// in.left = 3;

		// this.add(left);
		// this.add(right);
		// this.add(box3);

		this.add(namet);
		this.add(name);
		this.add(getEmptyComponent());

		this.add(levelt);
		this.add(level);
		this.add(levelV);

		this.add(pointst);
		this.add(points);
		this.add(getEmptyComponent());

		this.add(getEmptyComponent());
		this.add(getEmptyComponent());
		this.add(getEmptyComponent());

		this.add(healtht);
		this.add(health);
		this.add(getEmptyComponent());

		this.add(dustt);
		this.add(dust);
		this.add(getEmptyComponent());

		this.add(dustRegt);
		this.add(dustReg);
		this.add(getEmptyComponent());

		this.add(getEmptyComponent());
		this.add(getEmptyComponent());
		this.add(getEmptyComponent());

		this.add(strengtht);
		this.add(strength);
		this.add(selfV);

		this.add(dexterityt);
		this.add(dexterity);
		this.add(selfV2);

		this.add(psychot);
		this.add(psycho);
		this.add(selfV3);

		this.add(getEmptyComponent());
		this.add(getEmptyComponent());
		this.add(getEmptyComponent());

		this.add(axet);
		this.add(axe);
		this.add(axeV);

		this.add(lancet);
		this.add(lance);
		this.add(lanceV);

		this.add(swordt);
		this.add(sword);
		this.add(swordV);

		this.add(clubt);
		this.add(club);
		this.add(clubV);

		this.add(wolfknifet);
		this.add(wolfknife);
		this.add(wolfknifeV);

		this.add(getEmptyComponent());
		this.add(getEmptyComponent());
		this.add(getEmptyComponent());

		this.add(scoutt);
		this.add(scout);
		this.add(getEmptyComponent());

		this.add(getEmptyComponent());
		this.add(getEmptyComponent());
		this.add(getEmptyComponent());

		this.add(naturet);
		this.add(nature);
		this.add(natureV);

		this.add(creaturet);
		this.add(creature);
		this.add(creatureV);

		this.add(undeadt);
		this.add(undead);
		this.add(undeadV);

		this.add(getEmptyComponent());
		this.add(getEmptyComponent());
		this.add(getEmptyComponent());

		this.add(signt);
		this.add(sign);
		this.add(getEmptyComponent());

		this.add(weapont);
		this.add(weaponL);
		this.add(getEmptyComponent());

		this.add(armort);
		this.add(armorL);
		this.add(getEmptyComponent());

		this.add(helmett);
		this.add(helmetL);
		this.add(getEmptyComponent());

		this.add(shieldt);
		this.add(shieldL);
		this.add(getEmptyComponent());

		naturet.addMouseMotionListener(this);
		creaturet.addMouseMotionListener(this);
		undeadt.addMouseMotionListener(this);
		axet.addMouseMotionListener(this);
		clubt.addMouseMotionListener(this);
		lancet.addMouseMotionListener(this);
		swordt.addMouseMotionListener(this);
		wolfknifet.addMouseMotionListener(this);
		this.selfV.addMouseListener(this);
		this.selfV2.addMouseListener(this);
		this.selfV3.addMouseListener(this);
	
		
	}



	private Component getEmptyComponent() {
		return new JLabel("  ");
	}

	private String getAbsoluteValueString(double d) {
		int k = (int) (d / 10);
		return " --> " + k;
	}

	public void mouseReleased(MouseEvent me) {

	}

	public void mousePressed(MouseEvent me) {

	}

	public void mouseEntered(MouseEvent me) {

	}

	public void mouseExited(MouseEvent me) {

	}

	public void mouseClicked(MouseEvent me) {
		
		Object o = me.getSource();
		
		int key = -1;
		if(o == this.selfV) {
			key = Attribute.STRENGTH;
		}
		if(o == this.selfV2) {
			key = Attribute.DEXTERITY;
		}
		if(o == this.selfV3) {
			key = Attribute.PSYCHO;
		}
		if(key != -1) {
			gui.getControl().wannaSkillUp(key);
		}
		
	}

	public void updateView() {

		FigureInfo info = gui.getFigure();
		HeroInfo heroInfo = null;
		
		if (info instanceof HeroInfo) {
			heroInfo = ((HeroInfo) info);
		}
		if (heroInfo != null) {
			sign.setText(heroInfo.getSign());
			name.setText(heroInfo.getName());
			level.setText(Integer.toString(heroInfo.getLevel()));
			health.setText((int)(heroInfo
					.getAttributeValue(Attribute.HEALTH))
					+ "/"
					+ (int)(heroInfo
							.getAttributeBasic(Attribute.HEALTH)));
			
			dust.setText((int)(heroInfo
					.getAttributeValue(Attribute.DUST))
					+ "/"
					+ (int)(heroInfo
							.getAttributeBasic(Attribute.DUST)));
			
			double dustRegValue = heroInfo.getAttributeValue(Attribute.DUSTREG);
			double dustRegBasic = heroInfo.getAttributeBasic(Attribute.DUSTREG);
			
			dustReg.setText(Double.toString(dustRegValue).substring(0,3)
					+ "/"
					+ Double.toString(dustRegBasic).substring(0,3));
			strength.setText((int)(heroInfo
					.getAttributeValue(Attribute.STRENGTH))
					+ "/"
					+ (int)(heroInfo
							.getAttributeBasic(Attribute.STRENGTH)));
			dexterity.setText((int)(heroInfo
					.getAttributeValue(Attribute.DEXTERITY))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.DEXTERITY)));
			psycho.setText((int)(heroInfo
					.getAttributeValue(Attribute.PSYCHO))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.PSYCHO)));
			axe.setText((int)(heroInfo
					.getAttributeValue(Attribute.AXE))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.AXE))
					+ getAbsoluteValueString(heroInfo
							.getAttributeValue(Attribute.AXE)));
			lance.setText((int)(heroInfo
					.getAttributeValue(Attribute.LANCE))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.LANCE))
					+ getAbsoluteValueString(heroInfo
							.getAttributeValue(Attribute.LANCE)));
			sword.setText((int)(heroInfo
					.getAttributeValue(Attribute.SWORD))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.SWORD))
					+ getAbsoluteValueString(heroInfo
							.getAttributeValue(Attribute.SWORD)));
			club.setText((int)(heroInfo
					.getAttributeValue(Attribute.CLUB))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.CLUB))
					+ getAbsoluteValueString(heroInfo
							.getAttributeValue(Attribute.CLUB)));
			wolfknife.setText((int)(heroInfo
					.getAttributeValue(Attribute.WOLFKNIFE))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.WOLFKNIFE))
					+ getAbsoluteValueString(heroInfo
							.getAttributeValue(Attribute.WOLFKNIFE)));
			scout.setText((int)(heroInfo
					.getAttributeValue(Attribute.SCOUT))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.SCOUT)));
			nature.setText((int)(heroInfo
					.getAttributeValue(Attribute.NATURE_KNOWLEDGE))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.NATURE_KNOWLEDGE))
					+ getAbsoluteValueString(heroInfo
							.getAttributeValue(Attribute.NATURE_KNOWLEDGE)));
			creature.setText((int)(heroInfo
					.getAttributeValue(Attribute.CREATURE_KNOWLEDGE))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.CREATURE_KNOWLEDGE))
					+ getAbsoluteValueString(heroInfo
							.getAttributeValue(Attribute.CREATURE_KNOWLEDGE)));
			undead.setText((int)(heroInfo
					.getAttributeValue(Attribute.UNDEAD_KNOWLEDGE))
					+ "/"
					+ (int)((int) heroInfo
							.getAttributeBasic(Attribute.UNDEAD_KNOWLEDGE))
					+ getAbsoluteValueString(heroInfo
							.getAttributeValue(Attribute.UNDEAD_KNOWLEDGE)));
			// Inventory i = h.getInventory();
			// Weapon w = i.getWeapon1();
			ItemInfo it = heroInfo.getActualWeapon();
			if (it != null) {
				String wString = it.toString();
				weaponL.setText(InventoryView.getSizedString(wString));
			}
			else {
				weaponL.setText("leer");
			}
			// }
			// else {
			// weaponL.setText("leer");
			// }
			// ArmorItem a = i.getArmor1();
			// if(a != null) {
			it = heroInfo.getActualArmor();
			if (it != null) {
				armorL.setText(InventoryView.getSizedString(it.toString()));
			}else {
				armorL.setText("leer");
			}
			// }
			// else {
			// armorL.setText("leer");
			// }

			// Helmet h = i.getHelmet1();
			// if(h!= null) {
			it = heroInfo.getActualHelmet();
			if (it != null) {
				helmetL.setText(InventoryView.getSizedString(it.toString()));
			}
			else {
				helmetL.setText("leer");
			}
			// }else {
			// helmetL.setText("leer");
			// }
			// Shield s = i.getShield1();
			// if(s != null) {
			it = heroInfo.getActualShield();
			if (it != null) {
				shieldL.setText(InventoryView.getSizedString(it.toString()));
			}else {
				shieldL.setText("leer");
			}

			// }else {
			// shieldL.setText("leer");
			// }
			points.setText(Integer.toString(heroInfo.getTotalExp()));

			levelV.setVal(heroInfo.getExpInfo(0, 0)[0], heroInfo.getExpInfo(0,
					0)[1]);
			// //System.out.println("Werte fuer Level: "+c.getExpInfo(0, 0)[0]+
			// " - "+ c.getExpInfo(0, 0)[1]);

			int skillPoints = heroInfo.getSkillPoints();
			//System.out.println("skillPoints: "+skillPoints);
			selfV.setSkillPoints(skillPoints);
			selfV.setVal(heroInfo.getExpInfo(1, 0)[0], heroInfo
					.getExpInfo(1, 0)[1]);
			selfV2.setSkillPoints(skillPoints);
			selfV2.setVal(heroInfo.getExpInfo(1, 0)[0], heroInfo.getExpInfo(1,
					0)[1]);
			selfV3.setSkillPoints(skillPoints);
			selfV3.setVal(heroInfo.getExpInfo(1, 0)[0], heroInfo.getExpInfo(1,
					0)[1]);

			axeV.setVal(heroInfo.getExpInfo(3, 0)[0],
					heroInfo.getExpInfo(3, 0)[1]);
			clubV.setVal(heroInfo.getExpInfo(3, 1)[0], heroInfo
					.getExpInfo(3, 1)[1]);
			lanceV.setVal(heroInfo.getExpInfo(3, 2)[0], heroInfo.getExpInfo(3,
					2)[1]);
			swordV.setVal(heroInfo.getExpInfo(3, 3)[0], heroInfo.getExpInfo(3,
					3)[1]);
			wolfknifeV.setVal(heroInfo.getExpInfo(3, 4)[0], heroInfo
					.getExpInfo(3, 4)[1]);

			natureV.setVal(heroInfo.getExpInfo(2, 0)[0], heroInfo.getExpInfo(2,
					0)[1]);
			creatureV.setVal(heroInfo.getExpInfo(2, 1)[0], heroInfo.getExpInfo(
					2, 1)[1]);
			undeadV.setVal(heroInfo.getExpInfo(2, 2)[0], heroInfo.getExpInfo(2,
					2)[1]);


		}

	}

	public void mouseMoved(MouseEvent me) {
		Object sc = me.getSource();
		if (sc == naturet) {
			Paragraph[] p = new Paragraph[3];
			p[0] = new Paragraph("Naturkenntnis:");
			p[0].setSize(24);
			p[0].setCentered();
			p[0].setColor(Color.black);
			p[0].setBold();

			p[1] = new Paragraph(
					"Kenntnis des Helden über Naturmonster: Wölfe und Spinnen");
			p[1].setSize(12);
			p[1].setCentered();
			p[1].setColor(Color.black);
			p[1].setBold();

			p[2] = new Paragraph(
					"Erhöht Trefferchance, Trefferstärke, \nFluchtwahrscheinlichkeit, Drohstärke...");
			p[2].setSize(12);
			p[2].setCentered();
			p[2].setColor(Color.black);
			gui.getMainFrame().getText().setText(p);
		} else if (sc == creaturet) {
			Paragraph[] p = new Paragraph[3];
			p[0] = new Paragraph("Kreaturenkenntnis:");
			p[0].setSize(24);
			p[0].setCentered();
			p[0].setColor(Color.black);
			p[0].setBold();

			p[1] = new Paragraph(
					"Kenntnis des Helden über Kreaturenmonster: Orks und Oger");
			p[1].setSize(12);
			p[1].setCentered();
			p[1].setColor(Color.black);
			p[1].setBold();

			p[2] = new Paragraph(
					"Erhöht Trefferchance, Trefferstärke, \nFluchtwahrscheinlichkeit, Drohstärke...");
			p[2].setSize(12);
			p[2].setCentered();
			p[2].setColor(Color.black);
			gui.getMainFrame().getText().setText(p);
		} else if (sc == undeadt) {
			Paragraph[] p = new Paragraph[3];
			p[0] = new Paragraph("Untotenkenntnis:");
			p[0].setSize(24);
			p[0].setCentered();
			p[0].setColor(Color.black);
			p[0].setBold();

			p[1] = new Paragraph(
					"Kenntnis des Helden über Untotenmonster: Skelette und Ghuls");
			p[1].setSize(12);
			p[1].setCentered();
			p[1].setColor(Color.black);
			p[1].setBold();

			p[2] = new Paragraph(
					"Erhöht Trefferchance, Trefferstärke, \nFluchtwahrscheinlichkeit, Drohstärke...");
			p[2].setSize(12);
			p[2].setCentered();
			p[2].setColor(Color.black);
			gui.getMainFrame().getText().setText(p);
		} else if (sc == axet) {
			Paragraph[] p = new Paragraph[3];
			p[0] = new Paragraph("Axtfertigkeit:");
			p[0].setSize(24);
			p[0].setCentered();
			p[0].setColor(Color.black);
			p[0].setBold();

			p[1] = new Paragraph(
					" erhöht Schadensrate und Trefferwahrscheinlickeit mit Axt");
			p[1].setSize(12);
			p[1].setCentered();
			p[1].setColor(Color.black);
			p[1].setBold();

			p[2] = new Paragraph(
					"Ghul: ++      Spinne: +\nOger: 0       Skelett:  0\nOrk: 0       Wolf: -");
			p[2].setSize(14);
			p[2].setCentered();
			p[2].setColor(Color.black);

			gui.getMainFrame().getText().setText(p);
		} else if (sc == clubt) {
			Paragraph[] p = new Paragraph[3];
			p[0] = new Paragraph("Knüppelfertigkeit:");
			p[0].setSize(24);
			p[0].setCentered();
			p[0].setColor(Color.black);
			p[0].setBold();

			p[1] = new Paragraph(
					" erhöht Schadensrate und Trefferwahrscheinlickeit mit Knüppel");
			p[1].setSize(12);
			p[1].setCentered();
			p[1].setColor(Color.black);
			p[1].setBold();

			p[2] = new Paragraph(
					"Ghul: -      Spinne: --\nOger: -       Skelett:  +++\nOrk: 0       Wolf: 0");
			p[2].setSize(14);
			p[2].setCentered();
			p[2].setColor(Color.black);

			gui.getMainFrame().getText().setText(p);

		} else if (sc == lancet) {
			Paragraph[] p = new Paragraph[3];
			p[0] = new Paragraph("Lanzenfertigkeit:");
			p[0].setSize(24);
			p[0].setCentered();
			p[0].setColor(Color.black);
			p[0].setBold();

			p[1] = new Paragraph(
					" erhöht Schadensrate und Trefferwahrscheinlickeit mit Lanze");
			p[1].setSize(12);
			p[1].setCentered();
			p[1].setColor(Color.black);
			p[1].setBold();

			p[2] = new Paragraph(
					"Ghul: 0      Spinne: +\nOger: ++       Skelett:  -\nOrk: 0       Wolf: --");
			p[2].setSize(14);
			p[2].setCentered();
			p[2].setColor(Color.black);

			gui.getMainFrame().getText().setText(p);

		} else if (sc == swordt) {
			Paragraph[] p = new Paragraph[3];
			p[0] = new Paragraph("Schwertfertigkeit:");
			p[0].setSize(24);
			p[0].setCentered();
			p[0].setColor(Color.black);
			p[0].setBold();

			p[1] = new Paragraph(
					" erhöht Schadensrate und Trefferwahrscheinlickeit mit Schwert");
			p[1].setSize(12);
			p[1].setCentered();
			p[1].setColor(Color.black);
			p[1].setBold();

			p[2] = new Paragraph(
					"Ghul: 0      Spinne: 0\nOger: 0       Skelett:  0\nOrk: +       Wolf: 0");
			p[2].setSize(14);
			p[2].setCentered();
			p[2].setColor(Color.black);

			gui.getMainFrame().getText().setText(p);

		} else if (sc == wolfknifet) {
			Paragraph[] p = new Paragraph[3];
			p[0] = new Paragraph("Wolfsmesserfertigkeit:");
			p[0].setSize(24);
			p[0].setCentered();
			p[0].setColor(Color.black);
			p[0].setBold();

			p[1] = new Paragraph(
					" erhöht Schadensrate und Trefferwahrscheinlickeit mit Wolfsmesser");
			p[1].setSize(12);
			p[1].setCentered();
			p[1].setColor(Color.black);
			p[1].setBold();

			p[2] = new Paragraph(
					"Ghul: -      Spinne: -\nOger: -       Skelett:  --\nOrk: -       Wolf: +++");
			p[2].setSize(14);
			p[2].setCentered();
			p[2].setColor(Color.black);

			gui.getMainFrame().getText().setText(p);
		}
	}

	public void mouseDragged(MouseEvent me) {

	}

	/**
	 * 
	 * @uml.property name="namet"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel namet = new JDJLabel("Name: ");

	/**
	 * 
	 * @uml.property name="levelt"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel levelt = new JDJLabel("Level: ");

	/**
	 * 
	 * @uml.property name="pointst"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel pointst = new JDJLabel("Punkte: ");

	/**
	 * 
	 * @uml.property name="signt"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel signt = new JDJLabel("Sternzeichen: ");

	/**
	 * 
	 * @uml.property name="healtht"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel healtht = new JDJLabel("Gesundheit: ");

	/**
	 * 
	 * @uml.property name="dustt"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel dustt = new JDJLabel("Zauberstaub: ");

	/**
	 * 
	 * @uml.property name="dustRegt"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel dustRegt = new JDJLabel("Spiritualität: ");

	/**
	 * 
	 * @uml.property name="strengtht"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel strengtht = new JDJLabel("Kraft: ");

	/**
	 * 
	 * @uml.property name="dexterityt"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel dexterityt = new JDJLabel("Geschicklichkeit: ");

	/**
	 * 
	 * @uml.property name="psychot"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel psychot = new JDJLabel("Psyche: ");

	/**
	 * 
	 * @uml.property name="empty"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel empty = new JDJLabel("");

	/**
	 * 
	 * @uml.property name="axet"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel axet = new JDJLabel("Axt: ");

	/**
	 * 
	 * @uml.property name="lancet"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel lancet = new JDJLabel("Lanze: ");

	/**
	 * 
	 * @uml.property name="swordt"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel swordt = new JDJLabel("Schwert: ");

	/**
	 * 
	 * @uml.property name="clubt"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel clubt = new JDJLabel("Knüppel");

	/**
	 * 
	 * @uml.property name="wolfknifet"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel wolfknifet = new JDJLabel("Wolfmesser: ");

	/**
	 * 
	 * @uml.property name="scoutt"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel scoutt = new JDJLabel("Spähen: ");

	/**
	 * 
	 * @uml.property name="naturet"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel naturet = new JDJLabel("Naturkenntnis: ");

	/**
	 * 
	 * @uml.property name="creaturet"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel creaturet = new JDJLabel("Kreaturenkennt.: ");

	/**
	 * 
	 * @uml.property name="undeadt"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel undeadt = new JDJLabel("Untotenkenntnis: ");

	/**
	 * 
	 * @uml.property name="weapont"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel weapont = new JDJLabel("Waffe: ");

	/**
	 * 
	 * @uml.property name="armort"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel armort = new JDJLabel("Ruestung: ");

	/**
	 * 
	 * @uml.property name="helmett"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel helmett = new JDJLabel("Helm: ");

	/**
	 * 
	 * @uml.property name="shieldt"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel shieldt = new JDJLabel("Schild: ");

	/**
	 * 
	 * @uml.property name="name"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel name = new JDJLabel();

	/**
	 * 
	 * @uml.property name="level"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel level = new JDJLabel();

	/**
	 * 
	 * @uml.property name="points"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel points = new JDJLabel();

	/**
	 * 
	 * @uml.property name="sign"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel sign = new JDJLabel();

	/**
	 * 
	 * @uml.property name="health"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel health = new JDJLabel();

	/**
	 * 
	 * @uml.property name="dust"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel dust = new JDJLabel();

	/**
	 * 
	 * @uml.property name="dustReg"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel dustReg = new JDJLabel();

	/**
	 * 
	 * @uml.property name="strength"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel strength = new JDJLabel();

	/**
	 * 
	 * @uml.property name="dexterity"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel dexterity = new JDJLabel();

	/**
	 * 
	 * @uml.property name="psycho"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel psycho = new JDJLabel();

	/**
	 * 
	 * @uml.property name="axe"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel axe = new JDJLabel();

	/**
	 * 
	 * @uml.property name="lance"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel lance = new JDJLabel();

	/**
	 * 
	 * @uml.property name="sword"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel sword = new JDJLabel();

	/**
	 * 
	 * @uml.property name="club"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel club = new JDJLabel();

	/**
	 * 
	 * @uml.property name="wolfknife"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel wolfknife = new JDJLabel();

	/**
	 * 
	 * @uml.property name="scout"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel scout = new JDJLabel();

	/**
	 * 
	 * @uml.property name="nature"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel nature = new JDJLabel();

	/**
	 * 
	 * @uml.property name="creature"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel creature = new JDJLabel();

	/**
	 * 
	 * @uml.property name="undead"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel undead = new JDJLabel();

	/**
	 * 
	 * @uml.property name="weaponL"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel weaponL = new JDJLabel();

	/**
	 * 
	 * @uml.property name="armorL"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel armorL = new JDJLabel();

	/**
	 * 
	 * @uml.property name="helmetL"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel helmetL = new JDJLabel();

	/**
	 * 
	 * @uml.property name="shieldL"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	JLabel shieldL = new JDJLabel();

}