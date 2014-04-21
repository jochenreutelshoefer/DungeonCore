package gui.mainframe;

import figure.FigureInfo;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;
import game.JDEnv;
import gui.JDBackgroundPanel;
import gui.JDGUISwing;
import gui.JDJPanel;
import gui.JDJRadioButton;
import gui.JPanelNoRepaint;
import gui.MyJDGui;
import gui.Paragraph;
import gui.StartView;
import gui.Texts;
import gui.engine2D.AWTImageLoader;
import gui.mainframe.component.BoardView;
import gui.mainframe.component.CharacterView;
import gui.mainframe.component.DustView;
import gui.mainframe.component.HealthView;
import gui.mainframe.component.InfoView;
import gui.mainframe.component.InventoryView;
import gui.mainframe.component.ShowPanel;
import gui.mainframe.component.SpellsView;
import gui.mainframe.component.TextView;
import gui.mainframe.dialog.FtpSendView;
import gui.mainframe.dialog.WaitView;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import test.Logger;
import control.MainFrameI;
import dungeon.JDPoint;

//import javax.swing.text.*;

/**
 * @author Duke1
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class MainFrame extends JFrame implements ActionListener, ItemListener,
		MouseListener, ChangeListener, MainFrameI {

	public static final int UPDATE_ALL = 0;

	public static final int UPDATE_ITEM = 1;

	public static final int UPDATE_FIGHT = 2;

	public static final int UPDATE_FIELD = 3;

	public static final int UPDATE_VIEW = 4;

	public static final int UPDATE_DUST = 5;

	public static final int UPDATE_HEALTH = 6;

	public static final int UPDATE_CHARACTER = 7;

	public static final int UPDATE_INVENTORY = 8;

	public static final int UPDATE_SPELLS = 9;

	Container cp1;

	JDBackgroundPanel cp;

	int choosenEnemy = 0;

	boolean noControl = false;

	TextView verlauf;

	HealthView gesundheit;

	DustView staub;

	InfoView text;

	SpellsView zauberP;

	BoardView spielfeld;

	TextView kampfVerlauf;

	public TextView getKampfVerlauf() {
		return kampfVerlauf;
	}

	CharacterView character;

	InventoryView inventory;

	JPanel fightPanel;

	JTabbedPane northEast = new JTabbedPane();

	ShowPanel amatur;

	JPanel east;

	JButton threat = new JButton("Drohen");

	JButton slap = new JButton("Zuschlagen");

	JButton fleeNorth = new JButton("Norden");

	JButton fleeEast = new JButton("Osten");

	JButton fleeSouth = new JButton("Süden");

	JButton fleeWest = new JButton("Westen");

	JRadioButton[] Benemy = new JDJRadioButton[4];

	ButtonGroup bgenemy = new ButtonGroup();

	Box enemyBox = Box.createVerticalBox();

	Logger log;

	boolean logging = false;

	String playerName;

	boolean appletRunning;

	private boolean soundEffects = true;
	private boolean autoZoom = false;

	public boolean isSoundEffects() {
		return soundEffects;
	}

	public boolean isAutoZoom() {
		return autoZoom;
	}

	Applet applet;

	private final JDGUISwing gui;

	public static Font ButtonFont;

	public static AWTImageLoader imageSource;

	public MainFrame(StartView listenerx, String name, Applet a,
			JDGUISwing gui,
			String title) {

		super(title);
		this.gui = gui;

		if (listenerx != null) {
			this.autoZoom = listenerx.autoZoom();
			this.soundEffects = listenerx.soundEffects();
		}

		east = new JPanel();
		east.setBackground(JDJPanel.bgColor);

		ButtonFont = new Font("Arial", Font.BOLD, 17);
		amatur = new ShowPanel(gui);
		playerName = name;
		this.appletRunning = a != null;
		applet = a;
		imageSource = new AWTImageLoader(a);
		final StartView listener = listenerx;
		for (int i = 0; i < 4; i++) {
			Benemy[i] = new JDJRadioButton("Kein Feind");
			bgenemy.add(Benemy[i]);
			enemyBox.add(Benemy[i]);
			Benemy[i].addItemListener(this);
			Benemy[i].addMouseListener(this);

		}

		fleeNorth.addActionListener(this);
		fleeSouth.addActionListener(this);
		fleeEast.addActionListener(this);
		fleeWest.addActionListener(this);
		slap.addActionListener(this);
		threat.addActionListener(this);

		fleeNorth.setBackground(JDJPanel.bgColor);
		fleeSouth.setBackground(JDJPanel.bgColor);
		fleeEast.setBackground(JDJPanel.bgColor);
		fleeWest.setBackground(JDJPanel.bgColor);
		slap.setBackground(JDJPanel.bgColor);
		threat.setBackground(JDJPanel.bgColor);

		cp1 = this.getContentPane();

		cp = new JDBackgroundPanel();
		BorderLayout b = new BorderLayout();

		cp.setLayout(b);
		cp1.add(cp);
		if (logging) {
			log = new Logger("jd.log");
		}

		character = new CharacterView(gui);

		spielfeld = new BoardView(applet, gui);

		cp.add(spielfeld, BorderLayout.CENTER);

		verlauf = new TextView(380, 250, gui);

		east.setLayout(new BorderLayout());
		east.add(verlauf, BorderLayout.NORTH);
		amatur.setSize(100, 20);
		east.add(amatur, BorderLayout.CENTER);

		/*
		 * South panel
		 */
		JDJPanel southPanel = new JDJPanel(gui);
		BorderLayout fl = new BorderLayout();
		southPanel.setLayout(fl);
		southPanel.setBorder(new EmptyBorder(0, 50, 0, 50));
		gesundheit = new HealthView(this, gui);
		text = new InfoView(gui, this);
		staub = new DustView(gui);
		southPanel.add(gesundheit, BorderLayout.WEST);
		southPanel.add(text, BorderLayout.CENTER);
		southPanel.add(staub, BorderLayout.EAST);

		fightPanel = new JPanelNoRepaint();
		fightPanel.setBackground(JDJPanel.bgColor);
		fightPanel.setLayout(new BorderLayout());
		JDJPanel fightButtonP = new JDJPanel(gui);
		JDJPanel centerPanel = new JDJPanel(gui);
		centerPanel.setLayout(new GridLayout(2, 1));
		fightButtonP.setLayout(new BorderLayout());
		fightButtonP.add(centerPanel, BorderLayout.CENTER);
		centerPanel.add(slap);
		centerPanel.add(threat);
		fightButtonP.add(fleeNorth, BorderLayout.NORTH);
		fightButtonP.add(fleeEast, BorderLayout.EAST);
		fightButtonP.add(fleeSouth, BorderLayout.SOUTH);
		fightButtonP.add(fleeWest, BorderLayout.WEST);

		inventory = new InventoryView(gui);
		zauberP = new SpellsView(gui);
		kampfVerlauf = new TextView(380, 250, gui);

		JDJPanel enemyPanel = new JDJPanel(gui);
		enemyPanel.add(enemyBox);
		fightPanel.add(kampfVerlauf, BorderLayout.NORTH);

		northEast.setOpaque(true);
		northEast.addChangeListener(this);

		northEast.addTab(JDEnv.getResourceBundle().getString("movement"), east);
		east.setOpaque(true);
		northEast.addTab(JDEnv.getResourceBundle().getString("fight"),
				fightPanel);
		fightPanel.setOpaque(true);
		northEast.addTab("Char", character);
		character.setOpaque(true);
		northEast.addTab(JDEnv.getResourceBundle().getString("gui_inventory"),
				inventory);
		inventory.setOpaque(true);
		northEast.addTab(JDEnv.getResourceBundle().getString("spelling"),
				zauberP);
		zauberP.setOpaque(true);
		northEast.setBackground(JDJPanel.bgColor);
		northEast.setOpaque(true);
		northEast.setPreferredSize(new Dimension(390, 340));

		cp.add(northEast, BorderLayout.EAST);

		cp.add(southPanel, BorderLayout.SOUTH);

		this.setSize(1280, 780);
		this.setResizable(false);
		this.positionieren();

		final MainFrame mf = this;

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			/**
			 * Die Methode windowClosing gibt an, was passiert wenn "this" ge-
			 * geschlossen wird
			 * 
			 * @param windowEvent
			 *            Datentyp WindowEvent
			 */
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				int end = JOptionPane.showConfirmDialog(cp, JDEnv
						.getResourceBundle().getString("gui_really_quit_game"),
						JDEnv.getResourceBundle().getString("gui_quit_game"),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (end == 0) {

					sendScoreData(listener.registeredPlayer(),
							listener.ligaGame());
					dispose();
					signOff();
					listener.setVisible(true);

				}

			}
		});

	}

	private void signOff() {
		gui.getFigure().controlLeaves();
	}

	public void disableControl() {
		noControl = true;
	}

	public void sendScoreData(boolean reg, boolean liga) {
		FtpSendView sendWin = new FtpSendView(this);
		String s = sendWin.getText();
		if (s == null || s.length() == 0) {
			s = new String("-");
		}
		WaitView waitWin = new WaitView();

		try {

			String url = "http://denkbares.dyndns.org/DenkDungeon"
					+ "/PutData.jsp" + // file
					"?action=newEntry";

			// String url = "http://localhost:8080" + "/PutData.jsp" + // file
			// "?action=newEntry";

			Map dataMap = makeInfoString(s, reg, liga);// + dateString;
			if (dataMap == null) {
				sendWin.dispose();
				waitWin.dispose();
				return;
			}

			for (Iterator iter = dataMap.keySet().iterator(); iter.hasNext();) {
				Object key = iter.next();
				Object value = dataMap.get(key);
				String encodedValue = java.net.URLEncoder.encode(value
						.toString());
				url += "&" + key + "=" + encodedValue;
			}

			// System.out.println(url);
			URL jd = new URL(url);
			System.out.println("sending score to server: "
					+ jd.toExternalForm());
			URLConnection uc = jd.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));

			in.close();

		} catch (Exception e) { // at least: it's not Throwable ;-)
			// System.out.println(e.getMessage());
		}
		sendWin.dispose();
		waitWin.dispose();
	}

	public static String clearString(String s) {
		int k = s.indexOf("&&&");

		while (k != -1) {
			// System.out.println("String: "+s);
			String s1 = s.substring(0, k);
			// System.out.println(s1);

			int a = k + 4;
			// System.out.println(a);
			int e = s.length();
			// System.out.println("length: "+ e);
			String s2 = s.substring(k + 3, s.length());
			// System.out.println(s2);
			s = s1 + s2;
			k = s.indexOf("&&&");
			// System.out.println("k: "+k);
		}

		k = s.indexOf("$$$");
		System.out.println("k: " + k);
		while (k != -1) {
			// System.out.println("String: "+s);
			s = s.substring(0, k) + s.substring(k + 3, s.length());
			k = s.indexOf("$$$");
			// System.out.println("k: "+k);
		}

		return s;

	}

	// public static void sendScoreData(String string) {
	//
	// try {
	// String url = "http://jd.jupi.info" +
	// "/highscores.php" + //file
	// "?action=newEntry" + //action
	// "&verbose=on" + //output?
	// "&string="; // String to be added URL-encoded later
	//
	// // The string what all is about... (with linebreak)
	//
	//
	// // add URL-encoded string to url
	// url += java.net.URLEncoder.encode(string);
	//
	// URL jd = new URL(url);
	// URLConnection uc = jd.openConnection();
	//
	// BufferedReader in = new BufferedReader(
	// new InputStreamReader(uc.getInputStream()));
	//
	// //System.out.println("Sende String: "+string);
	//
	// // Read the output (response) to StdOut
	// // String inputLine;
	// // while ((inputLine = in.readLine()) != null){
	// // //System.out.println(inputLine);
	// // }
	//
	// in.close();
	//
	// } catch (Exception e) { // at least: it's not Throwable ;-)
	// //System.out.println(e.getMessage());
	// }
	//
	// }

	private Map makeInfoString(String comment, boolean reg, boolean liga) {
		return ((MyJDGui) gui).getHighScoreString(this.playerName, comment,
				reg, liga);

	}

	public boolean isAppletRunning() {
		return appletRunning;
	}

	public void setSelectedEnemy(int a) {
		this.Benemy[a].setSelected(true);
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		Object quelle = ie.getSource();
		if (quelle == Benemy[0]) {
			choosenEnemy = 0;
		} else if (quelle == Benemy[1]) {
			choosenEnemy = 1;
		} else if (quelle == Benemy[2]) {
			choosenEnemy = 2;
		} else if (quelle == Benemy[3]) {
			choosenEnemy = 3;
		}

	}

	// public void setGame(Game game) {
	// dasSpiel = game;
	// }
	//
	// public Game getGame() {
	// return dasSpiel;
	// }

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object quelle = ae.getSource();

		if (noControl) {
			quelle = null;
		}

		if (quelle == slap) {

			if (gui.getFigure().getRoomInfo().fightRunning().booleanValue()) {

				gui.getControl().wannaAttack(choosenEnemy);

			} else {
				newStatement("...ja, aber wen?", 0);
			}
		}

	}

	public void initMainframe() {

		newStatement(Texts.begin(), 0);

		updateGUI2(this.UPDATE_ALL, false);

		this.repaint();
		getSpielfeld().getSpielfeldBild().setGameStarted();
		text.setText(new Paragraph[0]);
		this.setVisible(true);

	}

	public void newStatement(String s, int code) {
		if (gui.getFigure().getRoomInfo().fightRunning().booleanValue()) {
			kampfVerlauf.newStatement(s, code);
		} else {
			verlauf.newStatement(s, code);
		}
	}

	public void newStatement(String s, int code, int to) {
		if (to == 0) {
			kampfVerlauf.newStatement(s, code);
		} else if (to == 1) {
			verlauf.newStatement(s, code);
		} else {
			kampfVerlauf.newStatement(s, code);
			verlauf.newStatement(s, code);
		}
	}

	public void log(String text, int align) {
		if (logging) {
			if (align == 10) {
				log.addTextLeft(text);
			} else if (align == 20) {
				log.addTextRight(text);
			} else if (align == -1) {
				int k = log.getTabs();
				log.addText(text, k);
			} else {
				log.addText(text, align);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent me) {

	}

	@Override
	public void mousePressed(MouseEvent me) {

	}

	@Override
	public void mouseEntered(MouseEvent me) {
		Object quelle = me.getSource();
		Paragraph[] p = null;
		int k = 0;
		for (int i = 0; i < 4; i++) {
			if (quelle == Benemy[i]) {
				k = i;
				break;
			}
		}

		List l = gui.getFigure().getRoomInfo().getMonsterInfos();
		if (k < l.size()) {
			MonsterInfo m = ((MonsterInfo) l.get(k));
			p = m.getParagraphs();
			getText().setText(p);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {

	}

	@Override
	public void mouseExited(MouseEvent me) {
		text.resetText();

	}

	public void updateGUI2(int code, boolean repaint) {

		if (code == this.UPDATE_CHARACTER) {
			character.updateView();
		}
		if (code == this.UPDATE_HEALTH) {
			gesundheit.updateView();
		}
		if (code == this.UPDATE_DUST) {
			staub.updateView();
		}
		if (code == this.UPDATE_INVENTORY) {
			inventory.updateView();
		}

		if (code == this.UPDATE_FIGHT) {
			if (gui.getFigure().getRoomInfo().fightRunning().booleanValue()) {
				northEast.setSelectedComponent(fightPanel);
				// //LinkedList monster = dasSpiel.getFight().getMonstersL();
				// List monster =
				// gui.getFigure().getRoomInfo().getMonsterInfos();
				// int i = 0;
				// ListIterator it = monster.listIterator();
				// while (it.hasNext()) {
				// MonsterInfo m = (MonsterInfo) it.next();
				// Benemy[i].setText(m.toString() + " (" + m.getShortStatus()
				// + ")");
				// i++;
				// }
				// for (int b = i; b < 4; b++) {
				// Benemy[b].setText("Kein Feind");
				// }
			} else {
				if (northEast.getSelectedComponent() == fightPanel) {
					northEast.setSelectedComponent(east);
				}
			}
			int size = spielfeld.getSpielfeldBild().getRoomSize();
			int off = spielfeld.getSpielfeldBild().getOffset();
			JDPoint p = gui.getFigure().getRoomNumber();
			// checkViewPos(off + (p.getX() * size), off + (p.getY() * size));

		}
		if (code == UPDATE_ALL) {
			for (int i = 1; i < 10; i++) {
				updateGUI2(i, false);
			}
		}
		if (code == UPDATE_VIEW) {

			spielfeld.resetViewPoint();
			spielfeld.getSpielfeldBild().updateInfoForMouseCursor();

		}
		if (code == UPDATE_SPELLS) {
			zauberP.updateView();
			int points = 0;
			FigureInfo f = gui.getFigure();
			if (f instanceof HeroInfo) {
				points = ((HeroInfo) f).getSpellPoints();

			}
			if (points > 0) {
				northEast.setForegroundAt(4, Color.BLUE);
			} else {
				northEast.setForegroundAt(4, Color.BLACK);
			}
		}
		// this.getSpielfeld().getSpielfeldBild().repaint();
		// if(getSpielfeld().getSpielfeldBild().offscreenImage != null) {
		// System.out.println("MALE!");
		// this.getSpielfeld().getSpielfeldBild().malen();
		// }
		// if(!repaint) {
		// northEast.getSelectedComponent().repaint();
		// }
		if (repaint) {
			this.repaint();
		}

	}

	public void gameOver() {

	}

	// public void setVerlauf() {
	// northEast.setSelectedComponent(east);
	// }

	// public void fightBegins() {
	// System.out.println("mainFrame fightBegins");
	// kampfVerlauf.cls();
	// //inFight = true;
	// this.updateGUI(this.UPDATE_FIGHT,true);
	// }

	// public void fightEnded() {
	// //inFight = false;
	// this.updateGUI(this.UPDATE_FIGHT,true);
	// }

	/**
	 * Returns the steuerung.
	 * 
	 * @return steuerungView
	 * 
	 */
	// public ControlView getSteuerung() {
	// return steuerung;
	// }

	/**
	 * Returns the choosenEnemy.
	 * 
	 * @return int
	 * 
	 */
	public int getChoosenEnemy() {
		return choosenEnemy;
	}

	/**
	 * Returns the text.
	 * 
	 * @return textView
	 * 
	 */
	public InfoView getText() {
		return text;
	}

	/**
	 * Returns the spielfeld.
	 * 
	 * @return spielfeldView
	 * 
	 */
	public BoardView getSpielfeld() {
		return spielfeld;
	}

	/**
	 * Returns the applet.
	 * 
	 * @return Applet
	 * 
	 * @uml.property name="applet"
	 */
	public Applet getApplet() {
		return applet;
	}

	/**
	 * @return
	 */
	public void positionieren() {
		Dimension dimension = new Dimension(getToolkit().getScreenSize());
		int screenWidth = (int) dimension.getWidth();
		int screenHeight = (int) dimension.getHeight();
		int width = this.getWidth();
		int height = this.getHeight();
		setLocation((screenWidth / 2) - (width / 2), (screenHeight / 2)
				- (height / 2));
	}

	/**
	 * @return Returns the noControl.
	 * 
	 */
	public boolean isNoControl() {
		return noControl;
	}

	/**
	 * @return Returns the staub.
	 * 
	 */
	public DustView getStaub() {
		return staub;
	}

	/**
	 * @return Returns the gesundheit.
	 * 
	 */
	public HealthView getGesundheit() {
		return gesundheit;
	}

	/**
	 * @return Returns the gui.
	 */
	public JDGUISwing getGui() {
		return gui;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setText(Paragraph[] p) {
		getText().setText(p);

	}

}
