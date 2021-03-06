package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.classification.ConcreteClassificationFactory;
import tyvrel.mag.core.factory.classification.ExposureClassificationFactory;
import tyvrel.mag.core.factory.classification.SteelFactory;
import tyvrel.mag.core.factory.longitudinalreinforcement.MinimumCrackLongitudinalReinforcementFactory;
import tyvrel.mag.core.model.CrossSection;
import tyvrel.mag.core.model.CrossSectionType;
import tyvrel.mag.core.model.Shape;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.core.model.classification.ExposureClassification;
import tyvrel.mag.core.model.classification.Steel;
import tyvrel.mag.core.model.reinforcement.LongitudinalReinforcement;
import tyvrel.mag.core.model.reinforcement.Reinforcement;
import tyvrel.mag.core.model.reinforcement.ShearReinforcement;
import tyvrel.mag.gui.component.LabeledTextField;
import tyvrel.mag.gui.view.*;

import javax.swing.*;

import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class MinimumCrackLongitudinalReinforcementCard extends AbstractCard {
	private BaseClassificationPanel<ConcreteClassification> concreteClassificationPanel;
	private BaseClassificationPanel<ExposureClassification> exposureClassificationPanel;
	private CementClassificationPanel cementClassificationPanel;
	private SteelPanel steelPanel;
	private ShapePanel shapePanel;
	private ReinforcementDiameterPanel asaDiameterPanel;
	private ReinforcementDiameterPanel asbDiameterPanel;
	private ReinforcementDiameterPanel asswDiameterPanel;
	private LabeledTextField ltfCnom;
	private LabeledTextField ltfNa;
	private LabeledTextField ltfNb;
	private LabeledTextField ltfT;

	public MinimumCrackLongitudinalReinforcementCard(String name) {
		super(name);
	}

	@Override
	protected void modifyDataPanel(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		concreteClassificationPanel =
				new BaseClassificationPanel<>("Beton", new ConcreteClassificationFactory(), false);
		exposureClassificationPanel = new
				BaseClassificationPanel<>("Klasy ekspozycji", new ExposureClassificationFactory(), true);
		cementClassificationPanel = new CementClassificationPanel();
		steelPanel = new SteelPanel(new SteelFactory());
		shapePanel = new ShapePanel();
		asaDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia górą", false);
		asbDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia dołem", false);
		asswDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia na ścinanie", false);
		ltfNa = new LabeledTextField("Liczba prętów górą [-]: ", "5");
		ltfNb = new LabeledTextField("Liczba prętów dołem [-]: ", "5");
		ltfCnom = new LabeledTextField("Otulina [mm]: ", "30");
		ltfT = new LabeledTextField("Wiek betonu [dni]: ", "28");

		JPanel pProps = new BaseJPanel("Inne");
		pProps.add(ltfNa);
		pProps.add(ltfNb);
		pProps.add(ltfCnom);
		pProps.add(ltfT);

		jPanel.add(concreteClassificationPanel);
		jPanel.add(cementClassificationPanel);
		jPanel.add(exposureClassificationPanel);
		jPanel.add(steelPanel);
		jPanel.add(shapePanel);
		jPanel.add(asaDiameterPanel);
		jPanel.add(asbDiameterPanel);
		jPanel.add(asswDiameterPanel);
		jPanel.add(pProps);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		LongitudinalReinforcement longitudinalReinforcement = calculateMinimumCrackLongitudinalReinforcement();
		Reinforcement asa = longitudinalReinforcement.getAsa();
		Reinforcement asb = longitudinalReinforcement.getAsb();
		return "<html>Minimalne zbrojenie górą: " +
				String.format("%.0f", asa.getN()) + "\u00D8" + String.format("%.0f", asa.getPhi() * 1000)
				+ "<br>Minimalne zbrojenie dołem: " +
				String.format("%.0f", asb.getN()) + "\u00D8" + String.format("%.0f", asb.getPhi() * 1000) + "</html>";
	}

	protected LongitudinalReinforcement calculateMinimumCrackLongitudinalReinforcement() throws ImproperDataException,
			LSException {
		return new MinimumCrackLongitudinalReinforcementFactory(getCrossSection(), getT(), getCementClassification(),
				getExposureClassifications())
				.build();
	}

	protected int getCementClassification() {
		return cementClassificationPanel.getCementClassification();
	}

	protected ExposureClassification[] getExposureClassifications() {
		return exposureClassificationPanel.getClassifications(ExposureClassification[]::new);
	}

	protected double getCnom() throws ImproperDataException, LSException {
		return pos(() -> ltfCnom.getDouble() / 1000);
	}

	protected double getT() throws ImproperDataException, LSException {
		return notNull(() -> ltfT.getDouble());
	}

	protected Shape getShape() throws ImproperDataException, LSException {
		return notNull(() -> shapePanel.getShape());
	}

	protected double getNa() throws ImproperDataException, LSException {
		return nonNeg(() -> ltfNa.getDouble());
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
				getShape(), getSteel(), null, getConcreteClassification(), CrossSectionType.BEAM,
				new LongitudinalReinforcement(
						new Reinforcement(getNb(), getPhib(), 0, 0),
						new Reinforcement(getNa(), getPhia(), 0, 0)
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
