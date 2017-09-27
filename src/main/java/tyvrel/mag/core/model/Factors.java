package tyvrel.mag.core.model;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Describes partial factors
 */
public class Factors {
	private double gammas;
	private double gammac;

	/**
	 * Creates an instance of partial factors
	 *
	 * @param gammas gammas factor
	 * @param gammac gammac factor
	 */
	public Factors(double gammas, double gammac) {
		this.gammas = gammas;
		this.gammac = gammac;
	}

	/**
	 * Returns gammas factor
	 *
	 * @return gammas factor
	 */
	public double getGammas() {
		return gammas;
	}

	/**
	 * Returns  gammac factor
	 *
	 * @return gammac factor
	 */
	public double getGammac() {
		return gammac;
	}


}
