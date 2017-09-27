package tyvrel.mag.core.model;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Describes the load, which cross section is subject to
 */
public class Load {
	private double medb;
	private double meda;
	private double mcharb;
	private double mchara;
	private double mquasipermb;
	private double mquasiperma;
	private double ved;

	/**
	 * Creates an instance of the load
	 *
	 * @param medb        design bending moment that tensions bottom of the cross section in kNm
	 * @param meda        design bending moment that tensions top of the cross section in kNm
	 * @param mcharb      characteristic bending moment that tensions bottom of the cross section in kNm
	 * @param mchara      characteristic bending moment that tensions top of the cross section in kNm
	 * @param mquasipermb quasi-permanent bending moment that tensions bottom of the cross section in kNm
	 * @param mquasiperma quasi-permanent bending moment that tensions top of the cross section in kNm
	 * @param ved         design shear force in kN
	 */
	public Load(double medb, double meda, double mcharb, double mchara, double mquasipermb, double mquasiperma, double
			ved) {
		this.medb = medb;
		this.meda = meda;
		this.mcharb = mcharb;
		this.mchara = mchara;
		this.mquasipermb = mquasipermb;
		this.mquasiperma = mquasiperma;
		this.ved = ved;
	}

	@Override
	public String toString() {
		return "Load{" +
				"medb=" + medb +
				", meda=" + meda +
				", mcharb=" + mcharb +
				", mchara=" + mchara +
				", mquasipermb=" + mquasipermb +
				", mquasiperma=" + mquasiperma +
				", ved=" + ved +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Load load = (Load) o;

		if (Double.compare(load.medb, medb) != 0) return false;
		if (Double.compare(load.meda, meda) != 0) return false;
		if (Double.compare(load.mcharb, mcharb) != 0) return false;
		if (Double.compare(load.mchara, mchara) != 0) return false;
		if (Double.compare(load.mquasipermb, mquasipermb) != 0) return false;
		if (Double.compare(load.mquasiperma, mquasiperma) != 0) return false;
		return Double.compare(load.ved, ved) == 0;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(medb);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(meda);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(mcharb);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(mchara);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(mquasipermb);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(mquasiperma);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(ved);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	/**
	 * Returns characteristic bending moment that tensions bottom of the cross section in kNm
	 *
	 * @return characteristic bending moment that tensions bottom of the cross section in kNm
	 */
	public double getMcharb() {
		return mcharb;
	}

	/**
	 * Returns characteristic bending moment that tensions top of the cross section in kNm
	 *
	 * @return characteristic bending moment that tensions top of the cross section in kNm
	 */
	public double getMchara() {
		return mchara;
	}

	/**
	 * Returns design bending moment that tensions bottom of the cross section in kNm
	 *
	 * @return design bending moment that tensions bottom of the cross section in kNm
	 */
	public double getMedb() {
		return medb;
	}

	/**
	 * Returns design bending moment that tensions top of the cross section in kNm
	 *
	 * @return design bending moment that tensions top of the cross section in kNm
	 */
	public double getMeda() {
		return meda;
	}

	/**
	 * Returns design shear force in kN
	 *
	 * @return design shear force in kN
	 */
	public double getVed() {
		return ved;
	}

	/**
	 * Returns quasi-permanent bending moment that tensions bottom of the cross section in kNm
	 *
	 * @return quasi-permanent bending moment that tensions bottom of the cross section in kNm
	 */
	public double getMquasipermb() {
		return mquasipermb;
	}

	/**
	 * Returns quasi-permanent bending moment that tensions top of the cross section in kNm
	 *
	 * @return quasi-permanent bending moment that tensions top of the cross section in kNm
	 */
	public double getMquasiperma() {
		return mquasiperma;
	}
}
