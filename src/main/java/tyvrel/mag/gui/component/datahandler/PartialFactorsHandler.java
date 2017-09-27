package tyvrel.mag.gui.component.datahandler;

import tyvrel.mag.gui.component.ApplicationFrame;
import tyvrel.mag.core.model.Factors;

import static tyvrel.mag.core.exception.Precondition.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class PartialFactorsHandler extends AbstractDataHandler {

	public PartialFactorsHandler(ApplicationFrame applicationFrame) {
		super(applicationFrame);
	}

	public Factors get() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące współczynników częściowych są nieprawidłowe lub " +
				"niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> notNull(applicationFrame.getLoadsPanel()
				.getPartialFactors()));
	}
}
