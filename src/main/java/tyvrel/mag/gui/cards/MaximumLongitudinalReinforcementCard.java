package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.longitudinalreinforcement.MaximumBeamLongitudinalReinforcementFactory;
import tyvrel.mag.core.factory.longitudinalreinforcement.MaximumLongitudinalReinforcementFactory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.CrossSectionType;
import tyvrel.mag.core.model.Shape;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;
import tyvrel.mag.gui.component.LabeledTextField;
import tyvrel.mag.gui.view.BaseJPanel;
import tyvrel.mag.gui.view.ReinforcementDiameterPanel;
import tyvrel.mag.gui.view.ShapePanel;

import javax.swing.*;

import static tyvrel.mag.core.exception.Precondition.notNull;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class MaximumLongitudinalReinforcementCard extends AbstractCard {
	private ShapePanel shapePanel;
	private ReinforcementDiameterPanel asaDiameterPanel;
	private ReinforcementDiameterPanel asbDiameterPanel;
	private ReinforcementDiameterPanel asswDiameterPanel;
	private LabeledTextField ltfDg;
	private LabeledTextField ltfCnom;

	public MaximumLongitudinalReinforcementCard(String name) {
		super(name);
	}

	@Override
	protected void modifyDataPanel(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		shapePanel = new ShapePanel();
		asaDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia górą", false);
		asbDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia dołem", false);
		asswDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia na ścinanie", false);
		ltfDg = new LabeledTextField("Maksymalny wymiar kruszywa [mm]: ", "32");
		ltfCnom = new LabeledTextField("Otulina [mm]: ", "30");

		JPanel pProps = new BaseJPanel("Inne");
		pProps.add(ltfDg);
		pProps.add(ltfCnom);

		jPanel.add(shapePanel);
		jPanel.add(asaDiameterPanel);
		jPanel.add(asbDiameterPanel);
		jPanel.add(asswDiameterPanel);
		jPanel.add(pProps);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		LongitudinalReinforcement longitudinalReinforcement = calculateMaximumLongitudinalReinforcement();
		Reinforcement asa = longitudinalReinforcement.getAsa();
		Reinforcement asb = longitudinalReinforcement.getAsb();
		return "<html>Maksymalne zbrojenie górą: " +
				String.format("%.0f", asa.getN()) + "\u00D8" + String.format("%.0f", asa.getPhi() * 1000)
				+ "<br>Maksymalne zbrojenie dołem: " +
				String.format("%.0f", asb.getN()) + "\u00D8" + String.format("%.0f", asb.getPhi() * 1000) + "</html>";
	}

	protected double getPhia() throws ImproperDataException, LSException {
		return pos(() -> asaDiameterPanel.getDiameters()[0]);
	}

	protected double getPhib() throws ImproperDataException, LSException {
		return pos(() -> asbDiameterPanel.getDiameters()[0]);
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
		return notNull(() -> shapePanel.getShape());
	}

	protected LongitudinalReinforcement getLongitudinalReinforcement() throws ImproperDataException, LSException {
		return notNull(() -> new LongitudinalReinforcement(
				new Reinforcement(0, getPhib(), 0, 0),
				new Reinforcement(0, getPhia(), 0, 0)
		));
	}

	protected CrossSection getCrossSection() throws ImproperDataException, LSException {
		return new CrossSection(
				getShape(), null, null, null, CrossSectionType.BEAM,
				getLongitudinalReinforcement(),
				new ShearReinforcement(0, getPhisw(), 0, 0),
				getCnom()
		);
	}

	protected LongitudinalReinforcement getMaximumLongitudinalReinforcement() throws ImproperDataException,
			LSException {
		return notNull(() -> new MaximumLongitudinalReinforcementFactory(getDg(), getCrossSection()).build());
	}

	protected LongitudinalReinforcement getMaximumBeamLongitudinalReinforcement() throws ImproperDataException,
			LSException {
		return notNull(() -> new MaximumBeamLongitudinalReinforcementFactory(getLongitudinalReinforcement(),
				getShape())
				.build());
	}

	protected LongitudinalReinforcement calculateMaximumLongitudinalReinforcement() throws ImproperDataException,
			LSException {
		LongitudinalReinforcement as1 = getMaximumLongitudinalReinforcement();
		LongitudinalReinforcement as2 = getMaximumBeamLongitudinalReinforcement();
		return new LongitudinalReinforcement(
				new Reinforcement(Math.min(as1.getAsb().getN(), as2.getAsb().getN()), getPhib(), 0, 0),
				new Reinforcement(Math.min(as1.getAsa().getN(), as2.getAsa().getN()), getPhib(), 0, 0)
		);
	}
}
