package tyvrel.mag.gui.cards;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.exception.LSException;
import tyvrel.mag.core.factory.stress.CrackedStressFactory;
import tyvrel.mag.core.factory.stress.StressFactory;
import tyvrel.mag.core.factory.stress.UncrackedStressFactory;
import tyvrel.mag.core.model.Stress;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
@SuppressWarnings("WeakerAccess")
public class StressCard extends CrackedStressCard {

	public StressCard(String name) {
		super(name);
	}

	@Override
	protected String getSuccessMessage() throws Exception {
		Stress stress = calculateStress();
		return "<html>Maksymalne naprężenia w betonie wynoszą: " + String.format("%.3f", stress.getSigmac() / 1000000)
				+ "MPa"
				+ "<br>Maksymalne naprężenia w stali wynoszą: " + String.format("%.3f", stress.getSigmas() / 1000000)
				+ "MPa</html>";
	}

	protected Stress getCrackedStress() throws ImproperDataException, LSException {
		return new CrackedStressFactory(getCrossSection(), getMa(), getMb(), getPhi()).build();
	}

	protected Stress getUncrackedStress() throws ImproperDataException, LSException {
		return new UncrackedStressFactory(getCrossSection(), getMa(), getMb()).build();
	}

	protected Stress calculateStress() throws ImproperDataException, LSException {
		return new StressFactory(getConcreteClassification(), getUncrackedStress(), getCrackedStress()).build();
	}
}
