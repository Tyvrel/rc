package tyvrel.mag.gui.component.datahandler;

import tyvrel.mag.gui.component.ApplicationFrame;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ConcreteCoverDataHandler extends AbstractDataHandler {
	public ConcreteCoverDataHandler(ApplicationFrame applicationFrame) {
		super(applicationFrame);
	}

	public boolean is100yWorkingLife() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące otuliny są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> applicationFrame.getOthersPanel().is100yWorkingLife());
	}

	public boolean isQualityEnsured() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące otuliny są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> applicationFrame.getOthersPanel().isQualityEnsured());
	}

	public boolean isHighAir() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące otuliny są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> applicationFrame.getOthersPanel().isHighAir());
	}

	public double getDgNomMax() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące otuliny są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> applicationFrame.getOthersPanel().getDgNomMax());
	}
}
