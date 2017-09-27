package tyvrel.mag.core.model;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Describes stress in the cross section
 */
public class Stress {
	private double sigmac;
	private double sigmas;

	/**
	 * Creates an instance of the stress
	 *
	 * @param sigmac stress in concrete in Pa
	 * @param sigmas stress in concrete in Pa
	 */
	public Stress(double sigmac, double sigmas) {
		this.sigmac = sigmac;
		this.sigmas = sigmas;
	}

	/**
	 * Calculates and returns maximum out of two stresses
	 *
	 * @param stress1 one stress
	 * @param stress2 another stress
	 * @return maximum out of two stresses
	 */
	public static Stress max(Stress stress1, Stress stress2) {
		if (stress1 == null) return stress2;
		if (stress2 == null) return stress1;
		return new Stress(
				Math.max(stress1.getSigmac(), stress2.getSigmac()),
				Math.max(stress1.getSigmas(), stress2.getSigmas())
		);
	}

	@Override
	public String toString() {
		return "Stress{" +
				"sigmac=" + sigmac +
				", sigmas=" + sigmas +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Stress stress = (Stress) o;

		if (Double.compare(stress.sigmac, sigmac) != 0) return false;
		return Double.compare(stress.sigmas, sigmas) == 0;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(sigmac);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(sigmas);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Returns stress in concrete in Pa
	 *
	 * @return stress in concrete in Pa
	 */
	public double getSigmac() {
		return sigmac;
	}

	/**
	 * Returns stress in steel in Pa
	 *
	 * @return stress in steel in Pa
	 */
	public double getSigmas() {
		return sigmas;
	}
}
