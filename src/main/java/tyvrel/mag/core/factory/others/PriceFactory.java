package tyvrel.mag.core.factory.others;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.Factory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.PriceList;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;

import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class PriceFactory implements Factory<Double> {
	private CrossSection crossSection;
	private PriceList priceList;

	public PriceFactory(CrossSection crossSection, PriceList priceList) {
		this.crossSection = crossSection;
		this.priceList = priceList;
	}

	@Override
	public Double build() throws ImproperDataException, LSException {
		return pos(pos(calculateReinforcementPrice()) + pos(calculateFormworkPrice()) + pos(calculateConcretePrice()));
	}


	protected double calculateReinforcementPrice() throws ImproperDataException, LSException {
		notNull(crossSection);
		LongitudinalReinforcement as = notNull(crossSection.getAs());
		double asAarea = pos(notNull(as.getAsa().getA()));
		double asBarea = pos(notNull(as.getAsb().getA()));
		double aSwArea = pos(notNull(crossSection.getAsw().getA()));
		double steelArea = pos(asAarea + asBarea + aSwArea);
		double steelDensity = 7850; //kg/m3
		double steelMass = pos(steelArea * steelDensity);
		return pos(steelMass * nonNeg(notNull(priceList).getReinforcementPrice()));
	}

	protected double calculateFormworkPrice() throws ImproperDataException, LSException {
		double b = pos(() -> crossSection.getShape().getB());
		double h = pos(() -> crossSection.getShape().getH());
		double perimeter = pos(b + 2 * h);
		return nonNeg(perimeter * nonNeg(notNull(priceList).getFormworkPrice()));
	}

	protected double calculateConcretePrice() throws ImproperDataException, LSException {
		double price;
		notNull(priceList);
		String symbol = notNull(notNull(crossSection).getConcreteClassification()).getSymbol();
		switch (symbol) {
			case "C12/15":
				price = nonNeg(priceList.getC1215Price());
				break;
			case "C16/20":
				price = nonNeg(priceList.getC1620Price());
				break;
			case "C20/25":
				price = nonNeg(priceList.getC2025Price());
				break;
			case "C25/30":
				price = nonNeg(priceList.getC2530Price());
				break;
			case "C30/37":
				price = nonNeg(priceList.getC3037Price());
				break;
			case "C35/45":
				price = nonNeg(priceList.getC3545Price());
				break;
			case "C40/50":
				price = nonNeg(priceList.getC4050Price());
				break;
			case "C45/55":
				price = nonNeg(priceList.getC4555Price());
				break;
			case "C50/60":
				price = nonNeg(priceList.getC5060Price());
				break;
			case "C55/67":
				price = nonNeg(priceList.getC5567Price());
				break;
			case "C60/75":
				price = nonNeg(priceList.getC6075Price());
				break;
			case "C70/85":
				price = nonNeg(priceList.getC7085Price());
				break;
			case "C80/95":
				price = nonNeg(priceList.getC8095Price());
				break;
			case "C90/105":
				price = nonNeg(priceList.getC90105Price());
				break;
			default:
				throw new ImproperDataException();
		}
		price += priceList.getConcreteLabourPrice();
		return nonNeg(pos(() -> crossSection.getShape().getA()) * price);
	}

}
