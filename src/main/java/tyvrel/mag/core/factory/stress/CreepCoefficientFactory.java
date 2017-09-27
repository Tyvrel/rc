package tyvrel.mag.core.factory.stress;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.Stress;
import tyvrel.mag.core.model.classification.CementClassification;

import static java.lang.Math.exp;
import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates creep coefficient according to 3.1.4 EN 1992-1-1
 */
@SuppressWarnings("WeakerAccess")
public class CreepCoefficientFactory implements Factory<Double> {
	private final double philn;
	private final CrossSection crossSection;
	private final double maquasiperm;
	private final double mbquasiperm;
	private final double t0;
	private final int cementClassification;

	/**
	 * Creates and instance of the factory
	 *
	 * @param philn                linear creep coefficient
	 * @param crossSection         cross section
	 * @param maquasiperm          quasi-permanent bending moment that tensions top of the cross section in kNm
	 * @param mbquasiperm          quasi-permanent bending moment that tensions bottom of the cross section in kNm
	 * @param t0                   time of loading
	 * @param cementClassification classification of cement
	 */
	public CreepCoefficientFactory(double philn, CrossSection crossSection, double maquasiperm,
	                               double mbquasiperm, double t0, int cementClassification) {
		this.philn = philn;
		this.crossSection = crossSection;
		this.maquasiperm = maquasiperm;
		this.mbquasiperm = mbquasiperm;
		this.t0 = t0;
		this.cementClassification = cementClassification;
	}

	/**
	 * Calculates and returns creep coefficient
	 *
	 * @return creep coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	@Override
	public Double build() throws ImproperDataException, LSException {
		return calculatePhi();
	}

	/**
	 * Calculates and returns creep coefficient
	 *
	 * @return creep coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculatePhi() throws ImproperDataException, LSException {
		double phi = pos(philn);
		for (int i = 0; i < 100; i++) {
			double finalPhi = phi;
			double sigmac = nonNeg(() -> calculateStress(finalPhi).getSigmac());
			double phi2 = calculatePhi(sigmac, phi);
			if (phi2 > phi) phi = phi2;
			else break;
		}
		return phi;
	}

	/**
	 * Calculates and returns stress in cross section
	 *
	 * @param phi creep coefficient
	 * @return stress in cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Stress calculateStress(double phi) throws ImproperDataException, LSException {
		pos(phi);
		return notNull(() -> new StressFactory(crossSection.getConcreteClassification(),
				calculateUncrackedStress(phi),
				calculateCrackedStress(phi)).build());
	}

	/**
	 * Calculates and returns stress in uncracked cross section
	 *
	 * @param phi creep coefficient
	 * @return stress in uncracked cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Stress calculateUncrackedStress(double phi) throws ImproperDataException, LSException {
		return notNull(() -> new UncrackedStressFactory(crossSection, maquasiperm, mbquasiperm).build());
	}

	/**
	 * Calculates and returns stress in cracked cross section
	 *
	 * @param phi creep coefficient
	 * @return stress in cracked cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Stress calculateCrackedStress(double phi) throws ImproperDataException, LSException {
		pos(phi);
		return notNull(() -> new CrackedStressFactory(crossSection, maquasiperm, mbquasiperm, phi).build());
	}

	/**
	 * Calculates and returns characteristic compressive strength of concrete in Pa
	 *
	 * @return mean compressive strength of concrete in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateFckt0() throws ImproperDataException, LSException {
		if (t0 < 3) throw new ImproperDataException();
		return (t0 < 28) ? pos(() -> pos(this::calculateFcmt0) - 8000000)
				: pos(() -> crossSection.getConcreteClassification().getFck());
	}

	/**
	 * Calculates and returns mean compressive strength of concrete in Pa
	 *
	 * @return mean compressive strength of concrete in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateFcmt0() throws ImproperDataException, LSException {
		double betacc = pos(this::calculateBetacc);
		double fcm = pos(() -> crossSection.getConcreteClassification().getFcm());
		return pos(() -> betacc * fcm);
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
		pos(t0);
		return pos(() -> Math.exp(s * (1.0 - Math.sqrt(28.0 / t0))));
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


	private double calculatePhi(double sigmac, double phi) throws ImproperDataException, LSException {
		pos(phi);
		nonNeg(sigmac);
		if (isCreepLinear(sigmac)) {
			return phi;
		} else {
			double ksigma = pos(() -> calculateKsigma(sigmac));
			return pos(() -> phi * exp(1.5 * pos(ksigma - 0.45)));
		}
	}

	private boolean isCreepLinear(double sigmac) throws ImproperDataException, LSException {
		nonNeg(sigmac);
		double fck = pos(this::calculateFckt0);
		return sigmac < pos(() -> 0.45 * fck);
	}

	private double calculateKsigma(double sigmac) throws ImproperDataException, LSException {
		nonNeg(sigmac);
		double fck = pos(this::calculateFckt0);
		return nonNeg(() -> sigmac / fck);
	}
}
