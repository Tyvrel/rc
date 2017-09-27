package tyvrel.mag.core.model.classification;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Describes steel
 */
public class Steel {
	/**
	 * Represents A steel class according to EN 1992-1-1
	 */
	public static final String A = "A";
	/**
	 * Represents B steel class according to EN 1992-1-1
	 */
	public static final String B = "B";
	/**
	 * Represents C steel class according to EN 1992-1-1
	 */
	public static final String C = "C";

	private double fy;
	private double es;
	private String classification;

	/**
	 * Creates an instance of the steel
	 *
	 * @param es             elasticity modulus in Pa
	 * @param fy             yield strength in Pa
	 * @param classification classificaion according to EN 1992-1-1
	 */
	public Steel(double es, double fy, String classification) {
		this.fy = fy;
		this.es = es;
		this.classification = classification;
	}

	@Override
	public String toString() {
		return "Steel{" +
				"fy=" + fy +
				", es=" + es +
				", classification='" + classification + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || (!(o instanceof Steel))) return false;

		Steel steel = (Steel) o;

		if (Double.compare(steel.fy, fy) != 0) return false;
		if (Double.compare(steel.es, es) != 0) return false;
		return classification != null ? classification.equals(steel.classification) : steel.classification == null;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(fy);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(es);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (classification != null ? classification.hashCode() : 0);
		return result;
	}

	/**
	 * Returns yield strength in Pa
	 *
	 * @return yield strength in Pa
	 */
	public double getFy() {
		return fy;
	}

	/**
	 * Returns elasticity modulus in Pa
	 *
	 * @return elasticity modulus in Pa
	 */
	public double getEs() {
		return es;
	}

	/**
	 * Returns classificaion according to EN 1992-1-1
	 *
	 * @return classificaion according to EN 1992-1-1
	 */
	public String getClassification() {
		return classification;
	}

}
