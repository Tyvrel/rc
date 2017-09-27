package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.shearreinforcement.MaximumShearReinforcementFactory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.CrossSectionType;
import tyvrel.mag.core.model.Shape;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;
import tyvrel.mag.gui.component.LabeledTextField;
import tyvrel.mag.gui.view.BaseJPanel;
import tyvrel.mag.gui.view.ReinforcementDiameterPanel;

import javax.swing.*;

import static tyvrel.mag.core.exception.Precondition.notNull;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class MaximumShearReinforcementCard extends AbstractCard {
	private ReinforcementDiameterPanel asswDiameterPanel;
	private LabeledTextField ltfWidth;
	private LabeledTextField ltfDg;
	private LabeledTextField ltfCnom;

	public MaximumShearReinforcementCard(String name) {
		super(name);
	}

	@Override
	protected void modifyDataPanel(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		asswDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia na ścinanie", false);
		ltfWidth = new LabeledTextField("Szerokość przekroju [m]: ", "0.3");
		ltfDg = new LabeledTextField("Maksymalny wymiar kruszywa [mm]: ", "32");
		ltfCnom = new LabeledTextField("Otulina [mm]: ", "30");

		JPanel pProps = new BaseJPanel("Inne");
		pProps.add(ltfDg);
		pProps.add(ltfCnom);
		pProps.add(ltfWidth);

		jPanel.add(asswDiameterPanel);
		jPanel.add(pProps);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		ShearReinforcement asw = calculateMaximumShearReinforcement();
		return "Maksymalne zbrojenie na ścinanie: " +
				String.format("%.0f", asw.getNleg()) + "-cięte \u00D8" + String.format("%.0f", asw.getPhi() * 1000)
				+ " co " + String.format("%.0f", 100 / (asw.getN() - 1)) + "cm";
	}

	protected ShearReinforcement calculateMaximumShearReinforcement() throws ImproperDataException, LSException {
		return notNull(() -> new MaximumShearReinforcementFactory(getDg(), getClearance(), getPhisw()).build());
	}

	protected CrossSection getCrossSection() throws ImproperDataException, LSException {
		return new CrossSection(getShape(), null, null, null, CrossSectionType.BEAM, null,
				new ShearReinforcement(0, getPhisw(), 0, 0),
				getCnom()
		);
	}

	protected double getClearance() throws ImproperDataException, LSException {
		return pos(() -> getCrossSection().getAswClearance());
	}

	protected double getPhisw() throws ImproperDataException, LSException {
		return pos(() -> asswDiameterPanel.getDiameters()[0]);
	}

	protected double getDg() throws ImproperDataException, LSException {
		return pos(() -> ltfDg.getDouble() / 1000);
	}

	protected double getCnom() throws ImproperDataException, LSException {
		return pos(() -> ltfCnom.getDouble() / 1000);
	}

	protected Shape getShape() throws ImproperDataException, LSException {
		return notNull(() -> new Shape(ltfWidth.getDouble(), 0.0));
	}
}
