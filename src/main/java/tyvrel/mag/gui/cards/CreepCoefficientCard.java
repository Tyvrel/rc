package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.classification.ConcreteClassificationFactory;
import tyvrel.mag.core.factory.classification.SteelFactory;
import tyvrel.mag.core.factory.stress.CreepCoefficientFactory;
import tyvrel.mag.core.factory.stress.LinearCreepCoefficientFactory;
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

import static tyvrel.mag.core.exception.Precondition.nonNeg;
import static tyvrel.mag.core.exception.Precondition.notNull;
import static tyvrel.mag.core.exception.Precondition.pos;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class CreepCoefficientCard extends AbstractCard {
	private BaseClassificationPanel<ConcreteClassification> concreteClassificationPanel;
	private SteelPanel steelPanel;
	private ShapePanel shapePanel;
	private CementClassificationPanel cementClassificationPanel;
	private LabeledTextField ltfMa;
	private LabeledTextField ltfMb;
	private ReinforcementDiameterPanel asaDiameterPanel;
	private ReinforcementDiameterPanel asbDiameterPanel;
	private ReinforcementDiameterPanel asswDiameterPanel;
	private LabeledTextField ltfT0;
	private LabeledTextField ltfT;
	private LabeledTextField ltfRh;
	private LabeledTextField ltfNa;
	private LabeledTextField ltfNb;
	private LabeledTextField ltfCnom;

	public CreepCoefficientCard(String name) {
		super(name);
	}

	@Override
	protected void modifyDataPanel(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		concreteClassificationPanel =
				new BaseClassificationPanel<>("Beton", new ConcreteClassificationFactory(), false);
		steelPanel = new SteelPanel(new SteelFactory());
		shapePanel = new ShapePanel();
		cementClassificationPanel = new CementClassificationPanel();
		ltfMa = new LabeledTextField("Moment zginający rozciągajacy górę przekroju  kombinacji quasi-stałej [kNm]: ",
				"100");
		ltfMb = new LabeledTextField("Moment zginający rozciągajacy dół przekroju  kombinacji quasi-stałej [kNm]: ",
				"100");
		asaDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia górą", false);
		asbDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia dołem", false);
		asswDiameterPanel = new ReinforcementDiameterPanel("Średnica zbrojenia na ścinanie", false);
		ltfNa = new LabeledTextField("Liczba prętów górą [-]: ", "5");
		ltfNb = new LabeledTextField("Liczba prętów dołem [-]: ", "5");
		ltfT0 = new LabeledTextField("Wiek betonu w chwili obciążenia [dni]: ", "28");
		ltfT = new LabeledTextField("Wiek betonu [dni]: ", "18250");
		ltfRh = new LabeledTextField("Wilgotność powietrza [-]: ", "0.5");
		ltfCnom = new LabeledTextField("Otulina [mm]: ", "30");

		JPanel pLoad = new BaseJPanel("Obciążenie");
		pLoad.add(ltfMa);
		pLoad.add(ltfMb);

		JPanel pProps = new BaseJPanel("Inne");
		pProps.add(ltfNa);
		pProps.add(ltfNb);
		pProps.add(ltfT0);
		pProps.add(ltfT);
		pProps.add(ltfRh);
		pProps.add(ltfCnom);

		jPanel.add(concreteClassificationPanel);
		jPanel.add(steelPanel);
		jPanel.add(cementClassificationPanel);
		jPanel.add(shapePanel);
		jPanel.add(pLoad);
		jPanel.add(asaDiameterPanel);
		jPanel.add(asbDiameterPanel);
		jPanel.add(asswDiameterPanel);
		jPanel.add(pProps);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		return "Obliczony współczynnik pełzania wynosi: " + String.format("%.3f", calculatePhi());
	}

	private double calculatePhi() throws ImproperDataException, LSException {
		return pos(() -> new CreepCoefficientFactory(getPhiln(), getCrossSection(), getMaquasiPerm(), getMbquasiPerm()
				, getT0(), getCementClassification()).build());

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

	protected int getCementClassification() {
		return cementClassificationPanel.getCementClassification();
	}

	protected Steel getSteel() throws ImproperDataException, LSException {
		return notNull(() -> steelPanel.getSteel());
	}

	protected Shape getShape() throws ImproperDataException, LSException {
		return notNull(() -> shapePanel.getShape());
	}

	protected double getNa() throws ImproperDataException, LSException {
		return pos(() -> ltfNa.getDouble());
	}

	protected double getNb() throws ImproperDataException, LSException {
		return pos(() -> ltfNb.getDouble());
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

	protected double getT() throws ImproperDataException, LSException {
		return notNull(() -> ltfT.getDouble());
	}

	protected double getT0() throws ImproperDataException, LSException {
		return notNull(() -> ltfT0.getDouble());
	}

	protected double getRh() throws ImproperDataException, LSException {
		return notNull(() -> ltfRh.getDouble());
	}

	protected double getMaquasiPerm() throws ImproperDataException, LSException {
		return nonNeg(() -> ltfMa.getDouble() * 1000);
	}

	protected double getMbquasiPerm() throws ImproperDataException, LSException {
		return nonNeg(() -> ltfMb.getDouble() * 1000);
	}

	protected double getCnom() throws ImproperDataException, LSException {
		return pos(() -> ltfCnom.getDouble() / 1000);
	}

	protected double getPhiln() throws ImproperDataException, LSException {
		return new LinearCreepCoefficientFactory(getConcreteClassification(), getRh(), getT0(), getT(), getShape(),
				getCementClassification()).build();
	}
}
