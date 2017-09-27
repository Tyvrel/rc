package tyvrel.mag.core.factory.shearreinforcement;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;

import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class MaximumShearReinforcementFactory implements Factory<ShearReinforcement> {
	private final double dg;
	private final double phi;
	private final double clearance;

	/**
	 * Creates instance of the factory
	 *
	 * @param dg        maximum diameter of aggregate
	 * @param clearance clearance in which shear reinforcement will be placed in meters
	 * @param phi       diameter of shear reinforcement in meters
	 */
	public MaximumShearReinforcementFactory(double dg, double clearance, double phi) {
		this.dg = dg;
		this.phi = phi;
		this.clearance = clearance;
	}

	/**
	 * Creates shear reinforcement
	 *
	 * @return shear reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	@Override
	public ShearReinforcement build() throws ImproperDataException, LSException {
		return createShearReinforcement();
	}

	/**
	 * Creates shear reinforcement
	 *
	 * @return shear reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected ShearReinforcement createShearReinforcement() throws ImproperDataException, LSException {
		return new ShearReinforcement(
				pos(this::calculateN),
				pos(() -> phi),
				0,
				pos(this::calculateLegsN)
		);
	}

	/**
	 * Calculates and returns maximum numer of shear reinforcement in meter
	 *
	 * @return returns maximum numer of shear reinforcement in meter
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateN() throws ImproperDataException, LSException {
		return pos(1 / pos(this::calculateS) + 1);
	}

	/**
	 * Calculates and returns maximum number of arms
	 *
	 * @return maximum number of arms
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateLegsN() throws ImproperDataException, LSException {
		pos(clearance);
		pos(phi);
		double horizontalClearance = pos(() -> clearance - phi);
		return pos(() -> Math.floor(horizontalClearance / pos(this::calculateS)) + 1);
	}

	/**
	 * Calculates and returns minimum axial spacing of shear reinforcement in meters according to 8.2
	 *
	 * @return minimum axial spacing of bottom shear reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateS() throws ImproperDataException, LSException {
		pos(dg);
		double s = pos(() -> Math.max(phi + phi * pos(calculateK1()), phi + dg + pos(calculateK2())));
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
