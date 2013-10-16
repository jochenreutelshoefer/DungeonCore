package util;

public class JDColor {
	
	public enum color {white, black, dark_gray, yellow};
	
	private final int redValue;
	private final int greenValue;
	private final int blueValue;
	
	public static final JDColor YELLOW = new JDColor(240,240,60);
	public static final JDColor yellow = new JDColor(240, 240, 60);
	public static final JDColor BLACK = new JDColor(0,0,0);
	public static final JDColor black = new JDColor(0,0,0);
	public static final JDColor DARK_GRAY = new JDColor(40,40,40);
	public static final JDColor WHITE = new JDColor(255,255,255);
	public static final JDColor orange = new JDColor(245,200,30);
	public static final JDColor blue = new JDColor(0, 0, 230);
	public static final JDColor red = new JDColor(255, 0, 0);
	
	
	public JDColor(int r, int g, int b) {
		this.redValue = r;
		this.greenValue = g;
		this.blueValue = b;
	}

	public int getRed() {
		return redValue;
	}

	public int getBlue() {
		return blueValue;
	}

	public int getGreen() {
		return greenValue;
	}
	
}
