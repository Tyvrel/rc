package tyvrel.mag.core.factory.dimensioning;

import tyvrel.mag.core.exception.*;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.*;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.core.model.classification.ExposureClassification;
import tyvrel.mag.core.model.classification.Steel;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;

import java.util.ArrayList;
import java.util.List;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Factory, which calculates cross sections
 */
@SuppressWarnings("WeakerAccess")
public class CrossSectionsFactory implements Factory {
	private final double[] fias;
	private final double[] fibs;
	private final double[] fisws;
	private final Range widthRange;
	private final Range heightRange;
	private final ConcreteClassification[] concreteClasses;
	private final ExposureClassification[] exposureClasses;
	private final Load[] loads;
	private final Steel longitudinalReinforcementSteel;
	private final Steel shearReinforcementSteel;
	private final Factors factors;
	private final boolean is100yWorkingLife;
	private final boolean isQualityEnsured;
	private final boolean isHighAir;
	private final double dg;

	/**
	 * Creates an instance of the factory
	 *
	 * @param fias                           diameters of top longitudinal reinforcement in m
	 * @param fibs                           diameters of bottom longitudinal reinforcement in m
	 * @param fisws                          diameters of shear reinforcement in m
	 * @param widthRange                     range of width
	 * @param heightRange                    range of height
	 * @param concreteClasses                concrete classes
	 * @param exposureClasses                exposure classes
	 * @param loads                          loads
	 * @param longitudinalReinforcementSteel longitudinal reinforcement longitudinalReinforcementSteel
	 * @param shearReinforcementSteel        shear reinforcement longitudinalReinforcementSteel
	 * @param factors                        partial factors
	 * @param is100yWorkingLife              if element is designed for 100 years working life
	 * @param isQualityEnsured               if special concrete quality control is ensure
	 * @param isHighAir                      if air containment is higher than 4%
	 * @param dg                             maximal diameter of aggregate in m
	 */
	public CrossSectionsFactory(double[] fias, double[] fibs, double[] fisws, Range widthRange, Range heightRange,
	                            ConcreteClassification[] concreteClasses, ExposureClassification[] exposureClasses,
	                            Load[] loads, Steel longitudinalReinforcementSteel, Steel shearReinforcementSteel,
	                            Factors factors, boolean is100yWorkingLife, boolean isQualityEnsured, boolean
			                            isHighAir, double dg) {
		this.fias = fias;
		this.fibs = fibs;
		this.fisws = fisws;
		this.widthRange = widthRange;
		this.heightRange = heightRange;
		this.concreteClasses = concreteClasses;
		this.exposureClasses = exposureClasses;
		this.loads = loads;
		this.longitudinalReinforcementSteel = longitudinalReinforcementSteel;
		this.shearReinforcementSteel = shearReinforcementSteel;
		this.factors = factors;
		this.is100yWorkingLife = is100yWorkingLife;
		this.isQualityEnsured = isQualityEnsured;
		this.isHighAir = isHighAir;
		this.dg = dg;
	}

	/**
	 * Calculates and returns cross sections
	 *
	 * @return cross sections
	 * @throws ImproperDataException never
	 * @throws LSException           never
	 */
	public CrossSection[] build() throws ImproperDataException, LSException {
		return calculateCrossSections();
	}

	/**
	 * Calculates and returns cross sections
	 *
	 * @return cross sections
	 * @throws ImproperDataException never
	 * @throws LSException           never
	 */
	protected CrossSection[] calculateCrossSections() throws ImproperDataException, LSException {
		List<CrossSection> crossSectionList = new ArrayList<>();

		concrete:
		for (ConcreteClassification concreteClass : concreteClasses) {
			height:
			for (double h = heightRange.getFrom(); h <= heightRange.getTo(); h += heightRange.getIncrement()) {
				width:
				for (double b = widthRange.getFrom(); b <= widthRange.getTo(); b += widthRange.getIncrement()) {
					reinforcementB:
					for (double fib : fibs) {
						reinforcementA:
						for (double fia : fias) {
							reinforcementSw:
							for (double fisw : fisws) {
								try {
									CrossSection crossSection = new CrossSection(new Shape(b, h),
											longitudinalReinforcementSteel,
											shearReinforcementSteel, concreteClass, CrossSectionType.BEAM,
											new LongitudinalReinforcement(
													new Reinforcement(2, fib, 0, 0),
													new Reinforcement(2, fia, 0, 0)
											),
											new ShearReinforcement(0, fisw, 0, 2),
											0
									);

									crossSectionList.add(new CrossSectionFactory(exposureClasses,
											loads, factors, crossSection, is100yWorkingLife, isQualityEnsured,
											isHighAir, dg)
											.build());
								} catch (HeightTooSmallException e) {
									continue height;
								} catch (ConcreteClassTooSmallException e) {
									continue concrete;
								} catch (CompressionTooHighException | WidthTooSmallException e) {
									continue width;
								} catch (ImproperDataException e) {
								} catch (ForceInReinforcementATooHighException e) {
									continue reinforcementA;
								} catch (ForceInReinforcementBTooHighException e) {
									continue reinforcementB;
								} catch (ForceInReinforcementSWTooHighException e) {
									continue reinforcementSw;
								} catch (ShearReinforcementDiameterTooHighException e) {
									continue reinforcementSw;
								} catch (SLSException e) {
									continue;
								} catch (LSException e) {
									continue;
								}
							}
						}
					}
				}
			}
		}
		return crossSectionList.toArray(new CrossSection[0]);
	}

}
