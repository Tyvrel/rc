package tyvrel.mag.core.factory.longitudinalreinforcement;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.factory.classification.ConcreteClassificationFactory;
import tyvrel.mag.core.model.Factors;
import tyvrel.mag.core.model.classification.Steel;
import tyvrel.mag.core.model.classification.ConcreteClassification;

import static tyvrel.mag.core.exception.Precondition.notNull;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates required anchorage length according to EN 1992-1-1 8.4.3
 */
@SuppressWarnings("WeakerAccess")
public class LongitudinalReinforcementRequiredAnchorageLengthFactory implements Factory<Double> {
	private final ConcreteClassification concreteClassification;
	private final Factors factors;
	private final double phi;
	private final int bondConditionType;
	private final Steel steel;

	/**
	 * Type that indicates good boon conditions
	 */
	public static final int GOOD_BOND_CONDITIONS = 0;
	/**
	 * Type that indicates bad boon conditions
	 */
	public static final int BAD_BOND_CONDITIONS = 1;

	/**
	 * Creates instance of the factory
	 *
	 * @param concreteClassification concrete classification of cross section
	 * @param steel                  steel of reinforcement
	 * @param factors                partials factors
	 * @param phi                    diameter of reinforcement in meters
	 * @param bondConditionType      type of bond conditions
	 */
	public LongitudinalReinforcementRequiredAnchorageLengthFactory(ConcreteClassification concreteClassification,
	                                                               Steel steel, Factors factors,
	                                                               double phi, int bondConditionType) {
		this.concreteClassification = concreteClassification;
		this.factors = factors;
		this.phi = phi;
		this.steel = steel;
		this.bondConditionType = bondConditionType;
	}

	/**
	 * Calculates and returns basic required anchorage length in meters according to 8.4.3
	 *
	 * @return basic required anchorage length in meters
	 * @throws ImproperDataException if data is improper
	 */
	@Override
	public Double build() throws ImproperDataException, LSException {
		return calculateLbrqd();
	}

	/**
	 * Calculates and returns basic required anchorage length in meters according to 8.4.3
	 *
	 * @return basic required anchorage length in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateLbrqd() throws ImproperDataException, LSException {
		pos(phi);
		double sigmaSd = pos(calculateSigmasd());
		double fbd = pos(calculateFbd());
		return pos((phi / 4) * (sigmaSd / fbd));
	}

	/**
	 * Calculates and returns design stress of the bar in Pa according to 8.4.3
	 *
	 * @return design stress of the bar in Pa
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateSigmasd() throws ImproperDataException, LSException {
		double fy = pos(notNull(steel).getFy());
		double gammaS = pos(notNull(factors).getGammas());
		return pos(fy / gammaS);
	}

	/**
	 * Calculates and returns ultimate bond stress in Pa according to 8.4.2
	 *
	 * @return ultimate bond stress in Pa
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateFbd() throws ImproperDataException, LSException {
		double eta1 = pos(calculateEta1());
		double eta2 = pos(calculateEta2());
		double fctd = pos(calculateFctd());
		return pos(2.25 * eta1 * eta2 * fctd);
	}

	/**
	 * Calculates and returns design tensile strength in Pa according to 3.1.6
	 *
	 * @return design tensile strength in Pa
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateFctd() throws ImproperDataException, LSException {
		double alfaCt = pos(calculateAlphaCt());
		double fctk005 = pos(notNull(concreteClassification).getFctk005());
		fctk005 = Math.min(fctk005, new ConcreteClassificationFactory().get("C60/75")
				.getFctk005());
		double gammaC = pos(notNull(factors).getGammac());
		return pos(alfaCt * fctk005 / gammaC);
	}

	/**
	 * Calculates and returns alphact coefficient according to 3.1.6
	 *
	 * @return alphact
	 * @throws ImproperDataException never
	 */
	protected double calculateAlphaCt() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates and returns eta1 coefficient according to 8.4.2
	 *
	 * @return eta1 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateEta1() throws ImproperDataException, LSException {
		switch (bondConditionType) {
			case GOOD_BOND_CONDITIONS:
				return 1;
			case BAD_BOND_CONDITIONS:
				return 0.7;
			default:
				throw new ImproperDataException();
		}
	}

	/**
	 * Calculates and returns eta2 coefficient according to 8.4.2
	 *
	 * @return eta2 coefficient
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateEta2() throws ImproperDataException, LSException {
		pos(phi);
		return (phi <= 0.032) ? 1 : (132 - phi * 1000) / 100;
	}
}
