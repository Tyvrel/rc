package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.stress.LinearCreepCoefficientFactory;
import tyvrel.mag.core.factory.classification.ConcreteClassificationFactory;
import tyvrel.mag.core.model.Shape;
import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.gui.component.LabeledTextField;
import tyvrel.mag.gui.view.BaseClassificationPanel;
import tyvrel.mag.gui.view.BaseJPanel;
import tyvrel.mag.gui.view.CementClassificationPanel;
import tyvrel.mag.gui.view.ShapePanel;

import javax.swing.*;

import static tyvrel.mag.core.exception.Precondition.notNull;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class LinearCreepCoefficientCard extends AbstractCard {
	private BaseClassificationPanel<ConcreteClassification> concreteClassificationPanel;
	private ShapePanel shapePanel;
	private CementClassificationPanel cementClassificationPanel;
	private LabeledTextField ltfT0;
	private LabeledTextField ltfT;
	private LabeledTextField ltfRh;


	public LinearCreepCoefficientCard(String name) {
		super(name);
	}

	@Override
	protected void modifyDataPanel(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		concreteClassificationPanel =
				new BaseClassificationPanel<>("Beton", new ConcreteClassificationFactory(), false);
		shapePanel = new ShapePanel();
		cementClassificationPanel = new CementClassificationPanel();
		ltfT0 = new LabeledTextField("Wiek betonu w chwili obciążenia [dni]: ", "28");
		ltfT = new LabeledTextField("Wiek betonu [dni]: ", "18250");
		ltfRh = new LabeledTextField("Wilgotność powietrza [-]: ", "0.5");


		JPanel pProps = new BaseJPanel("Inne");
		pProps.add(ltfT0);
		pProps.add(ltfT);
		pProps.add(ltfRh);

		jPanel.add(concreteClassificationPanel);
		jPanel.add(cementClassificationPanel);
		jPanel.add(shapePanel);
		jPanel.add(pProps);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		return "Obliczony liniowy współczynnik pełzania wynosi: " + String.format("%.3f", calculatePhiln());
	}

	protected ConcreteClassification getConcreteClassification() throws ImproperDataException, LSException {
		return notNull(() -> concreteClassificationPanel.getClassifications(ConcreteClassification[]::new)[0]);
	}

	protected int getCementClassification() {
		return cementClassificationPanel.getCementClassification();
	}

	protected Shape getShape() throws ImproperDataException, LSException {
		return notNull(() -> shapePanel.getShape());
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


	protected double calculatePhiln() throws ImproperDataException, LSException {
		return new LinearCreepCoefficientFactory(getConcreteClassification(), getRh(), getT0(), getT(), getShape(),
				getCementClassification()).build();
	}


}
