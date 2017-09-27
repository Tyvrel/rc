package tyvrel.mag.core.factory.stress;


import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.Shape;
import tyvrel.mag.core.model.classification.CementClassification;
import tyvrel.mag.core.model.classification.ConcreteClassification;

import static java.lang.Math.*;
import static tyvrel.mag.core.exception.Precondition.*;
import static tyvrel.mag.core.model.classification.CementClassification.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates and returns linear creep coefficient according to B.1 Annex B EN 1992-1-1
 */
@SuppressWarnings("WeakerAccess")
public class LinearCreepCoefficientFactory implements Factory<Double> {
	private ConcreteClassification concreteClassification;
	private double rh;
	private double t0;
	private double t;
	private Shape shape;
	private int cementClassification;

	/**
	 * Creates an instance of the factory
	 *
	 * @param concreteClassification concrete classification of the cross section
	 * @param rh                     relative humidity of environment
	 * @param t0                     time of loading in days
	 * @param t                      age of concrete in days
	 * @param shape                  shape of cross section
	 * @param cementClassification   cement classification of concrete
	 * @see CementClassification#CEMENT_S
	 * @see CementClassification#CEMENT_N
	 * @see CementClassification#CEMENT_R
	 */
	public LinearCreepCoefficientFactory(ConcreteClassification concreteClassification, double rh, double t0, double
			t, Shape shape, int cementClassification) {
		this.concreteClassification = concreteClassification;
		this.rh = rh;
		this.t0 = t0;
		this.t = t;
		this.shape = shape;
		this.cementClassification = cementClassification;
	}

	/**
	 * Calculates and returns linear creep coefficient according to B.1
	 *
	 * @return linear creep coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	@Override
	public Double build() throws ImproperDataException, LSException {
		return calculatePhiln();
	}

	/**
	 * Calculates and returns linear creep coefficient according to B.1
	 *
	 * @return linear creep coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculatePhiln() throws ImproperDataException, LSException {
		double phi0 = pos(calculatePhi0());
		double betactt0 = pos(calculateBetactt0());
		return pos(phi0 * betactt0);
	}

	/**
	 * Calculates and returns linear notional creep coefficient according to B.1
	 *
	 * @return linear notional creep coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculatePhi0() throws ImproperDataException, LSException {
		double phirh = pos(calculatePhirh());
		double betafcm = pos(calculateBetafcm());
		double betat0 = pos(calculateBetat0());
		return pos(phirh * betafcm * betat0);
	}

	/**
	 * Calculates and returns concrete strength factor according to B.1
	 *
	 * @return concrete strength factor
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateBetafcm() throws ImproperDataException, LSException {
		double fcm = pos(notNull(concreteClassification).getFcm());
		return pos(16800 / sqrt(fcm));
	}

	/**
	 * Calculates and returns concrete age factor according to B.1
	 *
	 * @return concrete age factor
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateBetat0() throws ImproperDataException, LSException {
		double t0mod = pos(this::calculateT0mod);
		return pos(1 / pos(0.1 + pow(t0mod, 0.2)));
	}

	/**
	 * Calculates and returns cement adjusted time of loading in days according to B.1
	 *
	 * @return cement adjusted time of loading in days
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateT0mod() throws ImproperDataException, LSException {
		pos(t0);
		double alfacem = real(calculateAlpha());
		return pos(t0 * pow(1 + 9 / (2 + pos(pow(t0, 1.2))), alfacem));
	}

	/**
	 * Calculates and returns alpha coefficient returns according to B.1
	 *
	 * @return alpha coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAlpha() throws ImproperDataException, LSException {
		switch (cementClassification) {
			case CEMENT_S:
				return -1;
			case CEMENT_N:
				return 0;
			case CEMENT_R:
				return 1;
			default:
				throw new ImproperDataException();
		}
	}

	/**
	 * Calculates and returns relative humidity factor according to B.1
	 *
	 * @return relative humidity factor
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculatePhirh() throws ImproperDataException, LSException {
		double fcm = pos(notNull(concreteClassification).getFcm());
		pos(rh);
		double h0 = pos(calculateH0());
		double alfa1 = pos(calculateAlpha1());
		double alfa2 = pos(calculateAlpha2());
		double factor = pos(pos(1 - rh) / pos(pow(h0, 1.0 / 3.0)));
		return (fcm <= 35000000) ? pos(1 + factor) : pos((1 + factor * alfa1) * alfa2);
	}

	/**
	 * Calculates and returns alpha1 coefficient returns according to B.1
	 *
	 * @return alpha1 coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAlpha1() throws ImproperDataException, LSException {
		return calculateAlpha(0.7);
	}

	/**
	 * Calculates and returns alpha2 coefficient returns according to B.1
	 *
	 * @return alpha2 coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAlpha2() throws ImproperDataException, LSException {
		return calculateAlpha(0.2);
	}

	/**
	 * Calculates and returns notional size in meters according to B.1
	 *
	 * @return notional size in meters
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateH0() throws ImproperDataException, LSException {
		notNull(shape);
		double ac = pos(shape.getA());
		double u = pos(shape.getU());
		return pos(2 * ac / u);
	}

	/**
	 * Calculates and returns development factor according to B.1
	 *
	 * @return development factor
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateBetactt0() throws ImproperDataException, LSException {
		if (t == Double.POSITIVE_INFINITY) return 1;
		pos(t);
		double t0mod = pos(this::calculateT0mod);
		double betaH = pos(calculateBetaH());
		pos(rh);
		return pos(pow(pos(t - t0mod) / pos(betaH + t - t0mod), 0.3));
	}

	/**
	 * Calculates and returns relative humidity and notional size factor according to B.1
	 *
	 * @return relative humidity and notional size factor
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateBetaH() throws ImproperDataException, LSException {
		double fcm = pos(notNull(concreteClassification).getFcm());
		double h0 = pos(calculateH0());
		double factor = pos(1.5 * ((1 + pow(1.2 * rh, 18)) * h0 * 1000));
		double alfa3 = calculateAlfa3();
		return pos((fcm <= 35000000) ? min(factor + 250, 1500) : min(factor + 250 * alfa3, 1500 * alfa3));
	}

	/**
	 * Calculates and returns alpha coefficient returns according to B.1
	 *
	 * @return alpha coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAlfa3() throws ImproperDataException, LSException {
		return calculateAlpha(0.5);
	}

	private double calculateAlpha(double exponent) throws ImproperDataException, LSException {
		double fcm = pos(notNull(concreteClassification).getFcm());
		return pow((35000000 / fcm), pos(exponent));
	}
}

