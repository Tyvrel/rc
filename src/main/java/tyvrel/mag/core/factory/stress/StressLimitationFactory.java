package tyvrel.mag.core.factory.stress;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.Stress;
import tyvrel.mag.core.model.classification.AbstractClassification;
import tyvrel.mag.core.model.classification.ExposureClassification;

import java.util.Arrays;
import java.util.Objects;

import static tyvrel.mag.core.exception.Precondition.notNull;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which checks if stress in the cross section is limited according to 7.2 EN 1992-1-1
 */
@SuppressWarnings("WeakerAccess")
public class StressLimitationFactory implements Factory<Boolean> {
	private final Stress stress;
	private final CrossSection crossSection;
	private final ExposureClassification[] exposureClassifications;

	/**
	 * Creates an instance of the factory
	 *
	 * @param stress                  stress in the cross section under characteristic combination
	 * @param crossSection            cross section
	 * @param exposureClassifications exposure classifications
	 */
	public StressLimitationFactory(Stress stress, CrossSection crossSection, ExposureClassification[]
			exposureClassifications) {
		this.stress = stress;
		this.crossSection = crossSection;
		this.exposureClassifications = exposureClassifications;
	}

	/**
	 * Calculates and returns if stress in the cross section is limited according to 7.2 EN 1992-1-1
	 *
	 * @return if stress in the cross section is limited
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	@Override
	public Boolean build() throws ImproperDataException, LSException {
		return isStressLimited();
	}

	/**
	 * Calculates and returns if stress in the cross section is limited according to 7.2 EN 1992-1-1
	 *
	 * @return if stress in the cross section is limited
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected boolean isStressLimited() throws ImproperDataException, LSException {
		notNull(() -> stress);
		Stress minStress = notNull(this::calculateMinimumStress);
		return (stress.getSigmac() <= minStress.getSigmac() && stress.getSigmas() <= minStress.getSigmas());
	}

	/**
	 * Calculates and returns minimum stress according to 7.2 EN 1992-1-1
	 *
	 * @return minimum stress
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Stress calculateMinimumStress() throws ImproperDataException, LSException {
		return new Stress(pos(this::calculateMinimumSigmac), pos(this::calculateMinimumSigmas));
	}

	/**
	 * Calculates and returns minimum stress of steel in Pa according to 7.2 EN 1992-1-1
	 *
	 * @return minimum stress of steel in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateMinimumSigmas() throws ImproperDataException, LSException {
		double fyk = pos(() -> crossSection.getLongitudinalReinforcementSteel().getFy());
		double k3 = pos(this::calculateK3);
		return pos(() -> fyk * k3);
	}

	/**
	 * Calculates and returns k3 coefficient according to 7.2 EN 1992-1-1
	 *
	 * @return k3 coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateK3() throws ImproperDataException, LSException {
		return 0.8;
	}

	/**
	 * Calculates and returns minimum stress of concrete in Pa according to 7.2 EN 1992-1-1
	 *
	 * @return minimum stress of concrete in Pa
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateMinimumSigmac() throws ImproperDataException, LSException {
		double fck = pos(() -> crossSection.getConcreteClassification().getFck());
		double k1 = pos(this::calculateK1);
		return (isExposedToAggressiveEnvironment()) ? fck * k1 :
				10000000000000000000000000000000000000000000000000000.0;
	}

	/**
	 * Calculates and returns k1 coefficient according to 7.2 EN 1992-1-1
	 *
	 * @return k1 coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateK1() throws ImproperDataException, LSException {
		return 0.6;
	}

	/**
	 * Calculates and returns if environment is aggressive according to 7.2 EN 1992-1-1
	 *
	 * @return if environment is aggressive
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected boolean isExposedToAggressiveEnvironment() throws ImproperDataException, LSException {
		return Arrays.stream(notNull(exposureClassifications))
				.filter(Objects::nonNull)
				.map(AbstractClassification::getSymbol)
				.filter(Objects::nonNull)
				.anyMatch(s -> s.contains("XD") || s.contains("XF") || s.contains("XS"));
	}
}
