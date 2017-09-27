package tyvrel.mag.core.exception;

/**
 * This work is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by/4.0/.
 * <p>
 * Thrown to indicate that ULS has been exceeded.
 */
public class LSException extends Exception {
	private static final long serialVersionUID = -6679164095239690126L;

	public LSException() {
		super();
	}

	public LSException(String message) {
		super(message);
	}

	public LSException(String message, Throwable cause) {
		super(message, cause);
	}

	public LSException(Throwable cause) {
		super(cause);
	}
}
