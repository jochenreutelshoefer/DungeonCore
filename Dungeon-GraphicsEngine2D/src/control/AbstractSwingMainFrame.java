package control;

import java.awt.Dimension;

import javax.swing.JFrame;

public abstract class AbstractSwingMainFrame extends JFrame implements
		MainFrameI {

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


	public AbstractSwingMainFrame(String title) {
		super(title);
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
	
	public abstract void newStatement(String s, int code, int to);

	public abstract void newStatement(String s, int code);

	public abstract void updateHealth();

}
