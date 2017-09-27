package tyvrel.mag.core.factory.shearreinforcement;

import tyvrel.mag.core.exception.CompressionTooHighException;
import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.Load;
import tyvrel.mag.core.model.Factors;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;

import static java.lang.Math.*;
import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates and returns shear reinforcement defined by EN 1992-1-1 6.2
 */
@SuppressWarnings("WeakerAccess")
public class ShearReinforcementFactory implements Factory<ShearReinforcement> {
	private final double lbd;
	private Load load;
	private Factors factors;
	private CrossSection crossSection;

	private final double legn = 2;

	/**
	 * Creates an instance of the factory
	 *
	 * @param lbd          anchorage length in m
	 * @param crossSection cross section
	 * @param load         load
	 * @param factors      partial factors
	 */
	public ShearReinforcementFactory(double lbd, CrossSection
			crossSection, Load load, Factors factors) {
		this.crossSection = crossSection;
		this.load = load;
		this.factors = factors;
		this.lbd = lbd;
	}

	/**
	 * Calculates and returns shear reinforcement
	 *
	 * @return shear reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public ShearReinforcement build() throws ImproperDataException, LSException {
		return notNull(this::createShearReinforcement);
	}

	/**
	 * Calculates and returns shear reinforcement
	 *
	 * @return shear reinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected ShearReinforcement createShearReinforcement() throws ImproperDataException, LSException {
		return new ShearReinforcement(
				nonNeg(this::calculateN),
				pos(() -> crossSection.getAsw().getPhi()),
				pos(lbd),
				pos(legn)
		);
	}

	/**
	 * Calculates and returns number of shear assemblies per meter in 1/meter according to 6.2.3
	 *
	 * @return number of shear assemblies per meter in 1/meter
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateN() throws ImproperDataException, LSException {
		double asw = nonNeg(this::calculateAsw);
		double stirrupArea = pos(() -> crossSection.getAsw().getAphi() * pos(legn));
		return nonNeg(() -> 1.0 + asw / stirrupArea);
	}

	/**
	 * Calculates and returns area of shear reinforcement per meter in meters according to 6.2.3
	 *
	 * @return area of shear reinforcement per meter in meters
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAsw() throws ImproperDataException, LSException {
		double vrdc = pos(this::calculateVrdc);
		double fywd = pos(() -> crossSection.getShearReinforcementSteel().getFy() / factors.getGammas());
		double ved = nonNeg(() -> load.getVed());
		if (vrdc >= ved) {
			return 0;
		} else {
			double vrdmax = pos(this::calculateVrdmax);
			if (vrdmax < ved) throw new CompressionTooHighException();
			double z = pos(this::calculateZ);
			double cot0 = pos(this::calculateCottheta);
			return nonNeg(() -> ved / z / fywd / cot0);
		}
	}

	/**
	 * Calculates and returns shear resistance of the member without shear reinforcement in N according to 6.2.2
	 *
	 * @return shear resistance of the member without shear shearreinforcement in N
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateVrdc() throws ImproperDataException, LSException {
		double vrdc1 = pos(this::calculateVrdc1);
		double vrdc2 = pos(this::calculateVrdc2);
		return max(vrdc1, vrdc2);
	}

	/**
	 * Calculates and returns shear resistance of the member without shear reinforcement in N according to formula
	 * 6.2.a
	 *
	 * @return shear resistance of the member without shear reinforcement in N
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateVrdc1() throws ImproperDataException, LSException {
		double crdc = pos(this::calculateCrdc);
		double k = pos(this::calculateK);
		double rol = nonNeg(this::calculateRhol);
		double d = pos(Math.max(pos(() -> crossSection.getDb()), pos(() -> crossSection.getDa())) * 1000);
		double fck = pos(pos(() -> crossSection.getConcreteClassification().getFck()) / 1000000);
		double b = pos(pos(() -> crossSection.getShape().getB()) * 1000);
		return pos(() -> (crdc * k * pow(100 * rol * fck, 1 / 3)) * b * d);
	}

	/**
	 * Calculates and returns Crdc coefficient according to 6.2.2
	 *
	 * @return Crdc coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCrdc() throws ImproperDataException, LSException {
		return 0.18 / pos(() -> factors.getGammac());
	}

	/**
	 * Calculates and returns k coefficient according to 6.2.2
	 *
	 * @return k coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateK() throws ImproperDataException, LSException {
		double d = pos(Math.max(pos(() -> crossSection.getDb()), pos(() -> crossSection.getDa())) * 1000);
		return pos(() -> min(2, 1 + sqrt(200 / d)));
	}

	/**
	 * Calculates and returns rhol coefficient returns according to 6.2.2
	 *
	 * @return thol coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateRhol() throws ImproperDataException, LSException {
		double d = max(pos(() -> crossSection.getDb()), pos(() -> crossSection.getDa()));
		double b = pos(() -> crossSection.getShape().getB());
		double as1 = nonNeg(max(crossSection.getAs().getAsb().getA(), crossSection.getAs().getAsa().getA()));
		return min(nonNeg(() -> as1 * b * pos(d)), 0.02);
	}

	/**
	 * Calculates and returns shear resistance of the member without shear reinforcement in N according to formula
	 * 6.2.b
	 *
	 * @return shear resistance of the member without shear reinforcement in N
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateVrdc2() throws ImproperDataException, LSException {
		double d = pos(Math.max(pos(() -> crossSection.getDb()), pos(() -> crossSection.getDa())) * 1000);
		double b = pos(pos(() -> crossSection.getShape().getB()) * 1000);
		double vmin = pos(this::calculateVmin);
		return pos(() -> vmin * b * d);
	}

	/**
	 * Calculates and returns vmin coefficient returns according to 6.2.2
	 *
	 * @return vmin coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateVmin() throws ImproperDataException, LSException {
		double k = pos(this::calculateK);
		double fck = pos(pos(() -> crossSection.getConcreteClassification().getFck()) / 1000000);
		return pos(() -> 0.035 * pow(k, 3 / 2) * pow(fck, 1 / 2));
	}

	/**
	 * Calculates maximum shear force in N and returns according to 6.2.3
	 *
	 * @return maximum shear force in N
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateVrdmax() throws ImproperDataException, LSException {
		double alfacw = pos(this::calculateAlphacw);
		double z = pos(this::calculateZ);
		double cot0 = pos(this::calculateCottheta);
		double v1 = pos(this::calculateV1);
		double fcd = pos(this::calculateFcd);
		double b = pos(() -> crossSection.getShape().getB());
		return pos(() -> alfacw * b * z * v1 * fcd / pos(cot0 + 1 / pos(cot0)));
	}

	/**
	 * Calculates and returns alphacw coefficient returns according to 6.2.3
	 *
	 * @return alphacw coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAlphacw() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates and returns cottheta coefficient returns according to 6.2.3
	 *
	 * @return cottheta coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCottheta() throws ImproperDataException, LSException {
		return 1;
	}

	/**
	 * Calculates and returns v1 coefficient returns according to 6.2.3
	 *
	 * @return v1 coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateV1() throws ImproperDataException, LSException {
		double fck = pos(pos(() -> crossSection.getConcreteClassification().getFck()) / 1000000);
		return pos(() -> 0.6 * pos(1 - fck / 250));
	}

	/**
	 * Calculates and returns design concrete compressive strength in Pa according to 6.2.3
	 *
	 * @return design concrete compressive strength in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateFcd() throws ImproperDataException, LSException {
		double fck = pos(() -> crossSection.getConcreteClassification().getFck());
		double gammaC = pos(() -> factors.getGammac());
		return pos(fck / gammaC);
	}

	/**
	 * Calculates and returns inner lever arm in meters according to 6.2.3
	 *
	 * @return inner lever arm in meters
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateZ() throws ImproperDataException, LSException {
		double d = Math.max(pos(() -> crossSection.getDb()), pos(() -> crossSection.getDa()));
		return pos(0.9 * d);
	}
}