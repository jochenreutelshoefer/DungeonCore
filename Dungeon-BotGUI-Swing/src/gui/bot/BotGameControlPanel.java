package gui.bot;

import gui.JDJPanel;
import ai.GuiAI;

public class BotGameControlPanel extends JDJPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BotAISelectionBox botSelectionBox;

	public BotGameControlPanel(BotJDGUISwing gui) {
		super(gui);

		this.add(new PlayPauseButton(gui));
		this.add(new RestartGameButton(gui));
		botSelectionBox = new BotAISelectionBox();
		this.add(botSelectionBox);
	}

	public Class<? extends GuiAI> getSelectedBotAIClass() {
		// TODO Auto-generated method stub
		return botSelectionBox.getSelectedBotAIClass();
	}

}
