package tyvrel.mag.core.factory.shearreinforcement;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;

import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates maximum diameter of stirrup according to EN 1992-1-1 8.3
 */
@SuppressWarnings("WeakerAccess")
public class MaximumShearReinforcementDiameterFactory implements Factory<Double> {
	private final double fiB;
	private final double fiA;

	/**
	 * Creates instance of the factory
	 *
	 * @param fiB diameter of bottom reinforcement
	 * @param fiA diameter of top reinforcement
	 */
	public MaximumShearReinforcementDiameterFactory(double fiB, double fiA) {
		this.fiB = fiB;
		this.fiA = fiA;
	}

	/**
	 * Calculates and returns maximum diameter of stirrup in meters
	 *
	 * @return maximum diameter of stirrup in meters
	 * @throws ImproperDataException if data is improper
	 */
	@Override
	public Double build() throws ImproperDataException, LSException {
		return pos(this::calculateFiswmax);
	}

	/**
	 * Calculates and returns maximum diameter of stirrup in meters according to 8.3
	 *
	 * @return maximum diameter of stirrup in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateFiswmax() throws ImproperDataException, LSException {
		pos(fiA);
		pos(fiB);
		return Math.min(fiA, fiB);
	}
}
