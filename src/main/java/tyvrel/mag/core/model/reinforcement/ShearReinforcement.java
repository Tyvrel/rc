package tyvrel.mag.core.model.reinforcement;

import tyvrel.mag.core.exception.ImproperDataException;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Describes shear reinforcement
 */
public class ShearReinforcement {
	private double phi;
	private double n;
	private double nleg;
	private double lbd;

	/**
	 * Creates an instance of the shear reinforcement
	 *
	 * @param n    number of shear assemblies in meter in 1/m
	 * @param phi  diameter of rebar in m
	 * @param lbd  anchorage length in m
	 * @param nleg number of arms
	 */
	public ShearReinforcement(double n, double phi, double lbd, double nleg) {
		this.n = n;
		this.phi = phi;
		this.lbd = lbd;
		this.nleg = nleg;
	}

	/**
	 * Merges two shear reinforcements and returns as new object
	 *
	 * @param asw1 one shear reinforcement to merge
	 * @param asw2 another shear reinforcement to merge
	 * @return merged shear reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	public static ShearReinforcement merge(ShearReinforcement asw1, ShearReinforcement asw2) throws
			ImproperDataException {
		if (asw2 == null) return asw1;
		if (asw1 == null) return asw2;
		if (asw1.getPhi() != asw2.getPhi()) throw new ImproperDataException();

		double numberOfLegsInMeter = Math.max(asw1.getNleg() * asw1.getN(), asw2.getNleg() * asw2.getN());
		double numberOfLegs = Math.max(asw1.getNleg(), asw2.getNleg());
		return new ShearReinforcement(
				numberOfLegsInMeter / numberOfLegs,
				asw1.getPhi(),
				Math.max(asw1.getLbd(), asw2.getLbd()),
				numberOfLegs
		);
	}


	@Override
	public String toString() {
		return "ShearReinforcement{" +
				"legn=" + nleg +
				"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		ShearReinforcement that = (ShearReinforcement) o;

		return Double.compare(that.nleg, nleg) == 0;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(nleg);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Returns number of legs
	 *
	 * @return number of legs
	 */
	public double getNleg() {
		return nleg;
	}

	/**
	 * Returns diameter of rebar in m
	 *
	 * @return diameter of rebar in m
	 */
	public double getPhi() {
		return phi;
	}

	/**
	 * Returns number of shear assemblies in meter in 1/m
	 *
	 * @return number of shear assemblies in meter in 1/m
	 */
	public double getN() {
		return n;
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
}
