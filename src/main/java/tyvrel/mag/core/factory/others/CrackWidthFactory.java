package tyvrel.mag.core.factory.others;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.factory.stress.CrackedStressFactory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.classification.CementClassification;

import static tyvrel.mag.core.exception.Precondition.nonNeg;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates crack width according to EN 1992-1-1 7.3.4
 */
@SuppressWarnings("WeakerAccess")
public class CrackWidthFactory implements Factory<Double> {
	private final CrossSection crossSection;
	private final double phi;
	private final double ma;
	private final double mb;
	private final double t;
	private final int cementClassification;

	private double fcteff = 0;

	/**
	 * Creates an instance of the factory
	 *
	 * @param crossSection         cross section
	 * @param phi                  creep coefficient
	 * @param ma                   bending moment that tensions top of the cross section in Nm
	 * @param mb                   bending moment that tensions bottom of the cross section in Nm
	 * @param t                    age of concrete in days
	 * @param cementClassification classification of cement
	 */
	public CrackWidthFactory(CrossSection crossSection, double phi, double ma, double mb, double t, int
			cementClassification) {
		this.crossSection = crossSection;
		this.phi = phi;
		this.ma = ma;
		this.mb = mb;
		this.t = t;
		this.cementClassification = cementClassification;
	}

	/**
	 * Calculates and returns crack width in m
	 *
	 * @return crack width in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	@Override
	public Double build() throws ImproperDataException, LSException {
		return nonNeg(this::calculateCrackWidth);
	}

	/**
	 * Calculates and returns crack width in m
	 *
	 * @return crack width in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCrackWidth() throws ImproperDataException, LSException {
		return nonNeg(() -> Math.max(calculateCrackWidtha(), calculateCrackWidthb()));
	}

	/**
	 * Calculates and returns crack width in the top of cross section in m
	 *
	 * @return crack width in the top of cross section in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCrackWidtha() throws ImproperDataException, LSException {
		double srmax = nonNeg(this::calculateSrmaxa);
		double deltaEpsilonm = nonNeg(this::calculateDeltaEpsilonMa);
		return nonNeg(() -> srmax * deltaEpsilonm);
	}

	/**
	 * Calculates and returns crack width in the bottom of cross section in m
	 *
	 * @return crack width in the bottom of cross section in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCrackWidthb() throws ImproperDataException, LSException {
		double srmax = nonNeg(this::calculateSrmaxb);
		double deltaEpsilonm = nonNeg(this::calculateDeltaEpsilonMb);
		return nonNeg(() -> srmax * deltaEpsilonm);
	}

	/**
	 * Calculates and returns maximum crack spacing in the top of cross section in m
	 *
	 * @return maximum crack spacing in the top of cross section in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSrmaxa() throws ImproperDataException, LSException {
		if (isAsaspacingHigh()) {
			double h = pos(() -> crossSection.getShape().getH());
			double neutralAxis = pos(() -> crossSection.getShape().getNeutralAxis());
			return pos(() -> 1.3 * (h - neutralAxis));
		} else {
			double cnom = pos(crossSection::getCnom);
			double k1 = pos(this::calculateK1);
			double k2 = pos(this::calculateK2);
			double k3 = pos(this::calculateK3);
			double k4 = pos(this::calculateK4);
			double ropeff = pos(this::calculateRhopeffa);
			double phi = pos(() -> crossSection.getAs().getAsa().getPhi());
			return pos(() -> k3 * cnom + k1 * k2 * k4 * phi / ropeff);
		}
	}

	/**
	 * Calculates and returns maximum crack spacing in the bottom of cross section in m
	 *
	 * @return maximum crack spacing in the bottom of cross section in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSrmaxb() throws ImproperDataException, LSException {
		if (isAsbspacingHigh()) {
			double h = pos(() -> crossSection.getShape().getH());
			double neutralAxis = pos(() -> crossSection.getShape().getNeutralAxis());
			return pos(() -> 1.3 * (h - neutralAxis));
		} else {
			double cnom = pos(crossSection::getCnom);
			double k1 = pos(this::calculateK1);
			double k2 = pos(this::calculateK2);
			double k3 = pos(this::calculateK3);
			double k4 = pos(this::calculateK4);
			double ropeff = pos(this::calculateRhopeffb);
			double phi = pos(() -> crossSection.getAs().getAsb().getPhi());
			return pos(() -> k3 * cnom + k1 * k2 * k4 * phi / ropeff);
		}
	}

	/**
	 * Calculates and returns k1 coefficient
	 *
	 * @return k1 coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateK1() throws ImproperDataException, LSException {
		return 0.8;
	}

	/**
	 * Calculates and returns k2 coefficient
	 *
	 * @return k2 coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateK2() throws ImproperDataException, LSException {
		return 0.5;
	}

	/**
	 * Calculates and returns k3 coefficient
	 *
	 * @return k3 coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateK3() throws ImproperDataException, LSException {
		return 3.4;
	}

	/**
	 * Calculates and returns k4 coefficient
	 *
	 * @return k4 coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateK4() throws ImproperDataException, LSException {
		return 0.425;
	}

	/**
	 * Calculates and returns if spacing of top shearreinforcement is considered high
	 *
	 * @return if spacing of top shearreinforcement is considered high
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected boolean isAsaspacingHigh() throws ImproperDataException, LSException {
		double asSpacing = pos(crossSection::getAsaSpacing);
		double phi = pos(() -> crossSection.getAs().getAsa().getPhi());
		double cnom = pos(crossSection::getCnom);
		double limitSpacing = pos(() -> 5 * cnom + phi / 2);
		return asSpacing > limitSpacing;
	}

	/**
	 * Calculates and returns if spacing of bottom shearreinforcement is considered high
	 *
	 * @return if spacing of bottom shearreinforcement is considered high
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected boolean isAsbspacingHigh() throws ImproperDataException, LSException {
		double asSpacing = pos(crossSection::getAsbSpacing);
		double phi = pos(() -> crossSection.getAs().getAsb().getPhi());
		double cnom = pos(crossSection::getCnom);
		double limitSpacing = pos(() -> 5 * cnom + phi / 2);
		return asSpacing > limitSpacing;
	}

	/**
	 * Calculates and returns difference between mean strain in shearreinforcement and mean strain in concrete in
	 * top of
	 * cross section
	 *
	 * @return difference between mean strain in shearreinforcement and mean strain in concrete in top of cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateDeltaEpsilonMa() throws ImproperDataException, LSException {
		double es = pos(() -> crossSection.getLongitudinalReinforcementSteel().getEs());
		double kt = pos(this::calculateKt);
		double fcteff = pos(this::calculateFcteff);
		double ropeff = pos(this::calculateRhopeffa);
		double alphae = pos(this::calculateAlphae);
		double sigmas = nonNeg(this::calculateSigmasa);
		double deltaEpsilonM1 = nonNeg(() -> Math.max(0, (sigmas - kt * fcteff / ropeff * (1.0 + alphae * ropeff)) /
				es));
		double deltaEpsilonM2 = nonNeg(() -> Math.max(0, 0.6 * sigmas / es));
		return nonNeg(() -> Math.max(deltaEpsilonM1, deltaEpsilonM2));
	}

	/**
	 * Calculates and returns difference between mean strain in shearreinforcement and mean strain in concrete in
	 * bottom of
	 * cross section
	 *
	 * @return difference between mean strain in shearreinforcement and mean strain in concrete in bottom of cross
	 * section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateDeltaEpsilonMb() throws ImproperDataException, LSException {
		double es = pos(() -> crossSection.getLongitudinalReinforcementSteel().getEs());
		double kt = pos(this::calculateKt);
		double fcteff = pos(this::calculateFcteff);
		double ropeff = pos(this::calculateRhopeffb);
		double alphae = pos(this::calculateAlphae);
		double sigmas = nonNeg(this::calculateSigmasb);
		double deltaEpsilonM1 = nonNeg(() -> Math.max(0, (sigmas - kt * fcteff / ropeff * (1 + alphae * ropeff)) /
				es));
		double deltaEpsilonM2 = nonNeg(() -> Math.max(0, 0.6 * sigmas / es));
		return nonNeg(() -> Math.max(deltaEpsilonM1, deltaEpsilonM2));
	}

	/**
	 * Calculates and returns stress in top shearreinforcement in Pa
	 *
	 * @return stress in top shearreinforcement in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSigmasa() throws ImproperDataException, LSException {
		return nonNeg(() -> new CrackedStressFactory(crossSection, ma, 0, phi).build().getSigmas());
	}

	/**
	 * Calculates and returns stress in bottom shearreinforcement in Pa
	 *
	 * @return stress in bottom shearreinforcement in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSigmasb() throws ImproperDataException, LSException {
		return nonNeg(() -> new CrackedStressFactory(crossSection, 0, mb, phi).build().getSigmas());
	}

	/**
	 * Calculates and returns kt coefficient
	 *
	 * @return kt coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateKt() throws ImproperDataException, LSException {
		return 0.4;
	}

	/**
	 * Calculates and returns effective tensile strength of concrete in Pa
	 *
	 * @return effective tensile strength of concrete in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateFcteff() throws ImproperDataException, LSException {
		if (fcteff == 0) fcteff = pos(this::calculateFctm);
		return fcteff;
	}

	/**
	 * Calculates and returns mean tensile strength of concrete in Pa
	 *
	 * @return mean tensile strength of concrete in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateFctm() throws ImproperDataException, LSException {
		double fctmt = pos(() -> crossSection.getConcreteClassification().getfctm());
		double betacc = pos(this::calculateBetacc);
		double alpha = pos(this::calculateAlpha);
		return pos(() -> Math.pow(betacc, alpha) * fctmt);

	}

	/**
	 * Calculates and returns betacc coefficient
	 *
	 * @return betacc coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateBetacc() throws ImproperDataException, LSException {
		double s = pos(this::calculateS);
		pos(t);
		return pos(() -> Math.exp(s * (1.0 - Math.sqrt(28.0 / t))));
	}

	/**
	 * Calculates and returns s coefficient
	 *
	 * @return s coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateS() throws ImproperDataException, LSException {
		switch (cementClassification) {
			case CementClassification.CEMENT_S:
				return 0.38;
			case CementClassification.CEMENT_N:
				return 0.25;
			case CementClassification.CEMENT_R:
				return 0.20;
			default:
				throw new ImproperDataException();
		}
	}

	/**
	 * Calculates and returns alpha coefficient
	 *
	 * @return alpha coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAlpha() throws ImproperDataException, LSException {
		pos(t);
		if (t < 28) return 1.0;
		else return 0.667;
	}

	/**
	 * Calculates and returns alphae coefficient
	 *
	 * @return alphae coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAlphae() throws ImproperDataException, LSException {
		double es = pos(() -> crossSection.getLongitudinalReinforcementSteel().getEs());
		double ecm = pos(() -> crossSection.getConcreteClassification().getEcm());
		return pos(() -> es / ecm);
	}

	/**
	 * Calculates and returns ropeff coefficient of top of cross section
	 *
	 * @return ropeff coefficient of top of cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateRhopeffa() throws ImproperDataException, LSException {
		double as = pos(() -> crossSection.getAs().getAsa().getA());
		double aceff = pos(this::calculateAceffa);
		return pos(() -> as / aceff);
	}

	/**
	 * Calculates and returns ropeff coefficient of top of cross section
	 *
	 * @return ropeff coefficient of top of cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateRhopeffb() throws ImproperDataException, LSException {
		double as = pos(() -> crossSection.getAs().getAsb().getA());
		double aceff = pos(this::calculateAceffb);
		return pos(() -> as / aceff);
	}

	/**
	 * Calculates and returns effective tension area of concrete in m2
	 *
	 * @return effective tension area of concrete in m2
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAceffa() throws ImproperDataException, LSException {
		double b = pos(() -> crossSection.getShape().getB());
		return pos(() -> b * calculateHcefa());
	}

	/**
	 * Calculates and returns effective tension area of concrete in m2
	 *
	 * @return effective tension area of concrete in m2
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAceffb() throws ImproperDataException, LSException {
		double b = pos(() -> crossSection.getShape().getB());
		return pos(() -> b * calculateHcefb());
	}

	/**
	 * Calculates and returns effective tension height of concrete in m
	 *
	 * @return effective tension height of concrete in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateHcefa() throws ImproperDataException, LSException {
		double h = pos(() -> crossSection.getShape().getH());
		double d = pos(crossSection::getDa);
		double x = pos(() -> crossSection.getShape().getNeutralAxis());
		return pos(() -> Math.min(2.5 * (h - d), Math.min((h - x) / 3.0, h / 2.0)));
	}

	/**
	 * Calculates and returns effective tension height of concrete in m
	 *
	 * @return effective tension height of concrete in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateHcefb() throws ImproperDataException, LSException {
		double h = pos(() -> crossSection.getShape().getH());
		double d = pos(crossSection::getDb);
		double x = pos(() -> h - crossSection.getShape().getNeutralAxis());
		return pos(() -> Math.min(2.5 * (h - d), Math.min((h - x) / 3.0, h / 2.0)));
	}
}
