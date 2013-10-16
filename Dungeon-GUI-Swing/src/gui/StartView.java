package gui;

//import JDGuimainFrame;

import figure.Figure;
import figure.hero.Hero;
import game.DungeonGame;
import game.JDEnv;
import graphics.ImageManager;
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
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import audio.AudioEffectsManager;
import audio.AudioLoader;
import control.ActionAssembler;


public class StartView extends AbstractStartWindow implements ActionListener, KeyListener,
		ItemListener {

	public static final String GODMODE = "godmode1";

	public static final String AGENT = "AgentTest01";

	Container cp;

	JTextField name;

	JButton start = new JButton("<html>Neues Spiel starten<html>");

	JLabel label = new JLabel("Spielername:");

	JLabel label2 = new JLabel(
			"-Bitte immer den gleichen Spielernamen angeben!");

	JLabel label3 = new JLabel(
			"<html>-<font color=\"#ff0000\">Datenschutzrechtlicher Hinweis: </font> Dieser Name könnte <p>in der Highscoreliste veröffentlicht werden!</html>");

	JCheckBox ligaCB = new JCheckBox("Ligaspiel");
	JCheckBox sounds = new JCheckBox("Sound FX");
	JCheckBox autoZoom = new JCheckBox("Auto Zoom");

	String defaultString = new String("Spielername (ist nicht Heldname!)");

	String nameRequestString = new String("Bitte Name angeben!!!");

	JRadioButton germanRB = new JRadioButton("Deutsch");

	JRadioButton englishRB = new JRadioButton("English");
	
	JRadioButton rookieRB = new JRadioButton("Anfänger (keine Scores)");

	JRadioButton normalRB = new JRadioButton("Normal (Empfohlen)");

	// boolean appletRunning;

	Applet applet;
	Box box = null;
	Box boxDiff = null;
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
						//applet.stop();
					} else {
						//System.exit(0);
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
		//checkButtonPanel.add(ligaCB);
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
		//name.addActionListener(this);
		// name.setSelectionStart(0);
		//name.setSelectionEnd(name.getText().length());
		name.setEditable(true);

		initGui();
		
		this.setResizable(false);
		this.setSize(320, 480);
		// pack();
		positionieren();
		this.setVisible(true);

		//System.out.println("finished init StartView");
	}

	private void initGui() {
		PICS_LOADED = JDEnv.getResourceBundle().getString("state")+": "
			+ JDEnv.getResourceBundle().getString("gui_pics_are_loaded");
	PICS_NOT_LOADED = JDEnv.getResourceBundle().getString("state")+": "
			+ JDEnv.getResourceBundle().getString("gui_pics_not_loaded");

		
		if (ImageManager.imagesLoaded) {
			this.picStatusLabel.setText(PICS_LOADED);
		} else {
			this.picStatusLabel.setText(PICS_NOT_LOADED);
		}
		
		TitledBorder border = new TitledBorder(JDEnv.getResourceBundle().getString("language")+":");
		box.setBorder(border);
		
		TitledBorder borderDiff = new TitledBorder(JDEnv.getResourceBundle().getString("gui_difficulty")+":");
		boxDiff.setBorder(borderDiff);
		
		normalRB.setText(JDEnv.getString("gui_difficulty_normal"));
		rookieRB.setText(JDEnv.getString("gui_difficulty_rookie"));
		
		start.setText(JDEnv.getResourceBundle().getString("gui_start_new_game"));
		
		label.setText(JDEnv.getResourceBundle().getString("playername")+":");
		ligaCB.setText(JDEnv.getResourceBundle().getString("league")+":");
		sounds.setText("Sound FX");
		autoZoom.setText("Auto Zoom");
	}
	
	MainFrame main;

	DungeonGame dagame;

	MyJDGui gui;

	private boolean istGuest = false;

	Hero h;

	ActionAssembler guiControl;

	NewHeroView heldFenster;

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
		JDEnv.unsetGame();
		System.gc();
	}

	int startCount = 0;

	public void itemStateChanged(ItemEvent e) {
		if (englishRB.isSelected()) {
			setResourceBundles(true);
		} else {
			setResourceBundles(false);
		}
		this.initGui();
	}

	public static void setResourceBundles(boolean english) {
		ResourceBundle en = null;
		ResourceBundle de = null;
		Locale loc_de = Locale.GERMAN;
		Locale loc_en = Locale.ENGLISH;

		ResourceBundle myBundle = null;
		if (english) {
			myBundle = ResourceBundle.getBundle("texts", loc_en);
		} else {
			myBundle = ResourceBundle.getBundle("texts", loc_de);

		}

		if (myBundle == null) {
			System.out.println("Bundle ist null");
			System.exit(0);
		}
		// System.out.println("Bundle: "+myBundle.toString());
		// System.out.println("sword: "+myBundle.getString("sword"));
		JDEnv.setRes(myBundle);
		Texts.init();
		// try {
		// en = new PropertyResourceBundle(new FileInputStream(
		// "txt\\texts"));
		// de = new PropertyResourceBundle(new FileInputStream(
		// "txt\\texts_de"));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }else {
		// en = new PropertyResourceBundle(new InputStream(new
		// URL(applet.getCodeBase().toString()+"txt\\texts")));
		// de = new PropertyResourceBundle(new FileInputStream(
		// "txt\\texts_de"));
		// }

	}

	private String PICS_NOT_LOADED = "";

	private String PICS_LOADED = "";

	private String PICS_LOADING = "Status: Bilder werden geladen. Einen Moment..";

	private JLabel picStatusLabel = new JLabel(PICS_NOT_LOADED);

	public void imagesLoaded() {
		picStatusLabel.setText(PICS_LOADED);
	}

	private boolean sendHighscore = true;

	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == this.start) {
			killRefs();
			startCount++;

			final StartView view = this;

			if(soundEffects()) {
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
				waitDialog.setLocation(this.getX()+50, this.getY()+(this.getHeight()/2));
				waitDialog.pack();
				waitDialog.setVisible(true);
				
				if(soundEffects()) {
					AudioLoader audioLoader = new DefaultSwingAudioLoader();
					if(applet != null) {
						audioLoader = new AppletAudioLoader(applet);
					}
					AudioEffectsManager.init(audioLoader);
				}
				
				MediaTracker tracker = new MediaTracker(view);
				AWTImageLoader.setTracker(tracker);
				
				ImageManager imageManager = ImageManager.getInstance(new AWTImageLoader(applet));
				imageManager.loadImages();
				view.imagesLoaded();
				waitDialog.setVisible(false);
				waitDialog.dispose();
			}

			String s = name.getText();
//			if (!(s.equals(defaultString) | s.equals(this.nameRequestString))) {
				if(rookieRB.isSelected()) {
					JDEnv.setBeginnerGame(true);
				}else {
					JDEnv.setBeginnerGame(false);
				}
				this.setVisible(false);
				heldFenster = new NewHeroView(this);
				h = heldFenster.getHero();
				heldFenster.dispose();

				
				dagame = DungeonGame.getInstance();
//				dagame.init(this, applet, s, h,
//						this.sendHighscore, new MyJDGui(), AId3Factory.makeAI());
				dagame.init(this, applet, s, h,
						this.sendHighscore, new MyJDGui(), null);

//			} else {
//				if (s.equals(this.nameRequestString)) {
//					name.setText(this.defaultString);
//				} else {
//
//					name.setText(this.nameRequestString);
//				}
//				name.grabFocus();
//				name.selectAll();
//				name.setEditable(true);
//			}
//		}
		 }

		// waitWin.dispose();
	}

	public boolean isAppletRunning() {
		return applet != null;
	}

	public void keyPressed(KeyEvent ke) {

	}

	public void keyReleased(KeyEvent ke) {

	}

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
		//String name = "xy";
		StartView start = new StartView(name, 756851968, null, false);
		// }
		// });
	}
}
