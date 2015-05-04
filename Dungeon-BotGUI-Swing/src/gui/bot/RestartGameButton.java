package gui.bot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class RestartGameButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RestartGameButton(final BotJDGUISwing gui) {
		super("Start New Game");
		// playButton.enableInputMethods(true);
		final JButton button = this;

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String quitText = gui.getResourceBundle().getString(
						"gui_bot_really_quit_game");
				int end = JOptionPane.showConfirmDialog(gui.getMainFrame(),
						quitText,
						gui
						.getResourceBundle().getString("gui_bot_quit_game"),
						JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (end == 0) {
					gui.restartGame();
				} else {
					// we do not do anything
				}
			}
		});
	}
}
