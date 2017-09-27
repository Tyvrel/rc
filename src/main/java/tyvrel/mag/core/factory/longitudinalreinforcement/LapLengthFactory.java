package tyvrel.mag.core.factory.longitudinalreinforcement;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;

import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates lap length according to EN 1992-1-1 8.4.4
 */
@SuppressWarnings("WeakerAccess")
public class LapLengthFactory implements Factory<Double> {
	private final double phi;
	private final double p1;
	private final double lbrqd;

	/**
	 * Creates instance of the factory
	 *
	 * @param phi   diameter of reinforcement in meters
	 * @param p1    percentage of lapped bars
	 * @param lbrqd basic anchorage length in meters
	 */
	public LapLengthFactory(double phi, double p1, double lbrqd) {
		this.phi = phi;
		this.p1 = p1;
		this.lbrqd = lbrqd;
	}

	/**
	 * Calculates and returns lap length in meters
	 *
	 * @return lap length in meters
	 * @throws ImproperDataException if data is improper
	 */
	@Override
	public Double build() throws ImproperDataException, LSException {
		return calculateL0();
	}

	/**
	 * Calculates and returns lap length in meters according to 8.7.3
	 *
	 * @return lap length in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateL0() throws ImproperDataException, LSException {
		double alfa1 = pos(calculateAlfa1());
		double alfa235 = pos(calculateAlfa235());
		double alfa6 = pos(calculateAlfa6());
		pos(lbrqd);
		double l0min = pos(calculateL0min());
		return pos(Math.max(alfa1 * alfa235 * alfa6 * lbrqd, l0min));
	}

	/**
	 * Calculates minimum lap length in meters according to 8.7.3
	 *
	 * @return minimum lap length in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateL0min() throws ImproperDataException, LSException {
		pos(phi);
		pos(lbrqd);
		double alfa6 = pos(calculateAlfa6());
		return Math.max(pos(0.3 * alfa6 * lbrqd), Math.max(pos(15 * phi), 0.2));
	}

	/**
	 * Calculates product of alfa2, alfa3 and alfa5 according to 8.4.4
	 *
	 * @return product of alfa2, alfa3 and alfa5
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlfa235() throws ImproperDataException, LSException {
		double alfa2 = pos(this::calculateAlfa2);
		double alfa3 = pos(this::calculateAlfa3);
		double alfa5 = pos(this::calculateAlfa5);
		double alfa235 = pos(alfa2 * alfa3 * alfa5);
		return Math.max(0.7, alfa235);
	}

	/**
	 * Calculates alfa1 coefficient according to 8.7.3
	 *
	 * @return alfa1 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlfa1() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates alfa2 coefficient according to 8.7.3
	 *
	 * @return alfa2 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlfa2() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates alfa3 coefficient according to 8.7.3
	 *
	 * @return alfa3 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlfa3() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates alfa5 coefficient according to 8.7.3
	 *
	 * @return alfa5 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlfa5() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates alfa6 coefficient according to 8.7.3
	 *
	 * @return alfa6 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAlfa6() throws ImproperDataException, LSException {
		double alfa6 = pos(() -> Math.sqrt(pos(p1) / 0.25));
		alfa6 = Math.max(1.0, alfa6);
		alfa6 = Math.min(1.5, alfa6);
		return alfa6;
	}
}
