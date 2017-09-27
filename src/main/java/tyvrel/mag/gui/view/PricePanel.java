package tyvrel.mag.gui.view;


import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.model.PriceList;
import tyvrel.mag.gui.component.LabeledTextField;

import static tyvrel.mag.core.exception.Precondition.nonNeg;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class PricePanel extends BaseJPanel {
	private LabeledTextField ltfConcreteLabourPrice = new LabeledTextField("Cena betonowania (bez materiału) " +
			"[zł/m3]", "167.85");
	private LabeledTextField ltfC1215Price = new LabeledTextField("Cena betonu C12/15 [zł/m3]", "210.35");
	private LabeledTextField ltfC1620Price = new LabeledTextField("Cena betonu C16/20 [zł/m3]", "229.90");
	private LabeledTextField ltfC2025Price = new LabeledTextField("Cena betonu C20/25 [zł/m3]", "243.48");
	private LabeledTextField ltfC2530Price = new LabeledTextField("Cena betonu C25/30 [zł/m3]", "261.46");
	private LabeledTextField ltfC3037Price = new LabeledTextField("Cena betonu C30/37 [zł/m3]", "278.92");
	private LabeledTextField ltfC3545Price = new LabeledTextField("Cena betonu C35/45 [zł/m3]", "341.97");
	private LabeledTextField ltfC4050Price = new LabeledTextField("Cena betonu C40/50 [zł/m3]", "369.41");
	private LabeledTextField ltfC4555Price = new LabeledTextField("Cena betonu C45/55 [zł/m3]", "406.35");
	private LabeledTextField ltfC5060Price = new LabeledTextField("Cena betonu C50/60 [zł/m3]", "446.99");
	private LabeledTextField ltfC5567Price = new LabeledTextField("Cena betonu C55/67 [zł/m3]", "491.68");
	private LabeledTextField ltfC6075Price = new LabeledTextField("Cena betonu C60/75 [zł/m3]", "540.85");
	private LabeledTextField ltfC7085Price = new LabeledTextField("Cena betonu C70/85 [zł/m3]", "594.94");
	private LabeledTextField ltfC8095Price = new LabeledTextField("Cena betonu C80/95 [zł/m3]", "654.43");
	private LabeledTextField ltfC90105Price = new LabeledTextField("Cena betonu C90/105 [zł/m3]", "719.88");
	private LabeledTextField ltfFormworkPrice = new LabeledTextField("Cena deskowania [zł/m2]", "60.95");
	private LabeledTextField ltfReinforcementPrice = new LabeledTextField("Cena zbrojenia [zł/kg]", "3.93655");

	public PricePanel() {
		super("Cennik");
		add(ltfReinforcementPrice);
		add(ltfFormworkPrice);
		add(ltfConcreteLabourPrice);
		add(ltfC1215Price);
		add(ltfC1620Price);
		add(ltfC2025Price);
		add(ltfC2530Price);
		add(ltfC3037Price);
		add(ltfC3545Price);
		add(ltfC4050Price);
		add(ltfC4555Price);
		add(ltfC5060Price);
		add(ltfC5567Price);
		add(ltfC6075Price);
		add(ltfC7085Price);
		add(ltfC8095Price);
		add(ltfC90105Price);
	}

	public PriceList getPriceList() throws ImproperDataException {
		return new PriceList(
				nonNeg(ltfConcreteLabourPrice.getDouble()),
				nonNeg(ltfC1215Price.getDouble()),
				nonNeg(ltfC1620Price.getDouble()),
				nonNeg(ltfC2025Price.getDouble()),
				nonNeg(ltfC2530Price.getDouble()),
				nonNeg(ltfC3037Price.getDouble()),
				nonNeg(ltfC3545Price.getDouble()),
				nonNeg(ltfC4050Price.getDouble()),
				nonNeg(ltfC4555Price.getDouble()),
				nonNeg(ltfC5060Price.getDouble()),
				nonNeg(ltfC5567Price.getDouble()),
				nonNeg(ltfC6075Price.getDouble()),
				nonNeg(ltfC7085Price.getDouble()),
				nonNeg(ltfC8095Price.getDouble()),
				nonNeg(ltfC90105Price.getDouble()),
				nonNeg(ltfFormworkPrice.getDouble()),
				nonNeg(ltfReinforcementPrice.getDouble())
		);
	}
}
