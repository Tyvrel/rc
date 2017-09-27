package tyvrel.mag.gui.component.datahandler;

import tyvrel.mag.core.model.Load;
import tyvrel.mag.gui.component.ApplicationFrame;

import static tyvrel.mag.core.exception.Precondition.notEmpty;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class LoadsHandler extends AbstractDataHandler {

	public LoadsHandler(ApplicationFrame applicationFrame) {
		super(applicationFrame);
	}

	public Load[] get() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące obciążeń są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> notEmpty(applicationFrame.getLoadsPanel()
				.getLoads()));
	}
}
