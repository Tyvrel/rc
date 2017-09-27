package tyvrel.mag.gui.component.datahandler;

import tyvrel.mag.gui.component.ApplicationFrame;

import static tyvrel.mag.core.exception.Precondition.notNull;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ReinforcementDiametersHandler extends AbstractDataHandler {
	public ReinforcementDiametersHandler(ApplicationFrame applicationFrame) {
		super(applicationFrame);
	}

	public Double[] getFias() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące zbrojenia są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> notNull(applicationFrame.getMaterialsPanel().getAsaDiameters()));
	}

	public Double[] getFibs() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące zbrojenia są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> notNull(applicationFrame.getMaterialsPanel().getAsbDiameters()));
	}

	public Double[] getFisws() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące zbrojenia są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> notNull(applicationFrame.getMaterialsPanel().getAswDiameters()));
	}
}
