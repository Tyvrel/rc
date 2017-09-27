package tyvrel.mag.gui.component.datahandler;

import tyvrel.mag.gui.component.ApplicationFrame;
import tyvrel.mag.core.model.Range;

import static tyvrel.mag.core.exception.Precondition.notNull;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ShapeHandler extends AbstractDataHandler {
	public ShapeHandler(ApplicationFrame applicationFrame) {
		super(applicationFrame);
	}

	public Range getWidthRange() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące geometrii są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> notNull(applicationFrame.getShapeRangePanel().getWidthRange()));

	}

	public Range getHeightRange() throws DataHandlerException {
		String message = "<html><center>Wprowadzone dane dotyczące geometrii są nieprawidłowe lub niepełne." +
				"<br>Proszę sprawdzić, czy konieczne pola nie są puste lub nie zawierają nieprawidłowych " +
				"wartości.</html>";
		return get(message, applicationFrame -> notNull(applicationFrame.getShapeRangePanel().getHeightRange()));
	}
}
