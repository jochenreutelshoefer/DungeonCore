package graphics.util;

public class JDColor {
	
	public enum color {white, black, dark_gray, yellow};
	
	private int red;
	private int green;
	private int blue;
	
	public static final JDColor YELLOW = new JDColor(240,240,60);
	public static final JDColor BLACK = new JDColor(0,0,0);
	public static final JDColor DARK_GRAY = new JDColor(40,40,40);
	public static final JDColor WHITE = new JDColor(255,255,255);
	
	
	public JDColor(int r, int g, int b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	public int getRed() {
		return red;
	}

	public int getBlue() {
		return blue;
	}

	public int getGreen() {
		return green;
	}
	
}
