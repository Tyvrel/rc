package tyvrel.mag.core.exception;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 */
public class ImproperDataException extends Exception {

	public ImproperDataException() {
		super();
	}

	public ImproperDataException(String message) {
		super(message);
	}

	public ImproperDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImproperDataException(Throwable cause) {
		super(cause);
	}

	protected ImproperDataException(String message, Throwable cause,
	                                boolean enableSuppression,
	                                boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


}
