package tyvrel.mag.core.model.classification;

import tyvrel.mag.core.exception.ImproperDataException;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Class that represents concrete classification defined by PN-EN 206-1
 */
public class ConcreteClassification extends AbstractClassification implements Comparable<ConcreteClassification> {
	private static final String FORMAT_REGEX = ".*";

	// Characteristic compressive cylinder strength of concrete at 28 days in Pa
	private double fck;

	// Characteristic compressive cube strength of concrete at 28 days in Pa
	private double fckcube;

	// Mean value of concrete cylinder compressive strength in Pa
	private double fcm;

	// Mean value of axial tensile strength of concrete in Pa
	private double fctm;

	// 5% fractile of characteristic axial tensile strength of concrete in Pa
	private double fctk005;

	// 95% fractile of characteristic axial tensile strength of concrete in Pa
	private double fctk095;

	// Secant modulus of elasticity of concrete in Pa
	private double ecm;

	// Compressive strain in the concrete at the peak stresschar, dimensionless
	private double epsilonc1;

	// Nominal ultimate strain, dimensionless
	private double epsiloncu1;

	// Strain at reaching the maximum strength in parabola-rectangle approach, dimensionless
	private double epsilonc2;

	// Ultimate strain in parabola-rectangle approach, dimensionless
	private double epslioncu2;

	// Exponent of concrete stresschar-strain function in parabola-rectangle approach, dimensionless
	private double n;

	// Strain at reaching the maximum strength in bilinear approach, dimensionless
	private double epsilonc3;

	// Ultimate strain in bilinear approach, dimensionless
	private double epsiloncu3;

	/**
	 * Creates concrete classification denoted by <code>symbol</code>
	 */
	public ConcreteClassification(String symbol, double fck, double fckcube, double fcm, double
			fctm, double fctk005, double fctk095, double ecm, double epsilonc1, double epsiloncu1, double epsilonc2,
	                              double epslionCu2, double n, double epsilonc3, double epsiloncu3) throws
			ImproperDataException {
		super(FORMAT_REGEX, symbol);
		this.fck = fck;
		this.fckcube = fckcube;
		this.fcm = fcm;
		this.fctm = fctm;
		this.fctk005 = fctk005;
		this.fctk095 = fctk095;
		this.ecm = ecm;
		this.epsilonc1 = epsilonc1;
		this.epsiloncu1 = epsiloncu1;
		this.epsilonc2 = epsilonc2;
		this.epslioncu2 = epslionCu2;
		this.n = n;
		this.epsilonc3 = epsilonc3;
		this.epsiloncu3 = epsiloncu3;
	}

	/**
	 * Returns characteristic compressive cylinder strength of concrete at 28 days in Pa
	 *
	 * @return the characteristic compressive cylinder strength of concrete at 28 days in Pa
	 */
	public double getFck() {
		return fck;
	}

	/**
	 * Returns characteristic compressive cube strength of concrete at 28 days in Pa
	 *
	 * @return the characteristic compressive cube strength of concrete at 28 days in Pa
	 */
	public double getFckcube() {
		return fckcube;
	}

	/**
	 * Returns mean value of concrete cylinder compressive strength in Pa
	 *
	 * @return the mean value of concrete cylinder compressive strength in Pa
	 */
	public double getFcm() {
		return fcm;
	}

	/**
	 * Returns mean value of axial tensile strength of concrete in Pa
	 *
	 * @return the mean value of axial tensile strength of concrete in Pa
	 */
	public double getfctm() {
		return fctm;
	}

	/**
	 * Returns 5% fractile of characteristic axial tensile strength of concrete in Pa
	 *
	 * @return the 5% fractile of characteristic axial tensile strength of concrete in Pa
	 */
	public double getFctk005() {
		return fctk005;
	}

	/**
	 * Returns 95% fractile of characteristic axial tensile strength of concrete in Pa
	 *
	 * @return the 95% fractile of characteristic axial tensile strength of concrete in Pa
	 */
	public double getFctk095() {
		return fctk095;
	}

	/**
	 * Returns secant modulus of elasticity of concrete in Pa
	 *
	 * @return the secant modulus of elasticity of concrete in Pa
	 */
	public double getEcm() {
		return ecm;
	}

	/**
	 * Returns compressive strain in the concrete at the peak stresschar, dimensionless
	 *
	 * @return the compressive strain in the concrete at the peak stresschar, dimensionless
	 */
	public double getEpsilonc1() {
		return epsilonc1;
	}

	/**
	 * Returns nominal ultimate strain, dimensionless
	 *
	 * @return the nominal ultimate strain, dimensionless
	 */
	public double getEpsiloncu1() {
		return epsiloncu1;
	}

	/**
	 * Returns strain at reaching the maximum strength in parabola-rectangle approach, dimensionless
	 *
	 * @return the strain at reaching the maximum strength in parabola-rectangle approach, dimensionless
	 */
	public double getEpsilonc2() {
		return epsilonc2;
	}

	/**
	 * Returns ultimate strain in parabola-rectangle approach, dimensionless
	 *
	 * @return the ultimate strain in parabola-rectangle approach, dimensionless
	 */
	public double getEpslioncu2() {
		return epslioncu2;
	}

	/**
	 * Returns exponent of concrete stresschar-strain function in parabola-rectangle approach, dimensionless
	 *
	 * @return the exponent of concrete stresschar-strain function in parabola-rectangle approach, dimensionless
	 */
	public double getN() {
		return n;
	}

	/**
	 * Returns strain at reaching the maximum strength in bilinear approach, dimensionless
	 *
	 * @return the strain at reaching the maximum strength in bilinear approach, dimensionless
	 */
	public double getEpsilonc3() {
		return epsilonc3;
	}

	/**
	 * Returns ultimate strain in bilinear approach, dimensionless
	 *
	 * @return the ultimate strain in bilinear approach, dimensionless
	 */
	public double getEpsiloncu3() {
		return epsiloncu3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(ecm);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(epsilonc1);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(epsilonc2);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(epsilonc3);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(epsiloncu1);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(epsiloncu3);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(epslioncu2);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(fck);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(fckcube);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(fcm);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(fctk005);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(fctk095);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(fctm);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(n);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ConcreteClassification other = (ConcreteClassification) obj;
		if (Double.doubleToLongBits(ecm) != Double.doubleToLongBits(other.ecm)) return false;
		if (Double.doubleToLongBits(epsilonc1) != Double.doubleToLongBits(other.epsilonc1)) return false;
		if (Double.doubleToLongBits(epsilonc2) != Double.doubleToLongBits(other.epsilonc2)) return false;
		if (Double.doubleToLongBits(epsilonc3) != Double.doubleToLongBits(other.epsilonc3)) return false;
		if (Double.doubleToLongBits(epsiloncu1) != Double.doubleToLongBits(other.epsiloncu1)) return false;
		if (Double.doubleToLongBits(epsiloncu3) != Double.doubleToLongBits(other.epsiloncu3)) return false;
		if (Double.doubleToLongBits(epslioncu2) != Double.doubleToLongBits(other.epslioncu2)) return false;
		if (Double.doubleToLongBits(fck) != Double.doubleToLongBits(other.fck)) return false;
		if (Double.doubleToLongBits(fckcube) != Double.doubleToLongBits(other.fckcube)) return false;
		if (Double.doubleToLongBits(fcm) != Double.doubleToLongBits(other.fcm)) return false;
		if (Double.doubleToLongBits(fctk005) != Double.doubleToLongBits(other.fctk005)) return false;
		if (Double.doubleToLongBits(fctk095) != Double.doubleToLongBits(other.fctk095)) return false;
		if (Double.doubleToLongBits(fctm) != Double.doubleToLongBits(other.fctm)) return false;
		if (Double.doubleToLongBits(n) != Double.doubleToLongBits(other.n)) return false;
		return true;
	}

	@Override
	public int compareTo(ConcreteClassification otherConcreteClass) {
		if (otherConcreteClass == null) throw new NullPointerException("otherConcreteClass cannot be null");
		Double thisFCkDouble = fck;
		Double otherFCkDouble = otherConcreteClass.getFck();
		return thisFCkDouble.compareTo(otherFCkDouble);
	}

	@Override
	public String toString() {
		return "ConcreteClassification{" +
				"fck=" + fck +
				", fckcube=" + fckcube +
				", fcm=" + fcm +
				", fctm=" + fctm +
				", fctk005=" + fctk005 +
				", fctk095=" + fctk095 +
				", ecm=" + ecm +
				", epsilonc1=" + epsilonc1 +
				", epsiloncu1=" + epsiloncu1 +
				", epsilonc2=" + epsilonc2 +
				", epslioncu2=" + epslioncu2 +
				", n=" + n +
				", epsilonc3=" + epsilonc3 +
				", epsiloncu3=" + epsiloncu3 +
				"} " + super.toString();
	}
}
