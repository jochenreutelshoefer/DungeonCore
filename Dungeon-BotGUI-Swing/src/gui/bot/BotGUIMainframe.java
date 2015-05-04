package gui.bot;

import figure.FigureInfo;
import figure.hero.HeroInfo;
import game.JDEnv;
import gui.JDBackgroundPanel;
import gui.JDJPanel;
import gui.JPanelNoRepaint;
import gui.Paragraph;
import gui.mainframe.component.BoardView;
import gui.mainframe.component.CharacterView;
import gui.mainframe.component.DustView;
import gui.mainframe.component.HealthView;
import gui.mainframe.component.InfoView;
import gui.mainframe.component.InventoryView;
import gui.mainframe.component.SpellsView;
import gui.mainframe.component.TextView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ai.GuiAI;
import control.AbstractSwingMainFrame;

public class BotGUIMainframe extends AbstractSwingMainFrame implements
		ActionListener, ItemListener, MouseListener, ChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BotJDGUISwing gui;
	private JDJPanel amatur;


	private Container mainPane;
	private JDBackgroundPanel cp;
	private CharacterView character;
	private BoardView spielfeld;
	private TextView verlauf;
	private HealthView gesundheit;
	private InfoView text;
	private DustView staub;
	private JPanelNoRepaint fightPanel;
	private InventoryView inventory;
	private SpellsView zauberP;
	private TextView kampfVerlauf;

	private JTabbedPane northEast;
	private BotGameControlPanel botGameControlPanel;
	
	enum GameRunState {
		RUNNING, PAUSED
	};

	public BotGUIMainframe(BotJDGUISwing gui, String name) {
		super(name);
		this.gui = gui;

	}

	public void initMainframe() {
		mainPane = this.getContentPane();
		cp = new JDBackgroundPanel();
		cp.setLayout(new BorderLayout());
		mainPane.add(cp);
		spielfeld = new BoardView(null, gui);
		cp.add(spielfeld, BorderLayout.CENTER);

		// ButtonFont = new Font("Arial", Font.BOLD, 17);
		// imageSource = new AWTImageLoader(null);

		/*
		 * east panel
		 */
		JPanel east = new JPanel();
		east.setOpaque(true);
		east.setLayout(new BorderLayout());


		northEast = new JTabbedPane();
		northEast.setOpaque(true);
		northEast.addChangeListener(this);

		amatur = new JDJPanel(gui);
		amatur.setSize(100, 20);
		east.add(amatur, BorderLayout.CENTER);


		verlauf = new TextView(380, 250, gui);
		east.add(verlauf, BorderLayout.NORTH);


		fightPanel = new JPanelNoRepaint();
		fightPanel.setLayout(new BorderLayout());
		kampfVerlauf = new TextView(380, 250, gui);
		fightPanel.add(kampfVerlauf, BorderLayout.NORTH);



		northEast.addTab(JDEnv.getResourceBundle().getString("movement"), east);
		northEast.addTab(JDEnv.getResourceBundle().getString("fight"),
				fightPanel);
		fightPanel.setOpaque(true);
		northEast.addTab(JDEnv.getResourceBundle().getString("gui_inventory"),
				inventory);
		inventory = new InventoryView(gui);
		inventory.setOpaque(true);
		zauberP = new SpellsView(gui);
		northEast.addTab(JDEnv.getResourceBundle().getString("spelling"),
				zauberP);
		zauberP.setOpaque(true);
		northEast.setOpaque(true);
		northEast.setPreferredSize(new Dimension(390, 340));

		character = new CharacterView(gui);
		northEast.addTab("Char", character);
		character.setOpaque(true);

		cp.add(northEast, BorderLayout.EAST);

		/*
		 * South panel
		 */
		JDJPanel southPanel = new JDJPanel(gui);
		BorderLayout fl = new BorderLayout();
		southPanel.setLayout(fl);
		southPanel.setBorder(new EmptyBorder(0, 50, 0, 50));
		gesundheit = new HealthView(gui);
		text = new InfoView(gui);
		botGameControlPanel = new BotGameControlPanel(gui);
		staub = new DustView(gui, botGameControlPanel);
		southPanel.add(gesundheit, BorderLayout.WEST);
		southPanel.add(text, BorderLayout.CENTER);
		southPanel.add(staub, BorderLayout.EAST);


		JDJPanel fightButtonP = new JDJPanel(gui);
		JDJPanel centerPanel = new JDJPanel(gui);
		centerPanel.setLayout(new GridLayout(2, 1));
		fightButtonP.setLayout(new BorderLayout());
		fightButtonP.add(centerPanel, BorderLayout.CENTER);

		cp.add(southPanel, BorderLayout.SOUTH);

		this.setSize(1280, 780);
		this.setResizable(false);
		this.positionieren();

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
				JOptionPane.showConfirmDialog(cp, JDEnv
						.getResourceBundle().getString("gui_really_quit_game"),
						JDEnv.getResourceBundle().getString("gui_quit_game"),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				dispose();

			}
		});

		// newStatement(Texts.begin(), 0);

		updateGUI(AbstractSwingMainFrame.UPDATE_ALL, false);

		this.repaint();
		spielfeld.getSpielfeldBild().setGameStarted();
		text.setText(new Paragraph[0]);
		this.setVisible(true);

	}

	public void updateGUI(int code, boolean repaint) {
		FigureInfo f = gui.getFigure();
		if (f == null)
			return;

		if (code == UPDATE_CHARACTER) {
			character.updateView();
		}
		if (code == UPDATE_HEALTH) {
			gesundheit.updateView();
		}
		if (code == UPDATE_DUST) {
			staub.updateView();
		}
		if (code == UPDATE_INVENTORY) {
			inventory.updateView();
		}

		if (code == UPDATE_FIGHT) {
			if (f.getRoomInfo().fightRunning().booleanValue()) {
				northEast.setSelectedComponent(fightPanel);
			} else {
				if (northEast.getSelectedComponent() == fightPanel) {
					// northEast.setSelectedComponent(east);
				}
			}

		}
		if (code == UPDATE_ALL) {
			for (int i = 1; i < 10; i++) {
				updateGUI(i, false);
			}
		}
		if (code == UPDATE_VIEW) {

			spielfeld.resetViewPoint();
			spielfeld.getSpielfeldBild().updateInfoForMouseCursor();

		}
		if (code == UPDATE_SPELLS) {
			zauberP.updateView();
			int points = 0;
			if (f instanceof HeroInfo) {
				points = ((HeroInfo) f).getSpellPoints();

			}
			if (points > 0) {
				northEast.setForegroundAt(4, Color.BLUE);
			} else {
				northEast.setForegroundAt(4, Color.BLACK);
			}
		}
		if (repaint) {
			this.repaint();
		}

	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent me) {
		text.resetText();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setText(Paragraph[] p) {
		this.text.setText(p);
	}

	public BoardView getSpielfeld() {
		return spielfeld;
	}

	@Override
	public void newStatement(String s, int code) {
		if (gui.getFigure().getRoomInfo().fightRunning().booleanValue()) {
			kampfVerlauf.newStatement(s, code);
		} else {
			verlauf.newStatement(s, code);
		}
	}

	@Override
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

	@Override
	public void clearFightLog() {
		kampfVerlauf.cls();

	}

	@Override
	public void updateHealth() {
		gesundheit.updateView();

	}

	public Class<? extends GuiAI> getSelectedBotAIClass() {
		// TODO Auto-generated method stub
		return botGameControlPanel.getSelectedBotAIClass();
	}

	@Override
	public void clearGUI() {
		kampfVerlauf.clearGUI();
		verlauf.clearGUI();

	}

}
