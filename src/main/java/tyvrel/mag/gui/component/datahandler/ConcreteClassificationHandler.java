package tyvrel.mag.gui.component.datahandler;


import tyvrel.mag.core.model.classification.ConcreteClassification;
import tyvrel.mag.gui.component.ApplicationFrame;

import static tyvrel.mag.core.exception.Precondition.notEmpty;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ConcreteClassificationHandler extends AbstractDataHandler {
	public ConcreteClassificationHandler(ApplicationFrame applicationFrame) {
		super(applicationFrame);
	}

	public ConcreteClassification[] get() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące klas betonu są nieprawidłowe lub niepełne. " +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości</html>";
		return get(message, applicationFrame -> notEmpty(applicationFrame.getMaterialsPanel()
				.getConcreteClassifications()));
	}

}
