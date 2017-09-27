package tyvrel.mag.core.factory.stress;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.Stress;

import static java.lang.Math.max;
import static tyvrel.mag.core.exception.Precondition.nonNeg;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates stress in uncracked cross section. Doesn't check if cross section is cracked.
 */
@SuppressWarnings("WeakerAccess")
public class UncrackedStressFactory implements Factory<Stress> {
	private final CrossSection crossSection;
	private final double ma;
	private final double mb;

	/**
	 * Creates an instance of the factory
	 *
	 * @param crossSection cross section
	 * @param ma           bending moment tensioning top of cross section in kNm
	 * @param mb           bending moment tensioning bottom of cross section in kNm
	 */
	public UncrackedStressFactory(CrossSection crossSection, double ma, double mb) {
		this.crossSection = crossSection;
		this.ma = ma;
		this.mb = mb;
	}

	/**
	 * Calculates and returns stress in cross section
	 *
	 * @return stress in cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	@Override
	public Stress build() throws ImproperDataException, LSException {
		return calculateStress();
	}

	/**
	 * Calculates and returns stress in cross section
	 *
	 * @return stress in cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Stress calculateStress() throws ImproperDataException, LSException {
		double sigmac = nonNeg(this::calculateSigmac);
		double sigmas = nonNeg(this::calculateSigmas);
		return new Stress(sigmac, sigmas);
	}

	/**
	 * Calculates and returns compressive stress in concrete
	 *
	 * @return compressive stress in concrete
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSigmac() throws ImproperDataException, LSException {
		return max(nonNeg(this::calculateSigmaca), nonNeg(this::calculateSigmacb));
	}

	/**
	 * Calculates and returns compressive stress in concrete under bending moment tensioning top of cross section
	 *
	 * @return compressive stress in concrete
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSigmaca() throws ImproperDataException, LSException {
		nonNeg(ma);
		double y = pos(() -> crossSection.getShape().getNeutralAxis());
		return nonNeg(calculateSigma(ma, y));
	}

	/**
	 * Calculates and returns compressive stress in concrete under bending moment tensioning bottom of cross section
	 *
	 * @return compressive stress in concrete
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSigmacb() throws ImproperDataException, LSException {
		nonNeg(mb);
		double h = pos(() -> crossSection.getShape().getH());
		double axis = pos(() -> crossSection.getShape().getNeutralAxis());
		double y = pos(() -> h - axis);
		return nonNeg(calculateSigma(mb, y));
	}

	/**
	 * Calculates and returns tensile stress in any shearreinforcement
	 *
	 * @return tensile stress in shearreinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSigmas() throws ImproperDataException, LSException {
		return max(nonNeg(this::calculateSigmasa), nonNeg(this::calculateSigmasb));
	}

	/**
	 * Calculates and returns tensile stress in top shearreinforcement under bending moment tensioning top of cross
	 * section
	 *
	 * @return tensile stress in shearreinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSigmasa() throws ImproperDataException, LSException {
		nonNeg(ma);
		double axis = pos(() -> crossSection.getShape().getNeutralAxis());
		double a = pos(crossSection::getAa);
		double y = pos(() -> axis - a);
		return nonNeg(calculateSigma(ma, y));
	}

	/**
	 * Calculates and returns tensile stress in bottom shearreinforcement under bending moment tensioning bottom of
	 * cross
	 * section
	 *
	 * @return tensile stress in shearreinforcement
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateSigmasb() throws ImproperDataException, LSException {
		nonNeg(mb);
		double h = pos(() -> crossSection.getShape().getH());
		double axis = pos(() -> crossSection.getShape().getNeutralAxis());
		double a = pos(crossSection::getAb);
		double y = pos(() -> h - axis - a);
		return nonNeg(calculateSigma(mb, y));
	}

	private double calculateSigma(double m, double y) throws ImproperDataException, LSException {
		double i = pos(() -> crossSection.getShape().getI());
		nonNeg(m);
		return nonNeg(() -> m * y / i);
	}
}
