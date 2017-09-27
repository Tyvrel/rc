package tyvrel.mag.core.model.reinforcement;

import tyvrel.mag.core.exception.ImproperDataException;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Describes reinforcement
 */
public class Reinforcement {
	private double n;
	private double phi;
	private double lbd;
	private double l0;

	/**
	 * Creates an instance of the reinforcement
	 *
	 * @param n   number of rebars
	 * @param phi diameter of rebar in m
	 * @param lbd anchorage length in m
	 * @param l0  lap length in m
	 */
	public Reinforcement(double n, double phi, double lbd, double l0) {
		this.n = n;
		this.phi = phi;
		this.lbd = lbd;
		this.l0 = l0;
	}

	/**
	 * Merges two reinforcements and returns as new object
	 *
	 * @param asA one reinforcement to merge
	 * @param asB another reinforcement to merge
	 * @return merged reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	public static Reinforcement merge(Reinforcement asA, Reinforcement asB) throws ImproperDataException {
		if (asB == null) return asA;
		if (asA == null) return asB;
		if (asA.getPhi() != asB.getPhi()) throw new ImproperDataException();
		return new Reinforcement(
				Math.max(asA.getN(), asB.getN()),
				asA.getPhi(),
				Math.max(asA.getLbd(), asB.getLbd()),
				Math.max(asA.getL0(), asB.getL0())
		);
	}

	@Override
	public String toString() {
		return "Reinforcement{" +
				"n=" + n +
				", phi=" + phi +
				", lbd=" + lbd +
				", l0=" + l0 +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Reinforcement that = (Reinforcement) o;

		if (Double.compare(that.n, n) != 0) return false;
		if (Double.compare(that.phi, phi) != 0) return false;
		if (Double.compare(that.lbd, lbd) != 0) return false;
		return Double.compare(that.l0, l0) == 0;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(n);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(phi);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lbd);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(l0);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Returns axial mandrel diameter in m
	 *
	 * @return axial mandrel diameter in m
	 */
	public double getPhimmin() {
		double multi = (phi <= 0.016) ? 4 : 7;
		return multi * phi;
	}

	/**
	 * Returns area of one rebar in m2
	 *
	 * @return area of one rebar in m2
	 */
	public double getAphi() {
		return Math.PI * phi * phi / 4;
	}

	/**
	 * Returns area of one assembly in m2
	 *
	 * @return area of one assembly in m2
	 */
	public double getA() {
		return getAphi() * n;
	}

	/**
	 * Returns anchorage length in m
	 *
	 * @return anchorage length in m
	 */
	public double getLbd() {
		return lbd;
	}

	/**
	 * Returns lap length in m
	 *
	 * @return lap length in m
	 */
	public double getL0() {
		return l0;
	}

	/**
	 * Returns number of rebars
	 *
	 * @return number of rebars
	 */
	public double getN() {
		return n;
	}

	/**
	 * Returns diameter of rebar in m
	 *
	 * @return diameter of rebar in m
	 */
	public double getPhi() {
		return phi;
	}
}
