package tyvrel.mag.core.model;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.core.model.classification.Steel;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;

import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Describes cross section of the element
 */
public class CrossSection {
	private Shape shape;
	private int crossSectionType;
	private ConcreteClassification concreteClassification;
	private Steel longitudinalReinforcementSteel;
	private Steel shearReinforcementSteel;
	private LongitudinalReinforcement as;
	private ShearReinforcement asw;
	private double cnom;

	/**
	 * Creates an instance of the cross section
	 *
	 * @param shape                          shape
	 * @param longitudinalReinforcementSteel longitudinal reinforcement steel
	 * @param shearReinforcementSteel        shear reinforcement steel
	 * @param concreteClassification         concrete classification
	 * @param crossSectionType               type of the cross section
	 * @param as                             longitudinal reinforcement
	 * @param asw                            shear reinforcement
	 * @param cnom                           concrete cover in m
	 */
	public CrossSection(Shape shape, Steel longitudinalReinforcementSteel, Steel shearReinforcementSteel,
	                    ConcreteClassification concreteClassification, int crossSectionType,
	                    LongitudinalReinforcement as, ShearReinforcement asw, double cnom) {
		this.longitudinalReinforcementSteel = longitudinalReinforcementSteel;
		this.shearReinforcementSteel = shearReinforcementSteel;
		this.shape = shape;
		this.concreteClassification = concreteClassification;
		this.crossSectionType = crossSectionType;
		this.as = as;
		this.asw = asw;
		this.cnom = cnom;
	}

	/**
	 * Returns axial spacing between bottom reinforcement in m
	 *
	 * @return axial spacing between bottom reinforcement in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public double getAsbSpacing() throws ImproperDataException, LSException {
		Reinforcement as = this.as.getAsb();
		double horizontalClearance = pos(() -> shape.getB() - 2 * cnom - 2 * asw.getPhi() - as.getPhi());
		return pos(() -> horizontalClearance / (as.getN() - 1));
	}

	/**
	 * Returns axial spacing between top reinforcement in m
	 *
	 * @return axial spacing between top reinforcement in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public double getAsaSpacing() throws ImproperDataException, LSException {
		Reinforcement as = this.as.getAsa();
		double horizontalClearance = pos(() -> shape.getB() - 2 * cnom - 2 * asw.getPhi() - as.getPhi
				());
		return pos(() -> horizontalClearance / (as.getN() - 1));
	}

	/**
	 * Returns effective height if bottom is tensioned in m
	 *
	 * @return effective height if bottom is tensioned in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public double getDb() throws ImproperDataException, LSException {
		double h = pos(() -> shape.getH());
		double cNom = pos(() -> cnom);
		double fib = pos(() -> as.getAsb().getPhi());
		double fisw = pos(() -> asw.getPhi());
		return pos(() -> h - cNom - fisw - fib / 2);
	}

	/**
	 * Returns effective height if top is tensioned in m
	 *
	 * @return effective height if top is tensioned in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public double getDa() throws ImproperDataException, LSException {
		double h = pos(() -> shape.getH());
		double cNom = pos(() -> cnom);
		double fia = pos(() -> as.getAsa().getPhi());
		double fisw = pos(() -> asw.getPhi());
		return pos(() -> h - cNom - fisw - fia / 2);
	}

	/**
	 * Returns distance between axis of bottom reinforcement and bottom face of cross section in m
	 *
	 * @return distance between axis of bottom reinforcement and bottom face of cross section in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public double getAb() throws ImproperDataException, LSException {
		double cNom = pos(() -> cnom);
		double fib = pos(() -> as.getAsb().getPhi());
		double fisw = pos(() -> asw.getPhi());
		return pos(() -> cNom + fisw + fib / 2);
	}

	/**
	 * Returns distance between axis of top reinforcement and top face of cross section in m
	 *
	 * @return distance between axis of top reinforcement and top face of cross section in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public double getAa() throws ImproperDataException, LSException {
		double cNom = pos(() -> cnom);
		double fia = pos(() -> as.getAsa().getPhi());
		double fisw = pos(() -> asw.getPhi());
		return pos(() -> cNom + fisw + fia / 2);
	}

	/**
	 * Returns clearance for longitudinal reinforcement in m
	 *
	 * @return clearance for longitudinal reinforcement in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public double getAsClearance() throws ImproperDataException, LSException {
		double b = pos(() -> shape.getB());
		double fisw = pos(() -> asw.getPhi());
		return pos(() -> b - 2 * pos(cnom) - 2 * fisw);
	}

	/**
	 * Returns clearance for shear reinforcement in m
	 *
	 * @return clearance for shear reinforcement in m
	 * @throws ImproperDataException if data is improper
	 * @throws LSException           if limit state is exceeded
	 */
	public double getAswClearance() throws ImproperDataException, LSException {
		double b = pos(() -> shape.getB());
		return pos(() -> b - 2 * pos(cnom));
	}

	@Override
	public String toString() {
		return "CrossSection{" +
				"shape=" + shape +
				", concreteClassification=" + concreteClassification +
				", crossSectionType=" + crossSectionType +
				", as=" + as +
				", asw=" + asw +
				", cnom=" + cnom +
				", longitudinalReinforcementSteel=" + longitudinalReinforcementSteel +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CrossSection that = (CrossSection) o;

		if (crossSectionType != that.crossSectionType) return false;
		if (Double.compare(that.cnom, cnom) != 0) return false;
		if (shape != null ? !shape.equals(that.shape) : that.shape != null) return false;
		if (concreteClassification != null ? !concreteClassification.equals(that.concreteClassification) : that
				.concreteClassification != null)
			return false;
		if (as != null ? !as.equals(that.as) : that.as != null) return false;
		if (asw != null ? !asw.equals(that.asw) : that.asw != null) return false;
		return longitudinalReinforcementSteel != null ? longitudinalReinforcementSteel.equals(that
				.longitudinalReinforcementSteel) : that.longitudinalReinforcementSteel == null;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = shape != null ? shape.hashCode() : 0;
		result = 31 * result + (concreteClassification != null ? concreteClassification.hashCode() : 0);
		result = 31 * result + crossSectionType;
		result = 31 * result + (as != null ? as.hashCode() : 0);
		result = 31 * result + (asw != null ? asw.hashCode() : 0);
		temp = Double.doubleToLongBits(cnom);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (longitudinalReinforcementSteel != null ? longitudinalReinforcementSteel.hashCode() :
				0);
		return result;
	}

	/**
	 * Returns cross section type
	 *
	 * @return cross section type
	 */
	public int getCrossSectionType() {
		return crossSectionType;
	}

	/**
	 * Returns shear reinforcement
	 *
	 * @return shear reinforcement
	 */
	public ShearReinforcement getAsw() {
		return asw;
	}

	/**
	 * Returns concrete classification
	 *
	 * @return concrete classification
	 */
	public ConcreteClassification getConcreteClassification() {
		return concreteClassification;
	}

	/**
	 * Returns longitudinal reinforcement
	 *
	 * @return longitudinal reinforcement
	 */
	public LongitudinalReinforcement getAs() {
		return as;
	}

	/**
	 * Returns shape
	 *
	 * @return shape
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * Returns concrete cover in m
	 *
	 * @return concrete cover in m
	 */
	public double getCnom() {
		return cnom;
	}

	/**
	 * Returns longitudinal reinforcement steel
	 *
	 * @return longitudinal reinforcement steel
	 */
	public Steel getLongitudinalReinforcementSteel() {
		return longitudinalReinforcementSteel;
	}

	/**
	 * Returns shear reinforcement steel
	 *
	 * @return shear reinforcement steel
	 */
	public Steel getShearReinforcementSteel() {
		return shearReinforcementSteel;
	}
}