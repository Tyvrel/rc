package tyvrel.mag.core.factory.longitudinalreinforcement;

import tyvrel.mag.core.exception.*;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;

import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates minimum longitudinal reinforcement factory according to EN 1992-1-1 8.2
 */
@SuppressWarnings("WeakerAccess")
public class MaximumLongitudinalReinforcementFactory implements Factory<LongitudinalReinforcement> {
	private final double dg;
	private final CrossSection crossSection;

	/**
	 * Creates instance of the factory
	 *
	 * @param dg           maximum diameter of aggregate in meters
	 * @param crossSection cross section of the element
	 */
	public MaximumLongitudinalReinforcementFactory(double dg, CrossSection crossSection) {
		this.dg = dg;
		this.crossSection = crossSection;
	}

	/**
	 * Returns maximal longitudinal reinforcement
	 *
	 * @return maximal longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	@Override
	public LongitudinalReinforcement build() throws ImproperDataException, LSException {
		return new LongitudinalReinforcement(createReinforcementb(), createReinforcementa());
	}

	/**
	 * Returns maximal bottom longitudinal reinforcement
	 *
	 * @return maximal bottom longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected Reinforcement createReinforcementb() throws ImproperDataException, LSException {
		Reinforcement as = notNull(() -> this.crossSection.getAs().getAsb());
		double phi = as.getPhi();
		double n = pos(calculateNb());
		double lbd = as.getLbd();
		double l0 = as.getL0();
		return new Reinforcement(n, phi, lbd, l0);
	}

	/**
	 * Returns maximal top longitudinal reinforcement
	 *
	 * @return maximal top longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected Reinforcement createReinforcementa() throws ImproperDataException, LSException {
		Reinforcement as = notNull(() -> this.crossSection.getAs().getAsa());
		double phi = as.getPhi();
		double n = pos(calculateNa());
		double lbd = as.getLbd();
		double l0 = as.getL0();
		return new Reinforcement(n, phi, lbd, l0);
	}

	/**
	 * Calculates maximum number of rebars of bottom reinforcement
	 *
	 * @return maximum number of rebars of bottom reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateNb() throws ImproperDataException, LSException {
		double phi = pos(() -> crossSection.getAs().getAsb().getPhi());
		double s = pos(this::calculateSb);
		return pos(() -> calculateN(phi, s));
	}

	/**
	 * Calculates maximum number of rebars of top reinforcement
	 *
	 * @return maximum number of rebars of top reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateNa() throws ImproperDataException, LSException {
		double phi = pos(() -> crossSection.getAs().getAsa().getPhi());
		double s = pos(this::calculateSa);
		return pos(() -> calculateN(phi, s));
	}

	private double calculateN(double phi, double s) throws ImproperDataException, LSException {
		pos(phi);
		double horizontalClearance = pos(() -> pos(crossSection.getAsClearance()) - phi);
		return pos(Math.floor(horizontalClearance / pos(s)) + 1);
	}

	/**
	 * Calculates and returns minimum axial spacing of bottom reinforcement in meters according to 8.2
	 *
	 * @return minimum axial spacing of bottom reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateSb() throws ImproperDataException, LSException {
		double phi = pos(() -> crossSection.getAs().getAsb().getPhi());
		return pos(() -> calculateS(phi));
	}

	/**
	 * Calculates and returns minimum axial spacing of top reinforcement in meters according to 8.2
	 *
	 * @return minimum axial spacing of top reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateSa() throws ImproperDataException, LSException {
		double phi = pos(() -> crossSection.getAs().getAsa().getPhi());
		return pos(() -> calculateS(phi));
	}

	private double calculateS(double phi) throws ImproperDataException, LSException {
		pos(dg);
		double s = pos(() -> Math.max(phi * pos(calculateK1()) + phi, phi + dg + pos(calculateK2())));
		return Math.max(s, 0.020);
	}

	/**
	 * Calculates and returns k1 coefficient according to 8.2
	 *
	 * @return k1 coefficient
	 * @throws ImproperDataException never
	 */
	protected double calculateK1() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates and returns k2 coefficient according to 8.2
	 *
	 * @return k2 coefficient
	 * @throws ImproperDataException never
	 */
	protected double calculateK2() throws ImproperDataException, LSException {
		return 0.005;
	}
}
