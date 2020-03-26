package figure.attribute;

/**
 * Von einem anderen Attribut abgeleites Attribut.
 */
public class DerivedAttribute extends Attribute {

	public static final int FORMULAR_KEY_8TH = 1;

	private double base = 0;

	private final Attribute master;
	private int formularKey = 0;

	public DerivedAttribute(Type name, Attribute master, int key) {
		super(name, 0);
		this.formularKey = key;
		this.master = master;
	}

	@Override
	public double getBasic() {
		return derive();
	}

	@Override
	public double getValue() {
		return derive();

	}

	private double derive() {
		double res = 0;
		if (this.formularKey == FORMULAR_KEY_8TH) {
			double d = master.getValue();

			res = base + (d / 8);
		}

		return res;
	}

	@Override
	public void incBasic(double d) {
		base += d;
	}

	@Override
	public void modValue(double d) {
		base += d;
	}

	public void modValue(int d) {
		base += d;
	}

}
