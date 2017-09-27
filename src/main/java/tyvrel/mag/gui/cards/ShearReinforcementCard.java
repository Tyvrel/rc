package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.classification.ConcreteClassificationFactory;
import tyvrel.mag.core.factory.classification.SteelFactory;
import tyvrel.mag.core.factory.shearreinforcement.ShearReinforcementFactory;
import tyvrel.mag.core.model.*;
import tyvrel.mag.core.model.Shape;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.core.model.classification.Steel;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;
import tyvrel.mag.gui.component.LabeledTextField;
import tyvrel.mag.gui.view.*;

import javax.swing.*;

import java.awt.*;

import static tyvrel.mag.core.exception.Precondition.nonNeg;
import static tyvrel.mag.core.exception.Precondition.notNull;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class ShearReinforcementCard extends AbstractCard {
	private BaseClassificationPanel<ConcreteClassification> concreteClassificationPanel;
	private SteelPanel longitudinalReinforcementSteelPanel;
	private SteelPanel shearReinforcementSteelPanel;
	private ShapePanel shapePanel;
	private FactorsPanel factorsPanel;
	private ReinforcementDiameterPanel asaDiameterPanel;
	private ReinforcementDiameterPanel asbDiameterPanel;
	private ReinforcementDiameterPanel asswDiameterPanel;
	private LabeledTextField ltfCnom;
	private LabeledTextField ltfNb;
	private LabeledTextField ltfVed;

	public ShearReinforcementCard(String name) {
		super(name);
	}

	@Override
	protected void modifyDataPanel(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		concreteClassificationPanel =
				new BaseClassificationPanel<>("Beton", new ConcreteClassificationFactory(), false);
		JPanel steelPanel = new BaseJPanel("Stal");
		steelPanel.setLayout(new GridLayout(1, 2));
		longitudinalReinforcementSteelPanel = new SteelPanel(new SteelFactory(), "Stal zbrojenia na ścinanie");
		shearReinforcementSteelPanel = new SteelPanel(new SteelFactory());
		steelPanel.add(longitudinalReinforcementSteelPanel);
		steelPanel.add(shearReinforcementSteelPanel);
		shapePanel = new ShapePanel();
		factorsPanel = new FactorsPanel();
		asaDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia ściskanego", false);
		asbDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia rozciąganego", false);
		asswDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia na ścinanie", false);
		ltfNb = new LabeledTextField("Liczba prętów rozciąganych [-]: ", "5");
		ltfCnom = new LabeledTextField("Otulina [mm]: ", "30");
		ltfVed = new LabeledTextField("Obliczeniowa siła ścinająca [kN]: ", "50");

		JPanel pProps = new BaseJPanel("Inne");

		pProps.add(ltfNb);
		pProps.add(ltfCnom);
		pProps.add(ltfVed);

		jPanel.add(concreteClassificationPanel);
		jPanel.add(steelPanel);
		jPanel.add(shapePanel);
		jPanel.add(factorsPanel);
		jPanel.add(asaDiameterPanel);
		jPanel.add(asbDiameterPanel);
		jPanel.add(asswDiameterPanel);
		jPanel.add(pProps);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		ShearReinforcement asw = calculateShearReinforcement();
		if (asw.getN() - 1 == 0) return "Zbrojenie na ścinanie nie jest potrzebne";
		return "Zbrojenie na ścinanie: " +
				String.format("%.0f", asw.getNleg()) + "-cięte \u00D8" + String.format("%.0f", asw.getPhi() * 1000)
				+ " co " + String.format("%.0f", 100.0 / (asw.getN() - 1)) + "cm";
	}

	protected ShearReinforcement calculateShearReinforcement() throws ImproperDataException, LSException {
		return notNull(() -> new ShearReinforcementFactory(1, getCrossSection(), getLoad(), getFactors()).build());
	}

	protected Load getLoad() throws ImproperDataException {
		return new Load(0, 0, 0, 0, 0, 0, ltfVed.getDouble() * 1000);
	}

	protected Factors getFactors() throws ImproperDataException {
		return factorsPanel.getFactors();
	}

	protected double getCnom() throws ImproperDataException, LSException {
		return pos(() -> ltfCnom.getDouble() / 1000);
	}

	protected Shape getShape() throws ImproperDataException, LSException {
		return notNull(() -> shapePanel.getShape());
	}

	protected double getNb() throws ImproperDataException, LSException {
		return nonNeg(() -> ltfNb.getDouble());
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
				getShape(), getLongitudinalReinforcementSteel(), getShearReinforcementSteel(),
				getConcreteClassification(), CrossSectionType.BEAM,
				new LongitudinalReinforcement(
						new Reinforcement(getNb(), getPhib(), 0, 0),
						new Reinforcement(0, getPhia(), 0, 0)
				),
				new ShearReinforcement(0, getPhisw(), 0, 0),
				getCnom()
		);
	}

	protected ConcreteClassification getConcreteClassification() throws ImproperDataException, LSException {
		return notNull(() -> concreteClassificationPanel.getClassifications(ConcreteClassification[]::new)[0]);
	}

	protected Steel getLongitudinalReinforcementSteel() throws ImproperDataException, LSException {
		return notNull(() -> longitudinalReinforcementSteelPanel.getSteel());
	}

	protected Steel getShearReinforcementSteel() throws ImproperDataException, LSException {
		return notNull(() -> shearReinforcementSteelPanel.getSteel());
	}
}
