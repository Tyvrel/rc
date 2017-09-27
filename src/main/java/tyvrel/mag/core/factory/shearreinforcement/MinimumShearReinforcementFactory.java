package tyvrel.mag.core.factory.shearreinforcement;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;

import static java.lang.Math.*;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates and returns minimum shear reinforcement for beams defined by EN 1992-1-1 9.2
 */
@SuppressWarnings("WeakerAccess")
public class MinimumShearReinforcementFactory implements Factory<ShearReinforcement> {
	/**
	 * Indicates, that there must be even number of legs
	 */
	public static final int EVEN_LEGS_NUMBER = 0;
	/**
	 * Indicates, that there can be any integer number of legs (in case of using one-legged stirrups)
	 */
	public static final int INTEGER_LEGS_NUMBER = 1;
	/**
	 * Indicates, that there can be any rational number of legs (like in slabs)
	 */
	public static final int RATIONAL_LEGS_NUMBER = 2;

	private final int legsNumberType;
	private final CrossSection crossSection;

	/**
	 * Creates an instance of the factory
	 *
	 * @param legsNumberType type of leg number
	 * @param crossSection   cross section
	 * @see MinimumShearReinforcementFactory#EVEN_LEGS_NUMBER
	 * @see MinimumShearReinforcementFactory#INTEGER_LEGS_NUMBER
	 * @see MinimumShearReinforcementFactory#RATIONAL_LEGS_NUMBER
	 */
	public MinimumShearReinforcementFactory(int legsNumberType, CrossSection crossSection) {
		this.legsNumberType = legsNumberType;
		this.crossSection = crossSection;
	}

	/**
	 * Number of legs.
	 */
	protected double legN;

	/**
	 * Calculates and returns shear reinforcement. If overridden <code>legn</code> has to be initialised explicitly
	 *
	 * @return shear reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	@Override
	public ShearReinforcement build() throws ImproperDataException, LSException {
		legN = calculateLegN();
		return createShearReinforcement();
	}

	/**
	 * Calculates and returns shear reinforcement
	 *
	 * @return shear reinforcement
	 * @throws ImproperDataException if data is improper
	 */
	protected ShearReinforcement createShearReinforcement() throws ImproperDataException, LSException {
		return new ShearReinforcement(
				pos(this::calculateN),
				pos(() -> crossSection.getAsw().getPhi()),
				0,
				pos(legN)
		);
	}

	/**
	 * Calculates and returns number of legs
	 *
	 * @return returns number of legs
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateLegN() throws ImproperDataException, LSException {
		double fi = pos(() -> crossSection.getAsw().getPhi());
		double clearing = pos(() -> pos(crossSection::getAswClearance) - fi);
		double legsNumber = max(2, pos(() -> 1 + clearing / pos(calculateStmax())));

		switch (legsNumberType) {
			case INTEGER_LEGS_NUMBER:
				return ceil(legsNumber);
			case RATIONAL_LEGS_NUMBER:
				return legsNumber;
			case EVEN_LEGS_NUMBER:
				legsNumber = ceil(legsNumber);
				return ((int) legsNumber % 2 == 0) ? legsNumber : ++legsNumber;
			default:
				throw new ImproperDataException();
		}
	}

	/**
	 * Calculates and returns minimum transverse spacing in meters according to 9.2.2
	 *
	 * @return minimum transverse spacing in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateStmax() throws ImproperDataException, LSException {
		double d1 = pos(crossSection::getDb);
		double d2 = pos(crossSection::getDa);
		return min(min(d1, d2) * 0.75, 0.6);
	}

	/**
	 * Calculates and returns number of shear assemblies per meter in 1/meter
	 *
	 * @return number of shear assemblies per meter in 1/meter
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateN() throws ImproperDataException, LSException {
		double aswPerMeter = pos(this::calculateAswPerMeter);
		double stirrupArea = pos(() -> pos(() -> crossSection.getAsw().getPhi()) / pos(legN));
		double n = pos(() -> aswPerMeter / stirrupArea);
		return max(n, pos(() -> 1.0 + 1.0 / pos(this::calculateSlmax)));
	}

	/**
	 * Calculates and returns area of shear reinforcement per meter in meters
	 *
	 * @return area of shear reinforcement per meter in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateAswPerMeter() throws ImproperDataException, LSException {
		double fck = pos(() -> crossSection.getConcreteClassification().getFck());
		double fy = pos(() -> crossSection.getLongitudinalReinforcementSteel().getFy());
		double b = pos(() -> crossSection.getShape().getB());
		return pos(() -> 0.08 * sqrt(fck / 1000000.0) / (fy / 1000000.0) * b);
	}

	/**
	 * Calculates and returns maximum longitudinal spacing in meters
	 *
	 * @return maximum longitudinal spacing in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateSlmax() throws ImproperDataException, LSException {
		return min(pos(this::calculateSlmax1), pos(this::calculateSlmax2));
	}

	/**
	 * Calculates and returns maximum longitudinal spacing in meters according to 9.2.1.2
	 *
	 * @return maximum longitudinal spacing in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateSlmax1() throws ImproperDataException, LSException {
		double fi1 = pos(() -> crossSection.getAs().getAsb().getPhi());
		double fi2 = pos(() -> crossSection.getAs().getAsb().getPhi());
		return min(fi1, fi2) * 15;
	}

	/**
	 * Calculates and returns maximum longitudinal spacing in meters according to 9.2.2
	 *
	 * @return maximum longitudinal spacing in meters
	 * @throws ImproperDataException if data is improper
	 */
	protected double calculateSlmax2() throws ImproperDataException, LSException {
		double d1 = pos(crossSection::getDb);
		double d2 = pos(crossSection::getDa);
		return min(d1, d2) * 0.75;
	}
}
