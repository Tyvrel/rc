package tyvrel.mag.core.factory.stress;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.Stress;
import tyvrel.mag.core.model.classification.ConcreteClassification;

import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates stress in the cross section
 */
@SuppressWarnings("WeakerAccess")
public class StressFactory implements Factory<Stress> {
	private final ConcreteClassification concreteClassification;
	private final Stress uncrackedStress;
	private final Stress crackedStress;

	/**
	 * Creates an instance of the factory
	 *
	 * @param concreteClassification concrete classification of the cross section
	 * @param uncrackedStress        stress in uncracked cross section
	 * @param crackedStress          stress in cracked cross section
	 */
	public StressFactory(ConcreteClassification concreteClassification, Stress uncrackedStress, Stress crackedStress) {
		this.concreteClassification = concreteClassification;
		this.uncrackedStress = uncrackedStress;
		this.crackedStress = crackedStress;
	}

	/**
	 * Calculates and returns stress
	 *
	 * @return stress
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	@Override
	public Stress build() throws ImproperDataException, LSException {
		return calculateStress();
	}

	/**
	 * Calculates and returns stress
	 *
	 * @return stress
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Stress calculateStress() throws ImproperDataException, LSException {
		notNull(uncrackedStress);
		notNull(crackedStress);
		double sigmac = nonNeg(uncrackedStress::getSigmac);
		double fctm = pos(concreteClassification::getfctm);

		return (sigmac > fctm) ? crackedStress : uncrackedStress;
	}
}
