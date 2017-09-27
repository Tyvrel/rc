package tyvrel.mag.gui.view;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.core.model.Factors;
import tyvrel.mag.gui.component.LabeledTextField;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class FactorsPanel extends BaseJPanel {
	private LabeledTextField ltfGammaC;
	private LabeledTextField ltfGammaS;

	public FactorsPanel() {
		super("Współczynniki");
		ltfGammaC = new LabeledTextField("<html>\u03B3<sub>C</sub> [-]:</html>", "1.4");
		add(ltfGammaC);
		ltfGammaS = new LabeledTextField("<html>\u03B3<sub>S</sub> [-]:</html>", "1.15");
		add(ltfGammaS);
	}

	public Factors getFactors() throws ImproperDataException {
		return new Factors(
				ltfGammaS.getDouble(),
				ltfGammaC.getDouble()
		);
	}
}
