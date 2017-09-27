package tyvrel.mag.gui.view;

import tyvrel.mag.core.model.classification.ExposureClassification;
import tyvrel.mag.core.factory.classification.ExposureClassificationFactory;

import java.awt.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class OthersPanel extends BaseJPanel {
	private BaseClassificationPanel<ExposureClassification> exposureClassPanel;
	private ConcreteCoverPanel concreteCoverPanel;
	private ShapeRangePanel shapeRangePanel;

	public OthersPanel() {
		super("Inne");
		setLayout(new GridLayout(0, 1));

		shapeRangePanel = new ShapeRangePanel();
		exposureClassPanel = new BaseClassificationPanel<>("Klasy ekspozycji", new ExposureClassificationFactory());
		concreteCoverPanel = new ConcreteCoverPanel();

		add(shapeRangePanel);
		add(exposureClassPanel);
		add(concreteCoverPanel);
	}

	public ShapeRangePanel getShapeRangePanel() {
		return shapeRangePanel;
	}

	public ExposureClassification[] getExposureClassifications() {
		return exposureClassPanel.getClassifications(ExposureClassification[]::new);
	}

	public boolean is100yWorkingLife() {
		return concreteCoverPanel.is100yWorkingLife();
	}

	public boolean isQualityEnsured() {
		return concreteCoverPanel.isQualityEnsured();
	}

	public boolean isHighAir() {
		return concreteCoverPanel.isHighAir();
	}

	public double getDgNomMax() {
		return concreteCoverPanel.getDgNomMax();
	}
}
