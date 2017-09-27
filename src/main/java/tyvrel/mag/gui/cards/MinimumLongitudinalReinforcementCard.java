package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.classification.ConcreteClassificationFactory;
import tyvrel.mag.core.factory.classification.SteelFactory;
import tyvrel.mag.core.factory.longitudinalreinforcement.MinimumBeamLongitudinalReinforcementFactory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.CrossSectionType;
import tyvrel.mag.core.model.Shape;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.core.model.classification.Steel;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;
import tyvrel.mag.gui.component.LabeledTextField;
import tyvrel.mag.gui.view.*;

import javax.swing.*;

import static tyvrel.mag.core.exception.Precondition.notNull;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class MinimumLongitudinalReinforcementCard extends AbstractCard {
	private BaseClassificationPanel<ConcreteClassification> concreteClassificationPanel;
	private SteelPanel steelPanel;
	private ShapePanel shapePanel;
	private ReinforcementDiameterPanel asaDiameterPanel;
	private ReinforcementDiameterPanel asbDiameterPanel;
	private ReinforcementDiameterPanel asswDiameterPanel;
	private LabeledTextField ltfCnom;

	public MinimumLongitudinalReinforcementCard(String name) {
		super(name);
	}

	@Override
	protected void modifyDataPanel(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		concreteClassificationPanel =
				new BaseClassificationPanel<>("Beton", new ConcreteClassificationFactory(), false);
		steelPanel = new SteelPanel(new SteelFactory());
		shapePanel = new ShapePanel();
		asaDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia górnego", false);
		asbDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia dolnego", false);
		asswDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia na ścinanie", false);
		ltfCnom = new LabeledTextField("Otulina [mm]: ", "30");

		JPanel pProps = new BaseJPanel("Inne");

		pProps.add(ltfCnom);

		jPanel.add(concreteClassificationPanel);
		jPanel.add(steelPanel);
		jPanel.add(shapePanel);
		jPanel.add(asaDiameterPanel);
		jPanel.add(asbDiameterPanel);
		jPanel.add(asswDiameterPanel);
		jPanel.add(pProps);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		LongitudinalReinforcement longitudinalReinforcement = calculateMinimumLongitudinalReinforcement();
		Reinforcement asa = longitudinalReinforcement.getAsa();
		Reinforcement asb = longitudinalReinforcement.getAsb();
		return "<html>Minimalne zbrojenie górą: " +
				String.format("%.0f", asa.getN()) + "\u00D8" + String.format("%.0f", asa.getPhi() * 1000)
				+ "<br>Minimalne zbrojenie dołem: " +
				String.format("%.0f", asb.getN()) + "\u00D8" + String.format("%.0f", asb.getPhi() * 1000) + "</html>";
	}

	protected LongitudinalReinforcement calculateMinimumLongitudinalReinforcement() throws ImproperDataException,
			LSException {
		return notNull(() -> new MinimumBeamLongitudinalReinforcementFactory(getCrossSection()).build());
	}

	protected double getCnom() throws ImproperDataException, LSException {
		return pos(() -> ltfCnom.getDouble() / 1000);
	}

	protected Shape getShape() throws ImproperDataException, LSException {
		return notNull(() -> shapePanel.getShape());
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

	protected CrossSection getCrossSection() throws ImproperDataException, LSException {
		return new CrossSection(
				getShape(), getSteel(), null, getConcreteClassification(), CrossSectionType.BEAM,
				new LongitudinalReinforcement(
						new Reinforcement(0, getPhib(), 0, 0),
						new Reinforcement(0, getPhia(), 0, 0)
				),
				new ShearReinforcement(0, getPhisw(), 0, 0),
				getCnom()
		);
	}

	protected ConcreteClassification getConcreteClassification() throws ImproperDataException, LSException {
		return notNull(() -> concreteClassificationPanel.getClassifications(ConcreteClassification[]::new)[0]);
	}

	protected Steel getSteel() throws ImproperDataException, LSException {
		return notNull(() -> steelPanel.getSteel());
	}
}
