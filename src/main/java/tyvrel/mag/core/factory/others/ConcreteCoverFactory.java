package tyvrel.mag.core.factory.others;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.factory.classification.*;
import tyvrel.mag.core.model.classification.AbrasionClassification;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.core.model.classification.ExposureClassification;
import tyvrel.mag.core.model.classification.StructuralClassification;

import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculate concrete cover according to 4.4.1 EN 1992-1-1
 */
@SuppressWarnings("WeakerAccess")
public class ConcreteCoverFactory implements Factory<Double> {
	/**
	 * Describes typical surface of concrete
	 */
	public static final int TYPICAL_SURFACE = 5;
	/**
	 * Describes that the concrete is placed on another concrete
	 */
	public static final int ANOTHER_CONCRETE_SURFACE = 0;
	/**
	 * Describes that the surface of concrete is uneven
	 */
	public static final int UNEVEN_CONCRETE_SURFACE = 1;
	/**
	 * Describes that the surface of concrete is prone to abrasion
	 */
	public static final int SURFACE_PRONE_TO_ABRASION = 2;
	/**
	 * Describes that the concrete is placed on uneven surface
	 */
	public static final int UNEVEN_SURFACE = 3;
	/**
	 * Describes that the concrete is placed on prepared, uneven surface
	 */
	public static final int PREPARED_UNEVEN_SURFACE = 4;

	private AbstractClassificationFactory<StructuralClassification> structuralClassFactory = null;
	private AbstractClassificationFactory<ExposureClassification> exposureClassFactory = null;
	private AbstractClassificationFactory<ConcreteClassification> concreteClassFactory = null;
	private Map<Pair<ExposureClassification, Boolean>, String> minimalStrengthForStructuralClassReductionMap;
	private Map<Pair<StructuralClassification, ExposureClassification>, Double> cMinDurMap;
	private double phi = 0;
	private ExposureClassification[] exposureClasses = null;
	private ConcreteClassification concreteClass = null;
	private int surfaceType = TYPICAL_SURFACE;
	private boolean is100yWorkingLife = false;
	private boolean isSlab = false;
	private boolean isQualityEnsured = false;
	private boolean isHighAir = false;
	private boolean isExposureShort = false;
	private boolean isInterfaceRoughened = false;
	private AbrasionClassification abrasionClass = null;
	private double userDeltacdev = -1;
	private double dg = 0.032;

	/**
	 * Creates an instance of the factory
	 *
	 * @param structuralClassFactory structural classification factory
	 * @param exposureClassFactory   exposure classification factory
	 * @param concreteClassFactory   concrete classification factory
	 * @param phi                    diameter of shearreinforcement in meters
	 * @param exposureClasses        a set of exposure classes of the enviroment
	 * @param concreteClass          a concrete class of the concrete
	 * @param surfaceType            surface type
	 * @param is100yWorkingLife      if the element is designed for 100 years of working life
	 * @param isSlab                 if the element is a slab
	 * @param isQualityEnsured       if there is quality ensured
	 * @param isHighAir              if air entrainment is higher than 4%
	 * @param isExposureShort        if element is exposed to external environment for less than 28 days
	 * @param isInterfaceRoughened   if interface is roughened
	 * @param abrasionClass          abrasion class
	 * @param userDeltacdev          user defined deltacdev
	 * @param dg                     maximum diameter of aggregate in m
	 */
	public ConcreteCoverFactory(
			AbstractClassificationFactory<StructuralClassification> structuralClassFactory,
			AbstractClassificationFactory<ExposureClassification> exposureClassFactory,
			AbstractClassificationFactory<ConcreteClassification> concreteClassFactory,
			double phi,
			ExposureClassification[] exposureClasses,
			ConcreteClassification concreteClass,
			int surfaceType,
			boolean is100yWorkingLife, boolean isSlab, boolean isQualityEnsured, boolean isHighAir,
			boolean isExposureShort, boolean isInterfaceRoughened,
			AbrasionClassification abrasionClass,
			double userDeltacdev,
			double dg) {
		this.structuralClassFactory = structuralClassFactory;
		this.exposureClassFactory = exposureClassFactory;
		this.concreteClassFactory = concreteClassFactory;
		this.phi = phi;
		this.exposureClasses = exposureClasses;
		this.concreteClass = concreteClass;
		this.surfaceType = surfaceType;
		this.is100yWorkingLife = is100yWorkingLife;
		this.isSlab = isSlab;
		this.isQualityEnsured = isQualityEnsured;
		this.isHighAir = isHighAir;
		this.isExposureShort = isExposureShort;
		this.isInterfaceRoughened = isInterfaceRoughened;
		this.abrasionClass = abrasionClass;
		this.userDeltacdev = userDeltacdev;
		this.dg = dg;

		try {
			minimalStrengthForStructuralClassReductionMap = calculateMinimalStrengthForStructuralClassReductionMap();
			cMinDurMap = calculateCmindurMap();
		} catch (ImproperDataException | LSException e) {
		}
	}

	/**
	 * Calculates and returns cnom in m according to 4.4.1
	 *
	 * @return cnom in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public Double build() throws ImproperDataException, LSException {
		return calculateCnom();
	}

	/**
	 * Calculates and returns cnom in m according to 4.4.1
	 *
	 * @return cnom in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCnom() throws ImproperDataException, LSException {
		switch (surfaceType) {
			case TYPICAL_SURFACE:
				return pos(this::calculateCnomForTypicalSurface);
			case ANOTHER_CONCRETE_SURFACE:
				return pos(this::calculateCnomForAnotherConcreteSurface);
			case SURFACE_PRONE_TO_ABRASION:
				return pos(this::calculateCnomForUnevenConcreteSurface);
			case UNEVEN_CONCRETE_SURFACE:
				return pos(this::calculateCnomForProneToAbrasionSurface);
			case UNEVEN_SURFACE:
				return pos(this::calculateCnomForUnevenSurface);
			case PREPARED_UNEVEN_SURFACE:
				return pos(this::calculateCnomForPreparedUnevenSurface);
			default:
				throw new ImproperDataException();
		}
	}

	/**
	 * Calculates and returns cnom in m for typical surface according to 4.4.1.1
	 *
	 * @return cnom in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCnomForTypicalSurface() throws ImproperDataException, LSException {
		double cmin = pos(this::calculateCmin);
		double deltacdev = pos(this::calculateDeltacdev);
		return pos(() -> cmin + deltacdev);
	}

	/**
	 * Calculates and returns nominal cover <code>cnom</code> in meters modified due to concrete being placed on
	 * another concrete according to 4.4.1.2
	 *
	 * @return nominal cover <code>cNom</code> in meters modified due to uneven surface
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCnomForAnotherConcreteSurface() throws ImproperDataException, LSException {
		return (meetsRequirementsForBondBasedCnom()) ? pos(this::calculateCminb) : pos
				(this::calculateCnomForTypicalSurface);
	}

	/**
	 * Calculates and returns nominal cover <code>cnom</code> in meters modified due to uneven surface according to
	 * 4.4.1.2
	 *
	 * @return nominal cover <code>cnom</code> in meters modified due to uneven surface
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCnomForUnevenConcreteSurface() throws ImproperDataException, LSException {
		double cnom = pos(this::calculateCnomForTypicalSurface);
		return pos(() -> cnom + 0.005);
	}

	/**
	 * Calculates and returns nominal cover <code>cNom</code> in meters modified due to surface prone to abrasion
	 * according to 4.4.1.2
	 *
	 * @return nominal cover <code>cNom</code> in meters modified due to surface prone to abrasion
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCnomForProneToAbrasionSurface() throws ImproperDataException,
			LSException {
		double cnom = pos(this::calculateCnomForTypicalSurface);
		switch (notNull(() -> abrasionClass.getSymbol())) {
			case "XM0":
				return cnom;
			case "XM1":
				return cnom + 0.005;
			case "XM2":
				return cnom + 0.010;
			case "XM3":
				return cnom + 0.015;
			default:
				throw new ImproperDataException();
		}
	}

	/**
	 * Calculates and returns nominal cover <code>cNom</code> in meters modified due to uneven surface according to
	 * 4.4.1.2
	 *
	 * @return nominal cover <code>cNom</code> in meters modified due to uneven surface
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCnomForUnevenSurface() throws ImproperDataException, LSException {
		double cnom = pos(this::calculateCnomForTypicalSurface);
		cnom = (cnom < 0.040) ? 0.040 : cnom;
		return cnom;
	}

	/**
	 * Calculates and returns nominal cover <code>cNom</code> in meters modified due to prepared, uneven surface
	 * according to 4.4.1.2
	 *
	 * @return nominal cover <code>cNom</code> in meters modified due to prepared, uneven surface
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCnomForPreparedUnevenSurface() throws ImproperDataException, LSException {
		double cnom = pos(this::calculateCnomForTypicalSurface);
		cnom = (cnom < 0.075) ? 0.075 : cnom;
		return cnom;
	}

	/**
	 * Calculates and returns if cover can be reduced to a value corresponding to the requirement for bond according
	 * to 4.4.1.2
	 *
	 * @return if cover can be reduced to a value corresponding to the requirement for bond
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected boolean meetsRequirementsForBondBasedCnom() throws ImproperDataException, LSException {
		boolean meetsConcreteClassification =
				real(() -> (double) concreteClass.compareTo(concreteClassFactory.get("C25/30"))) >= 0;
		return meetsConcreteClassification && isExposureShort && isInterfaceRoughened;
	}

	/**
	 * Calculates and returns the cover deviation <code>deltaCDev</code> in meters according to 4.4.1.3. If
	 * userDeltacdev is lower than 0, returns recommended value.
	 *
	 * @return returns the cover deviation <code>deltaCDev</code> in meters
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateDeltacdev() throws ImproperDataException, LSException {
		real(userDeltacdev);
		if (userDeltacdev > 0.010) throw new ImproperDataException();
		if (userDeltacdev < 0) userDeltacdev = 0.010;
		return userDeltacdev;
	}

	/**
	 * Calculates and returns minimal cover <code>cMin</code> in meters according to 4.4.1.2
	 *
	 * @return minimal cover <code>cMin</code> in meters
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCmin() throws ImproperDataException, LSException {
		double cminb = pos(this::calculateCminb);
		double cmindur = pos(this::calculateCmindur);
		double deltacdurgamma = nonNeg(this::calculateDeltacdurgamma);
		double deltacdurst = nonNeg(this::calculateDeltacdurst);
		double deltacduradd = nonNeg(this::calculateDeltacduradd);
		double cmindurWithDeviations = pos(() -> cmindur + deltacdurgamma - deltacdurst - deltacduradd);
		double cmin = Math.max(cminb, cmindurWithDeviations);
		cmin = Math.max(cmin, 0.010);
		return cmin;
	}

	/**
	 * Calculates and returns minimum cover due to bond requirements <code>cMinB</code> in meters according to 4.4.1.2
	 *
	 * @return minimum cover due to bond requirements <code>cMinB</code> in meters
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCminb() throws ImproperDataException, LSException {
		double cminb = pos(phi);
		pos(dg);
		if (dg > 0.032) cminb += 0.005;
		return cminb;
	}

	/**
	 * Calculates and returns minimum cover due to enviromental conditions <code>cMinDur</code> in meters according to
	 * 4.4.1.2
	 *
	 * @return minimum cover due to enviromental conditions <code>cMinDur</code> in meters
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateCmindur() throws ImproperDataException, LSException {
		StructuralClassification structuralClassification = notNull(this::calculateModifiedStructuralClass);
		double cmindur = 0;
		for (ExposureClassification exposureClass : exposureClasses) {
			Double cmindurTemp = notNull(() -> cMinDurMap.get(Pair.of(structuralClassification, exposureClass)));
			cmindur = Math.max(cmindur, cmindurTemp);
		}
		return pos(cmindur);
	}

	/**
	 * Calculates and returns recommended, unmodified structural class according to 4.4.1.2
	 *
	 * @return recommended, unmodified structural class according to 4.4.1.2
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected StructuralClassification calculateRecommendedUnmodifiedStructuralClass() throws ImproperDataException,
			LSException {
		return notNull(() -> structuralClassFactory.get("S4"));
	}

	/**
	 * Calculates and returns StructuralClass modified according to 4.4.1.2
	 *
	 * @return StructuralClass modified according to 4.4.1.2
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected StructuralClassification calculateModifiedStructuralClass() throws ImproperDataException, LSException {
		StructuralClassification structuralClass = calculateRecommendedUnmodifiedStructuralClass();
		boolean meetsStrengthClass = meetsStructuralClassReductionStrengthRequirements();
		int summand = 0;
		if (is100yWorkingLife) summand += 2;
		if (meetsStrengthClass) summand--;
		if (isSlab) summand--;
		if (isQualityEnsured) summand--;
		int strength = (int) pos(() -> Double.valueOf(structuralClass.getSymbol().replace("S", "")));
		strength += summand;
		strength = Math.max(1, strength);
		strength = Math.min(strength, 6);
		int finalStrength = strength;
		return notNull(() -> structuralClassFactory.get("S" + finalStrength));
	}

	/**
	 * Determines if <code>concreteClass</code> meets requirements needed to reduce structural class
	 *
	 * @return true if concreteClass meets requirements, false otherwise
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected boolean meetsStructuralClassReductionStrengthRequirements() throws ImproperDataException, LSException {
		Map<Pair<ExposureClassification, Boolean>, String> map = minimalStrengthForStructuralClassReductionMap;
		for (ExposureClassification exposureClass : exposureClasses) {
			ConcreteClassification minimalConcreteClass =
					notNull(() -> concreteClassFactory.get(map.get(Pair.of(exposureClass, isHighAir))));
			if (real(() -> (double) concreteClass.compareTo(minimalConcreteClass)) < 0) return false;
		}
		return true;
	}

	/**
	 * Calculates and returns additive safety element <code>deltaCDurGamma</code> in meters according to 4.4.1.2
	 *
	 * @return additive safety element <code>deltaCDurGamma</code> in meters according to 4.4.1.2
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateDeltacdurgamma() throws ImproperDataException, LSException {
		return 0;
	}

	/**
	 * Calculates and returns reduction of minimum cover for use of stainless steel <code>deltaCDurSt</code> in
	 * meters according to 4.4.1.2
	 *
	 * @return reduction of minimum cover for use of stainless steel <code>deltaCDurSt</code> in meters according to
	 * 4.4.1.2
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateDeltacdurst() throws ImproperDataException, LSException {
		return 0;
	}

	/**
	 * Calculates and returns reduction of minimum cover for use of additional protection <code>deltaCDurAdd</code>
	 * in meters according to 4.4.1.2
	 *
	 * @return reduction of minimum cover for use of additional protection <code>deltaCDurAdd</code> in meters
	 * according to 4.4.1.2
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected double calculateDeltacduradd() throws ImproperDataException, LSException {
		return 0;
	}

	/**
	 * Generates and returns map of minimal concrete class symbols required to lower structural class of the
	 * construction according to 4.4.1.2.
	 *
	 * @return map of minimal concrete class symbols
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Map<Pair<ExposureClassification, Boolean>, String>
	calculateMinimalStrengthForStructuralClassReductionMap() throws ImproperDataException, LSException {
		String[][] columnNames = {{"X0", "XC1"}, {"XC2", "XC3"}, {"XC4", "XD1", "XD2", "XS1"},
				{"XD3", "XS2", "XS3"}};
		String[] lowAirMinimalConcreteClass = {"C30/37", "C35/45", "C40/50", "C45/55"};
		String[] highAirMinimalConcreteClass = {"C25/30", "C30/37", "C35/45", "C40/50"};
		Map<Pair<ExposureClassification, Boolean>, String> minimalStrengthForStructuralClassReductionMap =
				new HashMap<>();

		for (int i = 0; i < columnNames.length; i++) {
			for (String columnName : columnNames[i]) {
				ExposureClassification ExposureClass = exposureClassFactory.get(columnName);
				minimalStrengthForStructuralClassReductionMap.put(Pair.of(ExposureClass, false),
						lowAirMinimalConcreteClass[i]);
				minimalStrengthForStructuralClassReductionMap.put(Pair.of(ExposureClass, true),
						highAirMinimalConcreteClass[i]);
			}
		}

		return minimalStrengthForStructuralClassReductionMap;
	}

	/**
	 * Generates and returns map of minimum cover due to enviromental conditions <code>cMinDur</code> in meters
	 * according to 4.4.1.2.
	 *
	 * @return map of minimum cover due to enviromental conditions <code>cMinDur</code> in meters
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	protected Map<Pair<StructuralClassification, ExposureClassification>, Double>
	calculateCmindurMap() throws ImproperDataException, LSException {
		Map<Pair<StructuralClassification, ExposureClassification>, Double> cmindurMap = new
				HashMap<>();

		double[][] rawData = { //
				{10, 10, 10, 15, 20, 25, 30}, //
				{10, 10, 15, 20, 25, 30, 35}, //
				{10, 10, 20, 25, 30, 35, 40}, //
				{10, 15, 25, 30, 35, 40, 45}, //
				{15, 20, 30, 35, 45, 45, 50}, //
				{20, 25, 35, 40, 50, 50, 55}};

		String[][] columns = {{"X0"}, {"XC1"}, {"XC2", "XC3"}, {"XC4"}, {"XD1", "XS1"}, {"XD2", "XS2"},
				{"XD3", "XS3"},};
		String[] rows = {"S1", "S2", "S3", "S4", "S5", "S6"};

		for (int rowNum = 0; rowNum < rawData.length; rowNum++) {
			for (int colNum = 0; colNum < rawData[rowNum].length; colNum++) {
				double cmindur = rawData[rowNum][colNum] / 1000;
				String structuralClassSymbol = rows[rowNum];
				for (String exposureClassSymbol : columns[colNum]) {
					StructuralClassification structuralClass = structuralClassFactory.get(structuralClassSymbol);
					ExposureClassification exposureClass = exposureClassFactory.get(exposureClassSymbol);
					cmindurMap.put(Pair.of(structuralClass, exposureClass), cmindur);
				}
			}
		}
		return cmindurMap;
	}
}
