package tyvrel.mag.core.factory.others;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.Load;
import tyvrel.mag.core.model.classification.AbstractClassification;
import tyvrel.mag.core.model.classification.ExposureClassification;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static tyvrel.mag.core.exception.Precondition.nonNeg;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which verifies crack width according to EN 1992-1-1 7.2
 */
@SuppressWarnings("WeakerAccess")
public class CrackWidthVerificationFactory implements Factory<Boolean> {
	private final ExposureClassification[] exposureClassifications;
	private final Load load;
	private final CrossSection crossSection;
	private final double phi;
	private final double t;
	private final int cementClassification;

	/**
	 * Creates an instance of the factory
	 *
	 * @param exposureClassifications exposure classifications
	 * @param load                    load
	 * @param crossSection            cross section
	 * @param phi                     creep coefficient
	 * @param t                       age of concrete in days
	 * @param cementClassification    cement classification
	 */
	public CrackWidthVerificationFactory(ExposureClassification[] exposureClassifications, Load load, CrossSection
			crossSection, double phi, double t, int cementClassification) {
		this.exposureClassifications = exposureClassifications;
		this.load = load;
		this.crossSection = crossSection;
		this.phi = phi;
		this.t = t;
		this.cementClassification = cementClassification;
	}

	/**
	 * Calculates and returns if crack width is limited
	 *
	 * @return if crack width is limited
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	@Override
	public Boolean build() throws ImproperDataException, LSException {
		return isCrackWidthLimited();
	}

	/**
	 * Calculates and returns if crack width is limited
	 *
	 * @return if crack width is limited
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected boolean isCrackWidthLimited() throws ImproperDataException, LSException {
		double wmax = pos(this::calculateWmax);
		double w = nonNeg(this::calculateW);
		return w <= wmax;
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

	/**
	 * Calculates and returns crack width in m
	 *
	 * @return crack width in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateW() throws ImproperDataException, LSException {
		return nonNeg(() -> new CrackWidthFactory(crossSection, phi, load.getMquasiperma(), load.getMquasipermb(), t,
				cementClassification).build());
	}
}
