package gui.init;

//import JDGuimainFrame;

import figure.Figure;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import game.DungeonGame;
import game.JDEnv;
import graphics.ImageManager;
import gui.AbstractStartWindow;
import gui.MyJDGui;
import gui.audio.AppletAudioLoader;
import gui.audio.AudioSet;
import gui.audio.DefaultSwingAudioLoader;
import gui.engine2D.AWTImageLoader;
import gui.mainframe.MainFrame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.GregorianCalendar;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import audio.AudioEffectsManager;
import audio.AudioLoader;
import control.ActionAssembler;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.generate.DungeonGenerationFailedException;
import dungeon.util.DungeonManager;

public class StartView extends AbstractStartWindow implements ActionListener,
		KeyListener, ItemListener {

	public static final String GODMODE = "godmode1";

	public static final String AGENT = "AgentTest01";

	private final Container cp;

	private final JTextField name;

	private final JButton start = new JButton("<html>Neues Spiel starten<html>");

	private final JLabel label = new JLabel("Spielername:");

	private final JLabel label2 = new JLabel(
			"-Bitte immer den gleichen Spielernamen angeben!");

	private final JLabel label3 = new JLabel(
			"<html>-<font color=\"#ff0000\">Datenschutzrechtlicher Hinweis: </font> Dieser Name könnte <p>in der Highscoreliste veröffentlicht werden!</html>");

	private final JCheckBox ligaCB = new JCheckBox("Ligaspiel");
	private final JCheckBox sounds = new JCheckBox("Sound FX");
	private final JCheckBox autoZoom = new JCheckBox("Auto Zoom");

	private final String defaultString = new String(
			"Spielername (ist nicht Heldname!)");

	private final String nameRequestString = new String("Bitte Name angeben!!!");

	private final JRadioButton germanRB = new JRadioButton("Deutsch");

	private final JRadioButton englishRB = new JRadioButton("English");

	private final JRadioButton rookieRB = new JRadioButton(
			"Anfänger (keine Scores)");

	private final JRadioButton normalRB = new JRadioButton("Normal (Empfohlen)");

	// boolean appletRunning;

	private Applet applet = null;
	private Box box = null;
	private Box boxDiff = null;

	public StartView(String playerName, int code, Applet a, boolean english) {

		super("Willkommen bei JAVA-DUNGEON");

		setResourceBundles(english);

		GregorianCalendar c = new GregorianCalendar();
		int day = c.get(GregorianCalendar.DAY_OF_MONTH);
		int codedDay = code / 2047;
		// System.out.println("playerName: "+playerName);
		if (playerName != "Application") {
			if (code != 756851968) {
				if (code % 2047 != 167 || codedDay != day + 2) {
					System.out.println("Invalid start code");

					if (applet != null) {
						// applet.stop();
					} else {
						// System.exit(0);
					}
				}
			}
		}
		cp = getContentPane();

		// this.appletRunning = appletRunning;
		applet = a;

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
				if (applet == null) {
					System.exit(0);
				} else {
					applet.stop();
				}

			}
		});

		JPanel panel = new JPanel();

		JPanel middlePanel = new JPanel();
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new GridLayout(2, 1));
		JPanel buttonPanel = new JPanel();
		middlePanel.setLayout(new GridLayout(4, 1));
		panel.setLayout(new BorderLayout());

		FlowLayout fl = new FlowLayout();
		fl.setHgap(4);

		cp.setLayout(fl);
		cp.add(panel);

		autoZoom.setSelected(false);
		sounds.setSelected(true);

		name = new JTextField(20);
		name.setText(defaultString);
		if (playerName.equals("Application")) {
			name.setEditable(true);

			ligaCB.setSelected(false);
			sendHighscore = false;
		} else if (playerName.equals("Gast")) {
			// System.out.println("playername ist Gast");
			this.istGuest = true;
			ligaCB.setSelected(false);
			ligaCB.setEnabled(false);
			name.setEditable(true);
			this.sendHighscore = true;

		} else {
			registered = true;
			this.sendHighscore = true;
			name.setEditable(true);
			ligaCB.setSelected(false);
		}

		label.setPreferredSize(new Dimension(200, 40));
		name.setPreferredSize(new Dimension(200, 20));
		name.setEditable(true);
		name.setEnabled(true);
		start.setPreferredSize(new Dimension(150, 40));

		// middlePanel.add(name);
		// middlePanel.add(label2);
		// middlePanel.add(label3);

		if (english) {
			englishRB.setSelected(true);
		} else {
			germanRB.setSelected(true);
		}
		germanRB.addItemListener(this);
		englishRB.addItemListener(this);
		ButtonGroup bg = new ButtonGroup();
		bg.add(germanRB);
		bg.add(englishRB);
		box = Box.createHorizontalBox();
		box.setBorder(new TitledBorder(""));
		box.add(germanRB);
		box.add(englishRB);
		middlePanel.add(box);

		JPanel checkButtonPanel = new JPanel();
		// checkButtonPanel.add(ligaCB);
		checkButtonPanel.add(sounds);
		checkButtonPanel.add(autoZoom);

		middlePanel.add(checkButtonPanel);

		normalRB.setSelected(true);
		normalRB.addItemListener(this);
		rookieRB.addItemListener(this);
		ButtonGroup bgDiff = new ButtonGroup();
		bgDiff.add(rookieRB);
		bgDiff.add(normalRB);
		boxDiff = Box.createVerticalBox();
		boxDiff.setBorder(new TitledBorder(""));
		boxDiff.add(rookieRB);
		boxDiff.add(normalRB);
		middlePanel.add(boxDiff);

		middlePanel.add(picStatusLabel);

		// start.setSize(new Dimension(100,50));
		// start.setMinimumSize(new Dimension(100,50));
		// start.setMaximumSize(new Dimension(100,50));
		panel.add(upperPanel, BorderLayout.NORTH);

		panel.add(middlePanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		upperPanel.add(label);
		upperPanel.add(name);

		buttonPanel.add(start);
		buttonPanel.setAlignmentY(30);
		start.setAlignmentY(30);
		start.addActionListener(this);
		// name.addActionListener(this);
		// name.setSelectionStart(0);
		// name.setSelectionEnd(name.getText().length());
		name.setEditable(true);

		initGui();

		this.setResizable(false);
		this.setSize(320, 480);
		// pack();
		positionieren();
		this.setVisible(true);

		// System.out.println("finished init StartView");
	}

	private void initGui() {
		PICS_LOADED = JDEnv.getResourceBundle().getString("state") + ": "
				+ JDEnv.getResourceBundle().getString("gui_pics_are_loaded");
		PICS_NOT_LOADED = JDEnv.getResourceBundle().getString("state") + ": "
				+ JDEnv.getResourceBundle().getString("gui_pics_not_loaded");

		if (ImageManager.imagesLoaded) {
			this.picStatusLabel.setText(PICS_LOADED);
		} else {
			this.picStatusLabel.setText(PICS_NOT_LOADED);
		}

		TitledBorder border = new TitledBorder(JDEnv.getResourceBundle()
				.getString("language") + ":");
		box.setBorder(border);

		TitledBorder borderDiff = new TitledBorder(JDEnv.getResourceBundle()
				.getString("gui_difficulty") + ":");
		boxDiff.setBorder(borderDiff);

		normalRB.setText(JDEnv.getString("gui_difficulty_normal"));
		rookieRB.setText(JDEnv.getString("gui_difficulty_rookie"));

		start.setText(JDEnv.getResourceBundle().getString("gui_start_new_game"));

		label.setText(JDEnv.getResourceBundle().getString("playername") + ":");
		ligaCB.setText(JDEnv.getResourceBundle().getString("league") + ":");
		sounds.setText("Sound FX");
		autoZoom.setText("Auto Zoom");
	}

	private MainFrame main;

	private DungeonGame dagame;

	private MyJDGui gui;

	private boolean istGuest = false;

	private Hero h;

	private ActionAssembler guiControl;

	private NewHeroView heldFenster;

	private boolean registered = false;

	public boolean registeredPlayer() {
		return registered;
	}

	public boolean ligaGame() {
		return ligaCB.isSelected();
	}

	public boolean autoZoom() {
		return autoZoom.isSelected();
	}

	public boolean soundEffects() {
		return sounds.isSelected();
	}

	private void killRefs() {
		Figure.resetFigureList();
		if (main != null) {

			main.dispose();
			// main.doFinalize();
			main = null;

		}
		if (heldFenster != null) {
			heldFenster = null;
		}
		if (guiControl != null) {
			guiControl = null;
		}
		if (h != null) {
			h = null;
		}
		if (dagame != null) {
			dagame = null;
		}
		if (gui != null) {
			gui = null;
		}
		System.gc();
	}

	int startCount = 0;

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (englishRB.isSelected()) {
			setResourceBundles(true);
		} else {
			setResourceBundles(false);
		}
		this.initGui();
	}

	public static void setResourceBundles(boolean english) {
		// TODO: refactor
		JDEnv.init();
	}

	private String PICS_NOT_LOADED = "";

	private String PICS_LOADED = "";

	private final String PICS_LOADING = "Status: Bilder werden geladen. Einen Moment..";

	private final JLabel picStatusLabel = new JLabel(PICS_NOT_LOADED);

	public void imagesLoaded() {
		picStatusLabel.setText(PICS_LOADED);
	}

	private boolean sendHighscore = true;

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == this.start) {
			killRefs();
			startCount++;

			final StartView view = this;

			if (soundEffects()) {
				AudioSet.setSoundEnable(true);
			} else {
				AudioSet.setSoundEnable(false);
			}

			if (ImageManager.imagesLoaded) {

			} else {
				picStatusLabel.setText(PICS_LOADING);
				this.repaint();

				JFrame waitDialog = new JFrame("Downloading Pics");
				waitDialog.setPreferredSize(new Dimension(200, 20));
				waitDialog.setLocation(this.getX() + 50,
						this.getY() + (this.getHeight() / 2));
				waitDialog.pack();
				waitDialog.setVisible(true);

				if (soundEffects()) {
					AudioLoader audioLoader = new DefaultSwingAudioLoader();
					if (applet != null) {
						audioLoader = new AppletAudioLoader(applet);
					}
					AudioEffectsManager.init(audioLoader);
				}

				MediaTracker tracker = new MediaTracker(view);
				AWTImageLoader.setTracker(tracker);

				ImageManager imageManager = ImageManager
						.getInstance(new AWTImageLoader(applet));
				imageManager.loadImages();
				view.imagesLoaded();
				waitDialog.setVisible(false);
				waitDialog.dispose();
			}

			String playername = name.getText();
			if (rookieRB.isSelected()) {
				JDEnv.setBeginnerGame(true);
			} else {
				JDEnv.setBeginnerGame(false);
			}
			this.setVisible(false);
			heldFenster = new NewHeroView(this);
			h = heldFenster.getHero();
			heldFenster.dispose();

			dagame = DungeonGame.getInstance();
			// dagame.init(this, applet, playername, h, this.sendHighscore,
			// new MyJDGui());

			initGame(this, applet, playername, h, this.sendHighscore,
					new MyJDGui());

		}

	}

	private void initGame(StartView startView, Applet applet2,
			String playerName, Hero held, boolean sendHighscore2,
			MyJDGui myJDGui) {
		this.sendHighscore = sendHighscore;

		/*
		 * according to experiences, there is about 98% probability for success
		 * to generate a dungeon with the current generator hence, trying 3x to
		 * generate Dungeon
		 */
		Dungeon derDungeon = new Dungeon(dagame.DungeonSizeX,
				dagame.DungeonSizeY, 18, 39, dagame);
		try {
			dagame.fillDungeon(derDungeon);
		} catch (DungeonGenerationFailedException e) {
			derDungeon = new Dungeon(dagame.DungeonSizeX, dagame.DungeonSizeY,
					18, 39, dagame);
			try {
				dagame.fillDungeon(derDungeon);
			} catch (DungeonGenerationFailedException e1) {
				derDungeon = new Dungeon(dagame.DungeonSizeX,
						dagame.DungeonSizeY, 18, 39, dagame);
				try {
					dagame.fillDungeon(derDungeon);
				} catch (DungeonGenerationFailedException e2) {
					System.out
							.println("Cound not generate Dungeon - check Dungeon Generator!");
					e1.printStackTrace();
					System.exit(0);
				}
				e1.printStackTrace();
			}

		}

		HeroInfo heroInfo = DungeonManager.enterDungeon(held, derDungeon,
				new JDPoint(18, 39));

		myJDGui.setFigure(heroInfo);

		dagame.putGuiFigure(held, myJDGui);

		held.setControl(myJDGui);

		// hack to save some memory
		Figure.unsetUnnecessaryRoomObStatsObjects(derDungeon);

		myJDGui.initGui(startView, applet, playerName);

		if (playerName.equals("godmode1")) {
			// System.out.println("setting cheat mode!");
			dagame.setImortal(true);
			JDEnv.visCheat = true;
			held.getRoomVisibility().setVisCheat();
			held.setInvulnerable(true);
		} else {
			JDEnv.visCheat = false;
		}
		dagame.started = true;
		Thread th = new Thread(dagame);
		th.start();

	}

	public boolean isAppletRunning() {
		return applet != null;
	}

	@Override
	public void keyPressed(KeyEvent ke) {

	}

	@Override
	public void keyReleased(KeyEvent ke) {

	}

	@Override
	public void keyTyped(KeyEvent ke) {
		// System.out.println("keyTyped: "+ke.toString());
		int o = ke.getKeyCode();
		if (o == KeyEvent.VK_ENTER) {
			// System.out.println("Enter gedr�ckt!");
			actionPerformed(null);
		}

	}

	/**
	 * Die Methode positionieren sorgt dafuer, dass "this" genau in der Mitte
	 * des Bildschirms befindet
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

	public static void main(String[] args) {
		try {
			UIManager.getSystemLookAndFeelClassName();
		} catch (Exception e) {
			System.out.println("Fehler beim Look&Feel laden:" + e.toString());
		}
		// SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		String name = "AgentTest01";
		// String name = "xy";
		StartView start = new StartView(name, 756851968, null, false);
		// }
		// });
	}
}
