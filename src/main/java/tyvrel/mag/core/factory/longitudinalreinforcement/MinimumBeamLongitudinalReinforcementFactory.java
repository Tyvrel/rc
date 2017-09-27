package tyvrel.mag.core.factory.longitudinalreinforcement;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;

import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static tyvrel.mag.core.exception.Precondition.nonNeg;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates minimum longitudinal reinforcement for beams according to EN 1992-1-1 by 9.2.1.1, with and
 * assumption there is no cracking considered.
 */
@SuppressWarnings("WeakerAccess")
public class MinimumBeamLongitudinalReinforcementFactory implements Factory<LongitudinalReinforcement> {
	private final CrossSection crossSection;

	/**
	 * Creates instance of the factory
	 *
	 * @param crossSection a cross section for which minimum longitudinal reinforcement will be calculated
	 */
	public MinimumBeamLongitudinalReinforcementFactory(CrossSection crossSection) {
		this.crossSection = crossSection;
	}

	/**
	 * Calculates and returns minimal longitudinal reinforcement
	 *
	 * @return calculated minimal longitudinal reinforcement
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
	 * Returns minimal bottom longitudinal reinforcement
	 *
	 * @return minimal bottom longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected Reinforcement createReinforcementb() throws ImproperDataException, LSException {
		double phi = pos(() -> crossSection.getAs().getAsb().getPhi());
		double n = pos(this::calculateNb);
		return new Reinforcement(n, phi, 0, 0);
	}

	/**
	 * Returns minimal top longitudinal reinforcement
	 *
	 * @return minimal top longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected Reinforcement createReinforcementa() throws ImproperDataException, LSException {
		double phi = pos(() -> crossSection.getAs().getAsa().getPhi());
		double n = pos(this::calculateNa);
		return new Reinforcement(n, phi, 0, 0);
	}

	/**
	 * Returns minimal number of rebars of bottom longitudinal reinforcement
	 *
	 * @return minimal number of rebars of bottom longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateNb() throws ImproperDataException, LSException {
		double asmin = pos(this::calculateAsminb);
		double rebarArea = pos(() -> crossSection.getAs().getAsb().getAphi());
		return nonNeg(() -> max(2, ceil(asmin / rebarArea)));
	}

	/**
	 * Returns minimal number of rebars of top longitudinal reinforcement
	 *
	 * @return minimal number of rebars of top longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateNa() throws ImproperDataException, LSException {
		double asmin = pos(this::calculateAsmina);
		double rebarArea = pos(() -> crossSection.getAs().getAsa().getAphi());
		return nonNeg(() -> max(2, ceil(asmin / rebarArea)));
	}

	/**
	 * Returns minimal area of bottom longitudinal reinforcement in m2 using formula 9.1N
	 *
	 * @return minimal area of bottom longitudinal reinforcement in m2
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAsminb() throws ImproperDataException, LSException {
		double db = pos(crossSection::getDb);
		double asmina = nonNeg(() -> calculateAsmin1(db));
		double asminb = nonNeg(() -> calculateAsmin2(db));
		return max(asmina, asminb);
	}

	/**
	 * Returns minimal area of top longitudinal reinforcement in m2 using formula 9.1N
	 *
	 * @return minimal area of top longitudinal reinforcement in m2
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAsmina() throws ImproperDataException, LSException {
		double db = pos(crossSection::getDa);
		double asmina = nonNeg(() -> calculateAsmin1(db));
		double asminb = nonNeg(() -> calculateAsmin2(db));
		return max(asmina, asminb);
	}

	private double calculateAsmin1(double d) throws ImproperDataException, LSException {
		double fy = pos(() -> crossSection.getLongitudinalReinforcementSteel().getFy());
		double fctm = pos(() -> crossSection.getConcreteClassification().getfctm());
		double b = pos(() -> crossSection.getShape().getB());
		return pos(() -> 0.26 * fctm / fy * b * pos(d));
	}

	private double calculateAsmin2(double d) throws ImproperDataException, LSException {
		double b = pos(() -> crossSection.getShape().getB());
		return pos(() -> 0.0013 * b * pos(d));
	}
}
