package gui;


import item.*;
import item.equipment.Armor;
import item.equipment.Shield;
import item.equipment.weapon.Axe;
import item.equipment.weapon.Club;
import item.equipment.weapon.Lance;
import item.equipment.weapon.Sword;
import item.equipment.weapon.Weapon;
import item.equipment.weapon.Wolfknife;
import item.paper.BookSpell;
import item.paper.Scroll;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

import spell.*;

import figure.Spellbook;
import figure.hero.Hero;
import game.JDEnv;
import gui.mainframe.MainFrame;
import gui.mainframe.component.InfoView;


import java.awt.*;
import java.util.*;
//import javax.swing.text.*;

public class NewHeroView
	extends JDialog
	implements ItemListener, ActionListener, MouseListener {

	//    HP, ST,DX,PY,AX, LZ, SW, KN, WM,NN,KN, UN,SC, DU, SP
	public static final double[] warriorBasic =
		{ 42, 8, 6, 5, 10, 10, 20, 10, 0, 0, 10, 0, 1, 10, 0.2 };
	public static final double[] hunterBasic =
		{ 35, 5, 9, 5, 0, 0, 0, 20, 10, 0, 0, 10, 3, 13, 0.3 };
	public static final double[] druidBasic =
		{ 39, 6, 6, 7, 0, 0, 0, 10, 20, 10, 0, 0, 2, 20, 0.6 };
	public static final double[] mageBasic =
		{ 30, 5, 5, 9, 0, 10, 0, 0, 20, 10, 10, 10, 1, 23, 1.0 };

	boolean thief = false;
	String signString = new String("keins");
	int heroCode = Hero.HEROCODE_WARRIOR;
	String heroName = null;
	int healthVal;

	
	int strengthVal;

	int dexterityVal;
	int psychoVal;

	int axe;
	int lance;
	int sword;
	int club;
	int wolfknife;
	int spellPoints = 1;

	int nature;
	int creature;
	int undead;
	int scout;

	int dust;
	double dustReg;

	InfoView view;


	Hero held = null;

	
	JRadioButton Lion = new JRadioButton("L�we");

	
	JRadioButton Scorpion = new JRadioButton("Skorpion");

	
	JRadioButton Waterman = new JRadioButton("Wassermann");

	
	JRadioButton Bull = new JRadioButton("Stier");

	
	JRadioButton Fisch = new JRadioButton("Fisch");

	
	JRadioButton Waage = new JRadioButton("Waage");

	
	JRadioButton Zwilling = new JRadioButton("Zwilling");

	
	JRadioButton Jungfrau = new JRadioButton("Jungfrau");

	
	JRadioButton Krebs = new JRadioButton("Krebs");


	JRadioButton Schuetze = new JRadioButton("Sch�tze");


	JRadioButton Steinbock = new JRadioButton("Steinbock");

	
	JRadioButton Widder = new JRadioButton("Widder");

	
	JRadioButton Alchemist = new JRadioButton("Alchemist");

	
	JRadioButton Dieb = new JRadioButton("Dieb");

	
	JRadioButton Jaeger = new JRadioButton("Jäger");

	
	JRadioButton Holzfaeller = new JRadioButton("Holzfäller");

	
	JRadioButton Schmied = new JRadioButton("Schmied");


	JRadioButton Haendler = new JRadioButton("Händler");

	
	JRadioButton Magier = new JRadioButton("Zauberer");

	JRadioButton Grossgrundbesitzer = new JRadioButton("Großgrundbesitzer");

	JRadioButton Gelehrter = new JRadioButton("Gelehrter");

	
	JRadioButton Seemann = new JRadioButton("Seemann");

	
	JButton knopf = new JButton("OK");

	JTextField Zeile = new JTextField(20);

	
	JRadioButton Warrior = new JRadioButton("Krieger");

	
	JRadioButton Hunter = new JRadioButton("Dieb");

	
	JRadioButton Druid = new JRadioButton("Druide");

	
	JRadioButton Mage = new JRadioButton("Magier");

	
	JLabel choosen = new JLabel("Noch nichts gew�hlt");

	
	JPanel name = new JPanel();

	
	JPanel attr = new JPanel();

	
	JPanel sign = new JPanel();

	
	JPanel type = new JPanel();

	
	JPanel ok = new JPanel();

	JLabel health; //= new JLabel("Gesundheit:");

	JLabel strength;// = new JLabel("Kraft:");
	JLabel dexterity;// = new JLabel("Geschicklichkeit:    ");

	JLabel psycho;// = new JLabel("Psyche: ");

	JLabel healthValue = new JLabel();

	JLabel strengthValue = new JLabel();

	JLabel dexterityValue = new JLabel();

	JLabel psychoValue = new JLabel();

	ButtonGroup bg3;

	
	JButton generate;// = new JButton("generieren");


	String[] heroNames =
		{ "Bernd", "Harald", "Dieter", "Konstantinius", "Siegfried", "Billi" };

	public NewHeroView(Frame sup) {

		super(sup, "Neuer Held", true);
		
		Warrior = new JRadioButton(JDEnv.getResourceBundle().getString("warrior"));

		
		Hunter = new JRadioButton(JDEnv.getResourceBundle().getString("thief"));

		
		Druid = new JRadioButton(JDEnv.getResourceBundle().getString("druid"));

		
		Mage = new JRadioButton(JDEnv.getResourceBundle().getString("mage"));

		health = new JLabel(JDEnv.getResourceBundle().getString("attr_health")+": ");

		strength = new JLabel(JDEnv.getResourceBundle().getString("attr_strength")+": ");
		dexterity = new JLabel(JDEnv.getResourceBundle().getString("attr_dexterity")+": ");

		psycho = new JLabel(JDEnv.getResourceBundle().getString("attr_psycho")+": ");
		generate = new JButton(JDEnv.getResourceBundle().getString("gui_new_generate"));
		
		 Lion = new JRadioButton(JDEnv.getResourceBundle().getString("sign_leo"));

		
		 Scorpion = new JRadioButton(JDEnv.getResourceBundle().getString("sign_scorpio"));

		
		 Waterman = new JRadioButton(JDEnv.getResourceBundle().getString("sign_aquarius"));

		
		 Bull = new JRadioButton(JDEnv.getResourceBundle().getString("sign_taurus"));

		
		 Fisch = new JRadioButton(JDEnv.getResourceBundle().getString("sign_fish"));

		
		 Waage = new JRadioButton(JDEnv.getResourceBundle().getString("sign_libra"));

		
		 Zwilling = new JRadioButton(JDEnv.getResourceBundle().getString("sign_twin"));

		
		 Jungfrau = new JRadioButton(JDEnv.getResourceBundle().getString("sign_virgo"));

		
		 Krebs = new JRadioButton(JDEnv.getResourceBundle().getString("sign_cancer"));


		 Schuetze = new JRadioButton(JDEnv.getResourceBundle().getString("sign_sagittarius"));


		 Steinbock = new JRadioButton(JDEnv.getResourceBundle().getString("sign_capricorn"));

		
		 Widder = new JRadioButton(JDEnv.getResourceBundle().getString("sign_aries"));

		
		 Alchemist = new JRadioButton(JDEnv.getResourceBundle().getString("craft_alchemist"));

		
		 Dieb = new JRadioButton(JDEnv.getResourceBundle().getString("craft_thief"));

		
		 Jaeger = new JRadioButton(JDEnv.getResourceBundle().getString("craft_hunter"));

		
		 Holzfaeller = new JRadioButton(JDEnv.getResourceBundle().getString("craft_lumberjack"));

		
		 Schmied = new JRadioButton(JDEnv.getResourceBundle().getString("craft_blacksmith"));


		 Haendler = new JRadioButton(JDEnv.getResourceBundle().getString("craft_trader"));

		
		Magier = new JRadioButton(JDEnv.getResourceBundle().getString("craft_sorcerer"));

		 Grossgrundbesitzer = new JRadioButton(JDEnv.getResourceBundle().getString("craft_nobleman"));

		Gelehrter = new JRadioButton(JDEnv.getResourceBundle().getString("craft_bookman"));

		
		Seemann = new JRadioButton(JDEnv.getResourceBundle().getString("craft_sailor"));
		
		
//		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//		addWindowListener(new WindowAdapter() {
//			/**
//			  * Die Methode windowClosing gibt an, was passiert wenn "this"
//			ge-
//			  * geschlossen wird
//			*
//				* @param windowEvent Datentyp WindowEvent
//			  */
//			public void windowClosing(WindowEvent windowEvent) {
//				System.out.println("Mache closing adapter!");
//				int end =
//			JOptionPane.showConfirmDialog(
//				getContentPane(),
//				"Jetzt gibt es keine zur�ck mehr!",
//				"Spiel muss gestartet werden.",
//				JOptionPane.OK_OPTION,
//				JOptionPane.INFORMATION_MESSAGE);
//		if (end == 0) {
//				
//		}
//				
//			}
//		});
		int k = (int) (Math.random() * heroNames.length);
		Zeile.setText(heroNames[k]);
		//Zeile.addActionListener(this);
		Zeile.setSelectionStart(0);
		Zeile.setSelectionEnd(Zeile.getText().length());
		name.setBorder(new TitledBorder("Name"));
		type.setBorder(new TitledBorder(JDEnv.getResourceBundle().getString("type")));
		attr.setBorder(new TitledBorder(JDEnv.getResourceBundle().getString("attributes")));
		JPanel gilde = new JPanel();
		gilde.setBorder(new TitledBorder(JDEnv.getResourceBundle().getString("profession")));

		attr.setLayout(new GridLayout(1, 2));
		JPanel attr1 = new JPanel();
		GridLayout grid2 = new GridLayout(4, 1);
		attr1.setLayout(grid2);
		JPanel attr2 = new JPanel();

		JPanel info = new JPanel();
		info.setBorder(new TitledBorder("Info"));
		view = new InfoView(250, 120, false,null);
		info.add(view);

		attr2.setLayout(grid2);
		attr.add(attr1);
		attr.add(attr2);

		sign.setBorder(new TitledBorder(JDEnv.getResourceBundle().getString("sign")));

		attr1.add(health);
		attr2.add(healthValue);
		attr1.add(strength);
		attr2.add(strengthValue);
		attr1.add(dexterity);
		attr2.add(dexterityValue);
		attr1.add(psycho);
		attr2.add(psychoValue);

		Warrior.setSelected(true);

		JDesktopPane cp = new JDesktopPane();
		setContentPane(cp);
		GridLayout grid = new GridLayout(2, 3);
		cp.setLayout(grid);
		cp.setDesktopManager(new DefaultDesktopManager());

		ButtonGroup bg = new ButtonGroup();
		Box box = Box.createVerticalBox();

		Warrior.addItemListener(this);
		Hunter.addItemListener(this);
		Druid.addItemListener(this);
		Mage.addItemListener(this);
		Warrior.addMouseListener(this);
		Hunter.addMouseListener(this);
		Druid.addMouseListener(this);
		Mage.addMouseListener(this);
		bg.add(Warrior);
		bg.add(Hunter);
		bg.add(Druid);
		bg.add(Mage);
		box.add(Warrior);
		box.add(Hunter);
		box.add(Druid);
		box.add(Mage);

		cp.add(type);
		cp.add(attr);
		cp.add(sign);
		cp.add(gilde);
		cp.add(info);
		cp.add(name);
		name.setLayout(new BorderLayout());

		JPanel nameP = new JPanel();
		nameP.add(Zeile);
		JPanel knopfP = new JPanel();
		knopfP.add(knopf);
		name.add(nameP, BorderLayout.CENTER);
		name.add(knopfP, BorderLayout.SOUTH);
		type.add(box);
		type.add(generate);
		generate.addActionListener(this);

		ButtonGroup bg2 = new ButtonGroup();
		Box box2a = Box.createVerticalBox();
		Box box2b = Box.createVerticalBox();
		Box box2c = Box.createVerticalBox();

		Bull.addItemListener(this);
		Bull.addMouseListener(this);
		bg2.add(Bull);
		box2a.add(Bull);
		Waterman.addItemListener(this);
		Waterman.addMouseListener(this);
		bg2.add(Waterman);
		box2a.add(Waterman);
		Scorpion.addItemListener(this);
		Scorpion.addMouseListener(this);
		bg2.add(Scorpion);
		box2a.add(Scorpion);
		Lion.addItemListener(this);
		Lion.addMouseListener(this);
		bg2.add(Lion);
		box2a.add(Lion);
		Krebs.addItemListener(this);
		Krebs.addMouseListener(this);
		bg2.add(Krebs);
		box2b.add(Krebs);
		Schuetze.addItemListener(this);
		Schuetze.addMouseListener(this);
		bg2.add(Schuetze);
		box2b.add(Schuetze);
		Waage.addItemListener(this);
		Waage.addMouseListener(this);
		bg2.add(Waage);
		box2b.add(Waage);
		Jungfrau.addItemListener(this);
		Jungfrau.addMouseListener(this);
		bg2.add(Jungfrau);
		box2b.add(Jungfrau);
		Steinbock.addItemListener(this);
		Steinbock.addMouseListener(this);
		bg2.add(Steinbock);
		box2c.add(Steinbock);
		Widder.addItemListener(this);
		Widder.addMouseListener(this);
		bg2.add(Widder);
		box2c.add(Widder);
		Fisch.addItemListener(this);
		Fisch.addMouseListener(this);
		bg2.add(Fisch);
		box2c.add(Fisch);
		Zwilling.addItemListener(this);
		Zwilling.addMouseListener(this);
		bg2.add(Zwilling);
		box2c.add(Zwilling);
		sign.add(box2a);
		sign.add(box2b);
		sign.add(box2c);

		bg3 = new ButtonGroup();
		Box box3a = Box.createVerticalBox();
		Box box3b = Box.createVerticalBox();

		Magier.addItemListener(this);
		Magier.addMouseListener(this);
		bg3.add(Magier);
		box3a.add(Magier);
		Jaeger.addItemListener(this);
		Jaeger.addMouseListener(this);
		bg3.add(Jaeger);
		box3a.add(Jaeger);
		Haendler.addItemListener(this);
		Haendler.addMouseListener(this);
		bg3.add(Haendler);
		box3a.add(Haendler);
		Holzfaeller.addItemListener(this);
		Holzfaeller.addMouseListener(this);
		bg3.add(Holzfaeller);
		box3a.add(Holzfaeller);
		Grossgrundbesitzer.addItemListener(this);
		Grossgrundbesitzer.addMouseListener(this);
		bg3.add(Grossgrundbesitzer);
		box3a.add(Grossgrundbesitzer);
		Schmied.addItemListener(this);
		Schmied.addMouseListener(this);
		bg3.add(Schmied);
		box3b.add(Schmied);
		Alchemist.addItemListener(this);
		Alchemist.addMouseListener(this);
		bg3.add(Alchemist);
		box3b.add(Alchemist);
		Seemann.addItemListener(this);
		Seemann.addMouseListener(this);
		bg3.add(Seemann);
		box3b.add(Seemann);
		Dieb.addItemListener(this);
		Dieb.addMouseListener(this);
		bg3.add(Dieb);
		box3b.add(Dieb);
		Gelehrter.addItemListener(this);
		Gelehrter.addMouseListener(this);
		bg3.add(Gelehrter);
		box3b.add(Gelehrter);

		gilde.add(box3a);
		gilde.add(box3b);


		knopf.addActionListener(this);
		Zeile.addActionListener(this);
		knopf.addMouseListener(this);
		Zeile.addMouseListener(this);
		Zeile.grabFocus();
		knopf.setPreferredSize(new Dimension(90,55));
		
		generateAttr(heroCode);
		
		Zeile.setSelectionStart(0);
		Zeile.setSelectionEnd(Zeile.getText().length());
		//this.setLocation(300,0);
		this.setSize(900, 350);
		positionieren();
		this.setVisible(true);
		this.pack();

	}

	
	public void generateAttr(int type) {
		//		int bonus1a = 5; //basisbonus
		//		int bonus1b = Math.round(split.about_value(bonus1a));
		//		int[] bonus1c = split.split_equal(bonus1b, 3);
		//
		//		int a = 4;
		//		float b = split.about_value(a);
		//		float c = (a / b);
		//		if (c < 1)
		//			c = (-1 / c);
		//		int bonus2 = Math.round(c * 2);

		switch (type) {
			case Hero.HEROCODE_WARRIOR :
				healthVal = (int) warriorBasic[0];
				strengthVal = (int) warriorBasic[1];
				dexterityVal = (int) warriorBasic[2];
				psychoVal = (int) warriorBasic[3];
				break;
			case Hero.HEROCODE_HUNTER :
				healthVal = (int) hunterBasic[0];
				strengthVal = (int) hunterBasic[1];
				dexterityVal = (int) hunterBasic[2];
				psychoVal = (int) hunterBasic[3];
				break;
			case Hero.HEROCODE_DRUID :
				healthVal = (int) druidBasic[0];
				strengthVal = (int) druidBasic[1];
				dexterityVal = (int) druidBasic[2];
				psychoVal = (int) druidBasic[3];
				break;
			case Hero.HEROCODE_MAGE :
				healthVal = (int) mageBasic[0];
				strengthVal = (int) mageBasic[1];
				dexterityVal = (int) mageBasic[2];
				psychoVal = (int) mageBasic[3];
				break;
			default : {
				//System.out.println("heroCode Error!");
			}
		}
		int additionalPoints = 5;
		//System.out.println("vor schleife");
		for (int i = 0; i < additionalPoints; i++) {
			//System.out.println("schleife");
			int k = (int) (Math.random() * 3);
			if (k == 0) {
				strengthVal++;
				//System.out.println("Strength++!");
			}
			if (k == 1) {
				dexterityVal++;
			}
			if (k == 2) {
				psychoVal++;
			}

		}

		healthValue.setText(Integer.toString(healthVal));
		strengthValue.setText(Integer.toString(strengthVal));
		dexterityValue.setText(Integer.toString(dexterityVal));
		psychoValue.setText(Integer.toString(psychoVal));

	}


	public void itemStateChanged(ItemEvent ie) {

		Object quelle = ie.getSource();
		if (quelle == Warrior) {
			heroCode = Hero.HEROCODE_WARRIOR;
			generateAttr(heroCode);
		}
		if (quelle == Hunter) {
			heroCode = Hero.HEROCODE_HUNTER;
			generateAttr(heroCode);
		}
		if (quelle == Druid) {
			heroCode = Hero.HEROCODE_DRUID;
			generateAttr(heroCode);
		}
		if (quelle == Mage) {
			heroCode = Hero.HEROCODE_MAGE;
			generateAttr(heroCode);
		}

		if (quelle == Bull) {
			signString = ("Stier ");
		}
		if (quelle == Waterman) {
			signString = ("Wassermann ");
		}
		if (quelle == Scorpion) {
			signString = ("Skorpion ");
		}
		if (quelle == Lion) {
			signString = ("Löwe ");
		}
		if (quelle == Krebs) {
			signString = ("Krebs ");
		}
		if (quelle == Schuetze) {
			signString = ("Schütze ");
		}
		if (quelle == Waage) {
			signString = ("Waage ");
		}
		if (quelle == Jungfrau) {
			signString = ("Jungfrau ");
		}
		if (quelle == Steinbock) {
			signString = ("Steinbock ");
		}
		if (quelle == Widder) {
			signString = ("Widder ");
		}
		if (quelle == Fisch) {
			signString = ("Fisch ");
		}
		if (quelle == Zwilling) {
			signString = ("Zwilling ");
		}

	}

	public void actionPerformed(ActionEvent ae) {
		Object quelle = ae.getSource();
		Item jungfrauWeapon = null;
		if (quelle == generate) {
			//System.out.println("generieren!");
			generateAttr(heroCode);
		}

		if (quelle == knopf || quelle == Zeile) {
			if (!Zeile.getText().equals("")) {
				heroName = MainFrame.clearString(Zeile.getText());
				//System.out.println(Zeile.getText());

				switch (heroCode) {
					case 1 :
						axe = (int) warriorBasic[4];
						lance = (int) warriorBasic[5];
						sword = (int) warriorBasic[6];
						club = (int) warriorBasic[7];
						wolfknife = (int) warriorBasic[8];
						nature = (int) warriorBasic[9];
						creature = (int) warriorBasic[10];
						undead = (int) warriorBasic[11];
						scout = (int) warriorBasic[12];
						dust = (int) warriorBasic[13];
						dustReg = warriorBasic[14];
						
						break;

					case 2 :
						axe = (int) hunterBasic[4];
						lance = (int) hunterBasic[5];
						sword = (int) hunterBasic[6];
						club = (int) hunterBasic[7];
						wolfknife = (int) hunterBasic[8];
						nature = (int) hunterBasic[9];
						creature = (int) hunterBasic[10];
						undead = (int) hunterBasic[11];
						scout = (int) hunterBasic[12];
						dust = (int) hunterBasic[13];
						dustReg = hunterBasic[14];
						break;
					case 3 :
						axe = (int) druidBasic[4];
						lance = (int) druidBasic[5];
						sword = (int) druidBasic[6];
						club = (int) druidBasic[7];
						wolfknife = (int) druidBasic[8];
						nature = (int) druidBasic[9];
						creature = (int) druidBasic[10];
						undead = (int) druidBasic[11];
						scout = (int) druidBasic[12];
						dust = (int) druidBasic[13];
						dustReg = druidBasic[14];
						break;
					case 4 :
						axe = (int) mageBasic[4];
						lance = (int) mageBasic[5];
						sword = (int) mageBasic[6];
						club = (int) mageBasic[7];
						wolfknife = (int) mageBasic[8];
						nature = (int) mageBasic[9];
						creature = (int) mageBasic[10];
						undead = (int) mageBasic[11];
						scout = (int) mageBasic[12];
						dust = (int) mageBasic[13];
						dustReg = mageBasic[14];
						break;
					default :
						//System.out.println("heroCode Error!");
				}
				Item i = null;
				LinkedList itemsToAdd = new LinkedList();
				
				Spellbook book = new Spellbook();
				if (signString == "Stier ") {
					healthVal = healthVal + 6;
					strengthVal += 1;
				}
				if (signString == "Wassermann ") {
					for (int j = 0; j < 2; j++) {
						int k = (int) (Math.random() * 3);
						if (k == 0) {
							nature += 7 + (int)(Math.random()*8);
						}
						if (k == 1) {
							creature += 7 + (int)(Math.random()*8);
						}
						if (k == 2) {
							undead += 7 + (int)(Math.random()*8);
						}

					};
				}
				if (signString == "Skorpion ") {
					lance += 10;
					itemsToAdd.add(new Lance(20, false));
				}
				if (signString == "L�we ") {
					psychoVal += 2;
				}
				if (signString == "Krebs ") {
					itemsToAdd.add(new Axe(20, false));
					axe += 10;
				}
				if (signString == "Sch�tze ") {
					dexterityVal++;
					for (int j = 0; j < 2; j++) {
						int k = (int) (Math.random() * 4);
						if (k == 0) {
							axe += 10;
						}
						if (k == 1) {
							club += 10;
						}
						if (k == 2) {
							lance += 10 ;
						}
						if (k == 3) {
							sword += 10;
						}
						if (k == 4) {
							wolfknife += 10 ;
						}

					};
				}
				if (signString == "Waage ") {
					incMinAttr();
					healthVal += 3;
					dust += 3;
					itemsToAdd.add(new HealPotion(25));
				}
				if (signString == "Jungfrau ") {
					itemsToAdd.add(new Wolfknife(20, false));
					wolfknife += 10;
				}
				if (signString == "Steinbock ") {
					scout += 2;
				}
				if (signString == "Widder ") {
					itemsToAdd.add(new Club(20, false));
					club += 10;
				}
				if (signString == "Fisch ") {
					itemsToAdd.add(new Shield(30, false));
					dexterityVal += 1;
				}
				if (signString == "Zwilling ") {
					itemsToAdd.add(new HealPotion(25));
					itemsToAdd.add(new HealPotion(25));
					Spell s1 = new Heal(1);
					BookSpell heal_book = new BookSpell(s1, 4);
					itemsToAdd.add(heal_book);
					//spell s2 = new heal_spell(2);
					Spell s3 = new Escape(1);
					Spell s4 = new GoldenHit(1);
					//scroll scroll2 = new scroll(s2, 8);
					Scroll scroll3 = new Scroll(s3, 4);
					Scroll scroll4 = new Scroll(s4, 3);
					//itemsToAdd.add(scroll2);
					itemsToAdd.add(scroll3);
					itemsToAdd.add(scroll4);
				}

				if (Holzfaeller.isSelected()) {
					axe += 10;
					strengthVal += 1;
				} else if (Haendler.isSelected()) {
					////System.out.println("mache h�ndler");
					Item unique = ItemPool.getUnique(40,0);
					////System.out.println(unique.toString());
					itemsToAdd.add(unique);
				} else if (Grossgrundbesitzer.isSelected()) {
					itemsToAdd.add(new Armor(40, false));
				} else if (Dieb.isSelected()) {
					if(heroCode != Hero.HEROCODE_HUNTER) {
						book.addSpell(new Search(1));
						thief = true;
					}

				} else if (Jaeger.isSelected()) {

				} else if (Schmied.isSelected()) {

				} else if (Gelehrter.isSelected()) {
					spellPoints += 2;
				} else if (Alchemist.isSelected()) {

				} else if (Magier.isSelected()) {
					dustReg += 0.3;
					dust += 4;

				} else if (Seemann.isSelected()) {
					healthVal += 8;
				}
				
				

				held =
					new Hero(
						heroName,
						heroCode,
						signString,
						healthVal,
						strengthVal,
						dexterityVal,
						psychoVal,
						axe,
						lance,
						sword,
						club,
						wolfknife,
						nature,
						creature,
						undead,
						scout,
						dust,
						dustReg,
						0);

				
//				if (heroCode == Hero.HEROCODE_WARRIOR) {
//					book.addSpell(new GoldenHit(1));
//					//book.addSpell(new KeyLocator(1));
//				}
//				if (heroCode == Hero.HEROCODE_HUNTER) {
//					//book.addSpell(new key_locator_spell(1));
//					book.addSpell(new Search(1));
//					//book.addSpell(new steal_spell(1));
//				}
//				if (heroCode == Hero.HEROCODE_DRUID) {
//					//book.addSpell(new heal_spell(1,4,8,6,35));
//					book.addSpell(new Heal(1,8,4,6,35));
//				}
//				if (heroCode == Hero.HEROCODE_MAGE) {
//					//book.addSpell(new light_spell(1));
//					book.addSpell(new Fireball(1,9,6,5,12));
//				}
				book.addSpell(new Threat());
				held.setSpellbook(book);
				Armor hemd = new Armor(20, false);
				held.getCharacter().setSpellPoints(spellPoints);
				Weapon waffe;
				if(thief) {
					held.setThief(true);	
				}
				switch (held.getHeroCode()) {
					case 1 :
						waffe = new Sword(25, false);
						break;
					case 2 :
						waffe = new Club(25, false);
						held.setThief(true);
						break;
					case 3 :
						waffe = new Wolfknife(25, false);
						Spell s = new Bonebreaker(1);
						Scroll scroll1 = new Scroll(s, 5);
						Scroll scroll2 = new Scroll(s, 5);
						Scroll scroll3 = new Scroll(s, 5);
						held.takeItem(scroll1, null);
						held.takeItem(scroll2, null);
						held.takeItem(scroll3, null);
						break;
					case 4 :
						waffe = new Lance(25, false);
						break;
					default :
						waffe = new Sword(100, false);
						//System.out.println("HeroCode Error!");
				}
				held.getInventory().take_weapon1(waffe);
				
				//
				//held.getInventory().takeItem(itemPool.getGoodItem(120,1.6),null);
				//
				
				if (!Grossgrundbesitzer.isSelected()) {
					held.getInventory().takeItem(hemd, null);
				}
				for (int j = 0; j < itemsToAdd.size(); j++) {

					Item it = (Item) itemsToAdd.get(j);
					held.getInventory().takeItem(it, null);
				}

				this.setVisible(false);

			}
		}
		//if (quelle == Zeile)
		//	heroName = Zeile.getText();
	}


	public Hero getHero() {

		return held;
	}

	private void incMinAttr() {
		if ((strengthVal <= dexterityVal) && (strengthVal <= psychoVal))
			strengthVal++;
		else if ((dexterityVal <= strengthVal) && (dexterityVal <= psychoVal))
			dexterityVal++;
		else
			psychoVal++;
		healthVal += 5;
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

	public void mouseClicked(MouseEvent me) {

	}

	public void mouseEntered(MouseEvent me) {
		Object o = me.getSource();
		//String text[] = new String[1];
		Paragraph[] p = new Paragraph[1];
		String s = new String();
		if (o == Lion) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_leo");

		} else if (o == Scorpion) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_scorpio");
		} else if (o == Fisch) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_fish");
		} else if (o == Schuetze) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_sagittarius");
		} else if (o == Widder) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_aries");

		} else if (o == Steinbock) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_capricorn");
		} else if (o == Zwilling) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_twin");
		} else if (o == Waterman) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_aquarius");
		} else if (o == Jungfrau) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_virgo");

		} else if (o == Krebs) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_cancer");
		} else if (o == Bull) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_taurus");
		} else if (o == Waage) {
			s =
				JDEnv.getResourceBundle().getString("sign_text_libra");
		} else if (o == Hunter) {
			s =
				JDEnv.getResourceBundle().getString("thief_text");
		} else if (o == Druid) {
			s =
				JDEnv.getResourceBundle().getString("druid_text");
		} else if (o == Mage) {
			s =
				JDEnv.getResourceBundle().getString("mage_text");
		} else if (o == Warrior) {
			s =
				JDEnv.getResourceBundle().getString("warrior_text");
		} else if (o == Seemann) {
			s =
				JDEnv.getResourceBundle().getString("craft_text_sailor");
		} else if (o == Haendler) {
			s = JDEnv.getResourceBundle().getString("craft_text_not_available");// Die H�ndler lieben wertvolle und seltene Gegent�nde";
		} else if (o == Holzfaeller) {
			s =
				JDEnv.getResourceBundle().getString("craft_text_lumberjack");
		} else if (o == Grossgrundbesitzer) {
			s =
				JDEnv.getResourceBundle().getString("craft_text_nobleman");
		} else if (o == Dieb) {
			s =
				JDEnv.getResourceBundle().getString("craft_text_thief");
		} else if (o == Alchemist) {
			s =
				JDEnv.getResourceBundle().getString("craft_text_not_available"); //Alchemisten haben einen Faible f�r Tr�nke und Elexire aller Art.";
		} else if (o == Magier) {
			s = JDEnv.getResourceBundle().getString("craft_text_sorcerer");
		} else if (o == Gelehrter) {
			s =
				JDEnv.getResourceBundle().getString("craft_text_bookman");
		} else if (o == Jaeger) {
			s =
				JDEnv.getResourceBundle().getString("craft_text_not_available"); // Bringen alle Fertigkeiten mit, die man haben muss um allein in der Wildniss zurecht zu kommen.";
		} else if (o == Schmied) {
			s =
				JDEnv.getResourceBundle().getString("craft_text_not_available"); // Die Schmiede verstehen sich auf das reparieren von Waffen und R�stungen.";
		} else if (o == knopf) {
			s = JDEnv.getResourceBundle().getString("click_to_enter");
		} else if (o == Zeile) {
			s = JDEnv.getResourceBundle().getString("enter_hero_name");
		}
		p[0] = new Paragraph(s);
		view.setText2(p);

	}
	public void mouseExited(MouseEvent me) {
		view.resetText2();
	}

	public void mousePressed(MouseEvent me) {

	}

	public void mouseReleased(MouseEvent me) {

	}
}
