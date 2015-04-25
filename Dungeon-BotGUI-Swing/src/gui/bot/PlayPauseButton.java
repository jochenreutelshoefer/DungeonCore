package gui.bot;

import gui.bot.BotGUIMainframe.GameRunState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class PlayPauseButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameRunState gameRunState = GameRunState.RUNNING;

	public PlayPauseButton(final BotJDGUISwing gui) {
		super("Pause");
		// playButton.enableInputMethods(true);
		final JButton button = this;

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (gameRunState == GameRunState.RUNNING) {
					gameRunState = GameRunState.PAUSED;
					gui.getControl().pause();
					button.setText("Continue");
				} else if (gameRunState == GameRunState.PAUSED) {
					gameRunState = GameRunState.RUNNING;
					gui.getControl().run();
					button.setText("Pause");
				}
			}
		});
	}
}
