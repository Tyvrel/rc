package tyvrel.mag.gui.component.datahandler;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class DataHandlerException extends Exception {
	public DataHandlerException() {
		super();
	}

	public DataHandlerException(String message) {
		super(message);
	}

	public DataHandlerException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataHandlerException(Throwable cause) {
		super(cause);
	}

	protected DataHandlerException(String message, Throwable cause,
	                               boolean enableSuppression,
	                               boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
