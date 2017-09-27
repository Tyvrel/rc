package tyvrel.mag.core.factory.longitudinalreinforcement;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.factory.others.MaximumPermittedStressFactory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.DoublePair;
import tyvrel.mag.core.model.classification.AbstractClassification;
import tyvrel.mag.core.model.classification.CementClassification;
import tyvrel.mag.core.model.classification.ExposureClassification;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates minimum longitudinal reinforcement factory according to EN 1992-1-1 7.3.2
 */
@SuppressWarnings("WeakerAccess")
public class MinimumCrackLongitudinalReinforcementFactory implements Factory<LongitudinalReinforcement> {

	private final CrossSection crossSection;
	private final double t;
	private final int cementClassification;
	private final ExposureClassification[] exposureClassifications;

	private double fcteff = 0;
	private DoublePair sigmas = null;

	/**
	 * Creates instance of the factory
	 *
	 * @param crossSection            cross section
	 * @param t                       age of concrete in days
	 * @param cementClassification    cement classification
	 * @param exposureClassifications exposure classifications
	 */
	public MinimumCrackLongitudinalReinforcementFactory(CrossSection crossSection, double t, int cementClassification,
	                                                    ExposureClassification[] exposureClassifications) {
		this.crossSection = crossSection;
		this.t = t;
		this.cementClassification = cementClassification;
		this.exposureClassifications = exposureClassifications;
	}

	/**
	 * Calculates and returns minimal longitudinal reinforcement
	 *
	 * @return minimal longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	@Override
	public LongitudinalReinforcement build() throws ImproperDataException, LSException {
		return notNull(calculateLongitudinalReinforcement());
	}

	/**
	 * Calculates and returns minimal longitudinal reinforcement
	 *
	 * @return minimal longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected LongitudinalReinforcement calculateLongitudinalReinforcement() throws ImproperDataException,
			LSException {
		return notNull(() -> new LongitudinalReinforcement(calculateReinforcementb(), calculateReinforcementa()));
	}

	/**
	 * Calculates and returns minimal bottom longitudinal reinforcement
	 *
	 * @return minimal bottom longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Reinforcement calculateReinforcementb() throws ImproperDataException, LSException {
		double phi = nonNeg(() -> crossSection.getAs().getAsb().getPhi());
		double l0 = nonNeg(() -> crossSection.getAs().getAsb().getL0());
		double lbd = nonNeg(() -> crossSection.getAs().getAsb().getLbd());
		return notNull(() -> new Reinforcement(calculateNb(), phi, lbd, l0));
	}

	/**
	 * Calculates and returns minimal top longitudinal reinforcement
	 *
	 * @return minimal top longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Reinforcement calculateReinforcementa() throws ImproperDataException, LSException {
		double phi = nonNeg(() -> crossSection.getAs().getAsa().getPhi());
		double l0 = nonNeg(() -> crossSection.getAs().getAsa().getL0());
		double lbd = nonNeg(() -> crossSection.getAs().getAsa().getLbd());
		return notNull(() -> new Reinforcement(calculateNa(), phi, lbd, l0));
	}

	/**
	 * Calculates and returns number of rebars of bottom reinforcement
	 *
	 * @return number of rebars of bottom reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateNb() throws ImproperDataException, LSException {
		double area = nonNeg(this::calculateAreab);
		double areaPhi = pos(() -> crossSection.getAs().getAsb().getAphi());
		return nonNeg(Math.max(2, Math.ceil(area / areaPhi)));
	}

	/**
	 * Calculates and returns number of rebars of top reinforcement
	 *
	 * @return number of rebars of top reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateNa() throws ImproperDataException, LSException {
		double area = nonNeg(this::calculateAreaa);
		double areaPhi = pos(() -> crossSection.getAs().getAsa().getAphi());
		return nonNeg(Math.max(2, Math.ceil(area / areaPhi)));
	}

	/**
	 * Calculates and returns area of bottom reinforcement in m2
	 *
	 * @return area of bottom reinforcement in m2
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAreab() throws ImproperDataException, LSException {
		double sigmas = nonNeg(this::calculateSigmasb);
		return nonNeg(() -> calculateArea(sigmas));
	}

	/**
	 * Calculates and returns area of top shearreinforcement in m2
	 *
	 * @return area of top shearreinforcement in m2
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAreaa() throws ImproperDataException, LSException {
		double sigmas = nonNeg(this::calculateSigmasa);
		return nonNeg(() -> calculateArea(sigmas));
	}

	/**
	 * Calculates and returns kc coefficient
	 *
	 * @return kc coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateKc() throws ImproperDataException, LSException {
		return 0.4;
	}

	/**
	 * Calculates and returns k coefficient
	 *
	 * @return k coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateK() throws ImproperDataException, LSException {
		double h = pos(() -> crossSection.getShape().getH());
		double b = pos(() -> crossSection.getShape().getB());
		double x = Math.max(h, b);
		if (x < 0.3) return 1;
		if (x >= 0.8) return 0.65;
		return pos(() -> 0.65 + 0.35 * (0.8 - x) / 0.5);
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
	 * Calculates and returns area of tensile zone in m2
	 *
	 * @return area of tensile zone in m2
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAct() throws ImproperDataException, LSException {
		return pos(() -> crossSection.getShape().getA() / 2);
	}

	/**
	 * Calculates and returns maximum permitted stress in top reinforcement in Pa
	 *
	 * @return maximum permitted stress in top reinforcement in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSigmasa() throws ImproperDataException, LSException {
		return nonNeg(() -> calculateSigmas().getA());
	}

	/**
	 * Calculates and returns maximum permitted stress in top reinforcement in Pa
	 *
	 * @return maximum permitted stress in top reinforcement in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSigmasb() throws ImproperDataException, LSException {
		return nonNeg(() -> calculateSigmas().getB());
	}

	protected DoublePair calculateSigmas() throws ImproperDataException, LSException {
		if (sigmas == null) {
			sigmas = notNull(() -> new MaximumPermittedStressFactory(crossSection, t, cementClassification,
					calculateWmax())
					.build());
		}
		return sigmas;
	}

	/**
	 * Calculates and returns maximum crack width in m
	 *
	 * @return maximum crack width in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateWmax() throws ImproperDataException, LSException {
		List<String> symbols = Arrays.stream(exposureClassifications)
				.filter(Objects::nonNull)
				.map(AbstractClassification::getSymbol)
				.filter(s -> !s.equals("X0"))
				.filter(s -> !s.equals("XC1"))
				.collect(Collectors.toList());
		if (symbols.isEmpty()) return 0.0004;
		else return 0.0003;
	}

	private double calculateArea(double sigmas) throws ImproperDataException, LSException {
		double kc = nonNeg(this::calculateKc);
		double k = nonNeg(this::calculateK);
		double fcteff = pos(this::calculateFcteff);
		double act = pos(this::calculateAct);
		if (sigmas == 0) return 0;
		pos(sigmas);
		return nonNeg(() -> kc * k * fcteff * act / sigmas);
	}
}
