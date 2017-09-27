package tyvrel.mag.core.factory.dimensioning;

import tyvrel.mag.core.exception.*;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.factory.classification.*;
import tyvrel.mag.core.factory.longitudinalreinforcement.*;
import tyvrel.mag.core.factory.others.ConcreteCoverFactory;
import tyvrel.mag.core.factory.others.CrackWidthVerificationFactory;
import tyvrel.mag.core.factory.shearreinforcement.*;
import tyvrel.mag.core.factory.stress.*;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.Load;
import tyvrel.mag.core.model.Factors;
import tyvrel.mag.core.model.Stress;
import tyvrel.mag.core.model.classification.*;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;

import java.util.ArrayList;
import java.util.List;

import static tyvrel.mag.core.exception.Precondition.*;
import static tyvrel.mag.core.factory.longitudinalreinforcement.LongitudinalReinforcementRequiredAnchorageLengthFactory
		.BAD_BOND_CONDITIONS;
import static tyvrel.mag.core.factory.longitudinalreinforcement.LongitudinalReinforcementRequiredAnchorageLengthFactory
		.GOOD_BOND_CONDITIONS;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates cross section according to EN 1992-1-1
 */
@SuppressWarnings("WeakerAccess")
public class CrossSectionFactory implements Factory<CrossSection> {
	private final ExposureClassification[] exposureClasses;
	private final Load[] loads;
	private final Factors factors;
	private final CrossSection crossSection;

	private final boolean is100yWorkingLife;
	private final boolean isQualityEnsured;
	private final boolean isHighAir;
	private final double dg;

	private StructuralClassificationFactory structuralClassFactory;
	private ExposureClassificationFactory exposureClassFactory;
	private ConcreteClassificationFactory concreteClassFactory;
	private AbrasionClassificationFactory abrasionClassFactory;

	private double phi = 0;

	/**
	 * Creates an instance of the factory
	 *
	 * @param exposureClasses   exposure classes
	 * @param loads             loads
	 * @param factors           partial factors
	 * @param crossSection      cross section
	 * @param is100yWorkingLife if element is designed for 100 years working life
	 * @param isQualityEnsured  if special concrete quality control is ensure
	 * @param isHighAir         if air containment is higher than 4%
	 * @param dg                maximal diameter of aggregate in m
	 */
	public CrossSectionFactory(ExposureClassification[] exposureClasses, Load[] loads, Factors factors,
	                           CrossSection crossSection, boolean is100yWorkingLife, boolean isQualityEnsured, boolean
			                           isHighAir, double dg) {
		this.exposureClasses = exposureClasses;
		this.loads = loads;
		this.factors = factors;
		this.crossSection = crossSection;
		this.is100yWorkingLife = is100yWorkingLife;
		this.isQualityEnsured = isQualityEnsured;
		this.isHighAir = isHighAir;
		this.dg = dg;
	}

	/**
	 * Calculates and returns cross section
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public CrossSection build() throws ImproperDataException, LSException {
		return notNull(() -> calculateCrossSection(crossSection));
	}

	/**
	 * Calculates and returns cross section
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateCrossSection(CrossSection crossSection) throws ImproperDataException, LSException {
		crossSection = calculateCover(crossSection);
		validateConcreteClass(crossSection);
		validateMaximumPhisw(crossSection);
		crossSection = calculateAs(crossSection);
		crossSection = calculateAsw(crossSection);

		validateStressLimitation(crossSection);
		validateCrackWidth(crossSection);
		return crossSection;
	}

	protected void validateCrackWidth(CrossSection crossSection) throws ImproperDataException, LSException {
		for (Load load : notNull(loads)) {
			boolean meetsCrackWidth = new CrackWidthVerificationFactory(exposureClasses, load, crossSection,
					calculatePhi(crossSection), 28, CementClassification.CEMENT_N).build();
			if (!meetsCrackWidth) throw new SLSException();
		}
	}

	/**
	 * Validates if stress limit isn't exceeded
	 *
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected void validateStressLimitation(CrossSection crossSection) throws ImproperDataException, LSException {
		Stress stress = notNull(() -> calculateCharStress(crossSection));
		boolean meetsStressLimitation = new StressLimitationFactory(stress, crossSection, exposureClasses).build();
		if (!meetsStressLimitation) throw new SLSException();
	}

	/**
	 * Calculates and returns stress in cross section under characteristic combinations of load
	 *
	 * @return stress in cross section under characteristic combinations of load
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Stress calculateCharStress(CrossSection crossSection) throws ImproperDataException, LSException {
		Stress uncrackedStress = notNull(() -> calculateUncrackedCharStress(crossSection));
		Stress crackedStress = notNull(() -> calculateCrackedCharStress(crossSection));
		return new StressFactory(notNull(crossSection.getConcreteClassification()), uncrackedStress, crackedStress)
				.build();
	}

	/**
	 * Calculates and returns stress in uncracked cross section under characteristic combinations of load
	 *
	 * @return stress in uncracked cross section under characteristic combinations of load
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Stress calculateUncrackedCharStress(CrossSection crossSection) throws ImproperDataException,
			LSException {
		double ma = 0;
		double mb = 0;
		for (Load load : notNull(loads)) {
			ma = Math.max(ma, notNull(load::getMchara));
			mb = Math.max(mb, notNull(load::getMcharb));
		}
		return new UncrackedStressFactory(crossSection, ma, mb).build();
	}

	/**
	 * Calculates and returns stress in cracked cross section under characteristic combinations of load
	 *
	 * @return stress in cracked cross section under characteristic combinations of load
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Stress calculateCrackedCharStress(CrossSection crossSection) throws ImproperDataException, LSException {
		double phi = pos(() -> calculatePhi(crossSection));
		double ma = 0;
		double mb = 0;
		for (Load load : notNull(loads)) {
			ma = Math.max(ma, notNull(load::getMchara));
			mb = Math.max(mb, notNull(load::getMcharb));
		}
		Stress stressPhi = new CrackedStressFactory(crossSection, ma, mb, phi).build();
		Stress stressNoPhi = new CrackedStressFactory(crossSection, ma, mb, 0).build();
		return Stress.max(stressNoPhi, stressPhi);
	}

	/**
	 * Calculates and returns creep coefficient
	 *
	 * @return creep coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculatePhi(CrossSection crossSection) throws ImproperDataException, LSException {
		if (phi == 0) {
			double philn = pos(() -> calculatePhiln(crossSection));
			double maquasiperm = 0;
			double mbquasiperm = 0;
			for (Load load : notNull(loads)) {
				maquasiperm = Math.max(maquasiperm, notNull(load::getMquasiperma));
				mbquasiperm = Math.max(mbquasiperm, notNull(load::getMquasipermb));
			}
			phi = new CreepCoefficientFactory(philn, crossSection, maquasiperm, mbquasiperm, 28, CementClassification
					.CEMENT_N)
					.build();
		}
		return phi;
	}

	/**
	 * Calculates and returns linear creep coefficient
	 *
	 * @return linear creep coefficient
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculatePhiln(CrossSection crossSection) throws ImproperDataException, LSException {
		return new LinearCreepCoefficientFactory(notNull(crossSection::getConcreteClassification), 0.5, 28,
				Double.POSITIVE_INFINITY, notNull(crossSection::getShape), CementClassification.CEMENT_N)
				.build();
	}

	/**
	 * Returns cross section modified by calculated shear shearreinforcement
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateAsw(CrossSection crossSection) throws ImproperDataException, LSException {
		crossSection = calculateMinimalAsw(crossSection);
		crossSection = calculateProvidedAsw(crossSection);
		validateMaximalAsw(crossSection);
		return crossSection;
	}

	/**
	 * Returns cross section modified by calculated minimal shear shearreinforcement
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateMinimalAsw(CrossSection crossSection)
			throws ImproperDataException, LSException {
		MinimumShearReinforcementFactory bmsrf = new MinimumShearReinforcementFactory(
				MinimumShearReinforcementFactory.INTEGER_LEGS_NUMBER, crossSection);
		ShearReinforcement asw = notNull(() -> ShearReinforcement.merge(crossSection.getAsw(), bmsrf.build()));
		return new CrossSection(crossSection.getShape(), crossSection.getLongitudinalReinforcementSteel(),
				crossSection.getShearReinforcementSteel(), crossSection.getConcreteClassification(),
				crossSection.getCrossSectionType(), crossSection.getAs(), asw, crossSection.getCnom());
	}

	/**
	 * Returns cross section modified by calculated provided shear shearreinforcement
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateProvidedAsw(CrossSection crossSection) throws ImproperDataException, LSException {
		List<ShearReinforcement> aswList = new ArrayList<>();
		for (Load load : loads) {
			aswList.add(new ShearReinforcementFactory(pos(() -> calculateAswLbd(crossSection)),
					crossSection, load, factors).build());
		}

		ShearReinforcement asw = notNull(crossSection::getAsw);
		for (ShearReinforcement aswprov : aswList) {
			asw = ShearReinforcement.merge(asw, aswprov);
		}

		return new CrossSection(crossSection.getShape(), crossSection.getLongitudinalReinforcementSteel(),
				crossSection.getShearReinforcementSteel(), crossSection.getConcreteClassification(),
				crossSection.getCrossSectionType(), crossSection.getAs(), asw, crossSection.getCnom());
	}

	/**
	 * Returns cross section modified by calculated anchorage length of shear shearreinforcement
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateAswLbd(CrossSection crossSection) throws ImproperDataException, LSException {
		double phisw = pos(() -> crossSection.getAsw().getPhi());
		return new ShearReinforcementAnchorageLengthFactory(phisw).build();
	}

	/**
	 * Validates if shear shearreinforcement doesn't exceed maximal
	 *
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected void validateMaximalAsw(CrossSection crossSection) throws ImproperDataException, LSException {
		ShearReinforcement maxAs = new MaximumShearReinforcementFactory(dg,
				pos(crossSection::getAswClearance), pos(() -> crossSection.getAsw().getPhi())).build();

		double n = nonNeg(() -> crossSection.getAsw().getN());
		double nLegs = nonNeg(() -> crossSection.getAsw().getNleg());
		double nMax = nonNeg(maxAs::getN);
		double nLegsMax = nonNeg(maxAs::getNleg);
		if (n > nMax || nLegs > nLegsMax) throw new ForceInReinforcementSWTooHighException();
	}

	/**
	 * Returns cross section modified by calculated longitudinal shearreinforcement
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateAs(CrossSection crossSection) throws ImproperDataException, LSException {
		crossSection = calculateMinimalAs(crossSection);
		crossSection = calculateProvidedAs(crossSection);
		crossSection = calculateMinimalCrackAs(crossSection);
		validateMaximalAs(crossSection);
		crossSection = calculateAsL0(crossSection);
		crossSection = calculateAsLbd(crossSection);

		return crossSection;
	}

	/**
	 * Returns cross section modified by calculated minimal longitudinal shearreinforcement
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateMinimalAs(CrossSection crossSection) throws ImproperDataException, LSException {
		MinimumBeamLongitudinalReinforcementFactory bmrf = new MinimumBeamLongitudinalReinforcementFactory(
				notNull(crossSection));
		LongitudinalReinforcement as =
				notNull(() -> LongitudinalReinforcement.merge(crossSection.getAs(), bmrf.build()));
		return new CrossSection(crossSection.getShape(), crossSection.getLongitudinalReinforcementSteel(),
				crossSection.getShearReinforcementSteel(), crossSection.getConcreteClassification(),
				crossSection.getCrossSectionType(), as, crossSection.getAsw(),
				crossSection.getCnom());
	}

	/**
	 * Returns cross section modified by calculated provided longitudinal shearreinforcement
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateProvidedAs(CrossSection crossSection)
			throws ImproperDataException, LSException {

		List<LongitudinalReinforcement> asList = new ArrayList<>();
		for (Load load : notNull(loads)) {
			asList.add(new LongitudinalReinforcementFactory(crossSection, load, factors).build());
		}

		LongitudinalReinforcement as = notNull(crossSection::getAs);
		for (LongitudinalReinforcement asprov : asList) {
			as = LongitudinalReinforcement.merge(as, asprov);
		}

		return new CrossSection(crossSection.getShape(), crossSection.getLongitudinalReinforcementSteel(),
				crossSection.getShearReinforcementSteel(), crossSection.getConcreteClassification(),
				crossSection.getCrossSectionType(), as, crossSection.getAsw(),
				crossSection.getCnom());
	}

	/**
	 * Returns cross section modified by calculated minimal crack longitudinal shearreinforcement
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateMinimalCrackAs(CrossSection crossSection) throws ImproperDataException,
			LSException {
		LongitudinalReinforcement asmin = new MinimumCrackLongitudinalReinforcementFactory(
				crossSection, 28, CementClassification.CEMENT_N, exposureClasses).build();

		LongitudinalReinforcement as = LongitudinalReinforcement.merge(notNull(crossSection::getAs), asmin);

		return new CrossSection(crossSection.getShape(), crossSection.getLongitudinalReinforcementSteel(),
				crossSection.getShearReinforcementSteel(), crossSection.getConcreteClassification(),
				crossSection.getCrossSectionType(), as, crossSection.getAsw(),
				crossSection.getCnom());
	}

	/**
	 * Validates if longitudinal shearreinforcement doesn't exceed maximal
	 *
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected void validateMaximalAs(CrossSection crossSection) throws ImproperDataException, LSException {
		double nAs1 = nonNeg(() -> crossSection.getAs().getAsb().getN());
		double nAs2 = nonNeg(() -> crossSection.getAs().getAsa().getN());

		LongitudinalReinforcement maxAs1 = new MaximumBeamLongitudinalReinforcementFactory(
				notNull(crossSection::getAs), notNull(crossSection::getShape)).build();
		double nAs1max = nonNeg(() -> maxAs1.getAsb().getN());
		double nAs2max = nonNeg(() -> maxAs1.getAsa().getN());
		if (nAs1 > nAs1max) throw new ForceInReinforcementBTooHighException();
		if (nAs2 > nAs2max) throw new ForceInReinforcementATooHighException();

		LongitudinalReinforcement maxAs2 =
				new MaximumLongitudinalReinforcementFactory(dg, notNull(crossSection)).build();
		double nAs1max2 = nonNeg(() -> maxAs2.getAsb().getN());
		double nAs2max2 = nonNeg(() -> maxAs2.getAsa().getN());
		if (nAs1 > nAs1max2) throw new ForceInReinforcementBTooHighException();
		if (nAs2 > nAs2max2) throw new ForceInReinforcementATooHighException();
	}

	/**
	 * Returns cross section modified by calculated lap length
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateAsL0(CrossSection crossSection) throws ImproperDataException, LSException {
		double phia = pos(() -> crossSection.getAs().getAsa().getPhi());
		double phib = pos(() -> crossSection.getAs().getAsb().getPhi());
		double l0a = new LapLengthFactory(phia, 0.6, pos(() -> calculateLbdrqda(crossSection))).build();
		double l0b = new LapLengthFactory(phib, 0.6, pos(() -> calculateLbdrqdb(crossSection))).build();
		Reinforcement asa = new Reinforcement(
				pos(() -> crossSection.getAs().getAsa().getN()),
				phia,
				nonNeg(() -> crossSection.getAs().getAsa().getLbd()),
				Math.max(nonNeg(() -> crossSection.getAs().getAsa().getL0()), l0a));
		Reinforcement asb = new Reinforcement(
				pos(() -> crossSection.getAs().getAsb().getN()),
				phib,
				nonNeg(() -> crossSection.getAs().getAsb().getLbd()),
				Math.max(nonNeg(() -> crossSection.getAs().getAsb().getL0()), l0b));
		LongitudinalReinforcement as = new LongitudinalReinforcement(asb, asa);
		return new CrossSection(crossSection.getShape(), crossSection.getLongitudinalReinforcementSteel(),
				crossSection.getShearReinforcementSteel(), crossSection.getConcreteClassification(),
				crossSection.getCrossSectionType(), as, crossSection.getAsw(),
				crossSection.getCnom());
	}

	/**
	 * Returns cross section modified by calculated design anchorage length of longitudinal shearreinforcement
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateAsLbd(CrossSection crossSection) throws ImproperDataException, LSException {
		double phia = pos(() -> crossSection.getAs().getAsa().getPhi());
		double phib = pos(() -> crossSection.getAs().getAsb().getPhi());
		double lbda = new LongitudinalReinforcementDesignAnchorageLengthFactory(
				phia, pos(() -> calculateLbdrqda(crossSection))).build();
		double lbdb = new LongitudinalReinforcementDesignAnchorageLengthFactory(
				phib, pos(() -> calculateLbdrqdb(crossSection))).build();
		Reinforcement asa = new Reinforcement(
				pos(() -> crossSection.getAs().getAsa().getN()),
				phia,
				Math.max(nonNeg(() -> crossSection.getAs().getAsa().getLbd()), lbda),
				nonNeg(() -> crossSection.getAs().getAsa().getL0()));
		Reinforcement asb = new Reinforcement(
				pos(() -> crossSection.getAs().getAsb().getN()),
				phib,
				Math.max(nonNeg(() -> crossSection.getAs().getAsb().getLbd()), lbdb),
				nonNeg(() -> crossSection.getAs().getAsb().getL0()));
		LongitudinalReinforcement as = new LongitudinalReinforcement(asb, asa);
		return new CrossSection(crossSection.getShape(), crossSection.getLongitudinalReinforcementSteel(),
				crossSection.getShearReinforcementSteel(), crossSection.getConcreteClassification(),
				crossSection.getCrossSectionType(), as, crossSection.getAsw(),
				crossSection.getCnom());
	}

	/**
	 * Calculates and returns required anchorage length of top shearreinforcement in m
	 *
	 * @return required anchorage length of top shearreinforcement in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateLbdrqda(CrossSection crossSection) throws ImproperDataException, LSException {
		double cnom = pos(crossSection::getCnom);
		double phi = pos(() -> crossSection.getAs().getAsa().getPhi());
		double phisw = pos(() -> crossSection.getAsw().getPhi());
		int bondConditionsType;
		double h = pos(() -> crossSection.getShape().getH());
		if (h < 0.250) bondConditionsType = GOOD_BOND_CONDITIONS;
		else if (h > 0.600) bondConditionsType = BAD_BOND_CONDITIONS;
		else if (h - 0.250 > cnom + phisw + phi) bondConditionsType = BAD_BOND_CONDITIONS;
		else bondConditionsType = GOOD_BOND_CONDITIONS;
		return new LongitudinalReinforcementRequiredAnchorageLengthFactory(notNull
				(crossSection::getConcreteClassification),
				notNull(crossSection::getLongitudinalReinforcementSteel), notNull(factors),
				phi, bondConditionsType).build();
	}

	/**
	 * Calculates and returns required anchorage length of bottom shearreinforcement in m
	 *
	 * @return required anchorage length of bottom shearreinforcement in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateLbdrqdb(CrossSection crossSection) throws ImproperDataException, LSException {
		return new LongitudinalReinforcementRequiredAnchorageLengthFactory(notNull
				(crossSection::getConcreteClassification),
				notNull(crossSection::getLongitudinalReinforcementSteel), notNull(factors),
				pos(() -> crossSection.getAs().getAsa().getPhi()),
				GOOD_BOND_CONDITIONS).build();
	}

	/**
	 * Validates diameter of shear shearreinforcement
	 *
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected void validateMaximumPhisw(CrossSection crossSection) throws ImproperDataException, LSException {
		double phib = pos(() -> crossSection.getAs().getAsb().getPhi());
		double phia = pos(() -> crossSection.getAs().getAsa().getPhi());
		double phisw = pos(() -> crossSection.getAsw().getPhi());
		double maxPhisw = pos(() -> new MaximumShearReinforcementDiameterFactory(phib, phia).build());
		if (notNull(phisw > maxPhisw)) throw new ShearReinforcementDiameterTooHighException();
	}

	/**
	 * Validates concrete classification
	 *
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected void validateConcreteClass(CrossSection crossSection) throws ImproperDataException, LSException {
		ConcreteClassification minConcrete = notNull(new AppendixEConcreteClassificationFactory(exposureClasses)
				.build());
		if (crossSection.getConcreteClassification().compareTo(minConcrete) < 0)
			throw new ConcreteClassTooSmallException("Concrete class is: " + crossSection.getConcreteClassification()
					.getSymbol() + ", but " +
					"should be at least " + minConcrete.getSymbol());
	}

	/**
	 * Returns cross section modified by calculated concrete cover
	 *
	 * @return cross section
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected CrossSection calculateCover(CrossSection crossSection) throws ImproperDataException, LSException {
		double phi =
				Math.max(pos(() -> crossSection.getAsw().getPhi()),
						Math.max(pos(() -> crossSection.getAs().getAsb().getPhi()),
								pos(() -> crossSection.getAs().getAsb().getPhi())));

		ConcreteCoverFactory ccfas = new ConcreteCoverFactory(notNull(this::getStructuralClassificationFactory),
				notNull(this::getExposureClassificationFactory), notNull(this::getConcreteClassificationFactory),
				phi, notNull(exposureClasses), notNull(crossSection::getConcreteClassification),
				(int) real(() -> (double) crossSection.getCrossSectionType()),
				is100yWorkingLife, false, isQualityEnsured, isHighAir, false, false,
				notNull(() -> getAbrasionClassificationFactory().get("XM0")),
				-1, dg);

		return new CrossSection(crossSection.getShape(), crossSection.getLongitudinalReinforcementSteel(),
				crossSection.getShearReinforcementSteel(), crossSection.getConcreteClassification(),
				crossSection.getCrossSectionType(), crossSection.getAs(), crossSection.getAsw(),
				ccfas.build());
	}

	/**
	 * Calculates and returns factory of structural classification
	 *
	 * @return factory of abrasion classification
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected AbstractClassificationFactory<StructuralClassification> getStructuralClassificationFactory()
			throws ImproperDataException, LSException {
		if (structuralClassFactory == null) {
			structuralClassFactory = new StructuralClassificationFactory();
		}
		return structuralClassFactory;
	}

	/**
	 * Calculates and returns factory of exposure classification
	 *
	 * @return factory of abrasion classification
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected AbstractClassificationFactory<ExposureClassification> getExposureClassificationFactory()
			throws ImproperDataException, LSException {
		if (exposureClassFactory == null) {
			exposureClassFactory = new ExposureClassificationFactory();
		}
		return exposureClassFactory;
	}

	/**
	 * Calculates and returns factory of concrete classification
	 *
	 * @return factory of abrasion classification
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected AbstractClassificationFactory<ConcreteClassification> getConcreteClassificationFactory()
			throws ImproperDataException, LSException {
		if (concreteClassFactory == null) {
			concreteClassFactory = new ConcreteClassificationFactory();
		}
		return concreteClassFactory;
	}

	/**
	 * Calculates and returns factory of abrasion classification
	 *
	 * @return factory of abrasion classification
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected AbstractClassificationFactory<AbrasionClassification> getAbrasionClassificationFactory()
			throws ImproperDataException, LSException {
		if (abrasionClassFactory == null) {
			abrasionClassFactory = new AbrasionClassificationFactory();
		}
		return abrasionClassFactory;
	}
}
