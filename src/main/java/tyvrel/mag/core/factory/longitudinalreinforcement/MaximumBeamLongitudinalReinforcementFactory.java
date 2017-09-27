package tyvrel.mag.core.factory.longitudinalreinforcement;


import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.Shape;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;

import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates maximum longitudinal reinforcement for beams according to EN 1992-1-1 9.2.1.1
 */
@SuppressWarnings("WeakerAccess")
public class MaximumBeamLongitudinalReinforcementFactory implements Factory<LongitudinalReinforcement> {
	private final LongitudinalReinforcement as;
	private final Shape shape;

	/**
	 * Creates instance of the factory
	 *
	 * @param as    longitudinal reinforcement which minimum should be found
	 * @param shape shape of the cross section
	 */
	public MaximumBeamLongitudinalReinforcementFactory(LongitudinalReinforcement as, Shape shape) {
		this.as = as;
		this.shape = shape;
	}

	/**
	 * Calculates and returns maximal longitudinal reinforcement
	 *
	 * @return calculated maximal longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	@Override
	public LongitudinalReinforcement build() throws ImproperDataException, LSException {
		return new LongitudinalReinforcement(
				createReinforcementb(),
				createReinforcementa()
		);
	}

	/**
	 * Returns maximal bottom longitudinal reinforcement
	 *
	 * @return maximal bottom longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected Reinforcement createReinforcementb() throws ImproperDataException, LSException {
		Reinforcement as = notNull(this.as::getAsb);
		double fi = as.getPhi();
		double n = pos(calculateNb());
		double lbd = as.getLbd();
		double l0 = as.getL0();
		return new Reinforcement(n, fi, lbd, l0);
	}

	/**
	 * Returns maximal top longitudinal reinforcement
	 *
	 * @return maximal top longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected Reinforcement createReinforcementa() throws ImproperDataException, LSException {
		Reinforcement as = notNull(this.as::getAsa);
		double fi = as.getPhi();
		double n = pos(calculateNa());
		double lbd = as.getLbd();
		double l0 = as.getL0();
		return new Reinforcement(n, fi, lbd, l0);
	}

	/**
	 * Returns maximal number of rebars of top longitudinal reinforcement
	 *
	 * @return maximal number of rebars of top longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateNa() throws ImproperDataException, LSException {
		double asmax = notNull(this::calculateAsmax);
		double rebarArea = pos(() -> as.getAsa().getAphi());
		return nonNeg(() -> Math.floor(asmax / rebarArea));
	}

	/**
	 * Returns maximal number of rebars of bottom longitudinal reinforcement
	 *
	 * @return maximal number of rebars of bottom longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateNb() throws ImproperDataException, LSException {
		double asmax = notNull(this::calculateAsmax);
		double rebarArea = pos(() -> as.getAsb().getAphi());
		return nonNeg(() -> Math.floor(asmax / rebarArea));
	}

	/**
	 * Returns maximal area of longitudinal reinforcement in m2 using formula 9.1N
	 *
	 * @return maximal area of longitudinal reinforcement in m2
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAsmax() throws ImproperDataException, LSException {
		return nonNeg(() -> 0.04 * shape.getA());
	}
}
