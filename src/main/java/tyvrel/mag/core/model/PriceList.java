package tyvrel.mag.core.model;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Describes price list
 */
public class PriceList {
	private double concreteLabourPrice = 167.85;
	private double c1215Price = 210.35;
	private double c1620Price = 229.9;
	private double c2025Price = 243.48;
	private double c2530Price = 261.46;
	private double c3037Price = 278.92;
	private double c3545Price = 341.97;
	private double c4050Price = 369.41;
	private double c4555Price = c4050Price * 1.1;
	private double c5060Price = c4555Price * 1.1;
	private double c5567Price = c5060Price * 1.1;
	private double c6075Price = c5567Price * 1.1;
	private double c7085Price = c6075Price * 1.1;
	private double c8095Price = c7085Price * 1.1;
	private double c90105Price = c8095Price * 1.1;
	private double formworkPrice = 60.95;
	private double reinforcementPrice = 3.93655;

	/**
	 * Creates an instance of price list
	 *
	 * @param concreteLabourPrice price of concrete labour in currency/m3
	 * @param c1215Price          price of concrete C12/15 in currency/m3
	 * @param c1620Price          price of concrete C16/20 in currency/m3
	 * @param c2025Price          price of concrete C20/25 in currency/m3
	 * @param c2530Price          price of concrete C25/30 in currency/m3
	 * @param c3037Price          price of concrete C30/37 in currency/m3
	 * @param c3545Price          price of concrete C35/45 in currency/m3
	 * @param c4050Price          price of concrete C40/50 in currency/m3
	 * @param c4555Price          price of concrete C45/55 in currency/m3
	 * @param c5060Price          price of concrete C50/60 in currency/m3
	 * @param c5567Price          price of concrete C55/67 in currency/m3
	 * @param c6075Price          price of concrete C60/75 in currency/m3
	 * @param c7085Price          price of concrete C70/85 in currency/m3
	 * @param c8095Price          price of concrete C80/95 in currency/m3
	 * @param c90105Price         price of concrete C90/105 in currency/m3
	 * @param formworkPrice       price of formwork work in currency/m2
	 * @param reinforcementPrice  price of shearreinforcement work in currency/kg
	 */
	public PriceList(double concreteLabourPrice, double c1215Price, double c1620Price, double c2025Price, double
			c2530Price, double c3037Price, double c3545Price, double c4050Price, double c4555Price, double
			                 c5060Price, double c5567Price, double c6075Price, double c7085Price, double c8095Price,
	                 double
			                 c90105Price, double formworkPrice, double reinforcementPrice) {
		this.concreteLabourPrice = concreteLabourPrice;
		this.c1215Price = c1215Price;
		this.c1620Price = c1620Price;
		this.c2025Price = c2025Price;
		this.c2530Price = c2530Price;
		this.c3037Price = c3037Price;
		this.c3545Price = c3545Price;
		this.c4050Price = c4050Price;
		this.c4555Price = c4555Price;
		this.c5060Price = c5060Price;
		this.c5567Price = c5567Price;
		this.c6075Price = c6075Price;
		this.c7085Price = c7085Price;
		this.c8095Price = c8095Price;
		this.c90105Price = c90105Price;
		this.formworkPrice = formworkPrice;
		this.reinforcementPrice = reinforcementPrice;
	}

	/**
	 * Returns price of concrete labour in currency/m3
	 *
	 * @return price of concrete labour in currency/m3
	 */
	public double getConcreteLabourPrice() {
		return concreteLabourPrice;
	}

	/**
	 * Returns price of concrete C12/15 in currency/m3
	 *
	 * @return price of concrete C12/15 in currency/m3
	 */
	public double getC1215Price() {
		return c1215Price;
	}

	/**
	 * Returns price of concrete C16/20 in currency/m3
	 *
	 * @return price of concrete C16/20 in currency/m3
	 */
	public double getC1620Price() {
		return c1620Price;
	}

	/**
	 * Returns price of concrete C20/25 in currency/m3
	 *
	 * @return price of concrete C20/25 in currency/m3
	 */
	public double getC2025Price() {
		return c2025Price;
	}

	/**
	 * Returns price of concrete C25/30 in currency/m3
	 *
	 * @return price of concrete C25/30 in currency/m3
	 */
	public double getC2530Price() {
		return c2530Price;
	}

	/**
	 * Returns price of concrete C30/37 in currency/m3
	 *
	 * @return price of concrete C30/37 in currency/m3
	 */
	public double getC3037Price() {
		return c3037Price;
	}

	/**
	 * Returns price of concrete C35/45 in currency/m3
	 *
	 * @return price of concrete C35/45 in currency/m3
	 */
	public double getC3545Price() {
		return c3545Price;
	}

	/**
	 * Returns price of concrete C40/50 in currency/m3
	 *
	 * @return price of concrete C40/50 in currency/m3
	 */
	public double getC4050Price() {
		return c4050Price;
	}

	/**
	 * Returns price of concrete C45/55 in currency/m3
	 *
	 * @return price of concrete C45/55 in currency/m3
	 */
	public double getC4555Price() {
		return c4555Price;
	}

	/**
	 * Returns price of concrete C50/60 in currency/m3
	 *
	 * @return price of concrete C50/60 in currency/m3
	 */
	public double getC5060Price() {
		return c5060Price;
	}

	/**
	 * Returns price of concrete C55/67 in currency/m3
	 *
	 * @return price of concrete C55/67 in currency/m3
	 */
	public double getC5567Price() {
		return c5567Price;
	}

	/**
	 * Returns price of concrete C60/75 in currency/m3
	 *
	 * @return price of concrete C60/75 in currency/m3
	 */
	public double getC6075Price() {
		return c6075Price;
	}

	/**
	 * Returns price of concrete C70/85 in currency/m3
	 *
	 * @return price of concrete C70/85 in currency/m3
	 */
	public double getC7085Price() {
		return c7085Price;
	}

	/**
	 * Returns price of concrete C80/95 in currency/m3
	 *
	 * @return price of concrete C80/95 in currency/m3
	 */
	public double getC8095Price() {
		return c8095Price;
	}

	/**
	 * Returns price of concrete C90/105 in currency/m3
	 *
	 * @return price of concrete C90/105 in currency/m3
	 */
	public double getC90105Price() {
		return c90105Price;
	}

	/**
	 * Returns price of formwork work in currency/m2
	 *
	 * @return price of formwork work in currency/m2
	 */
	public double getFormworkPrice() {
		return formworkPrice;
	}

	/**
	 * Returns price of shearreinforcement work in currency/kg
	 *
	 * @return price of shearreinforcement work in currency/kg
	 */
	public double getReinforcementPrice() {
		return reinforcementPrice;
	}
}
