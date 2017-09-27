package tyvrel.mag.gui.component.datahandler;

import tyvrel.mag.core.exception.ImproperDataException;
import tyvrel.mag.gui.component.ApplicationFrame;

import javax.swing.*;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public abstract class AbstractDataHandler {
	private ApplicationFrame applicationFrame;

	protected AbstractDataHandler(ApplicationFrame applicationFrame) {
		this.applicationFrame = applicationFrame;
	}

	public <R> R get(String message, ThrowableFunction<R> function) throws DataHandlerException {
		try {
			return function.apply(applicationFrame);
		} catch (ImproperDataException e) {
			JOptionPane.showMessageDialog(applicationFrame, message, "Błąd", JOptionPane.WARNING_MESSAGE);
			throw new DataHandlerException();
		}
	}

	public interface ThrowableFunction<R> {
		R apply(ApplicationFrame applicationFrame) throws ImproperDataException;
	}
}
