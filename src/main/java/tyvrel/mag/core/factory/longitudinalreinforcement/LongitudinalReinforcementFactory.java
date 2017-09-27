package tyvrel.mag.core.factory.longitudinalreinforcement;

import tyvrel.mag.core.exception.CompressionTooHighException;
import tyvrel.mag.core.exception.HeightTooSmallException;
import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.Load;
import tyvrel.mag.core.model.Factors;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;

import static java.lang.Math.*;
import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates reinforcement of bended, rectangular beam using simplified method
 */
@SuppressWarnings("WeakerAccess")
public class LongitudinalReinforcementFactory implements Factory<LongitudinalReinforcement> {
	private final CrossSection crossSection;
	private final Load load;
	private final Factors factors;

	/**
	 * Creates an instance of the factory
	 *
	 * @param crossSection cross section
	 * @param load         load
	 * @param factors      partial factors
	 */
	public LongitudinalReinforcementFactory(CrossSection crossSection, Load load, Factors factors) {
		this.crossSection = crossSection;
		this.load = load;
		this.factors = factors;
	}

	private DoublePair ns;
	private double fcdeff;
	private double ksiEffLim;

	/**
	 * Calculates and returns longitudinal reinforcement
	 *
	 * @return longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public LongitudinalReinforcement build() throws ImproperDataException, LSException {
		return notNull(this::createLongitudinalReinforcement);
	}

	/**
	 * Calculates and returns longitudinal reinforcement
	 *
	 * @return longitudinal reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected LongitudinalReinforcement createLongitudinalReinforcement() throws ImproperDataException, LSException {
		return new LongitudinalReinforcement(notNull(this::createReinforcementB), notNull(this::createReinforcementA));
	}

	/**
	 * Calculates and returns bottom reinforcement
	 *
	 * @return bottom reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Reinforcement createReinforcementB() throws ImproperDataException, LSException {
		double fi = pos(() -> crossSection.getAs().getAsb().getPhi());
		double n = pos(() -> calculateN().b);
		return new Reinforcement(n, fi, 0, 0);
	}

	/**
	 * Calculates and returns top reinforcement
	 *
	 * @return top reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Reinforcement createReinforcementA() throws ImproperDataException, LSException {
		double fi = pos(() -> crossSection.getAs().getAsa().getPhi());
		double n = pos(() -> calculateN().a);
		return new Reinforcement(n, fi, 0, 0);
	}

	/**
	 * Calculates and returns numbers of rebars of reinforcement
	 *
	 * @return numbers of rebars of reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected DoublePair calculateN() throws ImproperDataException, LSException {
		if (ns == null) {
			DoublePair as1 = notNull(this::calculateAsb);
			DoublePair as2 = notNull(this::calculateAsa);
			DoublePair as = new DoublePair(nonNeg(() -> max(as1.b, as2.b)), nonNeg(() -> max(as1.a, as2.a)));
			ns = new DoublePair(
					nonNeg(max(2, ceil(nonNeg(as.b) / crossSection.getAs().getAsb().getAphi()))),
					nonNeg(max(2, ceil(nonNeg(as.a) / crossSection.getAs().getAsa().getAphi())))
			);
		}
		return ns;
	}

	/**
	 * Calculates and returns areas of reinforcement in m due to moment that tensions bottom of the cross section
	 *
	 * @return areas of reinforcement in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected DoublePair calculateAsb() throws ImproperDataException, LSException {
		double ksiEff = nonNeg(this::calculateXieffb);
		double d = pos(crossSection::getDb);
		double a = pos(crossSection::getAa);
		return notNull(() -> calculateAs(nonNeg(load::getMedb), ksiEff, d, a));
	}

	/**
	 * Calculates and returns areas of reinforcement in m due to moment that tensions top of the cross section
	 *
	 * @return areas of reinforcement in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected DoublePair calculateAsa() throws ImproperDataException, LSException {
		double ksiEff = nonNeg(this::calculateXieffa);
		double d = pos(crossSection::getDa);
		double a = pos(crossSection::getAb);
		DoublePair as = notNull(() -> calculateAs(nonNeg(load::getMeda), ksiEff, d, a));
		return new DoublePair(as.a, as.b);
	}

	/**
	 * Calculates and returns relative effective limit compressed height
	 *
	 * @return relative effective limit compressed height
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateXiefflim() throws ImproperDataException, LSException {
		if (ksiEffLim == 0) {
			double eS = pos(() -> crossSection.getLongitudinalReinforcementSteel().getEs());
			double fYd = pos(this::calculateFyd);
			double epsilonC = pos(() -> crossSection.getConcreteClassification().getEpsiloncu3());
			double lambda = pos(this::calculateLambda);
			ksiEffLim = pos(() -> lambda * (epsilonC / (epsilonC + fYd / eS)));
		}
		return ksiEffLim;
	}

	/**
	 * Calculates and returns design yield strength in Pa
	 *
	 * @return design yield strength in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateFyd() throws ImproperDataException, LSException {
		double gammaS = pos(factors::getGammas);
		double fy = pos(() -> crossSection.getLongitudinalReinforcementSteel().getFy());
		return pos(() -> fy / gammaS);
	}

	/**
	 * Calculates and returns relative effective compressed height due to moment that tensions bottom of the cross
	 * section
	 *
	 * @return relative effective compressed height
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateXieffb() throws ImproperDataException, LSException {
		return nonNeg(() -> calculateXieff(nonNeg(this::calculateMuEffb)));
	}

	/**
	 * Calculates and returns relative effective compressed height due to moment that tensions top of the cross section
	 *
	 * @return relative effective compressed height
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateXieffa() throws ImproperDataException, LSException {
		return nonNeg(() -> calculateXieff(nonNeg(this::calculateMuEffa)));
	}

	/**
	 * Calculates and returns lambda coefficient
	 *
	 * @return lambda coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateLambda() throws ImproperDataException, LSException {
		double fck = pos(() -> crossSection.getConcreteClassification().getFck());
		return (fck <= 50000000) ? 0.8 : pos(() -> 0.8 - (fck - 50000000) / 400000000);
	}

	/**
	 * Calculates and returns mu coefficient due to moment that tensions bottom of the cross section
	 *
	 * @return mu coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateMuEffb() throws ImproperDataException, LSException {
		return nonNeg(() -> calculateMuEff(pos(crossSection::getDb), nonNeg(load::getMedb)));
	}

	/**
	 * Calculates and returns mu coefficient due to moment that tensions top of the cross section
	 *
	 * @return mu coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateMuEffa() throws ImproperDataException, LSException {
		return nonNeg(() -> calculateMuEff(pos(crossSection::getDa), nonNeg(load::getMeda)));
	}

	/**
	 * Calculates and returns design effective compressive strength of concrete in Pa
	 *
	 * @return design effective compressive strength of concrete
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateFcdeff() throws ImproperDataException, LSException {
		if (fcdeff == 0) {
			fcdeff = pos(() -> pos(this::calculateEta) * pos(this::calculateFcd));
		}
		return fcdeff;
	}

	/**
	 * Calculates and returns eta coefficient
	 *
	 * @return eta coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateEta() throws ImproperDataException, LSException {
		double fck = pos(() -> crossSection.getConcreteClassification().getFck());
		return (fck <= 50000000) ? 1.0 : pos(() -> 1.0 - (fck - 50000000) / 200000000);
	}

	/**
	 * Calculates and returns design compressive strength of concrete in Pa
	 *
	 * @return design compressive strength of concrete
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateFcd() throws ImproperDataException, LSException {
		double gammaC = pos(factors::getGammac);
		double fck = pos(() -> crossSection.getConcreteClassification().getFck());
		return pos(() -> fck / gammaC);
	}

	private double calculateMuEff(double d, double m) throws ImproperDataException, LSException {
		nonNeg(m);
		double fcdeff = pos(this::calculateFcdeff);
		double b = pos(crossSection.getShape().getB());
		pos(d);
		return nonNeg(() -> m / (fcdeff * b * d * d));
	}

	private double calculateXieff(double muEff) throws ImproperDataException, LSException {
		if (muEff > 0.5)
			throw new CompressionTooHighException("effective compressed area is larger than area of concrete");
		double lambda = pos(this::calculateLambda);
		return nonNeg(() -> lambda * (1 - sqrt(pos(1 - 2 * muEff))));
	}

	private DoublePair calculateAs(double med, double ksieff, double d, double a) throws ImproperDataException,
			LSException {
		double ksiEffLim = nonNeg(this::calculateXiefflim);
		nonNeg(ksieff);
		pos(d);
		return (ksieff <= ksiEffLim) ? notNull(() -> calculateNotHeavilyLoadedAs(d, ksieff))
				: notNull(() -> calculateHeavilyLoadedAs(nonNeg(med), d, pos(a)));
	}

	private DoublePair calculateNotHeavilyLoadedAs(double d, double ksiEff) throws ImproperDataException, LSException {
		double fYd = pos(this::calculateFyd);
		nonNeg(ksiEff);
		double b = pos(crossSection.getShape().getB());
		double fCd = pos(this::calculateFcd);
		pos(d);
		return new DoublePair(nonNeg(() -> ksiEff * d * b * fCd / fYd), 0);
	}

	private DoublePair calculateHeavilyLoadedAs(double mSd, double d, double a) throws ImproperDataException
			, LSException {
		double fYd = pos(this::calculateFyd);
		double ksiEffLim = nonNeg(this::calculateXiefflim);
		pos(d);
		double b = pos(crossSection.getShape().getB());
		double fCd = pos(this::calculateFcd);
		pos(a);
		nonNeg(mSd);
		if (d - a <= 0) throw new HeightTooSmallException("crosssection is too small to fit shearreinforcement");
		double mRdStar = pos(() -> ksiEffLim * (1 - 0.5 * ksiEffLim) * d * d * b * fCd);
		double deltaM = pos(() -> mSd - mRdStar);
		double aSStar = pos(() -> ksiEffLim * d * b * fCd / fYd);
		double aSStarStar = pos(() -> deltaM / (fYd * (d - a)));
		return new DoublePair(pos(aSStar + aSStarStar), pos(aSStarStar));
	}

	/**
	 * Representation of pair of doubles
	 */
	protected static class DoublePair {
		/**
		 * Value connected with the top of the cross section
		 */
		protected final double a;
		/**
		 * Value connected with the bottom of the cross section
		 */
		protected final double b;

		/**
		 * Creates an instance of pair of doubles
		 *
		 * @param b value connected with the top of the cross section
		 * @param a value connected with the bottom of the cross section
		 */
		public DoublePair(double b, double a) {
			this.a = a;
			this.b = b;
		}
	}
}
