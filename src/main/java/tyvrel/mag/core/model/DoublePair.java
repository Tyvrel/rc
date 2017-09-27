package tyvrel.mag.core.model;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Represents a pair of double
 */
public class DoublePair {
	private final double a;
	private final double b;

	/**
	 * Creates an instance of a pair
	 *
	 * @param a value describing property of top of cross section
	 * @param b value describing property of bottom of cross section
	 */
	public DoublePair(double a, double b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Returns value describing property of top of cross section
	 *
	 * @return value describing property of top of cross section
	 */
	public double getA() {
		return a;
	}

	/**
	 * Returns value describing property of bottom of cross section
	 *
	 * @return value describing property of bottom of cross section
	 */
	public double getB() {
		return b;
	}

	@Override
	public String toString() {
		return "DoublePair{" +
				"a=" + a +
				", b=" + b +
				'}';
	}
}
