package gui.mainframe.dialog;
import java.awt.*;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class WaitView extends Frame {

	public WaitView() {
		super("Bitte warten");
		this.setSize(250,100);
		positionieren();
		this.setResizable(false);
		Panel panel = new Panel();
		this.add(panel);
		panel.add(new Label("momentchen..."));
		this.setVisible(true);
		
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

}
