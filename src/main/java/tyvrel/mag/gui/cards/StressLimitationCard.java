package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.classification.ExposureClassificationFactory;
import tyvrel.mag.core.factory.stress.StressLimitationFactory;
import tyvrel.mag.core.model.Stress;
import tyvrel.mag.core.model.classification.ExposureClassification;
import tyvrel.mag.gui.view.BaseClassificationPanel;

import javax.swing.*;

import static tyvrel.mag.core.exception.Precondition.notNull;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class StressLimitationCard extends StressCard {
	private BaseClassificationPanel<ExposureClassification> exposureClassificationPanel;

	public StressLimitationCard(String name) {
		super(name);
	}

	@Override
	protected void modifyDataPanel(JPanel jPanel) {
		exposureClassificationPanel =
				new BaseClassificationPanel<>("Klasy ekspozycji", new ExposureClassificationFactory());
		super.modifyDataPanel(jPanel);
		jPanel.add(exposureClassificationPanel);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		return (calculateStressLimitation()) ? "Warunek ograniczenia naprężeń jest spełniony" :
				"Warunek ograniczenia naprężeń NIE jest spełniony";
	}

	protected Stress getStress() throws ImproperDataException, LSException {
		return notNull(calculateStress());
	}

	protected boolean calculateStressLimitation() throws ImproperDataException, LSException {
		return notNull(() -> new StressLimitationFactory(getStress(), getCrossSection(), getExposureClassification())
				.build());
	}

	protected ExposureClassification[] getExposureClassification() {
		return exposureClassificationPanel.getClassifications(ExposureClassification[]::new);
	}
}
