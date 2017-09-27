package tyvrel.mag.core.factory.stress;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.Stress;

import static java.lang.Math.sqrt;
import static tyvrel.mag.core.exception.Precondition.nonNeg;
import static tyvrel.mag.core.exception.Precondition.notNull;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates stress of the cross section
 */
@SuppressWarnings("WeakerAccess")
public class CrackedStressFactory implements Factory<Stress> {
	private final CrossSection crossSection;
	private final double ma;
	private final double mb;
	private final double phi;

	/**
	 * Creates an instance of the factory
	 *
	 * @param crossSection cross section
	 * @param ma           bending moment that tensions top of the cross section in kNm
	 * @param mb           bending moment that tensions bottom of the cross section in kNm
	 * @param phi          creep coefficient
	 */
	public CrackedStressFactory(CrossSection crossSection, double ma, double mb, double phi) {
		this.crossSection = crossSection;
		this.ma = ma;
		this.mb = mb;
		this.phi = phi;
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
		return notNull(this::calculateStress);
	}

	/**
	 * Calculates and returns stress
	 *
	 * @return stress
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Stress calculateStress() throws ImproperDataException, LSException {
		double es = pos(() -> crossSection.getLongitudinalReinforcementSteel().getEs());
		double eceff = pos(this::calculateEceff);
		double asa = nonNeg(() -> crossSection.getAs().getAsa().getA());
		double asb = nonNeg(() -> crossSection.getAs().getAsb().getA());
		double da = pos(crossSection::getDa);
		double db = pos(crossSection::getDb);
		double ya = pos(() -> calculateYcracked(asa, es, da, eceff));
		double yb = pos(() -> calculateYcracked(asb, es, db, eceff));
		Stress stress1 = calculateStress(ma, da, ya, asa, eceff);
		Stress stress2 = calculateStress(mb, db, yb, asb, eceff);
		return Stress.max(stress1, stress2);
	}

	/**
	 * Calculates and returns effective modulus of elasticity in Pa according to formula 7.20
	 *
	 * @return effective modulus of elasticity in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateEceff() throws ImproperDataException, LSException {
		double ecm = pos(() -> crossSection.getConcreteClassification().getEcm());
		nonNeg(phi);
		return pos(ecm / pos(phi + 1.0));
	}

	private Stress calculateStress(double m, double d, double y, double as, double eceff) throws ImproperDataException,
			LSException {
		double es = pos(() -> crossSection.getLongitudinalReinforcementSteel().getEs());
		double is = pos(() -> calculateIs(as, y, d, eceff, es));
		double ic = pos(() -> calculateIc(is, es, eceff));
		double sigmac = nonNeg(calculateSigma(m, y, ic));
		double sigmas = nonNeg(calculateSigma(m, d - y, is));
		return new Stress(sigmac, sigmas);
	}

	private double calculateSigma(double m, double y, double i) throws ImproperDataException, LSException {
		nonNeg(m);
		pos(y);
		pos(i);
		return nonNeg(() -> m * y / i);
	}

	private double calculateYcracked(double as, double es, double d, double eceff) throws ImproperDataException,
			LSException {
		nonNeg(as);
		pos(es);
		pos(d);
		double b = pos(() -> crossSection.getShape().getB());
		pos(eceff);
		return nonNeg((-as * es + sqrt(nonNeg(as * as * es * es + 2.0 * b * as * es * eceff * d))) / pos(b * eceff));
	}

	private double calculateIc(double is, double es, double eceff) throws
			ImproperDataException, LSException {
		pos(is);
		pos(es);
		pos(eceff);
		return pos(is * es / eceff);
	}

	private double calculateIs(double as, double y, double d, double eceff, double es) throws
			ImproperDataException, LSException {
		pos(es);
		pos(d);
		pos(as);
		pos(eceff);
		pos(y);
		double b = pos(() -> crossSection.getShape().getB());
		return pos(as * (d - y) * (d - y) + 1.0 / 3.0 * eceff / es * b * y * y * y);
	}
}
