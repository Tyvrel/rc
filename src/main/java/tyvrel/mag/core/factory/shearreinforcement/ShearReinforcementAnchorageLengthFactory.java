package tyvrel.mag.core.factory.shearreinforcement;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;

import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, that calculates anchorage length of shear reinforcement
 */
@SuppressWarnings("WeakerAccess")
public class ShearReinforcementAnchorageLengthFactory implements Factory<Double> {
	private final double phi;

	/**
	 * Creates instance of the factory
	 *
	 * @param phi diameter of shear reinforcement
	 */
	public ShearReinforcementAnchorageLengthFactory(double phi) {
		this.phi = phi;
	}

	/**
	 * Calculates and returns anchorage length in meters according to 8.5
	 *
	 * @return anchorage length in meters
	 * @throws ImproperDataException if data is improper
	 */
	@Override
	public Double build() throws ImproperDataException, LSException {
		return calculateLbd();
	}

	/**
	 * Calculates and returns anchorage length in meters according to 8.5
	 *
	 * @return anchorage length in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateLbd() throws ImproperDataException, LSException {
		pos(phi);
		return pos(Math.max(phi * 5, 0.050));
	}
}
