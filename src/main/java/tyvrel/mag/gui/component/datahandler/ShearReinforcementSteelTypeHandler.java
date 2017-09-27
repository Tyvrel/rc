package tyvrel.mag.gui.component.datahandler;

import tyvrel.mag.gui.component.ApplicationFrame;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ShearReinforcementSteelTypeHandler extends AbstractDataHandler {
	public ShearReinforcementSteelTypeHandler(ApplicationFrame applicationFrame) {
		super(applicationFrame);
	}

	public String get() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące stali są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> applicationFrame.getMaterialsPanel()
				.getLongitudinalReinforcementSteelType());
	}
}
