package tyvrel.mag.core.factory.longitudinalreinforcement;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;

import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates design anchorage length according to EN 1992-1-1 8.4.4
 */
@SuppressWarnings("WeakerAccess")
public class LongitudinalReinforcementDesignAnchorageLengthFactory implements Factory<Double> {
	private final double phi;
	private final double lbrqd;

	/**
	 * Creates instance of the factory
	 *
	 * @param phi   diameter of reinforcement in meters
	 * @param lbrqd basic anchorage length in meters
	 */
	public LongitudinalReinforcementDesignAnchorageLengthFactory(double phi, double lbrqd) {
		this.phi = phi;
		this.lbrqd = lbrqd;
	}

	/**
	 * Calculates design anchorage length in meters
	 *
	 * @return design anchorage length in meters
	 * @throws ImproperDataException if data is improper
	 */
	@Override
	public Double build() throws ImproperDataException, LSException {
		return calculateLbd();
	}


	/**
	 * Calculates design anchorage length in meters according to 8.4.4
	 *
	 * @return design anchorage length in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateLbd() throws ImproperDataException, LSException {
		double alpha1 = pos(calculateAlpha1());
		double alpha235 = pos(calculateAlpha235());
		double alpha4 = pos(calculateAlpha4());
		pos(lbrqd);
		double lbmin = pos(calculateLbmin());
		return pos(Math.max(alpha1 * alpha235 * alpha4 * lbrqd, lbmin));
	}

	/**
	 * Calculates minimum anchorage length in meters according to 8.4.4
	 *
	 * @return minimum anchorage length in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateLbmin() throws ImproperDataException, LSException {
		pos(phi);
		return Math.max(pos(0.6 * lbrqd), Math.max(pos(10 * phi), 0.1));
	}

	/**
	 * Calculates product of alpha2, alpha3 and alpha5 according to 8.4.4
	 *
	 * @return product of alpha2, alpha3 and alpha5
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlpha235() throws ImproperDataException, LSException {
		double alpha2 = pos(this::calculateAlpha2);
		double alpha3 = pos(this::calculateAlpha3);
		double alpha5 = pos(this::calculateAlpha5);
		double alpha235 = pos(alpha2 * alpha3 * alpha5);
		return Math.max(0.7, alpha235);
	}

	/**
	 * Calculates alpha1 coefficient according to 8.4.4
	 *
	 * @return alpha1 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlpha1() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates alpha2 coefficient according to 8.4.4
	 *
	 * @return alpha2 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlpha2() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates alpha3 coefficient according to 8.4.4
	 *
	 * @return alpha3 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlpha3() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates alpha4 coefficient according to 8.4.4
	 *
	 * @return alpha4 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlpha4() throws ImproperDataException, LSException {
		return 0.7;
	}

	/**
	 * Calculates alpha5 coefficient according to 8.4.4
	 *
	 * @return alpha5 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlpha5() throws ImproperDataException, LSException {
		return 1;
	}


}
